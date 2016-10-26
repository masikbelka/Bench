package com.epam.bench.service.impl;

import com.epam.bench.service.BenchCommentHistoryService;
import com.epam.bench.domain.BenchCommentHistory;
import com.epam.bench.repository.BenchCommentHistoryRepository;
import com.epam.bench.repository.search.BenchCommentHistorySearchRepository;
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
 * Service Implementation for managing BenchCommentHistory.
 */
@Service
@Transactional
public class BenchCommentHistoryServiceImpl implements BenchCommentHistoryService{

    private final Logger log = LoggerFactory.getLogger(BenchCommentHistoryServiceImpl.class);
    
    @Inject
    private BenchCommentHistoryRepository benchCommentHistoryRepository;

    @Inject
    private BenchCommentHistorySearchRepository benchCommentHistorySearchRepository;

    /**
     * Save a benchCommentHistory.
     *
     * @param benchCommentHistory the entity to save
     * @return the persisted entity
     */
    public BenchCommentHistory save(BenchCommentHistory benchCommentHistory) {
        log.debug("Request to save BenchCommentHistory : {}", benchCommentHistory);
        BenchCommentHistory result = benchCommentHistoryRepository.save(benchCommentHistory);
        benchCommentHistorySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the benchCommentHistories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BenchCommentHistory> findAll(Pageable pageable) {
        log.debug("Request to get all BenchCommentHistories");
        Page<BenchCommentHistory> result = benchCommentHistoryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one benchCommentHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BenchCommentHistory findOne(Long id) {
        log.debug("Request to get BenchCommentHistory : {}", id);
        BenchCommentHistory benchCommentHistory = benchCommentHistoryRepository.findOne(id);
        return benchCommentHistory;
    }

    /**
     *  Delete the  benchCommentHistory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BenchCommentHistory : {}", id);
        benchCommentHistoryRepository.delete(id);
        benchCommentHistorySearchRepository.delete(id);
    }

    /**
     * Search for the benchCommentHistory corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BenchCommentHistory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BenchCommentHistories for query {}", query);
        Page<BenchCommentHistory> result = benchCommentHistorySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
