package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.BillingType;
import com.epam.bench.service.BillingTypeService;
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
 * REST controller for managing BillingType.
 */
@RestController
@RequestMapping("/api")
public class BillingTypeResource {

    private final Logger log = LoggerFactory.getLogger(BillingTypeResource.class);
        
    @Inject
    private BillingTypeService billingTypeService;

    /**
     * POST  /billing-types : Create a new billingType.
     *
     * @param billingType the billingType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billingType, or with status 400 (Bad Request) if the billingType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/billing-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillingType> createBillingType(@Valid @RequestBody BillingType billingType) throws URISyntaxException {
        log.debug("REST request to save BillingType : {}", billingType);
        if (billingType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("billingType", "idexists", "A new billingType cannot already have an ID")).body(null);
        }
        BillingType result = billingTypeService.save(billingType);
        return ResponseEntity.created(new URI("/api/billing-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("billingType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /billing-types : Updates an existing billingType.
     *
     * @param billingType the billingType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billingType,
     * or with status 400 (Bad Request) if the billingType is not valid,
     * or with status 500 (Internal Server Error) if the billingType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/billing-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillingType> updateBillingType(@Valid @RequestBody BillingType billingType) throws URISyntaxException {
        log.debug("REST request to update BillingType : {}", billingType);
        if (billingType.getId() == null) {
            return createBillingType(billingType);
        }
        BillingType result = billingTypeService.save(billingType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("billingType", billingType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /billing-types : get all the billingTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of billingTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/billing-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BillingType>> getAllBillingTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BillingTypes");
        Page<BillingType> page = billingTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/billing-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /billing-types/:id : get the "id" billingType.
     *
     * @param id the id of the billingType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billingType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/billing-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillingType> getBillingType(@PathVariable Long id) {
        log.debug("REST request to get BillingType : {}", id);
        BillingType billingType = billingTypeService.findOne(id);
        return Optional.ofNullable(billingType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /billing-types/:id : delete the "id" billingType.
     *
     * @param id the id of the billingType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/billing-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBillingType(@PathVariable Long id) {
        log.debug("REST request to delete BillingType : {}", id);
        billingTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("billingType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/billing-types?query=:query : search for the billingType corresponding
     * to the query.
     *
     * @param query the query of the billingType search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/billing-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BillingType>> searchBillingTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of BillingTypes for query {}", query);
        Page<BillingType> page = billingTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/billing-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
