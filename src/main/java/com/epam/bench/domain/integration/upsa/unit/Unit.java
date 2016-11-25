package com.epam.bench.domain.integration.upsa.unit;

import java.math.BigDecimal;

import com.epam.bench.domain.integration.upsa.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Unit extends Entity {
    private BigDecimal ownerId;
    private String ownerName;
    private BigDecimal locationId;
    private boolean damageUnit;
    private String locationName;
    @JsonIgnore
    private boolean osmLocation;
    private BigDecimal costObjectId;
    @JsonIgnore
    private String costObjectName;

    public Unit() {
    }

    public Unit(BigDecimal id) {
        super(id);
    }

    public Unit(BigDecimal id, String name, BigDecimal ownerId) {
        super(id, name);
        this.ownerId = ownerId;
    }

    public BigDecimal getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(BigDecimal ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public void setLocationId(BigDecimal locationId) {
        this.locationId = locationId;
    }

    public boolean isDamageUnit() {
        return damageUnit;
    }

    public void setDamageUnit(boolean damageUnit) {
        this.damageUnit = damageUnit;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public boolean isOsmLocation() {
        return osmLocation;
    }

    public void setOsmLocation(boolean osmLocation) {
        this.osmLocation = osmLocation;
    }

    public BigDecimal getCostObjectId() {
        return costObjectId;
    }

    public void setCostObjectId(BigDecimal costObjectId) {
        this.costObjectId = costObjectId;
    }

    public String getCostObjectName() {
        return costObjectName;
    }

    public void setCostObjectName(String costObjectName) {
        this.costObjectName = costObjectName;
    }
}
