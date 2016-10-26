package com.epam.bench.repository;

import com.epam.bench.domain.BenchPredictions;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BenchPredictions entity.
 */
@SuppressWarnings("unused")
public interface BenchPredictionsRepository extends JpaRepository<BenchPredictions,Long> {

}
