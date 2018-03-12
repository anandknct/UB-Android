package com.unitybound.groups.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.beans.ProfileFriendsResponse;
import com.unitybound.events.fragment.adapter.ProfileFriendsAddEventAdapter;
import com.unitybound.groups.adapter.GroupsPeopleListAdapter;
import com.unitybound.groups.beans.addGroup.AddGroupResponse;
import com.unitybound.main.friendrequest.model.FriendRequestData;
import com.unitybound.utility.SpacesItemDecoration;
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

public class AddGroupActivity extends ActivityManagePermission implements CustomDialog.IDialogListener, GroupsPeopleListAdapter.IListAdapterCallback {


    private static final int REQUEST_CAMERA = 2122;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_group_photo)
    ImageView ivGroupPhoto;
    @BindView(R.id.tv_upload_photo)
    TextView tvUploadPhoto;
    @BindView(R.id.edt_group_name)
    EditText edtGroupName;
    @BindView(R.id.edtprofile_photo)
    EditText edtprofilePhoto;
    @BindView(R.id.sp_group_type)
    Spinner spGroupType;
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.edt_search_frnds)
    EditText edtSearchFrnds;
    @BindView(R.id.rv_people_list)
    RecyclerView rvPeoplesList;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.sp_public_group)
    RadioButton radioPublic;
    @BindView(R.id.sp_closed_group)
    RadioButton radioClosed;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    ArrayList<FriendRequestData> datalist = new ArrayList<FriendRequestData>();
    private ProgressDialog mProgressDialog;
    private Call<ProfileFriendsResponse> callProfileFriends = null;
    private ProfileFriendsAddEventAdapter friendsListAdapter = null;
    private Call<AddGroupResponse> callGroup = null;
    private String mPath = "";
    final CharSequence[] items = {"Take Photo", "Gallery", "Cancel"};
    private String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(AddGroupActivity.this, R.style.newDialog);
        setUpToolbarLayout();
