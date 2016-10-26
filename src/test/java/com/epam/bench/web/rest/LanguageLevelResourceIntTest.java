package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.LanguageLevel;
import com.epam.bench.repository.LanguageLevelRepository;
import com.epam.bench.service.LanguageLevelService;
import com.epam.bench.repository.search.LanguageLevelSearchRepository;

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
 * Test class for the LanguageLevelResource REST controller.
 *
 * @see LanguageLevelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class LanguageLevelResourceIntTest {

    private static final String DEFAULT_LANGUAGE = "AAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBB";

    private static final String DEFAULT_SPEAKING = "AAAAA";
    private static final String UPDATED_SPEAKING = "BBBBB";

    private static final String DEFAULT_WRITING = "AAAAA";
    private static final String UPDATED_WRITING = "BBBBB";

    @Inject
    private LanguageLevelRepository languageLevelRepository;

    @Inject
    private LanguageLevelService languageLevelService;

    @Inject
    private LanguageLevelSearchRepository languageLevelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLanguageLevelMockMvc;

    private LanguageLevel languageLevel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LanguageLevelResource languageLevelResource = new LanguageLevelResource();
        ReflectionTestUtils.setField(languageLevelResource, "languageLevelService", languageLevelService);
        this.restLanguageLevelMockMvc = MockMvcBuilders.standaloneSetup(languageLevelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LanguageLevel createEntity(EntityManager em) {
        LanguageLevel languageLevel = new LanguageLevel()
                .language(DEFAULT_LANGUAGE)
                .speaking(DEFAULT_SPEAKING)
                .writing(DEFAULT_WRITING);
        return languageLevel;
    }

    @Before
    public void initTest() {
        languageLevelSearchRepository.deleteAll();
        languageLevel = createEntity(em);
    }

    @Test
    @Transactional
    public void createLanguageLevel() throws Exception {
        int databaseSizeBeforeCreate = languageLevelRepository.findAll().size();

        // Create the LanguageLevel

        restLanguageLevelMockMvc.perform(post("/api/language-levels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(languageLevel)))
                .andExpect(status().isCreated());

        // Validate the LanguageLevel in the database
        List<LanguageLevel> languageLevels = languageLevelRepository.findAll();
        assertThat(languageLevels).hasSize(databaseSizeBeforeCreate + 1);
        LanguageLevel testLanguageLevel = languageLevels.get(languageLevels.size() - 1);
        assertThat(testLanguageLevel.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testLanguageLevel.getSpeaking()).isEqualTo(DEFAULT_SPEAKING);
        assertThat(testLanguageLevel.getWriting()).isEqualTo(DEFAULT_WRITING);

        // Validate the LanguageLevel in ElasticSearch
        LanguageLevel languageLevelEs = languageLevelSearchRepository.findOne(testLanguageLevel.getId());
        assertThat(languageLevelEs).isEqualToComparingFieldByField(testLanguageLevel);
    }

    @Test
    @Transactional
    public void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = languageLevelRepository.findAll().size();
        // set the field null
        languageLevel.setLanguage(null);

        // Create the LanguageLevel, which fails.

        restLanguageLevelMockMvc.perform(post("/api/language-levels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(languageLevel)))
                .andExpect(status().isBadRequest());

        List<LanguageLevel> languageLevels = languageLevelRepository.findAll();
        assertThat(languageLevels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLanguageLevels() throws Exception {
        // Initialize the database
        languageLevelRepository.saveAndFlush(languageLevel);

        // Get all the languageLevels
        restLanguageLevelMockMvc.perform(get("/api/language-levels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(languageLevel.getId().intValue())))
                .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
                .andExpect(jsonPath("$.[*].speaking").value(hasItem(DEFAULT_SPEAKING.toString())))
                .andExpect(jsonPath("$.[*].writing").value(hasItem(DEFAULT_WRITING.toString())));
    }

    @Test
    @Transactional
    public void getLanguageLevel() throws Exception {
        // Initialize the database
        languageLevelRepository.saveAndFlush(languageLevel);

        // Get the languageLevel
        restLanguageLevelMockMvc.perform(get("/api/language-levels/{id}", languageLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(languageLevel.getId().intValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.speaking").value(DEFAULT_SPEAKING.toString()))
            .andExpect(jsonPath("$.writing").value(DEFAULT_WRITING.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLanguageLevel() throws Exception {
        // Get the languageLevel
        restLanguageLevelMockMvc.perform(get("/api/language-levels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLanguageLevel() throws Exception {
        // Initialize the database
        languageLevelService.save(languageLevel);

        int databaseSizeBeforeUpdate = languageLevelRepository.findAll().size();

        // Update the languageLevel
        LanguageLevel updatedLanguageLevel = languageLevelRepository.findOne(languageLevel.getId());
        updatedLanguageLevel
                .language(UPDATED_LANGUAGE)
                .speaking(UPDATED_SPEAKING)
                .writing(UPDATED_WRITING);

        restLanguageLevelMockMvc.perform(put("/api/language-levels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLanguageLevel)))
                .andExpect(status().isOk());

        // Validate the LanguageLevel in the database
        List<LanguageLevel> languageLevels = languageLevelRepository.findAll();
        assertThat(languageLevels).hasSize(databaseSizeBeforeUpdate);
        LanguageLevel testLanguageLevel = languageLevels.get(languageLevels.size() - 1);
        assertThat(testLanguageLevel.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testLanguageLevel.getSpeaking()).isEqualTo(UPDATED_SPEAKING);
        assertThat(testLanguageLevel.getWriting()).isEqualTo(UPDATED_WRITING);

        // Validate the LanguageLevel in ElasticSearch
        LanguageLevel languageLevelEs = languageLevelSearchRepository.findOne(testLanguageLevel.getId());
        assertThat(languageLevelEs).isEqualToComparingFieldByField(testLanguageLevel);
    }

    @Test
    @Transactional
    public void deleteLanguageLevel() throws Exception {
        // Initialize the database
        languageLevelService.save(languageLevel);

        int databaseSizeBeforeDelete = languageLevelRepository.findAll().size();

        // Get the languageLevel
        restLanguageLevelMockMvc.perform(delete("/api/language-levels/{id}", languageLevel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean languageLevelExistsInEs = languageLevelSearchRepository.exists(languageLevel.getId());
        assertThat(languageLevelExistsInEs).isFalse();

        // Validate the database is empty
        List<LanguageLevel> languageLevels = languageLevelRepository.findAll();
        assertThat(languageLevels).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLanguageLevel() throws Exception {
        // Initialize the database
        languageLevelService.save(languageLevel);

        // Search the languageLevel
        restLanguageLevelMockMvc.perform(get("/api/_search/language-levels?query=id:" + languageLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(languageLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].speaking").value(hasItem(DEFAULT_SPEAKING.toString())))
            .andExpect(jsonPath("$.[*].writing").value(hasItem(DEFAULT_WRITING.toString())));
    }
}
