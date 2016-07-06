package com.hydrop.compassmobile.Activities.HelperClasses;

import com.hydrop.compassmobile.RealmObjects.TaskTemplateParameter;

import java.util.ArrayList;

/**
 * Created by Panos on 24/05/16.
 */
public class TaskDetailsItem {

    private TaskTemplateParameter taskTemplateParameter;
    private boolean visible;
    public ArrayList<TaskDetailsItem> depedencies;
    private String selectedValue;
    private boolean isValidated;


    public TaskDetailsItem(TaskTemplateParameter taskTemplateParameter, boolean visible) {
        depedencies = new ArrayList<>();
        this.taskTemplateParameter = taskTemplateParameter;
        this.visible = visible;
        this.isValidated = true;
    }

    public ArrayList<TaskDetailsItem> getDepedencies() {
        return depedencies;
    }

    public void setDepedencies(ArrayList<TaskDetailsItem> depedencies) {
        this.depedencies = depedencies;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public TaskTemplateParameter getTaskTemplateParameter() {
        return taskTemplateParameter;
    }

    public void setTaskTemplateParameter(TaskTemplateParameter taskTemplateParameter) {
        this.taskTemplateParameter = taskTemplateParameter;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
