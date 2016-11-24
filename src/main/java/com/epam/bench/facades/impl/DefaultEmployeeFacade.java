package com.epam.bench.facades.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.epam.bench.domain.BenchHistory;
import com.epam.bench.domain.Employee;
import com.epam.bench.facades.BenchHistoryFacade;
import com.epam.bench.facades.CommentHistoryFacade;
import com.epam.bench.facades.EmployeeFacade;
import com.epam.bench.facades.ProjectHistoryFacade;
import com.epam.bench.facades.integration.UpsaFacade;
import com.epam.bench.facades.integration.OpportunityFacade;
import com.epam.bench.facades.populators.Populator;
import com.epam.bench.service.EmployeeService;
import com.epam.bench.service.dto.bench.CommentHistoryDto;
import com.epam.bench.service.dto.bench.EmployeeDto;
import com.epam.bench.service.dto.bench.EmployeeSimpleViewDto;
import com.epam.bench.service.dto.bench.form.UpdateEmployeeFormDto;
import com.epam.bench.service.util.ServiceUtils;

/**
 * Created by Tetiana_Antonenko1
 */
@Service
public class DefaultEmployeeFacade implements EmployeeFacade {

    private final Logger LOG = LoggerFactory.getLogger(DefaultEmployeeFacade.class);

    @Inject
    private EmployeeService employeeService;
    @Inject
    private Populator<Employee, EmployeeDto> employeeDtoPopulator;
    @Inject
    private BenchHistoryFacade benchHistoryFacade;
    @Inject
    private ProjectHistoryFacade projectHistoryFacade;
    @Inject
    private UpsaFacade upsaFacade;
    @Inject
    private OpportunityFacade opportunityFacade;
    @Inject
    private CommentHistoryFacade commentHistoryFacade;

    @Override
    public Page<EmployeeDto> findAll(Pageable pageable) {
        Page<Employee> all = employeeService.findAll(pageable);
        List<Employee> content = all.getContent();

        List<EmployeeDto> employeeDtos = convertEmployeesToEmployeeDtos(content);

        return new PageImpl<>(employeeDtos, pageable, all.getTotalElements());
    }

    @Override
    public void removeFromBench(String upsaId) {
        ServiceUtils.validateParameterNotBlank(upsaId);

        final Employee employee = employeeService.findByUpsaId(upsaId);
        final Optional<BenchHistory> oHistory = benchHistoryFacade.getLastHistoryEntry(employee);

        BenchHistory history = oHistory.orElseGet(() -> benchHistoryFacade.createNewEntry(employee));
        benchHistoryFacade.releaseEmployeeFromBench(history);

    }

    @Override
    public Optional<EmployeeDto> getBenchEmployee(String upsaId) {
        ServiceUtils.validateParameterNotBlank(upsaId);
        Employee employee = employeeService.findByUpsaId(upsaId);
        if (isEmployeeOnBench(employee)) {
            return Optional.of(convertEmployeeDto(employee));
        }
        return Optional.empty();
    }

    @Override
    public boolean isEmployeeOnBench(Employee employee) {
        Optional<BenchHistory> history = benchHistoryFacade.getLastHistoryEntry(employee);

        return history.map(benchHistory -> benchHistory.isBench() && Objects.isNull(benchHistory.getValidTo())).orElse(false);
    }

    @Override
    public EmployeeDto updateBenchEmployee(UpdateEmployeeFormDto employee) {
        return new EmployeeDto();
    }

    @Override
    public EmployeeDto saveEmployeeToBench(String upsaId) {
        ServiceUtils.validateParameterNotBlank(upsaId);

        Optional<Employee> oUpsaEmployee = upsaFacade.getEmployee(upsaId);
        Employee dbEmployee = employeeService.findByUpsaId(upsaId);
        Employee employee = null;
        if (oUpsaEmployee.isPresent()) {
            Employee upsaEmployee = oUpsaEmployee.get();
            if (Objects.isNull(dbEmployee)) {
                employee = employeeService.save(upsaEmployee);
                benchHistoryFacade.createNewEntry(employee);
            } else {
                updateSignificantEmployeeFields(dbEmployee, upsaEmployee);
                employee = employeeService.save(dbEmployee);
                benchHistoryFacade.createNewEntry(employee);
            }
            employee.setProjectsWorkloads(projectHistoryFacade.getAndUpdateEmployeeWorkload(upsaId));
            employee.setOpportunityPositions(opportunityFacade.getOpportunities(upsaId));
        }

        return convertEmployeeDto(employee);
    }

    @Override
    public Optional<List<CommentHistoryDto>> getBenchEmployeeCommentHistory(String upsaId) {
        ServiceUtils.validateParameterNotBlank(upsaId);

        return commentHistoryFacade.getAll(upsaId);
    }

    @Override
    public List<EmployeeSimpleViewDto> suggestEmployees(String query) {
        return upsaFacade.getSuggestedEmployees(query);
    }

    private List<EmployeeDto> convertEmployeesToEmployeeDtos(Iterable<Employee> employees) {
        final List<EmployeeDto> resultEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            resultEmployees.add(convertEmployeeDto(employee));
        }
        return resultEmployees;
    }

    private EmployeeDto convertEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDtoPopulator.populate(employee, employeeDto);
        return employeeDto;
    }

    private void updateSignificantEmployeeFields(Employee dbEmployee, Employee upsaEmployee) {
        dbEmployee.setActive(upsaEmployee.isActive());
        dbEmployee.setManagerId(upsaEmployee.getManagerId());
        dbEmployee.setManagerFullName(upsaEmployee.getManagerFullName());
        dbEmployee.setFullName(upsaEmployee.getFullName());
        dbEmployee.setEnglishLevel(upsaEmployee.getEnglishLevel());
        dbEmployee.setUnit(upsaEmployee.getUnit());
        dbEmployee.setPrimarySkill(upsaEmployee.getPrimarySkill());
        dbEmployee.setTitle(upsaEmployee.getTitle());
        dbEmployee.setJobFunction(upsaEmployee.getJobFunction());
        dbEmployee.setProductionStatus(upsaEmployee.getProductionStatus());
        dbEmployee.setLocation(upsaEmployee.getLocation());
    }
}
