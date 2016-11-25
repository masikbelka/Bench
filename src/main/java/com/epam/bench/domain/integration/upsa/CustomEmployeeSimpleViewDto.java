package com.epam.bench.domain.integration.upsa;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomEmployeeSimpleViewDto extends EmployeeSimpleView {
    private String firstName;
    private String dottedManager;
    private String lastName;
    private BigDecimal statusId;
    private BigDecimal managerId;
    private BigDecimal dottedManagerId;
    private BigDecimal categoryId;
    private BigDecimal reporterId;
    private boolean active = true;
    private long costObjectId;
    private String costObject;
    private long orgUnitId;
    private String orgUnitName;
    @JsonIgnore
    private Date birthDate;
    @JsonIgnore
    private Date hireDate;
    @JsonIgnore
    private Date dismissalDate;
    @JsonIgnore
    private Date preAcquisitionStartDate;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDottedManager() {
        return dottedManager;
    }

    public void setDottedManager(String dottedManager) {
        this.dottedManager = dottedManager;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getStatusId() {
        return statusId;
    }

    public void setStatusId(BigDecimal statusId) {
        this.statusId = statusId;
    }

    public BigDecimal getManagerId() {
        return managerId;
    }

    public void setManagerId(BigDecimal managerId) {
        this.managerId = managerId;
    }

    public BigDecimal getDottedManagerId() {
        return dottedManagerId;
    }

    public void setDottedManagerId(BigDecimal dottedManagerId) {
        this.dottedManagerId = dottedManagerId;
    }

    public BigDecimal getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(BigDecimal categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getReporterId() {
        return reporterId;
    }

    public void setReporterId(BigDecimal reporterId) {
        this.reporterId = reporterId;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    public long getCostObjectId() {
        return costObjectId;
    }

    public void setCostObjectId(long costObjectId) {
        this.costObjectId = costObjectId;
    }

    public String getCostObject() {
        return costObject;
    }

    public void setCostObject(String costObject) {
        this.costObject = costObject;
    }

    public long getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(long orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public String getOrgUnitName() {
        return orgUnitName;
    }

    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public Date getHireDate() {
        return hireDate;
    }

    @Override
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getDismissalDate() {
        return dismissalDate;
    }

    public void setDismissalDate(Date dismissalDate) {
        this.dismissalDate = dismissalDate;
    }

    @Override
    public Date getPreAcquisitionStartDate() {
        return preAcquisitionStartDate;
    }

    @Override
    public void setPreAcquisitionStartDate(Date preAcquisitionStartDate) {
        this.preAcquisitionStartDate = preAcquisitionStartDate;
    }


}
