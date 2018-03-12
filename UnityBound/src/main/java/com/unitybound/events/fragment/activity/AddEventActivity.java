package com.unitybound.events.fragment.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.beans.ProfileFriendsResponse;
import com.unitybound.events.fragment.adapter.ProfileFriendsAddEventAdapter;
import com.unitybound.events.fragment.beans.addEvent.AddEventResponse;
import com.unitybound.events.fragment.beans.eventDetailAbout.Event;
import com.unitybound.events.fragment.beans.eventDetailAbout.EventDetailAboutResponse;
import com.unitybound.events.fragment.beans.updateEvent.UpdateEventResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventActivity extends ActivityManagePermission implements CustomDialog.IDialogListener {

    private static final int TIME_DIALOG_ID = 999;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_group_photo)
    ImageView ivGroupPhoto;
    @BindView(R.id.tv_upload_photo)
    TextView tvUploadPhoto;
    @BindView(R.id.edt_event_name)
    EditText edtEventName;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.edt_event_venue)
    EditText edtEventVenue;
    @BindView(R.id.ll_3)
    LinearLayout ll3;
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.ll_4)
    LinearLayout ll4;
    @BindView(R.id.edt_address1)
    EditText edtAddress1;
    @BindView(R.id.ll_5)
    LinearLayout ll5;
    @BindView(R.id.edt_address2)
    EditText edtAddress2;
    @BindView(R.id.ll_6)
    LinearLayout ll6;
    @BindView(R.id.ll_7)
    LinearLayout ll7;
    @BindView(R.id.ll_8)
    LinearLayout ll8;
    @BindView(R.id.ll_9)
    LinearLayout ll9;
    @BindView(R.id.ll_10)
    LinearLayout ll10;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.ll_12)
    LinearLayout ll12;
    @BindView(R.id.root)
    RelativeLayout rootView;
    @BindView(R.id.rd_public)
    RadioButton rd_public;
    @BindView(R.id.rd_private)
    RadioButton rd_private;
    @BindView(R.id.edt_city_name)
    EditText edtCityName;
    @BindView(R.id.edt_state_name)
    EditText edtStateName;
    @BindView(R.id.edt_zip_code)
    EditText edtZipCode;
    @BindView(R.id.edt_country)
    EditText edtCountry;
    @BindView(R.id.edt_start_date)
    EditText edtStartDate;
    @BindView(R.id.edt_end_date)
    EditText edtEndDate;
    private ProgressDialog mProgressDialog = null;
    private Call<AddEventResponse> callAddChurch = null;
    private ArrayList<String> arrayListDenomination = null;
    private int SELECT_IMAGE = 0;
    private int REQUEST_CAMERA = 1;
    final CharSequence[] items = {"Take Photo", "Gallery", "Cancel"};
    private String userChoosenTask = null;
    private String mPath = null;
    private Bitmap bm = null;
    private Call<ProfileFriendsResponse> callProfileFriends = null;
    private RecyclerView recyclerView;
    private ProfileFriendsAddEventAdapter adapter = null;
    private String mEVENT_ID = null;
    private EventDetailAboutResponse mData = null;
    private Call<UpdateEventResponse> callUpdateEvent = null;
