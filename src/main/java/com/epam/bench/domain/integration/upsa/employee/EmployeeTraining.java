package com.epam.bench.domain.integration.upsa.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epam.bench.domain.integration.upsa.common.SystemException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Tetiana_Antonenko1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeTraining implements Serializable {
    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";
    public static final String FTP_PREFIX = "ftp://";
    private static final String YEAR_PATTERN = "yyyy";
    private BigDecimal employeeId = null;
    private String employeeName;
    private BigDecimal trainingId = null;
    private String trainingName = null;
    private String trainingUrl = null;
    private BigDecimal externalTrainingId;
    private boolean auto;
    private Date trainingDate;
    private BigDecimal trainerId;
    @JsonIgnore
    private String trainerName;

    public EmployeeTraining() {
    }

    public boolean isTrainer(BigDecimal employeeId) {
        return employeeId.equals(this.trainerId);
    }

    public BigDecimal getTrainerId() {
        return this.trainerId;
    }

    public void setTrainerId(BigDecimal trainerId) {
        this.trainerId = trainerId;
    }

    @JsonIgnore
    public BigDecimal getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(BigDecimal employeeId) {
        this.employeeId = employeeId;
    }

    @JsonProperty("id")
    public BigDecimal getTrainingId() {
        return this.trainingId;
    }

    public void setTrainingId(BigDecimal trainingId) {
        this.trainingId = trainingId;
    }

    @JsonProperty("name")
    public String getTrainingName() {
        return this.trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingUrl() {
        return this.trainingUrl;
    }

    public void setTrainingUrl(String trainingUrl) {
        this.trainingUrl = trainingUrl;
    }

    @JsonIgnore
    public String getTrainingHttpUrl() {
        return this.trainingUrl == null?null:(!this.trainingUrl.startsWith("http://") && !this.trainingUrl.startsWith("https://") && !this.trainingUrl.startsWith("ftp://")?"http://" + this.trainingUrl:this.trainingUrl);
    }

    public String getTrainingYear() {
        if(this.trainingDate != null) {
            try {
                SimpleDateFormat ex = new SimpleDateFormat("yyyy");
                return ex.format(this.trainingDate);
            } catch (Exception var2) {
                throw new SystemException("Training year parsing failed: " + var2.toString());
            }
        } else {
            return null;
        }
    }

    public void setTrainingYear(String trainingYear) {
        if(trainingYear != null && trainingYear.length() != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

            try {
                Pattern ex = Pattern.compile("(?:\\D|\\b)(\\d{4})(?:\\D|\\b)");
                Matcher matcher = ex.matcher(trainingYear);
                boolean isFound = matcher.find();
                this.trainingDate = isFound?sdf.parse(matcher.group(1)):null;
            } catch (Exception var6) {
                throw new SystemException("Training year parsing failed: " + var6.toString());
            }
        }

    }

    public Date getTrainingDate() {
        return this.trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public BigDecimal getExternalTrainingId() {
        return this.externalTrainingId;
    }

    public void setExternalTrainingId(BigDecimal externalTrainingId) {
        this.externalTrainingId = externalTrainingId;
    }

    @JsonIgnore
    public boolean isAuto() {
        return this.auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    @JsonIgnore
    public String getEmployeeName() {
        return this.employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @JsonIgnore
    public BigDecimal getIdentifier() {
        return this.trainingId;
    }

    public String getTrainerName() {
        return this.trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
}
