package com.epam.bench.facades.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.epam.bench.domain.BenchHistory;
import com.epam.bench.domain.Employee;
import com.epam.bench.domain.User;
import com.epam.bench.facades.BenchHistoryFacade;
import com.epam.bench.facades.EmployeeFacade;
import com.epam.bench.facades.populators.Populator;
import com.epam.bench.security.SecurityUtils;
import com.epam.bench.service.BenchHistoryService;
import com.epam.bench.service.UserService;
import com.epam.bench.service.dto.bench.CommentHistoryDto;
import com.epam.bench.service.dto.bench.EmployeeSimpleViewDto;
import com.epam.bench.service.dto.bench.form.UpdateEmployeeFormDto;
import com.epam.bench.service.EmployeeService;
import com.epam.bench.service.dto.bench.EmployeeDto;
import com.epam.bench.service.util.ServiceUtil;

/**
 * Created by Tetiana_Antonenko1
 */
@Service
public class DefaultEmployeeFacade implements EmployeeFacade {

    private final Logger LOG = LoggerFactory.getLogger(DefaultEmployeeFacade.class);

    @Inject
    private EmployeeService employeeService;
    @Inject
    private UserService userService;
    @Inject
    private Populator<Employee, EmployeeDto> employeeDtoPopulator;
    @Inject
    private BenchHistoryFacade benchHistoryFacade;

    @Override
    public Page<EmployeeDto> findAll(Pageable pageable) {
        Page<Employee> all = employeeService.findAll(pageable);
        List<Employee> content = all.getContent();

        List<EmployeeDto> employeeDtos = convertEmployeesToEmployeeDtos(content);

        return new PageImpl<>(employeeDtos, pageable, all.getTotalElements());
    }

    @Override
    public void removeFromBench(String upsaId) {
        ServiceUtil.validateParameterNotBlank(upsaId);

        final Employee employee = employeeService.findByUpsaId(upsaId);
        final Optional<BenchHistory> oHistory = benchHistoryFacade.getLastHistoryEntry(employee);
        final User user = userService.getUserWithAuthorities();

        BenchHistory history = oHistory.orElseGet(() -> benchHistoryFacade.createNewEntry(employee, user));
        benchHistoryFacade.releaseEmployeeFromBench(history, user);

    }

    @Override
    public Optional<EmployeeDto> getBenchEmployee(String upsaId) {
        ServiceUtil.validateParameterNotBlank(upsaId);
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
        return new EmployeeDto();
    }

    @Override
    public CommentHistoryDto getBenchEmployeeCommentHistory(String upsaId) {
        return new CommentHistoryDto();
    }

    @Override
    public List<EmployeeSimpleViewDto> suggestEmployees(String query) {
        return new ArrayList<>();
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
}
