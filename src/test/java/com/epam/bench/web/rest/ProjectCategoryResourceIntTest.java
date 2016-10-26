package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.ProjectCategory;
import com.epam.bench.repository.ProjectCategoryRepository;
import com.epam.bench.service.ProjectCategoryService;
import com.epam.bench.repository.search.ProjectCategorySearchRepository;

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
 * Test class for the ProjectCategoryResource REST controller.
 *
 * @see ProjectCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class ProjectCategoryResourceIntTest {

    private static final String DEFAULT_UPSA_ID = "AAAAA";
    private static final String UPDATED_UPSA_ID = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private ProjectCategoryRepository projectCategoryRepository;

    @Inject
    private ProjectCategoryService projectCategoryService;

    @Inject
    private ProjectCategorySearchRepository projectCategorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectCategoryMockMvc;

    private ProjectCategory projectCategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectCategoryResource projectCategoryResource = new ProjectCategoryResource();
        ReflectionTestUtils.setField(projectCategoryResource, "projectCategoryService", projectCategoryService);
        this.restProjectCategoryMockMvc = MockMvcBuilders.standaloneSetup(projectCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectCategory createEntity(EntityManager em) {
        ProjectCategory projectCategory = new ProjectCategory()
                .upsaId(DEFAULT_UPSA_ID)
                .name(DEFAULT_NAME);
        return projectCategory;
    }

    @Before
    public void initTest() {
        projectCategorySearchRepository.deleteAll();
        projectCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectCategory() throws Exception {
        int databaseSizeBeforeCreate = projectCategoryRepository.findAll().size();

        // Create the ProjectCategory

        restProjectCategoryMockMvc.perform(post("/api/project-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectCategory)))
                .andExpect(status().isCreated());

        // Validate the ProjectCategory in the database
        List<ProjectCategory> projectCategories = projectCategoryRepository.findAll();
        assertThat(projectCategories).hasSize(databaseSizeBeforeCreate + 1);
        ProjectCategory testProjectCategory = projectCategories.get(projectCategories.size() - 1);
        assertThat(testProjectCategory.getUpsaId()).isEqualTo(DEFAULT_UPSA_ID);
        assertThat(testProjectCategory.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the ProjectCategory in ElasticSearch
        ProjectCategory projectCategoryEs = projectCategorySearchRepository.findOne(testProjectCategory.getId());
        assertThat(projectCategoryEs).isEqualToComparingFieldByField(testProjectCategory);
    }

    @Test
    @Transactional
    public void checkUpsaIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectCategoryRepository.findAll().size();
        // set the field null
        projectCategory.setUpsaId(null);

        // Create the ProjectCategory, which fails.

        restProjectCategoryMockMvc.perform(post("/api/project-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectCategory)))
                .andExpect(status().isBadRequest());

        List<ProjectCategory> projectCategories = projectCategoryRepository.findAll();
        assertThat(projectCategories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectCategories() throws Exception {
        // Initialize the database
        projectCategoryRepository.saveAndFlush(projectCategory);

        // Get all the projectCategories
        restProjectCategoryMockMvc.perform(get("/api/project-categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getProjectCategory() throws Exception {
        // Initialize the database
        projectCategoryRepository.saveAndFlush(projectCategory);

        // Get the projectCategory
        restProjectCategoryMockMvc.perform(get("/api/project-categories/{id}", projectCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectCategory.getId().intValue()))
            .andExpect(jsonPath("$.upsaId").value(DEFAULT_UPSA_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectCategory() throws Exception {
        // Get the projectCategory
        restProjectCategoryMockMvc.perform(get("/api/project-categories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectCategory() throws Exception {
        // Initialize the database
        projectCategoryService.save(projectCategory);

        int databaseSizeBeforeUpdate = projectCategoryRepository.findAll().size();

        // Update the projectCategory
        ProjectCategory updatedProjectCategory = projectCategoryRepository.findOne(projectCategory.getId());
        updatedProjectCategory
                .upsaId(UPDATED_UPSA_ID)
                .name(UPDATED_NAME);

        restProjectCategoryMockMvc.perform(put("/api/project-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjectCategory)))
                .andExpect(status().isOk());

        // Validate the ProjectCategory in the database
        List<ProjectCategory> projectCategories = projectCategoryRepository.findAll();
        assertThat(projectCategories).hasSize(databaseSizeBeforeUpdate);
        ProjectCategory testProjectCategory = projectCategories.get(projectCategories.size() - 1);
        assertThat(testProjectCategory.getUpsaId()).isEqualTo(UPDATED_UPSA_ID);
        assertThat(testProjectCategory.getName()).isEqualTo(UPDATED_NAME);

        // Validate the ProjectCategory in ElasticSearch
        ProjectCategory projectCategoryEs = projectCategorySearchRepository.findOne(testProjectCategory.getId());
        assertThat(projectCategoryEs).isEqualToComparingFieldByField(testProjectCategory);
    }

    @Test
    @Transactional
    public void deleteProjectCategory() throws Exception {
        // Initialize the database
        projectCategoryService.save(projectCategory);

        int databaseSizeBeforeDelete = projectCategoryRepository.findAll().size();

        // Get the projectCategory
        restProjectCategoryMockMvc.perform(delete("/api/project-categories/{id}", projectCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean projectCategoryExistsInEs = projectCategorySearchRepository.exists(projectCategory.getId());
        assertThat(projectCategoryExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjectCategory> projectCategories = projectCategoryRepository.findAll();
        assertThat(projectCategories).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjectCategory() throws Exception {
        // Initialize the database
        projectCategoryService.save(projectCategory);

        // Search the projectCategory
        restProjectCategoryMockMvc.perform(get("/api/_search/project-categories?query=id:" + projectCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
