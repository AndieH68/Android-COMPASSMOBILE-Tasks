package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 11/05/16.
 */
public class LocationWrapper {

    private LocationData d;

    public LocationData getD() {
        return d;
    }

    public void setD(LocationData d) {
        this.d = d;
    }

    public class LocationData{

        private ArrayList<Data> Location;

        public ArrayList<Data> getLocation() {
            return Location;
        }

        public void setLocation(ArrayList<Data> location) {
            this.Location = location;
        }

        public class Data{
            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedBy;
            private String LastUpdatedOn;
            private String PropertyId;
            private String Name;
            private String Description;
            private String Level;
            private String Deleted;
            private String Use;
            private String Number;

            public String getNumber() {
                return Number;
            }

            public void setNumber(String number) {
                Number = number;
            }

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

            public String getLevel() {
                return Level;
            }

            public void setLevel(String level) {
                Level = level;
            }

            public String getUse() {
                return Use;
            }

            public void setUse(String use) {
                Use = use;
            }
        }

    }

}