//        setUpPeoplesRV();
        if (Util.checkNetworkAvailablity(AddGroupActivity.this)) {
            getFriendsRequest();
        } else {
            CustomDialog customDialog1 = new CustomDialog(AddGroupActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    private void setUpPeoplesRV() {
        //add some person to list
        FriendRequestData p1 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p2 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p3 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p4 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p5 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p6 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");
        FriendRequestData p7 = new FriendRequestData("EmergencyFragment Call Option 1", "7389875222");


        datalist.add(p1);
        datalist.add(p2);
        datalist.add(p3);
        datalist.add(p4);
        datalist.add(p5);
        datalist.add(p6);
        datalist.add(p7);

        GroupsPeopleListAdapter adapter =
                new GroupsPeopleListAdapter(AddGroupActivity.this, datalist, AddGroupActivity.this);
        LinearLayoutManager lLayout = new LinearLayoutManager(AddGroupActivity.this);
//        rvPeoplesList.addItemDecoration(new DividerItemDecoration(AddGroupActivity.this, DividerItemDecoration.VERTICAL));
        rvPeoplesList.addItemDecoration(new SpacesItemDecoration(30));
        rvPeoplesList.setLayoutManager(lLayout);
        rvPeoplesList.setHasFixedSize(true);
        rvPeoplesList.setAdapter(adapter);
    }

    private void setUpToolbarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Group");
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


    @OnClick({R.id.tv_upload_photo, R.id.iv_group_photo, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_upload_photo:
                selectImage(permissionAsk);
                break;
            case R.id.iv_group_photo:
                selectImage(permissionAsk);
                break;
            case R.id.btn_create:
                if (Util.checkNetworkAvailablity(AddGroupActivity.this)) {
                    addGroupRequest(new File(mPath));
                } else {
                    CustomDialog customDialog1 = new CustomDialog(AddGroupActivity.this,
                            null,
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

    @OnClick(R.id.btn_cancel)
    public void onViewCanceled() {
        onBackPressed();
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        onBackPressed();
    }

    @Override
    public void onItemClickListner(String s, int position) {

    }

    private void getFriendsRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String userId = Util.loadPrefrence(Util.PREF_USER_ID, "", AddGroupActivity.this);
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
                        if (profileFriendsResponse.getData() != null &&
                                profileFriendsResponse.getStatus().equalsIgnoreCase("success")) {
                            friendsListAdapter =
                                    new ProfileFriendsAddEventAdapter(AddGroupActivity.this,
                                    profileFriendsResponse.getData().getFriends());
                            LinearLayoutManager lLayout = new LinearLayoutManager(AddGroupActivity.this);
                            rvPeoplesList.setLayoutManager(lLayout);
                            rvPeoplesList.setHasFixedSize(true);
                            rvPeoplesList.setAdapter(friendsListAdapter);
                        } else {
                            rvPeoplesList.setAdapter(null);
                            hideProgressDialog();
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

    private void addGroupRequest(File file) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        MultipartBody.Part body = null;
        RequestBody requestFile = null;
        if (file != null && file.exists()) {
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
                AddGroupActivity.this));

        RequestBody groupName = Util.createPartFromString(
                Util.isNull(edtGroupName.getText().toString()));
        RequestBody groupScope = Util.createPartFromString(
                radioPublic.isChecked() ? "public" : radioClosed.isChecked() ? "closed" : "");
        RequestBody groupType = Util.createPartFromString(
                spGroupType.getSelectedItem().toString());
        RequestBody groupDescription = Util.createPartFromString(
                Util.isNull(edtDescription.getText().toString()));

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("groupName", groupName);
        map.put("groupScope", groupScope);
        map.put("groupType", groupType);
        map.put("groupDescription", groupDescription);
        ArrayList<String> listId = friendsListAdapter.getAllCheckedBox();
        if (listId!=null && listId.size()>0) {
            String friendsKey;
            for (int i = 0; i <listId.size(); i++) {
                friendsKey = "groupFriends["+ i +"]";
                RequestBody friendId = Util.createPartFromString(Util.isNull(listId.get(i).toString()));
                map.put(friendsKey, friendId);
                Log.d("nik",listId.get(i).toString()+" Selected Friends Id");
            }
//            Log.d("nik",friendsListAdapter.getAllCheckedBox().size()+" CheckBox Selected");
        }

        callGroup = apiService.
                addGroup(
                        map,
                        body);
        callGroup.enqueue(new Callback<AddGroupResponse>()
        {

            @Override
            public void onResponse
                    (Call<AddGroupResponse> call, Response<AddGroupResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        AddGroupResponse loginResponse = response.body();
                        if (loginResponse.getStatus().equalsIgnoreCase("success")) {

                            CustomDialog customDialog = new CustomDialog(AddGroupActivity.this,
                                    "Thank you for creating Group",loginResponse.getMsg()
                                    ,
                                    AddGroupActivity.this);
                            customDialog.show();
                            /*"Lorem Ipsum is simply dummy text of the printing and typesetting " +
                                    "industry. " +
                                    "Lorem Ipsum has been the industry's standard dummy text ever" +
                                    " since the 1500s, when an unknown printer took a galley of" +
                                    " type and scrambled it to make a type specimen book."*/
                        } else {
                            CustomDialog customDialog1 = new CustomDialog(AddGroupActivity.this, null,
                                    "", loginResponse.getMsg(),
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        }
                        break;
                }
                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<AddGroupResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                CustomDialog customDialog1 = new CustomDialog(AddGroupActivity.this, null,
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddGroupActivity.this);
                builder.setTitle("Select image");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        boolean result = Util.checkPermission(AddGroupActivity.this);
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
                        .make(scrollView,
                                "All Permission are needed to run wegil!",
                                Snackbar.LENGTH_INDEFINITE)
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
                ArrayList<Image> images = data.getParcelableArrayListExtra(
                        ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

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
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

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
