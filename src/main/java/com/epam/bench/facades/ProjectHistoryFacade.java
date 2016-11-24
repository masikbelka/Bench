package com.epam.bench.facades;

import java.util.Set;

import com.epam.bench.domain.Employee;
import com.epam.bench.domain.ProjectHistory;

/**
 * Created by Tetiana_Antonenko1
 */
public interface ProjectHistoryFacade {

    void removeAll(Set<ProjectHistory> projectsWorkloads);

    Set<ProjectHistory> saveAll(Set<ProjectHistory> projectsWorkloads);

    Set<ProjectHistory> getAndUpdateEmployeeWorkload(String upsaId);

    Set<ProjectHistory> getAndUpdateEmployeeWorkload(Employee employee);
}
