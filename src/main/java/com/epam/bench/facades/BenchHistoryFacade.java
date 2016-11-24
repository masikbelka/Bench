package com.epam.bench.facades;

import java.util.Optional;

import com.epam.bench.domain.BenchHistory;
import com.epam.bench.domain.Employee;

/**
 * Created by Tetiana_Antonenko1
 */
public interface BenchHistoryFacade {

    Optional<BenchHistory> getLastHistoryEntry(Employee employee);
}
