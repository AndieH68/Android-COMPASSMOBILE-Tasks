package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 11/05/16.
 */
public class LocationGroupWrapper {

    private LocationGroupWrapperData d;

    public LocationGroupWrapperData getD() {
        return d;
    }

    public void setD(LocationGroupWrapperData d) {
        this.d = d;
    }

    public class LocationGroupWrapperData{

        private ArrayList<Data> LocationGroup;

        public ArrayList<Data> getLocationGroup() {
            return LocationGroup;
        }

        public void setLocationGroup(ArrayList<Data> locationGroup) {
            LocationGroup = locationGroup;
        }

        public class Data{

            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedBy;
            private String LastUpdatedOn;
            private String PropertyId;
            private String Type;
            private String Name;
            private String Description;
            private String OccupantRiskFactor;
            private String Deleted;

            public String getDeleted() {
                return Deleted;
            }

            public void setDeleted(String deleted) {
                Deleted = deleted;
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

            public String getPropertyId() {
                return PropertyId;
            }

            public void setPropertyId(String propertyId) {
                PropertyId = propertyId;
            }

            public String getType() {
                return Type;
            }

            public void setType(String type) {
                Type = type;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            public String getDescription() {
                return Description;
            }

            public void setDescription(String description) {
                Description = description;
            }

            public String getOccupantRiskFactor() {
                return OccupantRiskFactor;
            }

            public void setOccupantRiskFactor(String occupantRiskFactor) {
                OccupantRiskFactor = occupantRiskFactor;
            }
        }

    }
}
