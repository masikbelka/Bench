package com.epam.bench.repository;

import com.epam.bench.domain.SkillCategory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SkillCategory entity.
 */
@SuppressWarnings("unused")
public interface SkillCategoryRepository extends JpaRepository<SkillCategory,Long> {

}
