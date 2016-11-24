package com.epam.bench.facades.impl;

import org.springframework.stereotype.Service;

import com.epam.bench.domain.Employee;
import com.epam.bench.facades.ProjectWorkloadFacade;

/**
 * Created by Tetiana_Antonenko1
 */
@Service
public class DefaultProjectWorkloadFacade implements ProjectWorkloadFacade {

    @Override
    public int getDaysOnBench(Employee employee) {
        return 0;
    }
}
