package com.unitybound.main.my.prayer.request.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.activity.AddPostActivity;
import com.unitybound.account.activity.MyAccountAboutActivity;
import com.unitybound.account.beans.hidePost.HidePostResponse;
import com.unitybound.main.home.fragment.activity.FeedsCommentActivity;
import com.unitybound.main.home.fragment.adapter.HomeFeedsAdapter;
import com.unitybound.main.home.fragment.beans.bookmarkPost.BookmarkPostResponse;
import com.unitybound.main.home.fragment.beans.favUnfav.FavouriteUnfavResponse;
import com.unitybound.main.home.fragment.beans.like.LikePostResponse;
import com.unitybound.main.home.fragment.beans.prayerAnswer.PrayerAnswerResponse;
import com.unitybound.main.my.prayer.request.adapter.MyPrayerRequestAdapter;
import com.unitybound.main.my.prayer.request.model.AllPrayersResponse;
import com.unitybound.main.my.prayer.request.model.AllpostsItem;
import com.unitybound.main.my.prayer.request.model.archive.ArchiveResponse;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.SimpleDividerItemDecoration;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPrayerRequestFragment extends Fragment implements
        HomeFeedsAdapter.IListAdapterCallback, MyPrayerRequestAdapter.IListAdapterCallback,
        CustomDialog.IDialogListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    //    @BindView(swipe_refresh)
