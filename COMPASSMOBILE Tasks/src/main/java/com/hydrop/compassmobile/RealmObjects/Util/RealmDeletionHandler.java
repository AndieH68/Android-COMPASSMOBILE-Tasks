package com.hydrop.compassmobile.RealmObjects.Util;

import android.content.Context;

import com.hydrop.compassmobile.RealmObjects.Asset;
import com.hydrop.compassmobile.RealmObjects.Location;
import com.hydrop.compassmobile.RealmObjects.LocationGroup;
import com.hydrop.compassmobile.RealmObjects.LocationGroupMembership;
import com.hydrop.compassmobile.RealmObjects.Operative;
import com.hydrop.compassmobile.RealmObjects.Organisation;
import com.hydrop.compassmobile.RealmObjects.Property;
import com.hydrop.compassmobile.RealmObjects.ReferenceData;
import com.hydrop.compassmobile.RealmObjects.Site;
import com.hydrop.compassmobile.RealmObjects.Synchronisation;
import com.hydrop.compassmobile.RealmObjects.Task;
import com.hydrop.compassmobile.RealmObjects.TaskParameter;
import com.hydrop.compassmobile.RealmObjects.TaskTemplate;
import com.hydrop.compassmobile.RealmObjects.TaskTemplateParameter;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.BaseMultipleRequestsHandler;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by Panos on 26/05/16.
 */
public class RealmDeletionHandler {
    private Realm realm;

    public Realm getRealm() {
        if (!Utils.checkNotNull(realm)){
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    public RealmDeletionHandler() {
        realm = Realm.getDefaultInstance();
    }

    public void deleteTasks(Context context){
        deleteTable(Task.class);
        deleteTable(TaskParameter.class);
        String taskType = BaseWebRequests.getOrganisationId(realm,context)+":Receive:"+BaseWebRequests.TASK;
        String taskParamtype = BaseWebRequests.getOrganisationId(realm,context)+":Receive:"+BaseWebRequests.TASK_PARAMETER;
        SynchronisationHelper.deletePreviousSynchonisationEntry(realm,taskType);
        SynchronisationHelper.deletePreviousSynchonisationEntry(realm,taskParamtype);
        realm.close();
    }

    public void deleteAll(){
        deleteTable(Asset.class);
        deleteTable(Location.class);
        deleteTable(LocationGroup.class);
        deleteTable(LocationGroupMembership.class);
        deleteTable(Operative.class);
        deleteTable(Organisation.class);
        deleteTable(Property.class);
        deleteTable(ReferenceData.class);
        deleteTable(Site.class);
        deleteTable(Task.class);
        deleteTable(TaskTemplate.class);
        deleteTable(TaskParameter.class);
        deleteTable(TaskTemplateParameter.class);
        deleteTable(Synchronisation.class);
    }

    private <T extends RealmModel> void deleteTable(final Class<T> clazz){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<T> results = realm.where(clazz).findAll();
                results.deleteAllFromRealm();
            }
        });
    }
}

