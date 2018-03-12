package com.unitybound.notification.fragment;

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

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.main.interPhase.iNotificationUpdate;
import com.unitybound.notification.adapter.NotificationListAdapter;
import com.unitybound.notification.beans.NotificationListItem;
import com.unitybound.notification.beans.NotificationListResponse;
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
 * Created by charchitkasliwal on 10/05/17.
 */

public class NotificationsFragment extends Fragment {

    private ProgressDialog mProgressDialog = null;
    private iNotificationUpdate inotificationUpdate;

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

    RecyclerView recyclerView;
    NotificationListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mProgressDialog = new ProgressDialog(getActivity(), R.style.newDialog);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        if (Util.checkNetworkAvailablity(getActivity())) {

            getAllNotification();

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

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_layout);
//        adapter = new NotificationListAdapter(getActivity(), datalist);
//        LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(lLayout);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);

    }

    private void getAllNotification() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<NotificationListResponse> call = apiService.notificationListRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<NotificationListResponse>() {

            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        NotificationListResponse listResponse = response.body();
                        if (listResponse.getData().getNotificationCount() > 0) {
                            List<NotificationListItem> allNotification = listResponse.getData().getNotificationList();

                            if (allNotification != null) {
                                adapter = new NotificationListAdapter(getActivity(), allNotification);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(lLayout);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
                            }
                            inotificationUpdate.updateAllNotificationCount(
                                    listResponse.getData().getNotificationCount(),
                                    -1);
                        }else{
                            List<NotificationListItem> allNotification = new ArrayList<>();
                            adapter = new NotificationListAdapter(getActivity(), allNotification);
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
                        NotificationListResponse loginResponse1 = response.body();
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
                        NotificationListResponse body1 = response.body();
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
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    public void showProgressDialog() {
        try {
            Util.hideSoftKeyboard(getActivity());
        }catch (Exception e){

        }
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
        try {
            Util.hideSoftKeyboard(getActivity());
        }catch (Exception e){

        }
    }

}