package com.epam.bench.domain.integration.upsa;

import java.io.Serializable;
import java.util.List;

import com.epam.bench.domain.integration.upsa.common.Location;
import com.epam.bench.domain.integration.upsa.employee.EmployeeEducation;
import com.epam.bench.domain.integration.upsa.employee.EmployeeProjectParticipation;
import com.epam.bench.domain.integration.upsa.employee.EmployeeTraining;
import com.epam.bench.domain.integration.upsa.employee.ProbationSection;
import com.epam.bench.domain.integration.upsa.employee.personal.EmployeeAddress;
import com.epam.bench.domain.integration.upsa.employee.personal.EmployeeChild;
import com.epam.bench.domain.integration.upsa.employee.personal.EmployeePhone;
import com.epam.bench.domain.integration.upsa.skills.SkillCategory;
import com.epam.bench.domain.integration.upsa.unit.Unit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeComposeObject implements Serializable {
    private EmployeeSimpleView employeeSimpleView;

    private List<EmployeeEducation> employeeEducation;
    private List<List<SkillCategory>> skills;
    private List<EmployeeAddress> addresses;
    private List<EmployeeChild> children;
    private List<EmployeeTraining> employeeTrainings;
    private List<EmployeePhone> contacts;
    private Unit unit;
    private Location location;
    private List<EmployeeProjectParticipation> currentProjects;
    private ProbationSection probation;

    public EmployeeComposeObject() {
    }

    public EmployeeSimpleView getEmployeeSimpleView() {
        return employeeSimpleView;
    }

    public void setEmployeeSimpleView(EmployeeSimpleView employeeSimpleView) {
        this.employeeSimpleView = employeeSimpleView;
    }

    public List<EmployeeEducation> getEmployeeEducation() {
        return employeeEducation;
    }

    public void setEmployeeEducation(List<EmployeeEducation> employeeEducation) {
        this.employeeEducation = employeeEducation;
    }

    public List<List<SkillCategory>> getSkills() {
        return skills;
    }

    public void setSkills(List<List<SkillCategory>> skills) {
        this.skills = skills;
    }

    public List<EmployeeAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<EmployeeAddress> addresses) {
        this.addresses = addresses;
    }

    public List<EmployeeChild> getChildren() {
        return children;
    }

    public void setChildren(List<EmployeeChild> children) {
        this.children = children;
    }

    public List<EmployeeTraining> getEmployeeTrainings() {
        return employeeTrainings;
    }

    public void setEmployeeTrainings(List<EmployeeTraining> employeeTrainings) {
        this.employeeTrainings = employeeTrainings;
    }

    public List<EmployeePhone> getContacts() {
        return contacts;
    }

    public void setContacts(List<EmployeePhone> contacts) {
        this.contacts = contacts;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<EmployeeProjectParticipation> getCurrentProjects() {
        return currentProjects;
    }

    public void setCurrentProjects(List<EmployeeProjectParticipation> currentProjects) {
        this.currentProjects = currentProjects;
    }

    public ProbationSection getProbation() {
        return probation;
    }

    public void setProbation(ProbationSection probation) {
        this.probation = probation;
    }
}
