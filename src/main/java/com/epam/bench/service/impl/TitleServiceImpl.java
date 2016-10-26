package com.epam.bench.service.impl;

import com.epam.bench.service.TitleService;
import com.epam.bench.domain.Title;
import com.epam.bench.repository.TitleRepository;
import com.epam.bench.repository.search.TitleSearchRepository;
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
 * Service Implementation for managing Title.
 */
@Service
@Transactional
public class TitleServiceImpl implements TitleService{

    private final Logger log = LoggerFactory.getLogger(TitleServiceImpl.class);
    
    @Inject
    private TitleRepository titleRepository;

    @Inject
    private TitleSearchRepository titleSearchRepository;

    /**
     * Save a title.
     *
     * @param title the entity to save
     * @return the persisted entity
     */
    public Title save(Title title) {
        log.debug("Request to save Title : {}", title);
        Title result = titleRepository.save(title);
        titleSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the titles.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Title> findAll(Pageable pageable) {
        log.debug("Request to get all Titles");
        Page<Title> result = titleRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one title by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Title findOne(Long id) {
        log.debug("Request to get Title : {}", id);
        Title title = titleRepository.findOne(id);
        return title;
    }

    /**
     *  Delete the  title by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Title : {}", id);
        titleRepository.delete(id);
        titleSearchRepository.delete(id);
    }

    /**
     * Search for the title corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Title> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Titles for query {}", query);
        Page<Title> result = titleSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
