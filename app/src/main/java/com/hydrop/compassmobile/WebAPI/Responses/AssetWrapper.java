package com.hydrop.compassmobile.WebAPI.Responses;

import java.util.ArrayList;

/**
 * Created by Panos on 11/05/16.
 */
public class AssetWrapper {

    private AssetWrapperData d;

    public AssetWrapperData getD() {
        return d;
    }

    public void setD(AssetWrapperData d) {
        this.d = d;
    }

    public class AssetWrapperData{

        private ArrayList<Data> Asset;

        public ArrayList<Data> getAsset() {
            return Asset;
        }

        public void setAsset(ArrayList<Data> asset) {
            Asset = asset;
        }

        public class Data{
            private String RowId;
            private String CreatedBy;
            private String CreatedOn;
            private String LastUpdatedBy;
            private String LastUpdatedOn;
            private String Deleted;
            private String AssetType;
            private String PropertyId;
            private String LocationId;
            private String HydropName;
            private String HotType;
            private String ColdType;
            private String ClientName;
            private String ScanCode;

            public String getClientName() {
                return ClientName;
            }

            public void setClientName(String clientName) {
                ClientName = clientName;
            }

            public String getScanCode() {
                return ScanCode;
            }

            public void setScanCode(String scanCode) {
                ScanCode = scanCode;
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

            public String getAssetType() {
                return AssetType;
            }

            public void setAssetType(String assetType) {
                AssetType = assetType;
            }

            public String getPropertyId() {
                return PropertyId;
            }

            public void setPropertyId(String propertyId) {
                PropertyId = propertyId;
            }

            public String getLocationId() {
                return LocationId;
            }

            public void setLocationId(String locationId) {
                LocationId = locationId;
            }

            public String getHydropName() {
                return HydropName;
            }

            public void setHydropName(String hydropName) {
                HydropName = hydropName;
            }

            public String getHotType() {
                return HotType;
            }

            public void setHotType(String hotType) {
                HotType = hotType;
            }

            public String getColdType() {
                return ColdType;
            }

            public void setColdType(String coldType) {
                ColdType = coldType;
            }
        }
    }
}
