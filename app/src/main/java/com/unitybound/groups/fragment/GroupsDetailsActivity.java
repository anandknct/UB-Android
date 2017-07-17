package com.unitybound.groups.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.events.fragment.adapter.EventsListAdapter;
import com.unitybound.groups.adapter.GroupsDetailsFeedsAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GroupsDetailsActivity extends Activity implements
        GroupsDetailsFeedsAdapter.IListAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.iv_event_image)
    ImageView ivEventImage;
    @BindView(R.id.btn_photos)
    TextView btnPhotos;
    @BindView(R.id.btn_join)
    TextView btnJoin;
    @BindView(R.id.rr_top)
    RelativeLayout rrTop;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.activity_emergency)
    RelativeLayout activityEmergency;

    ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    EventsListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.fragment_groups_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(GroupsDetailsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh.setOnRefreshListener(GroupsDetailsActivity.this);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        GroupsDetailsFeedsAdapter adapter = new GroupsDetailsFeedsAdapter(GroupsDetailsActivity.this, arrayList,
                GroupsDetailsActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeRefresh.setRefreshing(false);
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

}
