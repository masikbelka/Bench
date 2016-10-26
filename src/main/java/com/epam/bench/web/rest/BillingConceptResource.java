package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.BillingConcept;
import com.epam.bench.service.BillingConceptService;
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
 * REST controller for managing BillingConcept.
 */
@RestController
@RequestMapping("/api")
public class BillingConceptResource {

    private final Logger log = LoggerFactory.getLogger(BillingConceptResource.class);
        
    @Inject
    private BillingConceptService billingConceptService;

    /**
     * POST  /billing-concepts : Create a new billingConcept.
     *
     * @param billingConcept the billingConcept to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billingConcept, or with status 400 (Bad Request) if the billingConcept has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/billing-concepts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillingConcept> createBillingConcept(@Valid @RequestBody BillingConcept billingConcept) throws URISyntaxException {
        log.debug("REST request to save BillingConcept : {}", billingConcept);
        if (billingConcept.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("billingConcept", "idexists", "A new billingConcept cannot already have an ID")).body(null);
        }
        BillingConcept result = billingConceptService.save(billingConcept);
        return ResponseEntity.created(new URI("/api/billing-concepts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("billingConcept", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /billing-concepts : Updates an existing billingConcept.
     *
     * @param billingConcept the billingConcept to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billingConcept,
     * or with status 400 (Bad Request) if the billingConcept is not valid,
     * or with status 500 (Internal Server Error) if the billingConcept couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/billing-concepts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillingConcept> updateBillingConcept(@Valid @RequestBody BillingConcept billingConcept) throws URISyntaxException {
        log.debug("REST request to update BillingConcept : {}", billingConcept);
        if (billingConcept.getId() == null) {
            return createBillingConcept(billingConcept);
        }
        BillingConcept result = billingConceptService.save(billingConcept);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("billingConcept", billingConcept.getId().toString()))
            .body(result);
    }

    /**
     * GET  /billing-concepts : get all the billingConcepts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of billingConcepts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/billing-concepts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BillingConcept>> getAllBillingConcepts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BillingConcepts");
        Page<BillingConcept> page = billingConceptService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/billing-concepts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /billing-concepts/:id : get the "id" billingConcept.
     *
     * @param id the id of the billingConcept to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billingConcept, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/billing-concepts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillingConcept> getBillingConcept(@PathVariable Long id) {
        log.debug("REST request to get BillingConcept : {}", id);
        BillingConcept billingConcept = billingConceptService.findOne(id);
        return Optional.ofNullable(billingConcept)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /billing-concepts/:id : delete the "id" billingConcept.
     *
     * @param id the id of the billingConcept to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/billing-concepts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBillingConcept(@PathVariable Long id) {
        log.debug("REST request to delete BillingConcept : {}", id);
        billingConceptService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("billingConcept", id.toString())).build();
    }

    /**
     * SEARCH  /_search/billing-concepts?query=:query : search for the billingConcept corresponding
     * to the query.
     *
     * @param query the query of the billingConcept search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/billing-concepts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BillingConcept>> searchBillingConcepts(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of BillingConcepts for query {}", query);
        Page<BillingConcept> page = billingConceptService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/billing-concepts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
