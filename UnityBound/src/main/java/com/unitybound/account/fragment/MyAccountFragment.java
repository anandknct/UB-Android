package com.unitybound.account.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.thebrownarrow.permissionhelper.FragmentManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.activity.AddPostActivity;
import com.unitybound.account.activity.MyAccountAboutActivity;
import com.unitybound.account.activity.MyAccountFrndsActivity;
import com.unitybound.account.activity.MyAccountMyPhotosActivity;
import com.unitybound.account.beans.coverUpdate.CoverPicUpdateResponse;
import com.unitybound.account.beans.hidePost.HidePostResponse;
import com.unitybound.account.beans.timeline.AllpostsItem;
import com.unitybound.account.beans.timeline.ProfileAboutData;
import com.unitybound.account.beans.timeline.TimelineResponse;
import com.unitybound.account.beans.timeline.UserInfo;
import com.unitybound.account.listner.NavigationFromProfileListener;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.main.home.fragment.activity.FeedsCommentActivity;
import com.unitybound.main.home.fragment.adapter.HomeFeedsAdapter;
import com.unitybound.main.home.fragment.beans.favUnfav.FavouriteUnfavResponse;
import com.unitybound.main.home.fragment.beans.like.LikePostResponse;
import com.unitybound.main.home.fragment.beans.prayerAnswer.PrayerAnswerResponse;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.SimpleDividerItemDecoration;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;
import com.unitybound.weddings.activity.WeddingsDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by @author nikhil.jogdand on 10/05/17.
 */

