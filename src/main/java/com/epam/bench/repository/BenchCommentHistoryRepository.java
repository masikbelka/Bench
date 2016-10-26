package com.epam.bench.repository;

import com.epam.bench.domain.BenchCommentHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BenchCommentHistory entity.
 */
@SuppressWarnings("unused")
public interface BenchCommentHistoryRepository extends JpaRepository<BenchCommentHistory,Long> {

}
