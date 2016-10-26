package com.epam.bench.repository;

import com.epam.bench.domain.OpportunityType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OpportunityType entity.
 */
@SuppressWarnings("unused")
public interface OpportunityTypeRepository extends JpaRepository<OpportunityType,Long> {

}
