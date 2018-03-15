package com.unitybound.church.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.activity.AddPostActivity;
import com.unitybound.account.beans.ProfileFriendsResponse;
import com.unitybound.account.beans.hidePost.HidePostResponse;
import com.unitybound.church.activity.EditChurchActivity;
import com.unitybound.church.adapter.ChurchBlockedUsersListAdapter;
import com.unitybound.church.adapter.ChurchLibraryAdapter;
import com.unitybound.church.adapter.ChurchMembersGridAdapter;
import com.unitybound.church.adapter.MembershipRequestListAdapter;
import com.unitybound.church.beans.JoinByAccessCodeResponse;
import com.unitybound.church.beans.blockChurchMember.BlockChurchMemberResponse;
import com.unitybound.church.beans.blockedUsers.ChurchBlockedUsersListResponse;
import com.unitybound.church.beans.churchDetail.AllpostsItem;
import com.unitybound.church.beans.churchDetail.Church;
import com.unitybound.church.beans.churchDetail.ChurchDetailResponse;
import com.unitybound.church.beans.churchDetail.Data;
import com.unitybound.church.beans.churchJoinedMembers.ChurchJoinedMembersResponse;
import com.unitybound.church.beans.churchRequestedMembers.ChurchJoinRequestedListResponse;
import com.unitybound.church.beans.coverPhotoAdd.AddCoverPhotoResponse;
import com.unitybound.church.beans.deleteMember.DeleteChurchMemberResponse;
import com.unitybound.church.beans.detailMembers.ChurchDetailMembersResponse;
import com.unitybound.church.beans.detailMembers.ChurchUserDetailsItem;
import com.unitybound.church.beans.joinRequest.JoinChurchResponse;
import com.unitybound.church.beans.leaveChurch.LeaveChurchResponse;
import com.unitybound.church.beans.libraryAddFile.AddLibraryResponse;
import com.unitybound.church.beans.libraryListMode.ChurchDocumentItem;
import com.unitybound.church.beans.libraryListMode.LibraryListResponse;
import com.unitybound.church.beans.refreshAccessCode.RefreshAccessCodeResponse;
import com.unitybound.church.listners.IchurchBlockUsers;
import com.unitybound.church.listners.IchurchDetailMemberRow;
import com.unitybound.church.listners.MembershipRequestListner;
import com.unitybound.events.fragment.activity.AddEventActivity;
import com.unitybound.events.fragment.adapter.EventsListAdapter;
import com.unitybound.events.fragment.adapter.ProfileFriendsAddEventAdapter;
import com.unitybound.events.fragment.beans.addEvent.AddEventResponse;
import com.unitybound.main.home.fragment.adapter.HomeFeedsAdapter;
import com.unitybound.main.home.fragment.beans.favUnfav.FavouriteUnfavResponse;
import com.unitybound.main.home.fragment.beans.like.LikePostResponse;
import com.unitybound.utility.DownloadFileFromURL;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.GridSpacingItemDecoration;
import com.unitybound.utility.SimpleDividerItemDecoration;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.unitybound.utility.customView.CustomDialog.NO_PARAM;
import static com.unitybound.utility.customView.CustomDialog.REQUEST_ACCESS;