public class MyAccountFragment extends FragmentManagePermission implements
        HomeFeedsAdapter.IListAdapterCallback, View.OnClickListener, CustomDialog.IDialogListener {

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
    @BindView(R.id.iv_edit_user_image)
    ImageView ivEditUserImage;
    @BindView(R.id.iv_edit_cover)
    ImageView ivEditCoverImage;
    RecyclerView recyclerView;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.coordinator_main)
    CoordinatorLayout coordinatorMainLayout;

    Unbinder unbinder;
    private ArrayList<FriendRequestData> datalist =
            new ArrayList<FriendRequestData>();
    private BottomSheetDialog mBottomSheetDialog = null;
    private FloatingActionButton addPost = null;
    private ProgressDialog mProgressDialog = null;
    private Call<TimelineResponse> callTimeline = null;
    private ArrayList<AllpostsItem> allposts = null;
    private UserInfo mUserData = null;
    private String mFilePath = "";
    private Bitmap bmp = null;
    private NavigationFromProfileListener iNavigationListnerToChangeScreen = null;
    private Call<LikePostResponse> callLikePost = null;
    @BindView(R.id.fragment_history_bottom_sheet_hide)
    LinearLayout hideSheetLayout = null;
    @BindView(R.id.fragment_history_bottom_sheet_report)
    LinearLayout reportSheetLayout = null;
    @BindView(R.id.fragment_history_bottom_sheet_edit)
    LinearLayout editSheetLayout = null;
    @BindView(R.id.fragment_history_bottom_sheet_fav)
    LinearLayout favSheetLayout = null;
    @BindView(R.id.fragment_history_bottom_sheet_del)
    LinearLayout deleteSheetLayout = null;
    private String mPostId4Answer = "";
    private String mForSheetUserId = null;
    private int mForSheetPosition = -1;
    private LinearLayout editSheetDialogButton = null, deleteSheetDialogButton = null,
            favSheetDialogButton = null, reportSheetDialogButton = null, hideSheetDialogButton = null;
    private Call<FavouriteUnfavResponse> callFavPost = null;
    private String mSelectedUserProfileId = null;
    private ProfileAboutData mProfileAboutData = null;
    private Call<HidePostResponse> callHidePost = null;
    private int mIMAGE_TYPE = -1;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (getActivity() instanceof NavigationFromProfileListener) {
            iNavigationListnerToChangeScreen = (NavigationFromProfileListener) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_accounts, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView(view);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null && getArguments().containsKey("SelectedUserProfileId")) {
            mSelectedUserProfileId = getArguments().getString("SelectedUserProfileId");
            ivEditCoverImage.setVisibility(View.GONE);
            ivEditUserImage.setVisibility(View.GONE);
            addPost.setVisibility(View.GONE);
            ivMore.setVisibility(View.INVISIBLE);
        } else {
            ivEditCoverImage.setVisibility(View.VISIBLE);
            ivEditUserImage.setVisibility(View.VISIBLE);
            addPost.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (mSelectedUserProfileId == null) {
                getTimeline(null);
            } else {
                getTimeline(mSelectedUserProfileId);
            }
        } else {
            CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    private void initView(View view) {
        mProgressDialog = new ProgressDialog(getActivity(), R.style.newDialog);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_layout);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity()));

        addPost = (FloatingActionButton) view.findViewById(R.id.fab_create_post);
        addPost.setOnClickListener(this);
        setUpBottomSheet();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    private void setUpBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheets_main_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        ButterKnife.bind(mBottomSheetDialog, sheetView);

        hideSheetDialogButton = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_hide);
        editSheetDialogButton = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
        deleteSheetDialogButton = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_del);
        favSheetDialogButton = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_fav);
        reportSheetDialogButton = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_report);

        hideSheetDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
                if (allposts != null) {
                    if (mForSheetPosition != -1) {
                        hidePost(allposts.get(mForSheetPosition).getId(),mForSheetPosition);
                    }
                }
            }
        });

        editSheetDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                if (allposts != null) {
                    if (mForSheetPosition != -1) {
                        Intent intent = new Intent(getActivity(), AddPostActivity.class);
                        intent.putExtra("POST_ID", allposts.get(mForSheetPosition).getId());
                        intent.putExtra("COMING_FROM", "PROFILE");
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("data", allposts.get(mForSheetPosition));
                        intent.putExtras(bundle);
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
                if (Util.checkNetworkAvailablity(getActivity())) {
                    if (allposts != null) {
                        if (mForSheetUserId != null && mForSheetPosition != -1) {
                            favPost(mForSheetUserId, mForSheetPosition);
                        }
                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
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
    }

    @Override
    public void onItemClickListner(String s, int position) {
        Intent intent = new Intent(getActivity(), WeddingsDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCommentClickListner(String postId, int position) {
        Intent intent = new Intent(getActivity(), FeedsCommentActivity.class);
        if (postId != null) {
            intent.putExtra("POST_ID", postId);
        }
        startActivity(intent);
    }

    @Override
    public void onOptionClickListener(Object s, int position) {
        if (allposts != null) {
            mForSheetUserId = Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity());
            mForSheetPosition = position;
            if (s instanceof AllpostsItem) {
                if (allposts.get(position).getPostBy().getId().equalsIgnoreCase(mForSheetUserId)) {
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
            }
            mBottomSheetDialog.findViewById(R.id.fragment_history_menu_bottom).setVisibility(View.VISIBLE);
            mBottomSheetDialog.show();
        }
    }

    @Override
    public void onUserNameClickListner(String s, int position) {
        Intent intent = new Intent(getActivity(), MyAccountAboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLikeClickListner(Object postsItem, int position) {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (postsItem instanceof com.unitybound.account.beans.timeline.AllpostsItem) {
                String mPostId = ((com.unitybound.account.beans.timeline.AllpostsItem) postsItem).getId();
                likePost(mPostId, position);
            }
        } else {
            CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
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
        if (Util.checkNetworkAvailablity(getActivity())) {
            mPostId4Answer = (String) postsItem;
            new CustomDialog(getActivity(), MyAccountFragment.this, 54545).show();
        } else {
            CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    @Override
    public void onBookmarkClickListener(Object postsItem, int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rr_user_image, R.id.ic_cover, R.id.tv_user_name, R.id.tv_timeline,
            R.id.tv_about, R.id.tv_friends, R.id.tv_photos, R.id.iv_more,
            R.id.iv_edit_cover, R.id.iv_edit_user_image,

            R.id.fragment_history_bottom_sheet_hide,
            R.id.fragment_history_bottom_sheet_report,
            R.id.fragment_history_bottom_sheet_edit,
            R.id.fragment_history_bottom_sheet_fav,
            R.id.fragment_history_bottom_sheet_del})
    public void onViewClicked(View view) {
        Log.e("", "");
        switch (view.getId()) {
            case R.id.iv_edit_cover:
                if (Util.checkNetworkAvailablity(getActivity())) {
                    mIMAGE_TYPE = 0;
                    initSelectImage();
                } else {
                    CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
                break;
            case R.id.iv_edit_user_image:
                if (Util.checkNetworkAvailablity(getActivity())) {
                    mIMAGE_TYPE = 1;
                    initSelectImage();
                } else {
                    CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
                break;
            case R.id.rr_user_image:
                if (mUserData != null && mUserData.getProfileImage().length() > 0) {
                    Util.navigateTOFullScreenActivity(getActivity(),
                            mUserData.getProfileImage(),
                            mUserData.getId(),
                            null,
                            null);
                } else {
                    Toast.makeText(getActivity(),
                            "Image not available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ic_cover:
                if (mUserData != null && mUserData.getProfileCoverImage().length() > 0) {
                    Util.navigateTOFullScreenActivity(getActivity(),
                            mUserData.getProfileCoverImage(),
                            mUserData.getId(),
                            null,
                            null);
                } else {
                    Toast.makeText(getActivity(),
                            "Image not available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_user_name:
                break;
            case R.id.tv_timeline:
//                Intent intentAbout = new Intent(getActivity(), MyAccountAboutActivity.class);
//                startActivity(intentAbout);
                break;
            case R.id.tv_about:
                Intent intentAbout = new Intent(getActivity(), MyAccountAboutActivity.class);
                intentAbout.putExtra("profile_user_id", Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));
                if (mProfileAboutData != null) {
                    intentAbout.putExtra("about", Util.isNull(mProfileAboutData.getAboutMe()));
                    intentAbout.putExtra("address1", Util.isNull(mProfileAboutData.getAddress()));
                    intentAbout.putExtra("address2", Util.isNull(mProfileAboutData.getAddress2()));
                    intentAbout.putExtra("city", Util.isNull(mProfileAboutData.getCity()));
                    intentAbout.putExtra("state", Util.isNull(mProfileAboutData.getState()));
                    intentAbout.putExtra("country", Util.isNull(mProfileAboutData.getCountry()));
                    intentAbout.putExtra("zipcode", Util.isNull(mProfileAboutData.getZipCode()));
                    intentAbout.putExtra("phone1", Util.isNull(mProfileAboutData.getMobileNo()));
                    intentAbout.putExtra("phone2", Util.isNull(mProfileAboutData.getHomephone()));
                    if (mUserData != null) {
                        intentAbout.putExtra("email", Util.isNull(mUserData.getEmail()));
                    }
                }
                startActivity(intentAbout);
                break;
            case R.id.tv_friends:
                Intent intentFriends = new Intent(getActivity(), MyAccountFrndsActivity.class);
                if (mSelectedUserProfileId != null) {
                    intentFriends.putExtra("SelectedUserProfileId", mSelectedUserProfileId);
                }
                startActivity(intentFriends);
                break;
            case R.id.tv_photos:
                Intent intentPhotos = new Intent(getActivity(), MyAccountMyPhotosActivity.class);
                if (mSelectedUserProfileId != null) {
                    intentPhotos.putExtra("SelectedUserProfileId", mSelectedUserProfileId);
                }
                startActivity(intentPhotos);
                break;
            case R.id.iv_more:
                popUpMenu(view);
                break;
            default:
                Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void popUpMenu(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getActivity(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putBoolean("COMING_FROM_PROFILE", true);
                switch (item.getItemId()) {
                    case R.id.item_church:
                        iNavigationListnerToChangeScreen.onNavigateToScreenFromProfile(1, bundle);
                        break;
                    case R.id.item_events:
                        iNavigationListnerToChangeScreen.onNavigateToScreenFromProfile(2, bundle);
                        break;
                    case R.id.item_groups:
                        iNavigationListnerToChangeScreen.onNavigateToScreenFromProfile(3, bundle);
                        break;
                    case R.id.item_obituaries:
                        iNavigationListnerToChangeScreen.onNavigateToScreenFromProfile(4, bundle);
                        break;
                    case R.id.item_weddings:
                        iNavigationListnerToChangeScreen.onNavigateToScreenFromProfile(5, bundle);
                        break;
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_create_post:
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
//                intent.putExtra("mEVENT_ID",mEVENT_ID);
                intent.putExtra("TITTLE", "Add Post Comment");
                startActivity(intent);
                break;
        }
    }

    private void getTimeline(String mSelectedUserProfileId) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callTimeline = apiService.getTimeline(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()),
                mSelectedUserProfileId == null ?
                        Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity())
                        : mSelectedUserProfileId
        );

        callTimeline.enqueue(new Callback<TimelineResponse>() {

            @Override
            public void onResponse(Call<TimelineResponse> call,
                                   Response<TimelineResponse> response) {

                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    TimelineResponse loginResponse = response.body();
                    if (loginResponse.getData() != null) {

                        mUserData = loginResponse.getData().getUserInfo();
                        if (tvUserName != null) {
                            tvUserName.setText(Util.isNull(mUserData.getFirstName()) + " " + Util.isNull(mUserData.getLastName()));
                        }
                        if (loginResponse.getData().getProfileData() != null) {
                            mProfileAboutData = loginResponse.getData().getProfileData();
                        }
//                        tv_user_address.setText(Util.isNull(mUserData.getFirstName()));
                        Glide.with(getActivity())
                                .load(mUserData.getProfileCoverImage())
                                .placeholder(R.drawable.ic_photos_placeholder)
                                .skipMemoryCache(false).centerCrop()
                                .into(icCover);

                        Glide.with(getActivity())
                                .load(mUserData.getProfileImage())
                                .placeholder(R.drawable.profile_def_user_image) // ic_me
                                .skipMemoryCache(false)
                                .into(rrUserImage);
                        new AsyncTask<Void, Void, Void>() {

                            public HomeFeedsAdapter adapter;
                            public UserInfo mUserInfo;

                            @Override
                            protected void onPreExecute() {
                                allposts = loginResponse.getData().getAllposts();
                                mUserInfo = loginResponse.getData().getUserInfo();
                                super.onPreExecute();
                            }

                            @Override
                            protected Void doInBackground(Void... voids) {

                                if (allposts != null && allposts.size() > 0) {
                                    adapter = new HomeFeedsAdapter(
                                            MyAccountFragment.this, allposts, mUserInfo, getActivity()
                                            , 0);

                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                if (adapter != null) {
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                                            getActivity());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setNestedScrollingEnabled(false);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    super.onPostExecute(aVoid);
                                } else {
                                    recyclerView.setAdapter(null);
                                }
                            }
                        }.execute();


                    }
                    hideProgressDialog();
                } else {

                    if (response.body() == null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if (jObjError != null) {
                                Gson gson = new Gson();
                                ErrorResponse error = gson.fromJson(jObjError.toString(),
                                        ErrorResponse.class);
                                String msg = null;
                                if (error != null) {
                                    msg = error.getMsg();
                                } else {
                                    msg = "Something went wrong";
                                }
                                new CustomDialog(getActivity(), null, "",
                                        msg,
                                        "ONFAILED").show();
                            } else {
                                new CustomDialog(getActivity(), null, "",
                                        "Something went wrong",
                                        "ONFAILED").show();
                            }
                            hideProgressDialog();

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<TimelineResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
            }
        });
    }

    /**
     * @param file
     * @param fileType
     */
    private void updateCoverImage(File file, String fileType) {
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

        HashMap<String, RequestBody> map = null;
        Call<CoverPicUpdateResponse> call = null;
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        if (mIMAGE_TYPE==0) {
            // create a map of data to pass along
            RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
            RequestBody user_id = Util.createPartFromString(""
                    + Util.loadPrefrence(Util.PREF_USER_ID, "",
                    getActivity()));
//            RequestBody coverImageCoordinate = Util.createPartFromString("");
            map = new HashMap<>();
            map.put("api_key", api_key);
            map.put("user_id", user_id);
//            map.put("coverImageCoordinate", coverImageCoordinate);

            // finally, execute the request
            call = apiService.
                    updateCoverPhoto(
                            map,
                            body);

        } else if (mIMAGE_TYPE==1) {
            RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
            RequestBody user_id = Util.createPartFromString(""
                    + Util.loadPrefrence(Util.PREF_USER_ID, "",
                    getActivity()));
//            RequestBody coverImageCoordinate = Util.createPartFromString("");
            map = new HashMap<>();
            map.put("api_key", api_key);
            map.put("user_id", user_id);

            // finally, execute the request
            call = apiService.
                    updateProfilePic(
                            map,
                            body);
        }


        call.enqueue(new Callback<CoverPicUpdateResponse>() {

            @Override
            public void onResponse(Call<CoverPicUpdateResponse> call,
                                   Response<CoverPicUpdateResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                    String sCode = response.code() + "";
                    String c = String.valueOf(sCode.charAt(0));
                    switch (c) {
                        case "2": // TODO Success response  task here and progress loader
                            CoverPicUpdateResponse cityListResponse = response.body();
                            if (cityListResponse.getStatuscode().equalsIgnoreCase("200")) {
                                CustomDialog customDialog = new CustomDialog(
                                        getActivity(), MyAccountFragment.this, "",
                                        cityListResponse.getMsg(),
                                        "SUCCESS");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            } else {
                                CustomDialog customDialog = new CustomDialog(
                                        getActivity(), MyAccountFragment.this, "",
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
                            CoverPicUpdateResponse businessResponse1 = response.body();
                            CustomDialog customDialog = new CustomDialog(getActivity(), null,
                                    "", businessResponse1.getMsg(),
                                    "ONFAILED");
                            if (customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            customDialog.show();

                        case "4": // TODO Server Error and display retry

                            hideProgressDialog();
                            if (sCode == "401") {
//                                Util.showDialog(MyAccountFragment.this,
//                                        (IAlertDialogCallback) MyAccountFragment.this,
//                                        getResources().getString(R.string.str_authfailed),
//                                        getResources().getString(R.string.str_authfaileddetail));
                            } else {
                                CoverPicUpdateResponse businessResponse2 = response.body();
                                CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                                        "", businessResponse2.getMsg(),
                                        "ONFAILED");
                                if (customDialog1.isShowing()) {
                                    customDialog1.dismiss();
                                }
                                customDialog1.show();
                            }
                            break;
                        default: // TODO Handle error message and show dialog here
                            CoverPicUpdateResponse businessResponse3 = response.body();
                            CustomDialog customDialog2 = new CustomDialog(getActivity(), null,
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
                    hideProgressDialog();
                    Log.d("ask", response.toString());
                    CustomDialog customDialog2 = new CustomDialog(getActivity(), null,
                            "", response.message(),
                            "ONFAILED");
                    if (customDialog2.isShowing()) {
                        customDialog2.dismiss();
                    }
                    customDialog2.show();
                }
            }

            @Override
            public void onFailure(Call<CoverPicUpdateResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());

                new CustomDialog(getActivity(), null, "", t.getMessage(),
                        "ONFAILED").show();
                hideProgressDialog();
            }
        });
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
    public void onYesPress(String param, String message) {
        if (param.equalsIgnoreCase("PRAYER_ANSWER_COMMENTS")) {
            answerPrayerPost(message, mPostId4Answer);
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
                        .make(coordinatorMainLayout, "Permission needed access camera and storage!", Snackbar.LENGTH_INDEFINITE)
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
                        .make(coordinatorMainLayout, "To use this feature requires permission please allow from setting" +
                                "please_try_again_later", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.str_takephotos), getResources().getString(R.string.str_choosefromphotos),
                getResources().getString(R.string.bt_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.MyAlertDialogStyle);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.str_takephotos))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Util.REQUEST_CAMERA);
                } else if (items[item].equals(getResources().getString(R.string.str_choosefromphotos))) {
                    Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
                    intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                    startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
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

                this.mFilePath = mPath;

                if (thumbnail!=null) {
                    if (mIMAGE_TYPE == 0) {
                        icCover.setImageBitmap(thumbnail);
                    } else if (mIMAGE_TYPE == 1) {
                        rrUserImage.setImageBitmap(thumbnail);
                    }
                }else{
                    Toast.makeText(getActivity(),"Bitmap null",Toast.LENGTH_SHORT).show();
                }

                if (mFilePath != null && mFilePath.length() > 0) {
                    File file = new File(mFilePath);
                    if (mIMAGE_TYPE==0) {
                        // Todo upload cover image here
                        updateCoverImage(file, "image");
                    } else if (mIMAGE_TYPE==1) {
                        // Todo upload profile image here
                        updateCoverImage(file, "image");
                    }
                } else {
                    updateCoverImage(null, "image");
                }
            }
        } else if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

                for (int i = 0; i < images.size(); i++) {
//                    Uri uri = Uri.fromFile(new File(images.get(i).path));

                    File imgFile = new File(images.get(i).path);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        if (myBitmap!=null) {
                            if (mIMAGE_TYPE == 0) {
                                icCover.setImageBitmap(myBitmap);
                            } else if (mIMAGE_TYPE == 1) {
                                rrUserImage.setImageBitmap(myBitmap);
                            }
                        }else{
                            Toast.makeText(getActivity(),"Bitmap null",Toast.LENGTH_SHORT).show();
                        }
                        myBitmap = null;
                    }

                    // start play with image uri
                    mFilePath = images.get(i).path;

                }

                if (mFilePath != null && mFilePath.length() > 0) {
                    File file = new File(mFilePath);
                    if (mIMAGE_TYPE==0) {
                        // Todo upload cover image here
                        updateCoverImage(file, "image");
                    } else if (mIMAGE_TYPE==1) {
                        // Todo upload profile image here
                        updateCoverImage(file, "image");
                    }
                } else {
                    updateCoverImage(null, "image");
                }
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

//    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }

    public String getRealPathFromURI(Uri contentUri) {

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }

        if (cursor != null) {
            cursor.close();
        }

        return res;
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

    private void likePost(String postId, final int position) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        callLikePost = apiService.likePostRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()),
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
                        Toast.makeText(getActivity(), "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        recyclerView.getAdapter().notifyItemChanged(position);
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
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
                                    new CustomDialog(getActivity(), null, "",
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
                        CustomDialog customDialog1 = new CustomDialog(getActivity(),
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
                        new CustomDialog(getActivity(), null, "",
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

    private void answerPrayerPost(String prayerAnswer, String postId) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<PrayerAnswerResponse> call = apiService.PrayerAnsweredRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()),
                postId, prayerAnswer);

        call.enqueue(new Callback<PrayerAnswerResponse>() {

            @Override
            public void onResponse(Call<PrayerAnswerResponse> call, Response<PrayerAnswerResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        PrayerAnswerResponse likePostResponse = response.body();
                        Toast.makeText(getActivity(), "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();

//                        if (allposts.get(position).isBookmarkLocal()) {
//                            allposts.get(position).setBookmarkLocal(false);
//                            recyclerView.getAdapter().notifyItemChanged(position);
//                        } else {
//                            allposts.get(position).setBookmarkLocal(true);
//                            recyclerView.getAdapter().notifyItemChanged(position);
//                        }

                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(getActivity(),
                                    null,
                                    "",
                                    "",
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
                                    new CustomDialog(getActivity(), null, "",
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
                        PrayerAnswerResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(getActivity(),
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        PrayerAnswerResponse body1 = response.body();
                        new CustomDialog(getActivity(), null, "",
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
            public void onFailure(Call<PrayerAnswerResponse> call, Throwable t) {
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
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()),
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
                        Toast.makeText(getActivity(), "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        if (allposts != null) {
                            if (allposts.get(position).isFavLocal()) {
                                allposts.get(position).setFavLocal(false);
                            } else {
                                allposts.get(position).setFavLocal(true);
                            }
                        }
                        recyclerView.getAdapter().notifyItemChanged(position);
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
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
                                    new CustomDialog(getActivity(), null, "",
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
                        CustomDialog customDialog1 = new CustomDialog(getActivity(),
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
                        new CustomDialog(getActivity(), null, "",
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
                new CustomDialog(getActivity(), null, "",
                        t.getMessage() != null ?
                                t.getMessage().length() > 0 ?
                                        t.getMessage()
                                        : "Something went wrong" : "Something went wrong",
                        "ONFAILED").show();
            }
        });

    }

    private void hidePost(String postId, final int position) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        callHidePost = apiService.hidePost(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()),
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
                        Toast.makeText(getActivity(), "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        if (allposts != null) {
                            allposts.remove(position);
                        }
                        recyclerView.getAdapter().notifyItemRemoved(position);
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
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
                                    new CustomDialog(getActivity(), null, "",
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
                        CustomDialog customDialog1 = new CustomDialog(getActivity(),
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
                        new CustomDialog(getActivity(), null, "",
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
                new CustomDialog(getActivity(), null, "",
                        t.getMessage() != null ?
                                t.getMessage().length() > 0 ?
                                        t.getMessage()
                                        : "Something went wrong" : "Something went wrong",
                        "ONFAILED").show();
            }
        });

    }

}

