package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.LanguageLevel;
import com.epam.bench.service.LanguageLevelService;
import com.epam.bench.web.rest.util.HeaderUtil;
import com.epam.bench.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing LanguageLevel.
 */
@RestController
@RequestMapping("/api")
public class LanguageLevelResource {

    private final Logger log = LoggerFactory.getLogger(LanguageLevelResource.class);
        
    @Inject
    private LanguageLevelService languageLevelService;

    /**
     * POST  /language-levels : Create a new languageLevel.
     *
     * @param languageLevel the languageLevel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new languageLevel, or with status 400 (Bad Request) if the languageLevel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/language-levels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LanguageLevel> createLanguageLevel(@Valid @RequestBody LanguageLevel languageLevel) throws URISyntaxException {
        log.debug("REST request to save LanguageLevel : {}", languageLevel);
        if (languageLevel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("languageLevel", "idexists", "A new languageLevel cannot already have an ID")).body(null);
        }
        LanguageLevel result = languageLevelService.save(languageLevel);
        return ResponseEntity.created(new URI("/api/language-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("languageLevel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /language-levels : Updates an existing languageLevel.
     *
     * @param languageLevel the languageLevel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated languageLevel,
     * or with status 400 (Bad Request) if the languageLevel is not valid,
     * or with status 500 (Internal Server Error) if the languageLevel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/language-levels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LanguageLevel> updateLanguageLevel(@Valid @RequestBody LanguageLevel languageLevel) throws URISyntaxException {
        log.debug("REST request to update LanguageLevel : {}", languageLevel);
        if (languageLevel.getId() == null) {
            return createLanguageLevel(languageLevel);
        }
        LanguageLevel result = languageLevelService.save(languageLevel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("languageLevel", languageLevel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /language-levels : get all the languageLevels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of languageLevels in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/language-levels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LanguageLevel>> getAllLanguageLevels(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LanguageLevels");
        Page<LanguageLevel> page = languageLevelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/language-levels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /language-levels/:id : get the "id" languageLevel.
     *
     * @param id the id of the languageLevel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the languageLevel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/language-levels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LanguageLevel> getLanguageLevel(@PathVariable Long id) {
        log.debug("REST request to get LanguageLevel : {}", id);
        LanguageLevel languageLevel = languageLevelService.findOne(id);
        return Optional.ofNullable(languageLevel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /language-levels/:id : delete the "id" languageLevel.
     *
     * @param id the id of the languageLevel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/language-levels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLanguageLevel(@PathVariable Long id) {
        log.debug("REST request to delete LanguageLevel : {}", id);
        languageLevelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("languageLevel", id.toString())).build();
    }

    /**
     * SEARCH  /_search/language-levels?query=:query : search for the languageLevel corresponding
     * to the query.
     *
     * @param query the query of the languageLevel search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/language-levels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LanguageLevel>> searchLanguageLevels(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of LanguageLevels for query {}", query);
        Page<LanguageLevel> page = languageLevelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/language-levels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
