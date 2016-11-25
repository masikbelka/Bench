package com.epam.bench.domain.integration.upsa.employee.personal;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.epam.bench.domain.integration.upsa.validation.constraints.ErrorMessageAnnotation;
import com.epam.bench.domain.integration.upsa.validation.constraints.Required;

import com.epam.bench.domain.integration.upsa.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeChild extends Entity{
    private static final int DEFAULT_NAME_LENGTH = 50;
    private BigDecimal employeeId = null;
    @Required(
        message = "{validation.error.requiredField}"
    )
    @Size(
        max = 50,
        message = "{validation.error.lengthValueOfField}"
    )
    @Pattern(
        regexp = "[^/]*",
        message = "{validation.error.symbolSlash}"
    )
    private String firstName;
    @Size(
        max = 50,
        message = "{validation.error.lengthValueOfField}"
    )
    @Pattern(
        regexp = "[^/]*",
        message = "{validation.error.symbolSlash}"
    )
    private String middleName = null;
    @Required(
        message = "{validation.error.requiredField}"
    )
    @Size(
        max = 50,
        message = "{validation.error.lengthValueOfField}"
    )
    @Pattern(
        regexp = "[^/]*",
        message = "{validation.error.symbolSlash}"
    )
    private String lastName = null;
    private String fullName = null;
    private Date birthday = null;

    public EmployeeChild() {
    }

    @JsonIgnore
    public String getName() {
        StringBuilder fullNameBuf = (new StringBuilder(50)).append(this.getFirstName()).append(" ");
        if(this.getMiddleName() != null) {
            fullNameBuf.append(this.getMiddleName()).append(" ");
        }

        return fullNameBuf.append(this.getLastName()).toString();
    }

    @ErrorMessageAnnotation("employee.first.name")
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @ErrorMessageAnnotation("employee.middle.name")
    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @ErrorMessageAnnotation("employee.last.name")
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public BigDecimal getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(BigDecimal employeeId) {
        this.employeeId = employeeId;
    }

    @JsonIgnore
    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
