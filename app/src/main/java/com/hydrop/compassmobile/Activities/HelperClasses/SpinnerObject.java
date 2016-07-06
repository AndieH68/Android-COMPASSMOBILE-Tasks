package com.hydrop.compassmobile.Activities.HelperClasses;

/**
 * Created by Panos on 12/05/16.
 */
public class SpinnerObject{
    private String id;
    private String name;
    private String extraRowId;

    public SpinnerObject(String id,String name){
        this.id = id;
        this.name = name;
    }

    public String getExtraRowId() {
        return extraRowId;
    }

    public void setExtraRowId(String extraRowId) {
        this.extraRowId = extraRowId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
