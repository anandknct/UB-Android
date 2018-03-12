package com.unitybound.events.fragment.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.activity.AddPostActivity;
import com.unitybound.events.fragment.activity.AddEventActivity;
import com.unitybound.events.fragment.adapter.EventDetailFeedsAdapter;
import com.unitybound.events.fragment.adapter.EventParticipantsGridAdapter;
import com.unitybound.events.fragment.adapter.EventStatusSpinnerAdapter;
import com.unitybound.events.fragment.beans.eventDetailAbout.EventDetailAboutResponse;
import com.unitybound.events.fragment.beans.eventDetailParticipants.EventDetailParticipantsResponse;
import com.unitybound.events.fragment.beans.eventDetailParticipants.EventMemberDetailsResponse;
import com.unitybound.events.fragment.beans.eventDiscussion.AllpostsItem;
import com.unitybound.events.fragment.beans.eventDiscussion.EventDetailDiscussionResponse;
import com.unitybound.events.fragment.beans.eventStatusUpdate.EventStatusUpdateResponse;
import com.unitybound.groups.adapter.GroupsDetailsFeedsAdapter;
import com.unitybound.utility.GridSpacingItemDecoration;
import com.unitybound.utility.SimpleDividerItemDecoration;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.unitybound.R.id.rv_grid_layout;

