package com.unitybound.main.home.fragment.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.unitybound.R;
import com.unitybound.main.home.fragment.adapter.CommentsAdapter;
import com.unitybound.main.home.fragment.adapter.HomeFeedsAdapter;

import java.util.ArrayList;

public class FeedsCommentActivity extends AppCompatActivity
        implements HomeFeedsAdapter.IListAdapterCallback, SwipeRefreshLayout.OnRefreshListener,
        CommentsAdapter.IListAdapterCallback {

    RecyclerView rv_list_layout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_comment);
        initView();
        initViews();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Please pray for ");
    }

    private void initViews() {
        rv_list_layout = (RecyclerView) findViewById(R.id.rv_list_layout);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_list_layout.setLayoutManager(mLayoutManager);
        rv_list_layout.setItemAnimator(new DefaultItemAnimator());

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        CommentsAdapter adapter = new CommentsAdapter(this, arrayList,
                FeedsCommentActivity.this);
        rv_list_layout.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickListner(String s, int position) {

    }

    @Override
    public void onCommentClickListner(String s, int position) {

    }

    @Override
    public void onOptionClickListner(String s, int position) {

    }

    @Override
    public void onRefresh() {

    }
}
