package com.hydrop.compassmobile.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hydrop.compassmobile.Activities.Adapters.SpinnerArrayAdapter;
import com.hydrop.compassmobile.Activities.HelperClasses.FilterHelper;
import com.hydrop.compassmobile.Activities.HelperClasses.SpinnerObject;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.RealmObjects.Asset;
import com.hydrop.compassmobile.RealmObjects.Location;
import com.hydrop.compassmobile.RealmObjects.LocationGroup;
import com.hydrop.compassmobile.RealmObjects.LocationGroupMembership;
import com.hydrop.compassmobile.RealmObjects.Property;
import com.hydrop.compassmobile.RealmObjects.ReferenceData;
import com.hydrop.compassmobile.RealmObjects.Site;
import com.hydrop.compassmobile.RealmObjects.Task;
import com.hydrop.compassmobile.RealmObjects.TaskTemplate;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;
import com.hydrop.compassmobile.WebAPI.Requests.SendTaskRequest;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class CreateTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner siteSpinner,propertySpinner,assetGroupSpinner,taskNameSpinner,assetTypeSpinner,areaSpinner,locationSpinner,assetNumberSpinner;
    public static final String EMPTY_VALUE = "-1";
    public static final String DEFAULT_VALUE = "-2";
    private Realm realm;

    private String selectedSiteId,selectedPropertyId,selectedLocationGroupId,selectedLocationGroupName,
            selectedLocationId,selectedLocationName,selectedLocationDescription,
            selectedAssetType, selectedAssetTypeId,selectedAssetGroupParentValue,selectedTaskName,selectedAssetNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.add_task));
        realm = Realm.getDefaultInstance();
        setupLayout();
        initializeSite();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.doneButton:
                doneButtonPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_task, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void setupLayout(){
        siteSpinner = (Spinner)findViewById(R.id.siteSpinner);
        siteSpinner.setOnItemSelectedListener(this);
        propertySpinner = (Spinner)findViewById(R.id.propertySpinner);
        propertySpinner.setOnItemSelectedListener(this);
        assetGroupSpinner = (Spinner)findViewById(R.id.assetGroupSpinner);
        assetGroupSpinner.setOnItemSelectedListener(this);
        taskNameSpinner = (Spinner)findViewById(R.id.taskNameSpinner);
        taskNameSpinner.setOnItemSelectedListener(this);
        assetTypeSpinner = (Spinner)findViewById(R.id.assetTypeSpinner);
        assetTypeSpinner.setOnItemSelectedListener(this);
        areaSpinner = (Spinner)findViewById(R.id.areaSpinner);
        areaSpinner.setOnItemSelectedListener(this);
        locationSpinner = (Spinner)findViewById(R.id.locationSpinner);
        locationSpinner.setOnItemSelectedListener(this);
        assetNumberSpinner = (Spinner)findViewById(R.id.assetNumberSpinner);
        assetNumberSpinner.setOnItemSelectedListener(this);
    }

    private void initializeSite() {
        RealmQuery<Site> query = realm.where(Site.class);
        query.equalTo("isDeleted",false);
        RealmResults<Site> result = query.findAll().distinct("rowId");
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,getString(R.string.please_select)));
        for (Site site : result){
            values.add(new SpinnerObject(site.getRowId(),site.getName()));
        }
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        siteSpinner.setAdapter(adapter);
        cleanSpinnersFromSite();

    }
    private void cleanSpinnersFromSite(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(propertySpinner);
        emSpinners.add(assetGroupSpinner);
        emSpinners.add(taskNameSpinner);
        emSpinners.add(assetTypeSpinner);
        emSpinners.add(areaSpinner);
        emSpinners.add(locationSpinner);
        emSpinners.add(assetNumberSpinner);

        emptySpinners(emSpinners);

    }

    private void siteSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)siteSpinner.getSelectedItem();

        if (!spinnerObject.getId().equals(DEFAULT_VALUE)){
            selectedSiteId = spinnerObject.getId();
            initialiseProperty(spinnerObject.getId());
        }else{
            cleanSpinnersFromSite();
        }
    }

    private void initialiseProperty(String siteId){
        RealmQuery<Property> query = realm.where(Property.class);
        query.equalTo("siteId",siteId);
        query.equalTo("isDeleted",false);
        RealmResults<Property> result = query.findAll();
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,getString(R.string.please_select)));
        for (Property property : result){
            values.add(new SpinnerObject(property.getRowId(),property.getName()));
        }

        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        propertySpinner.setAdapter(adapter);

    }

    private void cleanSpinnersFromProperty(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(assetGroupSpinner);
        emSpinners.add(taskNameSpinner);
        emSpinners.add(assetTypeSpinner);
        emSpinners.add(areaSpinner);
        emSpinners.add(locationSpinner);
        emSpinners.add(assetNumberSpinner);

        emptySpinners(emSpinners);

    }

    private void propertySpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)propertySpinner.getSelectedItem();
        if (!spinnerObject.getId().equals(DEFAULT_VALUE) && !spinnerObject.getId().equals(EMPTY_VALUE)){
            selectedPropertyId = spinnerObject.getId();
            initializeArea(spinnerObject.getId());
        }else if(spinnerObject.getId().equals(DEFAULT_VALUE)){
            cleanSpinnersFromProperty();
        }
    }

    private void initializeArea(String propertyId){

        RealmQuery<LocationGroup> query = realm.where(LocationGroup.class);
        query.equalTo("propertyId",propertyId);
        query.equalTo("isDeleted",false);
        RealmResults<LocationGroup> result = query.findAll();
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,getString(R.string.please_select)));
        for (LocationGroup locationGroup : result){
            values.add(new SpinnerObject(locationGroup.getRowId(),locationGroup.getName()));
        }

        SpinnerArrayAdapter adapter2 = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        areaSpinner.setAdapter(adapter2);
    }

    private void cleanSpinnersFromArea() {
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(assetGroupSpinner);
        emSpinners.add(taskNameSpinner);
        emSpinners.add(assetTypeSpinner);
        emSpinners.add(locationSpinner);
        emSpinners.add(assetNumberSpinner);
        emptySpinners(emSpinners);
    }

    private void areaSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)areaSpinner.getSelectedItem();
        if (spinnerObject.getId() != DEFAULT_VALUE && spinnerObject.getId() != EMPTY_VALUE){
            selectedLocationGroupId = spinnerObject.getId();
            selectedLocationGroupName = spinnerObject.getName();
            initializeLocation(selectedLocationGroupId);
        }else if(spinnerObject.getId().equals(DEFAULT_VALUE)){
            cleanSpinnersFromArea();
        }
    }

    private void initializeLocation(String locationGroupId){
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,getString(R.string.please_select)));

        RealmQuery<LocationGroupMembership> query = realm.where(LocationGroupMembership.class);
        query.equalTo("locationGroupId",locationGroupId);
        query.equalTo("isDeleted",false);
        RealmResults<LocationGroupMembership> result = query.findAll();
        for (LocationGroupMembership locationGroupMembership:result){
            RealmQuery<Location> locationQuery = realm.where(Location.class);
            locationQuery.equalTo("rowId",locationGroupMembership.getLocationId());
            RealmResults<Location> locationResult = locationQuery.findAll();
            for (Location location : locationResult){
                SpinnerObject spinnerObject = new SpinnerObject(location.getRowId(),location.getName());
                spinnerObject.setExtraRowId(location.getDescription());
                values.add(spinnerObject);
            }
        }
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        locationSpinner.setAdapter(adapter);
    }

    private void locationSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)locationSpinner.getSelectedItem();
        if (spinnerObject.getId() != DEFAULT_VALUE && spinnerObject.getId() != EMPTY_VALUE){
            selectedLocationId = spinnerObject.getId();
            selectedLocationName = spinnerObject.getName();
            selectedLocationDescription = spinnerObject.getExtraRowId();
            initializeAssetType(spinnerObject.getId());
        }else{
            cleanSpinnersFromLocation();
        }

    }

    private void cleanSpinnersFromLocation(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(assetNumberSpinner);
        emSpinners.add(taskNameSpinner);
        emSpinners.add(assetGroupSpinner);
        emSpinners.add(assetTypeSpinner);

        emptySpinners(emSpinners);
    }

    private void initializeAssetType(String locationId){

        RealmQuery<Asset> query = realm.where(Asset.class);
        query.equalTo("locationId",locationId);
        query.equalTo("isDeleted",false);
        RealmResults<Asset> result = query.findAll().distinct("assetType");
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,getString(R.string.please_select)));
        for (Asset asset : result){
            String assetTypeFullName = FilterHelper.getAssetTypeNameForKey(realm,asset.getAssetType());
            SpinnerObject spinnerObject = new SpinnerObject(asset.getAssetType(),assetTypeFullName);
            spinnerObject.setExtraRowId(asset.getRowId());
            values.add(spinnerObject);
        }

        SpinnerArrayAdapter adapter2 = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        assetTypeSpinner.setAdapter(adapter2);
    }

    private void cleanSpinnersFromAssetType(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(assetNumberSpinner);
        emSpinners.add(taskNameSpinner);
        emSpinners.add(assetGroupSpinner);

        emptySpinners(emSpinners);
    }

    private void assetTypeSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)assetTypeSpinner.getSelectedItem();
        if (spinnerObject.getId() != DEFAULT_VALUE && spinnerObject.getId() != EMPTY_VALUE){
            selectedAssetType = spinnerObject.getId();
            selectedAssetTypeId = spinnerObject.getExtraRowId();
            initializeAssetGroup(selectedAssetType);
        }else{
            cleanSpinnersFromAssetType();
        }
    }

    private void initializeAssetGroup(String assetType){
        RealmQuery<ReferenceData> query = realm.where(ReferenceData.class);
        query.equalTo("value",assetType);
        query.equalTo("type","PPMAssetType");
        query.equalTo("parentType","PPMAssetGroup");
        query.equalTo("isDeleted",false);
        RealmResults<ReferenceData> result = query.findAll();
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,getString(R.string.please_select)));
        for (ReferenceData referenceData : result){
            String parentValue = referenceData.getParentValue();
            String fullName = FilterHelper.getAssetGroupNameForKey(realm,parentValue,"PPMAssetGroup");
            values.add(new SpinnerObject(parentValue,fullName));
        }
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        assetGroupSpinner.setAdapter(adapter);



    }

    private void assetGroupSpinnerSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)assetGroupSpinner.getSelectedItem();
        if (spinnerObject.getId() != DEFAULT_VALUE && spinnerObject.getId() != EMPTY_VALUE){
            selectedAssetGroupParentValue = spinnerObject.getId();
            initializeTask(selectedAssetGroupParentValue);
        }else{
            cleanSpinnersFromAssetGroup();
        }

    }

    private void cleanSpinnersFromAssetGroup(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(assetNumberSpinner);
        emSpinners.add(taskNameSpinner);
        emptySpinners(emSpinners);

    }

    private void initializeTask(String assetGroupParentValue){
        RealmQuery<ReferenceData> query = realm.where(ReferenceData.class);
        query.equalTo("parentValue",assetGroupParentValue);
        query.equalTo("type","PPMTaskType");
        query.equalTo("parentType","PPMAssetGroup");
        query.equalTo("isDeleted",false);
        RealmResults<ReferenceData> result = query.findAll();
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,getString(R.string.please_select)));
        for (ReferenceData referenceData : result){
            values.add(new SpinnerObject(referenceData.getValue(),referenceData.getDisplay()));
        }
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        taskNameSpinner.setAdapter(adapter);
    }


    private void taskNameSpinnerSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)taskNameSpinner.getSelectedItem();
        if (spinnerObject.getId() != DEFAULT_VALUE && spinnerObject.getId() != EMPTY_VALUE){
            selectedTaskName = spinnerObject.getId();
            initializeAssetNumber();
        }else{
            cleanSpinnersFromTaskName();
        }

    }

    private void initializeAssetNumber(){
        RealmQuery<Asset> query = realm.where(Asset.class);
        query.equalTo("locationId",selectedLocationId);
        query.equalTo("propertyId",selectedPropertyId);
        query.equalTo("assetType",selectedAssetType);
        query.equalTo("isDeleted",false);
        RealmResults<Asset> result = query.findAll();
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(FilterActivity.DEFAULT_VALUE,getString(R.string.please_select)));
        for (Asset asset : result){
            String scanCode = Utils.checkNotNull((asset.getScanCode())) ? asset.getScanCode() : "";
            String clientName = asset.getClientName();
            if (!Utils.checkNotNull(clientName)){
                clientName = (Utils.checkNotNull(asset.getHydropName())) ? asset.getHydropName() : "UNKNOWN";
            }
            values.add(new SpinnerObject(asset.getAssetType(),scanCode+clientName));
        }

        SpinnerArrayAdapter adapter2 = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        assetNumberSpinner.setAdapter(adapter2);

    }

    private void assetNumberSpinnerSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)assetNumberSpinner.getSelectedItem();
        if (spinnerObject.getId() != DEFAULT_VALUE && spinnerObject.getId() != EMPTY_VALUE){
            selectedAssetNumber = spinnerObject.getName();
            //selectedAssetGroupParentValue = spinnerObject.getId();
            //initializeAssetNumber();
        }else{
            //cleanSpinnersFromTaskName();
        }

    }

    private void cleanSpinnersFromTaskName(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(assetNumberSpinner);
        emptySpinners(emSpinners);

    }




    private void emptySpinners(ArrayList<Spinner> spinners){
        for (Spinner spinner:spinners){
            SpinnerObject spinnerObject = new SpinnerObject(""+EMPTY_VALUE,getString(R.string.not_applicable));
            ArrayList<SpinnerObject> spinnerObjects = new ArrayList<>();
            spinnerObjects.add(spinnerObject);

            SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerObjects);
            spinner.setAdapter(adapter);

        }
    }

    private boolean allFieldsCompleted(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(siteSpinner);
        emSpinners.add(propertySpinner);
        emSpinners.add(assetGroupSpinner);
        emSpinners.add(taskNameSpinner);
        emSpinners.add(assetTypeSpinner);
        emSpinners.add(areaSpinner);
        emSpinners.add(locationSpinner);
        emSpinners.add(assetNumberSpinner);
        for (Spinner spinner : emSpinners){
            String selectedId = ((SpinnerObject)spinner.getSelectedItem()).getId();
            if (selectedId.equals(DEFAULT_VALUE) || selectedId.equals(EMPTY_VALUE)){
                return false;
            }
        }
        return true;


    }


    private void doneButtonPressed(){
        if (allFieldsCompleted()){
            String taskId = createTaskAndReturnId();
            Intent intent = new Intent();
            intent.putExtra("taskId",taskId);
            setResult(RESULT_OK,intent);
            finish();

        }else{
            Toast.makeText(this,getString(R.string.all_fields_must_be_completed),Toast.LENGTH_SHORT).show();
        }
    }

    private String createTaskAndReturnId(){
        Task task = new Task();
        task.setRowId(UUID.randomUUID().toString());
        task.setCreatedBy(BaseWebRequests.operativeId);
        task.setCreatedOn(Utils.getUTCDateNow());
        task.setOrganisationId(BaseWebRequests.getOrganisationId(realm,this));
        task.setSiteId(selectedSiteId);
        task.setPropertyId(selectedPropertyId);
        task.setLocationId(selectedLocationId);
        task.setAssetId(selectedAssetTypeId);
        task.setAssetType(selectedAssetType);
        task.setOperativeId(BaseWebRequests.operativeId);
        task.setLocationGroupName(selectedLocationGroupName);
        task.setLocationName(selectedLocationName);
        task.setTaskRef(Utils.getTaskRef());
        task.setPpmGroup(selectedAssetGroupParentValue);
        task.setTaskName(selectedTaskName);
        task.setFrequency("Ad-hoc");
        task.setAssetNumber(selectedAssetNumber);
        task.setScheduledDate(Utils.getUTCDateNow());
        task.setStatus("Pending");

        task.setActualDuration(0);
        task.setTravelDuration(0);
        task.setRoom(selectedLocationDescription);
        task.setDeleted(false);

        RealmQuery<TaskTemplate> query = realm.where(TaskTemplate.class);
        query.equalTo("organisationId",task.getOrganisationId());
        query.equalTo("taskName",task.getTaskName());
        query.equalTo("assetType",task.getPpmGroup());
        TaskTemplate taskTemplate = query.findFirst();
        if (Utils.checkNotNull(taskTemplate)){
            task.setTaskTemplateId(taskTemplate.getRowId());
            task.setPriority(taskTemplate.getPriority());
            task.setEstimatedDuration(task.getEstimatedDuration());
        }

        realm.beginTransaction();
        realm.copyToRealm(task);
        realm.commitTransaction();

        return task.getRowId();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == siteSpinner){
            siteSpinnerItemSelected();
        }else if (parent == propertySpinner){
            propertySpinnerItemSelected();
        }else if (parent == areaSpinner){
            areaSpinnerItemSelected();
        }else if (parent == locationSpinner){
            locationSpinnerItemSelected();
        }else if (parent == assetTypeSpinner){
            assetTypeSpinnerItemSelected();
        }else if (parent == assetGroupSpinner){
            assetGroupSpinnerSelected();
        }else if (parent == taskNameSpinner){
            taskNameSpinnerSelected();
        }else if (parent == assetNumberSpinner){
            assetNumberSpinnerSelected();
        }

    }
}
