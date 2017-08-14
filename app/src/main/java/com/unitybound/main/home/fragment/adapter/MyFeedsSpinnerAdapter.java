package com.unitybound.main.home.fragment.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unitybound.R;

import java.util.ArrayList;

/**
 * Created by Admin on 7/25/2017.
 */
public class MyFeedsSpinnerAdapter extends ArrayAdapter<String> {

    private Context activity;
    ArrayList<String> arrayList = null;
    TypedArray arrayIcons = null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public MyFeedsSpinnerAdapter(
            Context context,
            int textViewResourceId,
            ArrayList<String> arrayList,
            TypedArray arrayIcons
    ) {
        super(context, textViewResourceId, arrayList);

        /********** Take passed values **********/
        activity = context;
        this.arrayList = arrayList;
        this.arrayIcons = arrayIcons;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    private View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView tv_item_name = (TextView) row.findViewById(R.id.tv_item_name);
//        ImageView iv_spinner_icon = (ImageView) row.findViewById(R.id.iv_spinner_icon);

        tv_item_name.setText(arrayList.get(position));
//        iv_spinner_icon.setImageResource(arrayIcons.getResourceId(position, -1));
        return row;
    }
}