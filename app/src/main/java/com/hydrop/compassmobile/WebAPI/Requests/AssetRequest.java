package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.Asset;
import com.hydrop.compassmobile.RealmObjects.LocationGroup;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.AssetWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.LocationGroupWrapper;
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
public class AssetRequest extends BaseWebRequests {

    public  void getAssets(String date,String rowId) {

        HashMap map = getBody(date, ASSET,rowId);
        Call<AssetWrapper> call = WebAPI.getWebAPIInterface().getAssets(map);
        call.enqueue(new Callback<AssetWrapper>() {
            @Override
            public void onResponse(Call<AssetWrapper> call, Response<AssetWrapper> response) {
                if (Utils.checkNotNull(response.body())){
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<AssetWrapper.AssetWrapperData.Data> data = response.body().getD().getAsset();
                        if (data.size() > 0) {
                            AssetWrapper.AssetWrapperData.Data lastObject = data.get(data.size() - 1);
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
            public void onFailure(Call<AssetWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });


    }

    public  void addToRealm(final ArrayList<AssetWrapper.AssetWrapperData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                for (AssetWrapper.AssetWrapperData.Data data : mData) {
                    Asset asset = new Asset();

                    asset.setRowId(data.getRowId());
                    asset.setCreatedBy(data.getCreatedBy());
                    asset.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    asset.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    asset.setLastUpdatedBy(data.getLastUpdatedBy());
                    asset.setDeleted(Utils.stringToDate(data.getDeleted()));
                    asset.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        asset.setDeleted(true);
                    }
                    asset.setAssetType(data.getAssetType());
                    asset.setPropertyId(data.getPropertyId());
                    asset.setLocationId(data.getLocationId());
                    asset.setHydropName(data.getHydropName());
                    asset.setHotType(data.getHotType());
                    asset.setColdType(data.getColdType());
                    asset.setDeleted(Utils.stringToDate(data.getDeleted()));
                    asset.setClientName(data.getClientName());
                    asset.setScanCode(data.getScanCode());

                    bgRealm.copyToRealmOrUpdate(asset);

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getAssets(BaseWebRequests.defaultDate, rowId);
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
