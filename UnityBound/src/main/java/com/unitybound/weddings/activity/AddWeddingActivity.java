package com.unitybound.weddings.activity;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.events.fragment.adapter.ProfileFriendsAddEventAdapter;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;
import com.unitybound.weddings.beans.WeadingDetailResponse.WeddingDetails;
import com.unitybound.weddings.beans.addWedding.AddWeddingResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

public class AddWeddingActivity extends ActivityManagePermission implements CustomDialog.IDialogListener {

    private static final int TIME_DIALOG_ID = 999;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_group_photo)
    ImageView ivGroupPhoto;
    @BindView(R.id.tv_upload_photo)
    TextView tvUploadPhoto;
    @BindView(R.id.tv_uploadpics)
    TextView tv_uploadpics;
    @BindView(R.id.edt_bride_name)
    EditText edtBrideName;
    @BindView(R.id.edt_bride_mother_name)
    EditText edtBrideMotherName;
    @BindView(R.id.edt_bride_father_name)
    EditText edtBrideFatherName;
    @BindView(R.id.edt_groom_name)
    EditText edtGroomName;
    @BindView(R.id.edt_groom_mother_name)
    EditText edtGroomMotherName;
    @BindView(R.id.edt_groom_father_name)
    EditText edtGroomFatherName;
    @BindView(R.id.edt_start_date)
    EditText edtStartDate;
    @BindView(R.id.edt_end_date)
    EditText edtEndDate;
    @BindView(R.id.edt_address1)
    EditText edtAddress1;
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @BindView(R.id.edt_wedding_zip_code)
    EditText edtWeddingZipCode;
    @BindView(R.id.edt_gift_tittle1)
    EditText edtGiftTittle1;
    @BindView(R.id.edt_gift_url1)
    EditText edtGiftUrl1;
    @BindView(R.id.edt_gift_tittle2)
    EditText edtGiftTittle2;
    @BindView(R.id.edt_gift_url2)
    EditText edtGiftUrl2;
    @BindView(R.id.edt_gift_tittle3)
    EditText edtGiftTittle3;
    @BindView(R.id.edt_gift_url3)
    EditText edtGiftUrl3;

    @BindView(R.id.edt_wedding_album1)
    EditText edtWeddingAlbum1;
    @BindView(R.id.edt_album_url1)
    EditText edtWeddingAlbumUrl1;
    @BindView(R.id.edt_album_tittle2)
    EditText edtWeddingAlbum2;
    @BindView(R.id.edt_album_url2)
    EditText edtWeddingAlbumUrl2;
    @BindView(R.id.edt_album_tittle3)
    EditText edtWeddingAlbum3;
    @BindView(R.id.edt_album_url3)
    EditText edtWeddingAlbumUrl3;

    @BindView(R.id.iv_wedding_image1)
    ImageView ivWeddingImage1;
    @BindView(R.id.iv_wedding_image2)
    ImageView ivWeddingImage2;
    @BindView(R.id.iv_wedding_image3)
    ImageView ivWeddingImage3;
    @BindView(R.id.iv_wedding_image4)
    ImageView ivWeddingImage4;
    @BindView(R.id.iv_wedding_image5)
    ImageView ivWeddingImage5;
    @BindView(R.id.iv_wedding_image6)
    ImageView ivWeddingImage6;
    @BindView(R.id.iv_wedding_image7)
    ImageView ivWeddingImage7;
    @BindView(R.id.ll_upload_photos1)
    LinearLayout llUploadPhotos1;
    @BindView(R.id.ll_upload_photos2)
    LinearLayout llUploadPhotos2;

    @BindView(R.id.root)
    RelativeLayout root;

    private ProgressDialog mProgressDialog = null;
    private Call<AddWeddingResponse> callAddWeadding = null;
    private ArrayList<String> arrayListDenomination = null;
    private int SELECT_IMAGE = 0;
    private int REQUEST_CAMERA = 1;
    final CharSequence[] items = {"Take Photo", "Gallery", "Cancel"};
    private String userChoosenTask = null;
    private String mPath = null;
    private Bitmap bm = null;
    private ProfileFriendsAddEventAdapter adapter = null;
    private String mWEDDING_ID = null;
    private WeddingDetails mData = null;
    private ArrayList<Image> mFilePathList = null;
    String permissionAsk[] = {PermissionUtils.Manifest_CAMERA,
            PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};
    private int mSelectionImageType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_weadding_layout);
        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(AddWeddingActivity.this, R.style.newDialog);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("WEDDING_ID")) {
            mWEDDING_ID = getIntent().getStringExtra("WEDDING_ID");
            mData = getIntent().getExtras().getParcelable("data");
            setAllValuesOnUI();
        }

        setUpToolbarLayout();
    }

    private void setAllValuesOnUI() {
        if (mData != null) {
            if (Util.isNull(mData.getWeddingImage()).length() > 0) {
                Glide.with(AddWeddingActivity.this)
                        .load(mData.getWeddingImage())
                        .placeholder(R.drawable.ic_photos_placeholder)
                        .skipMemoryCache(false)
                        .into(ivGroupPhoto);
            }
            edtBrideName.setText(Util.isNull(mData.getWeddingBrideName()));
            edtBrideMotherName.setText(Util.isNull(mData.getWeddingBrideMotherName()));
            edtBrideFatherName.setText(Util.isNull(mData.getWeddingBrideFatherName()));
            edtGroomName.setText(Util.isNull(mData.getWeddingBrideName()));
            edtGroomMotherName.setText(Util.isNull(mData.getWeddingGroomMotherName()));
            edtGroomFatherName.setText(Util.isNull(mData.getWeddingBrideFatherName()));
            edtStartDate.setText(Util.isNull(mData.getWeddingDate()));
            edtEndDate.setText(Util.isNull(mData.getWeddingTime()));
            edtAddress1.setText(Util.isNull(mData.getWeddingLocation()));
            edtDescription.setText(Util.isNull(mData.getWeddingDescription()));
            btnCreate.setText("Update");
        }

    }

    private void setUpToolbarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Wedding");
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

    private boolean validation() {
        if (Util.isNull(edtBrideName.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter bride name",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtBrideFatherName.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter bride father name",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtBrideMotherName.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter bride mother name",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtGroomName.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter groom name",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtGroomFatherName.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter groom father name",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtGroomMotherName.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter groom mother name",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtStartDate.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter wedding date",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtEndDate.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter wedding time",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtDescription.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter event description",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtAddress1.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter event address",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtAddress1.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter wedding address",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtDescription.getText().toString()).length() == 0) {
            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
                    "", "Please enter wedding address",
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

    private void addWeddingRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        MultipartBody.Part body = null;
        MultipartBody.Part[] partsArray = null;
        RequestBody requestFile = null;


        if (mPath != null && mPath.length() > 0) {
            File file1 = new File(mPath);
            if (file1.exists()) {
                requestFile =
                        RequestBody.create(
                                MediaType.parse("image"),
                                file1);
                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("myfile", file1.getName(), requestFile);
            }
        }

        if (mFilePathList != null && mFilePathList.size() > 0) {

            partsArray = new MultipartBody.Part[mFilePathList.size()];
            for (int i = 0; i < mFilePathList.size(); i++) {
                File file = new File(mFilePathList.get(i).path);

                Log.d("nik", "requestUploadSurvey: survey image " + i + "  " + mFilePathList.get(i).path);
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image"), file);
                partsArray[i] = MultipartBody.Part.createFormData("wedding_image[]", file.getName(), surveyBody);
            }
        }

        // create a map of data to pass along
        RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
        RequestBody user_id = Util.createPartFromString(""
                + Util.loadPrefrence(Util.PREF_USER_ID, "",
                AddWeddingActivity.this));
        ///

        RequestBody GroomName = Util.createPartFromString(Util.isNull(edtGroomName.getText().toString()));
        RequestBody GroomFatherName = Util.createPartFromString(Util.isNull(edtGroomFatherName.getText().toString()));
        RequestBody GroomMotherName = Util.createPartFromString(Util.isNull(edtGroomMotherName.getText().toString()));
        RequestBody BrideName = Util.createPartFromString(Util.isNull(edtBrideName.getText().toString()));
        RequestBody BrideMotherName = Util.createPartFromString(Util.isNull(edtBrideMotherName.getText().toString()));
        RequestBody BrideFatherName = Util.createPartFromString(Util.isNull(edtBrideFatherName.getText().toString()));
        RequestBody Date = Util.createPartFromString(Util.isNull(edtStartDate.getText().toString()));
        RequestBody Time = Util.createPartFromString(Util.isNull(edtEndDate.getText().toString()));
        RequestBody location = Util.createPartFromString(Util.isNull(edtAddress1.getText().toString()));
        RequestBody desc = Util.createPartFromString(Util.isNull(edtDescription.getText().toString()));
        RequestBody ZipCode = Util.createPartFromString(Util.isNull(edtWeddingZipCode.getText().toString()));
        // Wedding Gift Registry
        RequestBody edtGiftTittle_1 = Util.createPartFromString(Util.isNull(edtGiftTittle1.getText().toString()));
        RequestBody edtGiftUrl_1 = Util.createPartFromString(Util.isNull(edtGiftUrl1.getText().toString()));
        RequestBody edtGiftTittle_2 = Util.createPartFromString(Util.isNull(edtGiftTittle2.getText().toString()));
        RequestBody edtGiftUrl_2 = Util.createPartFromString(Util.isNull(edtGiftUrl2.getText().toString()));
        RequestBody edtGiftTittle_3 = Util.createPartFromString(Util.isNull(edtGiftTittle3.getText().toString()));
        RequestBody edtGiftUrl_3 = Util.createPartFromString(Util.isNull(edtGiftUrl3.getText().toString()));
        // Wedding Album
        RequestBody edtWeddingAlbum_1 = Util.createPartFromString(Util.isNull(edtWeddingAlbum1.getText().toString()));
        RequestBody edtWeddingAlbumUrl1_1 = Util.createPartFromString(Util.isNull(edtWeddingAlbumUrl1.getText().toString()));
        RequestBody edtWeddingAlbum_2 = Util.createPartFromString(Util.isNull(edtWeddingAlbum2.getText().toString()));
        RequestBody edtWeddingAlbumUrl1_2 = Util.createPartFromString(Util.isNull(edtWeddingAlbumUrl2.getText().toString()));
        RequestBody edtWeddingAlbum_3 = Util.createPartFromString(Util.isNull(edtWeddingAlbum3.getText().toString()));
        RequestBody edtWeddingAlbumUrl1_3 = Util.createPartFromString(Util.isNull(edtWeddingAlbumUrl3.getText().toString()));

        RequestBody mwedding_id = Util.createPartFromString(Util.isNull(mWEDDING_ID));


        HashMap<String, RequestBody> map = new HashMap<>();

        map.put("weddingGiftTitle1", edtGiftTittle_1);
        map.put("weddingGiftUrl1", edtGiftUrl_1);
        map.put("weddingGiftTitle2", edtGiftTittle_2);
        map.put("weddingGiftUrl2", edtGiftUrl_2);
        map.put("weddingGiftTitle3", edtGiftTittle_3);
        map.put("weddingGiftUrl3", edtGiftUrl_3);

        map.put("weddingAlbumTitle1", edtWeddingAlbum_1);
        map.put("weddingAlbumUrl1", edtWeddingAlbumUrl1_1);
        map.put("weddingAlbumTitle1", edtWeddingAlbum_2);
        map.put("weddingAlbumUrl2", edtWeddingAlbumUrl1_2);
        map.put("weddingAlbumTitle1", edtWeddingAlbum_3);
        map.put("weddingAlbumUrl3", edtWeddingAlbumUrl1_3);

        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("weddingGroomName", GroomName);
        map.put("weddingGroomFatherName", GroomFatherName);
        map.put("weddingGroomMotherName", GroomMotherName);
        map.put("weddingBrideName", BrideName);
        map.put("weddingBrideMotherName", BrideMotherName);
        map.put("weddingBrideFatherName", BrideFatherName);
        map.put("weddingDate", Date);
        map.put("weddingTime", Time);
        map.put("weddingLocation", location);
        map.put("weddingDescription", desc);
        map.put("weddingDescription", ZipCode);


        if (mWEDDING_ID != null && mWEDDING_ID.length() > 0) {
            map.put("wedding_id", mwedding_id);
            callAddWeadding = apiService.
                    updateWeadding(
                            map,
                            body,
                            partsArray
                    );
        } else {
            callAddWeadding = apiService.
                    addWeadding(
                            map,
                            body,
                            partsArray);
        }
        callAddWeadding.enqueue(new Callback<AddWeddingResponse>()

        {

            @Override
            public void onResponse
                    (Call<AddWeddingResponse> call, Response<AddWeddingResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        AddWeddingResponse loginResponse = response.body();
                        if (loginResponse.getStatus().equalsIgnoreCase("success")) {
                            CustomDialog customDialog = new CustomDialog(AddWeddingActivity.this,
                                    loginResponse.getMsg(),
                                    "",
                                    AddWeddingActivity.this);
                            customDialog.show();

                        } else {
                            CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
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
            public void onFailure(Call<AddWeddingResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this, null,
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

    //----------DATE PICKER----------------------------------------------------------------------------
    @SuppressWarnings("deprecation")
    public void showDatePicker(String selectedDate) {
        Calendar calendar = Calendar.getInstance();
        int mTyear = calendar.get(Calendar.YEAR);
        int mTmonth = calendar.get(Calendar.MONTH);
        int mTday = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddWeddingActivity.this,
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

        StringBuilder fromDate = new StringBuilder()
                .append((month < 10 ? "0" + month : month)).append(day < 10 ? "0" + day : day).append("-").append("-").append(year);
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

    @OnClick({R.id.iv_group_photo, R.id.tv_upload_photo, R.id.tv_uploadpics, R.id.edt_start_date,
            R.id.edt_end_date, R.id.btn_create, R.id.btn_cancel})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tv_uploadpics:
                initSelectImage(2);
                break;
            case R.id.tv_upload_photo:
                initSelectImage(1);
                break;
            case R.id.iv_group_photo:
                initSelectImage(1);
                break;

            case R.id.btn_create:
                if (validation()) {
                    if (Util.checkNetworkAvailablity(AddWeddingActivity.this)) {
                        if (mFilePathList != null) {
                            addWeddingRequest();
                        } else {
                            addWeddingRequest();
                        }
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(AddWeddingActivity.this,
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

//--------------Image Picker--------------------------------------------------------------------------------------

    /**
     * Select Camera Image task performed below
     *
     * @param isWeddingHeaderImage
     */
    private void initSelectImage(int isWeddingHeaderImage) {
        mSelectionImageType = isWeddingHeaderImage;
        String permissionAsk[] = {PermissionUtils.Manifest_CAMERA,
                PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};
        askCompactPermissions(permissionAsk, new PermissionResult() {
            @Override
            public void permissionGranted() {
                try {
                    selectImage(mSelectionImageType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void permissionDenied() {
                Snackbar snackbar = Snackbar
                        .make(root, "Permission needed access camera and storage!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Allow", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                initSelectImage(isWeddingHeaderImage);
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
                Snackbar snackbar = Snackbar
                        .make(root, "To use this feature requires permission please allow from setting" +
                                "please_try_again_later", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

    }

    private void selectImage(int isWeddingHeaderImage) {
        final CharSequence[] items = {// getResources().getString(R.string.str_takephotos),
                getResources().getString(R.string.str_choosefromphotos),
                getResources().getString(R.string.bt_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddWeddingActivity.this,
                R.style.MyAlertDialogStyle);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.str_takephotos))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Util.REQUEST_CAMERA);
                } else if (items[item].equals(getResources().getString(R.string.str_choosefromphotos))) {
                    Intent intent = new Intent(AddWeddingActivity.this, AlbumSelectActivity.class);
                    if (isWeddingHeaderImage == 1) {
                        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                    } else {
                        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 7); // set limit for image selection
                    }
                    startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * To send places picker callback to AddParkingFragment with result below callback is used
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Util.REQUEST_CAMERA && resultCode == RESULT_OK) {
            if (null != data && data.getExtras().containsKey("data")) {
                Bitmap thumbnail;
                thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert thumbnail != null;
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPath = destination.getAbsolutePath();

//                if (null != iv_logo && null != thumbnail) {
//                    iv_logo.setImageBitmap(thumbnail);
//                this.mFilePathList = mPath;
//                }
            }
        } else if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                mFilePathList = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

                if (mSelectionImageType != 1) {
                    if (mFilePathList.size() >= 3) {
                        llUploadPhotos1.setVisibility(View.VISIBLE);
                    } else if (mFilePathList.size() > 4) {
                        llUploadPhotos2.setVisibility(View.VISIBLE);
                    }
                } else {
                    mPath = mFilePathList.get(0).path;
                }
                for (int i = 0; i < mFilePathList.size(); i++) {
                    Uri uri = Uri.fromFile(new File(mFilePathList.get(i).path));

                    File imgFile = new File(mFilePathList.get(i).path);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        if (myBitmap != null) {

                            if (mSelectionImageType == 1) {
                                ivGroupPhoto.setImageBitmap(myBitmap);
                                myBitmap = null;
                                return;
                            }

                            switch (i) {
                                case 0:
                                    ivWeddingImage1.setImageBitmap(myBitmap);
                                    ivWeddingImage1.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    ivWeddingImage2.setImageBitmap(myBitmap);
                                    ivWeddingImage2.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    ivWeddingImage3.setImageBitmap(myBitmap);
                                    ivWeddingImage3.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    ivWeddingImage4.setImageBitmap(myBitmap);
                                    ivWeddingImage4.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    ivWeddingImage5.setImageBitmap(myBitmap);
                                    ivWeddingImage5.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    ivWeddingImage6.setImageBitmap(myBitmap);
                                    ivWeddingImage6.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    ivWeddingImage7.setImageBitmap(myBitmap);
                                    ivWeddingImage7.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                        myBitmap = null;
                    }

                    // start play with image uri
//                    mFilePathList = images.get(i).path;

                }
            }
        }
    }
}