public class ChurchDetailsActivity extends ActivityManagePermission implements
        EventsListAdapter.IListAdapterCallback, CustomDialog.IDialogListener,
        HomeFeedsAdapter.IListAdapterCallback, IchurchDetailMemberRow,
        MembershipRequestListner, IchurchBlockUsers, View.OnClickListener, ChurchLibraryAdapter.AdapterClickListener {

    private static final int FILE_CHOOSER = 222;
    private ChurchBlockedUsersListAdapter adapter;
    private ChurchLibraryAdapter churchLibraryAdapter = null;
    private TextView tv_about_label = null, tv_member_label = null, TvIntiveFrnd = null, tvJoin = null;
    private ImageView iv_church_image = null;
    private ProgressDialog mProgressDialog = null;
    private RecyclerView rv_grid_layout = null;
    private String mCHURCH_ID = null;
    private TextView tvAddress = null, tvMemberCount = null, tvChurchName = null, tvPosterName = null,
            tvChurchAddress = null, tvChurchPhone = null, tvChurchDescription = null;
    private Call<ChurchDetailMembersResponse> callMembers = null;
    private Call<ChurchDetailResponse> callDetail = null;
    private ArrayList<ChurchUserDetailsItem> allChurchMembers = null;
    private ChurchDetailResponse churchDetailResponse = null;
    private Call<JoinChurchResponse> callJoin = null;
    private Call<JoinByAccessCodeResponse> callJoinByCode;
    private LinearLayout bottomLinearNotJoin = null, bottomLinearJoined = null, LinLayInviteMember = null;
    private TableLayout top_linear_joined = null;
    private TextView tv_post_label_joined = null, tv_about_label_joined = null,
            tv_library_joined = null, tv_member_joined = null;
    private TextView tv_membership_req_joined = null, tv_blocked_joined = null, tv_setting_joined = null;
    private RelativeLayout rr_description_layout = null;
    private TextView tv_event_description = null;
    private RelativeLayout settings_layout = null, library_layout = null;
    private BottomSheetDialog mBottomSheetDialog = null;
    private Call<ChurchJoinedMembersResponse> callJoineMembers = null;
    private Call<ChurchJoinRequestedListResponse> callJoinRequest = null;
    private Call<ChurchBlockedUsersListResponse> callBlockedUserRequest = null;
    private List<com.unitybound.church.beans.churchJoinedMembers.ChurchUserDetailsItem> allChurchJoinedMembers = null;
    private List<com.unitybound.church.beans.churchRequestedMembers.ChurchUserDetailsItem> allJoinMembersRequests = null;
    private ArrayList<AllpostsItem> mAllPostData = null;
    ;
    private ArrayList<com.unitybound.church.beans.blockedUsers.ChurchUserDetailsItem> mAllBlockedUsers = null;
    private Call<LeaveChurchResponse> callLeaveChurchRequest = null;
    private ImageView iv_edit_cover = null;
    private String mFilePath = null;
    private Bitmap bmp = null;
    private RelativeLayout main_root = null;
    private Call<RefreshAccessCodeResponse> callRefrashCode = null;
    private EditText edt_code = null;
    Button btn_copy_code = null;
    private TextView tv_refresh_code = null;
    private Call<BlockChurchMemberResponse> callBlockChurchMenberJoin = null;
    private Call<DeleteChurchMemberResponse> callDeleteChurchMenber = null;
    private FloatingActionButton fabCreatePost = null;
    private GridSpacingItemDecoration gridSpacingItemDecoration = null;
    private SimpleDividerItemDecoration simpleItemDecoration = null;
    private Call<LikePostResponse> callLikePost = null;
    private String mForSheetUserId = null;
    private int mForSheetPosition = -1;
    private int mLastSelectedPostion = 0;
    private Call<FavouriteUnfavResponse> callFavPost = null;
    private Call<HidePostResponse> callHidePost = null;
    private RecyclerView rvLibraryList = null;
    private Call<LibraryListResponse> callLibrary = null;
    private List<ChurchDocumentItem> allChurchDocuments = null;
    private EditText edt_upload_tittle = null, edt_tittle = null;
    private Button BtnEditAbout = null, BtnInviteMember = null;

    private Call<ProfileFriendsResponse> callProfileFriends = null;
    private Call<DeleteChurchMemberResponse> callInviteUsers = null;
    private RecyclerView recyclerView;
    private ProfileFriendsAddEventAdapter ProfileAdapter = null;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("allChurchMembers", allChurchMembers);
        bundle.putString("mCHURCH_ID", mCHURCH_ID);
        bundle.putParcelable("churchDetailResponse", churchDetailResponse);
        bundle.putParcelableArrayList("mAllPostData", mAllPostData);
        bundle.putParcelableArrayList("mAllBlockedUsers", mAllBlockedUsers);
        outState.putAll(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Bundle bundle = savedInstanceState;
        if (bundle != null) {
            allChurchMembers = bundle.getParcelableArrayList("allChurchMembers");
            churchDetailResponse = bundle.getParcelable("churchDetailResponse");
            mCHURCH_ID = bundle.getString("mCHURCH_ID");
            mAllPostData = bundle.getParcelableArrayList("mAllPostData");
            mAllBlockedUsers = bundle.getParcelableArrayList("mAllBlockedUsers");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_church_detail);

        initView();
        setUpToolbar();
        mCHURCH_ID = getIntent().getStringExtra(Util.CHURCH_ID);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getChurchDetail(mCHURCH_ID);
    }

    private void getChurchDetail(String mCHURCH_ID) {
        if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
            getChurchDetailRequest(mCHURCH_ID);
        } else {
            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null, "", getResources().getString(R.string.alt_checknet), "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    @Override
    protected void onDestroy() {
        if (callDetail != null && callDetail.isExecuted()) {
            callDetail.cancel();
        } else if (callMembers != null && callMembers.isExecuted()) {
            callMembers.cancel();
        } else if (callJoin != null && callJoin.isExecuted()) {
            callJoin.cancel();
        } else if (callJoinByCode != null && callJoinByCode.isExecuted()) {
            callJoinByCode.cancel();
        }
        super.onDestroy();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.str_church_detail));
    }

    private void initView() {
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_ten);
        gridSpacingItemDecoration = new GridSpacingItemDecoration(4, spacingInPixels, true, 0);
        simpleItemDecoration = new SimpleDividerItemDecoration(this);
        fabCreatePost = findViewById(R.id.fab_create_post);
        BtnEditAbout = findViewById(R.id.BtnEditAbout);
        main_root = findViewById(R.id.activity_emergency);
        iv_church_image = findViewById(R.id.iv_church_image);
        iv_church_image.setOnClickListener(this);
        iv_edit_cover = findViewById(R.id.iv_edit_cover);
        TvIntiveFrnd  = findViewById(R.id.TvIntiveFrnd);
        iv_edit_cover.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this, R.style.newDialog);

        Button btn_attach = findViewById(R.id.btn_attach);
        btn_attach.setOnClickListener(this);
        edt_tittle = findViewById(R.id.edt_tittle);
        edt_upload_tittle = findViewById(R.id.edt_upload_tittle);
        tvAddress = findViewById(R.id.tv_address);
        tvMemberCount = findViewById(R.id.tv_member_count);
        tvChurchName = findViewById(R.id.tv_church_name);
        tvPosterName = findViewById(R.id.tv_poster_name);
        tvChurchAddress = findViewById(R.id.tv_church_address);
        tvChurchPhone = findViewById(R.id.tv_church_phone);
        tvChurchDescription = findViewById(R.id.tv_church_description);
        tvJoin = findViewById(R.id.btn_join);
        rv_grid_layout = findViewById(R.id.rv_grid_layout);
        rvLibraryList = findViewById(R.id.ll_libraryList);
        tv_event_description = findViewById(R.id.tv_event_description);
        rr_description_layout = findViewById(R.id.rr_description_layout);
        settings_layout = findViewById(R.id.settings_layout);
        library_layout = findViewById(R.id.library_layout);
        LinLayInviteMember = findViewById(R.id.LinLayInviteMember);
        // Not Joined bottom layout below
        tv_about_label = findViewById(R.id.tv_about_label);
        tv_member_label = findViewById(R.id.tv_member_label);


        rv_grid_layout.addItemDecoration(gridSpacingItemDecoration);
        // Joined bottom layout below
        bottomLinearNotJoin = findViewById(R.id.bottom_linear_not_join);
        bottomLinearJoined = findViewById(R.id.bottom_linear_joined);
        top_linear_joined = findViewById(R.id.top_linear_joined);
        tv_post_label_joined = findViewById(R.id.tv_post_label_joined);
        tv_about_label_joined = findViewById(R.id.tv_about_label_joined);
        tv_library_joined = findViewById(R.id.tv_library_joined);
        tv_member_joined = findViewById(R.id.tv_member_joined);
        tv_membership_req_joined = findViewById(R.id.tv_membership_req_joined);
        tv_blocked_joined = findViewById(R.id.tv_blocked_joined);
        tv_setting_joined = findViewById(R.id.tv_setting_joined);

        // Settings layout
        edt_code = findViewById(R.id.edt_code);
        btn_copy_code = findViewById(R.id.btn_copy_code);
        tv_refresh_code = findViewById(R.id.tv_refresh_code);
        recyclerView = findViewById(R.id.rv_list_layout);
        BtnInviteMember = findViewById(R.id.BtnInviteMember);

        setUponClickDetailButtons();
        setUpBottomSheet();
    }

    private void setUponClickDetailButtons() {

        BtnInviteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendInviteToServer();
            }
        });
        fabCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChurchDetailsActivity.this, AddPostActivity.class);
                intent.putExtra("mCHURCH_ID", mCHURCH_ID);
                intent.putExtra("TITTLE", "Add Church Comment");
                startActivity(intent);
            }
        });

        iv_church_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (churchDetailResponse.getData().getChurch().getChurchImage() != null &&
                        churchDetailResponse.getData().getChurch().getChurchImage().length() > 0) {
                    Util.navigateTOFullScreenActivity(ChurchDetailsActivity.this,
                            churchDetailResponse.getData().getChurch().getChurchImage(),
                            churchDetailResponse.getData().getChurch().getId(), null, null);
                } else {
                    Toast.makeText(ChurchDetailsActivity.this, "Image not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvJoin.getText().toString().equalsIgnoreCase("join")) {
                    /*Join Church Custom dialog*/
                    new CustomDialog(ChurchDetailsActivity.this, ChurchDetailsActivity.this).show();
                } else {
                    if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
                        leaveChurchRequest(mCHURCH_ID);
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                                "", getResources().getString(R.string.alt_checknet), "ONFAILED");
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
                tv_member_label.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_member_label.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                TvIntiveFrnd.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.color_white));

                showLayoutFor(2);
            }
        });

        TvIntiveFrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_member_label.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_about_label.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                TvIntiveFrnd.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.color_white));
                showLayoutFor(8);
            }
        });
        tv_member_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_member_label.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.color_white));
                tv_about_label.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));

                showLayoutFor(1);

                if (allChurchMembers == null) {
                    if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
                        getChurchDetailMembers(mCHURCH_ID);
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
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

        // Bottom Footer tab buttons below
        tv_post_label_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeJoinedBottomColor(1);
                showLayoutFor(1);
                getChurchDetail(mCHURCH_ID);
//                if (mAllPostData != null && mAllPostData.size() > 0) {
//                    HomeFeedsAdapter adapter = new HomeFeedsAdapter(
//                            ChurchDetailsActivity.this,
//                            mAllPostData, ChurchDetailsActivity.this, false);
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
//                            ChurchDetailsActivity.this);
//                    rv_grid_layout.setLayoutManager(mLayoutManager);
//                    rv_grid_layout.setItemAnimator(new DefaultItemAnimator());
//                    rv_grid_layout.removeItemDecoration(simpleItemDecoration);
//                    rv_grid_layout.addItemDecoration(simpleItemDecoration);
//                    rv_grid_layout.setHasFixedSize(true);
//                    rv_grid_layout.setNestedScrollingEnabled(false);
//                    rv_grid_layout.setAdapter(adapter);
//                } else {
//                    rv_grid_layout.setAdapter(null);
//                }
            }
        });
        tv_about_label_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeJoinedBottomColor(2);
                showLayoutFor(2);

                if (mAllPostData != null && mAllPostData.size() > 0) {
                    HomeFeedsAdapter adapter = new HomeFeedsAdapter(
                            ChurchDetailsActivity.this, mAllPostData
                            , ChurchDetailsActivity.this, false);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                            ChurchDetailsActivity.this);
                    rv_grid_layout.setLayoutManager(mLayoutManager);
                    rv_grid_layout.setItemAnimator(new DefaultItemAnimator());
                    rv_grid_layout.removeItemDecoration(simpleItemDecoration);
                    rv_grid_layout.addItemDecoration(simpleItemDecoration);
                    rv_grid_layout.setHasFixedSize(true);
                    rv_grid_layout.setNestedScrollingEnabled(false);
                    rv_grid_layout.setAdapter(adapter);
                } else {
                    rv_grid_layout.setAdapter(null);
                }
            }
        });
        tv_library_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeJoinedBottomColor(3);
                showLayoutFor(3);
                if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
                    getChurchLibrary(mCHURCH_ID);
                } else {
                    CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
            }
        });
        tv_member_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeJoinedBottomColor(4);
                showLayoutFor(4);

                if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
//                    getChurchJoinedMembersPage(mCHURCH_ID);
                    getChurchDetailMembers(mCHURCH_ID);
                } else {
                    CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }
        });
