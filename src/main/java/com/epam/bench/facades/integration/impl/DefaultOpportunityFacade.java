package com.epam.bench.facades.integration.impl;

import java.util.Collections;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.epam.bench.domain.OpportunityPosition;
import com.epam.bench.facades.integration.OpportunityFacade;

/**
 * Created by Tetiana_Antonenko1
 */
@Service
public class DefaultOpportunityFacade implements OpportunityFacade {

    @Override
    public Set<OpportunityPosition> getOpportunities(String upsaId) {
        //TODO implement
        return Collections.emptySet();
    }
}
