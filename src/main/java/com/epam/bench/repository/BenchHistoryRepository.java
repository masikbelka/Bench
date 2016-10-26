package com.epam.bench.repository;

import com.epam.bench.domain.BenchHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BenchHistory entity.
 */
@SuppressWarnings("unused")
public interface BenchHistoryRepository extends JpaRepository<BenchHistory,Long> {

}
