package com.epam.bench.domain.integration.upsa.common;

import java.math.BigDecimal;

import com.epam.bench.domain.integration.upsa.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location extends Entity{

    public Location(BigDecimal id) {
        super(id);
    }

    public Location(BigDecimal id, String name) {
        super(id, name);
    }
}
