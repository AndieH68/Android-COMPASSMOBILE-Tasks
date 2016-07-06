package com.hydrop.compassmobile.WebAPI;

import android.content.Context;

import com.hydrop.compassmobile.RealmObjects.Util.SynchronisationHelper;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;

import io.realm.Realm;

/**
 * Created by Panos on 23/05/16.
 */
public class BaseMultipleRequestsHandler {
    public final static int ALL_REQUESTS = 13;
    public final static int REFRESH_TASKS = 2;

    public interface SetupCompletionCallback {
        void setupCompleted();
    }
    //private static final int NUMBER_OF_REQUESTS = 13;
    private int numberOfRequests = 0;
    protected int count = 0;
    private int requestErrors = 0;
    protected Realm realm;
    public SetupCompletionCallback setupCompletionCallback;
    protected Context context;


    public BaseMultipleRequestsHandler(int numberOfRequests,Context context){
        realm = Realm.getDefaultInstance();
        this.numberOfRequests = numberOfRequests;
        this.context = context;
    }


    protected void incrementCounter() {
        synchronized (BaseMultipleRequestsHandler.class) {
            count++;
            if (count == numberOfRequests) {
                if (Utils.checkNotNull(setupCompletionCallback)) {
                    setupCompletionCallback.setupCompleted();
                    realm.close();
                }
            }
        }
    }

    protected void incrementErrors() {
        synchronized (BaseMultipleRequestsHandler.class) {
            requestErrors++;
        }
    }



    public void addSetupCompletionCallbackLinstener(final SetupCompletionCallback setupCompletionCallback) {
        this.setupCompletionCallback = setupCompletionCallback;
    }


    protected void requestHasFinished(String message){
        if (message.contains(BaseWebRequests.SERVER_ERROR_RESPONSE) || message.contains(BaseWebRequests.MALFORMED_RESPONSE)) {
            SynchronisationHelper.setSyncronisationHistoryDate(realm, Utils.getUTCDateNow(),SynchronisationHelper.FAILED);
        }
        if (Utils.isDebugEnabled){
            BaseWebRequests.webPrint(message);
        }
        incrementCounter();
    }


}
