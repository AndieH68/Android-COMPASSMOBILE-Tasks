package com.hydrop.compassmobile.RealmObjects;

import com.hydrop.compassmobile.Activities.HelperClasses.TaskDetailsItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Panos on 05/05/16.
 */
public class TaskTemplateParameter extends RealmObject {

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



    @Required
    private String taskTemplateId;

    @Required
    private String parameterName;
    @Required
    private String parameterType;
    @Required
    private String parameterDisplay;
    private boolean collect;
    @Required
    private String referenceDataType;
    private String referenceDataExtendedType;

    private String predecessor;
    private String predecessorTrueValue;


    private int ordinal;

    public String getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(String predecessor) {
        this.predecessor = predecessor;
    }

    public String getPredecessorTrueValue() {
        return predecessorTrueValue;
    }

    public void setPredecessorTrueValue(String predecessorTrueValue) {
        this.predecessorTrueValue = predecessorTrueValue;
    }

    public String getTaskTemplateId() {
        return taskTemplateId;
    }

    public void setTaskTemplateId(String taskTemplateId) {
        this.taskTemplateId = taskTemplateId;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterDisplay() {
        return parameterDisplay;
    }

    public void setParameterDisplay(String parameterDisplay) {
        this.parameterDisplay = parameterDisplay;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public String getReferenceDataType() {
        return referenceDataType;
    }

    public void setReferenceDataType(String referenceDataType) {
        this.referenceDataType = referenceDataType;
    }

    public String getReferenceDataExtendedType() {
        return referenceDataExtendedType;
    }

    public void setReferenceDataExtendedType(String referenceDataExtendedType) {
        this.referenceDataExtendedType = referenceDataExtendedType;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

}
