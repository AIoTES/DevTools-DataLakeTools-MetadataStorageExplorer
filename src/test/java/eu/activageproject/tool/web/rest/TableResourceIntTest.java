package eu.activageproject.tool.web.rest;

import eu.activageproject.tool.DataLakeToolApp;

import eu.activageproject.tool.domain.Table;
import eu.activageproject.tool.repository.TableRepository;
import eu.activageproject.tool.service.TableService;
import eu.activageproject.tool.repository.search.TableSearchRepository;
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
 * Test class for the TableResource REST controller.
 *
 * @see TableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLakeToolApp.class)
public class TableResourceIntTest {

    private static final String DEFAULT_DB = "AAAAAAAAAA";
    private static final String UPDATED_DB = "BBBBBBBBBB";

    private static final String DEFAULT_TABLE = "AAAAAAAAAA";
    private static final String UPDATED_TABLE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_QUERY = "BBBBBBBBBB";

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableSearchRepository tableSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restTableMockMvc;

    private Table table;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TableResource tableResource = new TableResource(tableService);
        this.restTableMockMvc = MockMvcBuilders.standaloneSetup(tableResource)
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
    public static Table createEntity() {
        Table table = new Table()
            .db(DEFAULT_DB)
            .table(DEFAULT_TABLE)
            .data(DEFAULT_DATA)
            .query(DEFAULT_QUERY);
        return table;
    }

    @Before
    public void initTest() {
        tableRepository.deleteAll();
        tableSearchRepository.deleteAll();
        table = createEntity();
    }

    @Test
    public void createTable() throws Exception {
        int databaseSizeBeforeCreate = tableRepository.findAll().size();

        // Create the Table
        restTableMockMvc.perform(post("/api/tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(table)))
            .andExpect(status().isCreated());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeCreate + 1);
        Table testTable = tableList.get(tableList.size() - 1);
        assertThat(testTable.getDb()).isEqualTo(DEFAULT_DB);
        assertThat(testTable.getTable()).isEqualTo(DEFAULT_TABLE);
        assertThat(testTable.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testTable.getQuery()).isEqualTo(DEFAULT_QUERY);

        // Validate the Table in Elasticsearch
        Table tableEs = tableSearchRepository.findOne(testTable.getId());
        assertThat(tableEs).isEqualToIgnoringGivenFields(testTable);
    }

    @Test
    public void createTableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tableRepository.findAll().size();

        // Create the Table with an existing ID
        table.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restTableMockMvc.perform(post("/api/tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(table)))
            .andExpect(status().isBadRequest());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkDbIsRequired() throws Exception {
        int databaseSizeBeforeTest = tableRepository.findAll().size();
        // set the field null
        table.setDb(null);

        // Create the Table, which fails.

        restTableMockMvc.perform(post("/api/tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(table)))
            .andExpect(status().isBadRequest());

        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTableIsRequired() throws Exception {
        int databaseSizeBeforeTest = tableRepository.findAll().size();
        // set the field null
        table.setTable(null);

        // Create the Table, which fails.

        restTableMockMvc.perform(post("/api/tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(table)))
            .andExpect(status().isBadRequest());

        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTables() throws Exception {
        // Initialize the database
        tableRepository.save(table);

        // Get all the tableList
        restTableMockMvc.perform(get("/api/tables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(table.getId())))
            .andExpect(jsonPath("$.[*].db").value(hasItem(DEFAULT_DB.toString())))
            .andExpect(jsonPath("$.[*].table").value(hasItem(DEFAULT_TABLE.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY.toString())));
    }

    @Test
    public void getTable() throws Exception {
        // Initialize the database
        tableRepository.save(table);

        // Get the table
        restTableMockMvc.perform(get("/api/tables/{id}", table.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(table.getId()))
            .andExpect(jsonPath("$.db").value(DEFAULT_DB.toString()))
            .andExpect(jsonPath("$.table").value(DEFAULT_TABLE.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.query").value(DEFAULT_QUERY.toString()));
    }

    @Test
    public void getNonExistingTable() throws Exception {
        // Get the table
        restTableMockMvc.perform(get("/api/tables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTable() throws Exception {
        // Initialize the database
        tableService.save(table);

        int databaseSizeBeforeUpdate = tableRepository.findAll().size();

        // Update the table
        Table updatedTable = tableRepository.findOne(table.getId());
        updatedTable
            .db(UPDATED_DB)
            .table(UPDATED_TABLE)
            .data(UPDATED_DATA)
            .query(UPDATED_QUERY);

        restTableMockMvc.perform(put("/api/tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTable)))
            .andExpect(status().isOk());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate);
        Table testTable = tableList.get(tableList.size() - 1);
        assertThat(testTable.getDb()).isEqualTo(UPDATED_DB);
        assertThat(testTable.getTable()).isEqualTo(UPDATED_TABLE);
        assertThat(testTable.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testTable.getQuery()).isEqualTo(UPDATED_QUERY);

        // Validate the Table in Elasticsearch
        Table tableEs = tableSearchRepository.findOne(testTable.getId());
        assertThat(tableEs).isEqualToIgnoringGivenFields(testTable);
    }

    @Test
    public void updateNonExistingTable() throws Exception {
        int databaseSizeBeforeUpdate = tableRepository.findAll().size();

        // Create the Table

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTableMockMvc.perform(put("/api/tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(table)))
            .andExpect(status().isCreated());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteTable() throws Exception {
        // Initialize the database
        tableService.save(table);

        int databaseSizeBeforeDelete = tableRepository.findAll().size();

        // Get the table
        restTableMockMvc.perform(delete("/api/tables/{id}", table.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tableExistsInEs = tableSearchRepository.exists(table.getId());
        assertThat(tableExistsInEs).isFalse();

        // Validate the database is empty
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchTable() throws Exception {
        // Initialize the database
        tableService.save(table);

        // Search the table
        restTableMockMvc.perform(get("/api/_search/tables?query=id:" + table.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(table.getId())))
            .andExpect(jsonPath("$.[*].db").value(hasItem(DEFAULT_DB.toString())))
            .andExpect(jsonPath("$.[*].table").value(hasItem(DEFAULT_TABLE.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Table.class);
        Table table1 = new Table();
        table1.setId("id1");
        Table table2 = new Table();
        table2.setId(table1.getId());
        assertThat(table1).isEqualTo(table2);
        table2.setId("id2");
        assertThat(table1).isNotEqualTo(table2);
        table1.setId(null);
        assertThat(table1).isNotEqualTo(table2);
    }
}
