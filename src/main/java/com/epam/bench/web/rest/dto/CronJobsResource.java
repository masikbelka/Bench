package com.epam.bench.web.rest.dto;

import io.swagger.annotations.Api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.epam.bench.facades.EmployeeFacade;
import com.epam.bench.service.dto.bench.CommentHistoryDto;
import com.epam.bench.service.dto.bench.EmployeeDto;
import com.epam.bench.service.dto.bench.EmployeeSimpleViewDto;
import com.epam.bench.service.dto.bench.form.UpdateEmployeeFormDto;
import com.epam.bench.web.rest.util.HeaderUtil;
import com.epam.bench.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Employee.
 */
@Api(value = "Job", description = "")
@RestController
@RequestMapping("/api/bench/job")
public class CronJobsResource {

    private final Logger log = LoggerFactory.getLogger(CronJobsResource.class);


}
