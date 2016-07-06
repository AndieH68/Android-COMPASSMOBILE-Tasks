package com.hydrop.compassmobile.RealmObjects;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.Required;

/**
 * Created by Panos on 06/05/16.
 */
public class Synchronisation extends RealmObject {
    @Required
    private Date lastSynchronisationDate;
    @Required
    private String type;

    public Date getLastSynchronisationDate() {
        return lastSynchronisationDate;
    }

    public void setLastSynchronisationDate(Date lastSynchronisationDate) {
        this.lastSynchronisationDate = lastSynchronisationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static void deleteAll(Realm realm){
        final RealmResults<Synchronisation> results  = realm.where(Synchronisation.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }
}
