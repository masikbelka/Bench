package com.epam.bench.domain.integration.upsa.employee;

import com.epam.bench.domain.integration.upsa.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProbationSection {
    private Entity probationStatus;
    private String endDate;
    private String actualEndDate;
    private String originalEndDate;
    private Boolean prolonged;
    private String prolongationDate;

    public ProbationSection() {
    }

    public Entity getProbationStatus() {
        return probationStatus;
    }

    public void setProbationStatus(Entity probationStatus) {
        this.probationStatus = probationStatus;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(String actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public String getOriginalEndDate() {
        return originalEndDate;
    }

    public void setOriginalEndDate(String originalEndDate) {
        this.originalEndDate = originalEndDate;
    }

    public Boolean getProlonged() {
        return prolonged;
    }

    public void setProlonged(Boolean prolonged) {
        this.prolonged = prolonged;
    }

    public String getProlongationDate() {
        return prolongationDate;
    }

    public void setProlongationDate(String prolongationDate) {
        this.prolongationDate = prolongationDate;
    }
}