// Top Header tab buttons below
        tv_membership_req_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeJoinedBottomColor(5);
                showLayoutFor(5);

                if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
                    getJoinMembersRequest(mCHURCH_ID);
                } else {
                    CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
            }
        });

        tv_blocked_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeJoinedBottomColor(6);
                showLayoutFor(6);

                if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
                    getChurchChurchBlockedMembers(mCHURCH_ID);
                } else {
                    CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
            }
        });

        tv_setting_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeJoinedBottomColor(7);
                showLayoutFor(7);
            }
        });

        tv_refresh_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
                    refreshAccessCodeRequest(mCHURCH_ID);
                } else {
                    CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
            }
        });

        btn_copy_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Access Code", edt_code.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ChurchDetailsActivity.this, "Access Code Copied...", Toast.LENGTH_SHORT).show();
            }
        });


        BtnEditAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent intent = new Intent(ChurchDetailsActivity.this, EditChurchActivity.class);
                intent.putExtra("mCHURCH_ID", mCHURCH_ID);
                intent.putExtra("ChurchAboutData", gson.toJson(churchDetailResponse.getData()));
                intent.putExtra("TITTLE", "Edit Church");
                startActivity(intent);
            }
        });
    }

    private void changeJoinedBottomColor(int position) {
        switch (position) {
            case 1:
                tv_about_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_post_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.color_white));
                tv_library_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_member_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));

                tv_setting_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_blocked_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_membership_req_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                break;
            case 2:
                tv_about_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.color_white));
                tv_post_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_library_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_member_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));

                tv_setting_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_blocked_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_membership_req_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                break;
            case 3:
                tv_about_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_post_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_library_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.color_white));
                tv_member_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));

                tv_setting_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_blocked_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_membership_req_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                break;
            case 4:
                tv_about_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_post_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_library_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_member_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.color_white));

                tv_setting_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_blocked_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_membership_req_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                break;
            // Top Header selection below
            case 5:
                tv_setting_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_blocked_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_membership_req_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.color_white));

                tv_about_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_post_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_library_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_member_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                break;
            case 6:
                tv_setting_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_membership_req_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_blocked_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.color_white));

                tv_about_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_post_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_library_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_member_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                break;
            case 7:
                tv_membership_req_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_blocked_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_setting_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.color_white));

                tv_about_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_post_label_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_library_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                tv_member_joined.setTextColor(ContextCompat.getColor(ChurchDetailsActivity.this, R.color.unselected_text_color));
                break;

        }

    }

    private void showLayoutFor(int position) {

        switch (position) {
            // Not joined conditions below 2
            case 1:
                fabCreatePost.setVisibility(View.VISIBLE);
                rv_grid_layout.setVisibility(View.VISIBLE);
                rr_description_layout.setVisibility(View.GONE);
                settings_layout.setVisibility(View.GONE);
                library_layout.setVisibility(View.GONE);
                LinLayInviteMember.setVisibility(View.GONE);
                break;
            case 2:
                rv_grid_layout.setVisibility(View.GONE);
                rr_description_layout.setVisibility(View.VISIBLE);
                settings_layout.setVisibility(View.GONE);
                library_layout.setVisibility(View.GONE);
                LinLayInviteMember.setVisibility(View.GONE);
                break;
            case 3:
                fabCreatePost.setVisibility(View.GONE);
                rv_grid_layout.setVisibility(View.GONE);
                rr_description_layout.setVisibility(View.GONE);
                settings_layout.setVisibility(View.GONE);
                library_layout.setVisibility(View.VISIBLE);
                LinLayInviteMember.setVisibility(View.GONE);
                break;
            case 4:
                rv_grid_layout.setVisibility(View.VISIBLE);
                rr_description_layout.setVisibility(View.GONE);
                settings_layout.setVisibility(View.GONE);
                library_layout.setVisibility(View.GONE);
                LinLayInviteMember.setVisibility(View.GONE);
                break;
            case 5:
                rv_grid_layout.setVisibility(View.VISIBLE);
                rr_description_layout.setVisibility(View.GONE);
                settings_layout.setVisibility(View.GONE);
                LinLayInviteMember.setVisibility(View.GONE);
                break;
            case 6:
                rv_grid_layout.setVisibility(View.VISIBLE);
                rr_description_layout.setVisibility(View.GONE);
                settings_layout.setVisibility(View.GONE);
                library_layout.setVisibility(View.GONE);
                LinLayInviteMember.setVisibility(View.GONE);
                break;
            case 7:
                rv_grid_layout.setVisibility(View.GONE);
                rr_description_layout.setVisibility(View.GONE);
                settings_layout.setVisibility(View.VISIBLE);
                library_layout.setVisibility(View.GONE);
                LinLayInviteMember.setVisibility(View.GONE);
                break;

            case 8:
                rv_grid_layout.setVisibility(View.GONE);
                rr_description_layout.setVisibility(View.GONE);
                settings_layout.setVisibility(View.GONE);
                library_layout.setVisibility(View.GONE);
                LinLayInviteMember.setVisibility(View.VISIBLE);
                getEventsDetailAboutRequest();
                break;
        }
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

    // Called from post adapter if user already member of of church
    @Override
    public void onCommentClickListner(String s, int position) {

    }

    // Called from post adapter if user already member of of church
    @Override
    public void onUserNameClickListner(String s, int position) {
//        Util.navigateTOProfileAcitivity(ChurchDetailsActivity.this);

    }

    // Called from post adapter if user already member of of church
    @Override
    public void onLikeClickListner(Object postsItem, int position) {
        if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
            String mPostId = (String) postsItem; // ((AllpostsItem) postsItem).getId();
            likePost(mPostId, position);
        } else {
            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    @Override
    public void onDoveClickListener(Object postsItem, int position) {

    }

    @Override
    public void onBookmarkClickListener(Object postsItem, int position) {

    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String code) {
        if (param.equalsIgnoreCase(NO_PARAM)) {

        } else if (param.equalsIgnoreCase(REQUEST_ACCESS)) {
            if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
                joinChurchRequest(mCHURCH_ID, code);
            } else {
                CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                        "", getResources().getString(R.string.alt_checknet),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        } else if (param.equalsIgnoreCase("JOIN")) {
            if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
                joinChurchByAccessCodeRequest(mCHURCH_ID, code);
            } else {
                CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                        "", getResources().getString(R.string.alt_checknet),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        } else if (param.equalsIgnoreCase("CHURCH_LEFT")) {
            tvJoin.setText("Join");
            bottomLinearNotJoin.setVisibility(View.VISIBLE);
            bottomLinearJoined.setVisibility(View.GONE);
            top_linear_joined.setVisibility(View.GONE);
            tvJoin.setBackgroundColor(Color.parseColor("#552A83"));
            changeJoinedBottomColor(1);
            showLayoutFor(2);
        }
    }

    private void getChurchDetailRequest(String mCHURCH_ID) {
        showProgressDialog();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        callDetail = apiService.getChurchDetail(BuildConfig.API_KEY, Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this), mCHURCH_ID);

        callDetail.enqueue(new Callback<ChurchDetailResponse>() {

            @Override
            public void onResponse(Call<ChurchDetailResponse> call, Response<ChurchDetailResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        churchDetailResponse = response.body();
                        if (churchDetailResponse.getData() != null) {
                            setAllValuesOnUI(churchDetailResponse.getData(), churchDetailResponse);
                        }
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
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
                                    ErrorResponse error = gson.fromJson(jObjError.toString(), ErrorResponse.class);
                                    String msg = null;
                                    if (error != null) {
                                        msg = error.getMsg();
                                    } else {
                                        msg = "Something went wrong";
                                    }
                                    new CustomDialog(ChurchDetailsActivity.this, null, "", msg, "ONFAILED").show();
                                    hideProgressDialog();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case "5": // TODO Server Error and display retry
                        ChurchDetailResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null, "", loginResponse1.getMsg(), "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        ChurchDetailResponse body1 = response.body();
                        new CustomDialog(ChurchDetailsActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ?
                                                body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<ChurchDetailResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getChurchDetailMembers(String mCHURCH_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callMembers = apiService.getChurchDetailMembers(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                mCHURCH_ID);

        callMembers.enqueue(new Callback<ChurchDetailMembersResponse>() {

            @Override
            public void onResponse(Call<ChurchDetailMembersResponse> call, Response<ChurchDetailMembersResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        ChurchDetailMembersResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allChurchMembers = loginResponse.getData().getChurchUserDetails();
                            if (allChurchMembers != null && allChurchMembers.size() > 0) {
                                rv_grid_layout.setAdapter(null);
                                ChurchMembersGridAdapter churchMembersGridAdapter = new ChurchMembersGridAdapter(ChurchDetailsActivity.this,
                                        allChurchMembers, ChurchDetailsActivity.this, churchDetailResponse.getData().getChurch().getAddedBy());

                                GridLayoutManager lLayout = new GridLayoutManager(ChurchDetailsActivity.this, 2);
                                rv_grid_layout.setLayoutManager(lLayout);
                                rv_grid_layout.setHasFixedSize(true);
                                rv_grid_layout.setNestedScrollingEnabled(false);
                                rv_grid_layout.removeItemDecoration(simpleItemDecoration);
                                rv_grid_layout.addItemDecoration(simpleItemDecoration);
                                rv_grid_layout.setAdapter(churchMembersGridAdapter);
                            } else {
                                rv_grid_layout.setAdapter(null);
                            }
//                            swipeRefresh.setRefreshing(false);

                        }
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
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
                                    new CustomDialog(ChurchDetailsActivity.this, null, "",
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
                        ChurchDetailMembersResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        ChurchDetailMembersResponse body1 = response.body();
                        new CustomDialog(ChurchDetailsActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ?
                                                body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<ChurchDetailMembersResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getChurchLibrary(String mCHURCH_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callLibrary = apiService.churchLibrary(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                mCHURCH_ID);

        callLibrary.enqueue(new Callback<LibraryListResponse>() {

            @Override
            public void onResponse(Call<LibraryListResponse> call, Response<LibraryListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        LibraryListResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allChurchDocuments = loginResponse.getData().getChurchDocument();
                            if (allChurchDocuments != null && allChurchDocuments.size() > 0) {
                                rvLibraryList.setAdapter(null);
                                churchLibraryAdapter = new ChurchLibraryAdapter(ChurchDetailsActivity.this,
                                        allChurchDocuments, ChurchDetailsActivity.this);

                                LinearLayoutManager lLayout = new LinearLayoutManager(ChurchDetailsActivity.this);
                                rvLibraryList.setLayoutManager(lLayout);
                                rvLibraryList.setHasFixedSize(true);
                                rvLibraryList.setNestedScrollingEnabled(false);
                                rvLibraryList.removeItemDecoration(simpleItemDecoration);
//                                rvLibraryList.addItemDecoration(simpleItemDecoration);
                                rvLibraryList.setAdapter(churchLibraryAdapter);
                            } else {
                                rv_grid_layout.setAdapter(null);
                            }

                        }
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
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
                                    new CustomDialog(ChurchDetailsActivity.this, null, "",
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
                        LibraryListResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        LibraryListResponse body1 = response.body();
                        new CustomDialog(ChurchDetailsActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ?
                                                body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<LibraryListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void joinChurchRequest(String mCHURCH_ID, String code) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callJoin = apiService.joinChurch(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                mCHURCH_ID);

        callJoin.enqueue(new Callback<JoinChurchResponse>() {

            @Override
            public void onResponse(Call<JoinChurchResponse> call, Response<JoinChurchResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        JoinChurchResponse loginResponse = response.body();
                        if (loginResponse.getSuccess().equalsIgnoreCase("success")) {
                            Toast.makeText(ChurchDetailsActivity.this, loginResponse.getMsg(), Toast.LENGTH_SHORT).show();

//                          After join change UI REF from setAllValuesOnUI method
                            tvJoin.setText("Leave");
                            bottomLinearNotJoin.setVisibility(View.GONE);
                            bottomLinearJoined.setVisibility(View.VISIBLE);
                            top_linear_joined.setVisibility(View.VISIBLE);
                            tvJoin.setBackgroundColor(Color.parseColor("#552A83"));
                            changeJoinedBottomColor(1);
                            showLayoutFor(1);

                            if (churchDetailResponse != null && churchDetailResponse.getData().getAllposts().size() > 0) {
                                HomeFeedsAdapter adapter = new HomeFeedsAdapter(ChurchDetailsActivity.this,
                                        churchDetailResponse.getData().getAllposts(), ChurchDetailsActivity.this, false);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChurchDetailsActivity.this);
                                rv_grid_layout.setLayoutManager(mLayoutManager);
                                rv_grid_layout.setItemAnimator(new DefaultItemAnimator());
                                rv_grid_layout.removeItemDecoration(simpleItemDecoration);
                                rv_grid_layout.addItemDecoration(simpleItemDecoration);
                                rv_grid_layout.setHasFixedSize(true);
                                rv_grid_layout.setNestedScrollingEnabled(false);
                                rv_grid_layout.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                rv_grid_layout.setAdapter(null);
                            }
                        }
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
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
                                    new CustomDialog(ChurchDetailsActivity.this, null, "",
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
                        JoinChurchResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        JoinChurchResponse body1 = response.body();
                        new CustomDialog(ChurchDetailsActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ?
                                                body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<JoinChurchResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void joinChurchByAccessCodeRequest(String mCHURCH_ID, String code) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callJoinByCode = apiService.joinChurchByAccessCode(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "",
                        ChurchDetailsActivity.this),
                mCHURCH_ID, code.trim());

        callJoinByCode.enqueue(new Callback<JoinByAccessCodeResponse>() {

            @Override
            public void onResponse(Call<JoinByAccessCodeResponse> call,
                                   Response<JoinByAccessCodeResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        JoinByAccessCodeResponse loginResponse = response.body();
                        if (loginResponse.getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(ChurchDetailsActivity.this,
                                    loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            tvMemberCount.setText(String.format("%d %s",
                                    loginResponse.getMemberCount(),
                                    getString(R.string.str_members)));

                            tvJoin.setText("Leave");
                            bottomLinearNotJoin.setVisibility(View.GONE);
                            bottomLinearJoined.setVisibility(View.VISIBLE);
                            top_linear_joined.setVisibility(View.VISIBLE);
                            tvJoin.setBackgroundColor(Color.parseColor("#552A83"));
                            changeJoinedBottomColor(1);
                            showLayoutFor(1);

                            if (mAllPostData != null && mAllPostData.size() > 0) {
                                HomeFeedsAdapter adapter = new HomeFeedsAdapter(
                                        ChurchDetailsActivity.this,
                                        mAllPostData, ChurchDetailsActivity.this,
                                        false);
                                RecyclerView.LayoutManager mLayoutManager =
                                        new LinearLayoutManager(ChurchDetailsActivity.this);
                                rv_grid_layout.setLayoutManager(mLayoutManager);
                                rv_grid_layout.setItemAnimator(new DefaultItemAnimator());
                                rv_grid_layout.removeItemDecoration(simpleItemDecoration);
                                rv_grid_layout.addItemDecoration(simpleItemDecoration);
                                rv_grid_layout.setHasFixedSize(true);
                                rv_grid_layout.setNestedScrollingEnabled(false);
                                rv_grid_layout.setAdapter(adapter);
                            } else {
                                rv_grid_layout.setAdapter(null);
                            }
                        } else {
                            CustomDialog customDialog1 = new CustomDialog(
                                    ChurchDetailsActivity.this, null,
                                    "", loginResponse.getMsg(),
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        }
                        break;
                }

            }

            @Override
            public void onFailure(Call<JoinByAccessCodeResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                        "", t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    private void refreshAccessCodeRequest(String mCHURCH_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callRefrashCode = apiService.refreshAccessCode(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "",
                        ChurchDetailsActivity.this),
                mCHURCH_ID);

        callRefrashCode.enqueue(new Callback<RefreshAccessCodeResponse>() {

            @Override
            public void onResponse(Call<RefreshAccessCodeResponse> call,
                                   Response<RefreshAccessCodeResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        RefreshAccessCodeResponse loginResponse = response.body();
                        edt_code.setText(Util.isNull(loginResponse.getAccessCode()));
                        CustomDialog customDialog1 = new CustomDialog(
                                ChurchDetailsActivity.this, null,
                                "", loginResponse.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        break;
                }

            }

            @Override
            public void onFailure(Call<RefreshAccessCodeResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                        "", t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    private void getChurchJoinedMembersPage(String mCHURCH_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callJoinRequest = apiService.getChurchRequestedMembers(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                mCHURCH_ID);

        callJoinRequest.enqueue(new Callback<ChurchJoinRequestedListResponse>() {

            @Override
            public void onResponse(Call<ChurchJoinRequestedListResponse> call,
                                   Response<ChurchJoinRequestedListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    ChurchJoinRequestedListResponse loginResponse = response.body();
                    if (loginResponse.getData() != null) {
                        allJoinMembersRequests = loginResponse.getData()
                                .getChurchUserDetails();
                        if (allJoinMembersRequests != null && allJoinMembersRequests.size() > 0) {
                            rv_grid_layout.setAdapter(null);
                            rv_grid_layout.removeAllViews();
                            ChurchMembersGridAdapter adapter = new ChurchMembersGridAdapter(
                                    ChurchDetailsActivity.this,
                                    allChurchJoinedMembers,
                                    ChurchDetailsActivity.this, false);
                            int spacingInPixels = getResources()
                                    .getDimensionPixelSize(R.dimen.dimen_five);
                            rv_grid_layout.removeItemDecoration(gridSpacingItemDecoration);
                            rv_grid_layout.addItemDecoration(gridSpacingItemDecoration);
                            GridLayoutManager lLayout = new GridLayoutManager(
                                    ChurchDetailsActivity.this, 2);
//                            rv_grid_layout.removeItemDecoration(simpleItemDecoration);
//                            rv_grid_layout.removeItemDecoration(simpleItemDecoration);
                            rv_grid_layout.setLayoutManager(lLayout);
                            rv_grid_layout.setHasFixedSize(true);
                            rv_grid_layout.setNestedScrollingEnabled(false);
                            rv_grid_layout.setAdapter(adapter);
                        } else {
                            rv_grid_layout.setAdapter(null);
                        }
//                            swipeRefresh.setRefreshing(false);

                    }

                } else {

                }

            }

            @Override
            public void onFailure(Call<ChurchJoinRequestedListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getJoinMembersRequest(String mCHURCH_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callJoinRequest = apiService.getChurchRequestedMembers(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                mCHURCH_ID);

        callJoinRequest.enqueue(new Callback<ChurchJoinRequestedListResponse>() {

            @Override
            public void onResponse(Call<ChurchJoinRequestedListResponse> call, Response<ChurchJoinRequestedListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    ChurchJoinRequestedListResponse loginResponse = response.body();
                    if (loginResponse.getData() != null) {
                        allJoinMembersRequests = loginResponse.getData()
                                .getChurchUserDetails();
                        if (allJoinMembersRequests != null && allJoinMembersRequests.size() > 0) {
                            rv_grid_layout.setAdapter(null);
                            rv_grid_layout.removeAllViews();
                            MembershipRequestListAdapter adapter = new MembershipRequestListAdapter(
                                    ChurchDetailsActivity.this,
                                    allChurchJoinedMembers,
                                    ChurchDetailsActivity.this);
                            LinearLayoutManager lLayout = new LinearLayoutManager(
                                    ChurchDetailsActivity.this);
                            rv_grid_layout.setLayoutManager(lLayout);
                            rv_grid_layout.setHasFixedSize(true);
                            rv_grid_layout.setAdapter(adapter);
                        } else {
                            rv_grid_layout.setAdapter(null);
                        }
//                            swipeRefresh.setRefreshing(false);

                    }

                } else {

                    CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                            null, "", getString(R.string.str_server_error),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<ChurchJoinRequestedListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getChurchChurchBlockedMembers(String mCHURCH_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callBlockedUserRequest = apiService.getChurchBlockedMembers(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                mCHURCH_ID);

        callBlockedUserRequest.enqueue(new Callback<ChurchBlockedUsersListResponse>() {

            @Override
            public void onResponse(Call<ChurchBlockedUsersListResponse> call, Response<ChurchBlockedUsersListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    ChurchBlockedUsersListResponse loginResponse = response.body();
                    if (loginResponse.getData() != null) {
                        mAllBlockedUsers = loginResponse.getData()
                                .getChurchUserDetails();
                        if (mAllBlockedUsers != null && mAllBlockedUsers.size() > 0) {
                            rv_grid_layout.setAdapter(null);
                            rv_grid_layout.removeAllViews();
                            ChurchBlockedUsersListAdapter adapter = new ChurchBlockedUsersListAdapter(ChurchDetailsActivity.this,
                                    mAllBlockedUsers,
                                    ChurchDetailsActivity.this);
                            LinearLayoutManager lLayout = new LinearLayoutManager(ChurchDetailsActivity.this);
                            rv_grid_layout.setLayoutManager(lLayout);
                            rv_grid_layout.setHasFixedSize(true);
                            rv_grid_layout.setAdapter(adapter);
                        } else {
                            rv_grid_layout.setAdapter(null);
                        }

                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", getResources().getString(R.string.str_server_error),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<ChurchBlockedUsersListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void leaveChurchRequest(String mCHURCH_ID) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callLeaveChurchRequest = apiService.leaveChurch(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                mCHURCH_ID);

        callLeaveChurchRequest.enqueue(new Callback<LeaveChurchResponse>() {

            @Override
            public void onResponse(Call<LeaveChurchResponse> call, Response<LeaveChurchResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));
                LeaveChurchResponse loginResponse = response.body();
                if (sCode.equalsIgnoreCase("200")) {

                    if (loginResponse != null) {
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                                ChurchDetailsActivity.this, "", loginResponse.getMsg(),
                                "CHURCH_LEFT");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                                null, "", getString(R.string.str_server_error),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", loginResponse.getMsg(),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<LeaveChurchResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void blockChurchMemberRequest(String mCHURCH_ID, String blockedUserid, int position) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callBlockChurchMenberJoin = apiService.blockChurchMembersMembers(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                mCHURCH_ID, blockedUserid);

        callBlockChurchMenberJoin.enqueue(new Callback<BlockChurchMemberResponse>() {

            @Override
            public void onResponse(Call<BlockChurchMemberResponse> call, Response<BlockChurchMemberResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        BlockChurchMemberResponse blockChurchMemberResponse = response.body();
                        if (blockChurchMemberResponse.getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(ChurchDetailsActivity.this, blockChurchMemberResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            if (allChurchMembers != null && allChurchMembers.size() > 0) {
                                allChurchMembers.remove(position);

                                rv_grid_layout.setAdapter(null);
                                ChurchMembersGridAdapter churchMembersGridAdapter = new ChurchMembersGridAdapter(ChurchDetailsActivity.this, allChurchMembers);

                                GridLayoutManager lLayout = new GridLayoutManager(ChurchDetailsActivity.this, 2);
                                rv_grid_layout.setLayoutManager(lLayout);
                                rv_grid_layout.setHasFixedSize(true);
                                rv_grid_layout.setNestedScrollingEnabled(false);
                                rv_grid_layout.removeItemDecoration(gridSpacingItemDecoration);
                                rv_grid_layout.addItemDecoration(gridSpacingItemDecoration); // fff
                                rv_grid_layout.setAdapter(churchMembersGridAdapter);
                            }
                        } else {
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                                    "", blockChurchMemberResponse.getMsg(),
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        }
                        break;
                    case "4":
                        if (sCode.equalsIgnoreCase("401")) {
                            BlockChurchMemberResponse body = response.body();
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                                    "", body.getMsg(),
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
                                    new CustomDialog(ChurchDetailsActivity.this, null, "",
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
                        BlockChurchMemberResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        BlockChurchMemberResponse body1 = response.body();
                        new CustomDialog(ChurchDetailsActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ?
                                                body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<BlockChurchMemberResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void deleteChurchMember(String mCHURCH_ID, String blockedUserid, int position) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callDeleteChurchMenber = apiService.deleteChurchMembersMembers(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                mCHURCH_ID, blockedUserid);

        callDeleteChurchMenber.enqueue(new Callback<DeleteChurchMemberResponse>() {

            @Override
            public void onResponse(Call<DeleteChurchMemberResponse> call, Response<DeleteChurchMemberResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        DeleteChurchMemberResponse blockChurchMemberResponse = response.body();
                        if (blockChurchMemberResponse.getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(ChurchDetailsActivity.this, blockChurchMemberResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            if (allChurchMembers != null && allChurchMembers.size() > 0) {
                                allChurchMembers.remove(position);

                                rv_grid_layout.setAdapter(null);
                                ChurchMembersGridAdapter churchMembersGridAdapter = new ChurchMembersGridAdapter(ChurchDetailsActivity.this, allChurchMembers);

                                GridLayoutManager lLayout = new GridLayoutManager(ChurchDetailsActivity.this, 2);
                                rv_grid_layout.setLayoutManager(lLayout);
                                rv_grid_layout.setHasFixedSize(true);
                                rv_grid_layout.setNestedScrollingEnabled(false);
                                rv_grid_layout.removeItemDecoration(gridSpacingItemDecoration);
                                rv_grid_layout.addItemDecoration(gridSpacingItemDecoration); // fff
                                rv_grid_layout.setAdapter(churchMembersGridAdapter);
                            }
                        } else {
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                                    "", blockChurchMemberResponse.getMsg(),
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        }
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
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
                                    new CustomDialog(ChurchDetailsActivity.this, null, "",
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
                        DeleteChurchMemberResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        DeleteChurchMemberResponse body1 = response.body();
                        new CustomDialog(ChurchDetailsActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ?
                                                body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<DeleteChurchMemberResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    /**
     * After Response set all values on UI elements
     *
     * @param data
     * @param churchDetailResponse
     */
    @SuppressLint("DefaultLocale")
    private void setAllValuesOnUI(Data data, ChurchDetailResponse churchDetailResponse) {
        Church mChurchData = data.getChurch();
        tvAddress.setText(mChurchData.getChurchAddress1());
        tvMemberCount.setText(String.format("%d %s", mChurchData.getChurchMembers(), getString(R.string.str_members)));
        tvChurchName.setText(mChurchData.getChurchName());
        tvPosterName.setText(mChurchData.getChurchPastorName());
        tvChurchAddress.setText(Util.isNull(mChurchData.getChurchAddress1()) + ", " +
                Util.isNull(mChurchData.getChurchCity()) + ", " +
                Util.isNull(mChurchData.getChurchState()) + ", " +
                Util.isNull(mChurchData.getChurchZip()) + ", " +
                Util.isNull(mChurchData.getChurchCountry())
        );
        if (mChurchData.getAddedBy().equalsIgnoreCase(Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this))) {
            iv_edit_cover.setVisibility(View.VISIBLE);
            tvJoin.setVisibility(View.GONE);
            BtnEditAbout.setVisibility(View.VISIBLE);
        } else {
            iv_edit_cover.setVisibility(View.GONE);
            BtnEditAbout.setVisibility(View.GONE);
        }
        edt_code.setText(Util.isNull(mChurchData.getAccessCode()));
        tvChurchPhone.setText(mChurchData.getChurchPhoneNumber());
        tvChurchDescription.setText(mChurchData.getChurchDescription());
        Glide.with(this)
                .load(mChurchData.getChurchImage())
                .placeholder(R.drawable.profile_def_user_image)
                .into(iv_church_image);

        if (data.getMemberStatus().equalsIgnoreCase("not_a_member")) {
            tvJoin.setText("Join");
            bottomLinearNotJoin.setVisibility(View.VISIBLE);
            bottomLinearJoined.setVisibility(View.GONE);
            top_linear_joined.setVisibility(View.GONE);
            tvJoin.setBackgroundColor(Color.parseColor("#552A83"));
            changeJoinedBottomColor(1);
            showLayoutFor(2);
            mAllPostData = churchDetailResponse.getData().getAllposts();
        } else {
            tvJoin.setText("Leave");
            bottomLinearNotJoin.setVisibility(View.GONE);
            bottomLinearJoined.setVisibility(View.VISIBLE);
            top_linear_joined.setVisibility(View.VISIBLE);
            tvJoin.setBackgroundColor(Color.parseColor("#552A83"));
            changeJoinedBottomColor(1);
            showLayoutFor(1);

            mAllPostData = churchDetailResponse.getData().getAllposts();
            if (mAllPostData != null && mAllPostData.size() > 0) {
                HomeFeedsAdapter adapter = new HomeFeedsAdapter(ChurchDetailsActivity.this,
                        mAllPostData, ChurchDetailsActivity.this, false);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                rv_grid_layout.setLayoutManager(mLayoutManager);
                rv_grid_layout.setItemAnimator(new DefaultItemAnimator());
                rv_grid_layout.removeItemDecoration(simpleItemDecoration);
                rv_grid_layout.addItemDecoration(simpleItemDecoration);
                rv_grid_layout.setHasFixedSize(true);
                rv_grid_layout.setNestedScrollingEnabled(false);
                rv_grid_layout.setAdapter(adapter);
            } else {
                rv_grid_layout.setAdapter(null);
            }
        }
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

    private void setUpBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(ChurchDetailsActivity.this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheets_main_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        hideSheetDialogButton = sheetView.findViewById(R.id.fragment_history_bottom_sheet_hide);
        editSheetDialogButton = sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
        deleteSheetDialogButton = sheetView.findViewById(R.id.fragment_history_bottom_sheet_del);
        favSheetDialogButton = sheetView.findViewById(R.id.fragment_history_bottom_sheet_fav);
        reportSheetDialogButton = sheetView.findViewById(R.id.fragment_history_bottom_sheet_report);
        editSheetDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                if (mForSheetPosition != -1) {

                    if (mAllPostData != null) {
                        Intent intent = new Intent(ChurchDetailsActivity.this, AddPostActivity.class);
                        intent.putExtra("POST_ID", mAllPostData.get(mForSheetPosition).getId());
                        intent.putExtra("COMING_FROM", "HOME");
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("data", mAllPostData.get(mForSheetPosition));
                        intent.putExtras(bundle);
                        mLastSelectedPostion = 0;
                        startActivity(intent);
                    }

                }
            }
        });

        deleteSheetDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete code here;
                mBottomSheetDialog.dismiss();
            }
        });
        favSheetDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete code here;
                mBottomSheetDialog.dismiss();
                if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {

                    if (mAllPostData != null) {
                        if (mForSheetUserId != null && mForSheetPosition != -1) {
                            favPost(mForSheetUserId, mForSheetPosition);
                        }
                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
            }
        });
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Do something
            }
        });

        hideSheetDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
                if (mAllPostData != null) {
                    if (mForSheetPosition != -1) {
                        hidePost(mAllPostData.get(mForSheetPosition).getId(), mForSheetPosition);
                    }
                }
            }
        });
    }

    private LinearLayout editSheetDialogButton = null, deleteSheetDialogButton = null,
            favSheetDialogButton = null, reportSheetDialogButton = null, hideSheetDialogButton = null;

    // Called from post adapter if user already member of of church
    @Override
    public void onOptionClickListener(Object postsItem, int position) {
        mForSheetUserId = Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this);
        mForSheetPosition = position;

        if (((String) postsItem).equalsIgnoreCase(mForSheetUserId)) {
            editSheetDialogButton.setVisibility(View.VISIBLE);
            hideSheetDialogButton.setVisibility(View.GONE);
            deleteSheetDialogButton.setVisibility(View.VISIBLE);
            favSheetDialogButton.setVisibility(View.VISIBLE);
            reportSheetDialogButton.setVisibility(View.GONE);
        } else {
            editSheetDialogButton.setVisibility(View.GONE);
            hideSheetDialogButton.setVisibility(View.VISIBLE);
            deleteSheetDialogButton.setVisibility(View.GONE);
            favSheetDialogButton.setVisibility(View.VISIBLE);
            reportSheetDialogButton.setVisibility(View.VISIBLE);
        }

        mBottomSheetDialog.findViewById(R.id.fragment_history_menu_bottom).setVisibility(View.VISIBLE);
        mBottomSheetDialog.show();
    }

    /**
     * On Member grid list on disable click listeners
     *
     * @param id
     * @param position
     */
    @Override
    public void onEditClick(String id, int position) {
        if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
            blockChurchMemberRequest(mCHURCH_ID, id, position);
        } else {
            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    /**
     * On Member grid list on delete click listeners
     *
     * @param id
     * @param position
     */
    @Override
    public void onDeleteClick(String id, int position) {
        if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
            deleteChurchMember(mCHURCH_ID, id, position);
        } else {
            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    /**
     * On Accept membership Request adapter click listener
     *
     * @param id
     * @param position
     */
    @Override
    public void onAcceptReq(String id, int position) {
        Toast.makeText(this, getString(R.string.str_under_progress),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * On Reject membership Request adapter click listener
     *
     * @param id
     * @param position
     */
    @Override
    public void onRejectReq(String id, int position) {
        Toast.makeText(this, getString(R.string.str_under_progress),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Called from church blocked user list adapter unblock user button
     *
     * @param id
     * @param position
     */
    @Override
    public void onUnblockClick(String id, int position) {
        Toast.makeText(this, getString(R.string.str_under_progress),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_edit_cover:
                if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
                    initSelectImage();
                } else {
                    CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
                break;
            case R.id.btn_attach:
                if (edt_tittle.getText().toString().length() > 0) {
                    showFileChooser();
                } else {
                    Toast.makeText(ChurchDetailsActivity.this, "Document tittle should not be empty", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    /**
     * Select Camera Image task performed below
     */
    private void initSelectImage() {
        String permissionAsk[] = {PermissionUtils.Manifest_CAMERA,
                PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};
        askCompactPermissions(permissionAsk, new PermissionResult() {
            @Override
            public void permissionGranted() {
                try {
                    selectImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void permissionDenied() {
                Snackbar snackbar = Snackbar
                        .make(main_root, "Permission needed access camera and storage!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Allow", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                initSelectImage();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }

            @Override
            public void permissionForeverDenied() {
                Snackbar snackbar = Snackbar
                        .make(main_root, "To use this feature requires permission please allow from setting" +
                                "please_try_again_later", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.str_takephotos), getResources().getString(R.string.str_choosefromphotos),
                getResources().getString(R.string.bt_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(ChurchDetailsActivity.this,
                R.style.MyAlertDialogStyle);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.str_takephotos))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Util.REQUEST_CAMERA);
                } else if (items[item].equals(getResources().getString(R.string.str_choosefromphotos))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            Util.SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String mPath;
        if (requestCode == Util.REQUEST_CAMERA && resultCode == RESULT_OK) {
            if (null != data && data.getExtras().containsKey("data")) {
                Bitmap thumbnail;
                thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert thumbnail != null;
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPath = destination.getAbsolutePath();

//                if (null != iv_logo && null != thumbnail) {
//                    iv_logo.setImageBitmap(thumbnail);
                this.mFilePath = mPath;

                if (mFilePath != null && mFilePath.length() > 0) {
                    File file = new File(mFilePath);
                    updateCoverImage(file, "image", mCHURCH_ID);
                } else {
                    updateCoverImage(null, "image", mCHURCH_ID);
                }
            }
        } else if (requestCode == Util.SELECT_FILE && resultCode == RESULT_OK) {
            if (data != null) {

                Uri selectedImageUri = data.getData();
                if (selectedImageUri.toString().startsWith("content://com.sec.android.gallery3d.provider")) {
                    Toast.makeText(getApplicationContext(), "not able to read Picasa images", Toast.LENGTH_SHORT).show();
                } else if (selectedImageUri.toString().startsWith("content://com.google.android.apps.photos.contentprovider")) {
                    Uri mTempPath = getImageUrlWithAuthority(ChurchDetailsActivity.this, selectedImageUri);
                    mFilePath = getRealPathFromURI(mTempPath);
//                    iv_image_prev.setImageBitmap(bm);
//                    tv_type.setText("IMAGE");
//                    mUploadType = "IMAGE";

                } else if (selectedImageUri.toString().startsWith("content://com.android.providers.media.documents/document")) {
//                content://com.android.providers.media.documents/document/image%3A13
                    mFilePath = getPath(ChurchDetailsActivity.this, selectedImageUri);
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(mFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                        Log.d("EXIF", "Exif: " + orientation);
                    } else if (orientation == 3) {
                        matrix.postRotate(180);
                        Log.d("EXIF", "Exif: " + orientation);
                    } else if (orientation == 8) {
                        matrix.postRotate(270);
                        Log.d("EXIF", "Exif: " + orientation);
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(mFilePath, options);
                    final int REQUIRED_SIZE = 200;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    Bitmap thumbnail = BitmapFactory.decodeFile(mFilePath, options);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
//                        mProfileImg.setImageBitmap(rotatedBitmap);
                    bmp = rotatedBitmap;

                    rotatedBitmap = null;
//                    if (bm!=null) {
//                        iv_image_prev.setImageBitmap(bmp);
//                        tv_type.setText("IMAGE");
//                        mUploadType = "IMAGE";
//                    }
                } else {
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    CursorLoader cursorLoader = new CursorLoader(ChurchDetailsActivity.this, selectedImageUri, projection, null, null,
                            null);
                    Cursor cursor = cursorLoader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    String selectedImagePath = cursor.getString(column_index);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(selectedImagePath, options);
                    final int REQUIRED_SIZE = 200;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    try {
                        mFilePath = selectedImagePath;
                        mFilePath = getPath(ChurchDetailsActivity.this, selectedImageUri);
                        ExifInterface exif = new ExifInterface(selectedImagePath);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                            Log.d("EXIF", "Exif: " + orientation);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                            Log.d("EXIF", "Exif: " + orientation);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                            Log.d("EXIF", "Exif: " + orientation);
                        }
//                        Bitmap thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);
//                        Bitmap rotatedBitmap = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
//                        bmp = rotatedBitmap;
//
//                        rotatedBitmap = null;
//                        if (bmp!=null) {
//                            iv_image_prev.setImageBitmap(bmp);
//                            tv_type.setText("IMAGE");
//                        }else{
                        mFilePath = getPath(ChurchDetailsActivity.this, selectedImageUri);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (mFilePath != null && mFilePath.length() > 0) {
                    File file = new File(mFilePath);
                    updateCoverImage(file, "image", mCHURCH_ID);
                } else {
                    updateCoverImage(null, "image", mCHURCH_ID);
                }
            }
        } else if (requestCode == FILE_CHOOSER && resultCode == RESULT_OK) {
            String FilePath = data.getData().getPath();
            if (FilePath.length() != 0) {
                Uri selectedUri = data.getData();
                FilePath = mFilePath = getPath(ChurchDetailsActivity.this, selectedUri);//data.getData().getPath();
                String FileName = data.getData().getLastPathSegment();
                edt_upload_tittle.setText(FileName.trim());
                File file = new File(FilePath);
                if (file.exists()) {
                    updateLibraryDoc(file, "doc", mCHURCH_ID);
                }
            } else {
                Toast.makeText(this, "document not picked please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = ChurchDetailsActivity.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        Bitmap bmp = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
//                    bmp.recycle();
                    bmp = null;
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * @param file
     * @param fileType
     */
    private void updateCoverImage(File file, String fileType, String churchId) {
        showProgressDialog();

        MultipartBody.Part body = null;
        if (file != null && file.exists()) {
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(fileType),
                            file
                    );
            // MultipartBody.Part is used to send also the actual file name
            body =
                    MultipartBody.Part.createFormData("coverphotofile", file.getName(), requestFile);
        }
        // create a map of data to pass along
        RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
        RequestBody user_id = Util.createPartFromString(""
                + Util.loadPrefrence(Util.PREF_USER_ID, "",
                ChurchDetailsActivity.this));
        RequestBody church_id = Util.createPartFromString(churchId);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("church_id", church_id);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        // finally, execute the request
        Call<AddCoverPhotoResponse> call = apiService.
                addChurchCoverPhoto(
                        map,
                        body);

        call.enqueue(new Callback<AddCoverPhotoResponse>() {

            @Override
            public void onResponse(Call<AddCoverPhotoResponse> call,
                                   Response<AddCoverPhotoResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                    String sCode = response.code() + "";
                    String c = String.valueOf(sCode.charAt(0));
                    switch (c) {
                        case "2": // TODO Success response  task here and progress loader
                            AddCoverPhotoResponse cityListResponse = response.body();
                            if (cityListResponse.getStatuscode().equalsIgnoreCase("200")) {
                                CustomDialog customDialog = new CustomDialog(
                                        ChurchDetailsActivity.this, ChurchDetailsActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "SUCCESS");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            } else {
                                CustomDialog customDialog = new CustomDialog(
                                        ChurchDetailsActivity.this, ChurchDetailsActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "ONFAILED");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            }

                            break;
                        case "5": // TODO Server Error and display retry

                            hideProgressDialog();
                            AddCoverPhotoResponse businessResponse1 = response.body();
                            CustomDialog customDialog = new CustomDialog(ChurchDetailsActivity.this, null,
                                    "", businessResponse1.getMsg(),
                                    "ONFAILED");
                            if (customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            customDialog.show();

                        case "4": // TODO Server Error and display retry

                            hideProgressDialog();
                            if (sCode == "401") {
//                                Util.showDialog(ChurchDetailsActivity.this,
//                                        (IAlertDialogCallback) ChurchDetailsActivity.this,
//                                        getResources().getString(R.string.str_authfailed),
//                                        getResources().getString(R.string.str_authfaileddetail));
                            } else {
                                AddCoverPhotoResponse businessResponse2 = response.body();
                                CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                                        "", businessResponse2.getMsg(),
                                        "ONFAILED");
                                if (customDialog1.isShowing()) {
                                    customDialog1.dismiss();
                                }
                                customDialog1.show();
                            }
                            break;
                        default: // TODO Handle error message and show dialog here
                            AddCoverPhotoResponse businessResponse3 = response.body();
                            CustomDialog customDialog2 = new CustomDialog(ChurchDetailsActivity.this, null,
                                    "", businessResponse3.getMsg(),
                                    "ONFAILED");
                            if (customDialog2.isShowing()) {
                                customDialog2.dismiss();
                            }
                            customDialog2.show();
                            hideProgressDialog();
                            break;

                    }

                } else {
                    Log.d("ask", response.toString());
                    CustomDialog customDialog2 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", response.message(),
                            "ONFAILED");
                    if (customDialog2.isShowing()) {
                        customDialog2.dismiss();
                    }
                    customDialog2.show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<AddCoverPhotoResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());

                new CustomDialog(ChurchDetailsActivity.this, null, "", t.getMessage(),
                        "ONFAILED").show();
                hideProgressDialog();
            }
        });
    }

    private void likePost(String postId, final int position) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        callLikePost = apiService.likePostRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                postId);

        callLikePost.enqueue(new Callback<LikePostResponse>() {

            @Override
            public void onResponse(Call<LikePostResponse> call, Response<LikePostResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        LikePostResponse likePostResponse = response.body();
                        Toast.makeText(ChurchDetailsActivity.this, "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();

//                        if (mAllPostData != null) {
//                            if (mAllPostData.get(position).isFavLocal()) {
//                                mAllPostData.get(position).setFavLocal(false);
//                            } else {
//                                mAllPostData.get(position).setFavLocal(true);
//                            }
//                        }

                        rv_grid_layout.getAdapter().notifyItemChanged(position);
//                        rv_grid_layout.getAdapter().notifyDataSetChanged();
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
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
                                    new CustomDialog(ChurchDetailsActivity.this, null, "",
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
                        LikePostResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        LikePostResponse body1 = response.body();
                        new CustomDialog(ChurchDetailsActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ?
                                                body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<LikePostResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });

    }

    private void favPost(String postId, final int position) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        callFavPost = apiService.changeFavoriteUnfav(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                postId);

        callFavPost.enqueue(new Callback<FavouriteUnfavResponse>() {

            @Override
            public void onResponse(Call<FavouriteUnfavResponse> call, Response<FavouriteUnfavResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        FavouriteUnfavResponse likePostResponse = response.body();
                        Toast.makeText(ChurchDetailsActivity.this, "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();


                        if (mAllPostData != null) {
                            if (mAllPostData.get(position).isFavLocal()) {
                                mAllPostData.get(position).setFavLocal(false);
                            } else {
                                mAllPostData.get(position).setFavLocal(true);
                            }
                        }

                        rv_grid_layout.getAdapter().notifyItemChanged(position);
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
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
                                    new CustomDialog(ChurchDetailsActivity.this, null, "",
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
                        FavouriteUnfavResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        FavouriteUnfavResponse body1 = response.body();
                        new CustomDialog(ChurchDetailsActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ?
                                                body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<FavouriteUnfavResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                        "", "" + t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });

    }

    private void hidePost(String postId, final int position) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        callHidePost = apiService.hidePost(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this),
                postId);

        callHidePost.enqueue(new Callback<HidePostResponse>() {

            @Override
            public void onResponse(Call<HidePostResponse> call, Response<HidePostResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        HidePostResponse likePostResponse = response.body();
                        Toast.makeText(ChurchDetailsActivity.this, "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        if (mAllPostData != null) {
                            mAllPostData.remove(position);
                        }
                        rv_grid_layout.getAdapter().notifyItemRemoved(position);
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
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
                                    new CustomDialog(ChurchDetailsActivity.this, null, "",
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
                        HidePostResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        HidePostResponse body1 = response.body();
                        new CustomDialog(ChurchDetailsActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ?
                                                body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<HidePostResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(ChurchDetailsActivity.this, null, "",
                        t.getMessage() != null ?
                                t.getMessage().length() > 0 ?
                                        t.getMessage()
                                        : "Something went wrong" : "Something went wrong",
                        "ONFAILED").show();
            }
        });

    }

    @Override
    public void onEditLibraryClick(String url, String fileName, String type) {
        Intent intent = new Intent(this, DownloadFileFromURL.class);
        intent.putExtra("URL","document/church/1508188440_59e4f7cf727055063778b5fd.pdf"); // "document/church/1508188440_59e4f7cf727055063778b5fd.pdf"
        intent.putExtra("NAME",fileName);
        intent.putExtra("TYPE",type);
        startService(intent);
    }

    @Override
    public void onDeleteDocumentClick(String deleteLibraryId, int positionToRemove) {
        if (Util.checkNetworkAvailablity(ChurchDetailsActivity.this)) {
            deleteLibraryDoc(deleteLibraryId, positionToRemove);
        } else {
            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    private void showFileChooser() {

        String[] mimeTypes =
                {"application/pdf", "application/doc"}; // ,"application/vnd.ms-powerpoint","application/vnd.ms-excel"

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), FILE_CHOOSER);

    }

    /**
     * @param file
     * @param fileType
     */
    private void updateLibraryDoc(File file, String fileType, String churchId) {
        showProgressDialog();

        MultipartBody.Part body = null;
        if (file != null && file.exists()) {
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(fileType),
                            file
                    );
            // MultipartBody.Part is used to send also the actual file name
            body =
                    MultipartBody.Part.createFormData("documentfile", file.getName(), requestFile);
        }
        // create a map of data to pass along
        RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
        RequestBody documentName = Util.createPartFromString(edt_upload_tittle.getText().toString());
        RequestBody user_id = Util.createPartFromString(""
                + Util.loadPrefrence(Util.PREF_USER_ID, "",
                ChurchDetailsActivity.this));
        RequestBody church_id = Util.createPartFromString(churchId);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("church_id", church_id);
        map.put("documentName", documentName);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        // finally, execute the request
        Call<AddLibraryResponse> call = apiService.
                addChurchLibraryFile(
                        map,
                        body);

        call.enqueue(new Callback<AddLibraryResponse>() {

            @Override
            public void onResponse(Call<AddLibraryResponse> call,
                                   Response<AddLibraryResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                    String sCode = response.code() + "";
                    String c = String.valueOf(sCode.charAt(0));
                    switch (c) {
                        case "2": // TODO Success response  task here and progress loader
                            AddLibraryResponse cityListResponse = response.body();
                            if (cityListResponse.getStatuscode().equalsIgnoreCase("200")) {
                                CustomDialog customDialog = new CustomDialog(
                                        ChurchDetailsActivity.this, ChurchDetailsActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "SUCCESS");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                                getChurchLibrary(mCHURCH_ID);
                            } else {
                                CustomDialog customDialog = new CustomDialog(
                                        ChurchDetailsActivity.this, ChurchDetailsActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "ONFAILED");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            }

                            break;
                        case "5": // TODO Server Error and display retry

                            hideProgressDialog();
                            AddLibraryResponse businessResponse1 = response.body();
                            CustomDialog customDialog = new CustomDialog(ChurchDetailsActivity.this, null,
                                    "", businessResponse1.getMsg(),
                                    "ONFAILED");
                            if (customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            customDialog.show();

                        case "4": // TODO Server Error and display retry

                            hideProgressDialog();
                            if (sCode == "401") {
//                                Util.showDialog(ChurchDetailsActivity.this,
//                                        (IAlertDialogCallback) ChurchDetailsActivity.this,
//                                        getResources().getString(R.string.str_authfailed),
//                                        getResources().getString(R.string.str_authfaileddetail));
                            } else {
                                AddLibraryResponse businessResponse2 = response.body();
                                CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                                        "", businessResponse2.getMsg(),
                                        "ONFAILED");
                                if (customDialog1.isShowing()) {
                                    customDialog1.dismiss();
                                }
                                customDialog1.show();
                            }
                            break;
                        default: // TODO Handle error message and show dialog here
                            AddLibraryResponse businessResponse3 = response.body();
                            CustomDialog customDialog2 = new CustomDialog(ChurchDetailsActivity.this, null,
                                    "", businessResponse3.getMsg(),
                                    "ONFAILED");
                            if (customDialog2.isShowing()) {
                                customDialog2.dismiss();
                            }
                            customDialog2.show();
                            hideProgressDialog();
                            getChurchLibrary(mCHURCH_ID);
                            break;

                    }

                } else {
                    Log.d("ask", response.toString());
                    CustomDialog customDialog2 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", response.message(),
                            "ONFAILED");
                    if (customDialog2.isShowing()) {
                        customDialog2.dismiss();
                    }
                    customDialog2.show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<AddLibraryResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());

                new CustomDialog(ChurchDetailsActivity.this, null, "", t.getMessage(),
                        "ONFAILED").show();
                hideProgressDialog();
            }
        });
    }

    /**
     * @param positionToRemove
     */
    private void deleteLibraryDoc(String library_id, int positionToRemove) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        // finally, execute the request
        Call<AddLibraryResponse> call = apiService.
                deleteLibraryFile(BuildConfig.API_KEY,
                        Util.loadPrefrence(Util.PREF_USER_ID, "",
                                ChurchDetailsActivity.this),
                        library_id);

        call.enqueue(new Callback<AddLibraryResponse>() {

            @Override
            public void onResponse(Call<AddLibraryResponse> call,
                                   Response<AddLibraryResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                    String sCode = response.code() + "";
                    String c = String.valueOf(sCode.charAt(0));
                    switch (c) {
                        case "2": // TODO Success response  task here and progress loader
                            AddLibraryResponse cityListResponse = response.body();
                            if (cityListResponse.getStatuscode().equalsIgnoreCase("200")) {
                                CustomDialog customDialog = new CustomDialog(
                                        ChurchDetailsActivity.this, ChurchDetailsActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "SUCCESS");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                                if (allChurchDocuments != null)
                                    allChurchDocuments.remove(positionToRemove);
                                if (churchLibraryAdapter != null)
                                    churchLibraryAdapter.notifyItemRemoved(positionToRemove);
//                                getChurchLibrary(mCHURCH_ID);
                            } else {
                                CustomDialog customDialog = new CustomDialog(
                                        ChurchDetailsActivity.this, ChurchDetailsActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "ONFAILED");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            }

                            break;
                        case "5": // TODO Server Error and display retry

                            hideProgressDialog();
                            AddLibraryResponse businessResponse1 = response.body();
                            CustomDialog customDialog = new CustomDialog(ChurchDetailsActivity.this, null,
                                    "", businessResponse1.getMsg(),
                                    "ONFAILED");
                            if (customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            customDialog.show();

                        case "4": // TODO Server Error and display retry

                            hideProgressDialog();
                            if (sCode == "401") {
//                                Util.showDialog(ChurchDetailsActivity.this,
//                                        (IAlertDialogCallback) ChurchDetailsActivity.this,
//                                        getResources().getString(R.string.str_authfailed),
//                                        getResources().getString(R.string.str_authfaileddetail));
                            } else {
                                AddLibraryResponse businessResponse2 = response.body();
                                CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null,
                                        "", businessResponse2.getMsg(),
                                        "ONFAILED");
                                if (customDialog1.isShowing()) {
                                    customDialog1.dismiss();
                                }
                                customDialog1.show();
                            }
                            break;
                        default: // TODO Handle error message and show dialog here
                            AddLibraryResponse businessResponse3 = response.body();
                            CustomDialog customDialog2 = new CustomDialog(ChurchDetailsActivity.this, null,
                                    "", businessResponse3.getMsg(),
                                    "ONFAILED");
                            if (customDialog2.isShowing()) {
                                customDialog2.dismiss();
                            }
                            customDialog2.show();
                            hideProgressDialog();
                            getChurchLibrary(mCHURCH_ID);
                            break;

                    }

                } else {
                    Log.d("ask", response.toString());
                    CustomDialog customDialog2 = new CustomDialog(ChurchDetailsActivity.this, null,
                            "", response.message(),
                            "ONFAILED");
                    if (customDialog2.isShowing()) {
                        customDialog2.dismiss();
                    }
                    customDialog2.show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<AddLibraryResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());

                new CustomDialog(ChurchDetailsActivity.this, null, "", t.getMessage(),
                        "ONFAILED").show();
                hideProgressDialog();
            }
        });
    }

    private void fileDownload(String fileUrl,String name) {
        String TAG = "Event Detail fileDownload";
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.downloadFileWithDynamicUrlSync(fileUrl);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(),name);

                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body,String name) {
        try {

            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Church_"+mCHURCH_ID+"_"+name);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("Completed", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
    private void getEventsDetailAboutRequest() {
        showProgressDialog();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String userId = Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this);
        callProfileFriends = apiService.profileFriendRequestList(BuildConfig.API_KEY, userId, userId);

        callProfileFriends.enqueue(new Callback<ProfileFriendsResponse>() {

            @Override
            public void onResponse(Call<ProfileFriendsResponse> call,
                                   Response<ProfileFriendsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        ProfileFriendsResponse profileFriendsResponse = response.body();
                        if (profileFriendsResponse.getData() != null && profileFriendsResponse.getStatus().equalsIgnoreCase("success")) {
                            ProfileAdapter = new ProfileFriendsAddEventAdapter(ChurchDetailsActivity.this, profileFriendsResponse.getData().getFriends());

                            LinearLayoutManager lLayout = new LinearLayoutManager(ChurchDetailsActivity.this);
                            recyclerView.setLayoutManager(lLayout);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(ProfileAdapter);
                        } else {
                            recyclerView.setAdapter(null);
                            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null, "", profileFriendsResponse.getMsg(), "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProfileFriendsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    public void SendInviteToServer()
    {
        ArrayList<String> AllFriends;
        AllFriends = ProfileAdapter.getAllCheckedBox();

        if(AllFriends.size() == 0)
        {
            CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null, "", "Please select friend to send invite", "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
        else
        {
            showProgressDialog();

            Gson gson = new Gson();
            String FrondJsonString = gson.toJson(AllFriends).toString();

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            String userId = Util.loadPrefrence(Util.PREF_USER_ID, "", ChurchDetailsActivity.this);
            callInviteUsers = apiService.churchSendInvite(BuildConfig.API_KEY, userId, mCHURCH_ID, FrondJsonString);

            callInviteUsers.enqueue(new Callback<DeleteChurchMemberResponse>() {

                @Override
                public void onResponse(Call<DeleteChurchMemberResponse> call, Response<DeleteChurchMemberResponse> response) {
                    hideProgressDialog();
                    if (response.body() != null) {
                        Log.d("nik", response.body().toString());
                    }
                    String sCode = response.code() + "";
                    String c = String.valueOf(sCode.charAt(0));


                    switch (c) {
                        case "2":
                            DeleteChurchMemberResponse loginResponse = response.body();
                            if (loginResponse.getStatus().equalsIgnoreCase("success")) {
//                            Toast.makeText(AddEventActivity.this, loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                CustomDialog customDialog = new CustomDialog(ChurchDetailsActivity.this,
                                        "Invite Friends", loginResponse.getMsg(), ChurchDetailsActivity.this);
                                customDialog.show();

//                            CustomDialog customDialog = new CustomDialog(AddEventActivity.this,
//                                    "Thank you for creating Event",
//                                    "Lorem Ipsum is simply dummy text of the printing and typesetting " +
//                                            "industry. " +
//                                            "Lorem Ipsum has been the industry's standard dummy text ever" +
//                                            " since the 1500s, when an unknown printer took a galley of" +
//                                            " type and scrambled it to make a type specimen book.",
//                                    AddEventActivity.this);
//                            customDialog.show();
                            } else {
                                CustomDialog customDialog1 = new CustomDialog(ChurchDetailsActivity.this, null, "", loginResponse.getMsg(), "ONFAILED");
                                if (customDialog1.isShowing()) {
                                    customDialog1.dismiss();
                                }
                                customDialog1.show();
                            }
                            break;
                    }
                }

                @Override
                public void onFailure(Call<DeleteChurchMemberResponse> call, Throwable t) {
                    Log.d("nik", t.getMessage());
                }
            });
        }
    }

}
