package com.hydrop.compassmobile.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hydrop.compassmobile.Activities.Adapters.TaskDetailsTemperatureAdapter;
import com.hydrop.compassmobile.Activities.HelperClasses.TaskDetailsItem;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.RealmObjects.TaskTemplateParameter;

import uk.co.etiltd.bluetooth.service.bluetherm;
import uk.co.etiltd.bluetooth.service.bluethermReport;
import com.hydrop.compassmobile.RealmObjects.Asset;
import com.hydrop.compassmobile.Utils;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;

import io.realm.RealmQuery;

/**
 * Created by Panos on 27/05/16.
 */
public class TaskDetailsTemperatureActivity extends TaskDetailsActivity {
    static final int bluetherm_connect = 1;
    static final int bluetherm_setMessenger = 2;
    static final int bluetherm_removeMessenger = 3;
    static final int bluetherm_disconnect = 4;
    static final int bluetherm_Set_1_Name = 5;
    static final int bluetherm_Set_1_High_Limit = 6;
    static final int bluetherm_Set_1_Low_Limit = 7;
    static final int bluetherm_Set_1_Trim = 8;
    static final int bluetherm_Set_2_Name = 9;
    static final int bluetherm_Set_2_High_Limit = 10;
    static final int bluetherm_Set_2_Low_Limit = 11;
    static final int bluetherm_Set_2_Trim = 12;
    static final int bluetherm_Set_Update_Interval = 13;

    // messages from this service to the client
    static final int bluetherm_ConnectedToProbe = 50;
    static final int bluetherm_ProbeButtonPressed = 51;
    static final int bluetherm_ProbeShuttingDown = 52;
    static final int bluetherm_NewReading = 53;
    static final int bluetherm_disconnected = 54;
    static final int bluetherm_ConnectFailed = 55;
    static final int bluetherm_ErrorConnecting = 60;

    static final int bluetherm_UpdateEmissivity = 62;
    static final int bluetherm_GetEmissivity = 63;
    static final int bluetherm_EmissivityValue = 64; //sending value back to Main Activity

    static final int UPDATE_UI = 100;
    // whether or not we are bound to the service
    protected boolean mBound;
    // for sending messages to the service
    protected Messenger mService;
    // for receiving messages from the service
    protected Messenger mMessenger;
    // whether or not we are connected
    public boolean isConnected;
    // which device we are connected to
    public BluetoothDevice currentlyConnectedDevice;
    public bluethermReport lastReading;

    int connectButtonDelay;

    int connectFailCount = 1;
    boolean errorConnecting = false ;
    boolean errorReconnectingOnce = true;

    class IncomingMessageHandler extends Handler {
        public void UpdateHandler(Handler value) {
            formHandler = value;

        }

        private Handler formHandler;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0xF0:
                    connect();
                    break;
                case 0xF1:
                    disConnect();
                    break;
                case 0xF3:
                    doBindService();
                    break;
                case 0xF4:
                    doUnbindService();
                    break;
                case bluetherm_ConnectedToProbe:
                    printb("bluetherm_ConnectedToProbe");
                    this.sendEmptyMessage(UPDATE_UI);
                    changeBluetoothIcon(R.drawable.ic_bluetooth_connected_white_48dp);
                    isConnected = true;
                    Utils.isProbeConnected = true;
                    break;
                case bluetherm_ConnectFailed:
                    this.sendEmptyMessage(UPDATE_UI);
                    showMessage(getString(R.string.bluetooth_connection_issue));
                    changeBluetoothIcon(R.drawable.ic_bluetooth_disabled_white_48dp);

                    break;
                case bluetherm_disconnected:
                    printb("bluetherm_disconnected");
                    connectButtonDelay = 5000;
                    isConnected = false;
                    Utils.isProbeConnected = false;
                    this.sendEmptyMessage(UPDATE_UI);
                    changeBluetoothIcon(R.drawable.ic_bluetooth_disabled_white_48dp);

