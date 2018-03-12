package com.unitybound.main.friendrequest.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.main.friendrequest.adapter.FriendRequestListAdapter;
import com.unitybound.main.friendrequest.beans.FriendRequestListItem;
import com.unitybound.main.friendrequest.beans.FriendRequestListResponse;
import com.unitybound.main.friendrequest.beans.RejectReq.RejectReqResponse;
import com.unitybound.main.friendrequest.listner.IfriendRequestListner;
import com.unitybound.main.interPhase.iNotificationUpdate;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 1nikhiljogdand@gmail.com on 14/09/2017.
 */

public class FriendRequestFragment extends Fragment implements IfriendRequestListner {

    private ProgressDialog mProgressDialog = null;
    private iNotificationUpdate inotificationUpdate;
    RecyclerView recyclerView;
    FriendRequestListAdapter adapter;
    private List<FriendRequestListItem> requestListItems = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_layout);
        mProgressDialog = new ProgressDialog(getActivity(), R.style.newDialog);
    }

    @Override
    public void onResume() {
        if (Util.checkNetworkAvailablity(getActivity())) {

            getAllFriends();

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

    private void getAllFriends() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<FriendRequestListResponse> call = apiService.friendRequestList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<FriendRequestListResponse>() {

            @Override
            public void onResponse(Call<FriendRequestListResponse> call, Response<FriendRequestListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        FriendRequestListResponse listResponse = response.body();
                        if (listResponse.getData().getFriendRequestCount() > 0) {
                            requestListItems = listResponse.getData().getFriendRequestList();

                            if (requestListItems != null) {
                                adapter = new FriendRequestListAdapter(getActivity(),
                                        requestListItems,
                                        FriendRequestFragment.this);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(lLayout);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
                            }
                            inotificationUpdate.updateAllNotificationCount(
                                    -1,
                                    listResponse.getData().getFriendRequestCount());
                        }else{
                            requestListItems = new ArrayList<>();
                            adapter = new FriendRequestListAdapter(getActivity(),
                                    requestListItems,
                                    FriendRequestFragment.this);
                            LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(lLayout);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(adapter);
                        }
                        break;
                    case "4":
                        if (sCode.equalsIgnoreCase("401")) {
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
                        FriendRequestListResponse loginResponse1 = response.body();
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
                        FriendRequestListResponse body1 = response.body();
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
            public void onFailure(Call<FriendRequestListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
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
    public void onAcceptReq(String id, int position) {
        if (Util.checkNetworkAvailablity(getActivity())) {

            acceptReqFriends(id,position);

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
    public void onRejectReq(String id, int position) {
        if (Util.checkNetworkAvailablity(getActivity())) {

            rejectReqFriends(id,position);

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

    private void rejectReqFriends(String id, int position) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RejectReqResponse> call = apiService.cancelFriendRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()),
                id);

        call.enqueue(new Callback<RejectReqResponse>() {

            @Override
            public void onResponse(Call<RejectReqResponse> call, Response<RejectReqResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    RejectReqResponse listResponse = response.body();
                    if (listResponse != null) {
                        CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                                "", listResponse.getMsg(),
                                "SUCCESS");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();

                        try {
                            // Change the item id request rejected
                            FriendRequestListItem friendRequestListItem = requestListItems.get(position);
                            friendRequestListItem.setIsAccept(0);
                            requestListItems.set(position,friendRequestListItem);
                            adapter.notifyItemChanged(position);
                        } catch (Exception e) {
                            e.printStackTrace();
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
                        } else {
                            CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                                    "", "server error please try again",
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RejectReqResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                        "", "server error please try again",
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    private void acceptReqFriends(String id, int position) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RejectReqResponse> call = apiService.acceptFriendRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()),
                id);

        call.enqueue(new Callback<RejectReqResponse>() {

            @Override
            public void onResponse(Call<RejectReqResponse> call, Response<RejectReqResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    RejectReqResponse listResponse = response.body();
                    if (listResponse != null) {
                        CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                                "", listResponse.getMsg(),
                                "SUCCESS");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();

                        try {
                            // Change the item id request rejected
                            FriendRequestListItem friendRequestListItem = requestListItems.get(position);
                            friendRequestListItem.setIsAccept(1);
                            requestListItems.set(position,friendRequestListItem);
                            adapter.notifyItemChanged(position);
                        } catch (Exception e) {
                            e.printStackTrace();
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
                        } else {
                            CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                                    "", "server error please try again",
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RejectReqResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                        "", "server error please try again",
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

}