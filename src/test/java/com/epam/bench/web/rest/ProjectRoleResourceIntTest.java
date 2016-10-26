package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.ProjectRole;
import com.epam.bench.repository.ProjectRoleRepository;
import com.epam.bench.service.ProjectRoleService;
import com.epam.bench.repository.search.ProjectRoleSearchRepository;

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
 * Test class for the ProjectRoleResource REST controller.
 *
 * @see ProjectRoleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class ProjectRoleResourceIntTest {

    private static final String DEFAULT_UPSA_ID = "AAAAA";
    private static final String UPDATED_UPSA_ID = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private ProjectRoleRepository projectRoleRepository;

    @Inject
    private ProjectRoleService projectRoleService;

    @Inject
    private ProjectRoleSearchRepository projectRoleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectRoleMockMvc;

    private ProjectRole projectRole;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectRoleResource projectRoleResource = new ProjectRoleResource();
        ReflectionTestUtils.setField(projectRoleResource, "projectRoleService", projectRoleService);
        this.restProjectRoleMockMvc = MockMvcBuilders.standaloneSetup(projectRoleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectRole createEntity(EntityManager em) {
        ProjectRole projectRole = new ProjectRole()
                .upsaId(DEFAULT_UPSA_ID)
                .name(DEFAULT_NAME);
        return projectRole;
    }

    @Before
    public void initTest() {
        projectRoleSearchRepository.deleteAll();
        projectRole = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectRole() throws Exception {
        int databaseSizeBeforeCreate = projectRoleRepository.findAll().size();

        // Create the ProjectRole

        restProjectRoleMockMvc.perform(post("/api/project-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectRole)))
                .andExpect(status().isCreated());

        // Validate the ProjectRole in the database
        List<ProjectRole> projectRoles = projectRoleRepository.findAll();
        assertThat(projectRoles).hasSize(databaseSizeBeforeCreate + 1);
        ProjectRole testProjectRole = projectRoles.get(projectRoles.size() - 1);
        assertThat(testProjectRole.getUpsaId()).isEqualTo(DEFAULT_UPSA_ID);
        assertThat(testProjectRole.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the ProjectRole in ElasticSearch
        ProjectRole projectRoleEs = projectRoleSearchRepository.findOne(testProjectRole.getId());
        assertThat(projectRoleEs).isEqualToComparingFieldByField(testProjectRole);
    }

    @Test
    @Transactional
    public void checkUpsaIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRoleRepository.findAll().size();
        // set the field null
        projectRole.setUpsaId(null);

        // Create the ProjectRole, which fails.

        restProjectRoleMockMvc.perform(post("/api/project-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectRole)))
                .andExpect(status().isBadRequest());

        List<ProjectRole> projectRoles = projectRoleRepository.findAll();
        assertThat(projectRoles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectRoles() throws Exception {
        // Initialize the database
        projectRoleRepository.saveAndFlush(projectRole);

        // Get all the projectRoles
        restProjectRoleMockMvc.perform(get("/api/project-roles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectRole.getId().intValue())))
                .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getProjectRole() throws Exception {
        // Initialize the database
        projectRoleRepository.saveAndFlush(projectRole);

        // Get the projectRole
        restProjectRoleMockMvc.perform(get("/api/project-roles/{id}", projectRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectRole.getId().intValue()))
            .andExpect(jsonPath("$.upsaId").value(DEFAULT_UPSA_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectRole() throws Exception {
        // Get the projectRole
        restProjectRoleMockMvc.perform(get("/api/project-roles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectRole() throws Exception {
        // Initialize the database
        projectRoleService.save(projectRole);

        int databaseSizeBeforeUpdate = projectRoleRepository.findAll().size();

        // Update the projectRole
        ProjectRole updatedProjectRole = projectRoleRepository.findOne(projectRole.getId());
        updatedProjectRole
                .upsaId(UPDATED_UPSA_ID)
                .name(UPDATED_NAME);

        restProjectRoleMockMvc.perform(put("/api/project-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjectRole)))
                .andExpect(status().isOk());

        // Validate the ProjectRole in the database
        List<ProjectRole> projectRoles = projectRoleRepository.findAll();
        assertThat(projectRoles).hasSize(databaseSizeBeforeUpdate);
        ProjectRole testProjectRole = projectRoles.get(projectRoles.size() - 1);
        assertThat(testProjectRole.getUpsaId()).isEqualTo(UPDATED_UPSA_ID);
        assertThat(testProjectRole.getName()).isEqualTo(UPDATED_NAME);

        // Validate the ProjectRole in ElasticSearch
        ProjectRole projectRoleEs = projectRoleSearchRepository.findOne(testProjectRole.getId());
        assertThat(projectRoleEs).isEqualToComparingFieldByField(testProjectRole);
    }

    @Test
    @Transactional
    public void deleteProjectRole() throws Exception {
        // Initialize the database
        projectRoleService.save(projectRole);

        int databaseSizeBeforeDelete = projectRoleRepository.findAll().size();

        // Get the projectRole
        restProjectRoleMockMvc.perform(delete("/api/project-roles/{id}", projectRole.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean projectRoleExistsInEs = projectRoleSearchRepository.exists(projectRole.getId());
        assertThat(projectRoleExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjectRole> projectRoles = projectRoleRepository.findAll();
        assertThat(projectRoles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjectRole() throws Exception {
        // Initialize the database
        projectRoleService.save(projectRole);

        // Search the projectRole
        restProjectRoleMockMvc.perform(get("/api/_search/project-roles?query=id:" + projectRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