//    private TimePicker timePicker1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(AddEventActivity.this, R.style.newDialog);

        if (getIntent() != null && getIntent().getExtras()!=null && getIntent().getExtras().containsKey("EVENT_ID")) {
            mEVENT_ID = getIntent().getStringExtra("EVENT_ID");
            mData = getIntent().getExtras().getParcelable("data");
            setAllValuesOnUI();
            btnCreate.setText("Update");
        }
        setUpToolbarLayout();
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_layout);
//        getEventsDetailAboutRequest();

        if (Util.checkNetworkAvailablity(AddEventActivity.this)) {
            getEventsDetailAboutRequest();
        } else {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    private void setAllValuesOnUI() {
        if(mData!=null){
            Event mEventData = mData.getData().getEvent();
            if (Util.isNull(mEventData.getEventImage()).length()>0){
                Glide.with(AddEventActivity.this)
                        .load(mEventData.getEventImage())
                        .placeholder(R.drawable.ic_photos_placeholder)
                        .skipMemoryCache(false)
                        .into(ivGroupPhoto);
            }
            edtEventName.setText(Util.isNull(mEventData.getEventName()));
            if (mEventData.getEventScope().equalsIgnoreCase("Public")){
                rd_public.setChecked(true);
            }else if (mEventData.getEventScope().equalsIgnoreCase("Private")){
                rd_private.setChecked(true);
            }else{
                rd_private.setChecked(false);
                rd_public.setChecked(false);
            }
            edtEventVenue.setText(Util.isNull(mEventData.getEventLocation()));
            edtDescription.setText(Util.isNull(mEventData.getEventDescription()));
            edtAddress1.setText(Util.isNull(mEventData.getEventCity()
                    +" "+mEventData.getEventState()
                    +" "+mEventData.getEventCountry()));
            edtCityName.setText(Util.isNull(mEventData.getEventCity()));
            edtStateName.setText(Util.isNull(mEventData.getEventState()));
            edtZipCode.setText(Util.isNull(mEventData.getEventZip()));
            edtCountry.setText(Util.isNull(mEventData.getEventCountry()));
            edtStartDate.setText(Util.isNull(mEventData.getEventStartDate()));
            edtEndDate.setText(Util.isNull(mEventData.getEventEndDate()));
        }

    }

    private void setUpToolbarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Event");
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


    @OnClick({R.id.tv_upload_photo,R.id.iv_group_photo, R.id.btn_create, R.id.edt_start_date,
            R.id.edt_end_date,R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_upload_photo:
                selectImage(permissionAsk);
                break;
            case R.id.iv_group_photo:
                selectImage(permissionAsk);
                break;

            case R.id.btn_create:
                if (validation()) {
                    if (Util.checkNetworkAvailablity(AddEventActivity.this)) {
                        if (mEVENT_ID!=null && mEVENT_ID.length()>0){
                            if (mPath != null) {
                                updateEventRequest(new File(mPath));
                            } else {
                                updateEventRequest(null);
                            }
                        } else {
                            if (mPath != null) {
                                addEventRequest(new File(mPath));
                            } else {
                                addEventRequest(null);
                            }
                        }
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this,
                                null,
                                "", getResources().getString(R.string.alt_checknet),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                }
                break;
            case R.id.btn_cancel:
                onBackPressed();
                break;
            case R.id.edt_start_date:
                showDatePicker(edtStartDate.getText().toString());
                break;
            case R.id.edt_end_date:
                showDialog(TIME_DIALOG_ID);
                break;
        }
    }

    private boolean validation() {
        if (Util.isNull(edtEventName.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", "Please enter event name",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (!rd_public.isChecked() && !rd_private.isChecked()) {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", "Please enter event type",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtEventVenue.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", "Please enter event venue",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtDescription.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", "Please enter event description",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtAddress1.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", "Please enter event address",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
//        else if (Util.isNull(edtAddress2.getText().toString()).length() == 0) {
//            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
//                    "", "Please enter event Address 2",
//                    "ONFAILED");
//            if (customDialog1.isShowing()) {
//                customDialog1.dismiss();
//            }
//            customDialog1.show();
//        }
        else if (Util.isNull(edtCityName.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", "Please enter event city",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtStateName.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", "Please enter event state",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtZipCode.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", "Please enter event zip-code",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtStartDate.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", "Please enter event start date",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtEndDate.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                    "", "Please enter event time",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else {
            return true;
        }
        return false;
    }


    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        if (param.equalsIgnoreCase("CLOSE")) {
            onBackPressed();
        }
    }

    private void addEventRequest(File file) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        MultipartBody.Part body = null;
        RequestBody requestFile = null;
        if (file != null) {
            requestFile =
                    RequestBody.create(
                            MediaType.parse("image"),
                            file
                    );
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("myfile", file.getName(), requestFile);
        }

        // create a map of data to pass along
        RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
        RequestBody user_id = Util.createPartFromString(""
                + Util.loadPrefrence(Util.PREF_USER_ID, "",
                AddEventActivity.this));
        ///

        RequestBody eventName = Util.createPartFromString(Util.isNull(edtEventName.getText().toString()));
        RequestBody eventDescription = Util.createPartFromString(Util.isNull(edtDescription.getText().toString()));
        String mEventType = rd_public.isChecked() ? "Public" : rd_private.isChecked() ? "Private" : "Public";
        RequestBody eventScope = Util.createPartFromString(mEventType);
        RequestBody eventLocation = Util.createPartFromString(Util.isNull(edtAddress1.getText().toString()));
        RequestBody eventCity = Util.createPartFromString(Util.isNull(edtCityName.getText().toString()));
        RequestBody eventState = Util.createPartFromString(Util.isNull(edtStartDate.getText().toString()));
        RequestBody eventZip = Util.createPartFromString(Util.isNull(edtZipCode.getText().toString()));
        RequestBody eventCountry = Util.createPartFromString(Util.isNull(edtCountry.getText().toString()));
        RequestBody eventStartDate = Util.createPartFromString(Util.isNull(edtStartDate.getText().toString()));
        RequestBody eventStartTime = Util.createPartFromString(Util.isNull(edtEndDate.getText().toString()));


        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("eventName", eventName);
        map.put("eventDescription", eventDescription);
        map.put("eventScope", eventScope);
        map.put("eventLocation", eventLocation);
        map.put("eventCity", eventCity);
        map.put("eventState", eventState);
        map.put("eventZip", eventZip);
        map.put("eventCountry", eventCountry);
        map.put("eventStartDate", eventStartDate);
        map.put("eventStartTime", eventStartTime);
        Log.d("nik",adapter.getAllCheckedBox().size()+" Selected Friends");

        callAddChurch = apiService.
                addEvent(
                        map,
                        body);
        callAddChurch.enqueue(new Callback<AddEventResponse>()

        {

            @Override
            public void onResponse
                    (Call<AddEventResponse> call, Response<AddEventResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        AddEventResponse loginResponse = response.body();
                        if (loginResponse.getStatus().equalsIgnoreCase("success")) {
//                            Toast.makeText(AddEventActivity.this, loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            CustomDialog customDialog = new CustomDialog(AddEventActivity.this,
                                    "Thank you for creating Event",
                                    loginResponse.getMsg(),
                                    AddEventActivity.this);
                            customDialog.show();

//                            CustomDialog customDialog = new CustomDialog(AddEventActivity.this,
//                                    "Thank you for creating Event",
//                                    "Lorem Ipsum is simply dummy text of the printing and typesetting " +
//                                            "industry. " +
//                                            "Lorem Ipsum has been the industry's standard dummy text ever" +
//                                            " since the 1500s, when an unknown printer took a galley of" +
//                                            " type and scrambled it to make a type specimen book.",
//                                    AddEventActivity.this);
//                            customDialog.show();
                        } else {
                            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                                    "", loginResponse.getMsg(),
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        }
                        break;
                }

            }

            @Override
            public void onFailure(Call<AddEventResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                        "", t.getMessage(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    private void updateEventRequest(File file) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        MultipartBody.Part body = null;
        RequestBody requestFile = null;
        if (file != null) {
            requestFile =
                    RequestBody.create(
                            MediaType.parse("image"),
                            file
                    );
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("myfile", file.getName(), requestFile);
        }

        // create a map of data to pass along
        RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
        RequestBody user_id = Util.createPartFromString(""
                + Util.loadPrefrence(Util.PREF_USER_ID, "",
                AddEventActivity.this));
        ///

        RequestBody eventName = Util.createPartFromString(Util.isNull(edtEventName.getText().toString()));
        RequestBody eventDescription = Util.createPartFromString(Util.isNull(edtDescription.getText().toString()));
        String mEventType = rd_public.isChecked() ? "Public" : rd_private.isChecked() ? "Private" : "Public";
        RequestBody eventScope = Util.createPartFromString(mEventType);
        RequestBody eventLocation = Util.createPartFromString(Util.isNull(edtAddress1.getText().toString()));
        RequestBody eventCity = Util.createPartFromString(Util.isNull(edtCityName.getText().toString()));
        RequestBody eventState = Util.createPartFromString(Util.isNull(edtStartDate.getText().toString()));
        RequestBody eventZip = Util.createPartFromString(Util.isNull(edtZipCode.getText().toString()));
        RequestBody eventCountry = Util.createPartFromString(Util.isNull(edtCountry.getText().toString()));
        RequestBody eventStartDate = Util.createPartFromString(Util.isNull(edtStartDate.getText().toString()));
        RequestBody eventStartTime = Util.createPartFromString(Util.isNull(edtEndDate.getText().toString()));
        RequestBody EVENT_ID = Util.createPartFromString(Util.isNull(mEVENT_ID));


        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("event_id",EVENT_ID );
        map.put("eventName", eventName);
        map.put("eventDescription", eventDescription);
        map.put("eventScope", eventScope);
        map.put("eventLocation", eventLocation);
        map.put("eventCity", eventCity);
        map.put("eventState", eventState);
        map.put("eventZip", eventZip);
        map.put("eventCountry", eventCountry);
        map.put("eventStartDate", eventStartDate);
        map.put("eventStartTime", eventStartTime);
        Log.d("nik",adapter.getAllCheckedBox().size()+" Selected Friends");

        callUpdateEvent = apiService.
                updateEvent(
                        map,
                        body);
        callUpdateEvent.enqueue(new Callback<UpdateEventResponse>()

        {

            @Override
            public void onResponse
                    (Call<UpdateEventResponse> call, Response<UpdateEventResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        UpdateEventResponse loginResponse = response.body();
                        if (loginResponse.getStatus().equalsIgnoreCase("success")) {
//                            Toast.makeText(AddEventActivity.this, loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                                    "", loginResponse.getMsg(),
                                    "CLOSE");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        } else {
                            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                                    "", loginResponse.getMsg(),
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        }
                        break;
                }

            }

            @Override
            public void onFailure(Call<UpdateEventResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
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

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_IMAGE);

        Intent intent = new Intent(this, AlbumSelectActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
    }

    String permissionAsk[] = {PermissionUtils.Manifest_CAMERA,
            PermissionUtils.Manifest_READ_EXTERNAL_STORAGE,
            PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};

    private void selectImage(final String[] permissionAsk) {

        askCompactPermissions(permissionAsk, new PermissionResult() {
            @Override
            public void permissionGranted() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
                builder.setTitle("Select image");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        boolean result = Util.checkPermission(AddEventActivity.this);
                        if (items[item].equals("Take Photo")) {
                            userChoosenTask = "Take Photo";
                            if (result)
                                cameraIntent();
                        } else if (items[item].equals("Gallery")) {
                            userChoosenTask = "Gallery";
                            if (result)
                                galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }

            @Override
            public void permissionDenied() {
                Snackbar snackbar = Snackbar
                        .make(rootView, "All Permission are needed to run wegil!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Allow", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectImage(permissionAsk);
                            }
                        });
                snackbar.setActionTextColor(Color.RED);

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }

            @Override
            public void permissionForeverDenied() {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_IMAGE) {
//                onSelectFromGalleryResult(data, SELECT_IMAGE);
            if (requestCode == ConstantsCustomGallery.REQUEST_CODE && data != null) {
                //The array list has the image paths of the selected images
                ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

                for (int i = 0; i < images.size(); i++) {
                    Uri uri = Uri.fromFile(new File(images.get(i).path));

                    File imgFile = new File(images.get(i).path);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        ivGroupPhoto.setImageBitmap(myBitmap);
                        myBitmap = null;
                    }

                    // start play with image uri
                    mPath = images.get(i).path;

                }

            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPath = destination.getAbsolutePath();
        ivGroupPhoto.setImageBitmap(thumbnail);
    }


    //----------DATE PICKER----------------------------------------------------------------------------
    @SuppressWarnings("deprecation")
    public void showDatePicker(String selectedDate) {
        Calendar calendar = Calendar.getInstance();
        int mTyear = calendar.get(Calendar.YEAR);
        int mTmonth = calendar.get(Calendar.MONTH);
        int mTday = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                ToDateListener, mTyear, mTmonth,
                mTday);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        if (selectedDate.length() > 0) {
            String[] separated = selectedDate.split("-");
            Log.d("Date ", "" + separated[0] + "-" + Integer.valueOf(separated[1]) + "-" + Integer.valueOf(separated[2]));
            datePickerDialog.updateDate(Integer.valueOf(separated[0]), Integer.valueOf(separated[1]) - 1, Integer.valueOf(separated[2]));
        }
        datePickerDialog.show();
    }

    private int mTyear, mTmonth, mTday;
    private DatePickerDialog.OnDateSetListener ToDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            mTyear = arg1;
            mTmonth = arg2 + 1;
            mTday = arg3;
            setToDate(arg1, arg2 + 1, arg3);
        }
    };

    /**
     * @param year  the year
     * @param month the month
     * @param day   the day
     */
    private void setToDate(int year, int month, int day) {
        this.mTyear = year;
        this.mTmonth = month;
        this.mTday = day;
       /*
        edt_to_date.setText(new StringBuilder().append(year).append("-")
                .append((month < 10 ? "0" + month : month)).append("-").append(day < 10 ? "0" + day : day));
        edt_to_date.setError(null);
        */

        StringBuilder fromDate = new StringBuilder().append(day < 10 ? "0" + day : day).append("-")
                .append((month < 10 ? "0" + month : month)).append("-").append(year);
        edtStartDate.setText(fromDate);
//            mListner.onFromDateSelected(fromDate.toString());

    }

    //    --------------Time Picker--------------------------------------------------------------------------------------
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute, false);

        }
        return null;
    }

    private int hour;
    private int minute;
    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    String AM_PM;
                    if (hour < 12) {
                        AM_PM = " AM";
                    } else {
                        AM_PM = " PM";
                    }
                    // set current time into textview
                    edtEndDate.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)).append(AM_PM));

                    // set current time into timepicker