                    lastReading = bluethermReport.getEmptyReading();

                    if (null != formHandler) {
                        formHandler.sendEmptyMessage(0xF000);
                    }
                    if (currentlyConnectedDevice != null) {
                        //you could try to reconnect here
                        currentlyConnectedDevice = null;
                    }

                    break;
                case bluetherm_ProbeShuttingDown:
                    currentlyConnectedDevice = null;
                    this.sendEmptyMessage(UPDATE_UI);
                    break;
                case bluetherm_NewReading:
                    if (isConnected) {
                        errorConnecting = false;
                        connectFailCount = 1;
                        Bundle b = msg.getData();
                        try {
                            b.setClassLoader(bluethermReport.class.getClassLoader());
                            lastReading = (bluethermReport)b.getParcelable(null);
                        } catch (Exception e) {
                            printb(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    this.sendEmptyMessage(UPDATE_UI);
                    break;

                case bluetherm_EmissivityValue:

                    Bundle b = msg.getData();
                    try {
                        b.setClassLoader(bluethermReport.class.getClassLoader());
                        lastReading = (bluethermReport) b.getParcelable(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String emissivityValue = lastReading.emissivityValue;
                    printb("EmisValue "+emissivityValue);

                    break;
                case bluetherm_ErrorConnecting:
                    changeBluetoothIcon(R.drawable.ic_bluetooth_disabled_white_48dp);
                    if (Utils.checkNotNull(currentlyConnectedDevice)){
                        showMessage(getString(R.string.error_connecting_to_device)+" "+currentlyConnectedDevice.getName());
                    }
                    printb("bluetherm_ErrorConnecting");
                    imh.sendEmptyMessage(bluetherm_ConnectFailed);
                    break;
                case bluetherm_ProbeButtonPressed:
                    printb("Button pressed");
                    ((TaskDetailsTemperatureAdapter) mAdapter).resetFocus();
                    break;

                case UPDATE_UI:
                    printb("updateUI");
                    if(mBound){
                        if (isConnected){
                            printb("isConnected");
                            if (Utils.checkNotNull(lastReading)){

                                DecimalFormat df = new DecimalFormat("#.#");
                                double r1 = lastReading.Input1Reading + lastReading.Input1Trim;
                                updateFields(df.format(r1));

                                if (lastReading.isTwoInput){
                                    double r2 = lastReading.Input2Reading + lastReading.Input2Trim;
                                    updateFields(df.format(r2));

                                }
                            }

                        }

                    }
                    break;
            }
        }
    }
    private IncomingMessageHandler imh;
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.

            mService = new Messenger(service);

            try
            {
                mService.send(Message.obtain(null, bluetherm_setMessenger, mMessenger));
            }
            catch (RemoteException e)
            {
            }

            mBound = true;
            mConnection = this;
            imh.sendEmptyMessage(UPDATE_UI);
        }


        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
            imh.sendEmptyMessage(UPDATE_UI);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectButtonDelay = 100;
        if (Utils.checkNotNull(BluetoothAdapter.getDefaultAdapter())){
            doUnbindService();
            doBindService();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    connect();
                }
            }, connectButtonDelay);

        }

    }

    protected MenuItem bluetoothItem;

    private void changeBluetoothIcon(int icon){
        if (Utils.checkNotNull(bluetoothItem)){
            bluetoothItem.setIcon(icon);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_details, menu);
        bluetoothItem = menu.findItem(R.id.bluetooth);
        bluetoothItem.setVisible(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bluetooth:
                Drawable.ConstantState currentState = item.getIcon().getConstantState();
                if (currentState.equals(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_bluetooth_connected_white_48dp,null).getConstantState())){
                    showMessage(getString(R.string.connected_device)+" "+currentlyConnectedDevice.getName());
                }else if (currentState.equals(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_bluetooth_disabled_white_48dp,null).getConstantState())){
                    connect();
                }else{
                    if(Utils.checkNotNull(currentlyConnectedDevice)){
                        showMessage(getString(R.string.searching_for_device)+" "+currentlyConnectedDevice.getName());
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConnect();
        doUnbindService();
    }

    @Override
    protected void initAdapter() {
        RealmQuery<Asset> query = realm.where(Asset.class);
        query.equalTo("rowId", task.getAssetId());
        Asset asset = query.findFirst();
        mAdapter = new TaskDetailsTemperatureAdapter(modifiedResultList, realm, this, asset.getHotType(), asset.getColdType());
        mAdapter.setTaskDetailsAdapterOnClick(taskDetailsAdapterOnClick);
        recyclerView.setAdapter(mAdapter);
    }

    public void doBindService() {
        imh = new IncomingMessageHandler();
        mMessenger = new Messenger(imh);

//        Intent i = new Intent();
//        i.setClassName("uk.co.etiltd.bluetooth.service","uk.co.etiltd.bluetooth.service.bluetherm");
//        startService(i);

        startService(new Intent(this, bluetherm.class));
        try {
            printb("try bind");
            Context c = getApplicationContext();

//            c.bindService(i, mConnection, Context.BIND_AUTO_CREATE);
            c.bindService(new Intent(this, bluetherm.class), mConnection, Context.BIND_AUTO_CREATE);

        }catch(Exception e){
            printb("try bind failed "+e.getMessage());
        }
    }
    public void doUnbindService() {
        Intent i = new Intent();
        i.setClassName("uk.co.etiltd.bluetooth.service","uk.co.etiltd.bluetooth.service.bluetherm");
        stopService(i);
        try {
            printb("try unbind");
            Context c = getApplicationContext();
            c.unbindService(mConnection);
        }
        catch(Exception e){
            printb("unbind failed "+e.getMessage());
        }
    }

    public void connect()
    {
        Set<BluetoothDevice> sod = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        Iterator<BluetoothDevice> i = sod.iterator();
        while (i.hasNext())
        {
            BluetoothDevice device = i.next();
            String deviceName = device.getName();
            String preselectedDevice = SharedPrefs.getBluetoothDeviceName(this);
            if (Utils.checkNotNull(preselectedDevice)){
                if (preselectedDevice.equals(deviceName)){
                    currentlyConnectedDevice = device;
                    changeBluetoothIcon(R.drawable.ic_bluetooth_searching_white_48dp);
                    showMessage(getString(R.string.searching_for_device)+" "+deviceName);
                    break;
                }
            }
        }
        if (!Utils.checkNotNull(currentlyConnectedDevice)){
            changeBluetoothIcon(R.drawable.ic_bluetooth_disabled_white_48dp);
            showMessage(getString(R.string.please_select_your_device_from_app_settings));
            return;
        }

        if (currentlyConnectedDevice != null){
            try {
                if (null != mService) {
                    errorConnecting = false;
                    mService.send(Message.obtain(null, bluetherm_connect, currentlyConnectedDevice));
                }
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else {
            showMessage(getString(R.string.no_bonded_devices));
            changeBluetoothIcon(R.drawable.ic_bluetooth_disabled_white_48dp);

        }
    }

    public void disConnect(){
        try {
            if (mService != null) {
                mService.send(Message.obtain(null, bluetherm_disconnect, null));
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updateFields(String value){
        int children = modifiedResultList.size();
        for (int i = 0; i < children;i++){
            TaskDetailsItem taskDetailsItem = modifiedResultList.get(i);
            TaskTemplateParameter taskTemplateParameter = taskDetailsItem.getTaskTemplateParameter();
            if (taskTemplateParameter.getParameterName().startsWith("Temperature") && !taskTemplateParameter.getParameterName().endsWith("Set")) {
                ((TaskDetailsTemperatureAdapter) mAdapter).update(value);

            }
        }
    }



    private void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    private void printb(String message){
        Log.d("mlue800th",message);
    }


}
