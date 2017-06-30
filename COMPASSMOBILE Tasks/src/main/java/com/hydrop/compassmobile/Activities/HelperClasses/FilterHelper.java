package com.hydrop.compassmobile.Activities.HelperClasses;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hydrop.compassmobile.Activities.FilterActivity;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.RealmObjects.Property;
import com.hydrop.compassmobile.RealmObjects.ReferenceData;
import com.hydrop.compassmobile.RealmObjects.Site;
import com.hydrop.compassmobile.RealmObjects.Task;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Panos on 13/05/16.
 */
public class FilterHelper {


    public static ArrayList<SpinnerObject> siteResults;
    public static String selectedSiteId;
    public static String selectedSiteName;

    public static ArrayList<SpinnerObject> propertyResults;
    public static String selectedPropertyId;
    public static String selectedPropertyName;


    public static ArrayList<SpinnerObject> ppmGroupResults;
    public static String selectedPpmGroupName;


    public static ArrayList<SpinnerObject> taskNameResults;
    public static String selectedTaskName;


    public static ArrayList<SpinnerObject> assetTypeResults;
    public static String selectedAssetType;


    public static ArrayList<SpinnerObject> locationGroupNameResults;
    public static String selectedLocationGroupName;


    public static ArrayList<SpinnerObject> locationNameResults;
    public static String selectedLocationName;


    public static ArrayList<SpinnerObject> assetNumberResults;
    public static String selectedAssetNumber;


    public static ArrayList<SpinnerObject> frequencyResults;
    public static String selectedFrequency;
    public static int selectedFrequencySpinnerIndex;


    public static String selectedDateRange;
    public static int selectedDateRangeSpinnerIndex = 0;



    public static HashMap <String,String> assetGroupMap = new HashMap<>();
    public static HashMap <String,String> taskNamesMap = new HashMap<>();
    public static HashMap <String,String> assetTypeMap = new HashMap<>();
    public static HashMap <String,String> propertiesNameMap = new HashMap<>();


    public static String sortBy = "scheduledDate";
    public static boolean myTasks = false;

    public static void resetSort(){
        sortBy = "scheduledDate";
    }
    public static void resetDateRange(){
        selectedDateRangeSpinnerIndex = 0;
    }

    public static Date getEndDate(){
        Date today = new Date();
        if (selectedDateRangeSpinnerIndex == 0){//Due Today
            return Utils.endfDay(today);
        }else if (selectedDateRangeSpinnerIndex == 1){//Due This Week
            return Utils.endOfWeek(today);
        }else if (selectedDateRangeSpinnerIndex == 2){//Due This Month
            return Utils.endOfMonth(today);
        }else if (selectedDateRangeSpinnerIndex == 3){//Due By the End of Next Month
            return Utils.endOfNextMonth(today);
        }else if (selectedDateRangeSpinnerIndex == 4){//All
            return null;
        }
        return Utils.endfDay(today);
    }

    public static RealmQuery<Task> getTaskQuery(Realm realm,Date endDate){
        return getTaskQuery(realm,endDate,null);

    }

    public static RealmQuery<Task> getTaskQuery(Realm realm,Date endDate,Date startDate){
        RealmQuery<Task> query = realm.where(Task.class);
        if (Utils.checkNotNull(endDate)){
            query.lessThanOrEqualTo("scheduledDate",endDate);
            if (Utils.checkNotNull(startDate)){
                query.greaterThanOrEqualTo("scheduledDate",startDate);
            }
        }

        return query;
    }

