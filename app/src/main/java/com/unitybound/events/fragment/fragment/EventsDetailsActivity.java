package com.unitybound.events.fragment.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.groups.adapter.GroupMembersGridAdapter;
import com.unitybound.groups.adapter.GroupsDetailsFeedsAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.main.home.fragment.adapter.MyFeedsSpinnerAdapter;
import com.unitybound.utility.GridSpacingItemDecoration;

import java.util.ArrayList;

import static com.unitybound.R.id.rv_grid_layout;

public class EventsDetailsActivity extends AppCompatActivity implements GroupsDetailsFeedsAdapter.IListAdapterCallback {

    private TextView tv_discussion = null;
    private TextView tv_about_label = null, tv_member_label = null;
    private  RecyclerView recyclerView;
    private int mSelectedTab = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_events_detail);
        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Event Details");

        recyclerView = (RecyclerView) findViewById(rv_grid_layout);
        final TextView tv_event_description = (TextView) findViewById(R.id.tv_event_description);
        tv_discussion = (TextView) findViewById(R.id.tv_discussion);
        final RelativeLayout rr_desc = (RelativeLayout) findViewById(R.id.rr_desc);
        setUpCommentsList();
        tv_discussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab!=0) {
                    mSelectedTab = 0;
                    tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
                    tv_discussion.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.color_white));
                    tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_event_description.setVisibility(View.GONE);
                    rr_desc.setVisibility(View.GONE);
                    setUpCommentsList();
                }
            }
        });
        tv_about_label = (TextView) findViewById(R.id.tv_about_label);
        tv_about_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab!=1) {
                    mSelectedTab = 1;
                    tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
                    tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.color_white));
                    tv_discussion.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));

                    recyclerView.setVisibility(View.GONE);
                    tv_event_description.setVisibility(View.VISIBLE);
                    rr_desc.setVisibility(View.VISIBLE);
                }
            }
        });
        tv_member_label = (TextView) findViewById(R.id.tv_member_label);
        tv_member_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab!=2) {
                    mSelectedTab = 2;
                    tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.color_white));
                    tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
                    tv_discussion.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_event_description.setVisibility(View.GONE);
                    rr_desc.setVisibility(View.GONE);
                    setUpGroupMembersGrid();
                }
            }
        });


        /**
         * SetUp Spinner
         */
        Spinner spUserInput = (Spinner) findViewById(R.id.sp_user_input);
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Interested");
        arrayList1.add("Going");
        TypedArray imgs = getResources().obtainTypedArray(R.array.pinner_imgs);

        MyFeedsSpinnerAdapter spAdapter = new MyFeedsSpinnerAdapter(
                this,
                R.layout.spinner_item,
                arrayList1, imgs);

//        spAdapter.setDropDownViewResource(R.layout.spinner_drop_down_bg_layout);
        spUserInput.setAdapter(spAdapter);
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

    private void setUpCommentsList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(EventsDetailsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        GroupsDetailsFeedsAdapter adapter = new GroupsDetailsFeedsAdapter(
                EventsDetailsActivity.this, arrayList,
                EventsDetailsActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
//        swipeRefresh.setRefreshing(false);
    }

    private void setUpGroupMembersGrid() {
        ArrayList<FriendRequestData> datalist = new ArrayList<FriendRequestData>();
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
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_two);
        GroupMembersGridAdapter adapter = new GroupMembersGridAdapter(this, datalist);

        GridLayoutManager lLayout = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, spacingInPixels, true, 0));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClickListner(String s, int position) {

    }

    @Override
    public void onCommentClickListener(String s, int position) {

    }
}
