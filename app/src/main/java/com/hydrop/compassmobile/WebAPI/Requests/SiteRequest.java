package com.hydrop.compassmobile.WebAPI.Requests;

import android.util.Log;

import com.hydrop.compassmobile.RealmObjects.Organisation;
import com.hydrop.compassmobile.RealmObjects.Site;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.OrganisationWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.ReferenceDataWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.SiteWrapper;
import com.hydrop.compassmobile.WebAPI.WebAPI;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Panos on 06/05/16.
 */
public class SiteRequest extends BaseWebRequests {


    public  void getSites(String date,String rowId) {

        HashMap map = getBody(date, SITE,rowId);
        Call<SiteWrapper> call = WebAPI.getWebAPIInterface().getSites(map);
        call.enqueue(new Callback<SiteWrapper>() {
            @Override
            public void onResponse(Call<SiteWrapper> call, Response<SiteWrapper> response) {
                if (Utils.checkNotNull(response.body())) {
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<SiteWrapper.SiteData.Data> data = response.body().getD().getSite();
                        if (data.size() > 0) {
                            SiteWrapper.SiteData.Data lastObject = data.get(data.size() - 1);
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
            public void onFailure(Call<SiteWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });

    }

    public  void addToRealm(final ArrayList<SiteWrapper.SiteData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for (SiteWrapper.SiteData.Data data : mData) {
                    Site site = new Site();

                    site.setRowId(data.getRowId());
                    site.setCreatedBy(data.getCreatedBy());
                    site.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    site.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    site.setLastUpdatedBy(data.getLastUpdatedBy());
                    site.setOrganisationId(data.getOrganisationId());
                    site.setType(data.getType());
                    site.setName(data.getName());
                    site.setDeleted(Utils.stringToDate(data.getDeleted()));
                    site.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        site.setDeleted(true);
                    }

                    bgRealm.copyToRealmOrUpdate(site);
                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getSites(BaseWebRequests.defaultDate, rowId);
                realm.close();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                failed(this.getClass().getName() + " Realm Failed " + error.getMessage());
                realm.close();
            }
        });
    }



}