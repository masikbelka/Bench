package com.epam.bench.domain.integration.upsa.infogen;

import java.math.BigDecimal;

import com.epam.bench.domain.integration.upsa.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfongenEntityType extends Entity{

    private String pathName;

    public InfongenEntityType() {
    }
    public InfongenEntityType(BigDecimal typeId) {
        super(typeId);
    }

    public InfongenEntityType(BigDecimal id, String name) {
        super(id, name);
    }
    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}
