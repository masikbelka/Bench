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
 * A BenchCommentHistory.
 */
@Entity
@Table(name = "bench_comment_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "benchcommenthistory")
public class BenchCommentHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "change_time", nullable = false)
    private ZonedDateTime changeTime;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getChangeTime() {
        return changeTime;
    }

    public BenchCommentHistory changeTime(ZonedDateTime changeTime) {
        this.changeTime = changeTime;
        return this;
    }

    public void setChangeTime(ZonedDateTime changeTime) {
        this.changeTime = changeTime;
    }

    public String getOldValue() {
        return oldValue;
    }

    public BenchCommentHistory oldValue(String oldValue) {
        this.oldValue = oldValue;
        return this;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public BenchCommentHistory newValue(String newValue) {
        this.newValue = newValue;
        return this;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public User getUser() {
        return user;
    }

    public BenchCommentHistory user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee getEmployee() {
        return employee;
    }

    public BenchCommentHistory employee(Employee employee) {
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
        BenchCommentHistory benchCommentHistory = (BenchCommentHistory) o;
        if(benchCommentHistory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, benchCommentHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BenchCommentHistory{" +
            "id=" + id +
            ", changeTime='" + changeTime + "'" +
            ", oldValue='" + oldValue + "'" +
            ", newValue='" + newValue + "'" +
            '}';
    }
}
