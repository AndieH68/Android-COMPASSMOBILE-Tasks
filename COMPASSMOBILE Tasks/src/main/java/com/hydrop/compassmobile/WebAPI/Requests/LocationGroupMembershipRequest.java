package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.LocationGroup;
import com.hydrop.compassmobile.RealmObjects.LocationGroupMembership;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.LocationGroupMembershipWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.LocationGroupWrapper;
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
public class LocationGroupMembershipRequest extends BaseWebRequests {

    public  void getLocationGroupMemberships(String date,String rowId) {

        HashMap map = getBody(date, LOCATION_GROUP_MEMBERSHIP,rowId);
        Call<LocationGroupMembershipWrapper> call = WebAPI.getWebAPIInterface().getLocationGroupsMembership(map);
        call.enqueue(new Callback<LocationGroupMembershipWrapper>() {
            @Override
            public void onResponse(Call<LocationGroupMembershipWrapper> call, Response<LocationGroupMembershipWrapper> response) {
                if (Utils.checkNotNull(response.body())){
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<LocationGroupMembershipWrapper.LocationGroupMembershipData.Data> data = response.body().getD().getLocationGroupMembership();
                        if (data.size() > 0) {
                            LocationGroupMembershipWrapper.LocationGroupMembershipData.Data lastObject = data.get(data.size() - 1);
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
            public void onFailure(Call<LocationGroupMembershipWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });


    }

    public  void addToRealm(final ArrayList<LocationGroupMembershipWrapper.LocationGroupMembershipData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                for (LocationGroupMembershipWrapper.LocationGroupMembershipData.Data data : mData) {
                    LocationGroupMembership locationGroupMembership = new LocationGroupMembership();

                    locationGroupMembership.setRowId(data.getRowId());
                    locationGroupMembership.setCreatedBy(data.getCreatedBy());
                    locationGroupMembership.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    locationGroupMembership.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    locationGroupMembership.setLocationGroupId(data.getLocationGroupId());
                    locationGroupMembership.setLocationId(data.getLocationId());
                    locationGroupMembership.setLastUpdatedBy(data.getLastUpdatedBy());
                    locationGroupMembership.setDeleted(Utils.stringToDate(data.getDeleted()));
                    locationGroupMembership.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        locationGroupMembership.setDeleted(true);
                    }


                    bgRealm.copyToRealmOrUpdate(locationGroupMembership);

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getLocationGroupMemberships(BaseWebRequests.defaultDate, rowId);
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
