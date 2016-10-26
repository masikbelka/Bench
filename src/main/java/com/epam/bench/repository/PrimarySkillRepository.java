package com.epam.bench.repository;

import com.epam.bench.domain.PrimarySkill;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PrimarySkill entity.
 */
@SuppressWarnings("unused")
public interface PrimarySkillRepository extends JpaRepository<PrimarySkill,Long> {

}
