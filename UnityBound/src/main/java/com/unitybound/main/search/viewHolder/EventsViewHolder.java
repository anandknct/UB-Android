package com.unitybound.main.search.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.main.search.adapter.SearchListAdapter;

/**
 * Created by Admin on 1/19/2018.
 */

public class EventsViewHolder extends RecyclerView.ViewHolder {
    public View rr_top = null;
    public TextView name, phone, tv_my_label;
    public ImageView thumbnail;

    public EventsViewHolder(View view, SearchListAdapter.SearchRowItemListener listener) {
        super(view);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        thumbnail = view.findViewById(R.id.thumbnail);
        tv_my_label = view.findViewById(R.id.tv_my_label);
        rr_top = view.findViewById(R.id.rr_top);
    }
}