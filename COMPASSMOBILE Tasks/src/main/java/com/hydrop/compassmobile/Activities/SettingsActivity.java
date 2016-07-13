package com.hydrop.compassmobile.Activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hydrop.compassmobile.Activities.HelperClasses.FilterHelper;
import com.hydrop.compassmobile.InitialSetupHandler;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.RealmObjects.Synchronisation;
import com.hydrop.compassmobile.RealmObjects.Task;
import com.hydrop.compassmobile.RealmObjects.TaskParameter;
import com.hydrop.compassmobile.RealmObjects.Util.RealmDeletionHandler;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.BaseMultipleRequestsHandler;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;
import com.hydrop.compassmobile.WebAPI.Requests.OperationCallback;
import com.hydrop.compassmobile.WebAPI.Requests.SendTaskRequest;
import com.hydrop.compassmobile.WebAPI.WebAPI;

import java.util.Set;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView pairingTextView,uploadTaskValueTextView,synchoniseDataTextView,resetSynchronisationDatesTextView,
    resetTaskDataTextView,resetAllDataTextView,viewViewHelp;
    private RelativeLayout uploadTaskLayout;
    private Realm realm;
    private ProgressDialog progressDialog;
    public static String selectedBluetoothDeviceName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        realm = Realm.getDefaultInstance();
        setUI();
        checkTasksForUpload();

    }

    private void setUI(){
        uploadTaskLayout = (RelativeLayout)findViewById(R.id.upload_task);
        uploadTaskLayout.setOnClickListener(this);
        pairingTextView = (TextView)findViewById(R.id.pairing);
        pairingTextView.setOnClickListener(this);
        uploadTaskValueTextView = (TextView)findViewById(R.id.uploadValue);
        synchoniseDataTextView = (TextView)findViewById(R.id.synchroniseData);
        synchoniseDataTextView.setOnClickListener(this);
        resetSynchronisationDatesTextView = (TextView)findViewById(R.id.resetSyncDates);
        resetSynchronisationDatesTextView.setOnClickListener(this);
        resetTaskDataTextView = (TextView)findViewById(R.id.resetTaskData);
        resetTaskDataTextView.setOnClickListener(this);
        resetAllDataTextView = (TextView)findViewById(R.id.resetAllData);
        resetAllDataTextView.setOnClickListener(this);
        viewViewHelp = (TextView)findViewById(R.id.viewCompassMobileHelp);
        viewViewHelp.setOnClickListener(this);
    }

    private void checkTasksForUpload(){
        RealmQuery<Task> query = realm.where(Task.class);
        query.equalTo("organisationId", BaseWebRequests.getOrganisationId(realm,this));
        query.equalTo("status","Complete");
        query.equalTo("isDeleted",false);
        RealmResults<Task> results = query.findAll();
        int count = 0;
        for (Task task : results){
            RealmQuery<TaskParameter> taskParameterRealmQuery = realm.where(TaskParameter.class);
            taskParameterRealmQuery.equalTo("taskId",task.getRowId());
            RealmResults<TaskParameter> taskParameterRealmResults =  taskParameterRealmQuery.findAll();
            if (taskParameterRealmResults.size()>0){
                count++;
            }
        }
        uploadTaskValueTextView.setText(""+count);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                logOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == synchoniseDataTextView){
            synchroniseData();
        }else if (v == resetSynchronisationDatesTextView ){
            resetSyncDates();
        }else if (v == resetTaskDataTextView){
            resetTaskData();
        }else if (v == resetAllDataTextView){
            resetAll();
        }else if (v == pairingTextView){
            selectedBluetoothDevice();
        }else if (v == uploadTaskLayout){
            uploadCompletedTasks();
        }else if (v == viewViewHelp){
            showHelp();
        }

    }

    private void gotoLoginPage(){
        Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void logOut(){
        DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoLoginPage();
            }
        };
        showAlert(getString(R.string.are_you_sure_you_want_to_logout),positive);

    }

    private void showHelp(){
        String URL = "http://" + SharedPrefs.getServerName(this) + "/HelpDocuments/COMPASSMOBILE/COMPASSMOBILE-Tasks-User-Guide-for-Android.pdf";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(browserIntent);
    }

    private void synchroniseData(){
        showProgressDialog();
        InitialSetupHandler initialSetupHandler = new InitialSetupHandler(BaseMultipleRequestsHandler.ALL_REQUESTS,this);
        initialSetupHandler.addSetupCompletionCallbackLinstener(new InitialSetupHandler.SetupCompletionCallback() {
            @Override
            public void setupCompleted() {
                BaseWebRequests.webPrint("ALL DONE");
                dismissDialog();
            }
        });

        initialSetupHandler.start();

    }

    private void resetSyncDates(){
        DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Synchronisation.deleteAll(realm);
            }
        };
        showAlert(getString(R.string.are_you_sure_you_want_to_reset_sync_dates),positive);
    }

    private void resetAll(){

        DialogInterface.OnClickListener firstWarning = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DialogInterface.OnClickListener finalPositive = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        RealmDeletionHandler realmDeletionHandler = new RealmDeletionHandler();
                        realmDeletionHandler.deleteAll();
                        FilterHelper.resetAll();
                        gotoLoginPage();
                    }
                };

                showAlert(getString(R.string.are_you_absolutely_sure_you_want_to_reset_everything),finalPositive);
            }
        };

        showAlert(getString(R.string.are_you_sure_you_want_to_reset_everything),firstWarning);

    }


    private void resetTaskData(){
        DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                RealmDeletionHandler realmDeletionHandler = new RealmDeletionHandler();
                realmDeletionHandler.deleteTasks(SettingsActivity.this);
                FilterHelper.resetAll();
            }
        };
        showAlert(getString(R.string.are_you_sure_you_want_to_reset_the_tasks),positive);

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

    private int connectionTries = 0;
    private void selectedBluetoothDevice(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!Utils.checkNotNull(mBluetoothAdapter)){
            showMessage(getString(R.string.unsupported_bluetooth));
            return;
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        final String[] items = new String[pairedDevices.size()];
        int index = 0;
        int preSelectedDeviceIndex = 0;
        String preSelectedDevice = SharedPrefs.getBluetoothDeviceName(SettingsActivity.this);
        for (BluetoothDevice device : pairedDevices){
            if (device.getName().startsWith("ETI S") || device.getName().startsWith("BlueTherm") ||
                    device.getName().startsWith("ETI") || device.getName().contains("BlueTherm")) {
                items[index] = device.getName();
                if (Utils.checkNotNull(preSelectedDevice)){
                    if (preSelectedDevice.equals(device.getName())){
                        preSelectedDeviceIndex = index;
                    }
                }
                index++;

            }

        }
        if (items.length > 0){
            new AlertDialog.Builder(this)
                    .setSingleChoiceItems(items, preSelectedDeviceIndex, null)
                    .setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                            SharedPrefs.setBluetoothDeviceName(items[selectedPosition],SettingsActivity.this);
                            selectedBluetoothDeviceName = items[selectedPosition];
                        }
                    })
                    .show();
        }else{
            if (!mBluetoothAdapter.isEnabled()){
                boolean activated = mBluetoothAdapter.enable();
                if (activated){
                    showMessage(getString(R.string.bluetooth_activated));
                    connectionTries++;
                    if (connectionTries < 4){
                        selectedBluetoothDevice();

                    }
                }else{
                    showMessage(getString(R.string.bluetooth_not_activated));
                }

            }else{
                showMessage(getString(R.string.no_active_bluetherm_devices));
            }
        }
    }

    private void showMessage(String message){
        Toast.makeText(SettingsActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    private void uploadCompletedTasks(){
        RealmQuery<Task> query = realm.where(Task.class);
        query.equalTo("organisationId", BaseWebRequests.getOrganisationId(realm,this));
        query.equalTo("status","Complete");
        query.equalTo("isDeleted",false);
        RealmResults<Task> results = query.findAll();
        final int totalTasks = results.size();
        if (totalTasks > 0) {
            showProgressDialog();
            for (Task task : results) {
                SendTaskRequest sendTaskRequest = new SendTaskRequest();
                sendTaskRequest.addOperationCallback(new OperationCallback() {
                    @Override
                    public void success() {
                        incrementCounter(totalTasks);
                    }

                    @Override
                    public void failed(String message) {
                        incrementCounter(totalTasks);

                    }
                });
                sendTaskRequest.sendTask(task.getRowId());
            }
        }

    }

    private int count = 0;
    protected void incrementCounter(int numberOfRequests) {
        synchronized (SettingsActivity.class) {
            count++;
            if (count == numberOfRequests) {
                dismissDialog();
                checkTasksForUpload();
            }
        }
    }



}
