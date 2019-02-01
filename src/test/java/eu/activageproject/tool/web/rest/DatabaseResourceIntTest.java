package eu.activageproject.tool.web.rest;

import eu.activageproject.tool.DataLakeToolApp;

import eu.activageproject.tool.domain.Database;
import eu.activageproject.tool.repository.DatabaseRepository;
import eu.activageproject.tool.service.DatabaseService;
import eu.activageproject.tool.repository.search.DatabaseSearchRepository;
import eu.activageproject.tool.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static eu.activageproject.tool.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DatabaseResource REST controller.
 *
 * @see DatabaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLakeToolApp.class)
public class DatabaseResourceIntTest {

    private static final String DEFAULT_DB = "AAAAAAAAAA";
    private static final String UPDATED_DB = "BBBBBBBBBB";

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private DatabaseSearchRepository databaseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restDatabaseMockMvc;

    private Database database;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DatabaseResource databaseResource = new DatabaseResource(databaseService);
        this.restDatabaseMockMvc = MockMvcBuilders.standaloneSetup(databaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Database createEntity() {
        Database database = new Database()
            .db(DEFAULT_DB);
        return database;
    }

    @Before
    public void initTest() {
        databaseRepository.deleteAll();
        databaseSearchRepository.deleteAll();
        database = createEntity();
    }

    @Test
    public void createDatabase() throws Exception {
        int databaseSizeBeforeCreate = databaseRepository.findAll().size();

        // Create the Database
        restDatabaseMockMvc.perform(post("/api/databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(database)))
            .andExpect(status().isCreated());

        // Validate the Database in the database
        List<Database> databaseList = databaseRepository.findAll();
        assertThat(databaseList).hasSize(databaseSizeBeforeCreate + 1);
        Database testDatabase = databaseList.get(databaseList.size() - 1);
        assertThat(testDatabase.getDb()).isEqualTo(DEFAULT_DB);

        // Validate the Database in Elasticsearch
        Database databaseEs = databaseSearchRepository.findOne(testDatabase.getId());
        assertThat(databaseEs).isEqualToIgnoringGivenFields(testDatabase);
    }

    @Test
    public void createDatabaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = databaseRepository.findAll().size();

        // Create the Database with an existing ID
        database.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restDatabaseMockMvc.perform(post("/api/databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(database)))
            .andExpect(status().isBadRequest());

        // Validate the Database in the database
        List<Database> databaseList = databaseRepository.findAll();
        assertThat(databaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkDbIsRequired() throws Exception {
        int databaseSizeBeforeTest = databaseRepository.findAll().size();
        // set the field null
        database.setDb(null);

        // Create the Database, which fails.

        restDatabaseMockMvc.perform(post("/api/databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(database)))
            .andExpect(status().isBadRequest());

        List<Database> databaseList = databaseRepository.findAll();
        assertThat(databaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllDatabases() throws Exception {
        // Initialize the database
        databaseRepository.save(database);

        // Get all the databaseList
        restDatabaseMockMvc.perform(get("/api/databases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(database.getId())))
            .andExpect(jsonPath("$.[*].db").value(hasItem(DEFAULT_DB.toString())));
    }

    @Test
    public void getDatabase() throws Exception {
        // Initialize the database
        databaseRepository.save(database);

        // Get the database
        restDatabaseMockMvc.perform(get("/api/databases/{id}", database.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(database.getId()))
            .andExpect(jsonPath("$.db").value(DEFAULT_DB.toString()));
    }

    @Test
    public void getNonExistingDatabase() throws Exception {
        // Get the database
        restDatabaseMockMvc.perform(get("/api/databases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateDatabase() throws Exception {
        // Initialize the database
        databaseService.save(database);

        int databaseSizeBeforeUpdate = databaseRepository.findAll().size();

        // Update the database
        Database updatedDatabase = databaseRepository.findOne(database.getId());
        updatedDatabase
            .db(UPDATED_DB);

        restDatabaseMockMvc.perform(put("/api/databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDatabase)))
            .andExpect(status().isOk());

        // Validate the Database in the database
        List<Database> databaseList = databaseRepository.findAll();
        assertThat(databaseList).hasSize(databaseSizeBeforeUpdate);
        Database testDatabase = databaseList.get(databaseList.size() - 1);
        assertThat(testDatabase.getDb()).isEqualTo(UPDATED_DB);

        // Validate the Database in Elasticsearch
        Database databaseEs = databaseSearchRepository.findOne(testDatabase.getId());
        assertThat(databaseEs).isEqualToIgnoringGivenFields(testDatabase);
    }

    @Test
    public void updateNonExistingDatabase() throws Exception {
        int databaseSizeBeforeUpdate = databaseRepository.findAll().size();

        // Create the Database

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDatabaseMockMvc.perform(put("/api/databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(database)))
            .andExpect(status().isCreated());

        // Validate the Database in the database
        List<Database> databaseList = databaseRepository.findAll();
        assertThat(databaseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteDatabase() throws Exception {
        // Initialize the database
        databaseService.save(database);

        int databaseSizeBeforeDelete = databaseRepository.findAll().size();

        // Get the database
        restDatabaseMockMvc.perform(delete("/api/databases/{id}", database.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean databaseExistsInEs = databaseSearchRepository.exists(database.getId());
        assertThat(databaseExistsInEs).isFalse();

        // Validate the database is empty
        List<Database> databaseList = databaseRepository.findAll();
        assertThat(databaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchDatabase() throws Exception {
        // Initialize the database
        databaseService.save(database);

        // Search the database
        restDatabaseMockMvc.perform(get("/api/_search/databases?query=id:" + database.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(database.getId())))
            .andExpect(jsonPath("$.[*].db").value(hasItem(DEFAULT_DB.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Database.class);
        Database database1 = new Database();
        database1.setId("id1");
        Database database2 = new Database();
        database2.setId(database1.getId());
        assertThat(database1).isEqualTo(database2);
        database2.setId("id2");
        assertThat(database1).isNotEqualTo(database2);
        database1.setId(null);
        assertThat(database1).isNotEqualTo(database2);
    }
}
