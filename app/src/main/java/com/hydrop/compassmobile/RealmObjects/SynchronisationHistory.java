package com.hydrop.compassmobile.RealmObjects;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Panos on 06/05/16.
 */
public class SynchronisationHistory extends RealmObject {

    @Required
    private Date synchronisationDate;
    @Required
    private String createdBy;
    @Required
    private Date createdOn;
    @Required
    private String outcome;

    public Date getSynchronisationDate() {
        return synchronisationDate;
    }

    public void setSynchronisationDate(Date synchronisationDate) {
        this.synchronisationDate = synchronisationDate;
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

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
