package com.unitybound.main.home.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.activity.AddPostActivity;
import com.unitybound.account.beans.hidePost.HidePostResponse;
import com.unitybound.main.home.fragment.activity.FeedsCommentActivity;
import com.unitybound.main.home.fragment.adapter.HomeFeedsAdapter;
import com.unitybound.main.home.fragment.adapter.MyFeedsSpinnerAdapter;
import com.unitybound.main.home.fragment.beans.bookmarkPost.BookmarkPostResponse;
import com.unitybound.main.home.fragment.beans.favUnfav.FavouriteUnfavResponse;
import com.unitybound.main.home.fragment.beans.filterPost.DataItem;
import com.unitybound.main.home.fragment.beans.filterPost.FilterPostResponse;
import com.unitybound.main.home.fragment.beans.homeFeeds.AllPostResponse;
import com.unitybound.main.home.fragment.beans.homeFeeds.AllPostsItem;
import com.unitybound.main.home.fragment.beans.like.LikePostResponse;
import com.unitybound.main.home.fragment.beans.prayerAnswer.PrayerAnswerResponse;
import com.unitybound.main.interPhase.IUserClickFromFragmentListener;
import com.unitybound.main.interPhase.iNotificationUpdate;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.SimpleDividerItemDecoration;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Live feed Fragment
 */