//                    timePicker1.setCurrentHour(hour);
//                    timePicker1.setCurrentMinute(minute);

                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void getEventsDetailAboutRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String userId = Util.loadPrefrence(Util.PREF_USER_ID, "", AddEventActivity.this);
        callProfileFriends = apiService.profileFriendRequestList(
                BuildConfig.API_KEY,
                userId,
                userId);

        callProfileFriends.enqueue(new Callback<ProfileFriendsResponse>() {

            @Override
            public void onResponse(Call<ProfileFriendsResponse> call,
                                   Response<ProfileFriendsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        ProfileFriendsResponse profileFriendsResponse = response.body();
                        if (profileFriendsResponse.getData() != null && profileFriendsResponse.getStatus().equalsIgnoreCase("success")) {
                            adapter =
                                    new ProfileFriendsAddEventAdapter(AddEventActivity.this,
                                            profileFriendsResponse.getData().getFriends());

                            LinearLayoutManager lLayout = new LinearLayoutManager(AddEventActivity.this);
                            recyclerView.setLayoutManager(lLayout);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(adapter);
                        } else {
                            recyclerView.setAdapter(null);
                            CustomDialog customDialog1 = new CustomDialog(AddEventActivity.this, null,
                                    "", profileFriendsResponse.getMsg(),
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProfileFriendsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

}
