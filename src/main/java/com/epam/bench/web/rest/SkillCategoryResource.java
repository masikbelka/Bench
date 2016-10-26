package com.epam.bench.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.domain.SkillCategory;
import com.epam.bench.service.SkillCategoryService;
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
 * REST controller for managing SkillCategory.
 */
@RestController
@RequestMapping("/api")
public class SkillCategoryResource {

    private final Logger log = LoggerFactory.getLogger(SkillCategoryResource.class);
        
    @Inject
    private SkillCategoryService skillCategoryService;

    /**
     * POST  /skill-categories : Create a new skillCategory.
     *
     * @param skillCategory the skillCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skillCategory, or with status 400 (Bad Request) if the skillCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/skill-categories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SkillCategory> createSkillCategory(@Valid @RequestBody SkillCategory skillCategory) throws URISyntaxException {
        log.debug("REST request to save SkillCategory : {}", skillCategory);
        if (skillCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("skillCategory", "idexists", "A new skillCategory cannot already have an ID")).body(null);
        }
        SkillCategory result = skillCategoryService.save(skillCategory);
        return ResponseEntity.created(new URI("/api/skill-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("skillCategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skill-categories : Updates an existing skillCategory.
     *
     * @param skillCategory the skillCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skillCategory,
     * or with status 400 (Bad Request) if the skillCategory is not valid,
     * or with status 500 (Internal Server Error) if the skillCategory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/skill-categories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SkillCategory> updateSkillCategory(@Valid @RequestBody SkillCategory skillCategory) throws URISyntaxException {
        log.debug("REST request to update SkillCategory : {}", skillCategory);
        if (skillCategory.getId() == null) {
            return createSkillCategory(skillCategory);
        }
        SkillCategory result = skillCategoryService.save(skillCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("skillCategory", skillCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skill-categories : get all the skillCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of skillCategories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/skill-categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SkillCategory>> getAllSkillCategories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SkillCategories");
        Page<SkillCategory> page = skillCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/skill-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /skill-categories/:id : get the "id" skillCategory.
     *
     * @param id the id of the skillCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skillCategory, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/skill-categories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SkillCategory> getSkillCategory(@PathVariable Long id) {
        log.debug("REST request to get SkillCategory : {}", id);
        SkillCategory skillCategory = skillCategoryService.findOne(id);
        return Optional.ofNullable(skillCategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /skill-categories/:id : delete the "id" skillCategory.
     *
     * @param id the id of the skillCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/skill-categories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSkillCategory(@PathVariable Long id) {
        log.debug("REST request to delete SkillCategory : {}", id);
        skillCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("skillCategory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/skill-categories?query=:query : search for the skillCategory corresponding
     * to the query.
     *
     * @param query the query of the skillCategory search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/skill-categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SkillCategory>> searchSkillCategories(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SkillCategories for query {}", query);
        Page<SkillCategory> page = skillCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/skill-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
