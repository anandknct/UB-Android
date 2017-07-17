package com.unitybound.church.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.unitybound.R;
import com.unitybound.church.adapter.ChurchListAdapter;
import com.unitybound.church.adapter.ChurchMembersGridAdapter;
import com.unitybound.events.fragment.adapter.EventsListAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.utility.GridSpacingItemDecoration;

import java.util.ArrayList;


public class ChurchDetailsActivity extends AppCompatActivity implements
        ChurchListAdapter.IListAdapterCallback, EventsListAdapter.IListAdapterCallback {

    ArrayList<FriendRequestData> datalist = new ArrayList<FriendRequestData>();
    ChurchMembersGridAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_church_detail);

        initView();
        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Church Details");
    }

    private void initView() {
        RecyclerView rv_grid_layout = (RecyclerView) findViewById(R.id.rv_grid_layout);

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
        adapter = new ChurchMembersGridAdapter(ChurchDetailsActivity.this, datalist);

        GridLayoutManager lLayout = new GridLayoutManager(ChurchDetailsActivity.this, 4);
        rv_grid_layout.setLayoutManager(lLayout);
        rv_grid_layout.setHasFixedSize(true);
        rv_grid_layout.setNestedScrollingEnabled(false);
        rv_grid_layout.addItemDecoration(new GridSpacingItemDecoration(4, spacingInPixels, true, 0));
        rv_grid_layout.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClickListner(String s, int position) {

    }
}
