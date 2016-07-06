package com.hydrop.compassmobile.RealmObjects;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Panos on 05/05/16.
 */
public class Asset extends RealmObject {


    @PrimaryKey
    private String rowId;
    @Required
    private String createdBy;
    @Required
    private Date createdOn;
    private  String lastUpdatedBy;
    private Date lastUpdatedOn;
    private Date deleted;
    private boolean isDeleted;
    @Index
    private String assetType;

    @Required
    private String locationId;
    private String propertyId;

    private String hydropName;
    private String clientName;
    private String scanCode;
    private String hotType;
    private String coldType;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getHydropName() {
        return hydropName;
    }

    public void setHydropName(String hydropName) {
        this.hydropName = hydropName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public String getHotType() {
        return hotType;
    }

    public void setHotType(String hotType) {
        this.hotType = hotType;
    }

    public String getColdType() {
        return coldType;
    }

    public void setColdType(String coldType) {
        this.coldType = coldType;
    }
}
