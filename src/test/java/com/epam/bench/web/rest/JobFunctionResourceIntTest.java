package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.JobFunction;
import com.epam.bench.repository.JobFunctionRepository;
import com.epam.bench.service.JobFunctionService;
import com.epam.bench.repository.search.JobFunctionSearchRepository;

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
 * Test class for the JobFunctionResource REST controller.
 *
 * @see JobFunctionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class JobFunctionResourceIntTest {

    private static final String DEFAULT_UPSA_ID = "AAAAA";
    private static final String UPDATED_UPSA_ID = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_PREFIX = "AAAAA";
    private static final String UPDATED_PREFIX = "BBBBB";

    @Inject
    private JobFunctionRepository jobFunctionRepository;

    @Inject
    private JobFunctionService jobFunctionService;

    @Inject
    private JobFunctionSearchRepository jobFunctionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restJobFunctionMockMvc;

    private JobFunction jobFunction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobFunctionResource jobFunctionResource = new JobFunctionResource();
        ReflectionTestUtils.setField(jobFunctionResource, "jobFunctionService", jobFunctionService);
        this.restJobFunctionMockMvc = MockMvcBuilders.standaloneSetup(jobFunctionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobFunction createEntity(EntityManager em) {
        JobFunction jobFunction = new JobFunction()
                .upsaId(DEFAULT_UPSA_ID)
                .name(DEFAULT_NAME)
                .prefix(DEFAULT_PREFIX);
        return jobFunction;
    }

    @Before
    public void initTest() {
        jobFunctionSearchRepository.deleteAll();
        jobFunction = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobFunction() throws Exception {
        int databaseSizeBeforeCreate = jobFunctionRepository.findAll().size();

        // Create the JobFunction

        restJobFunctionMockMvc.perform(post("/api/job-functions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobFunction)))
                .andExpect(status().isCreated());

        // Validate the JobFunction in the database
        List<JobFunction> jobFunctions = jobFunctionRepository.findAll();
        assertThat(jobFunctions).hasSize(databaseSizeBeforeCreate + 1);
        JobFunction testJobFunction = jobFunctions.get(jobFunctions.size() - 1);
        assertThat(testJobFunction.getUpsaId()).isEqualTo(DEFAULT_UPSA_ID);
        assertThat(testJobFunction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testJobFunction.getPrefix()).isEqualTo(DEFAULT_PREFIX);

        // Validate the JobFunction in ElasticSearch
        JobFunction jobFunctionEs = jobFunctionSearchRepository.findOne(testJobFunction.getId());
        assertThat(jobFunctionEs).isEqualToComparingFieldByField(testJobFunction);
    }

    @Test
    @Transactional
    public void checkUpsaIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobFunctionRepository.findAll().size();
        // set the field null
        jobFunction.setUpsaId(null);

        // Create the JobFunction, which fails.

        restJobFunctionMockMvc.perform(post("/api/job-functions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobFunction)))
                .andExpect(status().isBadRequest());

        List<JobFunction> jobFunctions = jobFunctionRepository.findAll();
        assertThat(jobFunctions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobFunctionRepository.findAll().size();
        // set the field null
        jobFunction.setName(null);

        // Create the JobFunction, which fails.

        restJobFunctionMockMvc.perform(post("/api/job-functions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobFunction)))
                .andExpect(status().isBadRequest());

        List<JobFunction> jobFunctions = jobFunctionRepository.findAll();
        assertThat(jobFunctions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobFunctions() throws Exception {
        // Initialize the database
        jobFunctionRepository.saveAndFlush(jobFunction);

        // Get all the jobFunctions
        restJobFunctionMockMvc.perform(get("/api/job-functions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jobFunction.getId().intValue())))
                .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX.toString())));
    }

    @Test
    @Transactional
    public void getJobFunction() throws Exception {
        // Initialize the database
        jobFunctionRepository.saveAndFlush(jobFunction);

        // Get the jobFunction
        restJobFunctionMockMvc.perform(get("/api/job-functions/{id}", jobFunction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobFunction.getId().intValue()))
            .andExpect(jsonPath("$.upsaId").value(DEFAULT_UPSA_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobFunction() throws Exception {
        // Get the jobFunction
        restJobFunctionMockMvc.perform(get("/api/job-functions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobFunction() throws Exception {
        // Initialize the database
        jobFunctionService.save(jobFunction);

        int databaseSizeBeforeUpdate = jobFunctionRepository.findAll().size();

        // Update the jobFunction
        JobFunction updatedJobFunction = jobFunctionRepository.findOne(jobFunction.getId());
        updatedJobFunction
                .upsaId(UPDATED_UPSA_ID)
                .name(UPDATED_NAME)
                .prefix(UPDATED_PREFIX);

        restJobFunctionMockMvc.perform(put("/api/job-functions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedJobFunction)))
                .andExpect(status().isOk());

        // Validate the JobFunction in the database
        List<JobFunction> jobFunctions = jobFunctionRepository.findAll();
        assertThat(jobFunctions).hasSize(databaseSizeBeforeUpdate);
        JobFunction testJobFunction = jobFunctions.get(jobFunctions.size() - 1);
        assertThat(testJobFunction.getUpsaId()).isEqualTo(UPDATED_UPSA_ID);
        assertThat(testJobFunction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testJobFunction.getPrefix()).isEqualTo(UPDATED_PREFIX);

        // Validate the JobFunction in ElasticSearch
        JobFunction jobFunctionEs = jobFunctionSearchRepository.findOne(testJobFunction.getId());
        assertThat(jobFunctionEs).isEqualToComparingFieldByField(testJobFunction);
    }

    @Test
    @Transactional
    public void deleteJobFunction() throws Exception {
        // Initialize the database
        jobFunctionService.save(jobFunction);

        int databaseSizeBeforeDelete = jobFunctionRepository.findAll().size();

        // Get the jobFunction
        restJobFunctionMockMvc.perform(delete("/api/job-functions/{id}", jobFunction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean jobFunctionExistsInEs = jobFunctionSearchRepository.exists(jobFunction.getId());
        assertThat(jobFunctionExistsInEs).isFalse();

        // Validate the database is empty
        List<JobFunction> jobFunctions = jobFunctionRepository.findAll();
        assertThat(jobFunctions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobFunction() throws Exception {
        // Initialize the database
        jobFunctionService.save(jobFunction);

        // Search the jobFunction
        restJobFunctionMockMvc.perform(get("/api/_search/job-functions?query=id:" + jobFunction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobFunction.getId().intValue())))
            .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX.toString())));
    }
}
