package com.epam.bench.service.impl;

import com.epam.bench.service.ProjectCategoryService;
import com.epam.bench.domain.ProjectCategory;
import com.epam.bench.repository.ProjectCategoryRepository;
import com.epam.bench.repository.search.ProjectCategorySearchRepository;
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
 * Service Implementation for managing ProjectCategory.
 */
@Service
@Transactional
public class ProjectCategoryServiceImpl implements ProjectCategoryService{

    private final Logger log = LoggerFactory.getLogger(ProjectCategoryServiceImpl.class);
    
    @Inject
    private ProjectCategoryRepository projectCategoryRepository;

    @Inject
    private ProjectCategorySearchRepository projectCategorySearchRepository;

    /**
     * Save a projectCategory.
     *
     * @param projectCategory the entity to save
     * @return the persisted entity
     */
    public ProjectCategory save(ProjectCategory projectCategory) {
        log.debug("Request to save ProjectCategory : {}", projectCategory);
        ProjectCategory result = projectCategoryRepository.save(projectCategory);
        projectCategorySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the projectCategories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ProjectCategory> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectCategories");
        Page<ProjectCategory> result = projectCategoryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one projectCategory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProjectCategory findOne(Long id) {
        log.debug("Request to get ProjectCategory : {}", id);
        ProjectCategory projectCategory = projectCategoryRepository.findOne(id);
        return projectCategory;
    }

    /**
     *  Delete the  projectCategory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectCategory : {}", id);
        projectCategoryRepository.delete(id);
        projectCategorySearchRepository.delete(id);
    }

    /**
     * Search for the projectCategory corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectCategory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProjectCategories for query {}", query);
        Page<ProjectCategory> result = projectCategorySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
