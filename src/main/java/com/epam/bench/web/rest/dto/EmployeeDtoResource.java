package com.epam.bench.web.rest.dto;

import io.swagger.annotations.Api;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.facades.EmployeeFacade;
import com.epam.bench.service.dto.bench.CommentHistoryDto;
import com.epam.bench.service.dto.bench.EmployeeSimpleViewDto;
import com.epam.bench.service.dto.bench.form.UpdateEmployeeFormDto;
import com.epam.bench.service.EmployeeService;
import com.epam.bench.service.dto.bench.EmployeeDto;
import com.epam.bench.web.rest.util.HeaderUtil;
import com.epam.bench.web.rest.util.PaginationUtil;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.NotBlank;
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

/**
 * REST controller for managing Employee.
 */
@Api(value = "EmployeeDto", description = "")
@RestController
@RequestMapping("/api/bench")
public class EmployeeDtoResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeDtoResource.class);

    @Inject
    private EmployeeFacade employeeFacade;

    /**
     * POST  /employees : Create a new employee.
     *
     * @param upsaId the employee to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employee, or with status 400 (Bad Request) if the employee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employees/{upsaId}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDto> addEmployeeToBench(@NotBlank @RequestBody String upsaId) throws URISyntaxException {
        log.debug("REST request to save Employee to bench : {}", upsaId);
        if (employeeFacade.getBenchEmployee(upsaId) != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employee", "idexists", "Employee already on bench")).body(null);
        }
        EmployeeDto result = employeeFacade.saveEmployeeToBench(upsaId);
        return ResponseEntity.created(new URI("/v1/employees/" + result.getUpsaId()))
            .headers(HeaderUtil.createEntityCreationAlert("employee", result.getUpsaId()))
            .body(result);
    }

    /**
     * PUT  /employees : Updates an existing employee.
     *
     * @param employee the employee to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employee,
     * or with status 400 (Bad Request) if the employee is not valid,
     * or with status 500 (Internal Server Error) if the employee couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDto> updateBenchEmployee(@Valid @RequestBody UpdateEmployeeFormDto employee) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", employee);
        if (employee.getUpsaId() == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        EmployeeDto result = employeeFacade.updateBenchEmployee(employee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employee", employee.getUpsaId()))
            .body(result);
    }

    /**
     * GET  /employees : get all the bench employees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of employees in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EmployeeDto>> getAllBenchEmployees(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EmployeeDtos");
        Page<EmployeeDto> page = employeeFacade.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/v1/employees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /employees/search : get all employees that feat the search query.
     *
     * @param query the search query information
     * @return the ResponseEntity with status 200 (OK) and the list of employees in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/employees/search",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EmployeeSimpleViewDto>> searchEmployeesFromUpsa(String query)
        throws URISyntaxException {
        log.debug("REST request to get a page of EmployeeDtos");
        List<EmployeeSimpleViewDto> simpleViewDtoList = employeeFacade.suggestEmployees(query);
        if (CollectionUtils.isNotEmpty(simpleViewDtoList)) {
            return new ResponseEntity<>(simpleViewDtoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * GET  /employees/:id : get the "id" employee.
     *
     * @param upsaId the id of the employee to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employee, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employees/{upsaId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDto> getBenchEmployee(@PathVariable String upsaId) {
        log.debug("REST request to get Employee : {}", upsaId);
        EmployeeDto employee = employeeFacade.getBenchEmployee(upsaId);
        return Optional.ofNullable(employee)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /employees/:upsaId/comments : get comment history of employee.
     *
     * @param upsaId the id of the employee to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employee, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employees/{upsaId}/comments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommentHistoryDto> getBenchEmployeeCommentHistory(@PathVariable String upsaId) {
        log.debug("REST request to get Employee : {}", upsaId);
        CommentHistoryDto history = employeeFacade.getBenchEmployeeCommentHistory(upsaId);
        return Optional.ofNullable(history)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employees/:upsaId : removes concrete employee from bench.
     *
     * @param upsaId the upsa id of the employee to delete it from bench
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employees/{upsaId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBenchEmployee(@PathVariable String upsaId) {
        log.debug("REST request to delete Employee from bench: {}", upsaId);
        employeeFacade.removeFromBench(upsaId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employee", upsaId)).build();
    }


}
