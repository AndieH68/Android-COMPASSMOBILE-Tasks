package com.hydrop.compassmobile.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.hydrop.compassmobile.Activities.Adapters.SpinnerArrayAdapter;
import com.hydrop.compassmobile.Activities.HelperClasses.FilterHelper;
import com.hydrop.compassmobile.Activities.HelperClasses.SpinnerObject;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.Utils;
import com.hydrop.compassmobile.WebAPI.Requests.BaseWebRequests;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner siteSpinner,propertySpinner,frequencySpinner,periodSpinner,assetGroupSpinner,taskNameSpinner,assetTypeSpinner,areaSpinner,locationSpinner,assetNumberSpinner;
    private RadioGroup radioGroup;
    private Switch switchControl;
    private Realm realm;
    public static final String EMPTY_VALUE = "-1";
    public static final String DEFAULT_VALUE = "-2";

    private void debugPrint(String message){
        Log.d("checkingFs",message);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setTitle(getString(R.string.filter));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        realm = Realm.getDefaultInstance();
        setupLayout();
        recoverFilters();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                return true;
            case R.id.clear_filter:
                FilterHelper.resetAll();
                FilterHelper.resetDateRange();
                initializeSite(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }


    private void setupLayout(){
        siteSpinner = (Spinner)findViewById(R.id.siteSpinner);
        siteSpinner.setOnItemSelectedListener(this);
        propertySpinner = (Spinner)findViewById(R.id.propertySpinner);
        propertySpinner.setOnItemSelectedListener(this);
        frequencySpinner = (Spinner)findViewById(R.id.frequencySpinner);
        frequencySpinner.setOnItemSelectedListener(this);

        SpinnerArrayAdapter freqAdapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, FilterHelper.getFrequencies(realm,this));
        frequencySpinner.setAdapter(freqAdapter);

        periodSpinner = (Spinner)findViewById(R.id.periodSpinner);
        periodSpinner.setOnItemSelectedListener(this);
        ArrayList<SpinnerObject> periods = new  ArrayList<>();
        int  counter = 0;
        for (String period : getResources().getStringArray(R.array.date_ranges)){
            periods.add(new SpinnerObject(""+counter,period));
            counter++;
        }
        SpinnerArrayAdapter periodAdapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, periods);
        periodSpinner.setAdapter(periodAdapter);
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

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.dateRadioButton){
                    FilterHelper.sortBy = "scheduledDate";
                }else if (checkedId == R.id.locationRadioButton){
                    FilterHelper.sortBy = "locationName";
                }else if (checkedId == R.id.typeRadioButton){
                    FilterHelper.sortBy = "assetType";
                }else if (checkedId == R.id.taskRadioButton){
                    FilterHelper.sortBy = "taskName";
                }
            }
        });

        switchControl = (Switch)findViewById(R.id.switchControl);
        switchControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FilterHelper.myTasks = isChecked;
            }
        });

    }


    private int spinnerPositionFinderById(String id,ArrayList<SpinnerObject> values){
        int position = 0;
        for (SpinnerObject spinnerObject:values){
            if (spinnerObject.getId().equals(id)){
                return position;
            }
            position++;
        }
        return 0;
    }

    private int spinnerPositionFinderByString(String name,ArrayList<SpinnerObject> values){
        int position = 0;
        for (SpinnerObject spinnerObject:values){
            if (spinnerObject.getName().equals(name)){
                return position;
            }
            position++;
        }
        return 0;
    }



    private void initializeSite(boolean forceCacheReset) {
        if (siteSpinner == null){
            debugPrint("1");
        }
        if (siteSpinner.getAdapter() == null){
            debugPrint("2");
        }

        ArrayList<SpinnerObject> values = FilterHelper.siteResults;
        if (!Utils.checkNotNull(values) || forceCacheReset){
            values = FilterHelper.getSites(realm,this);
        }
        int position = spinnerPositionFinderById(FilterHelper.selectedSiteId,values);
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        siteSpinner.setAdapter(adapter);
        siteSpinner.setSelection(position);

       // cleanSpinnersFromSite();

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
            initialiseProperty(spinnerObject.getId(),false);
            FilterHelper.selectedSiteName = spinnerObject.getName();
        }else{
            cleanSpinnersFromSite();
            FilterHelper.selectedSiteId = null;
            FilterHelper.selectedSiteName = null;
        }
    }

    private void initialiseProperty(String siteId,boolean forceCacheReset){
        ArrayList<SpinnerObject> values = FilterHelper.propertyResults;
        if (!Utils.checkNotNull(values) || !siteId.equals(FilterHelper.selectedSiteId) || forceCacheReset){
            values = FilterHelper.getProperties(realm,this,siteId);
        }
        int position = spinnerPositionFinderById(FilterHelper.selectedPropertyId,values);

        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        propertySpinner.setAdapter(adapter);
        propertySpinner.setSelection(position);

        FilterHelper.selectedSiteId = siteId;

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
            initializeGroupAndArea(spinnerObject.getId(),false);
            FilterHelper.selectedPropertyName = spinnerObject.getName();

        }else{
            cleanSpinnersFromProperty();
            FilterHelper.selectedPropertyId = null;
            FilterHelper.selectedPropertyName = null;
        }
    }

    private void initializeGroupAndArea(String propertyId,boolean forceCacheReset){
        ArrayList<SpinnerObject> values = FilterHelper.ppmGroupResults;
        if (!Utils.checkNotNull(values) || !propertyId.equals(FilterHelper.selectedPropertyId) || forceCacheReset){
            values = FilterHelper.getAssetGroup(realm,this,propertyId);
        }
        int position = spinnerPositionFinderById(FilterHelper.selectedPpmGroupName,values);
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        assetGroupSpinner.setAdapter(adapter);
        assetGroupSpinner.setSelection(position);

        ArrayList<SpinnerObject> values2 = FilterHelper.locationGroupNameResults;
        if (!Utils.checkNotNull(values2) || !propertyId.equals(FilterHelper.selectedPropertyId) || forceCacheReset){
            values2 = FilterHelper.getArea(realm,this,propertyId);
        }

        int position2 = spinnerPositionFinderByString(FilterHelper.selectedLocationGroupName,values2);
        SpinnerArrayAdapter adapter2 = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values2);
        areaSpinner.setAdapter(adapter2);
        areaSpinner.setSelection(position2);


        FilterHelper.selectedPropertyId = propertyId;

    }
    private void cleanSpinnersFromAssetGroup(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(taskNameSpinner);
        emSpinners.add(assetTypeSpinner);
        emptySpinners(emSpinners);
    }
    private void cleanSpinnersFromArea(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(locationSpinner);
        emSpinners.add(assetNumberSpinner);
        emptySpinners(emSpinners);

    }


    private void assetGroupSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)assetGroupSpinner.getSelectedItem();
        if (!spinnerObject.getId().equals(DEFAULT_VALUE) && !spinnerObject.getId().equals(EMPTY_VALUE)){
            BaseWebRequests.webPrint(spinnerObject.getId());
            initializeTaskName(spinnerObject.getId());
        }else{
            cleanSpinnersFromAssetGroup();
            FilterHelper.selectedPpmGroupName = null;
        }
    }

    private void cleanSpinnersFromTaskName(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(assetTypeSpinner);
        emptySpinners(emSpinners);
    }



    private void initializeTaskName(String ppmGroup){
        ArrayList<SpinnerObject> values = FilterHelper.taskNameResults;
        if (!Utils.checkNotNull(values) || !ppmGroup.equals(FilterHelper.selectedPpmGroupName)){
            values = FilterHelper.getTaskName(realm,this,ppmGroup);
        }
        int position = spinnerPositionFinderById(FilterHelper.selectedTaskName,values);

        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        taskNameSpinner.setAdapter(adapter);
        taskNameSpinner.setSelection(position);

        FilterHelper.selectedPpmGroupName = ppmGroup;

    }

    private void taskNameSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)taskNameSpinner.getSelectedItem();
        if (!spinnerObject.getId().equals(DEFAULT_VALUE) && !spinnerObject.getId().equals(EMPTY_VALUE)){
            initializeAssetType(spinnerObject.getId(),false);
        }else{
            cleanSpinnersFromTaskName();
            FilterHelper.selectedTaskName = null;
        }
    }

    private void initializeAssetType(String taskName,boolean forceCacheReset){
        ArrayList<SpinnerObject> values = FilterHelper.assetTypeResults;
        if (!Utils.checkNotNull(values) || !taskName.equals(FilterHelper.selectedTaskName) || forceCacheReset){
            values = FilterHelper.getAssetType(realm,this,taskName);
        }
        int position = spinnerPositionFinderByString(FilterHelper.selectedAssetType,values);

        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        assetTypeSpinner.setAdapter(adapter);
        assetTypeSpinner.setSelection(position);

        FilterHelper.selectedTaskName = taskName;

    }

    private void assetTypeSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)assetTypeSpinner.getSelectedItem();
        if (!spinnerObject.getId().equals(DEFAULT_VALUE) && !spinnerObject.getId().equals(EMPTY_VALUE)){
            FilterHelper.selectedAssetType = spinnerObject.getName();
        }else{
            FilterHelper.selectedAssetType = null;
        }

    }
    private void areaSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)areaSpinner.getSelectedItem();
        if (spinnerObject.getId() == null ||!spinnerObject.getId().equals(DEFAULT_VALUE) && !spinnerObject.getId().equals(EMPTY_VALUE)){
            initializeLocation(spinnerObject.getName(),false);
        }else{
            cleanSpinnersFromArea();
            FilterHelper.selectedLocationGroupName = null;
        }
    }

    private void initializeLocation(String locationGroupName,boolean forceCacheReset){
        ArrayList<SpinnerObject> values = FilterHelper.locationNameResults;
        if (!Utils.checkNotNull(values) || !locationGroupName.equals(FilterHelper.selectedLocationGroupName) || forceCacheReset){
            values = FilterHelper.getLocation(realm,this,locationGroupName);
        }
        int position = spinnerPositionFinderByString(FilterHelper.selectedLocationName,values);
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setSelection(position);


        FilterHelper.selectedLocationGroupName = locationGroupName;
    }

    private void locationSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)locationSpinner.getSelectedItem();
        if (spinnerObject.getId() == null ||!spinnerObject.getId().equals(DEFAULT_VALUE) && !spinnerObject.getId().equals(EMPTY_VALUE)){
            initializeAssetNumber(spinnerObject.getName(),false);
        }else{
            cleanSpinnersFromLocation();
            FilterHelper.selectedLocationName = null;
        }

    }

    private void cleanSpinnersFromLocation(){
        ArrayList<Spinner> emSpinners = new ArrayList<>();
        emSpinners.add(assetNumberSpinner);
        emptySpinners(emSpinners);
    }

    private void initializeAssetNumber(String locationName,boolean forceCacheReset){
        ArrayList<SpinnerObject> values = FilterHelper.assetNumberResults;
        if (!Utils.checkNotNull(values) || !locationName.equals(FilterHelper.selectedLocationName)  || forceCacheReset){
            values = FilterHelper.getAssetNumber(realm,this,locationName);
        }

        int position = spinnerPositionFinderByString(FilterHelper.selectedAssetNumber,values);
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, values);
        assetNumberSpinner.setAdapter(adapter);
        assetNumberSpinner.setSelection(position);

        FilterHelper.selectedLocationName = locationName;
    }

    private void assetNumberSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)assetNumberSpinner.getSelectedItem();
        FilterHelper.selectedAssetNumber = null;
        if ( spinnerObject.getId() == null ||!spinnerObject.getId().equals(DEFAULT_VALUE) && !spinnerObject.getId().equals(EMPTY_VALUE)){
            FilterHelper.selectedAssetNumber = spinnerObject.getName();
        }
    }
    private void frequencySpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)frequencySpinner.getSelectedItem();
        FilterHelper.selectedFrequency = null;
        if (!spinnerObject.getId().equals(DEFAULT_VALUE) && !spinnerObject.getId().equals(EMPTY_VALUE)){
            FilterHelper.selectedFrequency = spinnerObject.getName();
        }

    }


    private void dateSpinnerItemSelected(){
        SpinnerObject spinnerObject = (SpinnerObject)periodSpinner.getSelectedItem();
        ArrayList<String> rangeOptions = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.date_ranges)));
        int previousIndex = rangeOptions.indexOf(FilterHelper.selectedDateRange);
        int newIndex = Integer.parseInt(spinnerObject.getId());
        BaseWebRequests.webPrint("previous "+previousIndex+" newIndex "+newIndex);
        if (previousIndex >=0 && newIndex >=0){
            if (previousIndex < newIndex){//Amend Data
                adjustFilters(true);
            }else if (previousIndex > newIndex){//Reset
                FilterHelper.resetAll();
                initializeSite(false);
            }
        }

        FilterHelper.selectedDateRangeSpinnerIndex = newIndex;
        FilterHelper.selectedDateRange = spinnerObject.getName();

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


    private void adjustFilters(boolean forceCacheReset){
        initializeSite(forceCacheReset);
        if (Utils.checkNotNull(FilterHelper.selectedSiteId)){
            initialiseProperty(FilterHelper.selectedSiteId,forceCacheReset);
        }
        if (Utils.checkNotNull(FilterHelper.selectedPropertyId)){
            initializeGroupAndArea(FilterHelper.selectedPropertyId,forceCacheReset);
        }
        if (Utils.checkNotNull(FilterHelper.selectedPpmGroupName)){
            initializeTaskName(FilterHelper.selectedPpmGroupName);
        }
        if (Utils.checkNotNull(FilterHelper.selectedTaskName)){
            initializeAssetType(FilterHelper.selectedTaskName,forceCacheReset);
        }
        if (Utils.checkNotNull(FilterHelper.selectedLocationGroupName)){
            initializeLocation(FilterHelper.selectedLocationGroupName,forceCacheReset);
        }
        if (Utils.checkNotNull(FilterHelper.selectedLocationName)){
            initializeAssetNumber(FilterHelper.selectedLocationName,forceCacheReset);
        }
        periodSpinner.setSelection(FilterHelper.selectedDateRangeSpinnerIndex);
        frequencySpinner.setSelection(FilterHelper.selectedFrequencySpinnerIndex);
        switchControl.setChecked(FilterHelper.myTasks);

        String selectedSort = FilterHelper.sortBy;

        switch (selectedSort){
            case "scheduledDate":
                radioGroup.check(R.id.dateRadioButton);
                break;
            case "locationName":
                radioGroup.check(R.id.locationRadioButton);
                break;
            case "assetType":
                radioGroup.check(R.id.typeRadioButton);
                break;
            case "taskName":
                radioGroup.check(R.id.taskRadioButton);
                break;

        }

    }

    private void recoverFilters(){
        adjustFilters(false);
    }



@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent == siteSpinner){
                siteSpinnerItemSelected();
            }else if (parent == propertySpinner){
                propertySpinnerItemSelected();
            }else if (parent == assetGroupSpinner){
                assetGroupSpinnerItemSelected();
            }else if (parent == taskNameSpinner){
                taskNameSpinnerItemSelected();
            }else if (parent == assetTypeSpinner){
                assetTypeSpinnerItemSelected();
            }else if (parent == areaSpinner){
                areaSpinnerItemSelected();
            }else if (parent == locationSpinner){
                locationSpinnerItemSelected();
            }else if (parent == assetNumberSpinner){
                assetNumberSpinnerItemSelected();
            }else if (parent == frequencySpinner){
                FilterHelper.selectedFrequencySpinnerIndex = position;
                frequencySpinnerItemSelected();
            }else if (parent == periodSpinner){
                FilterHelper.selectedDateRangeSpinnerIndex = position;
                dateSpinnerItemSelected();
            }


        }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
