package com.unitybound.account.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.unitybound.R;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.main.home.fragment.adapter.HomeFeedsAdapter;
import com.unitybound.weddings.activity.WeddingsDetailsActivity;

import java.util.ArrayList;

/**
 * Created by charchitkasliwal on 10/05/17.
 */

public class MyAccountFragment   extends Fragment implements HomeFeedsAdapter.IListAdapterCallback {


    private RecyclerView recyclerView;
    private ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    private HomeFeedsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_accounts, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_layout);

        //add some person to list
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        adapter = new HomeFeedsAdapter(getActivity(), arrayList, this);
        LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClickListner(String s, int position) {
        Intent intent = new Intent(getActivity(), WeddingsDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCommentClickListner(String s, int position) {

    }
}

