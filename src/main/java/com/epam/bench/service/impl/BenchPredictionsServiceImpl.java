package com.epam.bench.service.impl;

import com.epam.bench.service.BenchPredictionsService;
import com.epam.bench.domain.BenchPredictions;
import com.epam.bench.repository.BenchPredictionsRepository;
import com.epam.bench.repository.search.BenchPredictionsSearchRepository;
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
 * Service Implementation for managing BenchPredictions.
 */
@Service
@Transactional
public class BenchPredictionsServiceImpl implements BenchPredictionsService{

    private final Logger log = LoggerFactory.getLogger(BenchPredictionsServiceImpl.class);
    
    @Inject
    private BenchPredictionsRepository benchPredictionsRepository;

    @Inject
    private BenchPredictionsSearchRepository benchPredictionsSearchRepository;

    /**
     * Save a benchPredictions.
     *
     * @param benchPredictions the entity to save
     * @return the persisted entity
     */
    public BenchPredictions save(BenchPredictions benchPredictions) {
        log.debug("Request to save BenchPredictions : {}", benchPredictions);
        BenchPredictions result = benchPredictionsRepository.save(benchPredictions);
        benchPredictionsSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the benchPredictions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BenchPredictions> findAll(Pageable pageable) {
        log.debug("Request to get all BenchPredictions");
        Page<BenchPredictions> result = benchPredictionsRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one benchPredictions by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BenchPredictions findOne(Long id) {
        log.debug("Request to get BenchPredictions : {}", id);
        BenchPredictions benchPredictions = benchPredictionsRepository.findOne(id);
        return benchPredictions;
    }

    /**
     *  Delete the  benchPredictions by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BenchPredictions : {}", id);
        benchPredictionsRepository.delete(id);
        benchPredictionsSearchRepository.delete(id);
    }

    /**
     * Search for the benchPredictions corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BenchPredictions> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BenchPredictions for query {}", query);
        Page<BenchPredictions> result = benchPredictionsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
