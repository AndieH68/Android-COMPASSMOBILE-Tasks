package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 11/05/16.
 */
public class TaskTemplateParameterWrapper {

    private TaskTemplateParameterData d;

    public TaskTemplateParameterData getD() {
        return d;
    }

    public void setD(TaskTemplateParameterData d) {
        this.d = d;
    }

    public class TaskTemplateParameterData{

        private ArrayList<Data> TaskTemplateParameter;

        public ArrayList<Data> getTaskTemplateParameter() {
            return TaskTemplateParameter;
        }

        public void setTaskTemplateParameter(ArrayList<Data> taskTemplateParameter) {
            TaskTemplateParameter = taskTemplateParameter;
        }

        public class Data{
            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedOn;
            private String LastUpdatedBy;
            private String TaskTemplateId;
            private String ParameterName;
            private String ParameterType;
            private String ParameterDisplay;
            private String Collect;
            private String ReferenceDataType;
            private String ReferenceDataExtendedType;
            private String Ordinal;
            private String Deleted;
            private String Predecessor;
            private String PredecessorTrueValue;

            public String getPredecessor() {
                return Predecessor;
            }

            public void setPredecessor(String predecessor) {
                Predecessor = predecessor;
            }

            public String getPredecessorTrueValue() {
                return PredecessorTrueValue;
            }

            public void setPredecessorTrueValue(String predecessorTrueValue) {
                PredecessorTrueValue = predecessorTrueValue;
            }

            public boolean getCollectBoolen(){
                return (getCollect() == "true");
            }

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

            public String getTaskTemplateId() {
                return TaskTemplateId;
            }

            public void setTaskTemplateId(String taskTemplateId) {
                TaskTemplateId = taskTemplateId;
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

            public String getCollect() {
                return Collect;
            }

            public void setCollect(String collect) {
                Collect = collect;
            }

            public String getReferenceDataType() {
                return ReferenceDataType;
            }

            public void setReferenceDataType(String referenceDataType) {
                ReferenceDataType = referenceDataType;
            }

            public String getReferenceDataExtendedType() {
                return ReferenceDataExtendedType;
            }

            public void setReferenceDataExtendedType(String referenceDataExtendedType) {
                ReferenceDataExtendedType = referenceDataExtendedType;
            }

            public String getOrdinal() {
                return Ordinal;
            }

            public void setOrdinal(String ordinal) {
                Ordinal = ordinal;
            }
        }
    }
}
