package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.Title;
import com.epam.bench.service.TitleService;
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
 * REST controller for managing Title.
 */
@RestController
@RequestMapping("/api")
public class TitleResource {

    private final Logger log = LoggerFactory.getLogger(TitleResource.class);
        
    @Inject
    private TitleService titleService;

    /**
     * POST  /titles : Create a new title.
     *
     * @param title the title to create
     * @return the ResponseEntity with status 201 (Created) and with body the new title, or with status 400 (Bad Request) if the title has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/titles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Title> createTitle(@Valid @RequestBody Title title) throws URISyntaxException {
        log.debug("REST request to save Title : {}", title);
        if (title.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("title", "idexists", "A new title cannot already have an ID")).body(null);
        }
        Title result = titleService.save(title);
        return ResponseEntity.created(new URI("/api/titles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("title", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /titles : Updates an existing title.
     *
     * @param title the title to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated title,
     * or with status 400 (Bad Request) if the title is not valid,
     * or with status 500 (Internal Server Error) if the title couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/titles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Title> updateTitle(@Valid @RequestBody Title title) throws URISyntaxException {
        log.debug("REST request to update Title : {}", title);
        if (title.getId() == null) {
            return createTitle(title);
        }
        Title result = titleService.save(title);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("title", title.getId().toString()))
            .body(result);
    }

    /**
     * GET  /titles : get all the titles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of titles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/titles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Title>> getAllTitles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Titles");
        Page<Title> page = titleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/titles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /titles/:id : get the "id" title.
     *
     * @param id the id of the title to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the title, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/titles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Title> getTitle(@PathVariable Long id) {
        log.debug("REST request to get Title : {}", id);
        Title title = titleService.findOne(id);
        return Optional.ofNullable(title)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /titles/:id : delete the "id" title.
     *
     * @param id the id of the title to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/titles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
        log.debug("REST request to delete Title : {}", id);
        titleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("title", id.toString())).build();
    }

    /**
     * SEARCH  /_search/titles?query=:query : search for the title corresponding
     * to the query.
     *
     * @param query the query of the title search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/titles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Title>> searchTitles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Titles for query {}", query);
        Page<Title> page = titleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/titles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
