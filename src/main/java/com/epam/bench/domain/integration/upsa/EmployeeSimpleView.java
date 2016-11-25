package com.epam.bench.domain.integration.upsa;

import java.math.BigDecimal;
import java.util.Date;

import com.epam.bench.domain.integration.upsa.employee.ProbationSection;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeSimpleView {
    private BigDecimal employeeId;
    private String fullname;
    private BigDecimal uid;
    private String gender;
    private String title;
    private String status;
    private String category;
    private String location;
    private BigDecimal locationId;
    private String email;
    private String phone;
    private String unitName;
    private BigDecimal unitId;
    private boolean active;
    private String manager;
    private String reporter;
    private Date hireDate;
    private Date preAcquisitionStartDate;
    private String acquiredCompanyName;
    private String jobFunction;
    private Entity primarySkill;
    private ProbationSection probation;

    public EmployeeSimpleView() {
    }

    public BigDecimal getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(BigDecimal employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public BigDecimal getUid() {
        return uid;
    }

    public void setUid(BigDecimal uid) {
        this.uid = uid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public void setLocationId(BigDecimal locationId) {
        this.locationId = locationId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getUnitId() {
        return unitId;
    }

    public void setUnitId(BigDecimal unitId) {
        this.unitId = unitId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getPreAcquisitionStartDate() {
        return preAcquisitionStartDate;
    }

    public void setPreAcquisitionStartDate(Date preAcquisitionStartDate) {
        this.preAcquisitionStartDate = preAcquisitionStartDate;
    }

    public String getAcquiredCompanyName() {
        return acquiredCompanyName;
    }

    public void setAcquiredCompanyName(String acquiredCompanyName) {
        this.acquiredCompanyName = acquiredCompanyName;
    }

    public String getJobFunction() {
        return jobFunction;
    }

    public void setJobFunction(String jobFunction) {
        this.jobFunction = jobFunction;
    }

    public Entity getPrimarySkill() {
        return primarySkill;
    }

    public void setPrimarySkill(Entity primarySkill) {
        this.primarySkill = primarySkill;
    }

    public ProbationSection getProbation() {
        return probation;
    }

    public void setProbation(ProbationSection probation) {
        this.probation = probation;
    }
}
