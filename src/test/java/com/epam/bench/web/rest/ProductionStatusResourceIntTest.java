package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.ProductionStatus;
import com.epam.bench.repository.ProductionStatusRepository;
import com.epam.bench.service.ProductionStatusService;
import com.epam.bench.repository.search.ProductionStatusSearchRepository;

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
 * Test class for the ProductionStatusResource REST controller.
 *
 * @see ProductionStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class ProductionStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private ProductionStatusRepository productionStatusRepository;

    @Inject
    private ProductionStatusService productionStatusService;

    @Inject
    private ProductionStatusSearchRepository productionStatusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProductionStatusMockMvc;

    private ProductionStatus productionStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductionStatusResource productionStatusResource = new ProductionStatusResource();
        ReflectionTestUtils.setField(productionStatusResource, "productionStatusService", productionStatusService);
        this.restProductionStatusMockMvc = MockMvcBuilders.standaloneSetup(productionStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductionStatus createEntity(EntityManager em) {
        ProductionStatus productionStatus = new ProductionStatus()
                .name(DEFAULT_NAME);
        return productionStatus;
    }

    @Before
    public void initTest() {
        productionStatusSearchRepository.deleteAll();
        productionStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductionStatus() throws Exception {
        int databaseSizeBeforeCreate = productionStatusRepository.findAll().size();

        // Create the ProductionStatus

        restProductionStatusMockMvc.perform(post("/api/production-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productionStatus)))
                .andExpect(status().isCreated());

        // Validate the ProductionStatus in the database
        List<ProductionStatus> productionStatuses = productionStatusRepository.findAll();
        assertThat(productionStatuses).hasSize(databaseSizeBeforeCreate + 1);
        ProductionStatus testProductionStatus = productionStatuses.get(productionStatuses.size() - 1);
        assertThat(testProductionStatus.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the ProductionStatus in ElasticSearch
        ProductionStatus productionStatusEs = productionStatusSearchRepository.findOne(testProductionStatus.getId());
        assertThat(productionStatusEs).isEqualToComparingFieldByField(testProductionStatus);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productionStatusRepository.findAll().size();
        // set the field null
        productionStatus.setName(null);

        // Create the ProductionStatus, which fails.

        restProductionStatusMockMvc.perform(post("/api/production-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productionStatus)))
                .andExpect(status().isBadRequest());

        List<ProductionStatus> productionStatuses = productionStatusRepository.findAll();
        assertThat(productionStatuses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductionStatuses() throws Exception {
        // Initialize the database
        productionStatusRepository.saveAndFlush(productionStatus);

        // Get all the productionStatuses
        restProductionStatusMockMvc.perform(get("/api/production-statuses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productionStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getProductionStatus() throws Exception {
        // Initialize the database
        productionStatusRepository.saveAndFlush(productionStatus);

        // Get the productionStatus
        restProductionStatusMockMvc.perform(get("/api/production-statuses/{id}", productionStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productionStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductionStatus() throws Exception {
        // Get the productionStatus
        restProductionStatusMockMvc.perform(get("/api/production-statuses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductionStatus() throws Exception {
        // Initialize the database
        productionStatusService.save(productionStatus);

        int databaseSizeBeforeUpdate = productionStatusRepository.findAll().size();

        // Update the productionStatus
        ProductionStatus updatedProductionStatus = productionStatusRepository.findOne(productionStatus.getId());
        updatedProductionStatus
                .name(UPDATED_NAME);

        restProductionStatusMockMvc.perform(put("/api/production-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProductionStatus)))
                .andExpect(status().isOk());

        // Validate the ProductionStatus in the database
        List<ProductionStatus> productionStatuses = productionStatusRepository.findAll();
        assertThat(productionStatuses).hasSize(databaseSizeBeforeUpdate);
        ProductionStatus testProductionStatus = productionStatuses.get(productionStatuses.size() - 1);
        assertThat(testProductionStatus.getName()).isEqualTo(UPDATED_NAME);

        // Validate the ProductionStatus in ElasticSearch
        ProductionStatus productionStatusEs = productionStatusSearchRepository.findOne(testProductionStatus.getId());
        assertThat(productionStatusEs).isEqualToComparingFieldByField(testProductionStatus);
    }

    @Test
    @Transactional
    public void deleteProductionStatus() throws Exception {
        // Initialize the database
        productionStatusService.save(productionStatus);

        int databaseSizeBeforeDelete = productionStatusRepository.findAll().size();

        // Get the productionStatus
        restProductionStatusMockMvc.perform(delete("/api/production-statuses/{id}", productionStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean productionStatusExistsInEs = productionStatusSearchRepository.exists(productionStatus.getId());
        assertThat(productionStatusExistsInEs).isFalse();

        // Validate the database is empty
        List<ProductionStatus> productionStatuses = productionStatusRepository.findAll();
        assertThat(productionStatuses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProductionStatus() throws Exception {
        // Initialize the database
        productionStatusService.save(productionStatus);

        // Search the productionStatus
        restProductionStatusMockMvc.perform(get("/api/_search/production-statuses?query=id:" + productionStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productionStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
