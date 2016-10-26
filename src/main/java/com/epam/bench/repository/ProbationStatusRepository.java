package com.epam.bench.repository;

import com.epam.bench.domain.ProbationStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProbationStatus entity.
 */
@SuppressWarnings("unused")
public interface ProbationStatusRepository extends JpaRepository<ProbationStatus,Long> {

}
