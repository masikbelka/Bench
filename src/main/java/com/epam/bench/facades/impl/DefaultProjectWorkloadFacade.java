package com.epam.bench.facades.impl;

import com.epam.bench.domain.Employee;
import com.epam.bench.facades.ProjectWorkloadFacade;

/**
 * Created by Tetiana_Antonenko1
 */
public class DefaultProjectWorkloadFacade implements ProjectWorkloadFacade {

    @Override
    public int getDaysOnBench(Employee employee) {
        return 0;
    }
}
