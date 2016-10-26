package com.epam.bench.repository;

import com.epam.bench.domain.ProjectRole;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectRole entity.
 */
@SuppressWarnings("unused")
public interface ProjectRoleRepository extends JpaRepository<ProjectRole,Long> {

}
