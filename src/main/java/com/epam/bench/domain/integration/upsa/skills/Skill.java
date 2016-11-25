package com.epam.bench.domain.integration.upsa.skills;

import java.math.BigDecimal;

import com.epam.bench.domain.integration.upsa.infogen.InfongenEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Tetiana_Antonenko1
 */
public class Skill {
    @JsonIgnore
    private InfongenEntity skillEntity = new InfongenEntity();

    public Skill() {
    }

    public BigDecimal getSkillId() {

        return this.skillEntity.getId();
    }

    public void setSkillId(BigDecimal skillId) {
        this.skillEntity.setId(skillId);
    }

    public String getSkillName() {
        return this.skillEntity.getName();
    }

    public void setSkillName(String skillName) {
        this.skillEntity.setName(skillName);
    }
}
