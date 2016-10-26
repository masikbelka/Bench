package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.PrimarySkill;
import com.epam.bench.service.PrimarySkillService;
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
 * REST controller for managing PrimarySkill.
 */
@RestController
@RequestMapping("/api")
public class PrimarySkillResource {

    private final Logger log = LoggerFactory.getLogger(PrimarySkillResource.class);
        
    @Inject
    private PrimarySkillService primarySkillService;

    /**
     * POST  /primary-skills : Create a new primarySkill.
     *
     * @param primarySkill the primarySkill to create
     * @return the ResponseEntity with status 201 (Created) and with body the new primarySkill, or with status 400 (Bad Request) if the primarySkill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/primary-skills",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrimarySkill> createPrimarySkill(@Valid @RequestBody PrimarySkill primarySkill) throws URISyntaxException {
        log.debug("REST request to save PrimarySkill : {}", primarySkill);
        if (primarySkill.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("primarySkill", "idexists", "A new primarySkill cannot already have an ID")).body(null);
        }
        PrimarySkill result = primarySkillService.save(primarySkill);
        return ResponseEntity.created(new URI("/api/primary-skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("primarySkill", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /primary-skills : Updates an existing primarySkill.
     *
     * @param primarySkill the primarySkill to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated primarySkill,
     * or with status 400 (Bad Request) if the primarySkill is not valid,
     * or with status 500 (Internal Server Error) if the primarySkill couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/primary-skills",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrimarySkill> updatePrimarySkill(@Valid @RequestBody PrimarySkill primarySkill) throws URISyntaxException {
        log.debug("REST request to update PrimarySkill : {}", primarySkill);
        if (primarySkill.getId() == null) {
            return createPrimarySkill(primarySkill);
        }
        PrimarySkill result = primarySkillService.save(primarySkill);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("primarySkill", primarySkill.getId().toString()))
            .body(result);
    }

    /**
     * GET  /primary-skills : get all the primarySkills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of primarySkills in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/primary-skills",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PrimarySkill>> getAllPrimarySkills(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PrimarySkills");
        Page<PrimarySkill> page = primarySkillService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/primary-skills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /primary-skills/:id : get the "id" primarySkill.
     *
     * @param id the id of the primarySkill to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the primarySkill, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/primary-skills/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrimarySkill> getPrimarySkill(@PathVariable Long id) {
        log.debug("REST request to get PrimarySkill : {}", id);
        PrimarySkill primarySkill = primarySkillService.findOne(id);
        return Optional.ofNullable(primarySkill)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /primary-skills/:id : delete the "id" primarySkill.
     *
     * @param id the id of the primarySkill to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/primary-skills/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrimarySkill(@PathVariable Long id) {
        log.debug("REST request to delete PrimarySkill : {}", id);
        primarySkillService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("primarySkill", id.toString())).build();
    }

    /**
     * SEARCH  /_search/primary-skills?query=:query : search for the primarySkill corresponding
     * to the query.
     *
     * @param query the query of the primarySkill search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/primary-skills",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PrimarySkill>> searchPrimarySkills(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of PrimarySkills for query {}", query);
        Page<PrimarySkill> page = primarySkillService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/primary-skills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
