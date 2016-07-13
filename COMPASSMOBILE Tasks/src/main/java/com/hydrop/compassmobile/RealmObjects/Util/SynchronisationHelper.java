package com.hydrop.compassmobile.RealmObjects.Util;

import android.content.Context;

import com.hydrop.compassmobile.RealmObjects.Operative;
import com.hydrop.compassmobile.RealmObjects.Synchronisation;
import com.hydrop.compassmobile.RealmObjects.SynchronisationHistory;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;

import java.util.Date;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by Panos on 17/05/16.
 */
public class SynchronisationHelper {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";


    public static String getSynchronisationDate(Realm realm, int stage, Context context){
        if (Utils.checkNotNull(BaseWebRequests.getOrganisationId(realm,context))){
            String type = BaseWebRequests.getOrganisationId(realm,context)+":Receive:"+stage;

            RealmQuery<Synchronisation> query = realm.where(Synchronisation.class);
            query.equalTo("type",type);
            Synchronisation result = query.findFirst();
            if (Utils.checkNotNull(result)){
                return Utils.dateToISOString(result.getLastSynchronisationDate());
            }

        }
        return BaseWebRequests.defaultDate;
    }

    public static void setSyncronisationDate(Realm realm,int stage,Date date,Context context){
        String type = BaseWebRequests.getOrganisationId(realm,context)+":Receive:"+stage;
        deletePreviousSynchonisationEntry(realm,type);
        Synchronisation synchronisation = new Synchronisation();
        synchronisation.setType(type);
        synchronisation.setLastSynchronisationDate(date);
        realm.beginTransaction();
        realm.copyToRealm(synchronisation);
        realm.commitTransaction();
    }

    public static void deletePreviousSynchonisationEntry(Realm realm,String type){
        RealmQuery<Synchronisation> query = realm.where(Synchronisation.class);
        query.equalTo("type",type);
        Synchronisation result = query.findFirst();
        if (Utils.checkNotNull(result)){
            realm.beginTransaction();
            result.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public static void setSyncronisationHistoryDate(Realm realm,Date date,String status){
        SynchronisationHistory synchronisationHistory = new SynchronisationHistory();
        synchronisationHistory.setCreatedBy(BaseWebRequests.operativeId);
        synchronisationHistory.setCreatedOn(date);
        synchronisationHistory.setOutcome(status);
        synchronisationHistory.setSynchronisationDate(date);
        realm.beginTransaction();
        realm.copyToRealm(synchronisationHistory);
        realm.commitTransaction();
    }

    public static boolean checkIfDatabaseIsEmpty(Realm realm){
        RealmQuery<Operative> query = realm.where(Operative.class);
        Operative operative = query.findFirst();
        return (Utils.checkNotNull(operative) ? false : true);
    }

    public static String getUserGUID(Realm realm,String username,String password){
        RealmQuery<Operative> query = realm.where(Operative.class);
        query.equalTo("username",username, Case.INSENSITIVE);
        query.equalTo("password",password);
        Operative operative = query.findFirst();
        if (Utils.checkNotNull(operative)){
            return operative.getRowId();
        }
        return null;
    }
}
