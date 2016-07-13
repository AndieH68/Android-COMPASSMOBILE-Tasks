package com.hydrop.compassmobile.WebAPI.Responses.SendTaskWrapper;

import java.util.ArrayList;

/**
 * Created by Panos on 31/05/16.
 */
public class SynchonisationPackage {

    private ArrayList<NewTaskDetails> tasks;

    public ArrayList<NewTaskDetails> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<NewTaskDetails> tasks) {
        this.tasks = tasks;
    }
}
