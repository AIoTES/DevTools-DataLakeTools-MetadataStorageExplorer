package eu.activageproject.tool.web.rest;

import eu.activageproject.tool.DataLakeToolApp;

import eu.activageproject.tool.domain.Model;
import eu.activageproject.tool.repository.ModelRepository;
import eu.activageproject.tool.service.ModelService;
import eu.activageproject.tool.repository.search.ModelSearchRepository;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static eu.activageproject.tool.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ModelResource REST controller.
 *
 * @see ModelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLakeToolApp.class)
public class ModelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMS = "AAAAAAAAAA";
    private static final String UPDATED_PARAMS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelSearchRepository modelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restModelMockMvc;

    private Model model;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModelResource modelResource = new ModelResource(modelService);
        this.restModelMockMvc = MockMvcBuilders.standaloneSetup(modelResource)
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
    public static Model createEntity() {
        Model model = new Model()
            .name(DEFAULT_NAME)
            .params(DEFAULT_PARAMS)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED)
            .createdBy(DEFAULT_CREATED_BY);
        return model;
    }

    @Before
    public void initTest() {
        modelRepository.deleteAll();
        modelSearchRepository.deleteAll();
        model = createEntity();
    }

    @Test
    public void createModel() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().size();

        // Create the Model
        restModelMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isCreated());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate + 1);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModel.getParams()).isEqualTo(DEFAULT_PARAMS);
        assertThat(testModel.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testModel.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testModel.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);

        // Validate the Model in Elasticsearch
        Model modelEs = modelSearchRepository.findOne(testModel.getId());
        assertThat(modelEs).isEqualToIgnoringGivenFields(testModel);
    }

    @Test
    public void createModelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().size();

        // Create the Model with an existing ID
        model.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restModelMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().size();
        // set the field null
        model.setName(null);

        // Create the Model, which fails.

        restModelMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isBadRequest());

        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkParamsIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().size();
        // set the field null
        model.setParams(null);

        // Create the Model, which fails.

        restModelMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isBadRequest());

        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllModels() throws Exception {
        // Initialize the database
        modelRepository.save(model);

        // Get all the modelList
        restModelMockMvc.perform(get("/api/models?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].params").value(hasItem(DEFAULT_PARAMS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }

    @Test
    public void getModel() throws Exception {
        // Initialize the database
        modelRepository.save(model);

        // Get the model
        restModelMockMvc.perform(get("/api/models/{id}", model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(model.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.params").value(DEFAULT_PARAMS.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()));
    }

    @Test
    public void getNonExistingModel() throws Exception {
        // Get the model
        restModelMockMvc.perform(get("/api/models/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateModel() throws Exception {
        // Initialize the database
        modelService.save(model);

        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model
        Model updatedModel = modelRepository.findOne(model.getId());
        updatedModel
            .name(UPDATED_NAME)
            .params(UPDATED_PARAMS)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .createdBy(UPDATED_CREATED_BY);

        restModelMockMvc.perform(put("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModel)))
            .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModel.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testModel.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testModel.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testModel.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);

        // Validate the Model in Elasticsearch
        Model modelEs = modelSearchRepository.findOne(testModel.getId());
        assertThat(modelEs).isEqualToIgnoringGivenFields(testModel);
    }

    @Test
    public void updateNonExistingModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Create the Model

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restModelMockMvc.perform(put("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isCreated());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteModel() throws Exception {
        // Initialize the database
        modelService.save(model);

        int databaseSizeBeforeDelete = modelRepository.findAll().size();

        // Get the model
        restModelMockMvc.perform(delete("/api/models/{id}", model.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean modelExistsInEs = modelSearchRepository.exists(model.getId());
        assertThat(modelExistsInEs).isFalse();

        // Validate the database is empty
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void searchModel() throws Exception {
        // Initialize the database
        modelService.save(model);

        // Search the model
        restModelMockMvc.perform(get("/api/_search/models?query=id:" + model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].params").value(hasItem(DEFAULT_PARAMS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Model.class);
        Model model1 = new Model();
        model1.setId("id1");
        Model model2 = new Model();
        model2.setId(model1.getId());
        assertThat(model1).isEqualTo(model2);
        model2.setId("id2");
        assertThat(model1).isNotEqualTo(model2);
        model1.setId(null);
        assertThat(model1).isNotEqualTo(model2);
    }
}
