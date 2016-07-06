package com.hydrop.compassmobile;

import android.app.Application;

import com.crittercism.app.Crittercism;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Panos on 11/05/16.
 */
public class CompassApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Crittercism.initialize(getApplicationContext(),"d488ce1a6d7b4d26aff4a36d9b9942ae00555300");
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

    }

    void resetRealm(){
        try{
            RealmConfiguration config = Realm.getDefaultInstance().getConfiguration();
            Realm.deleteRealm(config);
        }catch(Exception e){
            BaseWebRequests.webPrint("Realm reset failed"+e.getMessage());
        }
    }
}
