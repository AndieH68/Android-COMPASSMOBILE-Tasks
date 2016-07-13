package com.hydrop.compassmobile.WebAPI;

import android.content.Context;

import com.hydrop.compassmobile.RealmObjects.Util.SynchronisationHelper;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;
import com.hydrop.compassmobile.WebAPI.Requests.OperationCallback;
import com.hydrop.compassmobile.WebAPI.Requests.TaskParameterRequest;
import com.hydrop.compassmobile.WebAPI.Requests.TaskRequest;

import io.realm.Realm;

/**
 * Created by Panos on 23/05/16.
 */
public class RefreshTasksHandler extends BaseMultipleRequestsHandler{


    public RefreshTasksHandler(int numberOfRequests,Context context) {
        super(numberOfRequests,context);
    }

    public void start(){
        count = 0;

        String taskDate = SynchronisationHelper.getSynchronisationDate(realm, BaseWebRequests.TASK,context);
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.TASK,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("TaskRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("TaskRequest Failed "+message);
                incrementErrors();
            }
        });
        taskRequest.getTasks(taskDate,"");


        String taskPar = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.TASK_PARAMETER,context);

        TaskParameterRequest taskParameterRequest = new TaskParameterRequest();
        taskParameterRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.TASK_PARAMETER,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("TaskParameterRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("TaskParameterRequest Failed "+message);
                incrementErrors();
            }
        });
        taskParameterRequest.getTaskParameter(taskPar,"");

    }





}
