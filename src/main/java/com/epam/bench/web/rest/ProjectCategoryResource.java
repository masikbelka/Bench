package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.ProjectCategory;
import com.epam.bench.service.ProjectCategoryService;
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
 * REST controller for managing ProjectCategory.
 */
@RestController
@RequestMapping("/api")
public class ProjectCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ProjectCategoryResource.class);
        
    @Inject
    private ProjectCategoryService projectCategoryService;

    /**
     * POST  /project-categories : Create a new projectCategory.
     *
     * @param projectCategory the projectCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectCategory, or with status 400 (Bad Request) if the projectCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-categories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectCategory> createProjectCategory(@Valid @RequestBody ProjectCategory projectCategory) throws URISyntaxException {
        log.debug("REST request to save ProjectCategory : {}", projectCategory);
        if (projectCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectCategory", "idexists", "A new projectCategory cannot already have an ID")).body(null);
        }
        ProjectCategory result = projectCategoryService.save(projectCategory);
        return ResponseEntity.created(new URI("/api/project-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectCategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-categories : Updates an existing projectCategory.
     *
     * @param projectCategory the projectCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectCategory,
     * or with status 400 (Bad Request) if the projectCategory is not valid,
     * or with status 500 (Internal Server Error) if the projectCategory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-categories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectCategory> updateProjectCategory(@Valid @RequestBody ProjectCategory projectCategory) throws URISyntaxException {
        log.debug("REST request to update ProjectCategory : {}", projectCategory);
        if (projectCategory.getId() == null) {
            return createProjectCategory(projectCategory);
        }
        ProjectCategory result = projectCategoryService.save(projectCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectCategory", projectCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-categories : get all the projectCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectCategories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/project-categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectCategory>> getAllProjectCategories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProjectCategories");
        Page<ProjectCategory> page = projectCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-categories/:id : get the "id" projectCategory.
     *
     * @param id the id of the projectCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectCategory, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-categories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectCategory> getProjectCategory(@PathVariable Long id) {
        log.debug("REST request to get ProjectCategory : {}", id);
        ProjectCategory projectCategory = projectCategoryService.findOne(id);
        return Optional.ofNullable(projectCategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-categories/:id : delete the "id" projectCategory.
     *
     * @param id the id of the projectCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-categories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectCategory(@PathVariable Long id) {
        log.debug("REST request to delete ProjectCategory : {}", id);
        projectCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectCategory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/project-categories?query=:query : search for the projectCategory corresponding
     * to the query.
     *
     * @param query the query of the projectCategory search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/project-categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectCategory>> searchProjectCategories(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ProjectCategories for query {}", query);
        Page<ProjectCategory> page = projectCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
