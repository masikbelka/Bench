package com.epam.bench.facades;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.epam.bench.service.dto.bench.CommentHistoryDto;
import com.epam.bench.service.dto.bench.EmployeeSimpleViewDto;
import com.epam.bench.service.dto.bench.form.UpdateEmployeeFormDto;
import com.epam.bench.service.dto.bench.EmployeeDto;

/**
 * Created by Tetiana_Antonenko1
 */
public interface EmployeeFacade {

    Page<EmployeeDto> findAll(Pageable pageable);

    void removeFromBench(String upsaId);

    EmployeeDto getBenchEmployee(String upsaId);

    EmployeeDto updateBenchEmployee(UpdateEmployeeFormDto employee);

    EmployeeDto saveEmployeeToBench(String upsaId);

    CommentHistoryDto getBenchEmployeeCommentHistory(String upsaId);

    List<EmployeeSimpleViewDto> suggestEmployees(String query);
}
