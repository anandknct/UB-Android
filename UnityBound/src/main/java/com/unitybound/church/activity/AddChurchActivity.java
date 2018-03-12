package com.unitybound.church.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.church.adapter.DenominationSpinnerAdapter;
import com.unitybound.church.beans.JoinByAccessCodeResponse;
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

public class AddChurchActivity extends ActivityManagePermission implements CustomDialog.IDialogListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_group_photo)
    ImageView ivGroupPhoto;
    @BindView(R.id.tv_upload_photo)
    TextView tvUploadPhoto;
    @BindView(R.id.spinner_denomination)
    Spinner spinnerDenomination;
    @BindView(R.id.ll_0)
    LinearLayout ll0;
    @BindView(R.id.edt_church_name)
    EditText edtChurchName;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.edt_poster_name)
    EditText edtPosterName;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.edt_address1)
    EditText edtAddress1;
    @BindView(R.id.ll_3)
    LinearLayout ll3;
    @BindView(R.id.edt_address2)
    EditText edtAddress2;
    @BindView(R.id.ll_4)
    LinearLayout ll4;
    @BindView(R.id.edt_city_name)
    EditText edtCityName;
    @BindView(R.id.edt_state_name)
    EditText edtStateName;
    @BindView(R.id.ll_5)
    LinearLayout ll5;
    @BindView(R.id.edt_zip_code)
    EditText edtZipCode;
    @BindView(R.id.edt_country)
    EditText edtCountry;
    @BindView(R.id.ll_6)
    LinearLayout ll6;
    @BindView(R.id.edt_mailing_address1)
    EditText edtMailingAddress1;
    @BindView(R.id.edt_mailing_address2)
    EditText edtMailingAddress2;
    @BindView(R.id.ll_7)
    LinearLayout ll7;
    @BindView(R.id.edt_address3)
    EditText edtAddress3;
    @BindView(R.id.ll_8)
    LinearLayout ll8;
    @BindView(R.id.edt_city2)
    EditText edtCity2;
    @BindView(R.id.edt_state2)
    EditText edtState2;
    @BindView(R.id.ll_9)
    LinearLayout ll9;
    @BindView(R.id.edt_zip2)
    EditText edtZip2;
    @BindView(R.id.edt_country2)
    EditText edtCountry2;
    @BindView(R.id.ll_10)
    LinearLayout ll10;
    @BindView(R.id.edt_church_phone)
    EditText edtChurchPhone;
    @BindView(R.id.ll_11)
    LinearLayout ll11;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.ll_12)
    LinearLayout ll12;
    @BindView(R.id.root)
    RelativeLayout rootView;
    private ProgressDialog mProgressDialog = null;
    private Call<JoinByAccessCodeResponse> callAddChurch = null;
    private ArrayList<String> arrayListDenomination = null;
    private int SELECT_IMAGE = 0;
    private int REQUEST_CAMERA = 1;
    final CharSequence[] items = {"Take Photo", "Gallery","Cancel"};
    private String userChoosenTask = null;
    private String mPath = null;
    private Bitmap bm = null;

    @Override
    protected void onDestroy() {
        if (callAddChurch != null && callAddChurch.isExecuted()) {
            callAddChurch.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_church);
        ButterKnife.bind(this);


        setUpToolbarLayout();
        setUpChurchDenomination();
    }

    private void setUpChurchDenomination() {
        /* Spinner Feeds Adapter*/
        arrayListDenomination = new ArrayList<>();
        arrayListDenomination.add("Anglican");
        arrayListDenomination.add("Baptist");
        arrayListDenomination.add("Catholic");
        arrayListDenomination.add("Eastern Orthodox");
        arrayListDenomination.add("Lutheran");
        arrayListDenomination.add("Methodist");
        arrayListDenomination.add("Nondenominational");
        arrayListDenomination.add("Oriental Orthodoxy");
        arrayListDenomination.add("Pentecostal");
        arrayListDenomination.add("Presbyterian");
        arrayListDenomination.add("Seventh-Day adventist");
        arrayListDenomination.add("Other");
//        TypedArray imgs = getResources().obtainTypedArray(R.array.pinner_imgs);

        DenominationSpinnerAdapter spAdapter = new DenominationSpinnerAdapter(
                AddChurchActivity.this,
                R.layout.spinner_item_white,
                arrayListDenomination, null);

        spAdapter.setDropDownViewResource(R.layout.spinner_drop_down_white);
        spinnerDenomination.setAdapter(spAdapter);
    }

    private void setUpToolbarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Church");
        mProgressDialog = new ProgressDialog(this, R.style.newDialog);
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


    @OnClick({R.id.iv_group_photo, R.id.tv_upload_photo, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_group_photo:
                selectImage(permissionAsk);
                break;
            case R.id.tv_upload_photo:
                break;
            case R.id.btn_create:
                if (validation())
                if (Util.checkNetworkAvailablity(AddChurchActivity.this)) {
                    if (mPath!=null && mPath.length()>0) {
                        addChurchRequest(new File(mPath));
                    }else{
                        addChurchRequest(null);
                    }
                } else {
                    CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }
                break;
        }
    }

    private boolean validation() {
        if (Util.isNull(edtChurchName.getText().toString()).length()==0){
            CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                    "", "Please enter church name",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtPosterName.getText().toString()).length()==0){
            CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                    "", "Please enter poster name",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtAddress1.getText().toString()).length()==0){
            CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                    "", "Please enter church address",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }else if (Util.isNull(edtCityName.getText().toString()).length()==0){
            CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                    "", "Please enter church city",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtStateName.getText().toString()).length()==0){
            CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                    "", "Please enter church state",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtZipCode.getText().toString()).length()==0){
            CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                    "", "Please enter church zip",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtMailingAddress1.getText().toString()).length()==0){
            CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                    "", "Please enter church mailing address",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtMailingAddress2.getText().toString()).length()==0){
            CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                    "", "Please enter church mailing address 2",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtAddress2.getText().toString()).length()==0){
            CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                    "", "Please enter church address 2",
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        } else if (Util.isNull(edtChurchPhone.getText().toString()).length()==0){
            CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                    "", "Please enter church phone number",
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

    private void addChurchRequest(File file) {
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
                AddChurchActivity.this));
        ///

        RequestBody ChurchName = Util.createPartFromString(Util.isNull(edtChurchName.getText().toString()));
        RequestBody churchPastorName = Util.createPartFromString(Util.isNull(edtPosterName.getText().toString()));
        RequestBody churchDenominations = Util.createPartFromString(arrayListDenomination.get(spinnerDenomination.getSelectedItemPosition()));
        RequestBody churchDescription = Util.createPartFromString(Util.isNull(""));
        RequestBody churchAddress1 = Util.createPartFromString(Util.isNull(edtAddress1.getText().toString()));
        RequestBody churchAddress2 = Util.createPartFromString(Util.isNull(edtAddress2.getText().toString()));
        RequestBody churchAddress3 = Util.createPartFromString(Util.isNull(edtAddress3.getText().toString()));
        RequestBody churchCity = Util.createPartFromString(Util.isNull(edtChurchName.getText().toString()));
        RequestBody churchState = Util.createPartFromString(Util.isNull(edtStateName.getText().toString()));
        RequestBody churchCountry = Util.createPartFromString(Util.isNull(edtCountry.getText().toString()));
        RequestBody churchZip = Util.createPartFromString(Util.isNull(edtZipCode.getText().toString()));
        RequestBody churchPhoneNumber = Util.createPartFromString(Util.isNull(edtChurchPhone.getText().toString()));
        RequestBody churchMailAddress1 = Util.createPartFromString(Util.isNull(edtMailingAddress1.getText().toString()));
        RequestBody churchMailAddress2 = Util.createPartFromString(Util.isNull(edtMailingAddress2.getText().toString()));
        RequestBody churchMailCity = Util.createPartFromString(Util.isNull(edtCity2.getText().toString()));
        RequestBody churchMailState = Util.createPartFromString(Util.isNull(edtState2.getText().toString()));
        RequestBody churchMailCountry = Util.createPartFromString(Util.isNull(edtCountry2.getText().toString()));
        RequestBody churchMailZip = Util.createPartFromString(Util.isNull(edtZip2.getText().toString()));


        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("churchName", ChurchName);
        map.put("churchPastorName", churchPastorName);
        map.put("churchDenominations", churchDenominations);
        map.put("churchDescription", churchDescription);
        map.put("churchAddress1", churchAddress1);
        map.put("churchAddress2", churchAddress2);
        map.put("churchAddress3", churchAddress3);
        map.put("churchCity", churchCity);
        map.put("churchState", churchState);
        map.put("churchCountry", churchCountry);
        map.put("churchZip", churchZip);
        map.put("churchPhoneNumber", churchPhoneNumber);
        map.put("churchMailAddress1", churchMailAddress1);
        map.put("churchMailAddress2", churchMailAddress2);
        map.put("churchMailCity", churchMailCity);
        map.put("churchMailState", churchMailState);
        map.put("churchMailCountry", churchMailCountry);
        map.put("churchMailZip", churchMailZip);

