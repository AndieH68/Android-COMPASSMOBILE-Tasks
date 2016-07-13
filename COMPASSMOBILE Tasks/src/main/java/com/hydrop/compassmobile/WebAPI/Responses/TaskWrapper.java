package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 11/05/16.
 */
public class TaskWrapper {

    private TaskWrapperData d;

    public TaskWrapperData getD() {
        return d;
    }

    public void setD(TaskWrapperData d) {
        this.d = d;
    }

    public class TaskWrapperData{

        private ArrayList<Data> Task;

        public ArrayList<Data> getTask() {
            return Task;
        }

        public void setTask(ArrayList<Data> task) {
            Task = task;
        }

        public class Data{

            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedBy;
            private String LastUpdatedOn;
            private String Deleted;
            private String OrganisationId;
            private String SiteId;
            private String PropertyId;
            private String LocationId;
            private String LocationGroupName;
            private String LocationName;
            private String Room;
            private String TaskTemplateId;
            private String TaskRef;
            private String PPMGroup;
            private String AssetType;
            private String TaskName;
            private String Frequency;
            private String AssetId;
            private String AssetNumber;
            private String ScheduledDate;
            private String CompletedDate;
            private String Status;
            private String Priority;
            private String EstimatedDuration;
            private String OperativeId;
            private String ActualDuration;
            private String TravelDuration;
            private String AlternateScanCode;
            private String Comments;

            public String getDeleted() {
                return Deleted;
            }

            public void setDeleted(String deleted) {
                Deleted = deleted;
            }

            public String getAlternateScanCode() {
                return AlternateScanCode;
            }

            public void setAlternateScanCode(String alternateScanCode) {
                AlternateScanCode = alternateScanCode;
            }

            public String getComments() {
                return Comments;
            }

            public void setComments(String comments) {
                Comments = comments;
            }

            public String getTravelDuration() {
                return TravelDuration;
            }

            public void setTravelDuration(String travelDuration) {
                TravelDuration = travelDuration;
            }

            public String getActualDuration() {
                return ActualDuration;
            }

            public void setActualDuration(String actualDuration) {
                ActualDuration = actualDuration;
            }

            public String getRowId() {
                return RowId;
            }

            public void setRowId(String rowId) {
                RowId = rowId;
            }

            public String getCreatedBy() {
                return CreatedBy;
            }

            public void setCreatedBy(String createdBy) {
                CreatedBy = createdBy;
            }

            public String getCreatedOn() {
                return CreatedOn;
            }

            public void setCreatedOn(String createdOn) {
                CreatedOn = createdOn;
            }

            public String getLastUpdatedBy() {
                return LastUpdatedBy;
            }

            public void setLastUpdatedBy(String lastUpdatedBy) {
                LastUpdatedBy = lastUpdatedBy;
            }

            public String getLastUpdatedOn() {
                return LastUpdatedOn;
            }

            public void setLastUpdatedOn(String lastUpdatedOn) {
                LastUpdatedOn = lastUpdatedOn;
            }

            public String getOrganisationId() {
                return OrganisationId;
            }

            public void setOrganisationId(String organisationId) {
                OrganisationId = organisationId;
            }

            public String getSiteId() {
                return SiteId;
            }

            public void setSiteId(String siteId) {
                SiteId = siteId;
            }

            public String getPropertyId() {
                return PropertyId;
            }

            public void setPropertyId(String propertyId) {
                PropertyId = propertyId;
            }

            public String getLocationId() {
                return LocationId;
            }

            public void setLocationId(String locationId) {
                LocationId = locationId;
            }

            public String getLocationGroupName() {
                return LocationGroupName;
            }

            public void setLocationGroupName(String locationGroupName) {
                LocationGroupName = locationGroupName;
            }

            public String getLocationName() {
                return LocationName;
            }

            public void setLocationName(String locationName) {
                LocationName = locationName;
            }

            public String getRoom() {
                return Room;
            }

            public void setRoom(String room) {
                Room = room;
            }

            public String getTaskTemplateId() {
                return TaskTemplateId;
            }

            public void setTaskTemplateId(String taskTemplateId) {
                TaskTemplateId = taskTemplateId;
            }

            public String getTaskRef() {
                return TaskRef;
            }

            public void setTaskRef(String taskRef) {
                TaskRef = taskRef;
            }

            public String getPPMGroup() {
                return PPMGroup;
            }

            public void setPPMGroup(String PPMGroup) {
                this.PPMGroup = PPMGroup;
            }

            public String getAssetType() {
                return AssetType;
            }

            public void setAssetType(String assetType) {
                AssetType = assetType;
            }

            public String getTaskName() {
                return TaskName;
            }

            public void setTaskName(String taskName) {
                TaskName = taskName;
            }

            public String getFrequency() {
                return Frequency;
            }

            public void setFrequency(String frequency) {
                Frequency = frequency;
            }

            public String getAssetId() {
                return AssetId;
            }

            public void setAssetId(String assetId) {
                AssetId = assetId;
            }

            public String getAssetNumber() {
                return AssetNumber;
            }

            public void setAssetNumber(String assetNumber) {
                AssetNumber = assetNumber;
            }

            public String getScheduledDate() {
                return ScheduledDate;
            }

            public void setScheduledDate(String scheduledDate) {
                ScheduledDate = scheduledDate;
            }

            public String getCompletedDate() {
                return CompletedDate;
            }

            public void setCompletedDate(String completedDate) {
                CompletedDate = completedDate;
            }

            public String getStatus() {
                return Status;
            }

            public void setStatus(String status) {
                Status = status;
            }

            public String getPriority() {
                return Priority;
            }

            public void setPriority(String priority) {
                Priority = priority;
            }

            public String getEstimatedDuration() {
                return EstimatedDuration;
            }

            public void setEstimatedDuration(String estimatedDuration) {
                EstimatedDuration = estimatedDuration;
            }

            public String getOperativeId() {
                return OperativeId;
            }

            public void setOperativeId(String operativeId) {
                OperativeId = operativeId;
            }
        }
    }
}
