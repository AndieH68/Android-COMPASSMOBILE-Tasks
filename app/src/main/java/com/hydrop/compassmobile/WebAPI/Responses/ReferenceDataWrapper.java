package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 06/05/16.
 */
public class ReferenceDataWrapper {

    private ReferenceData d;

    public ReferenceData getD() {
        return d;
    }

    public void setD(ReferenceData d) {
        this.d = d;
    }


    public class ReferenceData {

        private ArrayList<Data> ReferenceData;

        public ArrayList<Data> getReferenceData() {
            return ReferenceData;
        }

        public void setReferenceData(ArrayList<Data> referenceData) {
            this.ReferenceData = referenceData;
        }

        public class Data {
            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedBy;
            private String LastUpdatedOn;
            private String Deleted;
            private String StartDate;
            private String EndDate;
            private String Type;
            private String Value;
            private int Ordinal;
            private String Display;
            private String System;
            private String ParentValue;
            private String ParentType;

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

            public String getEndDate() {
                return EndDate;
            }

            public boolean getBooleanSystem(){
                return  (getSystem() == "true");
            }

            public void setEndDate(String endDate) {
                EndDate = endDate;
            }

            public String getParentValue() {
                return ParentValue;
            }

            public void setParentValue(String parentValue) {
                ParentValue = parentValue;
            }

            public String getParentType() {
                return ParentType;
            }

            public void setParentType(String parentType) {
                ParentType = parentType;
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

            public String getStartDate() {
                return StartDate;
            }

            public void setStartDate(String startDate) {
                StartDate = startDate;
            }

            public String getType() {
                return Type;
            }

            public void setType(String type) {
                Type = type;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String value) {
                Value = value;
            }

            public int getOrdinal() {
                return Ordinal;
            }

            public void setOrdinal(int ordinal) {
                Ordinal = ordinal;
            }

            public String getDisplay() {
                return Display;
            }

            public void setDisplay(String display) {
                Display = display;
            }

            public String getSystem() {
                return System;
            }

            public void setSystem(String system) {
                System = system;
            }
        }

    }
}
