package com.unitybound.account.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.adapter.GridRecyclerViewAdapter;
import com.unitybound.account.beans.coverUpdate.CoverPicUpdateResponse;
import com.unitybound.account.beans.photos.PhotosItem;
import com.unitybound.account.beans.photos.ProfilePhotosResponse;
import com.unitybound.account.listner.IAdapterClickListner;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.SpacesItemDecoration;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAccountMyPhotosActivity extends ActivityManagePermission implements IAdapterClickListner, CustomDialog.IDialogListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_photos_label)
    TextView tvAbout;
    @BindView(R.id.tv_add_photos)
    TextView tv_add_photos;
    @BindView(R.id.activity_emergency)
    RelativeLayout activity_emergency;
    RecyclerView rvPhotos;

    private ProgressDialog mProgressDialog = null;
    private List<PhotosItem> requestListItems = null;
    private String mFilePath = "";
    private Bitmap bmp = null;
    private String mSelectedUserProfileId = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myphotos_layout);
        ButterKnife.bind(this);
        initView();
        if (getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().containsKey("SelectedUserProfileId")){
            mSelectedUserProfileId = getIntent().getExtras().getString("SelectedUserProfileId");
        }
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

    private void initView() {
        mProgressDialog = new ProgressDialog(MyAccountMyPhotosActivity.this, R.style.newDialog);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.str_my_friends));
        rvPhotos = (RecyclerView) findViewById(R.id.rv_photos);


        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_five);
        rvPhotos.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    @OnClick(R.id.tv_add_photos)
    public void submit(View view) {
        if (Util.checkNetworkAvailablity(MyAccountMyPhotosActivity.this)) {

            initSelectImage();

        } else {
            CustomDialog customDialog1 = new CustomDialog(MyAccountMyPhotosActivity.this,
                    null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    @Override
    public void onResume() {
        if (Util.checkNetworkAvailablity(MyAccountMyPhotosActivity.this)) {
            if (mSelectedUserProfileId==null) {
                getAllPhotos(null);
            }else {
                getAllPhotos(mSelectedUserProfileId);
            }

        } else {
            CustomDialog customDialog1 = new CustomDialog(MyAccountMyPhotosActivity.this, null,
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
    public void onAdapterItemClick(String _Responce, int position) {

        if (requestListItems != null) {
            Util.navigateTOFullScreenActivity(this,
                    requestListItems.get(position).getPath(),
                    mSelectedUserProfileId!=null ? mSelectedUserProfileId : requestListItems.get(position).getPostId(),
                    null,
                    null);
        } else {
            Toast.makeText(this, "Image not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllPhotos(String mSelectedUserProfileId) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String user_id = Util.loadPrefrence(Util.PREF_USER_ID, "",
                MyAccountMyPhotosActivity.this);
        Call<ProfilePhotosResponse> call = apiService.getProfilePhotos(
                BuildConfig.API_KEY,
                user_id, mSelectedUserProfileId!=null ? mSelectedUserProfileId : user_id);

        call.enqueue(new Callback<ProfilePhotosResponse>() {

            @Override
            public void onResponse(Call<ProfilePhotosResponse> call,
                                   Response<ProfilePhotosResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        ProfilePhotosResponse listResponse = response.body();
                        if (listResponse.getData() != null &&
                                listResponse.getData().getPhotos() != null &&
                                listResponse.getData().getPhotos().size() > 0) {
                            requestListItems = listResponse.getData().getPhotos();

                            if (requestListItems != null) {
                                final GridLayoutManager mng_layout = new GridLayoutManager(
                                        MyAccountMyPhotosActivity.this, 3);
                                rvPhotos.setHasFixedSize(true);
                                rvPhotos.setLayoutManager(mng_layout);
                                rvPhotos.setNestedScrollingEnabled(false);
                                GridRecyclerViewAdapter rcAdapter =
                                        new GridRecyclerViewAdapter(MyAccountMyPhotosActivity.this,
                                                requestListItems, MyAccountMyPhotosActivity.this);
                                rvPhotos.setItemAnimator(new DefaultItemAnimator());
                                rvPhotos.setAdapter(rcAdapter);
                            } else {
                                Toast.makeText(MyAccountMyPhotosActivity.this,
                                        "No data found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MyAccountMyPhotosActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "4":
                        if (sCode.equalsIgnoreCase("401")) {
                            CustomDialog customDialog1 = new CustomDialog(
                                    MyAccountMyPhotosActivity.this, null,
                                    "", "",
                                    "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        } else {
                            if (response.body() == null) {
                                try {
                                    JSONObject jObjError = new JSONObject(
                                            response.errorBody().string());
                                    Gson gson = new Gson();
                                    ErrorResponse error = gson.fromJson(jObjError.toString(),
                                            ErrorResponse.class);
                                    String msg = null;
                                    if (error != null) {
                                        msg = error.getMsg();
                                    } else {
                                        msg = "Something went wrong";
                                    }
                                    new CustomDialog(MyAccountMyPhotosActivity.this,
                                            null, "",
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
                        ProfilePhotosResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(
                                MyAccountMyPhotosActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        ProfilePhotosResponse body1 = response.body();
                        new CustomDialog(MyAccountMyPhotosActivity.this,
                                null, "",
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
            public void onFailure(Call<ProfilePhotosResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    public void showProgressDialog() {
        mProgressDialog.show();
        if (mProgressDialog != null) {
            //  mProgressDialog = Utils.createProgressDialog(BaseActivity.this,
            // getString(R.string.str_logging_in));
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

    /**
     * Select Camera Image task performed below
     */
    private void initSelectImage() {
        String permissionAsk[] = {PermissionUtils.Manifest_CAMERA,
                PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};
        askCompactPermissions(permissionAsk, new PermissionResult() {
            @Override
            public void permissionGranted() {
                try {
                    selectImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void permissionDenied() {
                Snackbar snackbar = Snackbar
                        .make(activity_emergency, "Permission needed access camera and storage!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Allow", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                initSelectImage();
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
                        .make(activity_emergency, "To use this feature requires permission please allow from setting" +
                                "please_try_again_later", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.str_takephotos), getResources().getString(R.string.str_choosefromphotos),
                getResources().getString(R.string.bt_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MyAlertDialogStyle);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.str_takephotos))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Util.REQUEST_CAMERA);
                } else if (items[item].equals(getResources().getString(R.string.str_choosefromphotos))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            Util.SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String mPath;
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
                this.mFilePath = mPath;

                if (mFilePath != null && mFilePath.length() > 0) {
                    File file = new File(mFilePath);
                    updateCoverImage(file, "image");
                } else {
                    updateCoverImage(null, "image");
                }
//                }
            }
        } else if (requestCode == Util.SELECT_FILE && resultCode == RESULT_OK) {
            if (data != null) {

                Uri selectedImageUri = data.getData();
                if (selectedImageUri.toString().startsWith("content://com.sec.android.gallery3d.provider")) {
                    Toast.makeText(getApplicationContext(), "not able to read Picasa images", Toast.LENGTH_SHORT).show();
                } else if (selectedImageUri.toString().startsWith("content://com.google.android.apps.photos.contentprovider")) {
                    Uri mTempPath = getImageUrlWithAuthority(this, selectedImageUri);
                    mFilePath = getRealPathFromURI(mTempPath);
//                    iv_image_prev.setImageBitmap(bm);
//                    tv_type.setText("IMAGE");
//                    mUploadType = "IMAGE";

                } else if (selectedImageUri.toString().startsWith("content://com.android.providers.media.documents/document")) {
//                content://com.android.providers.media.documents/document/image%3A13
                    mFilePath = getPath(this, selectedImageUri);
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(mFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                        Log.d("EXIF", "Exif: " + orientation);
                    } else if (orientation == 3) {
                        matrix.postRotate(180);
                        Log.d("EXIF", "Exif: " + orientation);
                    } else if (orientation == 8) {
                        matrix.postRotate(270);
                        Log.d("EXIF", "Exif: " + orientation);
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(mFilePath, options);
                    final int REQUIRED_SIZE = 200;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    Bitmap thumbnail = BitmapFactory.decodeFile(mFilePath, options);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
//                        mProfileImg.setImageBitmap(rotatedBitmap);
                    bmp = rotatedBitmap;

                    rotatedBitmap = null;
//                    if (bm!=null) {
//                        iv_image_prev.setImageBitmap(bmp);
//                        tv_type.setText("IMAGE");
//                        mUploadType = "IMAGE";
//                    }
                } else {
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                            null);
                    Cursor cursor = cursorLoader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    String selectedImagePath = cursor.getString(column_index);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(selectedImagePath, options);
                    final int REQUIRED_SIZE = 200;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    try {
                        mFilePath = selectedImagePath;
                        mFilePath = getPath(this, selectedImageUri);
                        ExifInterface exif = new ExifInterface(selectedImagePath);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                            Log.d("EXIF", "Exif: " + orientation);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                            Log.d("EXIF", "Exif: " + orientation);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                            Log.d("EXIF", "Exif: " + orientation);
                        }
//                        Bitmap thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);
//                        Bitmap rotatedBitmap = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
//                        bmp = rotatedBitmap;
//
//                        rotatedBitmap = null;
//                        if (bmp!=null) {
//                            iv_image_prev.setImageBitmap(bmp);
//                            tv_type.setText("IMAGE");
//                        }else{
                        mFilePath = getPath(this, selectedImageUri);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (mFilePath != null && mFilePath.length() > 0) {
                    File file = new File(mFilePath);
                    updateCoverImage(file, "image");
                } else {
                    updateCoverImage(null, "image");
                }
            }
        }
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = this.getContentResolver().query(uri, null, null,
                null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        Bitmap bmp = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
//                    bmp.recycle();
                    bmp = null;
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * @param file
     * @param fileType
     */
    private void updateCoverImage(File file, String fileType) {

        if (fileType.length()>0) {
            Toast.makeText(this, "API not found", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();

        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(fileType),
                            file
                    );
            // MultipartBody.Part is used to send also the actual file name
            body =
                    MultipartBody.Part.createFormData("coverphotofile", file.getName(), requestFile);
        }
        // create a map of data to pass along
        RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
        RequestBody user_id = Util.createPartFromString(""
                + Util.loadPrefrence(Util.PREF_USER_ID, "",
                this));
        RequestBody coverImageCoordinate = Util.createPartFromString("");
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("coverImageCoordinate", coverImageCoordinate);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        // finally, execute the request
        Call<CoverPicUpdateResponse> call = apiService.
                updateCoverPhoto(
                        map,
                        body);

        call.enqueue(new Callback<CoverPicUpdateResponse>() {

            @Override
            public void onResponse(Call<CoverPicUpdateResponse> call,
                                   Response<CoverPicUpdateResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                    String sCode = response.code() + "";
                    String c = String.valueOf(sCode.charAt(0));
                    switch (c) {
                        case "2": // TODO Success response  task here and progress loader
                            CoverPicUpdateResponse cityListResponse = response.body();
                            if (cityListResponse.getStatuscode().equalsIgnoreCase("200")) {
                                CustomDialog customDialog = new CustomDialog(
                                        MyAccountMyPhotosActivity.this,
                                        MyAccountMyPhotosActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "SUCCESS");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            } else {
                                CustomDialog customDialog = new CustomDialog(
                                        MyAccountMyPhotosActivity.this,
                                        MyAccountMyPhotosActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "ONFAILED");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            }

                            break;
                        case "5": // TODO Server Error and display retry

                            hideProgressDialog();
                            CoverPicUpdateResponse businessResponse1 = response.body();
                            CustomDialog customDialog = new CustomDialog(
                                    MyAccountMyPhotosActivity.this, null,
                                    "", businessResponse1.getMsg(),
                                    "ONFAILED");
                            if (customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            customDialog.show();

                        case "4": // TODO Server Error and display retry

                            hideProgressDialog();
                            if (sCode == "401") {
//                                Util.showDialog(MyAccountFragment.this,
//                                        (IAlertDialogCallback) MyAccountFragment.this,
//                                        getResources().getString(R.string.str_authfailed),
//                                        getResources().getString(R.string.str_authfaileddetail));
                            } else {
                                CoverPicUpdateResponse businessResponse2 = response.body();
                                CustomDialog customDialog1 = new CustomDialog(
                                        MyAccountMyPhotosActivity.this, null,
                                        "", businessResponse2.getMsg(),
                                        "ONFAILED");
                                if (customDialog1.isShowing()) {
                                    customDialog1.dismiss();
                                }
                                customDialog1.show();
                            }
                            break;
                        default: // TODO Handle error message and show dialog here
                            CoverPicUpdateResponse businessResponse3 = response.body();
                            CustomDialog customDialog2 = new CustomDialog(
                                    MyAccountMyPhotosActivity.this, null,
                                    "", businessResponse3.getMsg(),
                                    "ONFAILED");
                            if (customDialog2.isShowing()) {
                                customDialog2.dismiss();
                            }
                            customDialog2.show();
                            hideProgressDialog();
                            break;

                    }

                } else {
                    Log.d("ask", response.toString());
                    CustomDialog customDialog2 = new CustomDialog(
                            MyAccountMyPhotosActivity.this, null,
                            "", response.toString(),
                            "ONFAILED");
                    if (customDialog2.isShowing()) {
                        customDialog2.dismiss();
                    }
                    customDialog2.show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<CoverPicUpdateResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());

                new CustomDialog(MyAccountMyPhotosActivity.this,
                        null, "", t.getMessage(),
                        "ONFAILED").show();
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        if (param.equalsIgnoreCase("SUCCESS")){
            onResume();
        }
    }
}
