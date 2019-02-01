package eu.activageproject.tool.web.rest;

import eu.activageproject.tool.DataLakeToolApp;

import eu.activageproject.tool.domain.Schema;
import eu.activageproject.tool.repository.SchemaRepository;
import eu.activageproject.tool.service.SchemaService;
import eu.activageproject.tool.repository.search.SchemaSearchRepository;
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
 * Test class for the SchemaResource REST controller.
 *
 * @see SchemaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLakeToolApp.class)
public class SchemaResourceIntTest {

    private static final String DEFAULT_ENTITY = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTIES = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTIES = "BBBBBBBBBB";

    @Autowired
    private SchemaRepository schemaRepository;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private SchemaSearchRepository schemaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restSchemaMockMvc;

    private Schema schema;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SchemaResource schemaResource = new SchemaResource(schemaService);
        this.restSchemaMockMvc = MockMvcBuilders.standaloneSetup(schemaResource)
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
    public static Schema createEntity() {
        Schema schema = new Schema()
            .entity(DEFAULT_ENTITY)
            .properties(DEFAULT_PROPERTIES);
        return schema;
    }

    @Before
    public void initTest() {
        schemaRepository.deleteAll();
        schemaSearchRepository.deleteAll();
        schema = createEntity();
    }

    @Test
    public void createSchema() throws Exception {
        int databaseSizeBeforeCreate = schemaRepository.findAll().size();

        // Create the Schema
        restSchemaMockMvc.perform(post("/api/schemata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schema)))
            .andExpect(status().isCreated());

        // Validate the Schema in the database
        List<Schema> schemaList = schemaRepository.findAll();
        assertThat(schemaList).hasSize(databaseSizeBeforeCreate + 1);
        Schema testSchema = schemaList.get(schemaList.size() - 1);
        assertThat(testSchema.getEntity()).isEqualTo(DEFAULT_ENTITY);
        assertThat(testSchema.getProperties()).isEqualTo(DEFAULT_PROPERTIES);

        // Validate the Schema in Elasticsearch
        Schema schemaEs = schemaSearchRepository.findOne(testSchema.getId());
        assertThat(schemaEs).isEqualToIgnoringGivenFields(testSchema);
    }

    @Test
    public void createSchemaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schemaRepository.findAll().size();

        // Create the Schema with an existing ID
        schema.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchemaMockMvc.perform(post("/api/schemata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schema)))
            .andExpect(status().isBadRequest());

        // Validate the Schema in the database
        List<Schema> schemaList = schemaRepository.findAll();
        assertThat(schemaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkEntityIsRequired() throws Exception {
        int databaseSizeBeforeTest = schemaRepository.findAll().size();
        // set the field null
        schema.setEntity(null);

        // Create the Schema, which fails.

        restSchemaMockMvc.perform(post("/api/schemata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schema)))
            .andExpect(status().isBadRequest());

        List<Schema> schemaList = schemaRepository.findAll();
        assertThat(schemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSchemata() throws Exception {
        // Initialize the database
        schemaRepository.save(schema);

        // Get all the schemaList
        restSchemaMockMvc.perform(get("/api/schemata?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schema.getId())))
            .andExpect(jsonPath("$.[*].entity").value(hasItem(DEFAULT_ENTITY.toString())))
            .andExpect(jsonPath("$.[*].properties").value(hasItem(DEFAULT_PROPERTIES.toString())));
    }

    @Test
    public void getSchema() throws Exception {
        // Initialize the database
        schemaRepository.save(schema);

        // Get the schema
        restSchemaMockMvc.perform(get("/api/schemata/{id}", schema.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schema.getId()))
            .andExpect(jsonPath("$.entity").value(DEFAULT_ENTITY.toString()))
            .andExpect(jsonPath("$.properties").value(DEFAULT_PROPERTIES.toString()));
    }

    @Test
    public void getNonExistingSchema() throws Exception {
        // Get the schema
        restSchemaMockMvc.perform(get("/api/schemata/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSchema() throws Exception {
        // Initialize the database
        schemaService.save(schema);

        int databaseSizeBeforeUpdate = schemaRepository.findAll().size();

        // Update the schema
        Schema updatedSchema = schemaRepository.findOne(schema.getId());
        updatedSchema
            .entity(UPDATED_ENTITY)
            .properties(UPDATED_PROPERTIES);

        restSchemaMockMvc.perform(put("/api/schemata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSchema)))
            .andExpect(status().isOk());

        // Validate the Schema in the database
        List<Schema> schemaList = schemaRepository.findAll();
        assertThat(schemaList).hasSize(databaseSizeBeforeUpdate);
        Schema testSchema = schemaList.get(schemaList.size() - 1);
        assertThat(testSchema.getEntity()).isEqualTo(UPDATED_ENTITY);
        assertThat(testSchema.getProperties()).isEqualTo(UPDATED_PROPERTIES);

        // Validate the Schema in Elasticsearch
        Schema schemaEs = schemaSearchRepository.findOne(testSchema.getId());
        assertThat(schemaEs).isEqualToIgnoringGivenFields(testSchema);
    }

    @Test
    public void updateNonExistingSchema() throws Exception {
        int databaseSizeBeforeUpdate = schemaRepository.findAll().size();

        // Create the Schema

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSchemaMockMvc.perform(put("/api/schemata")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schema)))
            .andExpect(status().isCreated());

        // Validate the Schema in the database
        List<Schema> schemaList = schemaRepository.findAll();
        assertThat(schemaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteSchema() throws Exception {
        // Initialize the database
        schemaService.save(schema);

        int databaseSizeBeforeDelete = schemaRepository.findAll().size();

        // Get the schema
        restSchemaMockMvc.perform(delete("/api/schemata/{id}", schema.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean schemaExistsInEs = schemaSearchRepository.exists(schema.getId());
        assertThat(schemaExistsInEs).isFalse();

        // Validate the database is empty
        List<Schema> schemaList = schemaRepository.findAll();
        assertThat(schemaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchSchema() throws Exception {
        // Initialize the database
        schemaService.save(schema);

        // Search the schema
        restSchemaMockMvc.perform(get("/api/_search/schemata?query=id:" + schema.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schema.getId())))
            .andExpect(jsonPath("$.[*].entity").value(hasItem(DEFAULT_ENTITY.toString())))
            .andExpect(jsonPath("$.[*].properties").value(hasItem(DEFAULT_PROPERTIES.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Schema.class);
        Schema schema1 = new Schema();
        schema1.setId("id1");
        Schema schema2 = new Schema();
        schema2.setId(schema1.getId());
        assertThat(schema1).isEqualTo(schema2);
        schema2.setId("id2");
        assertThat(schema1).isNotEqualTo(schema2);
        schema1.setId(null);
        assertThat(schema1).isNotEqualTo(schema2);
    }
}
