package com.unitybound.obtiuaries.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.TextView;

import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.events.fragment.adapter.EventsListAdapter;
import com.unitybound.obtiuaries.activity.ObitiuariesDetailsActivity;
import com.unitybound.obtiuaries.adapter.ObituariesListAdapter;
import com.unitybound.obtiuaries.beans.DataItem;
import com.unitybound.obtiuaries.beans.ObituariesListResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nikhil.jogdand on 10/05/17.
 */

public class ObituariesFragment extends Fragment implements
        EventsListAdapter.IListAdapterCallback, ObituariesListAdapter.IListAdapterCallback {

    private Call<ObituariesListResponse> callAllObituariesRequest = null;
    private List<DataItem> mObituariesList = null;
    private ProgressDialog mProgressDialog = null;
    private TextView obituaries_of_friend = null, tv_all = null,tv_my_obituaries = null;
    private boolean mCOMING_FROM_PROFILE = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    RecyclerView recyclerView;
    ObituariesListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obitiuaries, container, false);
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

        tv_my_obituaries = (TextView) view.findViewById(R.id.tv_my_obituaries);
        tv_my_obituaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tv_my_obituaries.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tv_my_obituaries.setTypeface(null, Typeface.BOLD);
                    obituaries_of_friend.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    obituaries_of_friend.setTypeface(null, Typeface.NORMAL);
                    tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_all.setTypeface(null, Typeface.NORMAL);
                    getMyObituaries();
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

        tv_all = (TextView) view.findViewById(R.id.tv_all);
        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tv_all.setTypeface(null, Typeface.BOLD);
                    obituaries_of_friend.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    obituaries_of_friend.setTypeface(null, Typeface.NORMAL);
                    tv_my_obituaries.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_my_obituaries.setTypeface(null, Typeface.NORMAL);
                    getAllObituaries();
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

        obituaries_of_friend = (TextView) view.findViewById(R.id.obituaries_of_friend);
        obituaries_of_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_all.setTypeface(null, Typeface.BOLD);
                    obituaries_of_friend.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    obituaries_of_friend.setTypeface(null, Typeface.NORMAL);
                    tv_my_obituaries.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_my_obituaries.setTypeface(null, Typeface.NORMAL);
                    getFriendsObituaries();
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
    public void onResume() {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (mCOMING_FROM_PROFILE) {
                getMyObituaries();
                tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                tv_all.setTypeface(null, Typeface.NORMAL);
                obituaries_of_friend.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                obituaries_of_friend.setTypeface(null, Typeface.NORMAL);
                tv_my_obituaries.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tv_my_obituaries.setTypeface(null, Typeface.BOLD);
            }else {
                getAllObituaries();
                tv_all.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                tv_all.setTypeface(null, Typeface.BOLD);
                obituaries_of_friend.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                obituaries_of_friend.setTypeface(null, Typeface.NORMAL);
                tv_my_obituaries.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tv_my_obituaries.setTypeface(null, Typeface.NORMAL);
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
    public void onItemClickListner(String s, int position) {
        Intent intent = new Intent(getActivity(), ObitiuariesDetailsActivity.class);
        intent.putExtra("OBITUARIES_ID",mObituariesList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onUserNameClickListner(String s, int position) {

    }

    private void getAllObituaries() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callAllObituariesRequest = apiService.getAllObituary(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        callAllObituariesRequest.enqueue(new Callback<ObituariesListResponse>() {

            @Override
            public void onResponse(Call<ObituariesListResponse> call, Response<ObituariesListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    ObituariesListResponse loginResponse = response.body();
                    if (loginResponse.getData() != null) {
                        mObituariesList = loginResponse.getData();
                        if (mObituariesList != null && mObituariesList.size() > 0) {
                            recyclerView.setAdapter(null);
                            recyclerView.removeAllViews();
                            adapter = new ObituariesListAdapter(getActivity(), mObituariesList,
                                    ObituariesFragment.this);
                            LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(lLayout);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(adapter);
                        } else {
                            recyclerView.setAdapter(null);
                        }

                    } else {
                        CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                                "", getResources().getString(R.string.str_server_error),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                            "", getResources().getString(R.string.str_server_error),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<ObituariesListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                        "", t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    private void getFriendsObituaries() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callAllObituariesRequest = apiService.getFriendsObituary(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        callAllObituariesRequest.enqueue(new Callback<ObituariesListResponse>() {

            @Override
            public void onResponse(Call<ObituariesListResponse> call, Response<ObituariesListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    ObituariesListResponse loginResponse = response.body();
                    if (loginResponse.getData() != null) {
                        mObituariesList = loginResponse.getData();
                        if (mObituariesList != null && mObituariesList.size() > 0) {
                            recyclerView.setAdapter(null);
                            recyclerView.removeAllViews();
                            adapter = new ObituariesListAdapter(getActivity(), mObituariesList,
                                    ObituariesFragment.this);
                            LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(lLayout);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(adapter);
                        } else {
                            recyclerView.setAdapter(null);
                        }

                    } else {
                        CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                                "", getResources().getString(R.string.str_server_error),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                            "", getResources().getString(R.string.str_server_error),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<ObituariesListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                        "", t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    private void getMyObituaries() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callAllObituariesRequest = apiService.getMyObituary(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        callAllObituariesRequest.enqueue(new Callback<ObituariesListResponse>() {

            @Override
            public void onResponse(Call<ObituariesListResponse> call, Response<ObituariesListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    ObituariesListResponse loginResponse = response.body();
                    if (loginResponse.getData() != null) {
                        mObituariesList = loginResponse.getData();
                        if (mObituariesList != null && mObituariesList.size() > 0) {
                            recyclerView.setAdapter(null);
                            recyclerView.removeAllViews();
                            adapter = new ObituariesListAdapter(getActivity(), mObituariesList,
                                    ObituariesFragment.this);
                            LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(lLayout);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(adapter);
                        } else {
                            recyclerView.setAdapter(null);
                        }

                    } else {
                        CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                                "", getResources().getString(R.string.str_server_error),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }

                } else {
                    CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                            "", getResources().getString(R.string.str_server_error),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<ObituariesListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                        "", t.getMessage(),
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

}

