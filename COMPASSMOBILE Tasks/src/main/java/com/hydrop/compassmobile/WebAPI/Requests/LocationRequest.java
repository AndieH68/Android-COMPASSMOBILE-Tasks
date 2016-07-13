package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.Location;
import com.hydrop.compassmobile.RealmObjects.Property;
import com.hydrop.compassmobile.RealmObjects.ReferenceData;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.LocationWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.ReferenceDataWrapper;
import com.hydrop.compassmobile.WebAPI.WebAPI;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Panos on 11/05/16.
 */
public class LocationRequest extends BaseWebRequests {


    public void getLocationData(String date,String rowId){

        HashMap map = getBody(date,LOCATION,rowId);
        Call<LocationWrapper> call = WebAPI.getWebAPIInterface().getLocations(map);
        call.enqueue(new Callback<LocationWrapper>() {
            @Override
            public void onResponse(Call<LocationWrapper> call, Response<LocationWrapper> response) {
                if (Utils.checkNotNull(response.body())) {
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<LocationWrapper.LocationData.Data> data = response.body().getD().getLocation();
                        if (data.size() > 0) {
                            LocationWrapper.LocationData.Data lastObject = data.get(data.size() - 1);
                            String rowId = lastObject.getRowId();
                            String date = (lastObject.getLastUpdatedOn() != null) ? lastObject.getLastUpdatedOn() : lastObject.getCreatedOn();
                            addToRealm(data, rowId, date);
                        } else {
                            success();
                        }
                    }else{
                        failed(MALFORMED_RESPONSE);
                    }
                }else{
                    failed(SERVER_ERROR_RESPONSE);
                }
            }

            @Override
            public void onFailure(Call<LocationWrapper> call, Throwable t) {
                failed("LocationRequest failed "+t.getMessage());
            }
        });

    }

    public  void addToRealm(final ArrayList<LocationWrapper.LocationData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for (LocationWrapper.LocationData.Data data : mData) {

                    Location location = new Location();
                    location.setRowId(data.getRowId());
                    location.setCreatedBy(data.getCreatedBy());
                    location.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    location.setLastUpdatedBy(data.getLastUpdatedBy());
                    location.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    location.setName(data.getName());
                    location.setLevel(data.getLevel());
                    location.setUse(data.getUse());
                    location.setNumber(data.getNumber());
                    location.setDescription(data.getDescription());
                    location.setDeleted(Utils.stringToDate(data.getDeleted()));
                    location.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        location.setDeleted(true);
                    }

                    location.setPropertyId(data.getPropertyId());

                    bgRealm.copyToRealmOrUpdate(location);

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getLocationData(BaseWebRequests.defaultDate, rowId);
                realm.close();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                failed(this.getClass().getName()+" Realm Failed "+error.getMessage());
                realm.close();
            }
        });
    }

}
