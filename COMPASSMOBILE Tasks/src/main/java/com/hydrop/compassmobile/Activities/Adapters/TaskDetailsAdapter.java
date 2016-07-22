package com.hydrop.compassmobile.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hydrop.compassmobile.Activities.HelperClasses.SpinnerObject;
import com.hydrop.compassmobile.Activities.HelperClasses.TaskDetailsItem;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.RealmObjects.ReferenceData;
import com.hydrop.compassmobile.RealmObjects.TaskTemplateParameter;
import com.hydrop.compassmobile.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Panos on 23/05/16.
 */
public class TaskDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String PLEASE_SELECT = "Please Select";
    private TaskDetailsAdapterOnClick taskDetailsAdapterOnClick;
    public interface TaskDetailsAdapterOnClick{
        void searchButtonPressedForItemInPosition(int position);
    }
    public void setTaskDetailsAdapterOnClick(TaskDetailsAdapterOnClick taskDetailsAdapterOnClick){
        this.taskDetailsAdapterOnClick = taskDetailsAdapterOnClick;
    }

    public static class FreeTextVH extends RecyclerView.ViewHolder {
        public TextView title;
        public EditText freeText;
        public ImageButton imageButton;
        public MyCustomEditTextListener myCustomEditTextListener;
        public MyCustomOnClickListener myCustomOnClickListener;

        public FreeTextVH(View view,MyCustomEditTextListener myCustomEditTextListener,MyCustomOnClickListener myCustomOnClickListener) {
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            freeText = (EditText)view.findViewById(R.id.freeText);
            imageButton = (ImageButton)view.findViewById(R.id.searchBtn);
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.myCustomOnClickListener = myCustomOnClickListener;
            imageButton.setOnClickListener(myCustomOnClickListener);
            freeText.addTextChangedListener(myCustomEditTextListener);
        }
    }
    private class MyCustomOnClickListener implements View.OnClickListener{
        private int position;
        public void updatePosition(int position) {
            this.position = position;
        }


        @Override
        public void onClick(View v) {
            taskDetailsAdapterOnClick.searchButtonPressedForItemInPosition(position);
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            TaskDetailsItem item = getItem(position);
            item.setSelectedValue(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    public static class SpinnerVH extends RecyclerView.ViewHolder {
        public TextView title;
        public Spinner spinner;
        public SpinnerVH(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            spinner = (Spinner)view.findViewById(R.id.spinner);
        }
    }

    public static final int FREETEXT = 0;
    public static final int SPINNER = 1;
    public static final int REMOVEASSET = 2;
    protected ArrayList<TaskDetailsItem> taskTemplateParameterArrayList;

    private Realm realm;
    private Context context;
    private HashMap<Integer,Integer> selectedItems = new HashMap<>();

    public TaskDetailsAdapter(ArrayList<TaskDetailsItem> taskTemplateParameterArrayList,Realm realm,Context context) {
        this.taskTemplateParameterArrayList = taskTemplateParameterArrayList;
        this.realm = realm;
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {
        TaskTemplateParameter taskTemplateParameter = taskTemplateParameterArrayList.get(position).getTaskTemplateParameter();
        if (taskTemplateParameter.getParameterName().equals("RemoveAsset")){
            return REMOVEASSET;
        }
        else if (taskTemplateParameter.getParameterType().equals("Reference Data")){
            return SPINNER;
        }
        return FREETEXT;
    }

    @Override
    public int getItemCount() {
        return taskTemplateParameterArrayList.size();
    }

    public TaskDetailsItem getItem(int position){
        return taskTemplateParameterArrayList.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case SPINNER:
                View v1 = inflater.inflate(R.layout.task_details_spinner_row, viewGroup, false);
                viewHolder = new SpinnerVH(v1);
                break;
            case FREETEXT:
                View v2 = inflater.inflate(R.layout.task_details_text_row, viewGroup, false);
                viewHolder = new FreeTextVH(v2, new MyCustomEditTextListener(), new MyCustomOnClickListener());
                break;
            case REMOVEASSET:
                View v3 = inflater.inflate(R.layout.task_details_spinner_row_remove_asset, viewGroup, false);
                viewHolder = new SpinnerVH(v3);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SPINNER:
                SpinnerVH vh1 = (SpinnerVH) holder;
                configueSpinnerVH(vh1, position);
                break;
            case FREETEXT:
                FreeTextVH vh2 = (FreeTextVH) holder;
                configureFreeTextVH(vh2,position);
                break;
            case REMOVEASSET:
                SpinnerVH vh3 = (SpinnerVH) holder;
                configueSpinnerVH(vh3,position);
                break;
            default:
                break;
        }
    }



    protected void configueSpinnerVH(SpinnerVH spinnerVH,final int position){
        final TaskDetailsItem item = taskTemplateParameterArrayList.get(position);
        spinnerVH.spinner.setEnabled(item.isVisible());
        final TaskTemplateParameter taskTemplateParameter = item.getTaskTemplateParameter();
        String title = taskTemplateParameter.getParameterDisplay();
        if (Utils.isDebugEnabled){
            title = item.getTaskTemplateParameter().getOrdinal()+"."+taskTemplateParameter.getParameterDisplay()+" -"+item.isVisible();
        }
        spinnerVH.title.setText(title);
        spinnerVH.spinner.setAdapter(getSpinnerAdapter(taskTemplateParameter));

        if (Utils.checkNotNull(selectedItems.get(position))){
            spinnerVH.spinner.setSelection(selectedItems.get(position));
        }

        if (item.isValidated()){
            spinnerVH.title.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.white,null));
        }else{
            spinnerVH.title.setTextColor(Color.RED);
        }

        spinnerVH.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (Utils.checkNotNull(item.depedencies)) {
                    String selectedAnswer = ((SpinnerObject) parent.getSelectedItem()).getName();
                    item.setSelectedValue(selectedAnswer);
                    selectedItems.put(position,pos);
                    if (item.depedencies.size() > 0) {
                        for(TaskDetailsItem answerTaskDetailItem : item.depedencies){
                            String acceptedAnswer = answerTaskDetailItem.getTaskTemplateParameter().getPredecessorTrueValue();
                            if (selectedAnswer.equals(acceptedAnswer)
                                    && !selectedAnswer.equals(PLEASE_SELECT)) {
                               showChildren(answerTaskDetailItem);
                            }else{
                                hideChildren(answerTaskDetailItem);
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void configureFreeTextVH(final FreeTextVH freeTextVH, int position){
        freeTextVH.myCustomEditTextListener.updatePosition(position);
        freeTextVH.myCustomOnClickListener.updatePosition(position);
        final TaskDetailsItem item = taskTemplateParameterArrayList.get(position);
        TaskTemplateParameter taskTemplateParameter = item.getTaskTemplateParameter();

        if (!item.isVisible()){
            freeTextVH.freeText.setHint(item.getTaskTemplateParameter().getReferenceDataType());
        }else{
            freeTextVH.freeText.setHint("");
        }
        if (item.getSelectedValue() != null){
                freeTextVH.freeText.setText(item.getSelectedValue());
        }else{
            freeTextVH.freeText.setText("");
        }
        if (item.isValidated()){
            freeTextVH.title.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.white,null));
        }else{
            freeTextVH.title.setTextColor(Color.RED);
        }
        int imageButtonVisibility =  item.getTaskTemplateParameter().getOrdinal() == 1 ? View.VISIBLE : View.GONE;
        freeTextVH.imageButton.setVisibility(imageButtonVisibility);

        freeTextVH.freeText.setEnabled(item.isVisible());

        String title = taskTemplateParameter.getParameterDisplay();
        if (Utils.isDebugEnabled){
            title = item.getTaskTemplateParameter().getOrdinal()+"."+taskTemplateParameter.getParameterDisplay()+" -"+item.isVisible();
        }
        freeTextVH.title.setText(title);
        if(taskTemplateParameter.getParameterName().startsWith("Temperature") && !taskTemplateParameter.getParameterName().endsWith("Set") && Utils.isProbeConnected){
            freeTextVH.freeText.setInputType(InputType.TYPE_NULL);
        } else
        if (taskTemplateParameter.getParameterType().equals("Number")){
            freeTextVH.freeText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        }else{
            freeTextVH.freeText.setInputType(InputType.TYPE_CLASS_TEXT);
        }

    }

    protected void showChildren(TaskDetailsItem item){
        int index = item.getTaskTemplateParameter().getOrdinal() - 2;
        if (!item.isVisible()) {
            item.setVisible(true);
            notifyItemChanged(index);
        }

    }
    protected void hideChildren(TaskDetailsItem item){
        if (item.isVisible()) {
            int index = taskTemplateParameterArrayList.indexOf(item);
            item.setVisible(false);
            notifyItemChanged(index);
        }
    }



    private SpinnerArrayAdapter getSpinnerAdapter(TaskTemplateParameter taskTemplateParameter ){
        ArrayList<SpinnerObject> values = new ArrayList<>();
        values.add(new SpinnerObject(null,PLEASE_SELECT));
        String[] references = {taskTemplateParameter.getReferenceDataType(),taskTemplateParameter.getReferenceDataExtendedType()};
        for (String referenceType : references){
            if (Utils.checkNotNull(referenceType)){
                RealmQuery<ReferenceData> query = realm.where(ReferenceData.class);
                query.equalTo("type",referenceType);
                query.equalTo("isDeleted",false);
                RealmResults<ReferenceData> results = query.findAll();
                results = results.sort("ordinal");
                for (ReferenceData result : results){
                    values.add(new SpinnerObject(null,result.getDisplay()));
                }

            }
        }
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(context, android.R.layout.simple_spinner_item, values);
        return adapter;


    }
}
