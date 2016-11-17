package com.epam.bench.service.dto.bench.form;

import javax.validation.constraints.Size;

import com.epam.bench.service.dto.bench.LanguageLevelDto;

/**
 * Created by Tetiana_Antonenko.
 */
public class UpdateEmployeeFormDto {

    private String upsaId;
    private String managerName;
    private String fullName;
    private String skill;
    private String skillId;
    private String title;
    private LanguageLevelDto language;
    private String status;
    private String availableFrom;
    private String pastProjects;
    private String selectedDate;
    @Size(max=256)
    private String comment;

    public String getUpsaId() {
        return upsaId;
    }

    public void setUpsaId(String upsaId) {
        this.upsaId = upsaId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LanguageLevelDto getLanguage() {
        return language;
    }

    public void setLanguage(LanguageLevelDto language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }

    public String getPastProjects() {
        return pastProjects;
    }

    public void setPastProjects(String pastProjects) {
        this.pastProjects = pastProjects;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }
}
