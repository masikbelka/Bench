package com.epam.bench.repository;

import com.epam.bench.domain.LanguageLevel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LanguageLevel entity.
 */
@SuppressWarnings("unused")
public interface LanguageLevelRepository extends JpaRepository<LanguageLevel,Long> {

}
