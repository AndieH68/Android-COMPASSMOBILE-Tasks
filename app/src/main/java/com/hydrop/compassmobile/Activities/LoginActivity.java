package com.hydrop.compassmobile.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hydrop.compassmobile.InitialSetupHandler;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.RealmObjects.Synchronisation;
import com.hydrop.compassmobile.RealmObjects.Util.SynchronisationHelper;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.BaseMultipleRequestsHandler;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;
import com.hydrop.compassmobile.WebAPI.Requests.LoginRequest;
import com.hydrop.compassmobile.WebAPI.Requests.OperationCallback;
import com.hydrop.compassmobile.WebAPI.Requests.OperativeRequest;
import com.hydrop.compassmobile.WebAPI.WebAPI;


import io.realm.Realm;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginButton;
    private EditText username,password,serverName;
    private LoginRequest loginRequest;
    private Realm realm;
    private ProgressDialog progressDialog;
    private String userName,passWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        realm = Realm.getDefaultInstance();
        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        serverName = (EditText)findViewById(R.id.serverName);

        serverName.setText(SharedPrefs.getServerName(this));
        username.setText(SharedPrefs.getUserName(this));
        if (Utils.isDebugEnabled){
            serverName.setText("compass2dev.hecs.local");
            username.setText("hydropTest");
            password.setText("test");
        }
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
    public void onClick(View v) {
        if (validate()){
            login();
        }
    }

    private void loginWithDBCredentials(){
        if (Utils.checkNotNull(SynchronisationHelper.getUserGUID(realm,userName,passWord))){
                BaseWebRequests.operativeId = SynchronisationHelper.getUserGUID(realm,userName,passWord);
                proceed();
        }else{
            toast(getString(R.string.operatives_credentials_dont_exist_locally));
        }


    }

    private void toast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private boolean validate(){
        if (serverName.getText().toString().isEmpty() ||
            username.getText().toString().isEmpty() ||
            password.getText().toString().isEmpty()){
            return false;
        }
        userName = username.getText().toString();
        passWord = password.getText().toString();
        loginButton.setEnabled(false);

        return true;
    }


    private void login(){
        showProgressDialog();
        String servername = serverName.getText().toString();
        WebAPI.endPoint = "http://"+servername+WebAPI.halfEndPoint;

        SharedPrefs.setServerName(servername,this);
        SharedPrefs.setUserName(userName,this);

        loginRequest = new LoginRequest();
        loginRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                sync();
            }

            @Override
            public void failed(String message) {
                if (!message.equals(getString(R.string.incorrect_credentials))){
                    loginWithDBCredentials();
                }
                dismissDialog();
                toast(message);
                loginButton.setEnabled(true);

            }
        });
        loginRequest.login(this,userName,passWord);
    }

    private void sync(){
        InitialSetupHandler initialSetupHandler = new InitialSetupHandler(BaseMultipleRequestsHandler.ALL_REQUESTS,this);
        initialSetupHandler.addSetupCompletionCallbackLinstener(new InitialSetupHandler.SetupCompletionCallback() {
            @Override
            public void setupCompleted() {
                BaseWebRequests.webPrint("ALL DONE");
                dismissDialog();
                proceed();
                loginButton.setEnabled(true);

            }
        });

         initialSetupHandler.start();
    }



    private void proceed(){
        Intent intent = new Intent(getApplicationContext(),TaskActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
