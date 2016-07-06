package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.TaskTemplate;
import com.hydrop.compassmobile.RealmObjects.TaskTemplateParameter;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.ReferenceDataWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.TaskTemplateParameterWrapper;
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
public class TaskTemplateParameterRequest extends BaseWebRequests {

    public  void getTaskTemplateParameters(String date,String rowId) {

        HashMap map = getBody(date, TASK_TEMPLATE_PARAMETER,rowId);
        Call<TaskTemplateParameterWrapper> call = WebAPI.getWebAPIInterface().getTaskTemplateParameters(map);
        call.enqueue(new Callback<TaskTemplateParameterWrapper>() {
            @Override
            public void onResponse(Call<TaskTemplateParameterWrapper> call, Response<TaskTemplateParameterWrapper> response) {
                if (Utils.checkNotNull(response.body())){
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<TaskTemplateParameterWrapper.TaskTemplateParameterData.Data> data = response.body().getD().getTaskTemplateParameter();
                        if (data.size() > 0) {
                            TaskTemplateParameterWrapper.TaskTemplateParameterData.Data lastObject = data.get(data.size() - 1);
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
            public void onFailure(Call<TaskTemplateParameterWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });


    }

    public  void addToRealm(final ArrayList<TaskTemplateParameterWrapper.TaskTemplateParameterData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                for (TaskTemplateParameterWrapper.TaskTemplateParameterData.Data data : mData) {
                    TaskTemplateParameter taskTemplateParameter = new TaskTemplateParameter();

                    taskTemplateParameter.setRowId(data.getRowId());
                    taskTemplateParameter.setCreatedBy(data.getCreatedBy());
                    taskTemplateParameter.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    taskTemplateParameter.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    taskTemplateParameter.setLastUpdatedBy(data.getLastUpdatedBy());
                    taskTemplateParameter.setDeleted(Utils.stringToDate(data.getDeleted()));
                    taskTemplateParameter.setDeleted(false);
                    if (Utils.checkNotNull(data.getDeleted())){
                        taskTemplateParameter.setDeleted(true);
                    }
                    taskTemplateParameter.setTaskTemplateId(data.getTaskTemplateId());
                    taskTemplateParameter.setParameterName(data.getParameterName());
                    taskTemplateParameter.setParameterType(data.getParameterType());
                    taskTemplateParameter.setParameterDisplay(data.getParameterDisplay());
                    taskTemplateParameter.setCollect(data.getCollectBoolen());
                    taskTemplateParameter.setReferenceDataType(data.getReferenceDataType());
                    taskTemplateParameter.setReferenceDataExtendedType(data.getReferenceDataExtendedType());
                    taskTemplateParameter.setOrdinal(Integer.parseInt(data.getOrdinal()));
                    taskTemplateParameter.setPredecessor(data.getPredecessor());
                    taskTemplateParameter.setPredecessorTrueValue(data.getPredecessorTrueValue());
                    bgRealm.copyToRealmOrUpdate(taskTemplateParameter);

                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getTaskTemplateParameters(BaseWebRequests.defaultDate, rowId);
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
