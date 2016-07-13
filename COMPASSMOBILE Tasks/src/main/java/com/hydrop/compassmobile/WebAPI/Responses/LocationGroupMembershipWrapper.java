package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 11/05/16.
 */
public class LocationGroupMembershipWrapper {

    private LocationGroupMembershipData d;

    public LocationGroupMembershipData getD() {
        return d;
    }

    public void setD(LocationGroupMembershipData d) {
        this.d = d;
    }

    public class LocationGroupMembershipData{

        private ArrayList<Data> LocationGroupMembership;

        public ArrayList<Data> getLocationGroupMembership() {
            return LocationGroupMembership;
        }

        public void setLocationGroupMembership(ArrayList<Data> locationGroupMembership) {
            LocationGroupMembership = locationGroupMembership;
        }

        public class Data{
            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedOn;
            private String LastUpdatedBy;
            private String Deleted;
            private String LocationGroupId;
            private String LocationId;

            public String getLastUpdatedBy() {
                return LastUpdatedBy;
            }

            public void setLastUpdatedBy(String lastUpdatedBy) {
                LastUpdatedBy = lastUpdatedBy;
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

            public String getLastUpdatedOn() {
                return LastUpdatedOn;
            }

            public void setLastUpdatedOn(String lastUpdatedOn) {
                LastUpdatedOn = lastUpdatedOn;
            }

            public String getLocationGroupId() {
                return LocationGroupId;
            }

            public void setLocationGroupId(String locationGroupId) {
                LocationGroupId = locationGroupId;
            }

            public String getLocationId() {
                return LocationId;
            }

            public void setLocationId(String locationId) {
                LocationId = locationId;
            }
        }
    }
}