public class HomeFeedsFragment extends Fragment implements
        HomeFeedsAdapter.IListAdapterCallback, CustomDialog.IDialogListener {

    private static final String DEFAULT_FILTER = "Everyone";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
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
    private ProgressDialog mProgressDialog = null;
    ArrayList<String> mFilterTypeArrayList = null;
    private final String EVERYONE = "Everyone";
    private final String ME = "Me";
    private final String FAVOURITES = "Favourites";
    private String FILTER_TYPE = "";
    private int LIST_TYPE = 0;
    private ArrayList<AllPostsItem> allposts = null;
    private ArrayList<DataItem> filterPosts = null;
    private Call<AllPostResponse> callAllPost = null;
    private Call<FilterPostResponse> callFilter = null;
    private Call<LikePostResponse> callLikePost = null;
    private Call<FavouriteUnfavResponse> callFavPost = null;
    private iNotificationUpdate inotificationUpdate;
    private String mPostId4Answer = "";
    private String mForSheetUserId = null;
    private int mForSheetPosition = -1;
    private int mLastSelectedPostion = 0;
    private Call<HidePostResponse> callHidePost = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            inotificationUpdate = (iNotificationUpdate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onViewSelected");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        setRetainInstance(true);
        mLastSelectedPostion = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_feeds, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        mProgressDialog = new ProgressDialog(getActivity(), R.style.newDialog);
        return view;
    }

    @Override
    public void onResume() {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (LIST_TYPE == 0) {
                getAllPostRequest();
            } else {
                filterPost(FILTER_TYPE);
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
        super.onResume();
    }

    @Override
    public void onDetach() {
        if (callAllPost != null && callAllPost.isExecuted()) {
            callAllPost.cancel();
        } else if (callFilter != null && callFilter.isExecuted()) {
            callFilter.cancel();
        } else if (callLikePost != null && callLikePost.isExecuted()) {
            callLikePost.cancel();
        }
        super.onDetach();
    }

    /**
     * @param view the view
     */
    private void initViews(View view) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        swipeRefresh.setOnRefreshListener(HomeFeedsFragment.this);

        /* Spinner Feeds Adapter*/
        mFilterTypeArrayList = new ArrayList<>();
        mFilterTypeArrayList.add("ALL Types");
        mFilterTypeArrayList.add("Devotional");
        mFilterTypeArrayList.add("Praise");
        mFilterTypeArrayList.add("Prayers");
        mFilterTypeArrayList.add("Testimonial");
        TypedArray imgs = getResources().obtainTypedArray(R.array.pinner_imgs);

        MyFeedsSpinnerAdapter spAdapter = new MyFeedsSpinnerAdapter(
                getContext(),
                mFilterTypeArrayList, imgs);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        spFeedsType.setAdapter(spAdapter);
        spFeedsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    if (spFeedsType.getSelectedItemPosition() == 0) {
                        FILTER_TYPE = "";
                        ivEveryone.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                        ivFavourite.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                        ivMe.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                        getAllPostRequest();
                    } else {
//                        if (FILTER_TYPE.equalsIgnoreCase("")) {
//                            FILTER_TYPE = DEFAULT_FILTER;
//                        }
                        filterPost(FILTER_TYPE);
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
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        userClickListener = (IUserClickFromFragmentListener) getActivity();
        setUpBottomSheet();
    }


//    @Override
//    public void onRefresh() {
//        swipeRefresh.setRefreshing(false);
//    }

    @Override
    public void onItemClickListner(String s, int position) {

    }

    @Override
    public void onCommentClickListner(String s, int position) {
        Intent intent = new Intent(getActivity(), FeedsCommentActivity.class);
        if (LIST_TYPE == 0) {
            if (allposts != null)
                intent.putExtra("POST_ID", allposts.get(position).getId());
        } else {
            if (filterPosts != null)
                intent.putExtra("POST_ID", filterPosts.get(position).getId());
        }
        mLastSelectedPostion = position;
        startActivity(intent);
    }

    private LinearLayout editSheetDialogButton = null, deleteSheetDialogButton = null,
            favSheetDialogButton = null, reportSheetDialogButton = null, hideSheetDialogButton = null;

    private void setUpBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheets_main_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        hideSheetDialogButton = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_hide);
        editSheetDialogButton = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
        deleteSheetDialogButton = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_del);
        favSheetDialogButton = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_fav);
        reportSheetDialogButton = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_report);
        editSheetDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                if (mForSheetPosition != -1) {
                    if (LIST_TYPE == 0) {
                        if (allposts != null) {
                            Intent intent = new Intent(getActivity(), AddPostActivity.class);
                            intent.putExtra("POST_ID", allposts.get(mForSheetPosition).getId());
                            intent.putExtra("COMING_FROM", "HOME");
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", allposts.get(mForSheetPosition));
                            intent.putExtras(bundle);
                            mLastSelectedPostion = 0;
                            startActivity(intent);
                        }
                    } else if (LIST_TYPE == 1) {
                        if (filterPosts != null) {
                            Intent intent = new Intent(getActivity(), AddPostActivity.class);
                            intent.putExtra("COMING_FROM", "FILTER");
                            intent.putExtra("POST_ID", filterPosts.get(mForSheetPosition).getId());
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", filterPosts.get(mForSheetPosition));
                            intent.putExtras(bundle);
                            mLastSelectedPostion = 0;
                            startActivity(intent);
                        }
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
                    if (LIST_TYPE == 0) {
                        if (allposts != null) {
                            if (mForSheetUserId != null && mForSheetPosition != -1) {
                                favPost(mForSheetUserId, mForSheetPosition);
                            }
                        }
                    } else if (LIST_TYPE == 1) {
                        if (filterPosts != null) {
                            if (mForSheetUserId != null && mForSheetPosition != -1) {
                                favPost(mForSheetUserId, mForSheetPosition);
                            }
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
    }

    @Override
    public void onOptionClickListener(Object item, int position) {
        mForSheetUserId = Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity());
        mForSheetPosition = position;
        if (item instanceof AllPostsItem) {
            if (((AllPostsItem) item).getPostBy().getId().equalsIgnoreCase(mForSheetUserId)) {
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
        } else if (item instanceof DataItem) {
            if (((DataItem) item).getPostBy().getId().equalsIgnoreCase(mForSheetUserId)) {
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

    @Override
    public void onUserNameClickListner(String s, int position) {
//        Intent intent = new Intent(getActivity(), MyAccountAboutActivity.class);
//        startActivity(intent);
        if (userClickListener != null) {
            userClickListener.onUserClickListener(s, 0);
        }
        mLastSelectedPostion = position;
    }

    @Override
    public void onLikeClickListner(Object postsItem, int position) {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (postsItem instanceof AllPostsItem) {
                String mPostId = ((AllPostsItem) postsItem).getId();
                likePost(mPostId, position);
            } else if (postsItem instanceof DataItem) {
                String mPostId = ((DataItem) postsItem).getId();
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
            new CustomDialog(getActivity(), HomeFeedsFragment.this, 543543).show();
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
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (postsItem instanceof AllPostsItem) {
                String mPostId = ((AllPostsItem) postsItem).getId();
                bookmarkPost(mPostId, position);
            } else if (postsItem instanceof DataItem) {
                String mPostId = ((DataItem) postsItem).getId();
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

    @OnClick(R.id.fab_create_post)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), AddPostActivity.class);
        startActivity(intent);
        mLastSelectedPostion = 0;
    }

    @OnClick(R.id.iv_everyone)
    public void onEveryOneFilter() {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (FILTER_TYPE.equalsIgnoreCase(EVERYONE)){
                FILTER_TYPE = "";
                getAllPostRequest();
                ivEveryone.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                ivFavourite.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                ivMe.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                return;
            }
            FILTER_TYPE = EVERYONE;
            filterPost(FILTER_TYPE);

            ivEveryone.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_selected));
            ivFavourite.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
            ivMe.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
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

    @OnClick(R.id.iv_me)
    public void onMeFilter() {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (FILTER_TYPE.equalsIgnoreCase(ME)){
                FILTER_TYPE = "";
                getAllPostRequest();
                ivEveryone.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                ivFavourite.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                ivMe.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                return;
            }
            FILTER_TYPE = ME;
            filterPost(FILTER_TYPE);
            ivEveryone.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
            ivFavourite.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
            ivMe.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_selected));
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

    @OnClick(R.id.iv_favourite)
    public void onFavouriteFilter() {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (FILTER_TYPE.equalsIgnoreCase(FAVOURITES)){
                FILTER_TYPE = "";
                getAllPostRequest();
                ivEveryone.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                ivFavourite.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                ivMe.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
                return;
            }
            FILTER_TYPE = FAVOURITES;
            filterPost(FILTER_TYPE);
            ivEveryone.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
            ivFavourite.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_selected));
            ivMe.setBackgroundColor(Util.getColor(getActivity(), R.color.home_screen_filter_unselected));
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

    private void getAllPostRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callAllPost = apiService.sendAllPostRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        callAllPost.enqueue(new Callback<AllPostResponse>() {

            @Override
            public void onResponse(Call<AllPostResponse> call, Response<AllPostResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    AllPostResponse loginResponse = response.body();
                    if (loginResponse.getAllposts() != null) {
                        allposts = loginResponse.getAllposts();
                        if (allposts != null && allposts.size() > 0) {
                            HomeFeedsAdapter adapter = new HomeFeedsAdapter(getActivity(), allposts,
                                    HomeFeedsFragment.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if (mLastSelectedPostion!=0) {
                                recyclerView.smoothScrollToPosition(mLastSelectedPostion);
                            }
                        } else {
                            recyclerView.setAdapter(null);
                        }
                        LIST_TYPE = 0;
                        filterPosts = null;
//                            swipeRefresh.setRefreshing(false);
                        inotificationUpdate.updateAllNotificationCount(
                                loginResponse.getNotificationCount(),
                                loginResponse.getFriendRequestCount());
                    }

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

            }

            @Override
            public void onFailure(Call<AllPostResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                        "", ""+t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    private void filterPost(String filterType) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        callFilter = apiService.filterAllPostRequest(
                BuildConfig.API_KEY, // mFilterTypeArrayList.get(spFeedsType.getSelectedItemPosition())
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()),
                spFeedsType.getSelectedItemPosition() == 0 ? "all" : spFeedsType.getSelectedItemPosition() == 1 ? "Devotional" :
                        spFeedsType.getSelectedItemPosition() == 2 ? "Praise" : spFeedsType.getSelectedItemPosition() == 3 ? "Prayer" :
                                spFeedsType.getSelectedItemPosition() == 4 ? "Testimonial" : "",
                filterType);

        callFilter.enqueue(new Callback<FilterPostResponse>() {

            @Override
            public void onResponse(Call<FilterPostResponse> call,
                                   Response<FilterPostResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik API RESPONSE ", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        FilterPostResponse filterPostResponse = response.body();
                        if (filterPostResponse.getData() != null) {
                            filterPosts = filterPostResponse.getData();
                            if (filterPosts != null && filterPosts.size() > 0) {
                                HomeFeedsAdapter adapter = new HomeFeedsAdapter(getActivity(),
                                        filterPosts,
                                        HomeFeedsFragment.this);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                LIST_TYPE = 1;
                                allposts = null;
                            } else {
                                recyclerView.setAdapter(null);
                            }
                            if (mLastSelectedPostion!=0) {
                                recyclerView.smoothScrollToPosition(mLastSelectedPostion);
                            }
//                            swipeRefresh.setRefreshing(false);
                            inotificationUpdate.updateAllNotificationCount(
                                    filterPostResponse.getNotificationCount(),
                                    filterPostResponse.getFriendRequestCount());
                        }

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
                        try {
                            FilterPostResponse loginResponse1 = response.body();
                            if (loginResponse1 != null && loginResponse1.getMsg() != null) {
                                CustomDialog customDialog1 = new CustomDialog(getActivity(),
                                        null, "", loginResponse1.getMsg(),
                                        "ONFAILED");
                                if (customDialog1.isShowing()) {
                                    customDialog1.dismiss();
                                }
                                customDialog1.show();
                            }
                            hideProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    default: // TODO Handle error message and show dialog here
                        FilterPostResponse body1 = response.body();
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
            public void onFailure(Call<FilterPostResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                        "", ""+t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });

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
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                        "", ""+t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });

    }

    private void bookmarkPost(String postId, final int position) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<BookmarkPostResponse> call = apiService.bookmarkPostRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()),
                postId);

        call.enqueue(new Callback<BookmarkPostResponse>() {

            @Override
            public void onResponse(Call<BookmarkPostResponse> call, Response<BookmarkPostResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        BookmarkPostResponse likePostResponse = response.body();
                        Toast.makeText(getActivity(), "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        if (LIST_TYPE == 0) {
                            if (allposts.get(position).isBookmarkLocal()) {
                                allposts.get(position).setBookmarkLocal(false);
                                recyclerView.getAdapter().notifyItemChanged(position);
                            } else {
                                allposts.get(position).setBookmarkLocal(true);
                                recyclerView.getAdapter().notifyItemChanged(position);
                            }
                        } else {
                            if (filterPosts.get(position).isBookmarkLocal()) {
                                filterPosts.get(position).setBookmarkLocal(false);
                                recyclerView.getAdapter().notifyItemChanged(position);
                            } else {
                                filterPosts.get(position).setBookmarkLocal(true);
                                recyclerView.getAdapter().notifyItemChanged(position);
                            }
                        }
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
                        BookmarkPostResponse loginResponse1 = response.body();
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
                        BookmarkPostResponse body1 = response.body();
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
            public void onFailure(Call<BookmarkPostResponse> call, Throwable t) {
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

                        if (LIST_TYPE == 0) {
                            if (allposts != null) {
                                if (allposts.get(position).isFavLocal()) {
                                    allposts.get(position).setFavLocal(false);
                                } else {
                                    allposts.get(position).setFavLocal(true);
                                }
                            }
                        } else if (LIST_TYPE == 1) {
                            if (filterPosts != null) {
                                if (filterPosts.get(position).isFavLocal()) {
                                    filterPosts.get(position).setFavLocal(false);
                                } else {
                                    filterPosts.get(position).setFavLocal(true);
                                }
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
                CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                        "", ""+t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
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
