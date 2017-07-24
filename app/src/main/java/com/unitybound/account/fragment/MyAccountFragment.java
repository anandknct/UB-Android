package com.unitybound.account.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.account.activity.MyAccountAboutActivity;
import com.unitybound.account.activity.MyAccountFrndsActivity;
import com.unitybound.account.activity.MyAccountMyPhotosActivity;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.main.home.fragment.adapter.HomeFeedsAdapter;
import com.unitybound.weddings.activity.WeddingsDetailsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by @author nikhil.jogdand on 10/05/17.
 */

public class MyAccountFragment extends Fragment implements HomeFeedsAdapter.IListAdapterCallback {


    @BindView(R.id.rr_user_image)
    ImageView rrUserImage;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.divider1)
    View divider1;
    @BindView(R.id.tv_timeline)
    TextView tvTimeline;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.tv_friends)
    TextView tvFriends;
    @BindView(R.id.tv_photos)
    TextView tvPhotos;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.ll_account_menu)
    LinearLayout llAccountMenu;
    @BindView(R.id.divider2)
    View divider2;
    @BindView(R.id.ic_cover)
    ImageView icCover;
    @BindView(R.id.rr_cover)
    CardView rrCover;
    @BindView(R.id.rr_top)
    LinearLayout rrTop;
    @BindView(R.id.divider)
    View divider;
    RecyclerView recyclerView;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    Unbinder unbinder;
    //    private RecyclerView recyclerView;
    private ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    private HomeFeedsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_accounts, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView(view);
        unbinder = ButterKnife.bind(this, view);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rr_user_image, R.id.tv_user_name, R.id.tv_timeline, R.id.tv_about, R.id.tv_friends, R.id.tv_photos, R.id.iv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rr_user_image:
                break;
            case R.id.tv_user_name:
                break;
            case R.id.tv_timeline:
//                Intent intentAbout = new Intent(getActivity(), MyAccountAboutActivity.class);
//                startActivity(intentAbout);
                break;
            case R.id.tv_about:
                Intent intentAbout = new Intent(getActivity(), MyAccountAboutActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.tv_friends:
                Intent intentFriends = new Intent(getActivity(), MyAccountFrndsActivity.class);
                startActivity(intentFriends);
                break;
            case R.id.tv_photos:
                Intent intentPhotos = new Intent(getActivity(), MyAccountMyPhotosActivity.class);
                startActivity(intentPhotos);
                break;
            case R.id.iv_more:
                break;
        }
    }
}

