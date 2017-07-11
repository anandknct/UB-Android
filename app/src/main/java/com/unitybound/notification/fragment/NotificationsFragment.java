package com.unitybound.notification.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.unitybound.R;
import com.unitybound.notification.adapter.NotificationListAdapter;
import com.unitybound.notification.model.NotificationData;

import java.util.ArrayList;

/**
 * Created by charchitkasliwal on 10/05/17.
 */

public class NotificationsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    RecyclerView recyclerView;
    ArrayList<NotificationData> datalist =
            new ArrayList<NotificationData>();
    NotificationListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_layout);

        //add some person to list
        NotificationData p1 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p2 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p3 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p4 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p5 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p6 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p7 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p8 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p9 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p10 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p11 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p12 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p13 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p14 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");
        NotificationData p15 = new NotificationData("EmergencyFragment Call Option 1", "7389875222");


        datalist.add(p1);
        datalist.add(p2);
        datalist.add(p3);
        datalist.add(p4);
        datalist.add(p5);
        datalist.add(p6);
        datalist.add(p7);
        datalist.add(p8);
        datalist.add(p9);
        datalist.add(p10);
        datalist.add(p11);
        datalist.add(p12);
        datalist.add(p13);
        datalist.add(p14);
        datalist.add(p15);

        adapter = new NotificationListAdapter(getActivity(), datalist);
        LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}