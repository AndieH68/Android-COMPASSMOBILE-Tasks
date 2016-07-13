package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.Organisation;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.OrganisationWrapper;
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
public class OrganisationRequest extends BaseWebRequests {

    public  void getOrganisationData(String date,String rowId) {
        HashMap map = getBody(date, ORGANISATION,rowId);
        Call<OrganisationWrapper> call = WebAPI.getWebAPIInterface().getOrganisations(map);
        call.enqueue(new Callback<OrganisationWrapper>() {
            @Override
            public void onResponse(Call<OrganisationWrapper> call, Response<OrganisationWrapper> response) {
                if (Utils.checkNotNull(response.body())) {
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<OrganisationWrapper.OrganisationData.Data> data = response.body().getD().getOrganisation();
                        if (data.size() > 0) {
                            OrganisationWrapper.OrganisationData.Data lastObject = data.get(data.size() - 1);
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
            public void onFailure(Call<OrganisationWrapper> call, Throwable t) {
                operationCallback.failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });


    }

    public  void addToRealm(final ArrayList<OrganisationWrapper.OrganisationData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for (OrganisationWrapper.OrganisationData.Data data : mData) {
                    Organisation organisation = new Organisation();

                    organisation.setRowId(data.getRowId());
                    organisation.setCreatedBy(data.getCreatedBy());
                    organisation.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    organisation.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    organisation.setLastUpdatedBy(data.getLastUpdatedBy());
                    organisation.setParentOrganisationId(data.getParentOrganisationId());
                    organisation.setName(data.getName());
                    organisation.setDeleted(Utils.stringToDate(data.getDeleted()));
                    organisation.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        organisation.setDeleted(true);
                    }

                    bgRealm.copyToRealmOrUpdate(organisation);
                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getOrganisationData(BaseWebRequests.defaultDate,rowId);
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