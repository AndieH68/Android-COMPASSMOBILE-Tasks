package com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper;

import com.hydrop.compassmobile.RealmObjects.TaskParameter;

import java.util.ArrayList;

/**
 * Created by Panos on 31/05/16.
 */
public class NewTaskDetails {

    private ArrayList<TaskParameterDetails> taskParameters;


    public ArrayList<TaskParameterDetails> getTaskParameters() {
        return taskParameters;
    }

    public void setTaskParameters(ArrayList<TaskParameterDetails> taskParameters) {
        this.taskParameters = taskParameters;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationGroupName() {
        return locationGroupName;
    }

    public void setLocationGroupName(String locationGroupName) {
        this.locationGroupName = locationGroupName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTaskTemplateId() {
        return taskTemplateId;
    }

    public void setTaskTemplateId(String taskTemplateId) {
        this.taskTemplateId = taskTemplateId;
    }

    public String getTaskRef() {
        return taskRef;
    }

    public void setTaskRef(String taskRef) {
        this.taskRef = taskRef;
    }

    public String getpPMGroup() {
        return pPMGroup;
    }

    public void setpPMGroup(String pPMGroup) {
        this.pPMGroup = pPMGroup;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getOperativeId() {
        return operativeId;
    }

    public void setOperativeId(String operativeId) {
        this.operativeId = operativeId;
    }

    public String getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(String actualDuration) {
        this.actualDuration = actualDuration;
    }

    public String getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(String travelDuration) {
        this.travelDuration = travelDuration;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAlternateAssetCode() {
        return alternateAssetCode;
    }

    public void setAlternateAssetCode(String alternateAssetCode) {
        this.alternateAssetCode = alternateAssetCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private String taskId;
    private String createdBy;
    private String createdOn;
    private String lastUpdatedBy;
    private String lastUpdatedOn;
    private String deleted;
    private String organisationId;
    private String siteId;
    private String propertyId;
    private String locationId;
    private String locationName;
    private String locationGroupName;
    private String room;
    private String taskTemplateId;
    private String taskRef;
    private String pPMGroup;
    private String assetType;
    private String taskName;
    private String frequency;
    private String assetId;
    private String assetNumber;
    private String scheduledDate;
    private String completedDate;
    private String status;
    private String priority;
    private String estimatedDuration;
    private String operativeId;
    private String actualDuration;
    private String travelDuration;
    private String comments;
    private String alternateAssetCode;
}
