package com.hydrop.compassmobile.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.hydrop.compassmobile.Activities.Adapters.TaskDetailsAdapter;
import com.hydrop.compassmobile.Activities.HelperClasses.FilterHelper;
import com.hydrop.compassmobile.Activities.HelperClasses.TaskDetailsItem;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.RealmObjects.Task;
import com.hydrop.compassmobile.RealmObjects.TaskParameter;
import com.hydrop.compassmobile.RealmObjects.TaskTemplateParameter;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;
import com.hydrop.compassmobile.WebAPI.Requests.SendTaskRequest;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class TaskDetailsActivity extends AppCompatActivity {
    protected Realm realm;
    protected Task task;
    private TextView ppmGroupTextView,taskNameTextView,locationValueTextView,taskRefValueTextView,assetNumberValueTextView;
    protected RecyclerView recyclerView;
    protected TaskDetailsAdapter mAdapter;
    private RealmResults<TaskTemplateParameter> results;
    protected ArrayList<TaskDetailsItem> modifiedResultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        setTitle(getString(R.string.task));
        realm = Realm.getDefaultInstance();
        Bundle extras = getIntent().getExtras();
        ppmGroupTextView = (TextView)findViewById(R.id.ppmGroupTextView);
        taskNameTextView = (TextView)findViewById(R.id.taskNameTextView);
        locationValueTextView = (TextView)findViewById(R.id.locationValueTextView);
        taskRefValueTextView = (TextView)findViewById(R.id.taskRefTextView);
        assetNumberValueTextView = (TextView)findViewById(R.id.assetNumberTextView);
        recyclerView = (RecyclerView)findViewById(R.id.recycleView);
        if (Utils.checkNotNull(extras)){
            String taskId = extras.getString("taskId");
            RealmQuery<Task> query = realm.where(Task.class);
            query.equalTo("rowId",taskId);
            task = query.findFirst();
            setUI();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.doneButton:
                doneButtonPressed();
                return true;
            case R.id.bluetooth:
                return true;
            case android.R.id.home:
                validateExit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        validateExit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_details, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     if (resultCode == Activity.RESULT_OK && requestCode == TaskActivity.SEARCH_BARCODE){
        String result = data.getStringExtra("barcode");
        TaskDetailsItem item = mAdapter.getItem(clickedPosition);
         item.setSelectedValue(result);
         mAdapter.notifyItemChanged(clickedPosition);

        }
    }

    private int clickedPosition;
    protected TaskDetailsAdapter.TaskDetailsAdapterOnClick taskDetailsAdapterOnClick = new TaskDetailsAdapter.TaskDetailsAdapterOnClick() {
        @Override
        public void searchButtonPressedForItemInPosition(int position) {
            clickedPosition = position;
            Intent intent = new Intent(TaskDetailsActivity.this,ScanActivity.class);
            startActivityForResult(intent,TaskActivity.SEARCH_BARCODE);

        }
    };

    private void validateExit(){
        DialogInterface.OnClickListener positiveAction = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                TaskDetailsActivity.this.finish();
            }
        };
        showAlert(getString(R.string.exit_current_task),positiveAction);
    }

    private void showAlert(String message, DialogInterface.OnClickListener positiveAction){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.yes),positiveAction);
        alertDialogBuilder.setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void doneButtonPressed(){
        if (validate()){
            createTaskParams();
            uploadTask(task.getRowId());
            setResult(RESULT_OK);
            finish();
        }else{
            Toast.makeText(this,getString(R.string.complete_required_fields),Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadTask(String taskId){
        SendTaskRequest sendTaskRequest = new SendTaskRequest();
        sendTaskRequest.sendTask(taskId);
    }

    private boolean validate(){
        int count = recyclerView.getAdapter().getItemCount();
        boolean isValid = true;
        for (int i=0;i<count;i++){
            TaskDetailsItem item = ((TaskDetailsAdapter)recyclerView.getAdapter()).getItem(i);
            String display = item.getTaskTemplateParameter().getParameterDisplay();
            item.setValidated(true);

            if (display.equals("Remove Asset") || display.equals("Alternate Asset Code")
                    || (display.contains("Notes") && display.startsWith("Add"))){
                continue;
            }
            if (item.isVisible()){
                String selectedValue = item.getSelectedValue();
                if (!Utils.checkNotNull(selectedValue) ||selectedValue.equals("") || selectedValue.equals(getString(R.string.not_applicable))
                        || selectedValue.equals(getString(R.string.please_select))){
                    print(item.getTaskTemplateParameter().getParameterDisplay());
                    isValid = false;
                    item.setValidated(false);
                }

            }
        }
        if(!isValid){
            mAdapter.notifyDataSetChanged();
        }
        return isValid;

    }

    private void createTaskParams(){
        int count = recyclerView.getAdapter().getItemCount();
        for (int i=0;i<count;i++) {
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
            if (!Utils.checkNotNull(selectedValue) ||selectedValue.equals("") || selectedValue.equals(getString(R.string.not_applicable))
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


    }

    private void setUI(){
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
        if (Utils.checkNotNull(task.getTaskTemplateId())) {
            createList();
            initAdapter();
        }else{
            Toast.makeText(this,getString(R.string.task_templateid_null),Toast.LENGTH_SHORT).show();
        }
    }

    //to be overrided by subclass
    protected void initAdapter(){
        mAdapter = new TaskDetailsAdapter(modifiedResultList, realm, this);
        mAdapter.setTaskDetailsAdapterOnClick(taskDetailsAdapterOnClick);
        recyclerView.setAdapter(mAdapter);
    }

    public static void print(String message){
        Log.d("checkingFs",message);
    }

    private void createList(){

        RealmQuery<TaskTemplateParameter> query = realm.where(TaskTemplateParameter.class);
        query.equalTo("taskTemplateId",task.getTaskTemplateId());
        query.equalTo("isDeleted",false);
        results = query.findAll();
        results = results.sort("ordinal");
        if (results.size() > 2){
            for (TaskTemplateParameter taskTemplateParameter : results){
                TaskDetailsItem item = addChild(taskTemplateParameter);
                modifiedResultList.add(item);
            }


            TaskDetailsItem removeAsset = modifiedResultList.remove(0);
            TaskDetailsItem assetCode = modifiedResultList.remove(0);
            modifiedResultList.add(removeAsset);
            modifiedResultList.add(assetCode);
        }


    }

    private TaskDetailsItem addChild(TaskTemplateParameter childParameter){
        String parentId = childParameter.getPredecessor();
        if (Utils.checkNotNull(parentId)){
            TaskDetailsItem parentItem = findParentForId(parentId);
            if(Utils.checkNotNull(parentItem)){
                TaskTemplateParameter parentParameter = parentItem.getTaskTemplateParameter();
                if (Utils.checkNotNull(parentParameter)){
                    TaskDetailsItem childItem = new TaskDetailsItem(childParameter,false);
                    parentItem.depedencies.add(childItem);
                    return childItem;
                }else{
                    //NEVER HAPPEN
                    return null;
                }
            }else{
                //no parent present send it full
                return new TaskDetailsItem(childParameter,true);
            }
        }else{
            //no parent present send it full
            return new TaskDetailsItem(childParameter,true);
        }
    }


    private TaskDetailsItem findParentForId(String parentId){
        for (TaskDetailsItem item : modifiedResultList){
            if (item.getTaskTemplateParameter().getRowId().equals(parentId)){
                return item;
            }
        }
        return null;
    }

}
