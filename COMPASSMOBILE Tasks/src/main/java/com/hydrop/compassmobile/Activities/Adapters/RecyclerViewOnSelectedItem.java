package com.hydrop.compassmobile.Activities.Adapters;

import android.view.View;

/**
 * Created by Panos on 20/05/16.
 */
 public interface RecyclerViewOnSelectedItem<T>{
    void selectedItem(T item,int position,View view);

}
