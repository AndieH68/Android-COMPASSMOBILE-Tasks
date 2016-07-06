package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.Task;
import com.hydrop.compassmobile.RealmObjects.TaskTemplate;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.TaskParameterWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.TaskTemplateWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.TaskWrapper;
import com.hydrop.compassmobile.WebAPI.WebAPI;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Panos on 12/05/16.
 */
public class TaskRequest extends BaseWebRequests {

    public  void getTasks(final String date, String rowId) {

        HashMap map = getBody(date, TASK,rowId);
        Call<TaskWrapper> call = WebAPI.getWebAPIInterface().getTasks(map);
        call.enqueue(new Callback<TaskWrapper>() {
            @Override
            public void onResponse(Call<TaskWrapper> call, Response<TaskWrapper> response) {
                if (Utils.checkNotNull(response.body())){
                    if (Utils.checkNotNull(response.body().getD())) {
                        ArrayList<TaskWrapper.TaskWrapperData.Data> data = response.body().getD().getTask();
                        if (data.size() > 0) {
                            webPrint(date);
                            TaskWrapper.TaskWrapperData.Data lastObject = data.get(data.size() - 1);
                            String rowId = lastObject.getRowId();
                            String date = (lastObject.getLastUpdatedOn() != null) ? lastObject.getLastUpdatedOn() : lastObject.getCreatedOn();
                            addToRealm(data, rowId, date);
                        } else {
                            //success();
                            deleteCompletedTasks();
                        }
                    }else{
                        failed(MALFORMED_RESPONSE);
                    }
                }else{
                    failed(SERVER_ERROR_RESPONSE);
                }
            }

            @Override
            public void onFailure(Call<TaskWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });


    }

    private void deleteCompletedTasks(){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery<Task> query = realm.where(Task.class);
                query.equalTo("updatedLocally",false);
                query.notEqualTo("status","Pending").or().equalTo("isDeleted",true);
                query.notEqualTo("status","Outstanding");
                RealmResults<Task> tasks = query.findAll();
                BaseWebRequests.webPrint("Deleted tasks "+tasks.size());

                for (Task task : tasks){
                    if (task.isUpdatedLocally()){
                        BaseWebRequests.webPrint("help "+task.getTaskRef());

                    }
                }

                tasks.deleteAllFromRealm();

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                success();
            }
        });
    }

    public  void addToRealm(final ArrayList<TaskWrapper.TaskWrapperData.Data> mData,final String rowId,final String date){

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                for (TaskWrapper.TaskWrapperData.Data data : mData) {
                    Task task = new Task();

                    task.setRowId(data.getRowId());
                    task.setCreatedBy(data.getCreatedBy());
                    task.setCreatedOn(Utils.stringToDate(data.getCreatedOn()));
                    task.setLastUpdatedOn(Utils.stringToDate(data.getLastUpdatedOn()));
                    task.setLastUpdatedBy(data.getLastUpdatedBy());
                    task.setOrganisationId(data.getOrganisationId());
                    task.setSiteId(data.getSiteId());
                    task.setPropertyId(data.getPropertyId());
                    task.setLocationId(data.getLocationId());
                    task.setLocationGroupName(data.getLocationGroupName());
                    task.setLocationName(data.getLocationName());
                    task.setRoom(data.getRoom());
                    task.setTaskTemplateId(data.getTaskTemplateId());
                    task.setTaskRef(data.getTaskRef());
                    task.setPpmGroup(data.getPPMGroup());
                    task.setAssetType(data.getAssetType());
                    task.setTaskName(data.getTaskName());
                    task.setFrequency(data.getFrequency());
                    task.setAssetId(data.getAssetId());
                    task.setAssetNumber(data.getAssetNumber());
                    task.setScheduledDate(Utils.stringToDate(data.getScheduledDate()));
                    task.setCompletedDate(Utils.stringToDate(data.getCompletedDate()));
                    task.setStatus(data.getStatus());
                    task.setPriority(Integer.parseInt(data.getPriority()));
                    task.setEstimatedDuration(Utils.stringToInteger(data.getEstimatedDuration()));
                    task.setOperativeId(data.getOperativeId());
                    task.setActualDuration(Utils.stringToInteger(data.getActualDuration()));
                    task.setTravelDuration(Utils.stringToInteger(data.getTravelDuration()));
                    task.setComments(data.getComments());
                    task.setAlternateScanCode(data.getAlternateScanCode());
                    task.setDeleted(Utils.stringToDate(data.getDeleted()));
                    task.setDeleted(false);
                    task.setUpdatedLocally(false);

                    if (Utils.checkNotNull(data.getDeleted())){
                        task.setDeleted(true);
                    }


                    bgRealm.copyToRealmOrUpdate(task);

                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getTasks(BaseWebRequests.defaultDate, rowId);
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
