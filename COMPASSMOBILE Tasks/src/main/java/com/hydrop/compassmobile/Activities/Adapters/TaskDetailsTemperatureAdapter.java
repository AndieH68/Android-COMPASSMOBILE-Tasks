package com.hydrop.compassmobile.Activities.Adapters;

import android.content.Context;
import android.view.View;

import com.hydrop.compassmobile.Activities.HelperClasses.TaskDetailsItem;
import com.hydrop.compassmobile.RealmObjects.TaskTemplateParameter;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Panos on 24/05/16.
 */
public class TaskDetailsTemperatureAdapter extends TaskDetailsAdapter {
    private String hotValue, coldValue;
    private int focusedIndex;

    public void resetFocus(){
        focusedIndex = -1;
    }

    public void update(String value){
        for (int i=0;i<getItemCount();i++){
            TaskDetailsItem item = getItem(i);
            TaskTemplateParameter taskTemplateParameter = item.getTaskTemplateParameter();
            if (taskTemplateParameter.getParameterName().startsWith("Temperature") && !taskTemplateParameter.getParameterName().endsWith("Set")){
                if (i == focusedIndex){
                    item.setSelectedValue(value);
                    notifyItemChanged(i);

                    return;
                }
            }
            
        }
    }

    public TaskDetailsTemperatureAdapter(ArrayList<TaskDetailsItem> taskTemplateParameterArrayList, Realm realm, Context context, String hotValue, String coldValue) {
        super(taskTemplateParameterArrayList, realm, context);
        this.hotValue = hotValue;
        this.coldValue = coldValue;
    }

    @Override
    protected void configureFreeTextVH(FreeTextVH freeTextVH, final int position) {
        super.configureFreeTextVH(freeTextVH,position);

        TaskDetailsItem taskDetailsItem = taskTemplateParameterArrayList.get(position);
        TaskTemplateParameter taskTemplateParameter= taskDetailsItem.getTaskTemplateParameter();
        String paramName = taskTemplateParameter.getParameterName();

        if (paramName.startsWith("Temperature")  && !paramName.endsWith("Set")) {

            freeTextVH.freeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    //focusedIndex = (hasFocus) ? position : -1;
                    focusedIndex = position;
                }
            });
        }
    }

    @Override
    protected void showChildren(TaskDetailsItem item){
        String paramName = item.getTaskTemplateParameter().getParameterName();
        if (paramName.equals("TemperatureHot")) {
            if(hotValue.equals("None")){
                return;
            }
        }
        if (paramName.equals("TemperatureCold")) {
            if(coldValue.equals("None")){
                return;
            }
        }
        super.showChildren(item);
    }
}
