package com.hydrop.compassmobile.WebAPI.Requests;

import android.content.Context;
import android.graphics.Path;
import android.util.Log;

import com.hydrop.compassmobile.Activities.SharedPrefs;
import com.hydrop.compassmobile.RealmObjects.Operative;
import com.hydrop.compassmobile.Utils;

import java.util.Date;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Panos on 06/05/16.
 */
public class BaseWebRequests {
    public final static int REFERENCE_DATA = 1;
    public final static int ORGANISATION = 2;
    public final static int SITE = 3;
    public final static int PROPERTY = 4;
    public final static int LOCATION = 5;
    public final static int LOCATION_GROUP = 6;
    public final static int LOCATION_GROUP_MEMBERSHIP = 7;
    public final static int ASSET = 8;
    public final static int OPERATIVE = 9;
    public final static int TASK_TEMPLATE = 10;
    public final static int TASK_TEMPLATE_PARAMETER = 11;
    public final static int TASK = 12;
    public final static int TASK_PARAMETER = 13;
    public final static  String TAG = "webRequests";
    public final static String MALFORMED_RESPONSE = "MALFORMED RESPONSE";
    public final static String SERVER_ERROR_RESPONSE = "SERVER ERROR RESPONSE";


    protected OperationCallback operationCallback;

    public void addOperationCallback(OperationCallback operationCallback){
        this.operationCallback = operationCallback;
    }

    public final static String defaultDate = "1990-01-01 12:00:00.000";

    public static String operativeId = null;
    private static String organisationId;

    public static String getOrganisationId(Realm realm, Context context){
        if (Utils.checkNotNull(organisationId)){
            return organisationId;
        }
        if (!Utils.checkNotNull(operativeId)){
            String username = SharedPrefs.getUserName(context);
            if (Utils.checkNotNull(username)){
                Operative operative = realm.where(Operative.class).equalTo("username", username).findFirst();
                operativeId = operative.getRowId();
                organisationId = operative.getOrganisationId();
                return organisationId;

            }else {
                return null;
            }
        }
        RealmQuery<Operative> query = realm.where(Operative.class);
        query.equalTo("rowId",operativeId.toUpperCase());
        Operative result = query.findFirst();
        if (Utils.checkNotNull(result)){
            organisationId = result.getOrganisationId();
        }
        return organisationId;
    }


//    public static String getOperativeId(){
//       // FULL ACCOUNT return "5bdf9b74-aaff-e411-8775-001999ef0567";
//        return "b4cf19ef-59f1-e311-9402-00155d0aca00";
//    }

    public static HashMap<String,String> getBody(String date,int category,String rowId){
        HashMap map = new HashMap();
        map.put("OperativeId",operativeId);
        map.put("LastSynchonisationDateTime",date);
        map.put("Stage",category);
        map.put("LastRowId",rowId);

        return map;
    }

    public static <T> void webPrint (T object){
        Log.d(TAG,""+object);
    }

    protected void success(){
        if (Utils.checkNotNull(operationCallback)) {
            operationCallback.success();
        }
    }
    protected void failed (String message){
        if (Utils.checkNotNull(operationCallback)) {
            operationCallback.failed(message);
        }
    }


}
