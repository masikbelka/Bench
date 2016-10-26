package com.epam.bench.web.rest;

import com.epam.bench.BenchApp;

import com.epam.bench.domain.Unit;
import com.epam.bench.repository.UnitRepository;
import com.epam.bench.service.UnitService;
import com.epam.bench.repository.search.UnitSearchRepository;

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
 * Test class for the UnitResource REST controller.
 *
 * @see UnitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchApp.class)
public class UnitResourceIntTest {

    private static final String DEFAULT_UPSA_ID = "AAAAA";
    private static final String UPDATED_UPSA_ID = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private UnitRepository unitRepository;

    @Inject
    private UnitService unitService;

    @Inject
    private UnitSearchRepository unitSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUnitMockMvc;

    private Unit unit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UnitResource unitResource = new UnitResource();
        ReflectionTestUtils.setField(unitResource, "unitService", unitService);
        this.restUnitMockMvc = MockMvcBuilders.standaloneSetup(unitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unit createEntity(EntityManager em) {
        Unit unit = new Unit()
                .upsaId(DEFAULT_UPSA_ID)
                .name(DEFAULT_NAME);
        return unit;
    }

    @Before
    public void initTest() {
        unitSearchRepository.deleteAll();
        unit = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnit() throws Exception {
        int databaseSizeBeforeCreate = unitRepository.findAll().size();

        // Create the Unit

        restUnitMockMvc.perform(post("/api/units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unit)))
                .andExpect(status().isCreated());

        // Validate the Unit in the database
        List<Unit> units = unitRepository.findAll();
        assertThat(units).hasSize(databaseSizeBeforeCreate + 1);
        Unit testUnit = units.get(units.size() - 1);
        assertThat(testUnit.getUpsaId()).isEqualTo(DEFAULT_UPSA_ID);
        assertThat(testUnit.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Unit in ElasticSearch
        Unit unitEs = unitSearchRepository.findOne(testUnit.getId());
        assertThat(unitEs).isEqualToComparingFieldByField(testUnit);
    }

    @Test
    @Transactional
    public void checkUpsaIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitRepository.findAll().size();
        // set the field null
        unit.setUpsaId(null);

        // Create the Unit, which fails.

        restUnitMockMvc.perform(post("/api/units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unit)))
                .andExpect(status().isBadRequest());

        List<Unit> units = unitRepository.findAll();
        assertThat(units).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitRepository.findAll().size();
        // set the field null
        unit.setName(null);

        // Create the Unit, which fails.

        restUnitMockMvc.perform(post("/api/units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unit)))
                .andExpect(status().isBadRequest());

        List<Unit> units = unitRepository.findAll();
        assertThat(units).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUnits() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        // Get all the units
        restUnitMockMvc.perform(get("/api/units?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(unit.getId().intValue())))
                .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        // Get the unit
        restUnitMockMvc.perform(get("/api/units/{id}", unit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unit.getId().intValue()))
            .andExpect(jsonPath("$.upsaId").value(DEFAULT_UPSA_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUnit() throws Exception {
        // Get the unit
        restUnitMockMvc.perform(get("/api/units/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnit() throws Exception {
        // Initialize the database
        unitService.save(unit);

        int databaseSizeBeforeUpdate = unitRepository.findAll().size();

        // Update the unit
        Unit updatedUnit = unitRepository.findOne(unit.getId());
        updatedUnit
                .upsaId(UPDATED_UPSA_ID)
                .name(UPDATED_NAME);

        restUnitMockMvc.perform(put("/api/units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUnit)))
                .andExpect(status().isOk());

        // Validate the Unit in the database
        List<Unit> units = unitRepository.findAll();
        assertThat(units).hasSize(databaseSizeBeforeUpdate);
        Unit testUnit = units.get(units.size() - 1);
        assertThat(testUnit.getUpsaId()).isEqualTo(UPDATED_UPSA_ID);
        assertThat(testUnit.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Unit in ElasticSearch
        Unit unitEs = unitSearchRepository.findOne(testUnit.getId());
        assertThat(unitEs).isEqualToComparingFieldByField(testUnit);
    }

    @Test
    @Transactional
    public void deleteUnit() throws Exception {
        // Initialize the database
        unitService.save(unit);

        int databaseSizeBeforeDelete = unitRepository.findAll().size();

        // Get the unit
        restUnitMockMvc.perform(delete("/api/units/{id}", unit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean unitExistsInEs = unitSearchRepository.exists(unit.getId());
        assertThat(unitExistsInEs).isFalse();

        // Validate the database is empty
        List<Unit> units = unitRepository.findAll();
        assertThat(units).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUnit() throws Exception {
        // Initialize the database
        unitService.save(unit);

        // Search the unit
        restUnitMockMvc.perform(get("/api/_search/units?query=id:" + unit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unit.getId().intValue())))
            .andExpect(jsonPath("$.[*].upsaId").value(hasItem(DEFAULT_UPSA_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
