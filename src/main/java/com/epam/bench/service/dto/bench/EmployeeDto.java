package com.epam.bench.service.dto.bench;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tetiana_Antonenko.
 */
public class EmployeeDto {

    private String upsaId;
    private String fullName;
    private String manager;
    private String managerId;

    private String skill;
    private String skillId;

    private TitleDto title;
    private LanguageLevelDto languageLevel;
    private String availableFrom;
    private String availableTill;
    private List<ProjectWorkloadDto> workload = new ArrayList<>();

    private List<ProposedPositionsDto> proposedPositions = new ArrayList<>();
    private String comment;
    private int daysOnBench;
    private String probability;

    public EmployeeDto() {
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

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
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

    public TitleDto getTitle() {
        return title;
    }

    public void setTitle(TitleDto title) {
        this.title = title;
    }

    public LanguageLevelDto getLanguageLevel() {
        return languageLevel;
    }

    public void setLanguageLevel(LanguageLevelDto languageLevel) {
        this.languageLevel = languageLevel;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }

    public List<ProjectWorkloadDto> getWorkload() {
        return workload;
    }

    public void setWorkload(List<ProjectWorkloadDto> workload) {
        this.workload = workload;
    }

    public List<ProposedPositionsDto> getProposedPositions() {
        return proposedPositions;
    }

    public void setProposedPositions(List<ProposedPositionsDto> proposedPositions) {
        this.proposedPositions = proposedPositions;
    }

    public String getAvailableTill() {
        return availableTill;
    }

    public void setAvailableTill(String availableTill) {
        this.availableTill = availableTill;
    }

    public int getDaysOnBench() {
        return daysOnBench;
    }

    public void setDaysOnBench(int daysOnBench) {
        this.daysOnBench = daysOnBench;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }
}
