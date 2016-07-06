package com.hydrop.compassmobile.WebAPI.Requests;

import android.util.Log;

import com.hydrop.compassmobile.RealmObjects.ReferenceData;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.ReferenceDataWrapper;
import com.hydrop.compassmobile.WebAPI.WebAPI;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Panos on 06/05/16.
 */
public class ReferenceDataRequest extends BaseWebRequests {


    public void getReferenceData(String date, String rowId){
       // webPrint("getReferenceData");
        HashMap map = getBody(date,REFERENCE_DATA,rowId);
        Call<ReferenceDataWrapper> call = WebAPI.getWebAPIInterface().getReferenceData(map);
        call.enqueue(new Callback<ReferenceDataWrapper>() {
            @Override
            public void onResponse(Call<ReferenceDataWrapper> call, Response<ReferenceDataWrapper> response) {
                if (Utils.checkNotNull(response.body())){
                    if (Utils.checkNotNull(response.body().getD())){
                        ArrayList<ReferenceDataWrapper.ReferenceData.Data> data = response.body().getD().getReferenceData();
                        if (data.size() > 0){
                            ReferenceDataWrapper.ReferenceData.Data lastObject = data.get(data.size()-1);
                            String rowId = lastObject.getRowId();
                            String date = (lastObject.getLastUpdatedOn() != null) ? lastObject.getLastUpdatedOn() : lastObject.getCreatedOn();
                            addToRealm(data,rowId,date);
                        }else{
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
            public void onFailure(Call<ReferenceDataWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());
            }
        });

    }

    public  void addToRealm(final ArrayList<ReferenceDataWrapper.ReferenceData.Data> mData,final String rowId,final String lastDate){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for (ReferenceDataWrapper.ReferenceData.Data data : mData) {
                    ReferenceData referenceData = new ReferenceData();
                    referenceData.setRowId(data.getRowId());
                    referenceData.setCreatedBy(data.getCreatedBy());
                    referenceData.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    referenceData.setDeleted(Utils.stringToDate(data.getDeleted()));
                    referenceData.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        referenceData.setDeleted(true);
                    }

                    referenceData.setStartDate(Utils.stringToDate(data.getStartDate()));
                    referenceData.setEndDate(Utils.stringToDate(data.getEndDate()));
                    referenceData.setType(data.getType());
                    referenceData.setValue(data.getValue());
                    referenceData.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    referenceData.setOrdinal(data.getOrdinal());
                    referenceData.setDisplay(data.getDisplay());
                    referenceData.setSystem(data.getBooleanSystem());
                    referenceData.setParentType(data.getParentType());
                    referenceData.setParentValue(data.getParentValue());

                    bgRealm.copyToRealmOrUpdate(referenceData);
                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getReferenceData(BaseWebRequests.defaultDate,rowId);
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
