package com.epam.bench.service.dto.bench;

/**
 * Created by Tetiana_Antonenko.
 */
public class EmployeeSimpleViewDto {

    private String upsaId;
    private String fullName;
    private String managerName;
    private String skill;
    private String title;
    private boolean active;

    public EmployeeSimpleViewDto() {
    }

    public EmployeeSimpleViewDto(String upsaId, String fullName) {
        this.upsaId = upsaId;
        this.fullName = fullName;
    }

    public String getUpsaId() {
        return upsaId;
    }

    public void setUpsaId(String upsaId) {
        this.upsaId = upsaId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
