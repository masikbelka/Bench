package com.epam.bench.facades.populators.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.epam.bench.domain.Employee;
import com.epam.bench.domain.JobFunction;
import com.epam.bench.domain.LanguageLevel;
import com.epam.bench.domain.PrimarySkill;
import com.epam.bench.domain.enumeration.Probability;
import com.epam.bench.facades.ProjectWorkloadFacade;
import com.epam.bench.facades.populators.Populator;
import com.epam.bench.service.dto.bench.EmployeeDto;
import com.epam.bench.service.dto.bench.LanguageLevelDto;
import com.epam.bench.service.dto.bench.ProjectWorkloadDto;
import com.epam.bench.service.dto.bench.ProposedPositionsDto;
import com.epam.bench.service.dto.bench.TitleDto;
import com.epam.bench.service.util.ServiceUtils;

/**
 * Created by Tetiana_Antonenko.
 */
@Component
public class EmployeeDtoPopulator implements Populator<Employee, EmployeeDto> {

    @Inject
    private ProjectWorkloadFacade projectWorkloadFacade;

    @Override
    public void populate(Employee employee, EmployeeDto employeeDto) {
        if (Objects.isNull(employee)) {
            return;
        }
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

        populateProbability(employee, employeeDto);

    }

    private void populateProbability(Employee employee, EmployeeDto employeeDto) {
        Probability probability = employee.getProbability();
        String probabilityString = StringUtils.EMPTY;
        if (Objects.nonNull(probability)) {
            probabilityString = probability.toString();
        }
        employeeDto.setProbability(probabilityString);
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

    private void populateWorkload(Employee employee, EmployeeDto employeeDto) {
        List<ProjectWorkloadDto> workloadDto = new ArrayList<>();
        ProjectWorkloadDto projectWorkloadDto = new ProjectWorkloadDto();
        projectWorkloadDto.setType("Internal Project");
        projectWorkloadDto.setName("EPM-TEST");
        projectWorkloadDto.setWorkload(80);
        projectWorkloadDto.setId("456432154651324621");

        workloadDto.add(projectWorkloadDto);
        employeeDto.setWorkload(workloadDto);
    }

    private void populateProposalPositions(Employee source, EmployeeDto target) {
       /* List<ProposalsPosition> sourcePositions = proposalsPositionDao.findByEmployeeUpsaId(source.getUpsaId());
        List<ProposedPositionsDto> targetPositions = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(sourcePositions)) {
            for (ProposalsPosition sourcePosition : sourcePositions) {
                ProposedPositionsDto targetPosition = new ProposedPositionsDto();
                targetPosition.setStatus(sourcePosition.getStaffingStatus());
                targetPosition.setName(sourcePosition.getName());
                targetPosition.setId(sourcePosition.getId());
                String opportunityContainerType = sourcePosition.getOpportunityContainerType();
                targetPosition.setType(getContainerType(opportunityContainerType));
                targetPositions.add(targetPosition);
            }
        }

        target.setProposedPositions(targetPositions);*/

        List<ProposedPositionsDto> targetPositions = new ArrayList<>();
        ProposedPositionsDto proposedPositionsDto = new ProposedPositionsDto();
        proposedPositionsDto.setName("EPM-OPP");
        proposedPositionsDto.setType("opportunities");
        proposedPositionsDto.setId("12325469897432");
        proposedPositionsDto.setStatus("PROPOSED");
        targetPositions.add(proposedPositionsDto);
        target.setProposedPositions(targetPositions);
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
        return ServiceUtils.getFormattedYearDate(dateToFormat);
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
