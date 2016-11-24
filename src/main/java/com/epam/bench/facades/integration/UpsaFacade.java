package com.epam.bench.facades.integration;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.epam.bench.domain.Employee;
import com.epam.bench.domain.LanguageLevel;
import com.epam.bench.domain.ProjectHistory;
import com.epam.bench.service.dto.bench.EmployeeSimpleViewDto;

/**
 * Created by Tetiana_Antonenko1
 */
@Service
public interface UpsaFacade {

    Optional<Employee> getEmployee(String upsaId);

    LanguageLevel getEnglishLevelAssessment(String upsaId);

    Set<ProjectHistory> getEmployeeWorkloads(String upsaId);

    List<EmployeeSimpleViewDto> getSuggestedEmployees(String query);
}