public class EventsDetailsActivity extends AppCompatActivity implements
        GroupsDetailsFeedsAdapter.IListAdapterCallback, EventDetailFeedsAdapter.IListAdapterCallback, CustomDialog.IDialogListener {

    private TextView tv_discussion = null, tv_event_name = null, tv_event_address = null, tv_event_time = null, tv_event_description = null, tv_edit = null;
    private TextView tv_about_label = null, tv_member_label = null, tv_events_date = null;
    private RecyclerView recyclerView;
    private int mSelectedTab = -1;
    private String mEVENT_ID = null;
    private ProgressDialog mProgressDialog = null;
    private ImageView iv_event_image = null;
    private Call<EventDetailDiscussionResponse> callComments = null;
    private Call<EventDetailParticipantsResponse> callParticipants = null;
    private Call<EventDetailAboutResponse> callDetailAbout = null;
    private ArrayList<AllpostsItem> allposts = null;
    private EventDetailAboutResponse aboutResponse = null;
    private ArrayList<EventMemberDetailsResponse> mParticipantsList = null;
    private RelativeLayout rr_desc = null;
    private Spinner spUserInput = null;
    private EventStatusSpinnerAdapter spAdapter = null;
    private FloatingActionButton fab_edit_event = null;
    private Call<EventStatusUpdateResponse> callEventstatusUpdate = null;
    private ArrayList<String> spinnerItems = null;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("allposts", allposts);
        bundle.putString("mEVENT_ID", mEVENT_ID);
        bundle.putParcelable("aboutResponse", aboutResponse);
        bundle.putParcelableArrayList("mParticipantsList", mParticipantsList);
        bundle.putInt("mSelectedTab", mSelectedTab);
        outState.putAll(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Bundle bundle = savedInstanceState;
        if (bundle != null) {
            allposts = bundle.getParcelableArrayList("allposts");
            mEVENT_ID = bundle.getString("mEVENT_ID");
            mSelectedTab = bundle.getInt("mEVENT_ID");
            // Restore Comment tab data
            setUpCommentsList(allposts);
            // Restore About tab data
            aboutResponse = bundle.getParcelable("aboutResponse");
            if (aboutResponse.getData() != null) {
                tv_event_name.setText(Util.isNull(aboutResponse.getData().getEvent().getEventName()));
                tv_event_address.setText(Util.isNull(aboutResponse.getData().getEvent().getEventLocation()));
                tv_event_time.setText(Util.isNull(aboutResponse.getData().getEvent().getEventStartTime()) + " - "
                        + Util.isNull(aboutResponse.getData().getEvent().getEventEndTime()));
                tv_event_description.setText(Util.isNull(aboutResponse.getData().getEvent().getEventDescription()));
                tv_events_date.setText(Html.fromHtml(Util.isNull(Util.eventDetailDateFormator(aboutResponse.getData().getEvent().getEventStartDate()))
                        + " - " + Util.isNull(Util.eventDetailDateFormator(aboutResponse.getData().getEvent().getEventEndDate()))));
                Glide.with(EventsDetailsActivity.this)
                        .load(Util.isNull(aboutResponse.getData().getEvent().getEventImage()))
                        .placeholder(R.drawable.ic_create_church_placeholder)
                        .skipMemoryCache(false)
                        .into(iv_event_image);
            }

            // Restore participants
            mParticipantsList = bundle.getParcelableArrayList("mParticipantsList");
            setUpEventMembersGrid(mParticipantsList);

            // Restore Default botton tab
            callDiscussionAPIByDefault(rr_desc, mSelectedTab);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_events_detail);
        mEVENT_ID = getIntent().getStringExtra("EVENT_ID");
        initView();
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(EventsDetailsActivity.this, R.style.newDialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Event Details");

        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_edit.setVisibility(View.GONE);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsDetailsActivity.this, AddEventActivity.class);
                intent.putExtra("EVENT_ID", mEVENT_ID);
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", aboutResponse);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(rv_grid_layout);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_two);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, spacingInPixels, true, 0));
        tv_event_description = (TextView) findViewById(R.id.tv_event_description);
        tv_discussion = (TextView) findViewById(R.id.tv_discussion);

        tv_event_name = (TextView) findViewById(R.id.tv_event_name);
        tv_event_address = (TextView) findViewById(R.id.tv_event_address);
        tv_event_time = (TextView) findViewById(R.id.tv_event_time);
        iv_event_image = (ImageView) findViewById(R.id.iv_event_image);
        tv_events_date = (TextView) findViewById(R.id.tv_events_date);
        tv_member_label = (TextView) findViewById(R.id.tv_member_label);
        tv_about_label = (TextView) findViewById(R.id.tv_about_label);
        spUserInput = (Spinner) findViewById(R.id.sp_user_input);
        rr_desc = (RelativeLayout) findViewById(R.id.rr_desc);
        callDiscussionAPIByDefault(rr_desc, mSelectedTab);
        tv_discussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_edit_event.setVisibility(View.VISIBLE);
                if (mSelectedTab != 0) {
                    mSelectedTab = 0;
                    tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
                    tv_discussion.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.color_white));
                    tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_event_description.setVisibility(View.GONE);
                    rr_desc.setVisibility(View.GONE);

                    if (Util.checkNetworkAvailablity(EventsDetailsActivity.this)) {
                        getEventComment();
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(EventsDetailsActivity.this, null,
                                "", getResources().getString(R.string.alt_checknet),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                }
            }
        });

        tv_about_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_edit_event.setVisibility(View.GONE);
                if (mSelectedTab != 1) {
                    mSelectedTab = 1;
                    tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
                    tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.color_white));
                    tv_discussion.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));

                    recyclerView.setVisibility(View.GONE);
                    tv_event_description.setVisibility(View.VISIBLE);
                    rr_desc.setVisibility(View.VISIBLE);

                    if (Util.checkNetworkAvailablity(EventsDetailsActivity.this)) {
                        getEventsDetailAboutRequest();
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(EventsDetailsActivity.this, null,
                                "", getResources().getString(R.string.alt_checknet),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                }
            }
        });

        tv_member_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_edit_event.setVisibility(View.GONE);
                if (mSelectedTab != 2) {


                    if (Util.checkNetworkAvailablity(EventsDetailsActivity.this)) {
                        getEventsDetailParticipantsRequest();
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(EventsDetailsActivity.this, null,
                                "", getResources().getString(R.string.alt_checknet),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                }
            }
        });


        /**
         * SetUp Spinner
         */
        spinnerItems = new ArrayList<>();
        spinnerItems.add("Select");
        spinnerItems.add("Participate");
        spinnerItems.add("Interested");
        spinnerItems.add("Decline");
        TypedArray imgs = getResources().obtainTypedArray(R.array.pinner_imgs);

        spAdapter = new EventStatusSpinnerAdapter(
                this,
                spinnerItems, imgs);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        spUserInput.setAdapter(spAdapter);
        setSpinnetItemClickListner();
        fab_edit_event = (FloatingActionButton) findViewById(R.id.fab_edit_event);
        fab_edit_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsDetailsActivity.this, AddPostActivity.class);
                intent.putExtra("mEVENT_ID", mEVENT_ID);
                intent.putExtra("TITTLE", "Add Event Comment");
                startActivity(intent);
            }
        });
    }

    private void setSpinnetItemClickListner() {
        spUserInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    if (Util.checkNetworkAvailablity(EventsDetailsActivity.this)) {
                        getEventMemberStatusUpdate(spinnerItems.get(i));
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(EventsDetailsActivity.this, null,
                                "", getResources().getString(R.string.alt_checknet),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void callDiscussionAPIByDefault(RelativeLayout rr_desc, int mSelectedTab) {
        if (mSelectedTab == -1 || mSelectedTab == 0) {
            this.mSelectedTab = 0;
            tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
            tv_discussion.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.color_white));
            tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
            recyclerView.setVisibility(View.VISIBLE);
            tv_event_description.setVisibility(View.GONE);
            rr_desc.setVisibility(View.GONE);

            if (Util.checkNetworkAvailablity(EventsDetailsActivity.this)) {
                getEventComment();
            } else {
                CustomDialog customDialog1 = new CustomDialog(EventsDetailsActivity.this, null,
                        "", getResources().getString(R.string.alt_checknet),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        } else if (mSelectedTab == 1) {
            tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
            tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.color_white));
            tv_discussion.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));

            recyclerView.setVisibility(View.GONE);
            tv_event_description.setVisibility(View.VISIBLE);
            rr_desc.setVisibility(View.VISIBLE);

            if (Util.checkNetworkAvailablity(EventsDetailsActivity.this)) {
                getEventsDetailAboutRequest();
            } else {
                CustomDialog customDialog1 = new CustomDialog(EventsDetailsActivity.this, null,
                        "", getResources().getString(R.string.alt_checknet),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        } else if (mSelectedTab == 2) {
            tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.color_white));
            tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
            tv_discussion.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
            recyclerView.setVisibility(View.VISIBLE);
            tv_event_description.setVisibility(View.GONE);
            rr_desc.setVisibility(View.GONE);

            if (Util.checkNetworkAvailablity(EventsDetailsActivity.this)) {
                getEventsDetailParticipantsRequest();
            } else {
                CustomDialog customDialog1 = new CustomDialog(EventsDetailsActivity.this, null,
                        "", getResources().getString(R.string.alt_checknet),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        }
    }

    private void getEventMemberStatusUpdate(String selectedPosition) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callEventstatusUpdate = apiService.getEventMemberStatusUpdate(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", EventsDetailsActivity.this),
                mEVENT_ID, selectedPosition);

        callEventstatusUpdate.enqueue(new Callback<EventStatusUpdateResponse>() {

            @Override
            public void onResponse(Call<EventStatusUpdateResponse> call,
                                   Response<EventStatusUpdateResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

//                switch (c) {
//                    case "2": // TODO Success response  task here and progress loader
                EventStatusUpdateResponse loginResponse = response.body();
                if (loginResponse.getStatus().equalsIgnoreCase("success")) {
                    CustomDialog customDialog1 = new CustomDialog(EventsDetailsActivity.this, EventsDetailsActivity.this,
                            "", loginResponse.getMsg(),
                            "EVENTUPDATED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();

                } else {
                    CustomDialog customDialog1 = new CustomDialog(EventsDetailsActivity.this, EventsDetailsActivity.this,
                            "", loginResponse.getMsg(),
                            "EVENTUPDATED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
//                        break;
//                }
            }

            @Override
            public void onFailure(Call<EventStatusUpdateResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.getMessage().toString());
                CustomDialog customDialog1 = new CustomDialog(EventsDetailsActivity.this, EventsDetailsActivity.this,
                        "", t.getMessage().toString(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpCommentsList(List<AllpostsItem> allposts) {

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(EventsDetailsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        EventDetailFeedsAdapter adapter = new EventDetailFeedsAdapter(
                EventsDetailsActivity.this, allposts,
                EventsDetailsActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
//        swipeRefresh.setRefreshing(false);
    }

    private void setUpEventMembersGrid(ArrayList<EventMemberDetailsResponse> items) {

        EventParticipantsGridAdapter adapter = new EventParticipantsGridAdapter(this, items);
        int spacingInPixels = getResources()
                .getDimensionPixelSize(R.dimen.dimen_ten);
        recyclerView.addItemDecoration(new
                GridSpacingItemDecoration(4, spacingInPixels,
                true, 0));
        GridLayoutManager lLayout = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClickListener(String s, int position) {

    }

    @Override
    public void onCommentClickListener(String s, int position) {

    }

    private void getEventComment() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callComments = apiService.getEventComment(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", EventsDetailsActivity.this),
                mEVENT_ID);

        callComments.enqueue(new Callback<EventDetailDiscussionResponse>() {

            @Override
            public void onResponse(Call<EventDetailDiscussionResponse> call,
                                   Response<EventDetailDiscussionResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        EventDetailDiscussionResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allposts = loginResponse.getData().getAllposts();
                            setUpCommentsList(allposts);

                            tv_events_date.setText(Html.fromHtml(Util.isNull(Util.eventDetailDateFormator(loginResponse.getData().getEvent().getEventStartDate()))
                                    + " - " + Util.isNull(Util.eventDetailDateFormator(loginResponse.getData().getEvent().getEventEndDate()))));
                            Glide.with(EventsDetailsActivity.this)
                                    .load(Util.isNull(loginResponse.getData().getEvent().getEventImage()))
                                    .placeholder(R.drawable.ic_create_church_placeholder)
                                    .skipMemoryCache(false)
                                    .into(iv_event_image);
                            if (loginResponse.getData().getIsEventMemberStatus().equalsIgnoreCase("not_a_member")) {
                                spUserInput.setOnItemSelectedListener(null);
//                                spUserInput.setSelection(0);
                            } else if (loginResponse.getData().getIsEventMemberStatus().equalsIgnoreCase("Participate")) {
                                spUserInput.setOnItemSelectedListener(null);
//                                spUserInput.setSelection(1);
                            }
                            setSpinnetItemClickListner();
                        } else {
                            recyclerView.setAdapter(null);
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<EventDetailDiscussionResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getEventsDetailAboutRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callDetailAbout = apiService.getEventDetailAbout(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", EventsDetailsActivity.this),
                mEVENT_ID);

        callDetailAbout.enqueue(new Callback<EventDetailAboutResponse>() {

            @Override
            public void onResponse(Call<EventDetailAboutResponse> call,
                                   Response<EventDetailAboutResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        aboutResponse = response.body();
                        if (aboutResponse.getData() != null) {
                            tv_event_name.setText(Util.isNull(aboutResponse.getData().getEvent().getEventName()));
                            tv_event_address.setText(Util.isNull(aboutResponse.getData().getEvent().getEventLocation()));
                            tv_event_time.setText(Util.isNull(aboutResponse.getData().getEvent().getEventStartTime()) + " - "
                                    + Util.isNull(aboutResponse.getData().getEvent().getEventEndTime()));
                            tv_event_description.setText(Util.isNull(aboutResponse.getData().getEvent().getEventDescription()));
                            tv_events_date.setText(Html.fromHtml(Util.isNull(Util.eventDetailDateFormator(aboutResponse.getData().getEvent().getEventStartDate()))
                                    + " - " + Util.isNull(Util.eventDetailDateFormator(aboutResponse.getData().getEvent().getEventEndDate()))));
                            Glide.with(EventsDetailsActivity.this)
                                    .load(Util.isNull(aboutResponse.getData().getEvent().getEventImage()))
                                    .placeholder(R.drawable.ic_create_church_placeholder)
                                    .skipMemoryCache(false)
                                    .into(iv_event_image);
                        } else {
                            recyclerView.setAdapter(null);
                        }

                        if (aboutResponse.getData() != null) {
                            if (aboutResponse.getData().getEvent() != null
                                    && aboutResponse.getData().getEvent().getAddedBy() != null
                                    && aboutResponse.getData().getEvent().getAddedBy()
                                    .equalsIgnoreCase(Util.loadPrefrence(Util.PREF_USER_ID, "", EventsDetailsActivity.this))) {
                                tv_edit.setVisibility(View.VISIBLE);
                            } else {
                                tv_edit.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<EventDetailAboutResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getEventsDetailParticipantsRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callParticipants = apiService.getEventDetailParticipants(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", EventsDetailsActivity.this),
                mEVENT_ID);

        callParticipants.enqueue(new Callback<EventDetailParticipantsResponse>() {

            @Override
            public void onResponse(Call<EventDetailParticipantsResponse> call,
                                   Response<EventDetailParticipantsResponse> response) {
                hideProgressDialog();
                mSelectedTab = 2;
                tv_member_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.color_white));
                tv_about_label.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
                tv_discussion.setTextColor(ContextCompat.getColor(EventsDetailsActivity.this, R.color.unselected_text_color));
                recyclerView.setVisibility(View.VISIBLE);
                tv_event_description.setVisibility(View.GONE);
                rr_desc.setVisibility(View.GONE);

                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        EventDetailParticipantsResponse participantsResponse = response.body();
                        if (participantsResponse.getData() != null) {
                            mParticipantsList = participantsResponse.getData().getEventMemberDetails();
                            setUpEventMembersGrid(mParticipantsList);
                        } else {
                            recyclerView.setAdapter(null);
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<EventDetailParticipantsResponse> call, Throwable t) {
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
    protected void onDestroy() {
        if (callComments != null && callComments.isExecuted()) {
            callComments.cancel();
        } else if (callDetailAbout != null && callDetailAbout.isExecuted()) {
            callDetailAbout.cancel();
        } else if (callParticipants != null && callParticipants.isExecuted()) {
            callParticipants.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        if (param.equalsIgnoreCase("EVENTUPDATED")) {

        }
    }

}
