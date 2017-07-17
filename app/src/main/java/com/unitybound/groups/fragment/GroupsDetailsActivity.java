package com.unitybound.groups.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.unitybound.R;
import com.unitybound.groups.adapter.GroupsDetailsFeedsAdapter;
import com.unitybound.main.friendrequest.model.FriendRequestData;

import java.util.ArrayList;

import butterknife.OnClick;


public class GroupsDetailsActivity extends AppCompatActivity implements
        GroupsDetailsFeedsAdapter.IListAdapterCallback, SwipeRefreshLayout.OnRefreshListener {


    ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_detail_activity);

        initView();
    }

    private void initView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(GroupsDetailsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh.setOnRefreshListener(GroupsDetailsActivity.this);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        GroupsDetailsFeedsAdapter adapter = new GroupsDetailsFeedsAdapter(
                GroupsDetailsActivity.this, arrayList,
                GroupsDetailsActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeRefresh.setRefreshing(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Events Details");
//        TextView tvTittle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        tvTittle.setText("Sign Up");
//        toolbar.setNavigationIcon(R.drawable.ic_back);
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
