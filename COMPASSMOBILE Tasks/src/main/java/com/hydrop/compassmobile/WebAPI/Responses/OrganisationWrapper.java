package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 06/05/16.
 */
public class OrganisationWrapper {

    private OrganisationData d;

    public OrganisationData getD() {
        return d;
    }

    public void setD(OrganisationData d) {
        this.d = d;
    }

    public class OrganisationData{

        private ArrayList<Data> Organisation;

        public ArrayList<Data> getOrganisation() {
            return Organisation;
        }

        public void setOrganisation(ArrayList<Data> organisation) {
            Organisation = organisation;
        }

        public class Data{
            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedBy;
            private String LastUpdatedOn;
            private String Deleted;
            private String ParentOrganisationId;
            private String Name;

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

            public void setCreatedBy(String createBy) {
                CreatedBy = createBy;
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

            public String getParentOrganisationId() {
                return ParentOrganisationId;
            }

            public void setParentOrganisationId(String parentOrganisationId) {
                ParentOrganisationId = parentOrganisationId;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }
        }
    }
}
