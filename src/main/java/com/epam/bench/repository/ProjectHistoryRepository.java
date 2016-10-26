package com.epam.bench.repository;

import com.epam.bench.domain.ProjectHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectHistory entity.
 */
@SuppressWarnings("unused")
public interface ProjectHistoryRepository extends JpaRepository<ProjectHistory,Long> {

}
