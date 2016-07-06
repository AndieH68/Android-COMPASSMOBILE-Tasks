package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 11/05/16.
 */
public class PropertyWrapper {

    private PropertyWrapperData d;

    public PropertyWrapperData getD() {
        return d;
    }

    public void setD(PropertyWrapperData d) {
        this.d = d;
    }

    public class PropertyWrapperData{

        private ArrayList<Data> Property;

        public ArrayList<Data> getProperty() {
            return Property;
        }

        public void setProperty(ArrayList<Data> property) {
            Property = property;
        }

        public class Data{
            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedBy;
            private String LastUpdatedOn;
            private String Deleted;
            private String SiteId;
            private String Name;
            private String HealthCare;

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

            public String getSiteId() {
                return SiteId;
            }

            public void setSiteId(String siteId) {
                SiteId = siteId;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            public String getHealthCare() {
                return HealthCare;
            }

            public void setHealthCare(String healthCare) {
                HealthCare = healthCare;
            }

            public boolean getBooleanHealthCare(){
                return (getHealthCare() == "true");
            }
        }
    }
}
