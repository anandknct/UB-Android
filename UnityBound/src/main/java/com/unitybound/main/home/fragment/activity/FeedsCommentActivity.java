package com.unitybound.main.home.fragment.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.main.home.fragment.HomeFeedsFragment;
import com.unitybound.main.home.fragment.adapter.CommentsAdapter;
import com.unitybound.main.home.fragment.beans.addCommentsList.AddCommentsResponse;
import com.unitybound.main.home.fragment.beans.commentsPost.CommentsItem;
import com.unitybound.main.home.fragment.beans.commentsPost.CommentsPostResponse;
import com.unitybound.main.home.fragment.beans.like_comment.LikeCommentResponse;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedsCommentActivity extends ActivityManagePermission
        implements SwipeRefreshLayout.OnRefreshListener,
        CommentsAdapter.IListAdapterCallback, View.OnClickListener, CustomDialog.IDialogListener {

    RecyclerView rv_list_layout = null;
    private String mPostId = null;
    private ProgressDialog mProgressDialog = null;
    private Call<CommentsPostResponse> callComments = null;
    private List<CommentsItem> allComments = null;
    private EditText edt_comments = null;
    private String mFilePath = null;
    private RelativeLayout rl_mainlayout = null;
    private CommentsAdapter adapter = null;
    String CommentID = "";
    int CommentRowPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_comment);
        mPostId = getIntent().getStringExtra("POST_ID");
        initToolbar();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Comments");
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

    private void initViews() {
        rl_mainlayout = (RelativeLayout) findViewById(R.id.rl_mainlayout);
        mProgressDialog = new ProgressDialog(FeedsCommentActivity.this, R.style.newDialog);
        ImageView btn_send_comment = (ImageView) findViewById(R.id.btn_send_comment);
        btn_send_comment.setOnClickListener(this);
        ImageView btn_image_attachment = (ImageView) findViewById(R.id.btn_image_attachment);
        btn_image_attachment.setOnClickListener(this);
        edt_comments = (EditText) findViewById(R.id.edt_comments);
        rv_list_layout = (RecyclerView) findViewById(R.id.rv_list_layout);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_list_layout.setLayoutManager(mLayoutManager);
        rv_list_layout.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.checkNetworkAvailablity(FeedsCommentActivity.this)) {
            if (mPostId != null) {
                getAllPostRequest();
            } else {
                Toast.makeText(this, "PostId null", Toast.LENGTH_SHORT).show();
            }
        } else {
            CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    @Override
    public void onItemClickListner(String s, int position) {

    }

    @Override
    public void onReplyClickListener(String commentId, int position) {
        Intent intent = new Intent(FeedsCommentActivity.this, FeedsCommentReplyActivity.class);
        intent.putExtra("COMMENT_ID", commentId);
        startActivity(intent);
    }

    @Override
    public void onLikeClickListener(String mPostId, int position) {
        likePost(mPostId, position);
    }

    @Override
    public void onDeleteClickListener(String StrCommentID, int position) {
        CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this, FeedsCommentActivity.this,
                "Delete Comment Alert", "Are you sure to delete this comment ?",
                "delete");
        if (customDialog1.isShowing()) {
            customDialog1.dismiss();
        }
        customDialog1.show();
        CommentID = StrCommentID;
        CommentRowPosition = position;
    }

    @Override
    public void onEditClickListener(String s, int position) {
        CommentsItem GetCommentData = allComments.get(position);
        edt_comments.setText(GetCommentData.getComments().toString());
    }

    @Override
    public void onSendCommentClickListener(String commentMessage, String postId, int position) {

    }

    @Override
    public void onRefresh() {
        if (Util.checkNetworkAvailablity(FeedsCommentActivity.this)) {
            getAllPostRequest();
        } else {
            CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    public void DeleteComment(String CommentID)
    {
        showProgressDialog();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LikeCommentResponse> callLikePost = apiService.deleteCommentRequest(BuildConfig.API_KEY, Util.loadPrefrence(Util.PREF_USER_ID, "", FeedsCommentActivity.this), CommentID);

        callLikePost.enqueue(new Callback<LikeCommentResponse>() {

            @Override
            public void onResponse(Call<LikeCommentResponse> call, Response<LikeCommentResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        LikeCommentResponse likePostResponse = response.body();
                        Toast.makeText(FeedsCommentActivity.this, "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        allComments.remove(CommentRowPosition);
                        rv_list_layout.getAdapter().notifyItemRemoved(CommentRowPosition);
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this, null, "", "", "ONFAILED");
                            if (customDialog1.isShowing()) {
                                customDialog1.dismiss();
                            }
                            customDialog1.show();
                        } else {
                            if (response.body() == null) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Gson gson = new Gson();
                                    ErrorResponse error = gson.fromJson(jObjError.toString(), ErrorResponse.class);
                                    String msg = null;
                                    if (error != null) {
                                        msg = error.getMsg();
                                    } else {
                                        msg = "Something went wrong";
                                    }
                                    new CustomDialog(FeedsCommentActivity.this, null, "", msg, "ONFAILED").show();
                                    hideProgressDialog();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case "5": // TODO Server Error and display retry
                        LikeCommentResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this, null, "", loginResponse1.getMsg(), "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        LikeCommentResponse body1 = response.body();
                        new CustomDialog(FeedsCommentActivity.this, null, "",
                                body1.getMsg() != null ?
                                        body1.getMsg().length() > 0 ? body1.getMsg()
                                                : "Something went wrong" : "Something went wrong",
                                "ONFAILED").show();
                        hideProgressDialog();
                        break;

                }

            }

            @Override
            public void onFailure(Call<LikeCommentResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void getAllPostRequest() {
        showProgressDialog();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        callComments = apiService.getAllPostComments(BuildConfig.API_KEY, Util.loadPrefrence(Util.PREF_USER_ID, "", FeedsCommentActivity.this), mPostId);

        callComments.enqueue(new Callback<CommentsPostResponse>() {

            @Override
            public void onResponse(Call<CommentsPostResponse> call, Response<CommentsPostResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        CommentsPostResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allComments = loginResponse.getData().getComments();
                            if (allComments != null && allComments.size() > 0) {
                                adapter = new CommentsAdapter(
                                        FeedsCommentActivity.this, allComments,
                                        FeedsCommentActivity.this);
                                rv_list_layout.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                rv_list_layout.setAdapter(null);
                            }
//                            swipeRefresh.setRefreshing(false);
                        }
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this, null,
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
                                    new CustomDialog(FeedsCommentActivity.this, null, "",
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
                        CommentsPostResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        CommentsPostResponse body1 = response.body();
                        new CustomDialog(FeedsCommentActivity.this, null, "",
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
            public void onFailure(Call<CommentsPostResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });
    }

    private void likePost(String comment_id, final int position) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<LikeCommentResponse> callLikePost = apiService.likeCommentRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", FeedsCommentActivity.this),
                comment_id);

        callLikePost.enqueue(new Callback<LikeCommentResponse>() {

            @Override
            public void onResponse(Call<LikeCommentResponse> call, Response<LikeCommentResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        LikeCommentResponse likePostResponse = response.body();
                        Toast.makeText(FeedsCommentActivity.this, "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        rv_list_layout.getAdapter().notifyItemChanged(position);
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this, null,
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
                                    new CustomDialog(FeedsCommentActivity.this, null, "",
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
                        LikeCommentResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        LikeCommentResponse body1 = response.body();
                        new CustomDialog(FeedsCommentActivity.this, null, "",
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
            public void onFailure(Call<LikeCommentResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_comment:
                if (Util.checkNetworkAvailablity(FeedsCommentActivity.this)) {
                    if (mFilePath != null && mFilePath.length() > 0) {
                        File file = new File(mFilePath);
                        sendComment(file, "image");
                    } else {
                        sendComment(null, "image");
                    }
                } else {
                    CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this, null,
                            "", getResources().getString(R.string.alt_checknet),
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

                break;
            case R.id.btn_image_attachment:
                initSelectImage();
                break;
        }
    }

    private void sendComment(File file, String fileType) {
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
                    MultipartBody.Part.createFormData("commentmyfile", file.getName(), requestFile);
        }
        // create a map of data to pass along
        RequestBody api_key = Util.createPartFromString(BuildConfig.API_KEY);
        RequestBody user_id = Util.createPartFromString(""
                + Util.loadPrefrence(Util.PREF_USER_ID, "",
                FeedsCommentActivity.this));
        RequestBody comment = Util.createPartFromString(edt_comments.getText().toString().trim());
        RequestBody post_id = Util.createPartFromString(mPostId);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("user_id", user_id);
        map.put("post_id", post_id);
        map.put("comment", comment);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        // finally, execute the request
        Call<AddCommentsResponse> call = apiService.
                addCommentsPost(
                        map,
                        body);

        call.enqueue(new Callback<AddCommentsResponse>() {

            @Override
            public void onResponse(Call<AddCommentsResponse> call,
                                   Response<AddCommentsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                    String sCode = response.code() + "";
                    String c = String.valueOf(sCode.charAt(0));
                    switch (c) {
                        case "2": // TODO Success response  task here and progress loader
                            AddCommentsResponse cityListResponse = response.body();
                            if (cityListResponse.getStatuscode().equalsIgnoreCase("200")) {

                                if (allComments!=null) {
                                    CommentsItem comments = new CommentsItem();
                                    comments.setCommentBy(cityListResponse.getData().getComment_by());
                                    comments.setId(cityListResponse.getData().getId());
                                    comments.setPostId(cityListResponse.getData().getPostId());
                                    comments.setCreatedAt(cityListResponse.getData().getCreatedAt());
                                    comments.setCommentImage(cityListResponse.getData().getCommentImage());
                                    comments.setLikeLocal(false);
                                    comments.setComments(cityListResponse.getData().getComments());
                                    if (allComments==null) {
                                        allComments = new ArrayList<>();
                                    }
                                    allComments.add(allComments.size(),comments);
                                        if (adapter==null) {
                                            adapter = new CommentsAdapter(
                                                    FeedsCommentActivity.this, allComments,
                                                    FeedsCommentActivity.this);
                                            rv_list_layout.setAdapter(adapter);
                                        } else {
                                            adapter.notifyItemInserted(allComments.size());
                                            adapter.notifyDataSetChanged();
                                        }
                                }
                                edt_comments.setText("");
                                CustomDialog customDialog = new CustomDialog(
                                        FeedsCommentActivity.this, FeedsCommentActivity.this, "",
                                        cityListResponse.getMsg(),
                                        "SUCCESS");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                                customDialog.show();
                            } else {
                                CustomDialog customDialog = new CustomDialog(
                                        FeedsCommentActivity.this, FeedsCommentActivity.this, "",
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
                            AddCommentsResponse businessResponse1 = response.body();
                            CustomDialog customDialog = new CustomDialog(FeedsCommentActivity.this, null,
                                    "", businessResponse1.getMsg(),
                                    "ONFAILED");
                            if (customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            customDialog.show();

                        case "4": // TODO Server Error and display retry
                            hideProgressDialog();
                            if (sCode == "401") {
                            } else {
                                AddCommentsResponse businessResponse2 = response.body();
                                CustomDialog customDialog1 = new CustomDialog(FeedsCommentActivity.this, null,
                                        "", businessResponse2.getMsg(),
                                        "ONFAILED");
                                if (customDialog1.isShowing()) {
                                    customDialog1.dismiss();
                                }
                                customDialog1.show();
                            }
                            break;
                        default: // TODO Handle error message and show dialog here
                            AddCommentsResponse businessResponse3 = response.body();
                            CustomDialog customDialog2 = new CustomDialog(FeedsCommentActivity.this, null,
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
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<AddCommentsResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());

                new CustomDialog(FeedsCommentActivity.this, null, "", t.getMessage(),
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

        AlertDialog.Builder builder = new AlertDialog.Builder(FeedsCommentActivity.this,
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
                    Intent intent = new Intent(FeedsCommentActivity.this, AlbumSelectActivity.class);
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
//                    Uri mTempPath = getImageUrlWithAuthority(FeedsCommentActivity.this, selectedImageUri);
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
//                            Toast.makeText(FeedsCommentActivity.this,"Bitmap null",Toast.LENGTH_SHORT).show();
//                        }
//                        myBitmap = null;
//                    }

                    // start play with image uri
                    mFilePath = images.get(i).path;

                }
            }
        }
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        if (param.equalsIgnoreCase("delete")) {
            DeleteComment(CommentID);
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
