package com.epam.bench.repository;

import com.epam.bench.domain.ProductionStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductionStatus entity.
 */
@SuppressWarnings("unused")
public interface ProductionStatusRepository extends JpaRepository<ProductionStatus,Long> {

}