//        callAddChurch = apiService.addChurch(
//                BuildConfig.API_KEY,
//                Util.loadPrefrence(Util.PREF_USER_ID, "", AddChurchActivity.this),
//                edtChurchName.getText().toString(),
//                edtPosterName.getText().toString(),
//                arrayListDenomination.get(spinnerDenomination.getSelectedItemPosition()),
//                "",
//                edtAddress1.getText().toString(),
//                edtAddress2.getText().toString(),
//                edtCityName.getText().toString(),
//                edtStateName.getText().toString(),
//                edtZipCode.getText().toString(),
//                edtCountry.getText().toString(),
//                edtChurchPhone.getText().toString()
//        );
        callAddChurch = apiService.
                addChurch(
                        map,
                        body);
        callAddChurch.enqueue(new Callback<JoinByAccessCodeResponse>()

        {

            @Override
            public void onResponse
                    (Call<JoinByAccessCodeResponse> call, Response<JoinByAccessCodeResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (c.equalsIgnoreCase("2")) {
                    JoinByAccessCodeResponse loginResponse = response.body();
                    if (loginResponse.getStatus().equalsIgnoreCase("success")) {
//                            Toast.makeText(AddChurchActivity.this, loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        CustomDialog customDialog = new CustomDialog(AddChurchActivity.this,
                                "Thank you for creating Church",
                                loginResponse.getMsg(),
                                AddChurchActivity.this);
                        customDialog.show();
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                                "", loginResponse.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                }else{
                    CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
                            "", response.body().getMsg()!=null
                            ? response.body().getMsg(): response.body().getMsg(),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<JoinByAccessCodeResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(AddChurchActivity.this, null,
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddChurchActivity.this);
                builder.setTitle("Select image");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        boolean result = Util.checkPermission(AddChurchActivity.this);
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

    public Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

}
