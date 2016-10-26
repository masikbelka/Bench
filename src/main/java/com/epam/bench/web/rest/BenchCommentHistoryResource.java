package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.BenchCommentHistory;
import com.epam.bench.service.BenchCommentHistoryService;
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
 * REST controller for managing BenchCommentHistory.
 */
@RestController
@RequestMapping("/api")
public class BenchCommentHistoryResource {

    private final Logger log = LoggerFactory.getLogger(BenchCommentHistoryResource.class);
        
    @Inject
    private BenchCommentHistoryService benchCommentHistoryService;

    /**
     * POST  /bench-comment-histories : Create a new benchCommentHistory.
     *
     * @param benchCommentHistory the benchCommentHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new benchCommentHistory, or with status 400 (Bad Request) if the benchCommentHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bench-comment-histories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BenchCommentHistory> createBenchCommentHistory(@Valid @RequestBody BenchCommentHistory benchCommentHistory) throws URISyntaxException {
        log.debug("REST request to save BenchCommentHistory : {}", benchCommentHistory);
        if (benchCommentHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("benchCommentHistory", "idexists", "A new benchCommentHistory cannot already have an ID")).body(null);
        }
        BenchCommentHistory result = benchCommentHistoryService.save(benchCommentHistory);
        return ResponseEntity.created(new URI("/api/bench-comment-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("benchCommentHistory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bench-comment-histories : Updates an existing benchCommentHistory.
     *
     * @param benchCommentHistory the benchCommentHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated benchCommentHistory,
     * or with status 400 (Bad Request) if the benchCommentHistory is not valid,
     * or with status 500 (Internal Server Error) if the benchCommentHistory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bench-comment-histories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BenchCommentHistory> updateBenchCommentHistory(@Valid @RequestBody BenchCommentHistory benchCommentHistory) throws URISyntaxException {
        log.debug("REST request to update BenchCommentHistory : {}", benchCommentHistory);
        if (benchCommentHistory.getId() == null) {
            return createBenchCommentHistory(benchCommentHistory);
        }
        BenchCommentHistory result = benchCommentHistoryService.save(benchCommentHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("benchCommentHistory", benchCommentHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bench-comment-histories : get all the benchCommentHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of benchCommentHistories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bench-comment-histories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BenchCommentHistory>> getAllBenchCommentHistories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BenchCommentHistories");
        Page<BenchCommentHistory> page = benchCommentHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bench-comment-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bench-comment-histories/:id : get the "id" benchCommentHistory.
     *
     * @param id the id of the benchCommentHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the benchCommentHistory, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bench-comment-histories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BenchCommentHistory> getBenchCommentHistory(@PathVariable Long id) {
        log.debug("REST request to get BenchCommentHistory : {}", id);
        BenchCommentHistory benchCommentHistory = benchCommentHistoryService.findOne(id);
        return Optional.ofNullable(benchCommentHistory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bench-comment-histories/:id : delete the "id" benchCommentHistory.
     *
     * @param id the id of the benchCommentHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bench-comment-histories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBenchCommentHistory(@PathVariable Long id) {
        log.debug("REST request to delete BenchCommentHistory : {}", id);
        benchCommentHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("benchCommentHistory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bench-comment-histories?query=:query : search for the benchCommentHistory corresponding
     * to the query.
     *
     * @param query the query of the benchCommentHistory search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/bench-comment-histories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BenchCommentHistory>> searchBenchCommentHistories(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of BenchCommentHistories for query {}", query);
        Page<BenchCommentHistory> page = benchCommentHistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bench-comment-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
