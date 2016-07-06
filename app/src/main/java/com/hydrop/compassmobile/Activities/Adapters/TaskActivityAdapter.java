package com.hydrop.compassmobile.Activities.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hydrop.compassmobile.Activities.HelperClasses.FilterHelper;
import com.hydrop.compassmobile.R;
import com.hydrop.compassmobile.RealmObjects.Task;
import com.hydrop.compassmobile.Utils;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Panos on 12/05/16.
 */
public class TaskActivityAdapter extends RecyclerView.Adapter<TaskActivityAdapter.MyViewHolder>{

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView taskRef, taskName, location,type,asset,dateDue;

        public MyViewHolder(View view) {
            super(view);
            taskRef = (TextView)view.findViewById(R.id.taskRefValue);
            taskName = (TextView)view.findViewById(R.id.taskNameValue);
            location =  (TextView)view.findViewById(R.id.locationValue);
            type = (TextView)view.findViewById(R.id.typeValue);
            asset = (TextView)view.findViewById(R.id.assetValue);
            dateDue = (TextView)view.findViewById(R.id.dateValue);
        }
    }


    private RealmResults<Task> taskList;
    private Realm realm;
    private boolean extraPropertyInfoEnabled;
    public RecyclerViewOnSelectedItem<Task> mRecyclerViewOnSelectedItemListener;

    public TaskActivityAdapter( RealmResults<Task> taskList,Realm realm,boolean extraPropertyInfoEnabled,RecyclerViewOnSelectedItem<Task> mRecyclerViewOnSelectedItemListener){
        this.taskList = taskList;
        this.realm = realm;
        this.extraPropertyInfoEnabled = extraPropertyInfoEnabled;
        this.mRecyclerViewOnSelectedItemListener = mRecyclerViewOnSelectedItemListener;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_cell_row,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Task task = taskList.get(position);
        String taskFullName = FilterHelper.getTaskFullNameForKey(realm,task.getTaskName(),"PPMTaskType","PPMAssetGroup",task.getPpmGroup());
        String assetGroupName = FilterHelper.getAssetGroupNameForKey(realm,task.getPpmGroup(),"PPMAssetGroup");
        String assetTypeName = FilterHelper.getAssetTypeNameForKey(realm,task.getAssetType());
        holder.taskRef.setText(task.getTaskRef());
        holder.taskName.setText(taskFullName);
        if (extraPropertyInfoEnabled){
            holder.location.setText(FilterHelper.getPropertyNameForKey(realm,task.getPropertyId())+","+task.getLocationName());
        }else{
            holder.location.setText(task.getLocationName());

        }
        holder.type.setText(assetGroupName);
        holder.asset.setText(assetTypeName);
        holder.dateDue.setText(Utils.dateToString(task.getScheduledDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mRecyclerViewOnSelectedItemListener.selectedItem(task,position,null);
            }
        });

    }
}
