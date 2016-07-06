package com.hydrop.compassmobile.WebAPI.Requests;

import com.hydrop.compassmobile.RealmObjects.Task;
import com.hydrop.compassmobile.RealmObjects.TaskParameter;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper.NewTask;
import com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper.NewTaskDetails;
import com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper.NewTaskResponseWrapper;
import com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper.SynchonisationPackage;
import com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper.TaskParameterDetails;
import com.hydrop.compassmobile.WebAPI.WebAPI;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Panos on 31/05/16.
 */
public class SendTaskRequest extends BaseWebRequests {

    public void sendTask(final String taskId){
        NewTask nTask = createNewTask(taskId);
        Call<NewTaskResponseWrapper> call  = WebAPI.getWebAPIInterface().sendTask(nTask);
        call.enqueue(new Callback<NewTaskResponseWrapper>() {
            @Override
            public void onResponse(Call<NewTaskResponseWrapper> call, Response<NewTaskResponseWrapper> response) {
                if (Utils.checkNotNull(response.body())) {
                    if (Utils.checkNotNull(response.body().isD())) {
                        BaseWebRequests.webPrint(response.body().isD());
                        Realm realm = Realm.getDefaultInstance();
                        final Task task = realm.where(Task.class).equalTo("rowId",taskId).findFirst();
                        final RealmResults<TaskParameter> parameters = realm.where(TaskParameter.class).equalTo("taskId",taskId).findAll();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                task.deleteFromRealm();
                                parameters.deleteAllFromRealm();
                            }
                        });
                        success();
                    }else{
                        failed(MALFORMED_RESPONSE);
                    }
                }else{
                    failed(SERVER_ERROR_RESPONSE);
                }

            }

            @Override
            public void onFailure(Call<NewTaskResponseWrapper> call, Throwable t) {
                failed(this.getClass().getName()+" failed "+t.getMessage());

            }
        });
    }

    private NewTask createNewTask(String taskId){
        Realm realm = Realm.getDefaultInstance();
        Task realmTask = realm.where(Task.class).equalTo("rowId",taskId).findFirst();

        NewTask newTask = new NewTask();
        newTask.setOperativeId(BaseWebRequests.operativeId);

        SynchonisationPackage synchonisationPackage = new SynchonisationPackage();
        newTask.setSynchronisationPackage(synchonisationPackage);

        NewTaskDetails newTaskDetails = new NewTaskDetails();
        newTaskDetails.setTaskId(taskId);
        newTaskDetails.setCreatedBy(realmTask.getCreatedBy());
        newTaskDetails.setCreatedOn(Utils.dateToISOString(realmTask.getCreatedOn()));
        newTaskDetails.setLastUpdatedBy(realmTask.getLastUpdatedBy());
        newTaskDetails.setLastUpdatedOn(Utils.dateToISOString(realmTask.getLastUpdatedOn()));
        if (Utils.checkNotNull(realmTask.getDeleted())){
            newTaskDetails.setDeleted(Utils.dateToISOString(realmTask.getDeleted()));
        }else{
            newTaskDetails.setDeleted(null);
        }
        newTaskDetails.setOrganisationId(realmTask.getOrganisationId());
        newTaskDetails.setSiteId(realmTask.getSiteId());
        newTaskDetails.setPropertyId(realmTask.getPropertyId());
        newTaskDetails.setLocationId(realmTask.getLocationId());
        newTaskDetails.setLocationGroupName(realmTask.getLocationGroupName());
        newTaskDetails.setLocationName(realmTask.getLocationName());
        newTaskDetails.setRoom(realmTask.getRoom());
        newTaskDetails.setTaskTemplateId(realmTask.getTaskTemplateId());
        newTaskDetails.setTaskRef(realmTask.getTaskRef());
        newTaskDetails.setpPMGroup(realmTask.getPpmGroup());
        newTaskDetails.setAssetType(realmTask.getAssetType());
        newTaskDetails.setTaskName(realmTask.getTaskName());
        newTaskDetails.setFrequency(realmTask.getFrequency());
        newTaskDetails.setAssetId(realmTask.getAssetId());
        newTaskDetails.setAssetNumber(realmTask.getAssetNumber());
        newTaskDetails.setScheduledDate(Utils.dateToISOString(realmTask.getScheduledDate()));
        newTaskDetails.setCompletedDate(Utils.dateToISOString(realmTask.getCompletedDate()));
        newTaskDetails.setStatus(realmTask.getStatus());
        newTaskDetails.setPriority(""+realmTask.getPriority());
        newTaskDetails.setEstimatedDuration(""+realmTask.getEstimatedDuration());
        newTaskDetails.setOperativeId(realmTask.getOperativeId());
        newTaskDetails.setActualDuration(""+realmTask.getActualDuration());
        newTaskDetails.setTravelDuration(""+realmTask.getTravelDuration());
        newTaskDetails.setComments(realmTask.getComments());
        newTaskDetails.setAlternateAssetCode(realmTask.getAlternateScanCode());

        RealmResults<TaskParameter> taskParameters = realm.where(TaskParameter.class).equalTo("taskId",taskId).findAll();
        ArrayList<TaskParameterDetails> taskParameterDetails = new ArrayList<>();
        for (TaskParameter taskParameter : taskParameters){
            TaskParameterDetails taskParamDetails = new TaskParameterDetails();
            taskParamDetails.setTaskParameterId(taskParameter.getRowId());
            taskParamDetails.setCreatedBy(taskParameter.getCreatedBy());
            taskParamDetails.setCreatedOn(Utils.dateToISOString(taskParameter.getCreatedOn()));
            taskParamDetails.setLastUpdatedBy(taskParameter.getLastUpdatedBy());
            taskParamDetails.setLastUpdatedOn(Utils.dateToISOString(taskParameter.getLastUpdatedOn()));
            taskParamDetails.setTaskTemplateParameterId(taskParameter.getTaskTemplateParameterId());
            taskParamDetails.setTaskId(taskId);
            taskParamDetails.setParameterName(taskParameter.getParameterName());
            taskParamDetails.setParameterType(taskParameter.getParameterType());
            taskParamDetails.setParameterDisplay(taskParameter.getParameterDisplay());
            taskParamDetails.setCollect((taskParameter.isCollect()) ? "true" : "false");
            taskParamDetails.setParameterValue(taskParameter.getParameterValue());

            taskParameterDetails.add(taskParamDetails);

        }

        newTaskDetails.setTaskParameters(taskParameterDetails);

        ArrayList<NewTaskDetails> taskDetails = new ArrayList<>();
        taskDetails.add(newTaskDetails);
        synchonisationPackage.setTasks(taskDetails);



        realm.close();

        return newTask;
    }
}
