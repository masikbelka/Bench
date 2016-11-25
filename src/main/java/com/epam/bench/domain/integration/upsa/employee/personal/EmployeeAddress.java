package com.epam.bench.domain.integration.upsa.employee.personal;

import java.math.BigDecimal;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeAddress {
    private BigDecimal itemId = null;
    private BigDecimal addressId = null;
    private BigDecimal addressTypeId = null;
    private String addressTypeName = null;
    private BigDecimal territoryId = null;
    private String territoryName = null;
    @Size(
        max = 50,
        message = "{validation.error.lengthValueOfField}"
    )
    private String city = null;
    @Size(
        max = 30,
        message = "{validation.error.lengthValueOfField}"
    )
    private String postBox = null;
    @Size(
        max = 50,
        message = "{validation.error.lengthValueOfField}"
    )
    private String street = null;
    @Size(
        max = 50,
        message = "{validation.error.lengthValueOfField}"
    )
    private String building = null;
    @Size(
        max = 10,
        message = "{validation.error.lengthValueOfField}"
    )
    private String room = null;
    @Size(
        max = 20,
        message = "{validation.error.lengthValueOfField}"
    )
    private String zipCode = null;
    private boolean isPrimary = false;

    public EmployeeAddress() {
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

    public BigDecimal getAddressTypeId() {
        return addressTypeId;
    }

    public void setAddressTypeId(BigDecimal addressTypeId) {
        this.addressTypeId = addressTypeId;
    }

    public String getAddressTypeName() {
        return addressTypeName;
    }

    public void setAddressTypeName(String addressTypeName) {
        this.addressTypeName = addressTypeName;
    }

    public BigDecimal getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(BigDecimal territoryId) {
        this.territoryId = territoryId;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostBox() {
        return postBox;
    }

    public void setPostBox(String postBox) {
        this.postBox = postBox;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
