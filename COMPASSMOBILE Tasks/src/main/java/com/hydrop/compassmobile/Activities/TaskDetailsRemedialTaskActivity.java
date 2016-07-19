package com.hydrop.compassmobile.Activities;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.hydrop.compassmobile.Activities.Adapters.TaskDetailsAdapter;
import com.hydrop.compassmobile.Activities.HelperClasses.FilterHelper;
import com.hydrop.compassmobile.Activities.HelperClasses.TaskDetailsItem;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.RealmObjects.TaskParameter;
import com.hydrop.compassmobile.RealmObjects.TaskTemplateParameter;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;

import java.util.UUID;

import io.realm.RealmQuery;
import io.realm.RealmResults;


/**
 * Created by Andie 13/07/16
 */
public class TaskDetailsRemedialTaskActivity extends TaskDetailsActivity {

    @Override
    protected void createTaskParams() {
        RealmResults<TaskParameter> localResults;

        RealmQuery<TaskParameter> query = realm.where(TaskParameter.class);
        query.equalTo("taskId", task.getRowId());
        query.equalTo("isDeleted", false);
        localResults = query.findAll();
        localResults = localResults.sort("createdOn");
        if (localResults.size() == 1) {
            int count = recyclerView.getAdapter().getItemCount();
            for (int i = 1; i < count; i++) {
                TaskDetailsItem item = ((TaskDetailsAdapter) recyclerView.getAdapter()).getItem(i);
                TaskTemplateParameter taskTemplateParameter = item.getTaskTemplateParameter();

                TaskParameter taskParameter = new TaskParameter();
                taskParameter.setRowId(UUID.randomUUID().toString());
                taskParameter.setCreatedBy(BaseWebRequests.operativeId);
                taskParameter.setCreatedOn(Utils.getUTCDateNow());
                taskParameter.setTaskId(task.getRowId());
                taskParameter.setTaskTemplateParameterId(taskTemplateParameter.getRowId());
                taskParameter.setParameterName(taskTemplateParameter.getParameterName());
                taskParameter.setParameterType(taskTemplateParameter.getParameterType());
                taskParameter.setParameterDisplay(taskTemplateParameter.getParameterDisplay());
                taskParameter.setCollect(true);
                String selectedValue = item.getSelectedValue();
                if (!Utils.checkNotNull(selectedValue) || selectedValue.equals("") || selectedValue.equals(getString(R.string.not_applicable))
                        || selectedValue.equals(getString(R.string.please_select))) {
                    selectedValue = getString(R.string.not_applicable);
                }
                taskParameter.setParameterValue(selectedValue);

                realm.beginTransaction();
                realm.copyToRealm(taskParameter);
                realm.commitTransaction();

            }
            print(task.getRowId());

            realm.beginTransaction();
            task.setLastUpdatedBy(BaseWebRequests.operativeId);
            task.setLastUpdatedOn(Utils.getUTCDateNow());
            task.setOperativeId(BaseWebRequests.operativeId);
            task.setCompletedDate(Utils.getUTCDateNow());
            task.setStatus("Complete");
            task.setUpdatedLocally(true);
            realm.commitTransaction();
        } else {
            Log.d("createingParameters", "something went seriously wrong");
        }
    }

    @Override
    protected void setUI(){
        String fullName = FilterHelper.getAssetGroupNameForKey(realm,task.getPpmGroup(),"PPMAssetGroup");
        ppmGroupTextView.setText(fullName);
        String taskFullName = FilterHelper.getTaskFullNameForKey(realm,task.getTaskName(),"PPMTaskType","PPMAssetGroup",task.getPpmGroup());
        taskNameTextView.setText(taskFullName);
        locationValueTextView.setText(task.getLocationName());
        taskRefValueTextView.setText(task.getTaskRef());
        assetNumberValueTextView.setText(task.getAssetNumber());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        createList();
        initAdapter();
    }

    @Override
    protected void createList(){
        RealmResults<TaskParameter> localResults;

        RealmQuery<TaskParameter> query = realm.where(TaskParameter.class);
        query.equalTo("taskId",task.getRowId());
        query.equalTo("isDeleted",false);
        localResults = query.findAll();
        localResults = localResults.sort("createdOn");
        if (localResults.size() == 1 ){
            for (TaskParameter taskParameter : localResults){
                TaskTemplateParameter workInstructions = new TaskTemplateParameter();
                workInstructions.setParameterName("Instruction");
                workInstructions.setParameterType("Freetext");
                workInstructions.setParameterDisplay("Work Details");
                workInstructions.setCollect(true);
                TaskDetailsItem item = addChild(workInstructions);
                item.setSelectedValue(taskParameter.getParameterValue());
                modifiedResultList.add(item);
            }
            TaskTemplateParameter workInstructions = new TaskTemplateParameter();

            TaskTemplateParameter workCarriedOut = new TaskTemplateParameter();
            workCarriedOut.setParameterName("WorkCarriedOut");
            workCarriedOut.setParameterType("Freetext");
            workCarriedOut.setParameterDisplay("Work Carried Out");
            workCarriedOut.setCollect(true);
            TaskDetailsItem item2 = addChild(workCarriedOut);
            modifiedResultList.add(item2);

            TaskTemplateParameter workCompleted = new TaskTemplateParameter();
            workCompleted.setParameterName("WorkCompleted");
            workCompleted.setParameterType("Reference Data");
            workCompleted.setParameterDisplay("Work Completed");
            workCompleted.setReferenceDataType("YN");
            workCompleted.setCollect(true);
            TaskDetailsItem item3 = addChild(workCompleted);
            modifiedResultList.add(item3);

            TaskTemplateParameter removeAsset = new TaskTemplateParameter();
            removeAsset.setParameterName("RemoveAsset");
            removeAsset.setParameterType("Reference Data");
            removeAsset.setParameterDisplay("Remove Asset");
            removeAsset.setReferenceDataType("YN");
            removeAsset.setCollect(true);
            TaskDetailsItem item4 = addChild(removeAsset);
            modifiedResultList.add(item4);

            TaskTemplateParameter additionalNotes = new TaskTemplateParameter();
            additionalNotes.setParameterName("AdditionalNotes");
            additionalNotes.setParameterType("Freetext");
            additionalNotes.setParameterDisplay("Additional Notes");
            additionalNotes.setCollect(true);
            TaskDetailsItem item5 = addChild(additionalNotes);
            modifiedResultList.add(item5);
        }
        else {
            for (TaskParameter taskParameter : localResults){
                TaskTemplateParameter taskTemplateParameter = new TaskTemplateParameter();
                taskTemplateParameter.setParameterName(taskParameter.getParameterName());
                taskTemplateParameter.setParameterType(taskParameter.getParameterType());
                taskTemplateParameter.setParameterDisplay(taskParameter.getParameterDisplay());
                taskTemplateParameter.setCollect(true);
                TaskDetailsItem item = addChild(taskTemplateParameter);
                item.setSelectedValue(taskParameter.getParameterValue());
                modifiedResultList.add(item);
            }
        }
    }
}
