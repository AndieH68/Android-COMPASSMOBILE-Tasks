package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.Organisation;
import com.hydrop.compassmobile.RealmObjects.Property;
import com.hydrop.compassmobile.Utils;
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
 * Created by Panos on 11/05/16.
 */
public class PropertyRequest extends BaseWebRequests {

    public  void getProperties(String date,String rowId) {

        HashMap map = getBody(date, PROPERTY,rowId);
        Call<PropertyWrapper> call = WebAPI.getWebAPIInterface().getProperties(map);
        call.enqueue(new Callback<PropertyWrapper>() {
            @Override
            public void onResponse(Call<PropertyWrapper> call, Response<PropertyWrapper> response) {
                if (Utils.checkNotNull(response.body())) {
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<PropertyWrapper.PropertyWrapperData.Data> data = response.body().getD().getProperty();
                        if (data.size() > 0) {
                            PropertyWrapper.PropertyWrapperData.Data lastObject = data.get(data.size() - 1);
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
            public void onFailure(Call<PropertyWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });


    }

    public  void addToRealm(final ArrayList<PropertyWrapper.PropertyWrapperData.Data> mData, final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for (PropertyWrapper.PropertyWrapperData.Data data : mData) {
                    Property property = new Property();

                    property.setRowId(data.getRowId());
                    property.setCreatedBy(data.getCreatedBy());
                    property.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    property.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    property.setLastUpdatedBy(data.getLastUpdatedBy());
                    property.setSiteId(data.getSiteId());
                    property.setHealthcare(data.getBooleanHealthCare());
                    property.setName(data.getName());
                    property.setDeleted(Utils.stringToDate(data.getDeleted()));
                    property.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        property.setDeleted(true);
                    }


                    bgRealm.copyToRealmOrUpdate(property);

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getProperties(BaseWebRequests.defaultDate, rowId);
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
