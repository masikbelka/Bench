package com.epam.bench.facades.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.epam.bench.domain.BenchHistory;
import com.epam.bench.domain.Employee;
import com.epam.bench.facades.BenchHistoryFacade;
import com.epam.bench.service.BenchHistoryService;

/**
 * Created by Tetiana_Antonenko1
 */
@Service
public class DefaultBenchHistoryFacade implements BenchHistoryFacade {

    @Inject
    private BenchHistoryService benchHistoryService;

    @Override
    public Optional<BenchHistory> getLastHistoryEntry(Employee employee) {
        Optional<List<BenchHistory>> oHistories = Optional.ofNullable(benchHistoryService.find(employee));
        Comparator<BenchHistory> byDate = Comparator.comparing(BenchHistory::getCreatedTime);
        Optional<BenchHistory> history = Optional.empty();

        if (oHistories.isPresent() && CollectionUtils.isNotEmpty(oHistories.get())) {
            List<BenchHistory> histories = oHistories.get();
            histories.sort(byDate);
            history = Optional.of(histories.get(histories.size() - 1));
        }
        return history;
    }
}