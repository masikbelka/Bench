package com.epam.bench.repository;

import com.epam.bench.domain.PredictionDetails;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PredictionDetails entity.
 */
@SuppressWarnings("unused")
public interface PredictionDetailsRepository extends JpaRepository<PredictionDetails,Long> {

}
