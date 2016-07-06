package com.hydrop.compassmobile;

import android.content.Context;

import com.hydrop.compassmobile.RealmObjects.Util.SynchronisationHelper;
import com.hydrop.compassmobile.WebAPI.BaseMultipleRequestsHandler;
import com.hydrop.compassmobile.WebAPI.Requests.AssetRequest;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;
import com.hydrop.compassmobile.WebAPI.Requests.LocationGroupMembershipRequest;
import com.hydrop.compassmobile.WebAPI.Requests.LocationGroupRequest;
import com.hydrop.compassmobile.WebAPI.Requests.LocationRequest;
import com.hydrop.compassmobile.WebAPI.Requests.OperationCallback;
import com.hydrop.compassmobile.WebAPI.Requests.OperativeRequest;
import com.hydrop.compassmobile.WebAPI.Requests.OrganisationRequest;
import com.hydrop.compassmobile.WebAPI.Requests.PropertyRequest;
import com.hydrop.compassmobile.WebAPI.Requests.ReferenceDataRequest;
import com.hydrop.compassmobile.WebAPI.Requests.SiteRequest;
import com.hydrop.compassmobile.WebAPI.Requests.TaskParameterRequest;
import com.hydrop.compassmobile.WebAPI.Requests.TaskRequest;
import com.hydrop.compassmobile.WebAPI.Requests.TaskTemplateParameterRequest;
import com.hydrop.compassmobile.WebAPI.Requests.TaskTemplateRequest;

import io.realm.Realm;

/**
 * Created by Panos on 11/05/16.
 */
public class InitialSetupHandler extends BaseMultipleRequestsHandler {

    private ReferenceDataRequest referenceDataRequest;

    public InitialSetupHandler(int numberOfRequests,Context context) {
        super(numberOfRequests,context);
    }

    public void start(){
        count = 0;
        fetchOperatives();
    }

    private void fetchOperatives(){

        String operativeDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.OPERATIVE,context);
        OperativeRequest operativeRequest = new OperativeRequest();
        operativeRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.OPERATIVE,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("OperativeRequest SuCccess");
                fetchRestOfData();
            }

            @Override
            public void failed(String message) {
                requestHasFinished("OperativeRequest Failed "+message);
                incrementErrors();
            }
        });
        operativeRequest.getOperatives(operativeDate,"");

    }


    private void fetchRestOfData() {
        String refDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.REFERENCE_DATA,context);
        referenceDataRequest = new ReferenceDataRequest();
        referenceDataRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.REFERENCE_DATA,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("ReferenceDataRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("ReferenceDataRequest Failed " + message);
                incrementErrors();

            }
        });
        referenceDataRequest.getReferenceData(refDate, "");

        String orgDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.ORGANISATION,context);
        OrganisationRequest organisationRequest = new OrganisationRequest();
        organisationRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.ORGANISATION,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("OrganisationRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("OrganisationRequest Failed "+message);
                incrementErrors();

            }
        });
        organisationRequest.getOrganisationData(orgDate,"");

        String siteDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.SITE,context);
        SiteRequest siteRequest = new SiteRequest();
        siteRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.SITE,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("SiteRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("SiteRequest Failed "+message);
                incrementErrors();

            }
        });
        siteRequest.getSites(siteDate,"");


        String propertyDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.PROPERTY,context);
        PropertyRequest propertyRequest = new PropertyRequest();
        propertyRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.PROPERTY,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("PropertyRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("PropertyRequest Failed "+message);
                incrementErrors();

            }
        });
        propertyRequest.getProperties(propertyDate,"");

        String locationDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.LOCATION,context);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.LOCATION,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("LocationRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("LocationRequest Failed "+message);
                incrementErrors();
            }
        });
        locationRequest.getLocationData(locationDate,"");


        String locationGroupDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.LOCATION_GROUP,context);
        LocationGroupRequest groupRequest = new LocationGroupRequest();
        groupRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.LOCATION_GROUP,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("LocationGroupRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("LocationGroupRequest Failed "+message);
                incrementErrors();
            }
        });
        groupRequest.getLocationGroups(locationGroupDate,"");

        String locationGroupMembershipDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.LOCATION_GROUP_MEMBERSHIP,context);
        LocationGroupMembershipRequest locationGroupMembershipRequest = new LocationGroupMembershipRequest();
        locationGroupMembershipRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.LOCATION_GROUP_MEMBERSHIP,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("LocationGroupMembershipRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("LocationGroupMembershipRequest Failed "+message);
                incrementErrors();
            }
        });
        locationGroupMembershipRequest.getLocationGroupMemberships(locationGroupMembershipDate,"");


        String assetDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.ASSET,context);
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.ASSET,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("AssetRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("AssetRequest Failed "+message);
                incrementErrors();
            }
        });
        assetRequest.getAssets(assetDate,"");



        String taskTemplateDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.TASK_TEMPLATE,context);
        TaskTemplateRequest taskTemplateRequest = new TaskTemplateRequest();
        taskTemplateRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.TASK_TEMPLATE,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("TaskTemplateRequest SuCccess");
            }
            @Override
            public void failed(String message) {
                requestHasFinished("TaskTemplateRequest Failed "+message);
                incrementErrors();
            }
        });
        taskTemplateRequest.getTaskTemplates(taskTemplateDate,"");

        String taskTemplateParDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.TASK_TEMPLATE_PARAMETER,context);
        TaskTemplateParameterRequest taskTemplateParameterRequest = new TaskTemplateParameterRequest();
        taskTemplateParameterRequest.addOperationCallback(new OperationCallback() {
            @Override
            public void success() {
                SynchronisationHelper.setSyncronisationDate(realm,BaseWebRequests.TASK_TEMPLATE_PARAMETER,Utils.getUTCDateNow(),context);
                SynchronisationHelper.setSyncronisationHistoryDate(realm,Utils.getUTCDateNow(),SynchronisationHelper.SUCCESS);
                requestHasFinished("TaskTemplateParameterRequest SuCccess");
            }

            @Override
            public void failed(String message) {
                requestHasFinished("TaskTemplateParameterRequest Failed "+message);
                incrementErrors();
            }
        });
        taskTemplateParameterRequest.getTaskTemplateParameters(taskTemplateParDate,"");

        String taskDate = SynchronisationHelper.getSynchronisationDate(realm,BaseWebRequests.TASK,context);
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






