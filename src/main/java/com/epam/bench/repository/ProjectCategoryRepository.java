package com.epam.bench.repository;

import com.epam.bench.domain.ProjectCategory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectCategory entity.
 */
@SuppressWarnings("unused")
public interface ProjectCategoryRepository extends JpaRepository<ProjectCategory,Long> {

}
