package com.epam.bench.domain.integration.upsa.employee;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeEducation implements Serializable {
    private Integer educationYear;
    private String educationInstitution = "";
    private BigDecimal educationInstitutionId;
    private String educationFaculty = "";
    private BigDecimal educationFacultyId;
    private String educationDepartment = "";
    private BigDecimal educationDepartmentId;
    private String educationSpeciality = "";
    private String educationDegree = "";
    private boolean isEducationIncomplete;
    private BigDecimal employeeId;
    private BigDecimal educationId;
    private boolean educationNoDegreeExist;

    public EmployeeEducation() {
    }

    public Integer getEducationYear() {
        return educationYear;
    }

    public void setEducationYear(Integer educationYear) {
        this.educationYear = educationYear;
    }

    public String getEducationInstitution() {
        return educationInstitution;
    }

    public void setEducationInstitution(String educationInstitution) {
        this.educationInstitution = educationInstitution;
    }

    public BigDecimal getEducationInstitutionId() {
        return educationInstitutionId;
    }

    public void setEducationInstitutionId(BigDecimal educationInstitutionId) {
        this.educationInstitutionId = educationInstitutionId;
    }

    public String getEducationFaculty() {
        return educationFaculty;
    }

    public void setEducationFaculty(String educationFaculty) {
        this.educationFaculty = educationFaculty;
    }

    public BigDecimal getEducationFacultyId() {
        return educationFacultyId;
    }

    public void setEducationFacultyId(BigDecimal educationFacultyId) {
        this.educationFacultyId = educationFacultyId;
    }

    public String getEducationDepartment() {
        return educationDepartment;
    }

    public void setEducationDepartment(String educationDepartment) {
        this.educationDepartment = educationDepartment;
    }

    public BigDecimal getEducationDepartmentId() {
        return educationDepartmentId;
    }

    public void setEducationDepartmentId(BigDecimal educationDepartmentId) {
        this.educationDepartmentId = educationDepartmentId;
    }

    public String getEducationSpeciality() {
        return educationSpeciality;
    }

    public void setEducationSpeciality(String educationSpeciality) {
        this.educationSpeciality = educationSpeciality;
    }

    public String getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(String educationDegree) {
        this.educationDegree = educationDegree;
    }

    public boolean isEducationIncomplete() {
        return isEducationIncomplete;
    }

    public void setEducationIncomplete(boolean educationIncomplete) {
        isEducationIncomplete = educationIncomplete;
    }

    public BigDecimal getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(BigDecimal employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getEducationId() {
        return educationId;
    }

    public void setEducationId(BigDecimal educationId) {
        this.educationId = educationId;
    }

    public boolean isEducationNoDegreeExist() {
        return educationNoDegreeExist;
    }

    public void setEducationNoDegreeExist(boolean educationNoDegreeExist) {
        this.educationNoDegreeExist = educationNoDegreeExist;
    }
}
