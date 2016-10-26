package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.ProjectHistory;
import com.epam.bench.service.ProjectHistoryService;
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
 * REST controller for managing ProjectHistory.
 */
@RestController
@RequestMapping("/api")
public class ProjectHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ProjectHistoryResource.class);
        
    @Inject
    private ProjectHistoryService projectHistoryService;

    /**
     * POST  /project-histories : Create a new projectHistory.
     *
     * @param projectHistory the projectHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectHistory, or with status 400 (Bad Request) if the projectHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-histories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectHistory> createProjectHistory(@Valid @RequestBody ProjectHistory projectHistory) throws URISyntaxException {
        log.debug("REST request to save ProjectHistory : {}", projectHistory);
        if (projectHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectHistory", "idexists", "A new projectHistory cannot already have an ID")).body(null);
        }
        ProjectHistory result = projectHistoryService.save(projectHistory);
        return ResponseEntity.created(new URI("/api/project-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectHistory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-histories : Updates an existing projectHistory.
     *
     * @param projectHistory the projectHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectHistory,
     * or with status 400 (Bad Request) if the projectHistory is not valid,
     * or with status 500 (Internal Server Error) if the projectHistory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-histories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectHistory> updateProjectHistory(@Valid @RequestBody ProjectHistory projectHistory) throws URISyntaxException {
        log.debug("REST request to update ProjectHistory : {}", projectHistory);
        if (projectHistory.getId() == null) {
            return createProjectHistory(projectHistory);
        }
        ProjectHistory result = projectHistoryService.save(projectHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectHistory", projectHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-histories : get all the projectHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectHistories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/project-histories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectHistory>> getAllProjectHistories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProjectHistories");
        Page<ProjectHistory> page = projectHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-histories/:id : get the "id" projectHistory.
     *
     * @param id the id of the projectHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectHistory, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-histories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectHistory> getProjectHistory(@PathVariable Long id) {
        log.debug("REST request to get ProjectHistory : {}", id);
        ProjectHistory projectHistory = projectHistoryService.findOne(id);
        return Optional.ofNullable(projectHistory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-histories/:id : delete the "id" projectHistory.
     *
     * @param id the id of the projectHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-histories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectHistory(@PathVariable Long id) {
        log.debug("REST request to delete ProjectHistory : {}", id);
        projectHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectHistory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/project-histories?query=:query : search for the projectHistory corresponding
     * to the query.
     *
     * @param query the query of the projectHistory search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/project-histories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectHistory>> searchProjectHistories(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ProjectHistories for query {}", query);
        Page<ProjectHistory> page = projectHistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
