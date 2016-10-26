package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.ProjectHistory;
import com.epam.bench.repository.ProjectHistoryRepository;
import com.epam.bench.service.ProjectHistoryService;
import com.epam.bench.repository.search.ProjectHistorySearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectHistoryResource REST controller.
 *
 * @see ProjectHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class ProjectHistoryResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_WORKLOAD = 1;
    private static final Integer UPDATED_WORKLOAD = 2;

    @Inject
    private ProjectHistoryRepository projectHistoryRepository;

    @Inject
    private ProjectHistoryService projectHistoryService;

    @Inject
    private ProjectHistorySearchRepository projectHistorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectHistoryMockMvc;

    private ProjectHistory projectHistory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectHistoryResource projectHistoryResource = new ProjectHistoryResource();
        ReflectionTestUtils.setField(projectHistoryResource, "projectHistoryService", projectHistoryService);
        this.restProjectHistoryMockMvc = MockMvcBuilders.standaloneSetup(projectHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectHistory createEntity(EntityManager em) {
        ProjectHistory projectHistory = new ProjectHistory()
                .date(DEFAULT_DATE)
                .workload(DEFAULT_WORKLOAD);
        return projectHistory;
    }

    @Before
    public void initTest() {
        projectHistorySearchRepository.deleteAll();
        projectHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectHistory() throws Exception {
        int databaseSizeBeforeCreate = projectHistoryRepository.findAll().size();

        // Create the ProjectHistory

        restProjectHistoryMockMvc.perform(post("/api/project-histories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectHistory)))
                .andExpect(status().isCreated());

        // Validate the ProjectHistory in the database
        List<ProjectHistory> projectHistories = projectHistoryRepository.findAll();
        assertThat(projectHistories).hasSize(databaseSizeBeforeCreate + 1);
        ProjectHistory testProjectHistory = projectHistories.get(projectHistories.size() - 1);
        assertThat(testProjectHistory.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProjectHistory.getWorkload()).isEqualTo(DEFAULT_WORKLOAD);

        // Validate the ProjectHistory in ElasticSearch
        ProjectHistory projectHistoryEs = projectHistorySearchRepository.findOne(testProjectHistory.getId());
        assertThat(projectHistoryEs).isEqualToComparingFieldByField(testProjectHistory);
    }

    @Test
    @Transactional
    public void checkWorkloadIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectHistoryRepository.findAll().size();
        // set the field null
        projectHistory.setWorkload(null);

        // Create the ProjectHistory, which fails.

        restProjectHistoryMockMvc.perform(post("/api/project-histories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectHistory)))
                .andExpect(status().isBadRequest());

        List<ProjectHistory> projectHistories = projectHistoryRepository.findAll();
        assertThat(projectHistories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectHistories() throws Exception {
        // Initialize the database
        projectHistoryRepository.saveAndFlush(projectHistory);

        // Get all the projectHistories
        restProjectHistoryMockMvc.perform(get("/api/project-histories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectHistory.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].workload").value(hasItem(DEFAULT_WORKLOAD)));
    }

    @Test
    @Transactional
    public void getProjectHistory() throws Exception {
        // Initialize the database
        projectHistoryRepository.saveAndFlush(projectHistory);

        // Get the projectHistory
        restProjectHistoryMockMvc.perform(get("/api/project-histories/{id}", projectHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectHistory.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.workload").value(DEFAULT_WORKLOAD));
    }

    @Test
    @Transactional
    public void getNonExistingProjectHistory() throws Exception {
        // Get the projectHistory
        restProjectHistoryMockMvc.perform(get("/api/project-histories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectHistory() throws Exception {
        // Initialize the database
        projectHistoryService.save(projectHistory);

        int databaseSizeBeforeUpdate = projectHistoryRepository.findAll().size();

        // Update the projectHistory
        ProjectHistory updatedProjectHistory = projectHistoryRepository.findOne(projectHistory.getId());
        updatedProjectHistory
                .date(UPDATED_DATE)
                .workload(UPDATED_WORKLOAD);

        restProjectHistoryMockMvc.perform(put("/api/project-histories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjectHistory)))
                .andExpect(status().isOk());

        // Validate the ProjectHistory in the database
        List<ProjectHistory> projectHistories = projectHistoryRepository.findAll();
        assertThat(projectHistories).hasSize(databaseSizeBeforeUpdate);
        ProjectHistory testProjectHistory = projectHistories.get(projectHistories.size() - 1);
        assertThat(testProjectHistory.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProjectHistory.getWorkload()).isEqualTo(UPDATED_WORKLOAD);

        // Validate the ProjectHistory in ElasticSearch
        ProjectHistory projectHistoryEs = projectHistorySearchRepository.findOne(testProjectHistory.getId());
        assertThat(projectHistoryEs).isEqualToComparingFieldByField(testProjectHistory);
    }

    @Test
    @Transactional
    public void deleteProjectHistory() throws Exception {
        // Initialize the database
        projectHistoryService.save(projectHistory);

        int databaseSizeBeforeDelete = projectHistoryRepository.findAll().size();

        // Get the projectHistory
        restProjectHistoryMockMvc.perform(delete("/api/project-histories/{id}", projectHistory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean projectHistoryExistsInEs = projectHistorySearchRepository.exists(projectHistory.getId());
        assertThat(projectHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjectHistory> projectHistories = projectHistoryRepository.findAll();
        assertThat(projectHistories).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjectHistory() throws Exception {
        // Initialize the database
        projectHistoryService.save(projectHistory);

        // Search the projectHistory
        restProjectHistoryMockMvc.perform(get("/api/_search/project-histories?query=id:" + projectHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].workload").value(hasItem(DEFAULT_WORKLOAD)));
    }
}
