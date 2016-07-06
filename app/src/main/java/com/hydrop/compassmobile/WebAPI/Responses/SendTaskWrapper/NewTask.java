package com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper;

import com.hydrop.compassmobile.RealmObjects.Synchronisation;

/**
 * Created by Panos on 31/05/16.
 */
public class NewTask {

    private String OperativeId;
    private SynchonisationPackage SynchronisationPackage;

    public String getOperativeId() {
        return OperativeId;
    }

    public void setOperativeId(String operativeId) {
        OperativeId = operativeId;
    }

    public SynchonisationPackage getSynchronisationPackage() {
        return SynchronisationPackage;
    }

    public void setSynchronisationPackage(SynchonisationPackage synchronisationPackage) {
        SynchronisationPackage = synchronisationPackage;
    }
}
