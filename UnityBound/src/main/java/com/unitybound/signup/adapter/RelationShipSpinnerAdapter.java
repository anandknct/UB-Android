package com.unitybound.signup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unitybound.R;

import java.util.ArrayList;

/**
 * Created by Admin on 7/25/2017.
 */
public class RelationShipSpinnerAdapter extends BaseAdapter {

    private Context activity;
    ArrayList<String> arrayList = null;
    LayoutInflater inflater;

    public RelationShipSpinnerAdapter(
            Context context,
            ArrayList<String> arrayList) {
        activity = context;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.relationship_spinner_item, parent, false);

        TextView tv_item_name = (TextView) row.findViewById(R.id.tv_item_name);

        tv_item_name.setText(arrayList.get(position));
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View dropDownView = inflater.inflate(R.layout.relationship_dropdown_row, parent, false);

        TextView tv_item_name = (TextView) dropDownView.findViewById(R.id.tv_item_name);
        if (tv_item_name != null && arrayList != null) {
            tv_item_name.setText(arrayList.get(position).toString());
        }

        return dropDownView;

    }
}