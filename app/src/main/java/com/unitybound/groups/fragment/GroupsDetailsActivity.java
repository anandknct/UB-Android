package com.unitybound.groups.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.groups.adapter.GroupMembersGridAdapter;
import com.unitybound.groups.adapter.GroupsDetailsFeedsAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.utility.GridSpacingItemDecoration;

import java.util.ArrayList;

import butterknife.OnClick;

public class GroupsDetailsActivity extends AppCompatActivity implements
        GroupsDetailsFeedsAdapter.IListAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    private TextView tv_group_description, tv_member_label, tv_comment_label = null, tv_photos_label, tv_about_label;
    private ImageView tv_blocked_label;
    private RecyclerView recyclerView = null;
//    private SwipeRefreshLayout swipeRefresh = null;
    private int mSelectedTab = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_detail_activity);
        setUpToolbar();
        initView();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Group Details");
//        TextView tvTittle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        tvTittle.setText("Sign Up");
//        toolbar.setNavigationIcon(R.drawable.ic_back);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_group_description = (TextView) findViewById(R.id.tv_group_description);
        tv_member_label = (TextView) findViewById(R.id.tv_member_label);
        tv_comment_label = (TextView) findViewById(R.id.tv_comment_label);
        tv_photos_label = (TextView) findViewById(R.id.tv_photos_label);
        tv_about_label = (TextView) findViewById(R.id.tv_about_label);
        tv_blocked_label = (ImageView) findViewById(R.id.tv_blocked_label);
        setUpBottomListener();

//        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
//        swipeRefresh.setOnRefreshListener(GroupsDetailsActivity.this);

        setUpCommentsList();
    }

    private void setUpBottomListener() {
        tv_comment_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab!=0) {
                    tv_member_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_comment_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.color_white));
                    tv_photos_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_about_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tv_blocked_label.setBackground(getDrawable(R.mipmap.ic_disable));
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_group_description.setVisibility(View.GONE);
                    mSelectedTab = 0;
                    setUpCommentsList();
                }
            }
        });

        tv_member_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab!=1) {
                    tv_comment_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_member_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.color_white));
                    tv_photos_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_about_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tv_blocked_label.setBackground(getDrawable(R.mipmap.ic_disable));
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_group_description.setVisibility(View.GONE);
                    mSelectedTab = 1;
                    setUpGroupMembersGrid();
                }
            }
        });

        tv_photos_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab!=2) {
                    tv_comment_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_photos_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.color_white));
                    tv_member_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_about_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tv_blocked_label.setBackground(getDrawable(R.mipmap.ic_disable));
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_group_description.setVisibility(View.GONE);
                    mSelectedTab = 2;
                    setUpGroupPhotosGrid();
                }
            }
        });

        tv_about_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab!=3) {
                    tv_comment_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_about_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.color_white));
                    tv_member_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_photos_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tv_blocked_label.setBackground(getDrawable(R.mipmap.ic_disable));
                    }
                    recyclerView.setVisibility(View.GONE);
                    tv_group_description.setVisibility(View.VISIBLE);
                    mSelectedTab = 3;
                }
            }
        });

        tv_blocked_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab!=4) {
                    tv_comment_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_about_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_member_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    tv_photos_label.setTextColor(ContextCompat.getColor(GroupsDetailsActivity.this, R.color.unselected_text_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tv_blocked_label.setBackground(getDrawable(R.drawable.ic_disable_white));
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_group_description.setVisibility(View.GONE);
                    mSelectedTab = 4;
                    setUpGroupBlockedUsersList();
                }
            }
        });
    }

    private void setUpCommentsList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(GroupsDetailsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        GroupsDetailsFeedsAdapter adapter = new GroupsDetailsFeedsAdapter(
                GroupsDetailsActivity.this, arrayList,
                GroupsDetailsActivity.this);
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

    private void setUpGroupPhotosGrid() {
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
        GroupPhotosGridAdapter adapter = new GroupPhotosGridAdapter(this, datalist);

        GridLayoutManager lLayout = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, spacingInPixels, true, 0));
        recyclerView.setAdapter(adapter);
    }

    private void setUpGroupBlockedUsersList() {
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

        ArrayList<FriendRequestData> datalist = new ArrayList<FriendRequestData>();
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

        GroupBlockUsersListAdapter adapter = new GroupBlockUsersListAdapter(this, datalist);
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onCommentClickListner(String s, int position) {

    }

    @Override
    public void onRefresh() {

    }

    @OnClick(R.id.btn_join)
    public void onViewClicked() {
    }

}
