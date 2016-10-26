package com.epam.bench.service.impl;

import com.epam.bench.service.PrimarySkillService;
import com.epam.bench.domain.PrimarySkill;
import com.epam.bench.repository.PrimarySkillRepository;
import com.epam.bench.repository.search.PrimarySkillSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PrimarySkill.
 */
@Service
@Transactional
public class PrimarySkillServiceImpl implements PrimarySkillService{

    private final Logger log = LoggerFactory.getLogger(PrimarySkillServiceImpl.class);
    
    @Inject
    private PrimarySkillRepository primarySkillRepository;

    @Inject
    private PrimarySkillSearchRepository primarySkillSearchRepository;

    /**
     * Save a primarySkill.
     *
     * @param primarySkill the entity to save
     * @return the persisted entity
     */
    public PrimarySkill save(PrimarySkill primarySkill) {
        log.debug("Request to save PrimarySkill : {}", primarySkill);
        PrimarySkill result = primarySkillRepository.save(primarySkill);
        primarySkillSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the primarySkills.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PrimarySkill> findAll(Pageable pageable) {
        log.debug("Request to get all PrimarySkills");
        Page<PrimarySkill> result = primarySkillRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one primarySkill by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PrimarySkill findOne(Long id) {
        log.debug("Request to get PrimarySkill : {}", id);
        PrimarySkill primarySkill = primarySkillRepository.findOne(id);
        return primarySkill;
    }

    /**
     *  Delete the  primarySkill by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PrimarySkill : {}", id);
        primarySkillRepository.delete(id);
        primarySkillSearchRepository.delete(id);
    }

    /**
     * Search for the primarySkill corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PrimarySkill> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrimarySkills for query {}", query);
        Page<PrimarySkill> result = primarySkillSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
