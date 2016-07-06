package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.LocationGroup;
import com.hydrop.compassmobile.RealmObjects.Operative;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.LocationGroupWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.OperativeWrapper;
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
public class OperativeRequest extends BaseWebRequests {

    public  void getOperatives(String date,String rowId) {

        HashMap map = getBody(date, OPERATIVE,rowId);
        Call<OperativeWrapper> call = WebAPI.getWebAPIInterface().getOperatives(map);
        call.enqueue(new Callback<OperativeWrapper>() {
            @Override
            public void onResponse(Call<OperativeWrapper> call, Response<OperativeWrapper> response) {
                if (Utils.checkNotNull(response.body())){
                    if (Utils.checkNotNull(response.body().getD())) {

                        ArrayList<OperativeWrapper.OperativeWrapperData.Data> data = response.body().getD().getOperative();
                        if (data.size() > 0) {
                            OperativeWrapper.OperativeWrapperData.Data lastObject = data.get(data.size() - 1);
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
            public void onFailure(Call<OperativeWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });


    }

    public  void addToRealm(final ArrayList<OperativeWrapper.OperativeWrapperData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                for (OperativeWrapper.OperativeWrapperData.Data data : mData) {
                    Operative operative = new Operative();

                    operative.setRowId(data.getRowId());
                    operative.setCreatedBy(data.getCreatedBy());
                    operative.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    operative.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    operative.setLastUpdatedBy(data.getLastUpdatedBy());
                    operative.setOrganisationId(data.getOrganisationId());
                    operative.setUsername(data.getUsername());
                    operative.setPassword(data.getPassword());
                    operative.setDeleted(Utils.stringToDate(data.getDeleted()));
                    operative.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        operative.setDeleted(true);
                    }


                    bgRealm.copyToRealmOrUpdate(operative);

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getOperatives(BaseWebRequests.defaultDate, rowId);
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
