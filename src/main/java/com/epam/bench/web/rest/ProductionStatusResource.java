package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.ProductionStatus;
import com.epam.bench.service.ProductionStatusService;
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
 * REST controller for managing ProductionStatus.
 */
@RestController
@RequestMapping("/api")
public class ProductionStatusResource {

    private final Logger log = LoggerFactory.getLogger(ProductionStatusResource.class);
        
    @Inject
    private ProductionStatusService productionStatusService;

    /**
     * POST  /production-statuses : Create a new productionStatus.
     *
     * @param productionStatus the productionStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productionStatus, or with status 400 (Bad Request) if the productionStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/production-statuses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductionStatus> createProductionStatus(@Valid @RequestBody ProductionStatus productionStatus) throws URISyntaxException {
        log.debug("REST request to save ProductionStatus : {}", productionStatus);
        if (productionStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productionStatus", "idexists", "A new productionStatus cannot already have an ID")).body(null);
        }
        ProductionStatus result = productionStatusService.save(productionStatus);
        return ResponseEntity.created(new URI("/api/production-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productionStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /production-statuses : Updates an existing productionStatus.
     *
     * @param productionStatus the productionStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productionStatus,
     * or with status 400 (Bad Request) if the productionStatus is not valid,
     * or with status 500 (Internal Server Error) if the productionStatus couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/production-statuses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductionStatus> updateProductionStatus(@Valid @RequestBody ProductionStatus productionStatus) throws URISyntaxException {
        log.debug("REST request to update ProductionStatus : {}", productionStatus);
        if (productionStatus.getId() == null) {
            return createProductionStatus(productionStatus);
        }
        ProductionStatus result = productionStatusService.save(productionStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productionStatus", productionStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /production-statuses : get all the productionStatuses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of productionStatuses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/production-statuses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProductionStatus>> getAllProductionStatuses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProductionStatuses");
        Page<ProductionStatus> page = productionStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/production-statuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /production-statuses/:id : get the "id" productionStatus.
     *
     * @param id the id of the productionStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productionStatus, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/production-statuses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductionStatus> getProductionStatus(@PathVariable Long id) {
        log.debug("REST request to get ProductionStatus : {}", id);
        ProductionStatus productionStatus = productionStatusService.findOne(id);
        return Optional.ofNullable(productionStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /production-statuses/:id : delete the "id" productionStatus.
     *
     * @param id the id of the productionStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/production-statuses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductionStatus(@PathVariable Long id) {
        log.debug("REST request to delete ProductionStatus : {}", id);
        productionStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productionStatus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/production-statuses?query=:query : search for the productionStatus corresponding
     * to the query.
     *
     * @param query the query of the productionStatus search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/production-statuses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProductionStatus>> searchProductionStatuses(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ProductionStatuses for query {}", query);
        Page<ProductionStatus> page = productionStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/production-statuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