    public static RealmQuery<Task> getFilterQuery(Realm realm,Context context){
        RealmQuery<Task> query = getTaskQuery(realm,getEndDate());
        query.equalTo("organisationId",BaseWebRequests.getOrganisationId(realm,context));
        query.equalTo("isDeleted",false);
        query.notEqualTo("status","Complete");
        if(Utils.checkNotNull(selectedSiteId)){
            query.equalTo("siteId",selectedSiteId);
        }
        if(Utils.checkNotNull(selectedPropertyId)){
            query.equalTo("propertyId",selectedPropertyId);
        }
        if(Utils.checkNotNull(selectedLocationGroupName)){
            query.equalTo("locationGroupName",selectedLocationGroupName);
        }
        if(Utils.checkNotNull(selectedLocationName)){
            query.equalTo("locationName",selectedLocationName);
        }
        if(Utils.checkNotNull(selectedPpmGroupName)){
            query.equalTo("ppmGroup",selectedPpmGroupName);
        }
        if(Utils.checkNotNull(selectedTaskName)){
            query.equalTo("taskName",selectedTaskName);
        }
        if(Utils.checkNotNull(selectedAssetNumber)){
            query.equalTo("assetNumber",selectedAssetNumber);
        }
        if(Utils.checkNotNull(selectedAssetType)){
            query.equalTo("assetType",selectedAssetType);
        }
        if(Utils.checkNotNull(selectedFrequency)){
            query.equalTo("Frequency",selectedFrequency);
        }
        if(myTasks){
            query.equalTo("operativeId",BaseWebRequests.operativeId);
        } else {
            query.beginGroup();
            query.equalTo("operativeId", BaseWebRequests.operativeId);
            query.or();
            query.equalTo("operativeId", "00000000-0000-0000-0000-000000000000");
            query.or();
            query.isNull("operativeId");
            query.endGroup();
        }
        BaseWebRequests.webPrint("index "+selectedDateRangeSpinnerIndex);
        if (selectedDateRangeSpinnerIndex == 3) {//Due By the End of Next Month
            query.greaterThanOrEqualTo("scheduledDate",new Date());
        }


        return query;
    }


