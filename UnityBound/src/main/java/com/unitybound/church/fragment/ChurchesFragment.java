package com.unitybound.church.fragment;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.church.activity.AddChurchActivity;
import com.unitybound.church.adapter.ChurchListAdapter;
import com.unitybound.church.beans.ChurchListResponse;
import com.unitybound.church.beans.ChurchesItem;
import com.unitybound.main.MainActivity;
import com.unitybound.main.interPhase.IUserClickFromFragmentListener;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.SpacesItemDecoration;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ChurchesFragment Fragment
 * Created by @aurthor nikhil.jogdand on 14/07/17.
 */
public class ChurchesFragment extends Fragment implements ChurchListAdapter.IListAdapterCallback {

    private ProgressDialog mProgressDialog = null;
    private List<ChurchesItem> allChurch = null;
    private TextView tvFollowed = null;
    private TextView tvAll = null;
    private TextView tv_nearByMe = null;
    private boolean mCOMING_FROM_PROFILE = false;
    private ArrayList<String> NearByMile = new ArrayList<String>();
    private Spinner SpnNearby;
    private String Radius = "0";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    RecyclerView recyclerView;
    ChurchListAdapter adapter;

    @BindView(R.id.fab_create_post)
    FloatingActionButton fabCreatePost;
    private IUserClickFromFragmentListener userClickListener = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_churches, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("COMING_FROM_PROFILE")) {
            mCOMING_FROM_PROFILE = bundle.getBoolean("COMING_FROM_PROFILE");
        }
        return view;
    }


    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rv_list_layout);
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        mProgressDialog = new ProgressDialog(getActivity(), R.style.newDialog);
        ImageView tvChurchLabel = view.findViewById(R.id.tv_church_label);
        tvFollowed = view.findViewById(R.id.tv_followed);
        tvAll = view.findViewById(R.id.tv_all);
        tv_nearByMe = view.findViewById(R.id.tv_nearByMe);

        NearByMile.add("10 Miles");
        NearByMile.add("25 Miles");
        NearByMile.add("50 Miles");
        NearByMile.add("75 Miles");
        NearByMile.add("100 Miles");

        SpnNearby = view.findViewById(R.id.SpnNearby);
        SpnNearby.setAdapter(null);
        ArrayAdapter<String> dataAdapter1;
        dataAdapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, NearByMile);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpnNearby.setAdapter(dataAdapter1);

        SpnNearby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    Radius = "10";
                }
                if(position == 1)
                {
                    Radius = "25";
                }
                if(position == 2)
                {
                    Radius = "50";
                }
                if(position == 3)
                {
                    Radius = "75";
                }
                if(position == 4)
                {
                    Radius = "100";
                }

                getMyNearByChurchRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvFollowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    SpnNearby.setVisibility(View.GONE);
                    tvFollowed.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tvAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_nearByMe.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));

                    tvAll.setTypeface(null, Typeface.NORMAL);
                    tv_nearByMe.setTypeface(null, Typeface.NORMAL);
                    tvFollowed.setTypeface(null, Typeface.BOLD);

                    getFollowChurchRequest();
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
        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpnNearby.setVisibility(View.GONE);
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tvFollowed.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_nearByMe.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvFollowed.setTypeface(null, Typeface.NORMAL);
                    tvFollowed.setTypeface(null, Typeface.NORMAL);

                    tvAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tvAll.setTypeface(null, Typeface.BOLD);
                    getAllChurchRequest();
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
        tvChurchLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    SpnNearby.setVisibility(View.GONE);
                    tvFollowed.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_nearByMe.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvFollowed.setTypeface(null, Typeface.NORMAL);
                    tv_nearByMe.setTypeface(null, Typeface.NORMAL);
                    tvAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvAll.setTypeface(null, Typeface.NORMAL);
                    getMyChurchRequest();
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

        tv_nearByMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    SpnNearby.setVisibility(View.VISIBLE);
                    tvFollowed.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvFollowed.setTypeface(null, Typeface.NORMAL);
                    tvAll.setTypeface(null, Typeface.NORMAL);
                    tv_nearByMe.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tv_nearByMe.setTypeface(null, Typeface.NORMAL);
                    //getMyNearByChurchRequest();
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
        userClickListener = (IUserClickFromFragmentListener) getActivity();
    }

    @Override
    public void onItemClickListner(String s, int position) {

        Intent intent = new Intent(getActivity(), ChurchDetailsActivity.class);
        intent.putExtra(Util.CHURCH_ID, allChurch.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onUserNameClickListener(String s, int position) {
//        Util.navigateTOProfileAcitivity(getActivity());
        if (userClickListener != null) {
            userClickListener.onUserClickListener(s, position);
        }
    }

    @OnClick(R.id.fab_create_post)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), AddChurchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (mCOMING_FROM_PROFILE) {
                //To Show first tab tv selected
                tvFollowed.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tvFollowed.setTypeface(null, Typeface.NORMAL);
                tvAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tvAll.setTypeface(null, Typeface.NORMAL);
                getMyChurchRequest();
            } else {
                //To Show first tab tv selected
                tvFollowed.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tvFollowed.setTypeface(null, Typeface.NORMAL);
                tvAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tvAll.setTypeface(null, Typeface.BOLD);
                getAllChurchRequest();
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

    private void getMyNearByChurchRequest()
    {
        showProgressDialog();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ChurchListResponse> call = apiService.getNearByChurchList(BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()), Radius);

        call.enqueue(new Callback<ChurchListResponse>() {

            @Override
            public void onResponse(Call<ChurchListResponse> call, Response<ChurchListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        ChurchListResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allChurch = loginResponse.getData().getChurches();
                            if (allChurch != null && allChurch.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new ChurchListAdapter(getActivity(), allChurch, ChurchesFragment.this);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//                              recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
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
                        ChurchListResponse loginResponse1 = response.body();
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
                        ChurchListResponse body1 = response.body();
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
            public void onFailure(Call<ChurchListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getAllChurchRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ChurchListResponse> call = apiService.getChurchList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<ChurchListResponse>() {

            @Override
            public void onResponse(Call<ChurchListResponse> call, Response<ChurchListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        ChurchListResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allChurch = loginResponse.getData().getChurches();
                            if (allChurch != null && allChurch.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new ChurchListAdapter(getActivity(), allChurch, ChurchesFragment.this);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//                              recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
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
                        ChurchListResponse loginResponse1 = response.body();
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
                        ChurchListResponse body1 = response.body();
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
            public void onFailure(Call<ChurchListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getMyChurchRequest() {
        showProgressDialog();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ChurchListResponse> call = apiService.getMyChurchList(BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<ChurchListResponse>() {

            @Override
            public void onResponse(Call<ChurchListResponse> call, Response<ChurchListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        ChurchListResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allChurch = loginResponse.getData().getChurches();
                            if (allChurch != null && allChurch.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new ChurchListAdapter(getActivity(), allChurch, ChurchesFragment.this);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//                              recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
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
                        ChurchListResponse loginResponse1 = response.body();
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
                        ChurchListResponse body1 = response.body();
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
            public void onFailure(Call<ChurchListResponse> call, Throwable t) {
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

    private void getFollowChurchRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ChurchListResponse> call = apiService.getFollowChurchList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<ChurchListResponse>() {

            @Override
            public void onResponse(Call<ChurchListResponse> call, Response<ChurchListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        ChurchListResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allChurch = loginResponse.getData().getChurches();
                            if (allChurch != null && allChurch.size() > 0) {
                                recyclerView.setAdapter(null);
                                adapter = new ChurchListAdapter(getActivity(), allChurch, ChurchesFragment.this);
                                LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
//                              recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
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
                        ChurchListResponse loginResponse1 = response.body();
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
                        ChurchListResponse body1 = response.body();
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
            public void onFailure(Call<ChurchListResponse> call, Throwable t) {
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
