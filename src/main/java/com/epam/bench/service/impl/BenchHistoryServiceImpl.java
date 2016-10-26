package com.epam.bench.service.impl;

import com.epam.bench.service.BenchHistoryService;
import com.epam.bench.domain.BenchHistory;
import com.epam.bench.repository.BenchHistoryRepository;
import com.epam.bench.repository.search.BenchHistorySearchRepository;
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
 * Service Implementation for managing BenchHistory.
 */
@Service
@Transactional
public class BenchHistoryServiceImpl implements BenchHistoryService{

    private final Logger log = LoggerFactory.getLogger(BenchHistoryServiceImpl.class);
    
    @Inject
    private BenchHistoryRepository benchHistoryRepository;

    @Inject
    private BenchHistorySearchRepository benchHistorySearchRepository;

    /**
     * Save a benchHistory.
     *
     * @param benchHistory the entity to save
     * @return the persisted entity
     */
    public BenchHistory save(BenchHistory benchHistory) {
        log.debug("Request to save BenchHistory : {}", benchHistory);
        BenchHistory result = benchHistoryRepository.save(benchHistory);
        benchHistorySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the benchHistories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BenchHistory> findAll(Pageable pageable) {
        log.debug("Request to get all BenchHistories");
        Page<BenchHistory> result = benchHistoryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one benchHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BenchHistory findOne(Long id) {
        log.debug("Request to get BenchHistory : {}", id);
        BenchHistory benchHistory = benchHistoryRepository.findOne(id);
        return benchHistory;
    }

    /**
     *  Delete the  benchHistory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BenchHistory : {}", id);
        benchHistoryRepository.delete(id);
        benchHistorySearchRepository.delete(id);
    }

    /**
     * Search for the benchHistory corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BenchHistory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BenchHistories for query {}", query);
        Page<BenchHistory> result = benchHistorySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
