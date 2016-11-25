package com.epam.bench.domain.integration.upsa.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeProjectParticipation implements Serializable {

    public static final String PARTIPATION_DELIMITER = "#";
    private BigDecimal projectId;
    private String projectName;
    private List<String> participationList;
    private String participation;

    public EmployeeProjectParticipation() {
    }

    public EmployeeProjectParticipation(List<String> participationList, String projectName) {
        this.participationList = participationList;
        this.projectName = projectName;
    }

    @JsonIgnore
    public String getParticipation() {
        return this.participation;
    }

    public void setParticipationList(List<String> participationList) {
        this.participationList = participationList;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BigDecimal getProjectId() {
        return this.projectId;
    }

    public void setProjectId(BigDecimal projectId) {
        this.projectId = projectId;
    }
}
