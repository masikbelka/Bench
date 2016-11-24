package com.epam.bench.facades.integration;

import java.util.Set;

import com.epam.bench.domain.OpportunityPosition;

/**
 * Created by Tetiana_Antonenko1
 */
public interface OpportunityFacade {

    Set<OpportunityPosition> getOpportunities(String upsaId);
}
