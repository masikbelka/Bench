package com.epam.bench.service.integration;

import com.epam.bench.domain.integration.upsa.EmployeeSimpleView;

/**
 * Created by Tetiana_Antonenko1
 */
public interface UpsaService {

    EmployeeSimpleView getEmployee(String employeeId);
}
