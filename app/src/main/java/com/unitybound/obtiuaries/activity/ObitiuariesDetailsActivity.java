package com.unitybound.obtiuaries.fragment.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.unitybound.R;
import com.unitybound.events.fragment.adapter.EventsListAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;

import java.util.ArrayList;


public class ObitiuariesDetailsActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obitiuaries_details);
        initView();
    }

    RecyclerView recyclerView;
    ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    EventsListAdapter adapter;

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Obituaries Details");
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
}
