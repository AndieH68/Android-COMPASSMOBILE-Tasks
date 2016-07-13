package com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper;

/**
 * Created by Panos on 31/05/16.
 */
public class TaskParameterDetails {

    private String taskParameterId;
    private String createdBy;
    private String createdOn;
    private String lastUpdatedBy;
    private String lastUpdatedOn;
    private String taskTemplateParameterId;
    private String taskId;
    private String parameterName;
    private String parameterType;
    private String parameterDisplay;
    private String collect;
    private String parameterValue;

    public String getTaskParameterId() {
        return taskParameterId;
    }

    public void setTaskParameterId(String taskParameterId) {
        this.taskParameterId = taskParameterId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getTaskTemplateParameterId() {
        return taskTemplateParameterId;
    }

    public void setTaskTemplateParameterId(String taskTemplateParameterId) {
        this.taskTemplateParameterId = taskTemplateParameterId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterDisplay() {
        return parameterDisplay;
    }

    public void setParameterDisplay(String parameterDisplay) {
        this.parameterDisplay = parameterDisplay;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

}
