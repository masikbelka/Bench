package com.epam.bench.facades.populators.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.epam.bench.domain.Employee;
import com.epam.bench.domain.JobFunction;
import com.epam.bench.domain.LanguageLevel;
import com.epam.bench.domain.PrimarySkill;
import com.epam.bench.domain.Title;
import com.epam.bench.facades.ProjectWorkloadFacade;
import com.epam.bench.facades.populators.Populator;
import com.epam.bench.service.dto.bench.EmployeeDto;
import com.epam.bench.service.dto.bench.LanguageLevelDto;
import com.epam.bench.service.dto.bench.ProposedPositionsDto;
import com.epam.bench.service.dto.bench.TitleDto;
import com.epam.bench.service.util.ServiceUtil;

/**
 * Created by Tetiana_Antonenko.
 */
@Component
public class EmployeeDtoPopulator implements Populator<Employee, EmployeeDto> {

    @Inject
    private ProjectWorkloadFacade projectWorkloadFacade;

    @Override
    public void populate(Employee employee, EmployeeDto employeeDto) {
        employeeDto.setUpsaId(StringUtils.defaultString(employee.getUpsaId()));
        employeeDto.setManagerId(StringUtils.defaultString(employee.getManagerId()));
        employeeDto.setManager(StringUtils.defaultString(employee.getManagerFullName()));

        employeeDto.setFullName(StringUtils.defaultString(employee.getFullName()));

        populateLanguage(employee, employeeDto);

        employeeDto.setAvailableFrom(dateOrDefault(employee.getAvailableFrom()));
        employeeDto.setAvailableTill(dateOrDefault(employee.getAvailableFrom()));

        populateSkill(employee, employeeDto);
        populateTitle(employee, employeeDto);

        employeeDto.setComment(StringUtils.defaultString(employee.getComment()));

        populateProposalPositions(employee, employeeDto);

        populateWorkload(employee, employeeDto);

        employeeDto.setDaysOnBench(projectWorkloadFacade.getDaysOnBench(employee));

    }

    private void populateTitle(Employee employee, EmployeeDto employeeDto) {
        TitleDto titleDto = new TitleDto();
        JobFunction jobFunction = employee.getJobFunction();
        if (Objects.nonNull(jobFunction)) {
            titleDto.setFullTitle(jobFunction.getName());
            titleDto.setShortTitle(jobFunction.getPrefix());
        }
        employeeDto.setTitle(titleDto);
    }

    private void populateSkill(Employee employee, EmployeeDto employeeDto) {
        PrimarySkill skill = employee.getPrimarySkill();
        if (Objects.nonNull(skill)) {
            employeeDto.setSkill(StringUtils.defaultString(skill.getName()));
            employeeDto.setSkillId(StringUtils.defaultString(skill.getUpsaId()));
        }
    }

    private void populateWorkload(Employee source, EmployeeDto target) {

    }

    private void populateProposalPositions(Employee source, EmployeeDto target) {
       /* List<ProposalsPosition> sourcePositions = proposalsPositionDao.findByEmployeeUpsaId(source.getUpsaId());
        List<ProposedPositionsDto> targetPositions = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(sourcePositions)) {
            for (ProposalsPosition sourcePosition : sourcePositions) {
                ProposedPositionsDto targetPosition = new ProposedPositionsDto();
                targetPosition.setStatus(sourcePosition.getStaffingStatus());
                targetPosition.setOpportunityCode(sourcePosition.getOpportunityCode());
                targetPosition.setOpportunityId(sourcePosition.getOpportunityId());
                String opportunityContainerType = sourcePosition.getOpportunityContainerType();
                targetPosition.setOpportunityType(getContainerType(opportunityContainerType));
                targetPositions.add(targetPosition);
            }
        }

        target.setProposedPositions(targetPositions);*/
    }

    private String getContainerType(String opportunityContainerType) {
        String result;
        switch (opportunityContainerType) {
            case "Opportunity":
                result = "opportunities";
                break;
            case "Project":
                result = "projects";
                break;
            default:
                result = "projects";
                break;
        }
        return result;
    }

    private String dateOrDefault(final ZonedDateTime availableFrom) {
        ZonedDateTime dateToFormat = ZonedDateTime.now();
        if (Objects.nonNull(availableFrom)) {
            dateToFormat = availableFrom;
        }
        return ServiceUtil.getFormattedYearDate(dateToFormat);
    }


    private void populateLanguage(Employee employee, EmployeeDto employeeDto) {
        LanguageLevel languageLevel = employee.getEnglishLevel();
        if (Objects.nonNull(languageLevel)) {
            LanguageLevelDto languageLevelDto = new LanguageLevelDto();
            languageLevelDto.setLanguage(languageLevel.getLanguage());
            languageLevelDto.setSpeaking(languageLevel.getSpeaking());
            languageLevelDto.setWriting(languageLevel.getWriting());
            employeeDto.setLanguageLevel(languageLevelDto);
        }

    }
}
