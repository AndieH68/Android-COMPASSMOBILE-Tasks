package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.LocationGroup;
import com.hydrop.compassmobile.RealmObjects.Property;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.LocationGroupWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.LocationWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.PropertyWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.ReferenceDataWrapper;
import com.hydrop.compassmobile.WebAPI.WebAPI;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Panos on 12/05/16.
 */
public class LocationGroupRequest extends BaseWebRequests {

    public  void getLocationGroups(String date,String rowId) {

        HashMap map = getBody(date, LOCATION_GROUP,rowId);
        Call<LocationGroupWrapper> call = WebAPI.getWebAPIInterface().getLocationGroups(map);
        call.enqueue(new Callback<LocationGroupWrapper>() {
            @Override
            public void onResponse(Call<LocationGroupWrapper> call, Response<LocationGroupWrapper> response) {
                if (Utils.checkNotNull(response.body())){
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<LocationGroupWrapper.LocationGroupWrapperData.Data> data = response.body().getD().getLocationGroup();
                        if (data.size() > 0) {
                            LocationGroupWrapper.LocationGroupWrapperData.Data lastObject = data.get(data.size() - 1);
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
            public void onFailure(Call<LocationGroupWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });


    }

    public  void addToRealm(final ArrayList<LocationGroupWrapper.LocationGroupWrapperData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                for (LocationGroupWrapper.LocationGroupWrapperData.Data data : mData) {
                    LocationGroup locationGroup = new LocationGroup();

                    locationGroup.setRowId(data.getRowId());
                    locationGroup.setCreatedBy(data.getCreatedBy());
                    locationGroup.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    locationGroup.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    locationGroup.setLastUpdatedBy(data.getLastUpdatedBy());
                    locationGroup.setPropertyId(data.getPropertyId());
                    locationGroup.setType(data.getType());
                    locationGroup.setName(data.getName());
                    locationGroup.setDescription(data.getDescription());
                    locationGroup.setOccupantRiskFactor(data.getOccupantRiskFactor());
                    locationGroup.setDeleted(Utils.stringToDate(data.getDeleted()));
                    locationGroup.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        locationGroup.setDeleted(true);
                    }


                    bgRealm.copyToRealmOrUpdate(locationGroup);

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getLocationGroups(BaseWebRequests.defaultDate, rowId);
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
