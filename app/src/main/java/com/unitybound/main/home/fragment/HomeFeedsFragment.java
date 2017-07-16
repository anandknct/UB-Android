package com.unitybound.main.home.fragment;

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

import com.unitybound.R;
import com.unitybound.main.home.fragment.adapter.HomeFeedsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.unitybound.R.id.swipe_refresh;

/**
 * Live feed Fragment
 */
public class HomeFeedsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        HomeFeedsAdapter.IListAdapterCallback{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_feeds, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }


    /**
     * @param view the view
     */
    private void initViews(View view) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh.setOnRefreshListener(HomeFeedsFragment.this);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        HomeFeedsAdapter adapter = new HomeFeedsAdapter(getActivity(), arrayList,
                HomeFeedsFragment.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeRefresh.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onItemClickListner(String s, int position) {

    }

    @Override
    public void onCommentClickListner(String s, int position) {

    }

}
