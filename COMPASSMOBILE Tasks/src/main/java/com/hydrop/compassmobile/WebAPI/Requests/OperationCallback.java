package com.hydrop.compassmobile.WebAPI.Requests;

/**
 * Created by Panos on 11/05/16.
 */
public interface OperationCallback {
    public void success();
    public void failed(String message);
}
