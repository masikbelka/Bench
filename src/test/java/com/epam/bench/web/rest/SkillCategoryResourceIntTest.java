package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.SkillCategory;
import com.epam.bench.repository.SkillCategoryRepository;
import com.epam.bench.service.SkillCategoryService;
import com.epam.bench.repository.search.SkillCategorySearchRepository;

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
 * Test class for the SkillCategoryResource REST controller.
 *
 * @see SkillCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class SkillCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_COLOR = "AAAAA";
    private static final String UPDATED_COLOR = "BBBBB";

    @Inject
    private SkillCategoryRepository skillCategoryRepository;

    @Inject
    private SkillCategoryService skillCategoryService;

    @Inject
    private SkillCategorySearchRepository skillCategorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSkillCategoryMockMvc;

    private SkillCategory skillCategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SkillCategoryResource skillCategoryResource = new SkillCategoryResource();
        ReflectionTestUtils.setField(skillCategoryResource, "skillCategoryService", skillCategoryService);
        this.restSkillCategoryMockMvc = MockMvcBuilders.standaloneSetup(skillCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillCategory createEntity(EntityManager em) {
        SkillCategory skillCategory = new SkillCategory()
                .name(DEFAULT_NAME)
                .color(DEFAULT_COLOR);
        return skillCategory;
    }

    @Before
    public void initTest() {
        skillCategorySearchRepository.deleteAll();
        skillCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createSkillCategory() throws Exception {
        int databaseSizeBeforeCreate = skillCategoryRepository.findAll().size();

        // Create the SkillCategory

        restSkillCategoryMockMvc.perform(post("/api/skill-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(skillCategory)))
                .andExpect(status().isCreated());

        // Validate the SkillCategory in the database
        List<SkillCategory> skillCategories = skillCategoryRepository.findAll();
        assertThat(skillCategories).hasSize(databaseSizeBeforeCreate + 1);
        SkillCategory testSkillCategory = skillCategories.get(skillCategories.size() - 1);
        assertThat(testSkillCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSkillCategory.getColor()).isEqualTo(DEFAULT_COLOR);

        // Validate the SkillCategory in ElasticSearch
        SkillCategory skillCategoryEs = skillCategorySearchRepository.findOne(testSkillCategory.getId());
        assertThat(skillCategoryEs).isEqualToComparingFieldByField(testSkillCategory);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillCategoryRepository.findAll().size();
        // set the field null
        skillCategory.setName(null);

        // Create the SkillCategory, which fails.

        restSkillCategoryMockMvc.perform(post("/api/skill-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(skillCategory)))
                .andExpect(status().isBadRequest());

        List<SkillCategory> skillCategories = skillCategoryRepository.findAll();
        assertThat(skillCategories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSkillCategories() throws Exception {
        // Initialize the database
        skillCategoryRepository.saveAndFlush(skillCategory);

        // Get all the skillCategories
        restSkillCategoryMockMvc.perform(get("/api/skill-categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(skillCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));
    }

    @Test
    @Transactional
    public void getSkillCategory() throws Exception {
        // Initialize the database
        skillCategoryRepository.saveAndFlush(skillCategory);

        // Get the skillCategory
        restSkillCategoryMockMvc.perform(get("/api/skill-categories/{id}", skillCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(skillCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSkillCategory() throws Exception {
        // Get the skillCategory
        restSkillCategoryMockMvc.perform(get("/api/skill-categories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkillCategory() throws Exception {
        // Initialize the database
        skillCategoryService.save(skillCategory);

        int databaseSizeBeforeUpdate = skillCategoryRepository.findAll().size();

        // Update the skillCategory
        SkillCategory updatedSkillCategory = skillCategoryRepository.findOne(skillCategory.getId());
        updatedSkillCategory
                .name(UPDATED_NAME)
                .color(UPDATED_COLOR);

        restSkillCategoryMockMvc.perform(put("/api/skill-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSkillCategory)))
                .andExpect(status().isOk());

        // Validate the SkillCategory in the database
        List<SkillCategory> skillCategories = skillCategoryRepository.findAll();
        assertThat(skillCategories).hasSize(databaseSizeBeforeUpdate);
        SkillCategory testSkillCategory = skillCategories.get(skillCategories.size() - 1);
        assertThat(testSkillCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSkillCategory.getColor()).isEqualTo(UPDATED_COLOR);

        // Validate the SkillCategory in ElasticSearch
        SkillCategory skillCategoryEs = skillCategorySearchRepository.findOne(testSkillCategory.getId());
        assertThat(skillCategoryEs).isEqualToComparingFieldByField(testSkillCategory);
    }

    @Test
    @Transactional
    public void deleteSkillCategory() throws Exception {
        // Initialize the database
        skillCategoryService.save(skillCategory);

        int databaseSizeBeforeDelete = skillCategoryRepository.findAll().size();

        // Get the skillCategory
        restSkillCategoryMockMvc.perform(delete("/api/skill-categories/{id}", skillCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean skillCategoryExistsInEs = skillCategorySearchRepository.exists(skillCategory.getId());
        assertThat(skillCategoryExistsInEs).isFalse();

        // Validate the database is empty
        List<SkillCategory> skillCategories = skillCategoryRepository.findAll();
        assertThat(skillCategories).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSkillCategory() throws Exception {
        // Initialize the database
        skillCategoryService.save(skillCategory);

        // Search the skillCategory
        restSkillCategoryMockMvc.perform(get("/api/_search/skill-categories?query=id:" + skillCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));
    }
}
