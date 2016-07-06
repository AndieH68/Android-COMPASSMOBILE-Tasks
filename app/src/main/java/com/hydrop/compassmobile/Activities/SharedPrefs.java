package com.hydrop.compassmobile.Activities;

import android.content.Context;
import android.content.SharedPreferences;

import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;

/**
 * Created by Panos on 27/05/16.
 */
public class SharedPrefs {
    public static final String COMMON_SETTINGS = "commonSettings";
    public static final String BluetoothDevice = "BluetoothName";
    public static final String ServerName = "ServerName";
    public static final String UserName = "username";


    public static void setBluetoothDeviceName(String name,Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(BaseWebRequests.operativeId,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(BluetoothDevice, name);
        editor.commit();
    }

    public static String  getBluetoothDeviceName(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(BaseWebRequests.operativeId,Context.MODE_PRIVATE);
        return sharedpreferences.getString(BluetoothDevice,null);
    }

    public static void setServerName(String name,Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(COMMON_SETTINGS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(ServerName, name);
        editor.commit();
    }

    public static String  getServerName(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(COMMON_SETTINGS,Context.MODE_PRIVATE);
        return sharedpreferences.getString(ServerName,null);
    }

    public static void setUserName(String name,Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(COMMON_SETTINGS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(UserName, name);
        editor.commit();
    }

    public static String  getUserName(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(COMMON_SETTINGS,Context.MODE_PRIVATE);
        return sharedpreferences.getString(UserName,null);
    }



}
