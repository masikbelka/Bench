package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.OpportunityType;
import com.epam.bench.repository.OpportunityTypeRepository;
import com.epam.bench.service.OpportunityTypeService;
import com.epam.bench.repository.search.OpportunityTypeSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OpportunityTypeResource REST controller.
 *
 * @see OpportunityTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class OpportunityTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private OpportunityTypeRepository opportunityTypeRepository;

    @Inject
    private OpportunityTypeService opportunityTypeService;

    @Inject
    private OpportunityTypeSearchRepository opportunityTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOpportunityTypeMockMvc;

    private OpportunityType opportunityType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OpportunityTypeResource opportunityTypeResource = new OpportunityTypeResource();
        ReflectionTestUtils.setField(opportunityTypeResource, "opportunityTypeService", opportunityTypeService);
        this.restOpportunityTypeMockMvc = MockMvcBuilders.standaloneSetup(opportunityTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OpportunityType createEntity(EntityManager em) {
        OpportunityType opportunityType = new OpportunityType()
                .name(DEFAULT_NAME);
        return opportunityType;
    }

    @Before
    public void initTest() {
        opportunityTypeSearchRepository.deleteAll();
        opportunityType = createEntity(em);
    }

    @Test
    @Transactional
    public void createOpportunityType() throws Exception {
        int databaseSizeBeforeCreate = opportunityTypeRepository.findAll().size();

        // Create the OpportunityType

        restOpportunityTypeMockMvc.perform(post("/api/opportunity-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(opportunityType)))
                .andExpect(status().isCreated());

        // Validate the OpportunityType in the database
        List<OpportunityType> opportunityTypes = opportunityTypeRepository.findAll();
        assertThat(opportunityTypes).hasSize(databaseSizeBeforeCreate + 1);
        OpportunityType testOpportunityType = opportunityTypes.get(opportunityTypes.size() - 1);
        assertThat(testOpportunityType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the OpportunityType in ElasticSearch
        OpportunityType opportunityTypeEs = opportunityTypeSearchRepository.findOne(testOpportunityType.getId());
        assertThat(opportunityTypeEs).isEqualToComparingFieldByField(testOpportunityType);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunityTypeRepository.findAll().size();
        // set the field null
        opportunityType.setName(null);

        // Create the OpportunityType, which fails.

        restOpportunityTypeMockMvc.perform(post("/api/opportunity-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(opportunityType)))
                .andExpect(status().isBadRequest());

        List<OpportunityType> opportunityTypes = opportunityTypeRepository.findAll();
        assertThat(opportunityTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOpportunityTypes() throws Exception {
        // Initialize the database
        opportunityTypeRepository.saveAndFlush(opportunityType);

        // Get all the opportunityTypes
        restOpportunityTypeMockMvc.perform(get("/api/opportunity-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(opportunityType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getOpportunityType() throws Exception {
        // Initialize the database
        opportunityTypeRepository.saveAndFlush(opportunityType);

        // Get the opportunityType
        restOpportunityTypeMockMvc.perform(get("/api/opportunity-types/{id}", opportunityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(opportunityType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOpportunityType() throws Exception {
        // Get the opportunityType
        restOpportunityTypeMockMvc.perform(get("/api/opportunity-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOpportunityType() throws Exception {
        // Initialize the database
        opportunityTypeService.save(opportunityType);

        int databaseSizeBeforeUpdate = opportunityTypeRepository.findAll().size();

        // Update the opportunityType
        OpportunityType updatedOpportunityType = opportunityTypeRepository.findOne(opportunityType.getId());
        updatedOpportunityType
                .name(UPDATED_NAME);

        restOpportunityTypeMockMvc.perform(put("/api/opportunity-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOpportunityType)))
                .andExpect(status().isOk());

        // Validate the OpportunityType in the database
        List<OpportunityType> opportunityTypes = opportunityTypeRepository.findAll();
        assertThat(opportunityTypes).hasSize(databaseSizeBeforeUpdate);
        OpportunityType testOpportunityType = opportunityTypes.get(opportunityTypes.size() - 1);
        assertThat(testOpportunityType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the OpportunityType in ElasticSearch
        OpportunityType opportunityTypeEs = opportunityTypeSearchRepository.findOne(testOpportunityType.getId());
        assertThat(opportunityTypeEs).isEqualToComparingFieldByField(testOpportunityType);
    }

    @Test
    @Transactional
    public void deleteOpportunityType() throws Exception {
        // Initialize the database
        opportunityTypeService.save(opportunityType);

        int databaseSizeBeforeDelete = opportunityTypeRepository.findAll().size();

        // Get the opportunityType
        restOpportunityTypeMockMvc.perform(delete("/api/opportunity-types/{id}", opportunityType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean opportunityTypeExistsInEs = opportunityTypeSearchRepository.exists(opportunityType.getId());
        assertThat(opportunityTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<OpportunityType> opportunityTypes = opportunityTypeRepository.findAll();
        assertThat(opportunityTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOpportunityType() throws Exception {
        // Initialize the database
        opportunityTypeService.save(opportunityType);

        // Search the opportunityType
        restOpportunityTypeMockMvc.perform(get("/api/_search/opportunity-types?query=id:" + opportunityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
