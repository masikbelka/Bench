package com.epam.bench.repository;

import com.epam.bench.domain.Opportunity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Opportunity entity.
 */
@SuppressWarnings("unused")
public interface OpportunityRepository extends JpaRepository<Opportunity,Long> {

}
