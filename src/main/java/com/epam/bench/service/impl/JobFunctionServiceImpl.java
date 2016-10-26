package com.epam.bench.service.impl;

import com.epam.bench.service.JobFunctionService;
import com.epam.bench.domain.JobFunction;
import com.epam.bench.repository.JobFunctionRepository;
import com.epam.bench.repository.search.JobFunctionSearchRepository;
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
 * Service Implementation for managing JobFunction.
 */
@Service
@Transactional
public class JobFunctionServiceImpl implements JobFunctionService{

    private final Logger log = LoggerFactory.getLogger(JobFunctionServiceImpl.class);
    
    @Inject
    private JobFunctionRepository jobFunctionRepository;

    @Inject
    private JobFunctionSearchRepository jobFunctionSearchRepository;

    /**
     * Save a jobFunction.
     *
     * @param jobFunction the entity to save
     * @return the persisted entity
     */
    public JobFunction save(JobFunction jobFunction) {
        log.debug("Request to save JobFunction : {}", jobFunction);
        JobFunction result = jobFunctionRepository.save(jobFunction);
        jobFunctionSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the jobFunctions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<JobFunction> findAll(Pageable pageable) {
        log.debug("Request to get all JobFunctions");
        Page<JobFunction> result = jobFunctionRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one jobFunction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public JobFunction findOne(Long id) {
        log.debug("Request to get JobFunction : {}", id);
        JobFunction jobFunction = jobFunctionRepository.findOne(id);
        return jobFunction;
    }

    /**
     *  Delete the  jobFunction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JobFunction : {}", id);
        jobFunctionRepository.delete(id);
        jobFunctionSearchRepository.delete(id);
    }

    /**
     * Search for the jobFunction corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<JobFunction> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of JobFunctions for query {}", query);
        Page<JobFunction> result = jobFunctionSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
