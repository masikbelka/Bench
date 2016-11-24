package com.epam.bench.facades.integration.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.epam.bench.domain.Employee;
import com.epam.bench.domain.LanguageLevel;
import com.epam.bench.domain.ProjectHistory;
import com.epam.bench.facades.integration.UpsaFacade;
import com.epam.bench.service.dto.bench.EmployeeSimpleViewDto;
import com.epam.bench.service.integration.UpsaService;

/**
 * Created by Tetiana_Antonenko1
 */
@Service
public class DefaultUpsaFacade implements UpsaFacade {

    @Inject
    private UpsaService upsaService;

    @Override
    public Optional<Employee> getEmployee(String upsaId) {
        //TODO implement
        return Optional.empty();
    }

    @Override
    public LanguageLevel getEnglishLevelAssessment(String upsaId) {
        //TODO implement
        return null;
    }

    @Override
    public Set<ProjectHistory> getEmployeeWorkloads(String upsaId) {
        //TODO implement
        return Collections.emptySet();
    }

    @Override
    public List<EmployeeSimpleViewDto> getSuggestedEmployees(String query) {
        //TODO implement
        return Collections.emptyList();
    }
}
