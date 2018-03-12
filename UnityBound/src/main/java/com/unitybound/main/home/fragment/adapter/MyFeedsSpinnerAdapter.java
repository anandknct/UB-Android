package com.unitybound.main.home.fragment.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unitybound.R;

import java.util.ArrayList;

/**
 * Created by Admin on 7/25/2017.
 */
public class MyFeedsSpinnerAdapter extends BaseAdapter {

    private Context activity;
    ArrayList<String> arrayList = null;
    TypedArray arrayIcons = null;
    LayoutInflater inflater;

    public MyFeedsSpinnerAdapter(
            Context context,
            ArrayList<String> arrayList,
            TypedArray arrayIcons
    ) {
        activity = context;
        this.arrayList = arrayList;
        this.arrayIcons = arrayIcons;
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

        View row = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView tv_item_name = (TextView) row.findViewById(R.id.tv_item_name);
        ImageView iv_spinner_icon = (ImageView) row.findViewById(R.id.iv_spinner_icon);

        tv_item_name.setText(arrayList.get(position));
        iv_spinner_icon.setImageResource(arrayIcons.getResourceId(position, -1));
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View dropDownView = inflater.inflate(R.layout.spinner_drop_down_white_bg_layout, parent, false);

        TextView tv_item_name = (TextView) dropDownView.findViewById(R.id.tv_item_name);
        ImageView iv_spinner_icon = (ImageView) dropDownView.findViewById(R.id.iv_spinner_icon);
        if (tv_item_name != null && arrayList != null) {
            tv_item_name.setText(arrayList.get(position).toString());
            iv_spinner_icon.setImageResource(arrayIcons.getResourceId(position, -1));
        }

        return dropDownView;

    }
}