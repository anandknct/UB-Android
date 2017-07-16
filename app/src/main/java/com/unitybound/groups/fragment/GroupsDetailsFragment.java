package com.unitybound.groups.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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


public class GroupsDetailsFragment extends Fragment implements GroupsDetailsFeedsAdapter.IListAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    EventsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_detail, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh.setOnRefreshListener(GroupsDetailsFragment.this);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        GroupsDetailsFeedsAdapter adapter = new GroupsDetailsFeedsAdapter(getActivity(), arrayList,
                GroupsDetailsFragment.this);
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
