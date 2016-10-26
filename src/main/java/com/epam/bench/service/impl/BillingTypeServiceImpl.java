package com.epam.bench.service.impl;

import com.epam.bench.service.BillingTypeService;
import com.epam.bench.domain.BillingType;
import com.epam.bench.repository.BillingTypeRepository;
import com.epam.bench.repository.search.BillingTypeSearchRepository;
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
 * Service Implementation for managing BillingType.
 */
@Service
@Transactional
public class BillingTypeServiceImpl implements BillingTypeService{

    private final Logger log = LoggerFactory.getLogger(BillingTypeServiceImpl.class);
    
    @Inject
    private BillingTypeRepository billingTypeRepository;

    @Inject
    private BillingTypeSearchRepository billingTypeSearchRepository;

    /**
     * Save a billingType.
     *
     * @param billingType the entity to save
     * @return the persisted entity
     */
    public BillingType save(BillingType billingType) {
        log.debug("Request to save BillingType : {}", billingType);
        BillingType result = billingTypeRepository.save(billingType);
        billingTypeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the billingTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BillingType> findAll(Pageable pageable) {
        log.debug("Request to get all BillingTypes");
        Page<BillingType> result = billingTypeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one billingType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BillingType findOne(Long id) {
        log.debug("Request to get BillingType : {}", id);
        BillingType billingType = billingTypeRepository.findOne(id);
        return billingType;
    }

    /**
     *  Delete the  billingType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BillingType : {}", id);
        billingTypeRepository.delete(id);
        billingTypeSearchRepository.delete(id);
    }

    /**
     * Search for the billingType corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BillingType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BillingTypes for query {}", query);
        Page<BillingType> result = billingTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
