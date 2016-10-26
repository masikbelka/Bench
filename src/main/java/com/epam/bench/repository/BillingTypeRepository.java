package com.epam.bench.repository;

import com.epam.bench.domain.BillingType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BillingType entity.
 */
@SuppressWarnings("unused")
public interface BillingTypeRepository extends JpaRepository<BillingType,Long> {

}
