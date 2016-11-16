package com.epam.bench.service.dto.bench;

/**
 * Created by Tetiana_Antonenko
 */
public class ProposedPositionsDto {

    private String status;
    private String opportunityCode;
    private String opportunityType;
    private String opportunityId;

    public ProposedPositionsDto() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpportunityCode() {
        return opportunityCode;
    }

    public void setOpportunityCode(String opportunityCode) {
        this.opportunityCode = opportunityCode;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getOpportunityType() {
        return opportunityType;
    }

    public void setOpportunityType(String opportunityType) {
        this.opportunityType = opportunityType;
    }
}