    public static ArrayList<SpinnerObject> getFrequencies(Realm realm,Context context) {

        if (frequencyResults == null){
            RealmQuery<ReferenceData> query = realm.where(ReferenceData.class);
            query.equalTo("type","PPMFrequency");
            RealmResults<ReferenceData> result = query.findAll();
            ArrayList<SpinnerObject> values = new ArrayList<>();
            values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,context.getString(R.string.please_select)));
            for (ReferenceData referenceData:result){
                values.add(new SpinnerObject("ss",referenceData.getDisplay()));

            }
            frequencyResults = values;
        }

        return  frequencyResults;


    }



    public static ArrayList<SpinnerObject> getSites(Realm realm,Context context){
        RealmQuery<Task> query = getTaskQuery(realm,getEndDate());
        query.contains("organisationId",BaseWebRequests.getOrganisationId(realm,context));
        RealmResults<Task> result = query.findAll().distinct("siteId");
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,context.getString(R.string.please_select)));
        for (Task task:result){
            String siteId = task.getSiteId();
            RealmQuery<Site> siteQuery = realm.where(Site.class);
            siteQuery.equalTo("rowId",siteId);
            RealmResults<Site> resultSite = siteQuery.findAll();
            if (resultSite.size() > 0){
                values.add(new SpinnerObject(siteId,resultSite.first().getName()));
            }

        }
        siteResults = values;
    return values;
    }

    public static ArrayList<SpinnerObject> getProperties(Realm realm,Context context,String siteId){
        RealmQuery<Task> query = getTaskQuery(realm,getEndDate());
        query.equalTo("siteId",siteId);
        RealmResults<Task> result = query.findAll().distinct("propertyId");
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,context.getString(R.string.please_select)));
        for (Task task:result){
            String propertyId = task.getPropertyId();
            RealmQuery<Property> propertyQuery = realm.where(Property.class);
            propertyQuery.equalTo("rowId",propertyId);
            RealmResults<Property> resultProperty = propertyQuery.findAll();
            if (resultProperty.size()>0){
                values.add(new SpinnerObject(propertyId,resultProperty.first().getName()));
            }
        }
        propertyResults = values;
        return values;
    }


    public static String getAssetGroupNameForKey(Realm realm , String ppmGroup, @NonNull String type){
        String fullAssetGroupName ;
        String key = ppmGroup+type;
        if (assetGroupMap.get(key) != null){
            fullAssetGroupName = assetGroupMap.get(key);
        }else{
            RealmQuery<ReferenceData> query2 = realm.where(ReferenceData.class);
            query2.equalTo("value",ppmGroup);
            query2.equalTo("type",type);
            RealmResults<ReferenceData> referenceData = query2.findAll();
            if (referenceData.size() > 0 ){
                fullAssetGroupName = referenceData.first().getDisplay();
                assetGroupMap.put(key,fullAssetGroupName);
            }else{
                fullAssetGroupName = ppmGroup;
            }
        }
        return fullAssetGroupName;
    }

    public static String getPropertyNameForKey(Realm realm,String propertyId){
        if (Utils.checkNotNull(propertiesNameMap.get(propertyId))){
            return propertiesNameMap.get(propertyId);
        }
        BaseWebRequests.webPrint(propertyId);
        RealmQuery<Property> query = realm.where(Property.class);
        query.equalTo("rowId",propertyId);
        Property property = query.findFirst();
        if (!Utils.checkNotNull(property)){
            return "PROPERTY NOT FOUND";
        }
        String propertyName = property.getName();
        propertiesNameMap.put(propertyId,propertyName);
        return propertyName;

    }
    public static ArrayList<SpinnerObject> getAssetGroup(Realm realm,Context context,String propertyId){
        RealmQuery<Task> query = getTaskQuery(realm,getEndDate());
        query.equalTo("siteId",selectedSiteId);
        query.equalTo("propertyId",propertyId);
        RealmResults<Task> result = query.findAll().distinct("ppmGroup");
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,context.getString(R.string.please_select)));
        for (Task task:result){
            String ppmGroup = task.getPpmGroup();
            if (Utils.checkNotNull(ppmGroup)){
                String fullAssetGroupName = getAssetGroupNameForKey(realm,ppmGroup,"PPMAssetGroup");
                values.add(new SpinnerObject(ppmGroup,fullAssetGroupName));

            }
        }
        ppmGroupResults = values;
        return values;
    }

    public static ArrayList<SpinnerObject> getArea(Realm realm,Context context,String propertyId){
        RealmQuery<Task> query = getTaskQuery(realm,getEndDate());
        query.equalTo("siteId",selectedSiteId);
        query.equalTo("propertyId",propertyId);
        RealmResults<Task> result = query.findAll().distinct("locationGroupName");
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,context.getString(R.string.please_select)));
        for (Task task:result){
            String locationGroupName = task.getLocationGroupName();
            values.add(new SpinnerObject(null,locationGroupName));
        }
        locationGroupNameResults = values;
        return values;
    }

    public static ArrayList<SpinnerObject> getLocation(Realm realm,Context context,String locationGroupName){
        RealmQuery<Task> query = getTaskQuery(realm,getEndDate());
        query.equalTo("siteId",selectedSiteId);
        query.equalTo("propertyId",selectedPropertyId);
        query.equalTo("locationGroupName",locationGroupName);

        RealmResults<Task> result = query.findAll().distinct("locationName");
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,context.getString(R.string.please_select)));
        for (Task task:result){
            String locationName = task.getLocationName();
            values.add(new SpinnerObject(null,locationName));
        }
        locationNameResults = values;
        return values;
    }

    public static ArrayList<SpinnerObject> getAssetNumber(Realm realm,Context context,String locationName){
        RealmQuery<Task> query = getTaskQuery(realm,getEndDate());
        query.equalTo("siteId",selectedSiteId);
        query.equalTo("propertyId",selectedPropertyId);
        query.equalTo("locationGroupName",selectedLocationGroupName);
        query.equalTo("locationName",locationName);


        RealmResults<Task> result = query.findAll().distinct("assetNumber");
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,context.getString(R.string.please_select)));
        for (Task task:result){
            String assetNumber = task.getAssetNumber();
            values.add(new SpinnerObject(null,assetNumber));
        }
        assetNumberResults = values;
        return values;
    }


    public static String getAssetTypeNameForKey(Realm realm,String assetType){
        String fullAssetType;

        if (assetTypeMap.get(assetType) != null) {
            fullAssetType = assetTypeMap.get(assetType);

        } else {
            RealmQuery<ReferenceData> query2 = realm.where(ReferenceData.class);
            query2.equalTo("value", assetType);
            RealmResults<ReferenceData> referenceData = query2.findAll();
            if (referenceData.size() > 0){
                fullAssetType = referenceData.first().getDisplay();
                assetTypeMap.put(assetType, fullAssetType);
            }else{
                fullAssetType = assetType;
            }
        }
        return fullAssetType;

    }

    public static ArrayList<SpinnerObject> getAssetType(Realm realm,Context context,String taskName){
        RealmQuery<Task> query = getTaskQuery(realm,getEndDate());
        query.equalTo("siteId",selectedSiteId);
        query.equalTo("propertyId",selectedPropertyId);
        query.equalTo("ppmGroup",selectedPpmGroupName);
        query.equalTo("taskName",taskName);

        RealmResults<Task> result = query.findAll().distinct("taskName");
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,context.getString(R.string.please_select)));
        for (Task task:result) {
            String assetType = task.getAssetType();
            String fullAssetType = getAssetTypeNameForKey(realm,assetType);

            values.add(new SpinnerObject(assetType, fullAssetType));

        }
        assetTypeResults = values;
        return values;
    }

    public static String getTaskFullNameForKey(Realm realm,String taskName,String type,String parentType,String parentValue){
        String fullTaskName;
        String key = taskName+type+parentType+parentValue;
        if (taskNamesMap.get(key) != null) {
            fullTaskName = taskNamesMap.get(key);
        } else {
            RealmQuery<ReferenceData> query = realm.where(ReferenceData.class);
            query.equalTo("value", taskName);
            query.equalTo("type",type);
            query.equalTo("parentType",parentType);
            query.equalTo("parentValue",parentValue);
            query.equalTo("isDeleted",false);
            RealmResults<ReferenceData> referenceData = query.findAll();
            if (referenceData.size() > 0){
                fullTaskName = referenceData.first().getDisplay();
                taskNamesMap.put(key, fullTaskName);
            }else{
                fullTaskName = taskName;
            }
        }
        return fullTaskName;

    }

    public static ArrayList<SpinnerObject> getTaskName(Realm realm,Context context,String ppmGroup){
        RealmQuery<Task> query = getTaskQuery(realm,getEndDate());
        query.equalTo("siteId",selectedSiteId);
        query.equalTo("propertyId",selectedPropertyId);
        query.equalTo("ppmGroup",ppmGroup, Case.INSENSITIVE);
        RealmResults<Task> result = query.findAll().distinct("taskName");
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,context.getString(R.string.please_select)));
        for (Task task:result) {
            String taskName = task.getTaskName();
            String fullTaskName = getTaskFullNameForKey(realm,taskName,"PPMTaskType","PPMAssetGroup",task.getPpmGroup());
            values.add(new SpinnerObject(taskName, fullTaskName));

        }
        taskNameResults = values;
        return values;
    }


    public static void resetAll(){
        siteResults = null;
        selectedSiteId = null;
        selectedSiteName = null;

        propertyResults = null;
        selectedSiteId = null;
        selectedPropertyName = null;

        ppmGroupResults = null;
        selectedPpmGroupName = null;

        taskNameResults = null;
        selectedTaskName = null;

        assetTypeResults = null;
        selectedAssetType = null;

        locationGroupNameResults = null;
        selectedLocationGroupName = null;

        locationNameResults = null;
        selectedLocationName = null;


        assetNumberResults = null;
        selectedAssetNumber = null;

        frequencyResults = null;
        selectedFrequency = null;
        selectedFrequencySpinnerIndex = 0;


        selectedDateRange = null;
        selectedDateRangeSpinnerIndex = 0;

    }

    public void resetCache(){
        assetGroupMap = new HashMap<>();
        taskNamesMap = new HashMap<>();
        assetTypeMap = new HashMap<>();

    }

}
