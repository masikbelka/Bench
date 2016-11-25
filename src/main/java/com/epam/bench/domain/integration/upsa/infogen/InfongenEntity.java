package com.epam.bench.domain.integration.upsa.infogen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.epam.bench.domain.integration.upsa.Entity;
import com.epam.bench.domain.integration.upsa.serializer.FullDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfongenEntity extends Entity implements Serializable{
    private String externalId;
    private String shortName;
    private InfongenEntityType type;
    private Date lastModified;
    private boolean deleted;
    private List<InfongenEntity> parents;

    public InfongenEntity() {
    }

    public InfongenEntity(BigDecimal id) {
        super(id);
    }

    public InfongenEntity(BigDecimal id, String name, String externalId) {
        super(id, name);
        this.externalId = externalId;
    }

    public InfongenEntity(BigDecimal id, String name, String externalId, BigDecimal typeId, String typeName) {
        super(id, name);
        this.externalId = externalId;
        this.type = new InfongenEntityType(typeId, typeName);
    }

    public String getExternalId() {
        return this.externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public InfongenEntityType getType() {
        return this.type;
    }

    public void setType(InfongenEntityType type) {
        this.type = type;
    }

    @JsonSerialize(
        using = FullDateSerializer.class
    )
    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<InfongenEntity> getParents() {
        return this.parents;
    }

    public void setParents(List<InfongenEntity> parents) {
        this.parents = parents;
    }

    @JsonIgnore
    public BigDecimal getIdentifier() {
        return this.type.getId();
    }
}
