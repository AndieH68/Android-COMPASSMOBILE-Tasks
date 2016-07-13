package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 06/05/16.
 */
public class SiteWrapper {

    private SiteData d;

    public SiteData getD() {
        return d;
    }

    public void setD(SiteData d) {
        this.d = d;
    }

    public class SiteData{

        private ArrayList<Data> Site;

        public ArrayList<Data> getSite() {
            return Site;
        }

        public void setSite(ArrayList<Data> site) {
            Site = site;
        }

        public class Data{
            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedBy;
            private String LastUpdatedOn;
            private String Deleted;
            private String OrganisationId;
            private String Name;
            private String Type;

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

            public String getDeleted() {
                return Deleted;
            }

            public void setDeleted(String deleted) {
                Deleted = deleted;
            }

            public String getOrganisationId() {
                return OrganisationId;
            }

            public void setOrganisationId(String organisationId) {
                OrganisationId = organisationId;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            public String getType() {
                return Type;
            }

            public void setType(String type) {
                Type = type;
            }
        }
    }
}
