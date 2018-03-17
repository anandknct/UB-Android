package com.unitybound.account.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.adapter.AddPostSpinnerAdapter;
import com.unitybound.account.beans.addPost.AddPostResponse;
import com.unitybound.account.beans.timeline.AllpostsItem;
import com.unitybound.main.home.fragment.beans.filterPost.DataItem;
import com.unitybound.main.home.fragment.beans.homeFeeds.AllPostsItem;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class AddPostActivity extends ActivityManagePermission implements CustomDialog.IDialogListener {

    boolean EditPost = false;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_message)
    EditText edtMessage;
    @BindView(R.id.sp_drop_down)
    Spinner spDropDown;
    @BindView(R.id.btn_post)
    Button btnPost;
    @BindView(R.id.rl_mainlayout)
    RelativeLayout rl_mainlayout;
    private String mFilePath = null;
    private Bitmap bmp = null;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    private ArrayList<String> arrayList1;
    private ProgressDialog mProgressDialog = null;
    private String mEVENT_ID = null, mGROUP_ID = null, mCHURCH_ID = null;
    private String mTittle = null;
    private String mPOST_ID = null;
    private AllpostsItem allpostsItem;
    private DataItem allPostFilterItem = null;
    private AllPostsItem allPostHomeItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_activity_layout);
        ButterKnife.bind(this);

        EditPost = false;
        /**
         * SetUp Spinner
         */
        arrayList1 = new ArrayList<>();
        arrayList1.add("DEVOTIONAL");
        arrayList1.add("PRAISE");
        arrayList1.add("PRAYERS");
        arrayList1.add("TESTIMONIALS");

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("mEVENT_ID")) {
            // Come from EventDetailActivity if user going to add post for particular event
            mEVENT_ID = getIntent().getExtras().getString("mEVENT_ID");
        } else if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("mGROUP_ID")) {
            // Come from EventDetailActivity if user going to add post for particular event
            mGROUP_ID = getIntent().getExtras().getString("mGROUP_ID");
        } else if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("mCHURCH_ID")) {
            // Come from EventDetailActivity if user going to add post for particular event
            mCHURCH_ID = getIntent().getExtras().getString("mCHURCH_ID");
        } else if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("POST_ID")) {
            mPOST_ID = getIntent().getExtras().getString("POST_ID");
            if (getIntent().getExtras().containsKey("COMING_FROM")) {
                String comingFrom = getIntent().getExtras().getString("COMING_FROM");
                if (comingFrom.equalsIgnoreCase("PROFILE")) {
                    allpostsItem = getIntent().getExtras().getParcelable("data");
                    setAllDataOnUi(allpostsItem);
                } else if (comingFrom.equalsIgnoreCase("HOME")) {
                    allPostHomeItem = getIntent().getExtras().getParcelable("data");
                    setAllDataOnUi(allPostHomeItem);
                } else if (comingFrom.equalsIgnoreCase("FILTER")) {
                    allPostFilterItem = getIntent().getExtras().getParcelable("data");
                    setAllDataOnUi(allPostFilterItem);
                }
                spDropDown.setVisibility(View.GONE);
            }
            EditPost = true;
            btnPost.setText("Update");
            ;
        }
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("TITTLE")) {
            // Come from EventDetailActivity if user going to add post for particular event
            mTittle = getIntent().getExtras().getString("TITTLE");
        }

        edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtMessage.getText().toString().length()>0) {
                    edtMessage.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mProgressDialog = new ProgressDialog(AddPostActivity.this, R.style.newDialog);
        setUpToolbarLayout();

        /**
         * SetUp Spinner
         */
        TypedArray imgs = getResources().obtainTypedArray(R.array.pinner_imgs);

        AddPostSpinnerAdapter spAdapter = new AddPostSpinnerAdapter(
                this,
                R.layout.create_post_spinner_item,
                arrayList1, imgs);

//        spAdapter.setDropDownViewResource(R.layout.spinner_drop_down_white_bg_layout);
        spDropDown.setAdapter(spAdapter);

    }

    private void setAllDataOnUi(Object allpostsItem) {
        if (allpostsItem != null) {
            if (allpostsItem instanceof AllpostsItem) {
                edtMessage.setText(Util.isNull(((AllpostsItem) allpostsItem).getPost()));
                for (int i = 0; i < arrayList1.size(); i++) {
                    AllpostsItem mData = ((AllpostsItem) allpostsItem);
                    if (mData!=null && mData.getPost()!= null && mData.getPost().equalsIgnoreCase(arrayList1.get(i))) {
                        spDropDown.setSelection(i);
                    }
                }
            } else if (allpostsItem instanceof AllPostsItem) {
                edtMessage.setText(Util.isNull(((AllPostsItem) allpostsItem).getPost()));
                for (int i = 0; i < arrayList1.size(); i++) {
                    AllPostsItem mData = ((AllPostsItem) allpostsItem);
                    if (mData!=null && mData.getPost()!= null && mData.getPost().equalsIgnoreCase(arrayList1.get(i))) {
                        spDropDown.setSelection(i);
                    }
                }
            } else if (allpostsItem instanceof DataItem) {
                edtMessage.setText(Util.isNull(((DataItem) allpostsItem).getPost()));
                for (int i = 0; i < arrayList1.size(); i++) {
                    DataItem mData = ((DataItem) allpostsItem);
                    if (mData!=null && mData.getPost()!= null && mData.getPost().equalsIgnoreCase(arrayList1.get(i))) {
                        spDropDown.setSelection(i);
                    }
                }
            }

        }
    }

    private void setUpToolbarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if(EditPost)
        {
            getSupportActionBar().setTitle("Update Post");
        }
        else
        {
            getSupportActionBar().setTitle("Create Post");
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

    @OnClick(R.id.btn_post)
    public void onViewClicked() {
        if (Util.checkNetworkAvailablity(AddPostActivity.this)) {
            if (edtMessage.getText().toString().length()==0){
                edtMessage.setError("Please enter post text.");
                edtMessage.requestFocus();
                return;
            } else {
                edtMessage.setError(null);
                if (mPOST_ID != null && mPOST_ID.length() > 0) {

                    if (mFilePath != null && mFilePath.length() > 0) {
                        File file = new File(mFilePath);
                        editPost(file, "image");
                    } else {
                        editPost(null, "image");
                    }
                } else {
                    if (mFilePath != null && mFilePath.length() > 0) {
                        File file = new File(mFilePath);
                        createPost(file, "image");
                    } else {
                        createPost(null, "image");
                    }
                }
            }
        } else {
            CustomDialog customDialog1 = new CustomDialog(AddPostActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    @OnClick(R.id.imageView2)
    public void onimageView2ViewClicked() {
        initSelectImage();
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
                        .make(rl_mainlayout, "Permission needed access camera and storage!", Snackbar.LENGTH_INDEFINITE)
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
                        .make(rl_mainlayout, "To use this feature requires permission please allow from setting" +
                                "please_try_again_later", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.str_takephotos), getResources().getString(R.string.str_choosefromphotos),
                getResources().getString(R.string.bt_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddPostActivity.this,
                R.style.MyAlertDialogStyle);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.str_takephotos))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Util.REQUEST_CAMERA);
                } else if (items[item].equals(getResources().getString(R.string.str_choosefromphotos))) {
//                    Intent intent = new Intent(
//                            Intent.ACTION_PICK,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select File"),
//                            Util.SELECT_FILE);
                    Intent intent = new Intent(AddPostActivity.this, AlbumSelectActivity.class);
                    intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
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
//                }
            }
        } else if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {

//                Uri selectedImageUri = data.getData();
//                if (selectedImageUri.toString().startsWith("content://com.sec.android.gallery3d.provider")) {
//                    Toast.makeText(getApplicationContext(), "not able to read Picasa images", Toast.LENGTH_SHORT).show();
//                } else if (selectedImageUri.toString().startsWith("content://com.google.android.apps.photos.contentprovider")) {
//                    Uri mTempPath = getImageUrlWithAuthority(AddPostActivity.this, selectedImageUri);
//                    mFilePath = getRealPathFromURI(mTempPath);
////                    iv_image_prev.setImageBitmap(bm);
////                    tv_type.setText("IMAGE");
////                    mUploadType = "IMAGE";
//
//                } else if (selectedImageUri.toString().startsWith("content://com.android.providers.media.documents/document")) {
////                content://com.android.providers.media.documents/document/image%3A13
//                    mFilePath = getPath(this, selectedImageUri);
//                    ExifInterface exif = null;
//                    try {
//                        exif = new ExifInterface(mFilePath);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//                    Matrix matrix = new Matrix();
//                    if (orientation == 6) {
//                        matrix.postRotate(90);
//                        Log.d("EXIF", "Exif: " + orientation);
//                    } else if (orientation == 3) {
//                        matrix.postRotate(180);
//                        Log.d("EXIF", "Exif: " + orientation);
//                    } else if (orientation == 8) {
//                        matrix.postRotate(270);
//                        Log.d("EXIF", "Exif: " + orientation);
//                    }
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(mFilePath, options);
//                    final int REQUIRED_SIZE = 200;
//                    int scale = 1;
//                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
//                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//                        scale *= 2;
//                    options.inSampleSize = scale;
//                    options.inJustDecodeBounds = false;
//                    Bitmap thumbnail = BitmapFactory.decodeFile(mFilePath, options);
//                    Bitmap rotatedBitmap = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
////                        mProfileImg.setImageBitmap(rotatedBitmap);
//                    bmp = rotatedBitmap;
//
//                    rotatedBitmap = null;
////                    if (bm!=null) {
////                        iv_image_prev.setImageBitmap(bmp);
////                        tv_type.setText("IMAGE");
////                        mUploadType = "IMAGE";
////                    }
//                } else {
//                    String[] projection = {MediaStore.MediaColumns.DATA};
//                    CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
//                            null);
//                    Cursor cursor = cursorLoader.loadInBackground();
//                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                    cursor.moveToFirst();
//                    String selectedImagePath = cursor.getString(column_index);
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(selectedImagePath, options);
//                    final int REQUIRED_SIZE = 200;
//                    int scale = 1;
//                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
//                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//                        scale *= 2;
//                    options.inSampleSize = scale;
//                    options.inJustDecodeBounds = false;
//                    try {
//                        mFilePath = selectedImagePath;
//                        mFilePath = getPath(this, selectedImageUri);
//                        ExifInterface exif = new ExifInterface(selectedImagePath);
//                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//                        Matrix matrix = new Matrix();
//                        if (orientation == 6) {
//                            matrix.postRotate(90);
//                            Log.d("EXIF", "Exif: " + orientation);
//                        } else if (orientation == 3) {
//                            matrix.postRotate(180);
//                            Log.d("EXIF", "Exif: " + orientation);
//                        } else if (orientation == 8) {
//                            matrix.postRotate(270);
//                            Log.d("EXIF", "Exif: " + orientation);
//                        }
////                        Bitmap thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);
////                        Bitmap rotatedBitmap = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
////                        bmp = rotatedBitmap;
////
////                        rotatedBitmap = null;
////                        if (bmp!=null) {
////                            iv_image_prev.setImageBitmap(bmp);
////                            tv_type.setText("IMAGE");
////                        }else{
//                        mFilePath = getPath(this, selectedImageUri);
////                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
                ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

                for (int i = 0; i < images.size(); i++) {
//                    Uri uri = Uri.fromFile(new File(images.get(i).path));

//                    File imgFile = new File(images.get(i).path);
//
//                    if (imgFile.exists()) {
//
//                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//                        if (myBitmap != null) {
//                                icCover.setImageBitmap(myBitmap);
//                        } else {
//                            Toast.makeText(AddPostActivity.this,"Bitmap null",Toast.LENGTH_SHORT).show();
//                        }
//                        myBitmap = null;
//                    }

                    // start play with image uri
                    mFilePath = images.get(i).path;

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
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
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
    private void createPost(File file, String fileType) {
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
                    MultipartBody.Part.createFormData("myfile", file.getName(), requestFile);
        }
        // create a map of data to pass along
        RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
        RequestBody user_id = Util.createPartFromString(""
                + Util.loadPrefrence(Util.PREF_USER_ID, "",
                AddPostActivity.this));
        String post = arrayList1.get(spDropDown.getSelectedItemPosition()).equalsIgnoreCase("Devotional") ? "Devotional" :
                arrayList1.get(spDropDown.getSelectedItemPosition()).equalsIgnoreCase("Prayers") ? "Prayer" :
                        arrayList1.get(spDropDown.getSelectedItemPosition()).equalsIgnoreCase("Praise") ? "Praise" :
                                arrayList1.get(spDropDown.getSelectedItemPosition()).equalsIgnoreCase("Testimonials") ? "Testimonial" : "";
        RequestBody mypost = Util.createPartFromString(edtMessage.getText().toString());
        RequestBody postType = Util.createPartFromString(post);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("mypost", mypost);
        map.put("postType", postType);
        if (mTittle != null) {
            if (mTittle.contains("Event")) {
                RequestBody Event = Util.createPartFromString("event");
                map.put("discussion_type", Event);
                RequestBody discussion_type_id = Util.createPartFromString(mEVENT_ID);
                map.put("discussion_type_id", discussion_type_id);
            } else if (mTittle.contains("Group")) {
                RequestBody Group = Util.createPartFromString("group");
                map.put("discussion_type", Group);
                RequestBody discussion_type_id = Util.createPartFromString(mGROUP_ID);
                map.put("discussion_type_id", discussion_type_id);
            } else if (mTittle.contains("Church")) {
                RequestBody Church = Util.createPartFromString("church");
                map.put("discussion_type", Church);
                RequestBody discussion_type_id = Util.createPartFromString(mCHURCH_ID);
                map.put("discussion_type_id", discussion_type_id);
            }
        }

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        // finally, execute the request
        Call<AddPostResponse> call = apiService.
                addPostFeed(
                        map,
                        body);

        call.enqueue(new Callback<AddPostResponse>() {

            @Override
            public void onResponse(Call<AddPostResponse> call,
                                   Response<AddPostResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                    String sCode = response.code() + "";
                    String c = String.valueOf(sCode.charAt(0));
                    switch (c) {
                        case "2": // TODO Success response  task here and progress loader
                            AddPostResponse cityListResponse = response.body();
                            if (cityListResponse.getStatuscode().equalsIgnoreCase("200")) {
                                CustomDialog customDialog = new CustomDialog(
                                        AddPostActivity.this, AddPostActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "SUCCESS");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            } else {
                                CustomDialog customDialog = new CustomDialog(
                                        AddPostActivity.this, AddPostActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "ONFAILED");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            }

                            break;
                        case "5": // TODO Server Error and display retry
//                            CityListResponse body = response.body();
//                            CustomDialog customDialog = new CustomDialog(CreateBusinessDialogActivity.this, null, "", body.getMeta().getMessage(),
//                                    "ONFAILED", false);
//                            if (customDialog.isShowing()) {
//                                customDialog.dismiss();
//                            }
//                            customDialog.show();
                            hideProgressDialog();
//                            circularRevealExit(rr_main, CreateBusinessDialogActivity.this, false);
                            AddPostResponse businessResponse1 = response.body();
                            CustomDialog customDialog = new CustomDialog(AddPostActivity.this, null,
                                    "", businessResponse1.getMsg(),
                                    "ONFAILED");
                            if (customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            customDialog.show();

                        case "4": // TODO Server Error and display retry
//                            CityListResponse body = response.body();
//                            CustomDialog customDialog = new CustomDialog(CreateBusinessDialogActivity.this, null, "", body.getMeta().getMessage(),
//                                    "ONFAILED", false);
//                            if (customDialog.isShowing()) {
//                                customDialog.dismiss();
//                            }
//                            customDialog.show();
                            hideProgressDialog();
                            if (sCode == "401") {
//                                Util.showDialog(AddPostActivity.this,
//                                        (IAlertDialogCallback) AddPostActivity.this,
//                                        getResources().getString(R.string.str_authfailed),
//                                        getResources().getString(R.string.str_authfaileddetail));
                            } else {
                                AddPostResponse businessResponse2 = response.body();
                                CustomDialog customDialog1 = new CustomDialog(AddPostActivity.this, null,
                                        "", businessResponse2.getMsg(),
                                        "ONFAILED");
                                if (customDialog1.isShowing()) {
                                    customDialog1.dismiss();
                                }
                                customDialog1.show();
                            }
                            break;
                        default: // TODO Handle error message and show dialog here
                            AddPostResponse businessResponse3 = response.body();
                            CustomDialog customDialog2 = new CustomDialog(AddPostActivity.this, null,
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
//                    mProgressBar.hide();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<AddPostResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());

                new CustomDialog(AddPostActivity.this, null, "", t.getMessage(),
                        "ONFAILED").show();
                hideProgressDialog();
            }
        });
    }

    /**
     * @param file
     * @param fileType
     */
    private void editPost(File file, String fileType) {
        showProgressDialog();

        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse(fileType), file);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("myfile", file.getName(), requestFile);
        }
        // create a map of data to pass along
        RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
        RequestBody user_id = Util.createPartFromString(""
                + Util.loadPrefrence(Util.PREF_USER_ID, "", AddPostActivity.this));
        String post = arrayList1.get(spDropDown.getSelectedItemPosition()).equalsIgnoreCase("Devotional") ? "Devotional" :
                arrayList1.get(spDropDown.getSelectedItemPosition()).equalsIgnoreCase("Prayers") ? "Prayer" :
                        arrayList1.get(spDropDown.getSelectedItemPosition()).equalsIgnoreCase("Praise") ? "Praise" :
                                arrayList1.get(spDropDown.getSelectedItemPosition()).equalsIgnoreCase("Testimonials") ? "Testimonial" : "";
        RequestBody mypost = Util.createPartFromString(edtMessage.getText().toString());
        RequestBody postType = Util.createPartFromString(post);
        RequestBody RqBdyPostID = Util.createPartFromString(mPOST_ID);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("mypost", mypost);
        map.put("postType", postType);
        map.put("post_id", RqBdyPostID);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        // finally, execute the request
        Call<AddPostResponse> call = apiService.editPostFeed(map, body);

        call.enqueue(new Callback<AddPostResponse>() {

            @Override
            public void onResponse(Call<AddPostResponse> call, Response<AddPostResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                    String sCode = response.code() + "";
                    String c = String.valueOf(sCode.charAt(0));
                    switch (c) {
                        case "2": // TODO Success response  task here and progress loader
                            AddPostResponse cityListResponse = response.body();
                            if (cityListResponse.getStatuscode().equalsIgnoreCase("200")) {
                                CustomDialog customDialog = new CustomDialog(
                                        AddPostActivity.this, AddPostActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "SUCCESS");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            } else {
                                CustomDialog customDialog = new CustomDialog(
                                        AddPostActivity.this, AddPostActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "ONFAILED");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            }

                            break;
                        case "5": // TODO Server Error and display retry
//                            CityListResponse body = response.body();
//                            CustomDialog customDialog = new CustomDialog(CreateBusinessDialogActivity.this, null, "", body.getMeta().getMessage(),
//                                    "ONFAILED", false);
//                            if (customDialog.isShowing()) {
//                                customDialog.dismiss();
//                            }
//                            customDialog.show();
                            hideProgressDialog();
//                            circularRevealExit(rr_main, CreateBusinessDialogActivity.this, false);
                            AddPostResponse businessResponse1 = response.body();
                            CustomDialog customDialog = new CustomDialog(AddPostActivity.this, null,
                                    "", businessResponse1.getMsg(),
                                    "ONFAILED");
                            if (customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            customDialog.show();

                        case "4": // TODO Server Error and display retry
//                            CityListResponse body = response.body();
//                            CustomDialog customDialog = new CustomDialog(CreateBusinessDialogActivity.this, null, "", body.getMeta().getMessage(),
//                                    "ONFAILED", false);
//                            if (customDialog.isShowing()) {
//                                customDialog.dismiss();
//                            }
//                            customDialog.show();
                            hideProgressDialog();
                            if (sCode == "401") {
//                                Util.showDialog(AddPostActivity.this,
//                                        (IAlertDialogCallback) AddPostActivity.this,
//                                        getResources().getString(R.string.str_authfailed),
//                                        getResources().getString(R.string.str_authfaileddetail));
                            } else {
                                AddPostResponse businessResponse2 = response.body();
                                CustomDialog customDialog1 = new CustomDialog(AddPostActivity.this, null,
                                        "", businessResponse2.getMsg(),
                                        "ONFAILED");
                                if (customDialog1.isShowing()) {
                                    customDialog1.dismiss();
                                }
                                customDialog1.show();
                            }
                            break;
                        default: // TODO Handle error message and show dialog here
                            AddPostResponse businessResponse3 = response.body();
                            CustomDialog customDialog2 = new CustomDialog(AddPostActivity.this, null,
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
//                    mProgressBar.hide();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<AddPostResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());

                new CustomDialog(AddPostActivity.this, null, "", t.getMessage(),
                        "ONFAILED").show();
                hideProgressDialog();
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
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        if (param.equalsIgnoreCase("SUCCESS")) {
            finish();
        }
    }

}

