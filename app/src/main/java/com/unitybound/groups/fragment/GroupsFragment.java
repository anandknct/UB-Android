package com.unitybound.groups.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unitybound.R;
import com.unitybound.groups.activity.AddGroupActivity;
import com.unitybound.groups.adapter.GroupsListAdapter;
import com.unitybound.main.MainActivity;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.utility.SpacesItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charchitkasliwal on 10/05/17.
 */

public class GroupsFragment extends Fragment implements GroupsListAdapter.IListAdapterCallback {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    RecyclerView recyclerView;
    ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    GroupsListAdapter adapter;
    @BindView(R.id.fab_create_group)
    FloatingActionButton fabCreateGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_groups,
                container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }


    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_layout);

        //add some person to list
        FriendRequestData p1 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p2 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p3 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p4 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p5 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p6 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p7 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p8 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p9 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p10 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p11 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p12 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p13 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p14 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p15 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");


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

        adapter = new GroupsListAdapter(getActivity(), datalist, GroupsFragment.this);
        LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClickListner(String s, int position) {
//        Fragment fragment = new GroupsDetailsActivity();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                android.R.anim.fade_out);
//        fragmentTransaction.replace(R.id.frame, fragment, fragment.getClass().getName());
//        fragmentTransaction.addToBackStack(fragment.getClass().getName());
//        fragmentTransaction.commitAllowingStateLoss();
        Intent i = new Intent(getActivity(), GroupsDetailsActivity.class);
        getActivity().startActivity(i);
    }

    @OnClick(R.id.fab_create_group)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), AddGroupActivity.class);
        startActivity(intent);
    }

}
