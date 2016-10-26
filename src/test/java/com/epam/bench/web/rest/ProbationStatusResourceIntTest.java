package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.ProbationStatus;
import com.epam.bench.repository.ProbationStatusRepository;
import com.epam.bench.service.ProbationStatusService;
import com.epam.bench.repository.search.ProbationStatusSearchRepository;

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
 * Test class for the ProbationStatusResource REST controller.
 *
 * @see ProbationStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class ProbationStatusResourceIntTest {

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_END_DATE);

    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";

    @Inject
    private ProbationStatusRepository probationStatusRepository;

    @Inject
    private ProbationStatusService probationStatusService;

    @Inject
    private ProbationStatusSearchRepository probationStatusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProbationStatusMockMvc;

    private ProbationStatus probationStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProbationStatusResource probationStatusResource = new ProbationStatusResource();
        ReflectionTestUtils.setField(probationStatusResource, "probationStatusService", probationStatusService);
        this.restProbationStatusMockMvc = MockMvcBuilders.standaloneSetup(probationStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProbationStatus createEntity(EntityManager em) {
        ProbationStatus probationStatus = new ProbationStatus()
                .endDate(DEFAULT_END_DATE)
                .status(DEFAULT_STATUS);
        return probationStatus;
    }

    @Before
    public void initTest() {
        probationStatusSearchRepository.deleteAll();
        probationStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createProbationStatus() throws Exception {
        int databaseSizeBeforeCreate = probationStatusRepository.findAll().size();

        // Create the ProbationStatus

        restProbationStatusMockMvc.perform(post("/api/probation-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(probationStatus)))
                .andExpect(status().isCreated());

        // Validate the ProbationStatus in the database
        List<ProbationStatus> probationStatuses = probationStatusRepository.findAll();
        assertThat(probationStatuses).hasSize(databaseSizeBeforeCreate + 1);
        ProbationStatus testProbationStatus = probationStatuses.get(probationStatuses.size() - 1);
        assertThat(testProbationStatus.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testProbationStatus.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the ProbationStatus in ElasticSearch
        ProbationStatus probationStatusEs = probationStatusSearchRepository.findOne(testProbationStatus.getId());
        assertThat(probationStatusEs).isEqualToComparingFieldByField(testProbationStatus);
    }

    @Test
    @Transactional
    public void getAllProbationStatuses() throws Exception {
        // Initialize the database
        probationStatusRepository.saveAndFlush(probationStatus);

        // Get all the probationStatuses
        restProbationStatusMockMvc.perform(get("/api/probation-statuses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(probationStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getProbationStatus() throws Exception {
        // Initialize the database
        probationStatusRepository.saveAndFlush(probationStatus);

        // Get the probationStatus
        restProbationStatusMockMvc.perform(get("/api/probation-statuses/{id}", probationStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(probationStatus.getId().intValue()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProbationStatus() throws Exception {
        // Get the probationStatus
        restProbationStatusMockMvc.perform(get("/api/probation-statuses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProbationStatus() throws Exception {
        // Initialize the database
        probationStatusService.save(probationStatus);

        int databaseSizeBeforeUpdate = probationStatusRepository.findAll().size();

        // Update the probationStatus
        ProbationStatus updatedProbationStatus = probationStatusRepository.findOne(probationStatus.getId());
        updatedProbationStatus
                .endDate(UPDATED_END_DATE)
                .status(UPDATED_STATUS);

        restProbationStatusMockMvc.perform(put("/api/probation-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProbationStatus)))
                .andExpect(status().isOk());

        // Validate the ProbationStatus in the database
        List<ProbationStatus> probationStatuses = probationStatusRepository.findAll();
        assertThat(probationStatuses).hasSize(databaseSizeBeforeUpdate);
        ProbationStatus testProbationStatus = probationStatuses.get(probationStatuses.size() - 1);
        assertThat(testProbationStatus.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProbationStatus.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the ProbationStatus in ElasticSearch
        ProbationStatus probationStatusEs = probationStatusSearchRepository.findOne(testProbationStatus.getId());
        assertThat(probationStatusEs).isEqualToComparingFieldByField(testProbationStatus);
    }

    @Test
    @Transactional
    public void deleteProbationStatus() throws Exception {
        // Initialize the database
        probationStatusService.save(probationStatus);

        int databaseSizeBeforeDelete = probationStatusRepository.findAll().size();

        // Get the probationStatus
        restProbationStatusMockMvc.perform(delete("/api/probation-statuses/{id}", probationStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean probationStatusExistsInEs = probationStatusSearchRepository.exists(probationStatus.getId());
        assertThat(probationStatusExistsInEs).isFalse();

        // Validate the database is empty
        List<ProbationStatus> probationStatuses = probationStatusRepository.findAll();
        assertThat(probationStatuses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProbationStatus() throws Exception {
        // Initialize the database
        probationStatusService.save(probationStatus);

        // Search the probationStatus
        restProbationStatusMockMvc.perform(get("/api/_search/probation-statuses?query=id:" + probationStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(probationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
