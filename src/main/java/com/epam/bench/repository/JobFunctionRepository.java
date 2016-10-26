package com.epam.bench.repository;

import com.epam.bench.domain.JobFunction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobFunction entity.
 */
@SuppressWarnings("unused")
public interface JobFunctionRepository extends JpaRepository<JobFunction,Long> {

}
