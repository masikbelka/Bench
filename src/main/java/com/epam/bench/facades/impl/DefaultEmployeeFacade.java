package com.epam.bench.facades.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.epam.bench.domain.Employee;
import com.epam.bench.facades.EmployeeFacade;
import com.epam.bench.facades.populators.Populator;
import com.epam.bench.service.dto.bench.CommentHistoryDto;
import com.epam.bench.service.dto.bench.EmployeeSimpleViewDto;
import com.epam.bench.service.dto.bench.form.UpdateEmployeeFormDto;
import com.epam.bench.service.EmployeeService;
import com.epam.bench.service.dto.bench.EmployeeDto;

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

    @Override
    public Page<EmployeeDto> findAll(Pageable pageable) {
        Page<Employee> all = employeeService.findAll(pageable);
        List<Employee> content = all.getContent();

        List<EmployeeDto> employeeDtos = convertEmployeesToEmployeeDtos(content);
        Page<EmployeeDto> dtoPage = new PageImpl<>(employeeDtos, pageable, all.getTotalElements());
        return dtoPage;
    }

    @Override
    public void removeFromBench(String upsaId) {

    }

    @Override
    public EmployeeDto getBenchEmployee(String upsaId) {
        if (StringUtils.isNotBlank(upsaId)) {
            Employee employee = employeeService.findByUpsaId(upsaId);
            return populateNewDtoEmployee(employee);
        }
        return new EmployeeDto();
    }

    @Override
    public EmployeeDto updateBenchEmployee(UpdateEmployeeFormDto employee) {
        return new EmployeeDto();
    }

    @Override
    public EmployeeDto saveEmployeeToBench(String upsaId) {
        return new EmployeeDto();
    }

    private List<EmployeeDto> convertEmployeesToEmployeeDtos(Iterable<Employee> employees) {
        final List<EmployeeDto> resultEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeDto employeeDto = populateNewDtoEmployee(employee);

            resultEmployees.add(employeeDto);
        }
        return resultEmployees;
    }

    private EmployeeDto populateNewDtoEmployee(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDtoPopulator.populate(employee, employeeDto);
        return employeeDto;
    }

    @Override
    public CommentHistoryDto getBenchEmployeeCommentHistory(String upsaId) {
        return new CommentHistoryDto();
    }

    @Override
    public List<EmployeeSimpleViewDto> suggestEmployees(String query) {
        return new ArrayList<>();
    }
}
