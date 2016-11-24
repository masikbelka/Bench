package com.epam.bench.facades.impl;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.epam.bench.domain.Employee;
import com.epam.bench.domain.ProjectHistory;
import com.epam.bench.facades.ProjectHistoryFacade;
import com.epam.bench.facades.integration.UpsaFacade;
import com.epam.bench.service.EmployeeService;
import com.epam.bench.service.ProjectHistoryService;

/**
 * Created by Tetiana_Antonenko1
 */
@Service
public class DefaultProjectHistoryFacade implements ProjectHistoryFacade {

    private final Logger LOG = LoggerFactory.getLogger(DefaultProjectHistoryFacade.class);

    @Inject
    private ProjectHistoryService projectHistoryService;
    @Inject
    private EmployeeService employeeService;
    @Inject
    private UpsaFacade upsaFacade;

    @Override
    public void removeAll(Set<ProjectHistory> projectsWorkloads) {
        LOG.debug("Removing {} workloads from employee", projectsWorkloads.size());
        for (ProjectHistory projectsWorkload : projectsWorkloads) {
            projectHistoryService.delete(projectsWorkload.getId());
        }
    }

    @Override
    public Set<ProjectHistory> saveAll(Set<ProjectHistory> projectsWorkloads) {
        Set<ProjectHistory> result = Collections.emptySet();
        if (CollectionUtils.isNotEmpty(projectsWorkloads)) {
            LOG.debug("Saving expected {} workloads for employee", projectsWorkloads.size());
            for (ProjectHistory workload : projectsWorkloads) {
                result.add(projectHistoryService.save(workload));
            }
        }
        LOG.debug("Saving actual {} workloads for employee", result.size());

        return result;
    }

    @Override
    public Set<ProjectHistory> getAndUpdateEmployeeWorkload(String upsaId) {
        Employee byUpsaId = employeeService.findByUpsaId(upsaId);
        return getAndUpdateEmployeeWorkload(byUpsaId);
    }

    @Override
    public Set<ProjectHistory> getAndUpdateEmployeeWorkload(Employee employee) {
        LOG.debug("Start update employee {} workloads", employee.getUpsaId());
        Set<ProjectHistory> projectsWorkloads = employee.getProjectsWorkloads();
        removeAll(projectsWorkloads);

        Set<ProjectHistory> currentWorkloads = upsaFacade.getEmployeeWorkloads(employee.getUpsaId());

        return saveAll(currentWorkloads);
    }

}
