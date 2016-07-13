package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.TaskParameter;
import com.hydrop.compassmobile.RealmObjects.TaskTemplate;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.TaskParameterWrapper;
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
public class TaskParameterRequest extends  BaseWebRequests {

    public  void getTaskParameter(String date,String rowId) {

        HashMap map = getBody(date, TASK_PARAMETER,rowId);
        Call<TaskParameterWrapper> call = WebAPI.getWebAPIInterface().getTaskParameter(map);
        call.enqueue(new Callback<TaskParameterWrapper>() {
            @Override
            public void onResponse(Call<TaskParameterWrapper> call, Response<TaskParameterWrapper> response) {
                if (Utils.checkNotNull(response.body())){
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<TaskParameterWrapper.TaskParameterWrapperData.Data> data = response.body().getD().getTaskParameter();
                        if (data.size() > 0) {
                            TaskParameterWrapper.TaskParameterWrapperData.Data lastObject = data.get(data.size() - 1);
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
            public void onFailure(Call<TaskParameterWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });


    }

    public  void addToRealm(final ArrayList<TaskParameterWrapper.TaskParameterWrapperData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                for (TaskParameterWrapper.TaskParameterWrapperData.Data data : mData) {
                    TaskParameter taskParameter = new TaskParameter();

                    taskParameter.setRowId(data.getRowId());
                    taskParameter.setCreatedBy(data.getCreatedBy());
                    taskParameter.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    taskParameter.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    taskParameter.setLastUpdatedBy(data.getLastUpdatedBy());
                    taskParameter.setTaskTemplateParameterId(data.getTaskTemplateParameterId());
                    taskParameter.setDeleted(Utils.stringToDate(data.getDeleted()));
                    taskParameter.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        taskParameter.setDeleted(true);
                    }

                    taskParameter.setTaskId(data.getTaskId());
                    taskParameter.setParameterName(data.getParameterName());
                    taskParameter.setParameterType(data.getParameterType());
                    taskParameter.setParameterDisplay(data.getParameterDisplay());
                    taskParameter.setCollect(data.getBooleanCollect());
                    taskParameter.setParameterValue(data.getParameterValue());

                    bgRealm.copyToRealmOrUpdate(taskParameter);

                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getTaskParameter(BaseWebRequests.defaultDate, rowId);
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
