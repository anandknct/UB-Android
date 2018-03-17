package com.unitybound.main.home.fragment.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.main.home.fragment.adapter.CommentsAdapter;
import com.unitybound.main.home.fragment.beans.addCommentsList.AddCommentsResponse;
import com.unitybound.main.home.fragment.beans.commentsReplyList.CommentsReplyListResponse;
import com.unitybound.main.home.fragment.beans.like_comment.LikeCommentResponse;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedsCommentReplyActivity extends AppCompatActivity implements CommentsAdapter.IListAdapterCallback, View.OnClickListener, CustomDialog.IDialogListener {

    RecyclerView rv_list_layout = null;
    private String mComments_Id = null;
    private ProgressDialog mProgressDialog = null;
    private Call<CommentsReplyListResponse> callComments = null;
    private List<com.unitybound.main.home.fragment.beans.commentsReplyList.CommentsItem> allComments = null;
    private EditText edt_comments = null;
    private String mFilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_comment);
        mComments_Id = getIntent().getStringExtra("COMMENT_ID");
        initToolbar();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Please pray for ");
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
        edt_comments = (EditText) findViewById(R.id.edt_comments);
        ImageView btn_send_comment = (ImageView) findViewById(R.id.btn_send_comment);
        btn_send_comment.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(FeedsCommentReplyActivity.this, R.style.newDialog);
        rv_list_layout = (RecyclerView) findViewById(R.id.rv_list_layout);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_list_layout.setLayoutManager(mLayoutManager);
        rv_list_layout.setItemAnimator(new DefaultItemAnimator());

        if (Util.checkNetworkAvailablity(FeedsCommentReplyActivity.this) && mComments_Id != null) {
            getAllPostRequest();
        } else {
            CustomDialog customDialog1 = new CustomDialog(FeedsCommentReplyActivity.this, null,
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

    }

    @Override
    public void onLikeClickListener(String mPostId, int position) {
        likePost(mPostId, position);
    }

    @Override
    public void onDeleteClickListener(String s, int position) {

    }

    @Override
    public void onSendCommentClickListener(String commentMessage, String postId, int position) {

    }

    private void getAllPostRequest() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callComments = apiService.getAllPostCommentsReplys(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "",
                        FeedsCommentReplyActivity.this), mComments_Id);

        callComments.enqueue(new Callback<CommentsReplyListResponse>() {

            @Override
            public void onResponse(Call<CommentsReplyListResponse> call, Response<CommentsReplyListResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2": // TODO Success response  task here and progress loader
                        CommentsReplyListResponse loginResponse = response.body();
                        if (loginResponse.getData() != null) {
                            allComments = loginResponse.getData().getComments();
                            if (allComments != null && allComments.size() > 0) {
                                CommentsAdapter adapter = new CommentsAdapter(
                                        FeedsCommentReplyActivity.this, allComments,
                                        FeedsCommentReplyActivity.this);
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
                            CustomDialog customDialog1 = new CustomDialog(FeedsCommentReplyActivity.this, null,
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
                                    new CustomDialog(FeedsCommentReplyActivity.this, null, "",
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
                        CommentsReplyListResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(FeedsCommentReplyActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        CommentsReplyListResponse body1 = response.body();
                        new CustomDialog(FeedsCommentReplyActivity.this, null, "",
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
            public void onFailure(Call<CommentsReplyListResponse> call, Throwable t) {
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

    private void sendComment(File file, String fileType) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        // finally, execute the request
        Call<AddCommentsResponse> call = apiService.
                replyCommentRequest(BuildConfig.API_KEY,
                        Util.loadPrefrence(Util.PREF_USER_ID, "", FeedsCommentReplyActivity.this),
                        mComments_Id,
                        edt_comments.getText().toString().trim());

        call.enqueue(new Callback<AddCommentsResponse>() {

            @Override
            public void onResponse(Call<AddCommentsResponse> call,
                                   Response<AddCommentsResponse> response) {
//                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                    String sCode = response.code() + "";
                    String c = String.valueOf(sCode.charAt(0));
                    switch (c) {
                        case "2": // TODO Success response  task here and progress loader
                            AddCommentsResponse cityListResponse = response.body();
                            if (cityListResponse.getStatuscode().equalsIgnoreCase("200")) {

//                                if (allComments!=null) {
//                                    CommentsItem comments = new CommentsItem();
//                                    comments.setCommentBy(cityListResponse.getData().getComment_by());
//                                    comments.setId(cityListResponse.getData().getId());
//                                    comments.setPostId(cityListResponse.getData().getPostId());
//                                    comments.setCreatedAt(cityListResponse.getData().getCreatedAt());
//                                    comments.setCommentImage(cityListResponse.getData().getCommentImage());
//                                    comments.setLikeLocal(false);
//                                    comments.setComments(cityListResponse.getData().getComments());
//                                    allComments.add(allComments.size(),comments);
//                                    adapter.notifyItemInserted(allComments.size());
//                                }
                                if (Util.checkNetworkAvailablity(FeedsCommentReplyActivity.this) && mComments_Id != null) {
                                    getAllPostRequest();
                                } else {
                                    CustomDialog customDialog1 = new CustomDialog(FeedsCommentReplyActivity.this, null,
                                            "", getResources().getString(R.string.alt_checknet),
                                            "ONFAILED");
                                    if (customDialog1.isShowing()) {
                                        customDialog1.dismiss();
                                    }
                                    customDialog1.show();
                                }

                                edt_comments.setText("");
//                                CustomDialog customDialog = new CustomDialog(
//                                        FeedsCommentReplyActivity.this, FeedsCommentReplyActivity.this, "",
//                                        cityListResponse.getMsg(),
//                                        "SUCCESS");
//                                if (customDialog.isShowing()) {
//                                    customDialog.dismiss();
//                                }
//                                customDialog.show();
                            } else {
                                CustomDialog customDialog = new CustomDialog(
                                        FeedsCommentReplyActivity.this, FeedsCommentReplyActivity.this, "",
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
                            CustomDialog customDialog = new CustomDialog(FeedsCommentReplyActivity.this, null,
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
                                CustomDialog customDialog1 = new CustomDialog(FeedsCommentReplyActivity.this, null,
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
                            CustomDialog customDialog2 = new CustomDialog(FeedsCommentReplyActivity.this, null,
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

                new CustomDialog(FeedsCommentReplyActivity.this, null, "", t.getMessage(),
                        "ONFAILED").show();
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (Util.checkNetworkAvailablity(FeedsCommentReplyActivity.this)) {
            if (mFilePath != null && mFilePath.length() > 0) {
                File file = new File(mFilePath);
                sendComment(file, "image");
            } else {
                sendComment(null, "image");
            }
        } else {
            CustomDialog customDialog1 = new CustomDialog(FeedsCommentReplyActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
//        if (param.equalsIgnoreCase("SUCCESS")) {
//            finish();
//        }
    }

    private void likePost(String comment_id, final int position) {

        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<LikeCommentResponse> callLikePost = apiService.replyCommentlikePostRequest(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", FeedsCommentReplyActivity.this),
                mComments_Id,
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
                        Toast.makeText(FeedsCommentReplyActivity.this, "" + likePostResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        rv_list_layout.getAdapter().notifyItemChanged(position);
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(FeedsCommentReplyActivity.this, null,
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
                                    new CustomDialog(FeedsCommentReplyActivity.this, null, "",
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
                        CustomDialog customDialog1 = new CustomDialog(FeedsCommentReplyActivity.this,
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
                        new CustomDialog(FeedsCommentReplyActivity.this, null, "",
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
