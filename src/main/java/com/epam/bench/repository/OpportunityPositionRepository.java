package com.epam.bench.repository;

import com.epam.bench.domain.OpportunityPosition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OpportunityPosition entity.
 */
@SuppressWarnings("unused")
public interface OpportunityPositionRepository extends JpaRepository<OpportunityPosition,Long> {

}
