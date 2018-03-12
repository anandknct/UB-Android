package com.unitybound.events.fragment.fragment;

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
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.events.fragment.activity.AddEventActivity;
import com.unitybound.events.fragment.adapter.EventsListAdapter;
import com.unitybound.events.fragment.beans.EventsItem;
import com.unitybound.events.fragment.beans.EventsListResponse;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nikhiljogdand on 10/06/17.
 */

public class EventsFragment extends Fragment implements EventsListAdapter.IListAdapterCallback,
        View.OnClickListener {

    private FloatingActionButton fabCreateChurch = null;
    private IUserClickFromFragmentListener userClickListener;
    private List<EventsItem> allEvents = null;
    private ProgressDialog mProgressDialog = null;
    private TextView tvMyEvents = null;
    private TextView tvEventsAccepted = null;
    private TextView tvEventsPrivate = null;
    private TextView tvEventsAll = null;
    private boolean mCOMING_FROM_PROFILE = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    RecyclerView recyclerView;
    EventsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
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
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        fabCreateChurch = (FloatingActionButton) view.findViewById(R.id.fab_create_event);
        fabCreateChurch.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(getActivity(), R.style.newDialog);

        userClickListener = (IUserClickFromFragmentListener) (MainActivity) getActivity();
        tvMyEvents = (TextView) view.findViewById(R.id.tv_my_events);
        tvMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tvMyEvents.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tvMyEvents.setTypeface(null, Typeface.BOLD);
                    tvEventsAccepted.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvEventsAccepted.setTypeface(null, Typeface.NORMAL);
                    tvEventsPrivate.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvEventsPrivate.setTypeface(null, Typeface.NORMAL);
                    tvEventsAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvEventsAll.setTypeface(null, Typeface.NORMAL);
                    getMyEventsRequest();
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

        tvEventsAccepted = (TextView) view.findViewById(R.id.tv_events_accepted);
        tvEventsAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tvMyEvents.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvMyEvents.setTypeface(null, Typeface.NORMAL);
                    tvEventsAccepted.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tvEventsAccepted.setTypeface(null, Typeface.BOLD);
                    tvEventsPrivate.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvEventsPrivate.setTypeface(null, Typeface.NORMAL);
                    tvEventsAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvEventsAll.setTypeface(null, Typeface.NORMAL);
                    getAcceptedEventsRequest();
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
        tvEventsPrivate = (TextView) view.findViewById(R.id.tv_events_private);
        tvEventsPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tvMyEvents.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvMyEvents.setTypeface(null, Typeface.NORMAL);
                    tvEventsAccepted.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvEventsAccepted.setTypeface(null, Typeface.NORMAL);
                    tvEventsPrivate.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tvEventsPrivate.setTypeface(null, Typeface.BOLD);
                    tvEventsAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvEventsAll.setTypeface(null, Typeface.NORMAL);
                    getPrivateEventsRequest();
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
        tvEventsAll = (TextView) view.findViewById(R.id.tv_events_all);
        tvEventsAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkNetworkAvailablity(getActivity())) {
                    tvMyEvents.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvMyEvents.setTypeface(null, Typeface.NORMAL);
                    tvEventsAccepted.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvEventsAccepted.setTypeface(null, Typeface.NORMAL);
                    tvEventsPrivate.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                    tvEventsPrivate.setTypeface(null, Typeface.NORMAL);
                    tvEventsAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                    tvEventsAll.setTypeface(null, Typeface.BOLD);
                    getAllEventsRequest();
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
                getMyEventsRequest();

                // To show def tv Selected
                tvMyEvents.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                tvMyEvents.setTypeface(null, Typeface.BOLD);
                tvEventsAccepted.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tvEventsAccepted.setTypeface(null, Typeface.NORMAL);
                tvEventsPrivate.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tvEventsPrivate.setTypeface(null, Typeface.NORMAL);
                tvEventsAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tvEventsAll.setTypeface(null, Typeface.NORMAL);
            }else {
                getAllEventsRequest();

                // To show def tv Selected
                tvMyEvents.setTextColor(Util.getColor(getActivity(),R.color.text_color_primary));
                tvMyEvents.setTypeface(null, Typeface.NORMAL);
                tvEventsAccepted.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tvEventsAccepted.setTypeface(null, Typeface.NORMAL);
                tvEventsPrivate.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tvEventsPrivate.setTypeface(null, Typeface.NORMAL);
                tvEventsAll.setTextColor(Util.getColor(getActivity(),R.color.text_color_secondary));
                tvEventsAll.setTypeface(null, Typeface.BOLD);
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
        Intent intent = new Intent(getActivity(), EventsDetailsActivity.class);
        intent.putExtra("EVENT_ID",allEvents.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onUserNameClickListner(String s, int position) {
//        Util.navigateTOProfileAcitivity(getActivity());
        if (userClickListener != null) {
            userClickListener.onUserClickListener(s, position);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.fab_create_event:
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getAllEventsRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<EventsListResponse> call = apiService.getAllEventsList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<EventsListResponse>() {

            @Override
            public void onResponse(Call<EventsListResponse> call, Response<EventsListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        EventsListResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allEvents = loginResponse.getData().getEvents();
                            if (allEvents != null && allEvents.size() > 0) {
                                adapter = new EventsListAdapter(getActivity(), allEvents,
                                        EventsFragment.this);
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
                        EventsListResponse loginResponse1 = response.body();
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
                        EventsListResponse body1 = response.body();
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
            public void onFailure(Call<EventsListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getMyEventsRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<EventsListResponse> call = apiService.getMyEventsList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<EventsListResponse>() {

            @Override
            public void onResponse(Call<EventsListResponse> call, Response<EventsListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        EventsListResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allEvents = loginResponse.getData().getEvents();
                            if (allEvents != null && allEvents.size() > 0) {
                                adapter = new EventsListAdapter(getActivity(), allEvents, EventsFragment.this);
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
                        EventsListResponse loginResponse1 = response.body();
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
                        EventsListResponse body1 = response.body();
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
            public void onFailure(Call<EventsListResponse> call, Throwable t) {
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

    private void getPrivateEventsRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<EventsListResponse> call = apiService.getPrivateEventsList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<EventsListResponse>() {

            @Override
            public void onResponse(Call<EventsListResponse> call, Response<EventsListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        EventsListResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allEvents = loginResponse.getData().getEvents();
                            if (allEvents != null && allEvents.size() > 0) {
                                adapter = new EventsListAdapter(getActivity(), allEvents, EventsFragment.this);
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
                        EventsListResponse loginResponse1 = response.body();
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
                        EventsListResponse body1 = response.body();
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
            public void onFailure(Call<EventsListResponse> call, Throwable t) {
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

    private void getAcceptedEventsRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<EventsListResponse> call = apiService.getAcceptedEventsList(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<EventsListResponse>() {

            @Override
            public void onResponse(Call<EventsListResponse> call, Response<EventsListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        EventsListResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allEvents = loginResponse.getData().getEvents();
                            if (allEvents != null && allEvents.size() > 0) {
                                adapter = new EventsListAdapter(getActivity(), allEvents, EventsFragment.this);
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
                        EventsListResponse loginResponse1 = response.body();
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
                        EventsListResponse body1 = response.body();
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
            public void onFailure(Call<EventsListResponse> call, Throwable t) {
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
