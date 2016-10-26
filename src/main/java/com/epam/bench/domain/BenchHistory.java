package com.epam.bench.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BenchHistory.
 */
@Entity
@Table(name = "bench_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "benchhistory")
public class BenchHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "created_time", nullable = false)
    private ZonedDateTime createdTime;

    @NotNull
    @Column(name = "bench", nullable = false)
    private Boolean bench;

    @Column(name = "manager_id")
    private String managerId;

    @Column(name = "valid_to")
    private ZonedDateTime validTo;

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public BenchHistory createdTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Boolean isBench() {
        return bench;
    }

    public BenchHistory bench(Boolean bench) {
        this.bench = bench;
        return this;
    }

    public void setBench(Boolean bench) {
        this.bench = bench;
    }

    public String getManagerId() {
        return managerId;
    }

    public BenchHistory managerId(String managerId) {
        this.managerId = managerId;
        return this;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public ZonedDateTime getValidTo() {
        return validTo;
    }

    public BenchHistory validTo(ZonedDateTime validTo) {
        this.validTo = validTo;
        return this;
    }

    public void setValidTo(ZonedDateTime validTo) {
        this.validTo = validTo;
    }

    public Employee getEmployee() {
        return employee;
    }

    public BenchHistory employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BenchHistory benchHistory = (BenchHistory) o;
        if(benchHistory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, benchHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BenchHistory{" +
            "id=" + id +
            ", createdTime='" + createdTime + "'" +
            ", bench='" + bench + "'" +
            ", managerId='" + managerId + "'" +
            ", validTo='" + validTo + "'" +
            '}';
    }
}
