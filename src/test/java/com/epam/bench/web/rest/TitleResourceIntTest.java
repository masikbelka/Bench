package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.Title;
import com.epam.bench.repository.TitleRepository;
import com.epam.bench.service.TitleService;
import com.epam.bench.repository.search.TitleSearchRepository;

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
 * Test class for the TitleResource REST controller.
 *
 * @see TitleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class TitleResourceIntTest {

    private static final String DEFAULT_UPSA_ID = "AAAAA";
    private static final String UPDATED_UPSA_ID = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private TitleRepository titleRepository;

    @Inject
    private TitleService titleService;

    @Inject
    private TitleSearchRepository titleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTitleMockMvc;

    private Title title;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TitleResource titleResource = new TitleResource();
        ReflectionTestUtils.setField(titleResource, "titleService", titleService);
        this.restTitleMockMvc = MockMvcBuilders.standaloneSetup(titleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Title createEntity(EntityManager em) {
        Title title = new Title()
                .upsaId(DEFAULT_UPSA_ID)
                .name(DEFAULT_NAME);
        return title;
    }

    @Before
    public void initTest() {
        titleSearchRepository.deleteAll();
        title = createEntity(em);
    }

    @Test
    @Transactional
    public void createTitle() throws Exception {
        int databaseSizeBeforeCreate = titleRepository.findAll().size();

        // Create the Title

        restTitleMockMvc.perform(post("/api/titles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(title)))
                .andExpect(status().isCreated());

        // Validate the Title in the database
        List<Title> titles = titleRepository.findAll();
        assertThat(titles).hasSize(databaseSizeBeforeCreate + 1);
        Title testTitle = titles.get(titles.size() - 1);
        assertThat(testTitle.getUpsaId()).isEqualTo(DEFAULT_UPSA_ID);
        assertThat(testTitle.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Title in ElasticSearch
        Title titleEs = titleSearchRepository.findOne(testTitle.getId());
        assertThat(titleEs).isEqualToComparingFieldByField(testTitle);
    }

    @Test
    @Transactional
    public void checkUpsaIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = titleRepository.findAll().size();
        // set the field null
        title.setUpsaId(null);

        // Create the Title, which fails.

        restTitleMockMvc.perform(post("/api/titles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(title)))
                .andExpect(status().isBadRequest());

        List<Title> titles = titleRepository.findAll();
        assertThat(titles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTitles() throws Exception {
        // Initialize the database
        titleRepository.saveAndFlush(title);

        // Get all the titles
        restTitleMockMvc.perform(get("/api/titles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(title.getId().intValue())))
                .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTitle() throws Exception {
        // Initialize the database
        titleRepository.saveAndFlush(title);

        // Get the title
        restTitleMockMvc.perform(get("/api/titles/{id}", title.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(title.getId().intValue()))
            .andExpect(jsonPath("$.upsaId").value(DEFAULT_UPSA_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTitle() throws Exception {
        // Get the title
        restTitleMockMvc.perform(get("/api/titles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTitle() throws Exception {
        // Initialize the database
        titleService.save(title);

        int databaseSizeBeforeUpdate = titleRepository.findAll().size();

        // Update the title
        Title updatedTitle = titleRepository.findOne(title.getId());
        updatedTitle
                .upsaId(UPDATED_UPSA_ID)
                .name(UPDATED_NAME);

        restTitleMockMvc.perform(put("/api/titles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTitle)))
                .andExpect(status().isOk());

        // Validate the Title in the database
        List<Title> titles = titleRepository.findAll();
        assertThat(titles).hasSize(databaseSizeBeforeUpdate);
        Title testTitle = titles.get(titles.size() - 1);
        assertThat(testTitle.getUpsaId()).isEqualTo(UPDATED_UPSA_ID);
        assertThat(testTitle.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Title in ElasticSearch
        Title titleEs = titleSearchRepository.findOne(testTitle.getId());
        assertThat(titleEs).isEqualToComparingFieldByField(testTitle);
    }

    @Test
    @Transactional
    public void deleteTitle() throws Exception {
        // Initialize the database
        titleService.save(title);

        int databaseSizeBeforeDelete = titleRepository.findAll().size();

        // Get the title
        restTitleMockMvc.perform(delete("/api/titles/{id}", title.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean titleExistsInEs = titleSearchRepository.exists(title.getId());
        assertThat(titleExistsInEs).isFalse();

        // Validate the database is empty
        List<Title> titles = titleRepository.findAll();
        assertThat(titles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTitle() throws Exception {
        // Initialize the database
        titleService.save(title);

        // Search the title
        restTitleMockMvc.perform(get("/api/_search/titles?query=id:" + title.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(title.getId().intValue())))
            .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
