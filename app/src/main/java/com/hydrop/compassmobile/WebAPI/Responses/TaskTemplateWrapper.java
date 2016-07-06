package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 11/05/16.
 */
public class TaskTemplateWrapper {

    private TaskTemplateWrapperData d;

    public TaskTemplateWrapperData getD() {
        return d;
    }

    public void setD(TaskTemplateWrapperData d) {
        this.d = d;
    }

    public class TaskTemplateWrapperData{

        private ArrayList<Data> TaskTemplate;

        public ArrayList<Data> getTaskTemplate() {
            return TaskTemplate;
        }

        public void setTaskTemplate(ArrayList<Data> taskTemplate) {
            TaskTemplate = taskTemplate;
        }

        public class Data{
            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedOn;
            private String LastUpdatedBy;
            private String OrganisationId;
            private String AssetType;
            private String TaskName;
            private String Priority;
            private String EstimatedDuration;
            private String Deleted;

            public String getDeleted() {
                return Deleted;
            }

            public void setDeleted(String deleted) {
                Deleted = deleted;
            }

            public String getLastUpdatedBy() {
                return LastUpdatedBy;
            }

            public void setLastUpdatedBy(String lastUpdatedBy) {
                LastUpdatedBy = lastUpdatedBy;
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
        }
    }
}
