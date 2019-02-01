package eu.activageproject.tool.web.rest;

import eu.activageproject.tool.DataLakeToolApp;

import eu.activageproject.tool.domain.Query;
import eu.activageproject.tool.repository.QueryRepository;
import eu.activageproject.tool.service.QueryService;
import eu.activageproject.tool.repository.search.QuerySearchRepository;
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
 * Test class for the QueryResource REST controller.
 *
 * @see QueryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLakeToolApp.class)
public class QueryResourceIntTest {

    private static final String DEFAULT_DB = "AAAAAAAAAA";
    private static final String UPDATED_DB = "BBBBBBBBBB";

    private static final String DEFAULT_TABLE = "AAAAAAAAAA";
    private static final String UPDATED_TABLE = "BBBBBBBBBB";

    private static final String DEFAULT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_QUERY = "BBBBBBBBBB";

    private static final String DEFAULT_OUTPUT = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT = "BBBBBBBBBB";

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private QueryService queryService;

    @Autowired
    private QuerySearchRepository querySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restQueryMockMvc;

    private Query query;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QueryResource queryResource = new QueryResource(queryService);
        this.restQueryMockMvc = MockMvcBuilders.standaloneSetup(queryResource)
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
    public static Query createEntity() {
        Query query = new Query()
            .db(DEFAULT_DB)
            .table(DEFAULT_TABLE)
            .query(DEFAULT_QUERY)
            .output(DEFAULT_OUTPUT);
        return query;
    }

    @Before
    public void initTest() {
        queryRepository.deleteAll();
        querySearchRepository.deleteAll();
        query = createEntity();
    }

    @Test
    public void createQuery() throws Exception {
        int databaseSizeBeforeCreate = queryRepository.findAll().size();

        // Create the Query
        restQueryMockMvc.perform(post("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(query)))
            .andExpect(status().isCreated());

        // Validate the Query in the database
        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeCreate + 1);
        Query testQuery = queryList.get(queryList.size() - 1);
        assertThat(testQuery.getDb()).isEqualTo(DEFAULT_DB);
        assertThat(testQuery.getTable()).isEqualTo(DEFAULT_TABLE);
        assertThat(testQuery.getQuery()).isEqualTo(DEFAULT_QUERY);
        assertThat(testQuery.getOutput()).isEqualTo(DEFAULT_OUTPUT);

        // Validate the Query in Elasticsearch
        Query queryEs = querySearchRepository.findOne(testQuery.getId());
        assertThat(queryEs).isEqualToIgnoringGivenFields(testQuery);
    }

    @Test
    public void createQueryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = queryRepository.findAll().size();

        // Create the Query with an existing ID
        query.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restQueryMockMvc.perform(post("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(query)))
            .andExpect(status().isBadRequest());

        // Validate the Query in the database
        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkDbIsRequired() throws Exception {
        int databaseSizeBeforeTest = queryRepository.findAll().size();
        // set the field null
        query.setDb(null);

        // Create the Query, which fails.

        restQueryMockMvc.perform(post("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(query)))
            .andExpect(status().isBadRequest());

        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTableIsRequired() throws Exception {
        int databaseSizeBeforeTest = queryRepository.findAll().size();
        // set the field null
        query.setTable(null);

        // Create the Query, which fails.

        restQueryMockMvc.perform(post("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(query)))
            .andExpect(status().isBadRequest());

        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkQueryIsRequired() throws Exception {
        int databaseSizeBeforeTest = queryRepository.findAll().size();
        // set the field null
        query.setQuery(null);

        // Create the Query, which fails.

        restQueryMockMvc.perform(post("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(query)))
            .andExpect(status().isBadRequest());

        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllQueries() throws Exception {
        // Initialize the database
        queryRepository.save(query);

        // Get all the queryList
        restQueryMockMvc.perform(get("/api/queries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(query.getId())))
            .andExpect(jsonPath("$.[*].db").value(hasItem(DEFAULT_DB.toString())))
            .andExpect(jsonPath("$.[*].table").value(hasItem(DEFAULT_TABLE.toString())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY.toString())))
            .andExpect(jsonPath("$.[*].output").value(hasItem(DEFAULT_OUTPUT.toString())));
    }

    @Test
    public void getQuery() throws Exception {
        // Initialize the database
        queryRepository.save(query);

        // Get the query
        restQueryMockMvc.perform(get("/api/queries/{id}", query.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(query.getId()))
            .andExpect(jsonPath("$.db").value(DEFAULT_DB.toString()))
            .andExpect(jsonPath("$.table").value(DEFAULT_TABLE.toString()))
            .andExpect(jsonPath("$.query").value(DEFAULT_QUERY.toString()))
            .andExpect(jsonPath("$.output").value(DEFAULT_OUTPUT.toString()));
    }

    @Test
    public void getNonExistingQuery() throws Exception {
        // Get the query
        restQueryMockMvc.perform(get("/api/queries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateQuery() throws Exception {
        // Initialize the database
        queryService.save(query);

        int databaseSizeBeforeUpdate = queryRepository.findAll().size();

        // Update the query
        Query updatedQuery = queryRepository.findOne(query.getId());
        updatedQuery
            .db(UPDATED_DB)
            .table(UPDATED_TABLE)
            .query(UPDATED_QUERY)
            .output(UPDATED_OUTPUT);

        restQueryMockMvc.perform(put("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuery)))
            .andExpect(status().isOk());

        // Validate the Query in the database
        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeUpdate);
        Query testQuery = queryList.get(queryList.size() - 1);
        assertThat(testQuery.getDb()).isEqualTo(UPDATED_DB);
        assertThat(testQuery.getTable()).isEqualTo(UPDATED_TABLE);
        assertThat(testQuery.getQuery()).isEqualTo(UPDATED_QUERY);
        assertThat(testQuery.getOutput()).isEqualTo(UPDATED_OUTPUT);

        // Validate the Query in Elasticsearch
        Query queryEs = querySearchRepository.findOne(testQuery.getId());
        assertThat(queryEs).isEqualToIgnoringGivenFields(testQuery);
    }

    @Test
    public void updateNonExistingQuery() throws Exception {
        int databaseSizeBeforeUpdate = queryRepository.findAll().size();

        // Create the Query

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQueryMockMvc.perform(put("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(query)))
            .andExpect(status().isCreated());

        // Validate the Query in the database
        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteQuery() throws Exception {
        // Initialize the database
        queryService.save(query);

        int databaseSizeBeforeDelete = queryRepository.findAll().size();

        // Get the query
        restQueryMockMvc.perform(delete("/api/queries/{id}", query.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean queryExistsInEs = querySearchRepository.exists(query.getId());
        assertThat(queryExistsInEs).isFalse();

        // Validate the database is empty
        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchQuery() throws Exception {
        // Initialize the database
        queryService.save(query);

        // Search the query
        restQueryMockMvc.perform(get("/api/_search/queries?query=id:" + query.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(query.getId())))
            .andExpect(jsonPath("$.[*].db").value(hasItem(DEFAULT_DB.toString())))
            .andExpect(jsonPath("$.[*].table").value(hasItem(DEFAULT_TABLE.toString())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY.toString())))
            .andExpect(jsonPath("$.[*].output").value(hasItem(DEFAULT_OUTPUT.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Query.class);
        Query query1 = new Query();
        query1.setId("id1");
        Query query2 = new Query();
        query2.setId(query1.getId());
        assertThat(query1).isEqualTo(query2);
        query2.setId("id2");
        assertThat(query1).isNotEqualTo(query2);
        query1.setId(null);
        assertThat(query1).isNotEqualTo(query2);
    }
}
