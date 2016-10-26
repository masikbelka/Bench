package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.Opportunity;
import com.epam.bench.service.OpportunityService;
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
 * REST controller for managing Opportunity.
 */
@RestController
@RequestMapping("/api")
public class OpportunityResource {

    private final Logger log = LoggerFactory.getLogger(OpportunityResource.class);
        
    @Inject
    private OpportunityService opportunityService;

    /**
     * POST  /opportunities : Create a new opportunity.
     *
     * @param opportunity the opportunity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new opportunity, or with status 400 (Bad Request) if the opportunity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/opportunities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Opportunity> createOpportunity(@Valid @RequestBody Opportunity opportunity) throws URISyntaxException {
        log.debug("REST request to save Opportunity : {}", opportunity);
        if (opportunity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("opportunity", "idexists", "A new opportunity cannot already have an ID")).body(null);
        }
        Opportunity result = opportunityService.save(opportunity);
        return ResponseEntity.created(new URI("/api/opportunities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("opportunity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /opportunities : Updates an existing opportunity.
     *
     * @param opportunity the opportunity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated opportunity,
     * or with status 400 (Bad Request) if the opportunity is not valid,
     * or with status 500 (Internal Server Error) if the opportunity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/opportunities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Opportunity> updateOpportunity(@Valid @RequestBody Opportunity opportunity) throws URISyntaxException {
        log.debug("REST request to update Opportunity : {}", opportunity);
        if (opportunity.getId() == null) {
            return createOpportunity(opportunity);
        }
        Opportunity result = opportunityService.save(opportunity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("opportunity", opportunity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /opportunities : get all the opportunities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of opportunities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/opportunities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Opportunity>> getAllOpportunities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Opportunities");
        Page<Opportunity> page = opportunityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/opportunities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /opportunities/:id : get the "id" opportunity.
     *
     * @param id the id of the opportunity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the opportunity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/opportunities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Opportunity> getOpportunity(@PathVariable Long id) {
        log.debug("REST request to get Opportunity : {}", id);
        Opportunity opportunity = opportunityService.findOne(id);
        return Optional.ofNullable(opportunity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /opportunities/:id : delete the "id" opportunity.
     *
     * @param id the id of the opportunity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/opportunities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOpportunity(@PathVariable Long id) {
        log.debug("REST request to delete Opportunity : {}", id);
        opportunityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("opportunity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/opportunities?query=:query : search for the opportunity corresponding
     * to the query.
     *
     * @param query the query of the opportunity search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/opportunities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Opportunity>> searchOpportunities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Opportunities for query {}", query);
        Page<Opportunity> page = opportunityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/opportunities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
