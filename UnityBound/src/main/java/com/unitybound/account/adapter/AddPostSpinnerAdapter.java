package com.unitybound.account.adapter;

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
public class AddPostSpinnerAdapter extends BaseAdapter {

    private final int textViewResourceId;
    private Context activity;
    ArrayList<String> arrayList = null;
    TypedArray arrayIcons = null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public AddPostSpinnerAdapter(
            Context context,
            int textViewResourceId,
            ArrayList<String> arrayList,
            TypedArray arrayIcons
    ) {

        /********** Take passed values **********/
        activity = context;
        this.arrayList = arrayList;
        this.arrayIcons = arrayIcons;
        this.textViewResourceId = textViewResourceId;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    private View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(textViewResourceId, parent, false);

        TextView tv_item_name = (TextView) row.findViewById(R.id.tv_item_name);
//        ImageView iv_spinner_icon = (ImageView) row.findViewById(R.id.iv_spinner_icon);

        tv_item_name.setText(arrayList.get(position));
//        iv_spinner_icon.setImageResource(arrayIcons.getResourceId(position, -1));
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View dropDownView = inflater.inflate(R.layout.spinner_drop_down_white_bg_layout, parent, false);

        TextView tv_item_name = (TextView) dropDownView.findViewById(R.id.tv_item_name);
        ImageView iv_spinner_icon = (ImageView) dropDownView.findViewById(R.id.iv_spinner_icon);
        iv_spinner_icon.setVisibility(View.GONE);
        if (tv_item_name != null && arrayList != null) {
            tv_item_name.setText(arrayList.get(position));
        }

        return dropDownView;

    }
}