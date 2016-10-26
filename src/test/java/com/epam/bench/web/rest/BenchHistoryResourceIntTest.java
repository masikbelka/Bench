package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.BenchHistory;
import com.epam.bench.repository.BenchHistoryRepository;
import com.epam.bench.service.BenchHistoryService;
import com.epam.bench.repository.search.BenchHistorySearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BenchHistoryResource REST controller.
 *
 * @see BenchHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class BenchHistoryResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATED_TIME);

    private static final Boolean DEFAULT_BENCH = false;
    private static final Boolean UPDATED_BENCH = true;

    private static final String DEFAULT_MANAGER_ID = "AAAAA";
    private static final String UPDATED_MANAGER_ID = "BBBBB";

    private static final ZonedDateTime DEFAULT_VALID_TO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_VALID_TO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_VALID_TO_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_VALID_TO);

    @Inject
    private BenchHistoryRepository benchHistoryRepository;

    @Inject
    private BenchHistoryService benchHistoryService;

    @Inject
    private BenchHistorySearchRepository benchHistorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBenchHistoryMockMvc;

    private BenchHistory benchHistory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BenchHistoryResource benchHistoryResource = new BenchHistoryResource();
        ReflectionTestUtils.setField(benchHistoryResource, "benchHistoryService", benchHistoryService);
        this.restBenchHistoryMockMvc = MockMvcBuilders.standaloneSetup(benchHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BenchHistory createEntity(EntityManager em) {
        BenchHistory benchHistory = new BenchHistory()
                .createdTime(DEFAULT_CREATED_TIME)
                .bench(DEFAULT_BENCH)
                .managerId(DEFAULT_MANAGER_ID)
                .validTo(DEFAULT_VALID_TO);
        return benchHistory;
    }

    @Before
    public void initTest() {
        benchHistorySearchRepository.deleteAll();
        benchHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createBenchHistory() throws Exception {
        int databaseSizeBeforeCreate = benchHistoryRepository.findAll().size();

        // Create the BenchHistory

        restBenchHistoryMockMvc.perform(post("/api/bench-histories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(benchHistory)))
                .andExpect(status().isCreated());

        // Validate the BenchHistory in the database
        List<BenchHistory> benchHistories = benchHistoryRepository.findAll();
        assertThat(benchHistories).hasSize(databaseSizeBeforeCreate + 1);
        BenchHistory testBenchHistory = benchHistories.get(benchHistories.size() - 1);
        assertThat(testBenchHistory.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testBenchHistory.isBench()).isEqualTo(DEFAULT_BENCH);
        assertThat(testBenchHistory.getManagerId()).isEqualTo(DEFAULT_MANAGER_ID);
        assertThat(testBenchHistory.getValidTo()).isEqualTo(DEFAULT_VALID_TO);

        // Validate the BenchHistory in ElasticSearch
        BenchHistory benchHistoryEs = benchHistorySearchRepository.findOne(testBenchHistory.getId());
        assertThat(benchHistoryEs).isEqualToComparingFieldByField(testBenchHistory);
    }

    @Test
    @Transactional
    public void checkCreatedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = benchHistoryRepository.findAll().size();
        // set the field null
        benchHistory.setCreatedTime(null);

        // Create the BenchHistory, which fails.

        restBenchHistoryMockMvc.perform(post("/api/bench-histories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(benchHistory)))
                .andExpect(status().isBadRequest());

        List<BenchHistory> benchHistories = benchHistoryRepository.findAll();
        assertThat(benchHistories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBenchIsRequired() throws Exception {
        int databaseSizeBeforeTest = benchHistoryRepository.findAll().size();
        // set the field null
        benchHistory.setBench(null);

        // Create the BenchHistory, which fails.

        restBenchHistoryMockMvc.perform(post("/api/bench-histories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(benchHistory)))
                .andExpect(status().isBadRequest());

        List<BenchHistory> benchHistories = benchHistoryRepository.findAll();
        assertThat(benchHistories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBenchHistories() throws Exception {
        // Initialize the database
        benchHistoryRepository.saveAndFlush(benchHistory);

        // Get all the benchHistories
        restBenchHistoryMockMvc.perform(get("/api/bench-histories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(benchHistory.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME_STR)))
                .andExpect(jsonPath("$.[*].bench").value(hasItem(DEFAULT_BENCH.booleanValue())))
                .andExpect(jsonPath("$.[*].managerId").value(hasItem(DEFAULT_MANAGER_ID.toString())))
                .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO_STR)));
    }

    @Test
    @Transactional
    public void getBenchHistory() throws Exception {
        // Initialize the database
        benchHistoryRepository.saveAndFlush(benchHistory);

        // Get the benchHistory
        restBenchHistoryMockMvc.perform(get("/api/bench-histories/{id}", benchHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(benchHistory.getId().intValue()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME_STR))
            .andExpect(jsonPath("$.bench").value(DEFAULT_BENCH.booleanValue()))
            .andExpect(jsonPath("$.managerId").value(DEFAULT_MANAGER_ID.toString()))
            .andExpect(jsonPath("$.validTo").value(DEFAULT_VALID_TO_STR));
    }

    @Test
    @Transactional
    public void getNonExistingBenchHistory() throws Exception {
        // Get the benchHistory
        restBenchHistoryMockMvc.perform(get("/api/bench-histories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBenchHistory() throws Exception {
        // Initialize the database
        benchHistoryService.save(benchHistory);

        int databaseSizeBeforeUpdate = benchHistoryRepository.findAll().size();

        // Update the benchHistory
        BenchHistory updatedBenchHistory = benchHistoryRepository.findOne(benchHistory.getId());
        updatedBenchHistory
                .createdTime(UPDATED_CREATED_TIME)
                .bench(UPDATED_BENCH)
                .managerId(UPDATED_MANAGER_ID)
                .validTo(UPDATED_VALID_TO);

        restBenchHistoryMockMvc.perform(put("/api/bench-histories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBenchHistory)))
                .andExpect(status().isOk());

        // Validate the BenchHistory in the database
        List<BenchHistory> benchHistories = benchHistoryRepository.findAll();
        assertThat(benchHistories).hasSize(databaseSizeBeforeUpdate);
        BenchHistory testBenchHistory = benchHistories.get(benchHistories.size() - 1);
        assertThat(testBenchHistory.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testBenchHistory.isBench()).isEqualTo(UPDATED_BENCH);
        assertThat(testBenchHistory.getManagerId()).isEqualTo(UPDATED_MANAGER_ID);
        assertThat(testBenchHistory.getValidTo()).isEqualTo(UPDATED_VALID_TO);

        // Validate the BenchHistory in ElasticSearch
        BenchHistory benchHistoryEs = benchHistorySearchRepository.findOne(testBenchHistory.getId());
        assertThat(benchHistoryEs).isEqualToComparingFieldByField(testBenchHistory);
    }

    @Test
    @Transactional
    public void deleteBenchHistory() throws Exception {
        // Initialize the database
        benchHistoryService.save(benchHistory);

        int databaseSizeBeforeDelete = benchHistoryRepository.findAll().size();

        // Get the benchHistory
        restBenchHistoryMockMvc.perform(delete("/api/bench-histories/{id}", benchHistory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean benchHistoryExistsInEs = benchHistorySearchRepository.exists(benchHistory.getId());
        assertThat(benchHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<BenchHistory> benchHistories = benchHistoryRepository.findAll();
        assertThat(benchHistories).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBenchHistory() throws Exception {
        // Initialize the database
        benchHistoryService.save(benchHistory);

        // Search the benchHistory
        restBenchHistoryMockMvc.perform(get("/api/_search/bench-histories?query=id:" + benchHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(benchHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME_STR)))
            .andExpect(jsonPath("$.[*].bench").value(hasItem(DEFAULT_BENCH.booleanValue())))
            .andExpect(jsonPath("$.[*].managerId").value(hasItem(DEFAULT_MANAGER_ID.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO_STR)));
    }
}
