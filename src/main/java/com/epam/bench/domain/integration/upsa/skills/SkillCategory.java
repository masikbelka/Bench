package com.epam.bench.domain.integration.upsa.skills;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Tetiana_Antonenko1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillCategory extends Skill {


    @XmlElementRef(name = "childSkills")
    private List<Skill> childSkills = new ArrayList();
    @JsonIgnore
    private int level;
    @JsonIgnore
    private boolean hasLeaf;
    private String shortSkillName;

    public SkillCategory() {
    }

    public List<Skill> getChildSkills() {
        return childSkills;
    }

    public void setChildSkills(List<Skill> childSkills) {
        this.childSkills = childSkills;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isHasLeaf() {
        return hasLeaf;
    }

    public void setHasLeaf(boolean hasLeaf) {
        this.hasLeaf = hasLeaf;
    }

    public String getShortSkillName() {
        return shortSkillName;
    }

    public void setShortSkillName(String shortSkillName) {
        this.shortSkillName = shortSkillName;
    }
}
