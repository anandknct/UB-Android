package com.unitybound.groups.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.activity.AddPostActivity;
import com.unitybound.groups.adapter.GroupMembersGridAdapter;
import com.unitybound.groups.adapter.GroupsDetailsFeedsAdapter;
import com.unitybound.groups.beans.blockGroupMember.BlockGroupMemberResponse;
import com.unitybound.groups.beans.blockUserList.BlockUsersItem;
import com.unitybound.groups.beans.blockUserList.BlockedUserListResponse;
import com.unitybound.groups.beans.deleteGroupMember.DeleteGroupMemberResponse;
import com.unitybound.groups.beans.groupComments.AllpostsItem;
import com.unitybound.groups.beans.groupComments.Group;
import com.unitybound.groups.beans.groupComments.GroupCommentsResponse;
import com.unitybound.groups.beans.groupMembers.GroupMembersResponse;
import com.unitybound.groups.beans.groupMembers.GroupUserDetailsItem;
import com.unitybound.groups.beans.groupPhotos.GroupPhotosResponse;
import com.unitybound.groups.beans.joinGroup.JoinGroupResponse;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.GridSpacingItemDecoration;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsDetailsActivity extends AppCompatActivity implements
        GroupsDetailsFeedsAdapter.IListAdapterCallback, SwipeRefreshLayout.OnRefreshListener,
        CustomDialog.IDialogListener, GroupBlockUsersListAdapter.onBlockUnblockClick,
        GroupMembersGridAdapter.onMemberGridClickListener, GroupPhotosGridAdapter.onGroupPhotosClickListener {

    private TextView tv_group_description, tv_member_label, tv_comment_label = null, tv_photos_label, tv_about_label;
    private ImageView tv_blocked_label;
    private RecyclerView recyclerView = null;
    //    private SwipeRefreshLayout swipeRefresh = null;
    private int mSelectedTab = 0;
    private Call<GroupCommentsResponse> callComments = null;
    private ProgressDialog mProgressDialog = null;
    private GroupCommentsResponse churchCommentsResponse = null;
    private String mGROUP_ID = null;
    private TextView tvJoin = null;
    private Call<JoinGroupResponse> callJoin = null;
    private Call<JoinGroupResponse> callLeaveChurchRequest = null;
    private Call<GroupMembersResponse> callMembersRequest = null;
    private List<GroupUserDetailsItem> allGroupMembersResponse = null;
    private GridSpacingItemDecoration mGridItemDecorator = null;
    private Call<GroupPhotosResponse> callPhotosRequest;
    private List<String> allGroupPhotosResponse = null;
    private ImageView iv_event_image = null;
    private TextView tv_group_name = null, btn_photos = null;
    private Call<BlockGroupMemberResponse> callBlockMembersRequest = null;
    private Call<BlockedUserListResponse> callBlockListRequest = null;
    private List<BlockUsersItem> allBlockedUserListResponse = null;
    private GroupBlockUsersListAdapter groupBlockUsersListAdapter = null;
    private Call<DeleteGroupMemberResponse> callDeleteGroupMembersRequest = null;
    private GroupMembersGridAdapter groupMembersGridAdapter = null;
    private FloatingActionButton fab_group_comment_post  =null;

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
        fab_group_comment_post = (FloatingActionButton) findViewById(R.id.fab_group_comment_post);
        mProgressDialog = new ProgressDialog(this, R.style.newDialog);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_group_description = (TextView) findViewById(R.id.tv_group_description);
        tv_member_label = (TextView) findViewById(R.id.tv_member_label);
        tv_comment_label = (TextView) findViewById(R.id.tv_comment_label);
        tv_photos_label = (TextView) findViewById(R.id.tv_photos_label);
        tv_about_label = (TextView) findViewById(R.id.tv_about_label);
        tv_blocked_label = (ImageView) findViewById(R.id.tv_blocked_label);
        iv_event_image = (ImageView) findViewById(R.id.iv_event_image);
        tvJoin = (TextView) findViewById(R.id.tv_join);
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        btn_photos = (TextView) findViewById(R.id.btn_photos);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_two);
        mGridItemDecorator = new GridSpacingItemDecoration(4, spacingInPixels,
                true, 0);

        setUpBottomListener();
        mGROUP_ID = getIntent().getStringExtra(Util.GROUP_ID);
        if (Util.checkNetworkAvailablity(GroupsDetailsActivity.this)) {
            getGroupCommentsRequest(mGROUP_ID);
        } else {
            CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
//        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
//        swipeRefresh.setOnRefreshListener(GroupsDetailsActivity.this);

    }

    private void setUpBottomListener() {
        tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(GroupsDetailsActivity.this)) {
                    if (tvJoin.getText().toString().equalsIgnoreCase("join")) {
                    /*Join Church Custom dialog*/
//                    new CustomDialog(GroupsDetailsActivity.this, GroupsDetailsActivity.this).show();

                        joinGroupRequest(mGROUP_ID, "");

                    } else {

                        leaveGroupRequest(mGROUP_ID);

                    }
                } else {
                    CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
            }
        });
        tv_comment_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab != 0) {
                    fab_group_comment_post.setVisibility(View.VISIBLE);
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
                    if (churchCommentsResponse != null && churchCommentsResponse.getAllposts() != null) {
                        setUpCommentsList(churchCommentsResponse.getAllposts(), churchCommentsResponse);
                    } else {
                        Toast.makeText(GroupsDetailsActivity.this, "No data found",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tv_member_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab != 1) {
                    fab_group_comment_post.setVisibility(View.GONE);
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
//                    setUpGroupMembersGrid(allGroupMembersResponse);
                    if (Util.checkNetworkAvailablity(GroupsDetailsActivity.this)) {

//                        if (allGroupMembersResponse != null && allGroupMembersResponse.size() > 0) {
//                            // If Already loaded group members then reset list on UI
//                            setUpGroupMembersGrid(allGroupMembersResponse);
//                        } else {
//                            recyclerView.setAdapter(null);
//                            recyclerView.removeAllViews();
                        getGroupMembersPage(mGROUP_ID);
//                        }
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this, null,
                                "", getResources().getString(R.string.alt_checknet),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }

                }
            }
        });

        tv_photos_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab != 2) {
                    fab_group_comment_post.setVisibility(View.GONE);
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

                    // Call API to get all photos
                    if (Util.checkNetworkAvailablity(GroupsDetailsActivity.this)) {

//                        if (allGroupPhotosResponse != null && allGroupPhotosResponse.size() > 0) {
//                            // If Already loaded group members then reset list on UI
//                            setUpGroupPhotosGrid(allGroupPhotosResponse);
//                        } else {
//                            recyclerView.setAdapter(null);
//                            recyclerView.removeAllViews();
                        getGroupPhotos(mGROUP_ID);
//                        }
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this, null,
                                "", getResources().getString(R.string.alt_checknet),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                }
            }
        });

        tv_about_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTab != 3) {
                    fab_group_comment_post.setVisibility(View.GONE);
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
                if (mSelectedTab != 4) {
                    fab_group_comment_post.setVisibility(View.GONE);
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

                    if (Util.checkNetworkAvailablity(GroupsDetailsActivity.this)) {
                        getGroupBlockedMembersList(mGROUP_ID);
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this, null,
                                "", getResources().getString(R.string.alt_checknet),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                }
            }
        });

        fab_group_comment_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsDetailsActivity.this, AddPostActivity.class);
                intent.putExtra("mGROUP_ID",mGROUP_ID);
                intent.putExtra("TITTLE","Add Group Comment");
                startActivity(intent);
            }
        });
    }

    private void setUpCommentsList(List<AllpostsItem> data, GroupCommentsResponse
            churchCommentsResponse) {
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(GroupsDetailsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        GroupsDetailsFeedsAdapter adapter = new GroupsDetailsFeedsAdapter(
                GroupsDetailsActivity.this, data,
                GroupsDetailsActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
//        swipeRefresh.setRefreshing(false);
    }

    private void setUpGroupMembersGrid(List<GroupUserDetailsItem> allGroupMembersResponse) {


        groupMembersGridAdapter = new GroupMembersGridAdapter(GroupsDetailsActivity.this,
                allGroupMembersResponse, GroupsDetailsActivity.this);
        recyclerView.removeAllViews();
        GridLayoutManager lLayout = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.removeItemDecoration(mGridItemDecorator);
        recyclerView.addItemDecoration(mGridItemDecorator);
        recyclerView.setAdapter(groupMembersGridAdapter);
    }

    private void setUpGroupPhotosGrid(List<String> allGroupPhotosResponse) {

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_two);
        GroupPhotosGridAdapter adapter = new GroupPhotosGridAdapter(
                this,
                allGroupPhotosResponse,GroupsDetailsActivity.this);
        recyclerView.removeAllViews();
        GridLayoutManager lLayout = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.removeItemDecoration(mGridItemDecorator);
        recyclerView.addItemDecoration(mGridItemDecorator);
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
    public void onItemClickListener(String s, int position) {

    }

    @Override
    public void onCommentClickListener(String s, int position) {

    }

    @Override
    public void onRefresh() {

    }

    public void showProgressDialog() {
        mProgressDialog.show();
        if (mProgressDialog != null) {
            //  mProgressDialog = Utils.createProgressDialog(BaseActivity.this, getString(R.string.str_logging_in));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.setContentView(R.layout.custom_progressdialog);
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String code) {
        if (param.equalsIgnoreCase(CustomDialog.REQUEST_ACCESS)) {

        }
    }

    private void getGroupCommentsRequest(String mGROUP_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callComments = apiService.getGroupComments(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", GroupsDetailsActivity.this),
                mGROUP_ID);

        callComments.enqueue(new Callback<GroupCommentsResponse>() {

            @Override
            public void onResponse(Call<GroupCommentsResponse> call, Response<GroupCommentsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        churchCommentsResponse = response.body();
                        if (churchCommentsResponse.getAllposts() != null) {
                            setUpCommentsList(churchCommentsResponse.getAllposts(), churchCommentsResponse);
                            setAboutValuesOnUI(churchCommentsResponse.getGroup());
                        }
                        break;
                    case "4":
                        if (sCode.equalsIgnoreCase("401")) {
                            CustomDialog customDialog1 = new CustomDialog(
                                    GroupsDetailsActivity.this, null,
                                    "", "",
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        } else {
                            if (response.body() == null) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Gson gson = new Gson();
                                    ErrorResponse error = gson.fromJson(jObjError.toString(),
                                            ErrorResponse.class);
                                    String msg = null;
                                    if (error != null) {
                                        msg = error.getMsg();
                                    } else {
                                        msg = "Something went wrong";
                                    }
                                    new CustomDialog(GroupsDetailsActivity.this, null, "",
                                            msg,
                                            "ONFAILED").show();
                                    hideProgressDialog();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case "5": // TODO Server Error and display retry
                        GroupCommentsResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                null, "", "No data found",
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        GroupCommentsResponse body1 = response.body();
                        new CustomDialog(GroupsDetailsActivity.this, null, "",
                                "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<GroupCommentsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(GroupsDetailsActivity.this, null, "",
                        t.getMessage() != null ?
                                t.getMessage().length() > 0 ?
                                        t.getMessage()
                                        : "Something went wrong" : "Something went wrong",
                        "ONFAILED").show();
            }
        });
    }

    /**
     * After Response set all values on UI elements
     *
     * @param group
     */
    @SuppressLint("DefaultLocale")
    private void setAboutValuesOnUI(Group group) {

        tv_group_description.setText(group.getGroupDescription());
        btn_photos.setText(String.format("%d %s", group.getGroupMembers(),
                getString(R.string.str_members)));
        tv_group_name.setText(group.getGroupName());
        Glide.with(this)
                .load(group.getGroupImage())
                .placeholder(R.drawable.ic_photos_placeholder)
                .into(iv_event_image);
    }

    private void joinGroupRequest(String mGROUP_ID, String code) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callJoin = apiService.joinGroupRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", GroupsDetailsActivity.this),
                mGROUP_ID);

        callJoin.enqueue(new Callback<JoinGroupResponse>() {

            @Override
            public void onResponse(Call<JoinGroupResponse> call, Response<JoinGroupResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        JoinGroupResponse joinChurchResponse = response.body();
                        if (joinChurchResponse.getSuccess().equalsIgnoreCase("success")) {
                            Toast.makeText(GroupsDetailsActivity.this, joinChurchResponse.getMsg(), Toast.LENGTH_SHORT).show();

//                          After join change UI REF from setAllValuesOnUI method
                            tvJoin.setText("Leave");

                            tvJoin.setBackgroundColor(Color.parseColor("#552A83"));

                            Toast.makeText(GroupsDetailsActivity.this, joinChurchResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this, null,
                                    "", "",
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        } else {
                            if (response.body() == null) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Gson gson = new Gson();
                                    ErrorResponse error = gson.fromJson(jObjError.toString(),
                                            ErrorResponse.class);
                                    String msg = null;
                                    if (error != null) {
                                        msg = error.getMsg();
                                    } else {
                                        msg = "Something went wrong";
                                    }
                                    new CustomDialog(GroupsDetailsActivity.this, null, "",
                                            msg,
                                            "ONFAILED").show();
                                    hideProgressDialog();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case "5": // TODO Server Error and display retry
                        JoinGroupResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        JoinGroupResponse body1 = response.body();
                        new CustomDialog(GroupsDetailsActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ?
                                                body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<JoinGroupResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(GroupsDetailsActivity.this, null, "",
                        t.getMessage() != null ?
                                t.getMessage().length() > 0 ?
                                        t.getMessage()
                                        : "Something went wrong" : "Something went wrong",
                        "ONFAILED").show();
            }
        });
    }

    private void leaveGroupRequest(String mGROUP_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callLeaveChurchRequest = apiService.leaveGroupRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", GroupsDetailsActivity.this),
                mGROUP_ID);

        callLeaveChurchRequest.enqueue(new Callback<JoinGroupResponse>() {

            @Override
            public void onResponse(Call<JoinGroupResponse> call, Response<JoinGroupResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    JoinGroupResponse loginResponse = response.body();
                    tvJoin.setText("Join");
                    tvJoin.setBackgroundColor(Util.getColor(GroupsDetailsActivity.this, R.color.btn_bg_color));
                    if (loginResponse != null) {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                GroupsDetailsActivity.this, "", loginResponse.getMsg(),
                                "SUCCESS");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                null, "", getString(R.string.str_server_error),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }

                } else {

                    CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                            null, "", getString(R.string.str_server_error),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<JoinGroupResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(GroupsDetailsActivity.this, null, "",
                        t.getMessage() != null ?
                                t.getMessage().length() > 0 ?
                                        t.getMessage()
                                        : "Something went wrong" : "Something went wrong",
                        "ONFAILED").show();
            }
        });
    }

    private void getGroupMembersPage(String mGROUP_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callMembersRequest = apiService.groupMembersRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", GroupsDetailsActivity.this),
                mGROUP_ID);

        callMembersRequest.enqueue(new Callback<GroupMembersResponse>() {

            @Override
            public void onResponse(Call<GroupMembersResponse> call,
                                   Response<GroupMembersResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    GroupMembersResponse groupMembersResponse = response.body();
                    if (groupMembersResponse.getData() != null) {
                        allGroupMembersResponse = groupMembersResponse.getData()
                                .getGroupUserDetails();
                        if (allGroupMembersResponse != null && allGroupMembersResponse.size() > 0) {
                            setUpGroupMembersGrid(allGroupMembersResponse);
                        } else {
                            recyclerView.setAdapter(null);
                            recyclerView.removeAllViews();
                        }
//                            swipeRefresh.setRefreshing(false);

                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                            null,
                            "", "",
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<GroupMembersResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(GroupsDetailsActivity.this, null, "",
                        t.getMessage() != null ?
                                t.getMessage().length() > 0 ?
                                        t.getMessage()
                                        : "Something went wrong" : "Something went wrong",
                        "ONFAILED").show();
            }
        });
    }

    private void getGroupPhotos(String mGROUP_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callPhotosRequest = apiService.groupPhotosRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", GroupsDetailsActivity.this),
                mGROUP_ID);

        callPhotosRequest.enqueue(new Callback<GroupPhotosResponse>() {

            @Override
            public void onResponse(Call<GroupPhotosResponse> call,
                                   Response<GroupPhotosResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    GroupPhotosResponse groupMembersResponse = response.body();
                    if (groupMembersResponse.getData() != null) {
                        allGroupPhotosResponse = groupMembersResponse.getData()
                                .getPhotos();
                        if (allGroupPhotosResponse != null && allGroupPhotosResponse.size() > 0) {
                            setUpGroupPhotosGrid(allGroupPhotosResponse);
                        } else {
                            recyclerView.setAdapter(null);
                            recyclerView.removeAllViews();
                        }
//                            swipeRefresh.setRefreshing(false);

                    } else {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                null,
                                "", "No data found...",
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                            null,
                            "", "Server error : " + sCode,
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<GroupPhotosResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                        null,
                        "", "Server error : " + t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    private void getGroupBlockedMembersList(String mGROUP_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callBlockListRequest = apiService.getBlockGroupMembers(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", GroupsDetailsActivity.this),
                mGROUP_ID);

        callBlockListRequest.enqueue(new Callback<BlockedUserListResponse>() {

            @Override
            public void onResponse(Call<BlockedUserListResponse> call,
                                   Response<BlockedUserListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    BlockedUserListResponse groupMembersResponse = response.body();
                    if (groupMembersResponse.getData() != null) {
                        allBlockedUserListResponse = groupMembersResponse.getData()
                                .getBlockUsers();
                        if (allBlockedUserListResponse != null && allBlockedUserListResponse.size() > 0) {
//                            setUpGroupMembersGrid(allBlockedUserListResponse);
                            setUpGroupBlockedUsersList(allBlockedUserListResponse);
                        } else {
                            recyclerView.setAdapter(null);
                            recyclerView.removeAllViews();
                        }
//                            swipeRefresh.setRefreshing(false);

                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                            null,
                            "", "",
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<BlockedUserListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(GroupsDetailsActivity.this, null, "",
                        t.getMessage() != null ?
                                t.getMessage().length() > 0 ?
                                        t.getMessage()
                                        : "Something went wrong" : "Something went wrong",
                        "ONFAILED").show();
            }
        });
    }

    private void setUpGroupBlockedUsersList(List<BlockUsersItem> allBlockedUserListResponse) {

        recyclerView.removeAllViews();
        groupBlockUsersListAdapter =
                new GroupBlockUsersListAdapter(GroupsDetailsActivity.this,
                        allBlockedUserListResponse, GroupsDetailsActivity.this);
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(groupBlockUsersListAdapter);
    }

    /**
     * From block user list unblock user this adapter click is called
     *
     * @param id
     * @param position
     */
    @Override
    public void onBlockUnblockButtonClick(String id, int position) {
        if (Util.checkNetworkAvailablity(GroupsDetailsActivity.this)) {
            unBlockMembers(mGROUP_ID, id, position);
        } else {
            CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    private void unBlockMembers(String mGROUP_ID, String userId, int position) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callBlockMembersRequest = apiService.unBlockGroupMembers(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", GroupsDetailsActivity.this),
                mGROUP_ID,
                userId);

        callBlockMembersRequest.enqueue(new Callback<BlockGroupMemberResponse>() {

            @Override
            public void onResponse(Call<BlockGroupMemberResponse> call,
                                   Response<BlockGroupMemberResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    BlockGroupMemberResponse groupMembersResponse = response.body();
                    if (groupMembersResponse.getStatus().equalsIgnoreCase("success")) {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                null,
                                "", groupMembersResponse.getMsg() + "",
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        if (allBlockedUserListResponse != null && allBlockedUserListResponse.size() > 0) {
                            allBlockedUserListResponse.remove(position);
                            groupBlockUsersListAdapter.notifyItemRemoved(position);
                        }
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                null,
                                "", groupMembersResponse.getMsg() + "",
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                } else {
                    CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                            null,
                            "", "",
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<BlockGroupMemberResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                        null,
                        "", "Server error : " + t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    @Override
    public void onDisableMemberButton(String id, int position) {
        if (Util.checkNetworkAvailablity(GroupsDetailsActivity.this)) {
            if (mGROUP_ID!=null) {
                blockMembers(this.mGROUP_ID, id, position);
            }else{
                Log.e("nik","mGROUP_ID null");
            }
        } else {
            CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    private void blockMembers(String mGROUP_ID, String userId, int position) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callBlockMembersRequest = apiService.BlockGroupMembers(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", GroupsDetailsActivity.this),
                mGROUP_ID, userId);

        callBlockMembersRequest.enqueue(new Callback<BlockGroupMemberResponse>() {

            @Override
            public void onResponse(Call<BlockGroupMemberResponse> call,
                                   Response<BlockGroupMemberResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    BlockGroupMemberResponse groupMembersResponse = response.body();
                    if (groupMembersResponse.getStatus().equalsIgnoreCase("success")) {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                null,
                                "", groupMembersResponse.getMsg() + "",
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        if (allGroupMembersResponse != null && allGroupMembersResponse.size() > 0) {
                            allGroupMembersResponse.remove(position);
                            groupMembersGridAdapter.notifyItemRemoved(position);
                        }
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                null,
                                "", groupMembersResponse.getMsg() + "",
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                } else {
                    CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                            null,
                            "", "",
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<BlockGroupMemberResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onDeleteMemberButton(String id, int position) {
        if (Util.checkNetworkAvailablity(GroupsDetailsActivity.this)) {
            deleteGroupMember(mGROUP_ID,id,position);
        } else {
            CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    /**
     * To Delete Group member
     * @param mGROUP_ID
     * @param userId
     * @param position
     */
    private void deleteGroupMember(String mGROUP_ID, String userId, int position) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callDeleteGroupMembersRequest = apiService.deleteGroupMembers(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", GroupsDetailsActivity.this),
                mGROUP_ID, userId);

        callDeleteGroupMembersRequest.enqueue(new Callback<DeleteGroupMemberResponse>() {

            @Override
            public void onResponse(Call<DeleteGroupMemberResponse> call,
                                   Response<DeleteGroupMemberResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    DeleteGroupMemberResponse groupMembersResponse = response.body();
                    if (groupMembersResponse.getSuccess().equalsIgnoreCase("success")) {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                null,
                                "", groupMembersResponse.getMsg() + "",
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        if (allGroupMembersResponse != null && allGroupMembersResponse.size() > 0) {
                            allGroupMembersResponse.remove(position);
                            groupMembersGridAdapter.notifyItemRemoved(position);
                        }
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                                null,
                                "", groupMembersResponse.getMsg() + "",
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                } else {
                    CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                            null,
                            "", "",
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<DeleteGroupMemberResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(GroupsDetailsActivity.this,
                        null,
                        "", "Server error : " + t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    @Override
    public void onGroupImageClickListener(String url, int position) {
        if (url!=null && url.length()>0) {
            Util.navigateTOFullScreenActivity(this,
                    url,
                    null,
                    null,
                    null);
        }
    }
}
