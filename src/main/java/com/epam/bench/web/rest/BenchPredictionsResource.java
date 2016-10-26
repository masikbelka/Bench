package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.BenchPredictions;
import com.epam.bench.service.BenchPredictionsService;
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
 * REST controller for managing BenchPredictions.
 */
@RestController
@RequestMapping("/api")
public class BenchPredictionsResource {

    private final Logger log = LoggerFactory.getLogger(BenchPredictionsResource.class);
        
    @Inject
    private BenchPredictionsService benchPredictionsService;

    /**
     * POST  /bench-predictions : Create a new benchPredictions.
     *
     * @param benchPredictions the benchPredictions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new benchPredictions, or with status 400 (Bad Request) if the benchPredictions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bench-predictions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BenchPredictions> createBenchPredictions(@Valid @RequestBody BenchPredictions benchPredictions) throws URISyntaxException {
        log.debug("REST request to save BenchPredictions : {}", benchPredictions);
        if (benchPredictions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("benchPredictions", "idexists", "A new benchPredictions cannot already have an ID")).body(null);
        }
        BenchPredictions result = benchPredictionsService.save(benchPredictions);
        return ResponseEntity.created(new URI("/api/bench-predictions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("benchPredictions", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bench-predictions : Updates an existing benchPredictions.
     *
     * @param benchPredictions the benchPredictions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated benchPredictions,
     * or with status 400 (Bad Request) if the benchPredictions is not valid,
     * or with status 500 (Internal Server Error) if the benchPredictions couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bench-predictions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BenchPredictions> updateBenchPredictions(@Valid @RequestBody BenchPredictions benchPredictions) throws URISyntaxException {
        log.debug("REST request to update BenchPredictions : {}", benchPredictions);
        if (benchPredictions.getId() == null) {
            return createBenchPredictions(benchPredictions);
        }
        BenchPredictions result = benchPredictionsService.save(benchPredictions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("benchPredictions", benchPredictions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bench-predictions : get all the benchPredictions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of benchPredictions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bench-predictions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BenchPredictions>> getAllBenchPredictions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BenchPredictions");
        Page<BenchPredictions> page = benchPredictionsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bench-predictions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bench-predictions/:id : get the "id" benchPredictions.
     *
     * @param id the id of the benchPredictions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the benchPredictions, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bench-predictions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BenchPredictions> getBenchPredictions(@PathVariable Long id) {
        log.debug("REST request to get BenchPredictions : {}", id);
        BenchPredictions benchPredictions = benchPredictionsService.findOne(id);
        return Optional.ofNullable(benchPredictions)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bench-predictions/:id : delete the "id" benchPredictions.
     *
     * @param id the id of the benchPredictions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bench-predictions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBenchPredictions(@PathVariable Long id) {
        log.debug("REST request to delete BenchPredictions : {}", id);
        benchPredictionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("benchPredictions", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bench-predictions?query=:query : search for the benchPredictions corresponding
     * to the query.
     *
     * @param query the query of the benchPredictions search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/bench-predictions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BenchPredictions>> searchBenchPredictions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of BenchPredictions for query {}", query);
        Page<BenchPredictions> page = benchPredictionsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bench-predictions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
