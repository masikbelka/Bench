package com.epam.bench.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SkillCategory.
 */
@Entity
@Table(name = "skill_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "skillcategory")
public class SkillCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "skillCategory")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PrimarySkill> children = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SkillCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public SkillCategory color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<PrimarySkill> getChildren() {
        return children;
    }

    public SkillCategory children(Set<PrimarySkill> primarySkills) {
        this.children = primarySkills;
        return this;
    }

    public SkillCategory addChildren(PrimarySkill primarySkill) {
        children.add(primarySkill);
        primarySkill.setSkillCategory(this);
        return this;
    }

    public SkillCategory removeChildren(PrimarySkill primarySkill) {
        children.remove(primarySkill);
        primarySkill.setSkillCategory(null);
        return this;
    }

    public void setChildren(Set<PrimarySkill> primarySkills) {
        this.children = primarySkills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SkillCategory skillCategory = (SkillCategory) o;
        if(skillCategory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, skillCategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SkillCategory{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", color='" + color + "'" +
            '}';
    }
}
