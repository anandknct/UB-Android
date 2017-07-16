package com.unitybound.church.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.unitybound.R;
import com.unitybound.church.adapter.ChurchListAdapter;
import com.unitybound.church.adapter.ChurchMembersGridAdapter;
import com.unitybound.events.fragment.adapter.EventsListAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.utility.GridSpacingItemDecoration;

import java.util.ArrayList;


public class ChurchDetailsFragment extends Fragment implements
        ChurchListAdapter.IListAdapterCallback, EventsListAdapter.IListAdapterCallback {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    ChurchMembersGridAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_church_detail, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView rv_grid_layout = (RecyclerView) view.findViewById(R.id.rv_grid_layout);

        //add some person to list
        FriendRequestData p1 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p2 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p3 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p4 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p5 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p6 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p7 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p8 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");

        datalist.add(p1);
        datalist.add(p2);
        datalist.add(p3);
        datalist.add(p4);
        datalist.add(p5);
        datalist.add(p6);
        datalist.add(p7);
        datalist.add(p8);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_five);
        adapter = new ChurchMembersGridAdapter(getActivity(), datalist);

        GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 4);
        rv_grid_layout.setLayoutManager(lLayout);
        rv_grid_layout.setHasFixedSize(true);
        rv_grid_layout.setNestedScrollingEnabled(false);
        rv_grid_layout.addItemDecoration(new GridSpacingItemDecoration(4, spacingInPixels, true, 0));
        rv_grid_layout.setAdapter(adapter);
    }

    @Override
    public void onItemClickListner(String s, int position) {

    }
}
