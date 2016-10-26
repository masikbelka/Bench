package com.epam.bench.service.impl;

import com.epam.bench.service.SkillCategoryService;
import com.epam.bench.domain.SkillCategory;
import com.epam.bench.repository.SkillCategoryRepository;
import com.epam.bench.repository.search.SkillCategorySearchRepository;
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
 * Service Implementation for managing SkillCategory.
 */
@Service
@Transactional
public class SkillCategoryServiceImpl implements SkillCategoryService{

    private final Logger log = LoggerFactory.getLogger(SkillCategoryServiceImpl.class);
    
    @Inject
    private SkillCategoryRepository skillCategoryRepository;

    @Inject
    private SkillCategorySearchRepository skillCategorySearchRepository;

    /**
     * Save a skillCategory.
     *
     * @param skillCategory the entity to save
     * @return the persisted entity
     */
    public SkillCategory save(SkillCategory skillCategory) {
        log.debug("Request to save SkillCategory : {}", skillCategory);
        SkillCategory result = skillCategoryRepository.save(skillCategory);
        skillCategorySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the skillCategories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<SkillCategory> findAll(Pageable pageable) {
        log.debug("Request to get all SkillCategories");
        Page<SkillCategory> result = skillCategoryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one skillCategory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SkillCategory findOne(Long id) {
        log.debug("Request to get SkillCategory : {}", id);
        SkillCategory skillCategory = skillCategoryRepository.findOne(id);
        return skillCategory;
    }

    /**
     *  Delete the  skillCategory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SkillCategory : {}", id);
        skillCategoryRepository.delete(id);
        skillCategorySearchRepository.delete(id);
    }

    /**
     * Search for the skillCategory corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SkillCategory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SkillCategories for query {}", query);
        Page<SkillCategory> result = skillCategorySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
