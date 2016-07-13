package com.hydrop.compassmobile.Activities.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hydrop.compassmobile.Activities.HelperClasses.SpinnerObject;

import java.util.ArrayList;

/**
 * Created by Panos on 12/05/16.
 */
public class SpinnerArrayAdapter extends ArrayAdapter<SpinnerObject>{



    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<SpinnerObject> values;

    public SpinnerArrayAdapter(Context context, int textViewResourceId, ArrayList<SpinnerObject> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
        return values.size();
    }

    public SpinnerObject getItem(int position){
        return values.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setGravity(Gravity.RIGHT);
        int padding = 25;
        label.setPadding(padding,padding,padding,padding);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        int padding = 25;
        label.setPadding(padding,padding,padding,padding);

        label.setText(values.get(position).getName());

        return label;
    }




}
