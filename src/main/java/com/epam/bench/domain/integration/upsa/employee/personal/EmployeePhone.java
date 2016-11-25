package com.epam.bench.domain.integration.upsa.employee.personal;

import java.math.BigDecimal;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeePhone {
    private BigDecimal itemId = null;
    private BigDecimal addressId = null;
    private BigDecimal phoneTypeId = null;
    private String phoneTypeName = null;
    @Size(
        max = 50,
        message = "{validation.error.lengthValueOfField}"
    )
    private String phoneNumber = null;
    @Size(
        max = 20,
        message = "{validation.error.lengthValueOfField}"
    )
    @Pattern(
        regexp = "[^/]*",
        message = "{validation.error.symbolSlash}"
    )
    private String phoneExtention = null;
    @Size(
        max = 500,
        message = "{validation.error.lengthValueOfField}"
    )
    private String description = null;

    public EmployeePhone() {
    }

    public BigDecimal getItemId() {
        return itemId;
    }

    public void setItemId(BigDecimal itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getAddressId() {
        return addressId;
    }

    public void setAddressId(BigDecimal addressId) {
        this.addressId = addressId;
    }

    public BigDecimal getPhoneTypeId() {
        return phoneTypeId;
    }

    public void setPhoneTypeId(BigDecimal phoneTypeId) {
        this.phoneTypeId = phoneTypeId;
    }

    public String getPhoneTypeName() {
        return phoneTypeName;
    }

    public void setPhoneTypeName(String phoneTypeName) {
        this.phoneTypeName = phoneTypeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneExtention() {
        return phoneExtention;
    }

    public void setPhoneExtention(String phoneExtention) {
        this.phoneExtention = phoneExtention;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
