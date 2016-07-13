package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.Property;
import com.hydrop.compassmobile.RealmObjects.TaskTemplate;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.PropertyWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.TaskTemplateWrapper;
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
public class TaskTemplateRequest extends BaseWebRequests {

    public  void getTaskTemplates(String date,String rowId) {

        HashMap map = getBody(date, TASK_TEMPLATE,rowId);
        Call<TaskTemplateWrapper> call = WebAPI.getWebAPIInterface().getTaskTemplates(map);
        call.enqueue(new Callback<TaskTemplateWrapper>() {
            @Override
            public void onResponse(Call<TaskTemplateWrapper> call, Response<TaskTemplateWrapper> response) {
                if (Utils.checkNotNull(response.body())){
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<TaskTemplateWrapper.TaskTemplateWrapperData.Data> data = response.body().getD().getTaskTemplate();
                        if (data.size() > 0) {
                            TaskTemplateWrapper.TaskTemplateWrapperData.Data lastObject = data.get(data.size() - 1);
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
            public void onFailure(Call<TaskTemplateWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });


    }

    public  void addToRealm(final ArrayList<TaskTemplateWrapper.TaskTemplateWrapperData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                for (TaskTemplateWrapper.TaskTemplateWrapperData.Data data : mData) {
                    TaskTemplate taskTemplate = new TaskTemplate();

                    taskTemplate.setRowId(data.getRowId());
                    taskTemplate.setCreatedBy(data.getCreatedBy());
                    taskTemplate.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    taskTemplate.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    taskTemplate.setLastUpdatedBy(data.getLastUpdatedBy());
                    taskTemplate.setOrganisationId(data.getOrganisationId());
                    taskTemplate.setAssetType(data.getAssetType());
                    taskTemplate.setTaskName(data.getTaskName());
                    taskTemplate.setPriority(Integer.parseInt(data.getPriority()));
                    taskTemplate.setEstimatedDuration(Integer.parseInt(data.getEstimatedDuration()));
                    taskTemplate.setDeleted(Utils.stringToDate(data.getDeleted()));
                    taskTemplate.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        taskTemplate.setDeleted(true);
                    }


                    bgRealm.copyToRealmOrUpdate(taskTemplate);

                }


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getTaskTemplates(BaseWebRequests.defaultDate, rowId);
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
