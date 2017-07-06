package com.unitybound.main.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.unitybound.R;
import com.unitybound.main.model.SideMenu;

import java.util.ArrayList;

/**
 * Created by vaibhav.malviya on 21-09-2016.
 */

public class SliderCustomAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SideMenu> sideMenuArrayList;

    public SliderCustomAdapter(Context context, ArrayList<SideMenu> sideMenuArrayList) {
        this.mContext = context;
        this.sideMenuArrayList = sideMenuArrayList;
    }

    @Override
    public int getCount() {
        return sideMenuArrayList.size();
    }

    public void updateMenuList(ArrayList<SideMenu> newlist) {
        sideMenuArrayList.clear();
        sideMenuArrayList.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return sideMenuArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.nav_bar_item_row, null);
        } else {
            view = convertView;
        }
        TextView titleView = (TextView) view.findViewById(R.id.title);
        ImageView iconView = (ImageView) view.findViewById(R.id.menu_icon);
        LinearLayout parentLayout = (LinearLayout) view.findViewById(R.id.parent_layout);
        View topLine = view.findViewById(R.id.top_shadow_line);
//        View bottomLine=view.findViewById(R.id.bottom_divider_line);
        SideMenu sideMenu = sideMenuArrayList.get(position);

        titleView.setText(sideMenu.getName());
        iconView.setImageResource(sideMenu.getDrawable());
        if (sideMenu.isSelected()) {
            //arrowImageView.setVisibility(View.VISIBLE);
            parentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_ligh_blue));
          //  topLine.setVisibility(View.VISIBLE);
        } else {
          //  topLine.setVisibility(View.INVISIBLE);
           // arrowImageView.setVisibility(View.INVISIBLE);
           // parentLayout.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        }

        return view;
    }
}
