package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.PrimarySkill;
import com.epam.bench.repository.PrimarySkillRepository;
import com.epam.bench.service.PrimarySkillService;
import com.epam.bench.repository.search.PrimarySkillSearchRepository;

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
 * Test class for the PrimarySkillResource REST controller.
 *
 * @see PrimarySkillResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class PrimarySkillResourceIntTest {

    private static final String DEFAULT_UPSA_ID = "AAAAA";
    private static final String UPDATED_UPSA_ID = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private PrimarySkillRepository primarySkillRepository;

    @Inject
    private PrimarySkillService primarySkillService;

    @Inject
    private PrimarySkillSearchRepository primarySkillSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPrimarySkillMockMvc;

    private PrimarySkill primarySkill;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrimarySkillResource primarySkillResource = new PrimarySkillResource();
        ReflectionTestUtils.setField(primarySkillResource, "primarySkillService", primarySkillService);
        this.restPrimarySkillMockMvc = MockMvcBuilders.standaloneSetup(primarySkillResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrimarySkill createEntity(EntityManager em) {
        PrimarySkill primarySkill = new PrimarySkill()
                .upsaId(DEFAULT_UPSA_ID)
                .name(DEFAULT_NAME);
        return primarySkill;
    }

    @Before
    public void initTest() {
        primarySkillSearchRepository.deleteAll();
        primarySkill = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrimarySkill() throws Exception {
        int databaseSizeBeforeCreate = primarySkillRepository.findAll().size();

        // Create the PrimarySkill

        restPrimarySkillMockMvc.perform(post("/api/primary-skills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(primarySkill)))
                .andExpect(status().isCreated());

        // Validate the PrimarySkill in the database
        List<PrimarySkill> primarySkills = primarySkillRepository.findAll();
        assertThat(primarySkills).hasSize(databaseSizeBeforeCreate + 1);
        PrimarySkill testPrimarySkill = primarySkills.get(primarySkills.size() - 1);
        assertThat(testPrimarySkill.getUpsaId()).isEqualTo(DEFAULT_UPSA_ID);
        assertThat(testPrimarySkill.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the PrimarySkill in ElasticSearch
        PrimarySkill primarySkillEs = primarySkillSearchRepository.findOne(testPrimarySkill.getId());
        assertThat(primarySkillEs).isEqualToComparingFieldByField(testPrimarySkill);
    }

    @Test
    @Transactional
    public void checkUpsaIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = primarySkillRepository.findAll().size();
        // set the field null
        primarySkill.setUpsaId(null);

        // Create the PrimarySkill, which fails.

        restPrimarySkillMockMvc.perform(post("/api/primary-skills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(primarySkill)))
                .andExpect(status().isBadRequest());

        List<PrimarySkill> primarySkills = primarySkillRepository.findAll();
        assertThat(primarySkills).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = primarySkillRepository.findAll().size();
        // set the field null
        primarySkill.setName(null);

        // Create the PrimarySkill, which fails.

        restPrimarySkillMockMvc.perform(post("/api/primary-skills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(primarySkill)))
                .andExpect(status().isBadRequest());

        List<PrimarySkill> primarySkills = primarySkillRepository.findAll();
        assertThat(primarySkills).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrimarySkills() throws Exception {
        // Initialize the database
        primarySkillRepository.saveAndFlush(primarySkill);

        // Get all the primarySkills
        restPrimarySkillMockMvc.perform(get("/api/primary-skills?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(primarySkill.getId().intValue())))
                .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPrimarySkill() throws Exception {
        // Initialize the database
        primarySkillRepository.saveAndFlush(primarySkill);

        // Get the primarySkill
        restPrimarySkillMockMvc.perform(get("/api/primary-skills/{id}", primarySkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(primarySkill.getId().intValue()))
            .andExpect(jsonPath("$.upsaId").value(DEFAULT_UPSA_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrimarySkill() throws Exception {
        // Get the primarySkill
        restPrimarySkillMockMvc.perform(get("/api/primary-skills/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrimarySkill() throws Exception {
        // Initialize the database
        primarySkillService.save(primarySkill);

        int databaseSizeBeforeUpdate = primarySkillRepository.findAll().size();

        // Update the primarySkill
        PrimarySkill updatedPrimarySkill = primarySkillRepository.findOne(primarySkill.getId());
        updatedPrimarySkill
                .upsaId(UPDATED_UPSA_ID)
                .name(UPDATED_NAME);

        restPrimarySkillMockMvc.perform(put("/api/primary-skills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPrimarySkill)))
                .andExpect(status().isOk());

        // Validate the PrimarySkill in the database
        List<PrimarySkill> primarySkills = primarySkillRepository.findAll();
        assertThat(primarySkills).hasSize(databaseSizeBeforeUpdate);
        PrimarySkill testPrimarySkill = primarySkills.get(primarySkills.size() - 1);
        assertThat(testPrimarySkill.getUpsaId()).isEqualTo(UPDATED_UPSA_ID);
        assertThat(testPrimarySkill.getName()).isEqualTo(UPDATED_NAME);

        // Validate the PrimarySkill in ElasticSearch
        PrimarySkill primarySkillEs = primarySkillSearchRepository.findOne(testPrimarySkill.getId());
        assertThat(primarySkillEs).isEqualToComparingFieldByField(testPrimarySkill);
    }

    @Test
    @Transactional
    public void deletePrimarySkill() throws Exception {
        // Initialize the database
        primarySkillService.save(primarySkill);

        int databaseSizeBeforeDelete = primarySkillRepository.findAll().size();

        // Get the primarySkill
        restPrimarySkillMockMvc.perform(delete("/api/primary-skills/{id}", primarySkill.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean primarySkillExistsInEs = primarySkillSearchRepository.exists(primarySkill.getId());
        assertThat(primarySkillExistsInEs).isFalse();

        // Validate the database is empty
        List<PrimarySkill> primarySkills = primarySkillRepository.findAll();
        assertThat(primarySkills).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPrimarySkill() throws Exception {
        // Initialize the database
        primarySkillService.save(primarySkill);

        // Search the primarySkill
        restPrimarySkillMockMvc.perform(get("/api/_search/primary-skills?query=id:" + primarySkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(primarySkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
