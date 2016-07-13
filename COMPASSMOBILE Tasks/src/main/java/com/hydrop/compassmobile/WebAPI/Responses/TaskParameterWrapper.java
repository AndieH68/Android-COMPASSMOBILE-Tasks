package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 12/05/16.
 */
public class TaskParameterWrapper {

    private TaskParameterWrapperData d;

    public TaskParameterWrapperData getD() {
        return d;
    }

    public void setD(TaskParameterWrapperData d) {
        this.d = d;
    }

    public class TaskParameterWrapperData{

        private ArrayList<Data> TaskParameter;

        public ArrayList<Data> getTaskParameter() {
            return TaskParameter;
        }

        public void setTaskParameter(ArrayList<Data> taskParameter) {
            TaskParameter = taskParameter;
        }

        public class Data{

            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedBy;
            private String LastUpdatedOn;
            private String Deleted;
            private String TaskTemplateParameterId;
            private String TaskId;
            private String ParameterName;
            private String ParameterType;
            private String ParameterDisplay;
            private String ParameterValue;
            private String Collect;

            public boolean getBooleanCollect(){
                return (getCollect() == "true");
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

            public String getDeleted() {
                return Deleted;
            }

            public void setDeleted(String deleted) {
                Deleted = deleted;
            }

            public String getTaskTemplateParameterId() {
                return TaskTemplateParameterId;
            }

            public void setTaskTemplateParameterId(String taskTemplateParameterId) {
                TaskTemplateParameterId = taskTemplateParameterId;
            }

            public String getTaskId() {
                return TaskId;
            }

            public void setTaskId(String taskId) {
                TaskId = taskId;
            }

            public String getParameterName() {
                return ParameterName;
            }

            public void setParameterName(String parameterName) {
                ParameterName = parameterName;
            }

            public String getParameterType() {
                return ParameterType;
            }

            public void setParameterType(String parameterType) {
                ParameterType = parameterType;
            }

            public String getParameterDisplay() {
                return ParameterDisplay;
            }

            public void setParameterDisplay(String parameterDisplay) {
                ParameterDisplay = parameterDisplay;
            }

            public String getParameterValue() {
                return ParameterValue;
            }

            public void setParameterValue(String parameterValue) {
                ParameterValue = parameterValue;
            }

            public String getCollect() {
                return Collect;
            }

            public void setCollect(String collect) {
                Collect = collect;
            }
        }
    }
}
