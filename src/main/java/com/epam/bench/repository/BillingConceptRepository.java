package com.epam.bench.repository;

import com.epam.bench.domain.BillingConcept;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BillingConcept entity.
 */
@SuppressWarnings("unused")
public interface BillingConceptRepository extends JpaRepository<BillingConcept,Long> {

}
