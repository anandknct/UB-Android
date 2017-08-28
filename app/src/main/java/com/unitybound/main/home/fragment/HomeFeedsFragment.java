package com.unitybound.main.home.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.unitybound.R;
import com.unitybound.account.activity.AddPostActivity;
import com.unitybound.main.home.fragment.activity.FeedsCommentActivity;
import com.unitybound.main.home.fragment.adapter.HomeFeedsAdapter;
import com.unitybound.main.home.fragment.adapter.MyFeedsSpinnerAdapter;
import com.unitybound.main.interPhase.IUserClickFromFragmentListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Live feed Fragment
 */
public class HomeFeedsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        HomeFeedsAdapter.IListAdapterCallback {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.spinner_feeds_type)
    Spinner spFeedsType;
    @BindView(R.id.iv_everyone)
    ImageView ivEveryone;
    @BindView(R.id.iv_me)
    ImageView ivMe;
    @BindView(R.id.iv_favourite)
    ImageView ivFavourite;
    @BindView(R.id.ll_top_menu)
    LinearLayout llTopMenu;
    @BindView(R.id.fab_create_post)
    FloatingActionButton fabCreatePost;
    private BottomSheetDialog mBottomSheetDialog = null;
    private IUserClickFromFragmentListener userClickListener = null;

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

        /* Spinner Feeds Adapter*/
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("ALL Types");
        arrayList1.add("Devotional");
        arrayList1.add("Praise");
        arrayList1.add("Prayers");
        arrayList1.add("Testimonials");
        TypedArray imgs = getResources().obtainTypedArray(R.array.pinner_imgs);

        MyFeedsSpinnerAdapter spAdapter = new MyFeedsSpinnerAdapter(
                getContext(),
                R.layout.spinner_item,
                arrayList1, imgs);

//        spAdapter.setDropDownViewResource(R.layout.spinner_drop_down_bg_layout);
        spFeedsType.setAdapter(spAdapter);
        userClickListener = (IUserClickFromFragmentListener) getActivity();
        setUpBottomSheet();
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
        Intent intent = new Intent(getActivity(), FeedsCommentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onOptionClickListner(String s, int position) {
        mBottomSheetDialog.findViewById(R.id.fragment_history_menu_bottom).setVisibility(View.VISIBLE);
        mBottomSheetDialog.show();
    }

    @Override
    public void onUserNameClickListner(String s, int position) {
//        Intent intent = new Intent(getActivity(), MyAccountAboutActivity.class);
//        startActivity(intent);
        if (userClickListener != null) {
            userClickListener.onUserClickListener(s, position);
        }
    }

    private void setUpBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheets_main_layout, null);
        mBottomSheetDialog.setContentView(sheetView);

        LinearLayout edit = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
        LinearLayout delete = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete code here;
            }
        });
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Do something
            }
        });
    }

    @OnClick(R.id.fab_create_post)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), AddPostActivity.class);
        startActivity(intent);
    }
}
