package com.unitybound.weddings.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;
import com.unitybound.weddings.activity.AddWeddingActivity;
import com.unitybound.weddings.activity.WeddingsDetailsActivity;
import com.unitybound.weddings.adapter.WeddingsListAdapter;
import com.unitybound.weddings.beans.allWeddings.AllWeaddingsResponse;
import com.unitybound.weddings.beans.allWeddings.DataItem;
import com.unitybound.weddings.beans.myWeddings.MyWeddingsResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nikhil.jogdand on 29/09/2017.
 */

public class WeddingsFragment  extends Fragment implements WeddingsListAdapter.IListAdapterCallback {


    RecyclerView recyclerView;
    WeddingsListAdapter adapter;
    private ProgressDialog mProgressDialog = null;
    private List<DataItem> allWedding = null;
    private List<com.unitybound.weddings.beans.myWeddings.DataItem> allMyWeadding = null;
    private boolean mCOMING_FROM_PROFILE = false;
    private TextView tv_my_wedding_label;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weddings, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView(view);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("COMING_FROM_PROFILE")) {
            mCOMING_FROM_PROFILE = bundle.getBoolean("COMING_FROM_PROFILE");
        }
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_layout);
        mProgressDialog = new ProgressDialog(getActivity(), R.style.newDialog);
        FloatingActionButton fabCreateWedding = (FloatingActionButton) view.findViewById(R.id.fab_create_wedding);
        fabCreateWedding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddWeddingActivity.class);
                startActivity(intent);
            }
        });
        tv_my_wedding_label = (TextView) view.findViewById(R.id.tv_my_wedding_label);
        TextView tv_all = (TextView) view.findViewById(R.id.tv_all);
        TextView tv_friends_weddings = (TextView) view.findViewById(R.id.tv_weddings_friends);

        tv_friends_weddings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_all.setTypeface(null, Typeface.NORMAL);
                    tv_my_wedding_label.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_my_wedding_label.setTypeface(null, Typeface.NORMAL);
                    tv_friends_weddings.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tv_friends_weddings.setTypeface(null, Typeface.BOLD);
                    getFriendWeaddingRequest();
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

        tv_my_wedding_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_all.setTypeface(null, Typeface.NORMAL);
                    tv_my_wedding_label.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tv_my_wedding_label.setTypeface(null, Typeface.BOLD);
                    tv_friends_weddings.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_friends_weddings.setTypeface(null, Typeface.NORMAL);
                    getMyWeaddingRequest();
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
                    tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tv_all.setTypeface(null, Typeface.BOLD);
                    tv_my_wedding_label.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_my_wedding_label.setTypeface(null, Typeface.NORMAL);
                    tv_friends_weddings.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_friends_weddings.setTypeface(null, Typeface.NORMAL);
                    getAllWeaddingRequest();
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
        Intent intent = new Intent(getActivity(), WeddingsDetailsActivity.class);
        if (allWedding!=null) {
            intent.putExtra("WEDDING_ID", allWedding.get(position).getId());
        }else if (allMyWeadding!=null){
            intent.putExtra("WEDDING_ID", allMyWeadding.get(position).getId());
        }
        startActivity(intent);
    }

    @Override
    public void onResume() {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (mCOMING_FROM_PROFILE) {
                getMyWeaddingRequest();
            }else {
                getMyWeaddingRequest();
            }
            tv_my_wedding_label.setTypeface(null, Typeface.BOLD);
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

    private void getMyWeaddingRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MyWeddingsResponse> call = apiService.getMyWeaddingList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<MyWeddingsResponse>() {

            @Override
            public void onResponse(Call<MyWeddingsResponse> call, Response<MyWeddingsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        MyWeddingsResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allMyWeadding = loginResponse.getData();
                            if (allMyWeadding != null && allMyWeadding.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new WeddingsListAdapter(getActivity(), allMyWeadding, WeddingsFragment.this);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(lLayout);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
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
                        MyWeddingsResponse loginResponse1 = response.body();
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
                        MyWeddingsResponse body1 = response.body();
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
            public void onFailure(Call<MyWeddingsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getAllWeaddingRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<AllWeaddingsResponse> call = apiService.getWeaddingList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<AllWeaddingsResponse>() {

            @Override
            public void onResponse(Call<AllWeaddingsResponse> call, Response<AllWeaddingsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        AllWeaddingsResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allWedding = loginResponse.getData();
                            if (allWedding != null && allWedding.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new WeddingsListAdapter(getActivity(), allWedding, WeddingsFragment.this);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(lLayout);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
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
                        AllWeaddingsResponse loginResponse1 = response.body();
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
                        AllWeaddingsResponse body1 = response.body();
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
            public void onFailure(Call<AllWeaddingsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getFriendWeaddingRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<AllWeaddingsResponse> call = apiService.getFriendWeaddingList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<AllWeaddingsResponse>() {

            @Override
            public void onResponse(Call<AllWeaddingsResponse> call, Response<AllWeaddingsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        AllWeaddingsResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allWedding = loginResponse.getData();
                            if (allWedding != null && allWedding.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new WeddingsListAdapter(getActivity(), allWedding, WeddingsFragment.this);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(lLayout);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
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
                        AllWeaddingsResponse loginResponse1 = response.body();
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
                        AllWeaddingsResponse body1 = response.body();
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
            public void onFailure(Call<AllWeaddingsResponse> call, Throwable t) {
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

}