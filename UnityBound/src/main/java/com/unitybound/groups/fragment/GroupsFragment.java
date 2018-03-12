package com.unitybound.groups.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.groups.activity.AddGroupActivity;
import com.unitybound.groups.adapter.GroupsListAdapter;
import com.unitybound.groups.beans.MyGroupsResponse;
import com.unitybound.groups.beans.PublicGroups.AllPublicGroupsResponse;
import com.unitybound.groups.beans.UserGroupsItem;
import com.unitybound.groups.beans.joinedGroups.JoinedGroupsResponse;
import com.unitybound.groups.beans.joinedGroups.UserJoinedGroupsItem;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.SpacesItemDecoration;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by charchitkasliwal on 10/05/17.
 */

public class GroupsFragment extends Fragment implements GroupsListAdapter.IListAdapterCallback {

    private ProgressDialog mProgressDialog = null;
    private Call<MyGroupsResponse> callMyGroupsPost = null;
    private Call<JoinedGroupsResponse> callMyJoinedPost = null;
    private List<UserGroupsItem> allMyGrops;
    private List<UserJoinedGroupsItem> allMyJoinedGrops = null;
    private Call<AllPublicGroupsResponse> callAllPublicGroups = null;
    private List<com.unitybound.groups.beans.PublicGroups.UserGroupsItem> allAllPublicGrops = null;
    private Call<AllPublicGroupsResponse> callMyClosedGroupsPost = null;
    private List<com.unitybound.groups.beans.PublicGroups.UserGroupsItem> allMyClosedGrops = null;
    private boolean mCOMING_FROM_PROFILE = false;
    private TextView tv_all;
    private TextView tv_closed;
    private TextView tv_joined;
    private TextView tv_tv_groups_label;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    RecyclerView recyclerView;
    GroupsListAdapter adapter;
    @BindView(R.id.fab_create_group)
    FloatingActionButton fabCreateGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_groups,
                container, false);
        ButterKnife.bind(this, view);
        initView(view);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("COMING_FROM_PROFILE")) {
            mCOMING_FROM_PROFILE = bundle.getBoolean("COMING_FROM_PROFILE");
        }
        return view;
    }


    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_layout);
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        mProgressDialog = new ProgressDialog(getActivity(), R.style.newDialog);

        tv_tv_groups_label = (TextView) view.findViewById(R.id.tv_groups_label);
        tv_joined = (TextView) view.findViewById(R.id.tv_joined);
        tv_closed = (TextView) view.findViewById(R.id.tv_closed);
        tv_all = (TextView) view.findViewById(R.id.tv_all);

        tv_tv_groups_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tv_tv_groups_label.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tv_tv_groups_label.setTypeface(null, Typeface.BOLD);
                    tv_joined.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_joined.setTypeface(null, Typeface.NORMAL);
                    tv_closed.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_closed.setTypeface(null, Typeface.NORMAL);
                    tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_all.setTypeface(null, Typeface.NORMAL);
                    getAllGroupsRequest();
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


        tv_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tv_tv_groups_label.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_tv_groups_label.setTypeface(null, Typeface.NORMAL);
                    tv_joined.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tv_joined.setTypeface(null, Typeface.BOLD);
                    tv_closed.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_closed.setTypeface(null, Typeface.NORMAL);
                    tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_all.setTypeface(null, Typeface.NORMAL);
                    getJoinedGroupsRequest();
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


        tv_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tv_tv_groups_label.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_tv_groups_label.setTypeface(null, Typeface.NORMAL);
                    tv_joined.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_joined.setTypeface(null, Typeface.NORMAL);
                    tv_closed.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tv_closed.setTypeface(null, Typeface.BOLD);
                    tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_all.setTypeface(null, Typeface.NORMAL);
                    getClosedGroupsRequest();
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


        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tv_tv_groups_label.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_tv_groups_label.setTypeface(null, Typeface.NORMAL);
                    tv_joined.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_joined.setTypeface(null, Typeface.NORMAL);
                    tv_closed.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_closed.setTypeface(null, Typeface.NORMAL);
                    tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tv_all.setTypeface(null, Typeface.BOLD);
                    getPublicGroupsRequest();
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
    }

    @Override
    public void onItemClickListner(String s, int position) {
//        Fragment fragment = new GroupsDetailsActivity();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                android.R.anim.fade_out);
//        fragmentTransaction.replace(R.id.frame, fragment, fragment.getClass().getName());
//        fragmentTransaction.addToBackStack(fragment.getClass().getName());
//        fragmentTransaction.commitAllowingStateLoss();
        Intent i = new Intent(getActivity(), GroupsDetailsActivity.class);
        i.putExtra(Util.GROUP_ID,s);
        getActivity().startActivity(i);
    }

    @OnClick(R.id.fab_create_group)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), AddGroupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (mCOMING_FROM_PROFILE) {
                getJoinedGroupsRequest();
                tv_tv_groups_label.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tv_tv_groups_label.setTypeface(null, Typeface.NORMAL);
                tv_joined.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                tv_joined.setTypeface(null, Typeface.BOLD);
                tv_closed.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tv_closed.setTypeface(null, Typeface.NORMAL);
                tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tv_all.setTypeface(null, Typeface.NORMAL);
            } else {
                getAllGroupsRequest();
                tv_tv_groups_label.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tv_tv_groups_label.setTypeface(null, Typeface.NORMAL);
                tv_joined.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tv_joined.setTypeface(null, Typeface.NORMAL);
                tv_closed.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tv_closed.setTypeface(null, Typeface.NORMAL);
                tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                tv_all.setTypeface(null, Typeface.BOLD);
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

    private void getAllGroupsRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callMyGroupsPost = apiService.getMyGroupsList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        callMyGroupsPost.enqueue(new Callback<MyGroupsResponse>()   {

            @Override
            public void onResponse(Call<MyGroupsResponse> call, Response<MyGroupsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        MyGroupsResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allMyGrops = loginResponse.getData().getUserGroups();
                            if (allMyGrops != null && allMyGrops.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new GroupsListAdapter(getActivity(), allMyGrops, GroupsFragment.this);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                                recyclerView.setLayoutManager(lLayout);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setAdapter(null);
                            }

//                            swipeRefresh.setRefreshing(false);
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
                        MyGroupsResponse loginResponse1 = response.body();
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
                        MyGroupsResponse body1 = response.body();
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
            public void onFailure(Call<MyGroupsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(getActivity(), null, "",
                        t.getMessage() != null ?
                                t.getMessage() : "Something went wrong",
                        "ONFAILED").show();
            }
        });
    }

    private void getJoinedGroupsRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callMyJoinedPost = apiService.getMyJoinedGroupsList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        callMyJoinedPost.enqueue(new Callback<JoinedGroupsResponse>()   {

            @Override
            public void onResponse(Call<JoinedGroupsResponse> call, Response<JoinedGroupsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        JoinedGroupsResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allMyJoinedGrops = loginResponse.getData().getUserGroups();
                            if (allMyJoinedGrops != null && allMyJoinedGrops.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new GroupsListAdapter(getActivity(), GroupsFragment.this, allMyJoinedGrops);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                                recyclerView.setLayoutManager(lLayout);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setAdapter(null);
                            }

//                            swipeRefresh.setRefreshing(false);
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
                        JoinedGroupsResponse loginResponse1 = response.body();
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
                        JoinedGroupsResponse body1 = response.body();
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
            public void onFailure(Call<JoinedGroupsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(getActivity(), null, "",
                        t.getMessage() != null ?
                                t.getMessage() : "Something went wrong",
                        "ONFAILED").show();
            }
        });
    }

    private void getPublicGroupsRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callAllPublicGroups = apiService.getAllPublicGroupsList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        callAllPublicGroups.enqueue(new Callback<AllPublicGroupsResponse>()   {

            @Override
            public void onResponse(Call<AllPublicGroupsResponse> call, Response<AllPublicGroupsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        AllPublicGroupsResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allAllPublicGrops = loginResponse.getData().getUserGroups();
                            if (allAllPublicGrops != null && allAllPublicGrops.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new GroupsListAdapter(allAllPublicGrops , getActivity(), GroupsFragment.this);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                                recyclerView.setLayoutManager(lLayout);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setAdapter(null);
                            }

//                            swipeRefresh.setRefreshing(false);
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
                        AllPublicGroupsResponse loginResponse1 = response.body();
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
                        AllPublicGroupsResponse body1 = response.body();
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
            public void onFailure(Call<AllPublicGroupsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(getActivity(), null, "",
                        t.getMessage() != null ?
                                t.getMessage() : "Something went wrong",
                        "ONFAILED").show();
            }
        });
    }

    private void getClosedGroupsRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callMyClosedGroupsPost = apiService.getClosedGroupsList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        callMyClosedGroupsPost.enqueue(new Callback<AllPublicGroupsResponse>() {

            @Override
            public void onResponse(Call<AllPublicGroupsResponse> call, Response<AllPublicGroupsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        AllPublicGroupsResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allMyClosedGrops = loginResponse.getData().getUserGroups();
                            if (allMyClosedGrops != null && allMyClosedGrops.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new GroupsListAdapter( GroupsFragment.this,getActivity(), allMyClosedGrops);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//                              recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                                recyclerView.setLayoutManager(lLayout);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setAdapter(null);
                            }

//                            swipeRefresh.setRefreshing(false);
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
                        AllPublicGroupsResponse loginResponse1 = response.body();
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
                        AllPublicGroupsResponse body1 = response.body();
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
            public void onFailure(Call<AllPublicGroupsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(getActivity(), null, "",
                        t.getMessage() != null ?
                                t.getMessage() : "Something went wrong",
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

}
