package com.unitybound.events.fragment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.events.fragment.adapter.EventsListAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;

import java.util.ArrayList;


public class EventsDetailsActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_events_detail);
        initView();
    }

    RecyclerView recyclerView = null;
    ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    EventsListAdapter adapter = null;
    private TextView tv_about_label = null,tv_member_label = null;

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Group Details");

        final RecyclerView rv_grid_layout = (RecyclerView) findViewById(R.id.rv_grid_layout);
        final TextView tv_event_description = (TextView) findViewById(R.id.tv_event_description);
        tv_about_label = (TextView) findViewById(R.id.tv_about_label);
        tv_about_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this,R.color.unselected_text_color));
                tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this,R.color.color_white));

                rv_grid_layout.setVisibility(View.GONE);
                tv_event_description.setVisibility(View.VISIBLE);
            }
        });
        tv_member_label = (TextView) findViewById(R.id.tv_member_label);
        tv_member_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this,R.color.color_white));
                tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this,R.color.unselected_text_color));

                rv_grid_layout.setVisibility(View.VISIBLE);
                tv_event_description.setVisibility(View.GONE);
            }
        });
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
