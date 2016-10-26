package com.epam.bench.service.impl;

import com.epam.bench.service.BillingConceptService;
import com.epam.bench.domain.BillingConcept;
import com.epam.bench.repository.BillingConceptRepository;
import com.epam.bench.repository.search.BillingConceptSearchRepository;
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
 * Service Implementation for managing BillingConcept.
 */
@Service
@Transactional
public class BillingConceptServiceImpl implements BillingConceptService{

    private final Logger log = LoggerFactory.getLogger(BillingConceptServiceImpl.class);
    
    @Inject
    private BillingConceptRepository billingConceptRepository;

    @Inject
    private BillingConceptSearchRepository billingConceptSearchRepository;

    /**
     * Save a billingConcept.
     *
     * @param billingConcept the entity to save
     * @return the persisted entity
     */
    public BillingConcept save(BillingConcept billingConcept) {
        log.debug("Request to save BillingConcept : {}", billingConcept);
        BillingConcept result = billingConceptRepository.save(billingConcept);
        billingConceptSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the billingConcepts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BillingConcept> findAll(Pageable pageable) {
        log.debug("Request to get all BillingConcepts");
        Page<BillingConcept> result = billingConceptRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one billingConcept by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BillingConcept findOne(Long id) {
        log.debug("Request to get BillingConcept : {}", id);
        BillingConcept billingConcept = billingConceptRepository.findOne(id);
        return billingConcept;
    }

    /**
     *  Delete the  billingConcept by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BillingConcept : {}", id);
        billingConceptRepository.delete(id);
        billingConceptSearchRepository.delete(id);
    }

    /**
     * Search for the billingConcept corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BillingConcept> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BillingConcepts for query {}", query);
        Page<BillingConcept> result = billingConceptSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
