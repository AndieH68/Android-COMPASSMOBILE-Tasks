package com.hydrop.compassmobile.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hydrop.compassmobile.Activities.Adapters.RecyclerViewOnSelectedItem;
import com.hydrop.compassmobile.Activities.Adapters.TaskActivityAdapter;
import com.hydrop.compassmobile.Activities.HelperClasses.FilterHelper;
import com.hydrop.compassmobile.InitialSetupHandler;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.RealmObjects.Task;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.BaseMultipleRequestsHandler;
import com.hydrop.compassmobile.WebAPI.RefreshTasksHandler;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private ImageView filter, search, add;
    private TaskActivityAdapter mAdapter;
    private Realm realm;
    private TextView siteHeader,propertyHeader;
    private MenuItem barcodeReset;
    private String activeBarcode;
    public static final int FILTER_PAGE = 0;
    public static final int ADD_TASK = 1;
    public static final int SEARCH_BARCODE = 2;
    public static final int EDIT_TASK = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.tasks));

        filter = (ImageView) findViewById(R.id.filter);
        filter.setOnClickListener(this);
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(this);
        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(this);
        siteHeader = (TextView)findViewById(R.id.siteValueTextView);
        propertyHeader = (TextView)findViewById(R.id.propertyValueTextView);
        realm = Realm.getDefaultInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        prepareAdapter();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private RecyclerViewOnSelectedItem<Task> recyclerViewOnSelectedItem = new RecyclerViewOnSelectedItem<Task>() {
        @Override
        public void selectedItem(Task task, int position,View view) {
            BaseWebRequests.webPrint(task.getRowId());
            navigateToTaskDetails(task.getRowId());
        }
    };


    private void prepareAdapter() {
        if (Utils.checkNotNull(activeBarcode)){
            FilterHelper.resetAll();
        }

        RealmQuery<Task> query = FilterHelper.getFilterQuery(realm,this);
        if (Utils.checkNotNull(activeBarcode)){
            query.contains("assetNumber",activeBarcode);
        }
        query.contains("organisationId",BaseWebRequests.getOrganisationId(realm,this));

        RealmResults<Task> results = query.findAll();
        BaseWebRequests.webPrint("size "+results.size());
//        if (FilterHelper.sortBy.equals("scheduledDate")){
//            results = results.sort(FilterHelper.sortBy, Sort.DESCENDING);
//
//        }else{
            results = results.sort(FilterHelper.sortBy);

//        }
        boolean extraPropertyInfoEnabled = FilterHelper.selectedPropertyName == null;
        mAdapter = new TaskActivityAdapter(results,realm,extraPropertyInfoEnabled,recyclerViewOnSelectedItem);
        if(Utils.checkNotNull(mAdapter)){
            recyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
        propertyHeader.setText(getResources().getString(R.string.all));
        siteHeader.setText(getResources().getString(R.string.all));

        if (Utils.checkNotNull(FilterHelper.selectedPropertyName)){
            propertyHeader.setText(FilterHelper.selectedPropertyName);
        }
        if (Utils.checkNotNull(FilterHelper.selectedSiteName)){
            siteHeader.setText(FilterHelper.selectedSiteName);
        }

    }


    private void dismissDialog() {
        if (Utils.checkNotNull(progressDialog)) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (!Utils.checkNotNull(progressDialog)) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setTitle(getString(R.string.processing__));
        progressDialog.setMessage(getString(R.string.please_wait__));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void downloadTasks(){
        showProgressDialog();
        RefreshTasksHandler refreshTasksHandler = new RefreshTasksHandler(BaseMultipleRequestsHandler.REFRESH_TASKS,this);
        refreshTasksHandler.addSetupCompletionCallbackLinstener(new InitialSetupHandler.SetupCompletionCallback() {
            @Override
            public void setupCompleted() {
                BaseWebRequests.webPrint("Refresh Done");
                dismissDialog();
                prepareAdapter();
            }
        });
        refreshTasksHandler.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task, menu);
        barcodeReset = menu.findItem(R.id.action_reset_barcode);
        barcodeReset.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_refresh){
            downloadTasks();
            return true;
        }else if (id == R.id.action_reset_barcode){
            barcodeReset.setVisible(false);
            activeBarcode = null;
            prepareAdapter();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if (v == filter) {
            Intent intent = new Intent(this, FilterActivity.class);
            startActivityForResult(intent,FILTER_PAGE);
        }else if (v == search){
            Intent intent = new Intent(this,ScanActivity.class);
            startActivityForResult(intent,SEARCH_BARCODE);
        }else if (v == add){
            Intent intent = new Intent(this,CreateTaskActivity.class);
            startActivityForResult(intent,ADD_TASK);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == FILTER_PAGE){
            prepareAdapter();
        }else if (resultCode == Activity.RESULT_OK && requestCode == ADD_TASK){
            prepareAdapter();
            String taskId = data.getStringExtra("taskId");
            navigateToTaskDetails(taskId);
        }else if (resultCode == Activity.RESULT_OK && requestCode == SEARCH_BARCODE){
            String result = data.getStringExtra("barcode");
            activeBarcode = result;
            barcodeReset.setVisible(true);
            prepareAdapter();
            Toast.makeText(this,result,Toast.LENGTH_LONG).show();
        }else if (resultCode == Activity.RESULT_OK && requestCode == EDIT_TASK){
            prepareAdapter();
        }
    }

    private void navigateToTaskDetails(String taskId){
        Task task = realm.where(Task.class).equalTo("rowId",taskId).findFirst();
        String ppmGroup = task.getPpmGroup();
        String taskName = task.getTaskName();

        Intent intent = new Intent(TaskActivity.this,TaskDetailsActivity.class);

        if (taskName.equals("Remedial Task")){
            intent = new Intent(TaskActivity.this, TaskDetailsRemedialTaskActivity.class);
        }
        else if (taskName.startsWith("Temperature")) {
            intent = new Intent(TaskActivity.this, TaskDetailsTemperatureActivity.class);
        }

        intent.putExtra("taskId",taskId);
        startActivityForResult(intent,EDIT_TASK);
    }
}
