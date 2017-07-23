package com.unitybound.account.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.main.friendrequest.adapter.FriendRequestListAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyAccountFrndsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.rv_friends)
    RecyclerView rvFriends;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_layout);
        ButterKnife.bind(this);
        initView();
    }

    RecyclerView recyclerView;
    ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    FriendRequestListAdapter adapter;



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.str_my_friends));
        recyclerView = (RecyclerView) findViewById(R.id.rv_friends);

        //Add some person to list
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

        adapter = new FriendRequestListAdapter(this, datalist);
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

}
