package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 11/05/16.
 */
public class OperativeWrapper {

    private OperativeWrapperData d;

    public OperativeWrapperData getD() {
        return d;
    }

    public void setD(OperativeWrapperData d) {
        this.d = d;
    }

    public class OperativeWrapperData{

        private ArrayList<Data> Operative;

        public ArrayList<Data> getOperative() {
            return Operative;
        }

        public void setOperative(ArrayList<Data> operative) {
            Operative = operative;
        }

        public class Data{
            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedBy;
            private String LastUpdatedOn;
            private String OrganisationId;
            private String Username;
            private String Password;
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

            public String getOrganisationId() {
                return OrganisationId;
            }

            public void setOrganisationId(String organisationId) {
                OrganisationId = organisationId;
            }

            public String getUsername() {
                return Username;
            }

            public void setUsername(String username) {
                Username = username;
            }

            public String getPassword() {
                return Password;
            }

            public void setPassword(String password) {
                Password = password;
            }
        }
    }
}
