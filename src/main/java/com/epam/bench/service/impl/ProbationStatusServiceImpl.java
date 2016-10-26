package com.epam.bench.service.impl;

import com.epam.bench.service.ProbationStatusService;
import com.epam.bench.domain.ProbationStatus;
import com.epam.bench.repository.ProbationStatusRepository;
import com.epam.bench.repository.search.ProbationStatusSearchRepository;
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
 * Service Implementation for managing ProbationStatus.
 */
@Service
@Transactional
public class ProbationStatusServiceImpl implements ProbationStatusService{

    private final Logger log = LoggerFactory.getLogger(ProbationStatusServiceImpl.class);
    
    @Inject
    private ProbationStatusRepository probationStatusRepository;

    @Inject
    private ProbationStatusSearchRepository probationStatusSearchRepository;

    /**
     * Save a probationStatus.
     *
     * @param probationStatus the entity to save
     * @return the persisted entity
     */
    public ProbationStatus save(ProbationStatus probationStatus) {
        log.debug("Request to save ProbationStatus : {}", probationStatus);
        ProbationStatus result = probationStatusRepository.save(probationStatus);
        probationStatusSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the probationStatuses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ProbationStatus> findAll(Pageable pageable) {
        log.debug("Request to get all ProbationStatuses");
        Page<ProbationStatus> result = probationStatusRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one probationStatus by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProbationStatus findOne(Long id) {
        log.debug("Request to get ProbationStatus : {}", id);
        ProbationStatus probationStatus = probationStatusRepository.findOne(id);
        return probationStatus;
    }

    /**
     *  Delete the  probationStatus by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProbationStatus : {}", id);
        probationStatusRepository.delete(id);
        probationStatusSearchRepository.delete(id);
    }

    /**
     * Search for the probationStatus corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProbationStatus> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProbationStatuses for query {}", query);
        Page<ProbationStatus> result = probationStatusSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