//    SwipeRefreshLayout swipeRefresh;
    private BottomSheetDialog mBottomSheetDialog = null;
    private ProgressDialog mProgressDialog = null;
    private Call<AllPrayersResponse> callAllPost = null;
    private List<AllpostsItem> allPosts = null;
    private Call<LikePostResponse> callLikePost = null;
    private String mPostId4Answer = "";
    private TextView tvArchivedAccepted = null;
    private TextView tv_my_prayers = null, tv_events_all = null;
    private Call<ArchiveResponse> callArchivePost = null;
    private String mForSheetUserId = null;
    private int mForSheetPosition = -1;
    private int mLastSelectedPostion = 0;
    private Call<FavouriteUnfavResponse> callFavPost = null;
    private Call<HidePostResponse> callHidePost = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mLastSelectedPostion = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_prayer_request, container, false);
        ButterKnife.bind(this, view);
        mProgressDialog = new ProgressDialog(getActivity(), R.style.newDialog);
        initViews(view);
        return view;
    }

    @Override
    public void onDetach() {
        if (callAllPost != null && callAllPost.isExecuted()) {
            callAllPost.cancel();
        }
        super.onDetach();
    }

    /**
     * @param view the view
     */
    private void initViews(View view) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        tv_events_all = (TextView) view.findViewById(R.id.tv_events_all);
        tv_events_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    getAllPrayersRequest();
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

        tvArchivedAccepted = (TextView) view.findViewById(R.id.tv_events_accepted);
        tvArchivedAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    getArchivePrayersRequest();
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

        tv_my_prayers = (TextView) view.findViewById(R.id.tv_my_prayers);
        tv_my_prayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    getMyPrayersRequest();
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

        setUpBottomSheet();
    }

    @Override
    public void onItemClickListner(String s, int position) {

    }

    @Override
    public void onCommentClickListner(String s, int position) {
        mLastSelectedPostion = position;
        Intent intent = new Intent(getActivity(), FeedsCommentActivity.class);
        if (allPosts != null) {
            intent.putExtra("POST_ID", allPosts.get(position).getId());
        }
        startActivity(intent);
    }

    @Override
    public void onUserNameClickListner(String s, int position) {
        Intent intent = new Intent(getActivity(), MyAccountAboutActivity.class);
        startActivity(intent);
        mLastSelectedPostion = position;
    }

    @Override
    public void onArchiveClickListner(String postId, int position) {
        if (Util.checkNetworkAvailablity(getActivity())) {
            archivePost(postId, position);
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
    public void onBookmarkClickListner(String postId, int position) {
        if (Util.checkNetworkAvailablity(getActivity())) {
            bookmarkPost(postId, position);
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
    public void onLikeClickListner(Object postsId, int position) {
        if (Util.checkNetworkAvailablity(getActivity())) {
            String mPostId = (String) postsId; // ((AllpostsItem) postsItem).getId();
            likePost(mPostId, position);
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

            new CustomDialog(getActivity(), MyPrayerRequestFragment.this, 545445).show();
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
    public void onOptionClickListener(Object item, int position) {
        mForSheetUserId = Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity());
        mForSheetPosition = position;
        if (item instanceof AllpostsItem) {
            if ((allPosts.get(position).getPostBy().getId().equalsIgnoreCase(mForSheetUserId))) {
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

                    if (allPosts != null) {
                        Intent intent = new Intent(getActivity(), AddPostActivity.class);
                        intent.putExtra("POST_ID", allPosts.get(mForSheetPosition).getId());
                        intent.putExtra("COMING_FROM", "HOME");
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("data", allPosts.get(mForSheetPosition));
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
                if (Util.checkNetworkAvailablity(getActivity())) {

                    if (allPosts != null) {
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

        hideSheetDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
                if (allPosts != null) {
                    if (mForSheetPosition != -1) {
                        hidePost(allPosts.get(mForSheetPosition).getId(), mForSheetPosition);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        if (Util.checkNetworkAvailablity(getActivity())) {
            getAllPrayersRequest();
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

    private void getAllPrayersRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callAllPost = apiService.getPrayerList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        callAllPost.enqueue(new Callback<AllPrayersResponse>() {

            @Override
            public void onResponse(Call<AllPrayersResponse> call, Response<AllPrayersResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        AllPrayersResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allPosts = loginResponse.getData().getAllposts();
                            if (allPosts != null && allPosts.size() > 0) {

                                MyPrayerRequestAdapter adapter =
                                        new MyPrayerRequestAdapter(getActivity(), allPosts,
                                                MyPrayerRequestFragment.this, "Archived");
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                adapter.notifyDataSetChanged();

                                if (mLastSelectedPostion!=0) {
                                    recyclerView.smoothScrollToPosition(mLastSelectedPostion);
                                }
                            } else {
                                recyclerView.setAdapter(null);
                            }
                            tvArchivedAccepted.setTextColor(Util.getColor(getActivity(), R.color.unselected_text_color));
                            tv_my_prayers.setTextColor(Util.getColor(getActivity(), R.color.unselected_text_color));
                            tv_events_all.setTextColor(Util.getColor(getActivity(), R.color.text_color_secondary));
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
                        AllPrayersResponse loginResponse1 = response.body();
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
                        AllPrayersResponse body1 = response.body();
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
            public void onFailure(Call<AllPrayersResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getMyPrayersRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callAllPost = apiService.getMyPrayerList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()), "0");

        callAllPost.enqueue(new Callback<AllPrayersResponse>() {

            @Override
            public void onResponse(Call<AllPrayersResponse> call, Response<AllPrayersResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        AllPrayersResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allPosts = loginResponse.getData().getAllposts();
                            if (allPosts != null && allPosts.size() > 0) {

                                MyPrayerRequestAdapter adapter =
                                        new MyPrayerRequestAdapter(getActivity(), allPosts,
                                                MyPrayerRequestFragment.this, "Archived");
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setAdapter(null);
                            }
                            tvArchivedAccepted.setTextColor(Util.getColor(getActivity(), R.color.unselected_text_color));
                            tv_my_prayers.setTextColor(Util.getColor(getActivity(), R.color.text_color_secondary));
                            tv_events_all.setTextColor(Util.getColor(getActivity(), R.color.unselected_text_color));
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
                        AllPrayersResponse loginResponse1 = response.body();
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
                        AllPrayersResponse body1 = response.body();
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
            public void onFailure(Call<AllPrayersResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
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
            }
        });

    }

    private void archivePost(String postId, final int position) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        callArchivePost = apiService.archivePost(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()),
                postId);

        callArchivePost.enqueue(new Callback<ArchiveResponse>() {

            @Override
            public void onResponse(Call<ArchiveResponse> call, Response<ArchiveResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        ArchiveResponse likePostResponse = response.body();
                        Toast.makeText(getActivity(), "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        if (allPosts.get(position).getPrayerArchive().toLowerCase().contains("archive")) {
                            allPosts.get(position).setPrayerArchive("Archived");
                        } else if (allPosts.get(position).getPrayerArchive().toLowerCase().contains("unarchived")) {
                            allPosts.get(position).setPrayerArchive("Unarchive");
                        } else {
                            allPosts.get(position).setPrayerArchive("Archived");
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
                        ArchiveResponse loginResponse1 = response.body();
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
                        ArchiveResponse body1 = response.body();
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
            public void onFailure(Call<ArchiveResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });

    }

    private void getArchivePrayersRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callAllPost = apiService.getArchivePrayerList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()), "0");

        callAllPost.enqueue(new Callback<AllPrayersResponse>() {

            @Override
            public void onResponse(Call<AllPrayersResponse> call, Response<AllPrayersResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        AllPrayersResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allPosts = loginResponse.getData().getAllposts();
                            if (allPosts != null && allPosts.size() > 0) {

                                MyPrayerRequestAdapter adapter =
                                        new MyPrayerRequestAdapter(getActivity(), allPosts,
                                                MyPrayerRequestFragment.this, "Archived");
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setAdapter(null);
                            }
                            tvArchivedAccepted.setTextColor(Util.getColor(getActivity(), R.color.text_color_secondary));
                            tv_my_prayers.setTextColor(Util.getColor(getActivity(), R.color.unselected_text_color));
                            tv_events_all.setTextColor(Util.getColor(getActivity(), R.color.unselected_text_color));
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
                        AllPrayersResponse loginResponse1 = response.body();
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
                        AllPrayersResponse body1 = response.body();
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
            public void onFailure(Call<AllPrayersResponse> call, Throwable t) {
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


                            if (allPosts != null) {
                                if (allPosts.get(position).isFavLocal()) {
                                    allPosts.get(position).setFavLocal(false);
                                } else {
                                    allPosts.get(position).setFavLocal(true);
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

                        if (allPosts != null) {
                            allPosts.remove(position);
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

//                        if (allPosts.get(position).isBookmarkLocal()) {
//                            allPosts.get(position).setBookmarkLocal(false);
//                            recyclerView.getAdapter().notifyItemChanged(position);
//                        } else {
//                            allPosts.get(position).setBookmarkLocal(true);
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

                        if (allPosts.get(position).isBookmarkLocal()) {
                            allPosts.get(position).setBookmarkLocal(false);
                            recyclerView.getAdapter().notifyItemChanged(position);
                        } else {
                            allPosts.get(position).setBookmarkLocal(true);
                            recyclerView.getAdapter().notifyItemChanged(position);
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

}
