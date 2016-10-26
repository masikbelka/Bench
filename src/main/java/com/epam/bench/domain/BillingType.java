package com.epam.bench.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BillingType.
 */
@Entity
@Table(name = "billing_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "billingtype")
public class BillingType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "upsa_id", nullable = false)
    private String upsaId;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUpsaId() {
        return upsaId;
    }

    public BillingType upsaId(String upsaId) {
        this.upsaId = upsaId;
        return this;
    }

    public void setUpsaId(String upsaId) {
        this.upsaId = upsaId;
    }

    public String getName() {
        return name;
    }

    public BillingType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BillingType billingType = (BillingType) o;
        if(billingType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, billingType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BillingType{" +
            "id=" + id +
            ", upsaId='" + upsaId + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
