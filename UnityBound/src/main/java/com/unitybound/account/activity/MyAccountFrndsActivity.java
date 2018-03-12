package com.unitybound.account.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.adapter.ProfileFriendRequestListAdapter;
import com.unitybound.account.beans.FriendsItem;
import com.unitybound.account.beans.ProfileFriendsResponse;
import com.unitybound.main.MainActivity;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAccountFrndsActivity extends AppCompatActivity implements ProfileFriendRequestListAdapter.profileListClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.rv_friends)
    RecyclerView rvFriends;
    private ProgressDialog mProgressDialog = null;
    RecyclerView recyclerView;
    ProfileFriendRequestListAdapter adapter;
    private String mSelectedUserProfileId = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_layout);
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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.str_my_friends));
        recyclerView = (RecyclerView) findViewById(R.id.rv_friends);
        mProgressDialog = new ProgressDialog(MyAccountFrndsActivity.this, R.style.newDialog);

    }

    @Override
    public void onResume() {
        if (Util.checkNetworkAvailablity(MyAccountFrndsActivity.this)) {
            if (mSelectedUserProfileId==null) {
                getAllFriends(null);
            }else {
                getAllFriends(mSelectedUserProfileId);
            }

        } else {
            CustomDialog customDialog1 = new CustomDialog(MyAccountFrndsActivity.this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
        super.onResume();
    }

    private void getAllFriends(String mSelectedUserProfileId) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String user_id = Util.loadPrefrence(Util.PREF_USER_ID, "",
                MyAccountFrndsActivity.this);
        Call<ProfileFriendsResponse> call = apiService.profileFriendRequestList(
                BuildConfig.API_KEY,
                user_id,mSelectedUserProfileId!=null ? mSelectedUserProfileId : user_id);

        call.enqueue(new Callback<ProfileFriendsResponse>() {

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
                        ProfileFriendsResponse listResponse = response.body();
                        if (listResponse.getData()!=null &&
                                listResponse.getData().getFriends()!=null &&
                                listResponse.getData().getFriends().size() > 0) {
                            List<FriendsItem> requestListItems = listResponse.getData().getFriends();

                            if (requestListItems != null) {
                                adapter = new ProfileFriendRequestListAdapter(
                                        MyAccountFrndsActivity.this,
                                        requestListItems,MyAccountFrndsActivity.this,mSelectedUserProfileId);
                                LinearLayoutManager lLayout = new LinearLayoutManager(
                                        MyAccountFrndsActivity.this);
                                recyclerView.setLayoutManager(lLayout);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
                            }else{
                                Toast.makeText(MyAccountFrndsActivity.this,
                                        "No data found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            List<FriendsItem> requestListItems = new ArrayList<>();
                            adapter = new ProfileFriendRequestListAdapter(
                                    MyAccountFrndsActivity.this,
                                    requestListItems,MyAccountFrndsActivity.this,mSelectedUserProfileId);
                            LinearLayoutManager lLayout = new LinearLayoutManager(
                                    MyAccountFrndsActivity.this);
                            recyclerView.setLayoutManager(lLayout);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(adapter);
                        }
                        break;
                    case "4":
                        if (sCode.equalsIgnoreCase("401")) {
                            CustomDialog customDialog1 = new CustomDialog(
                                    MyAccountFrndsActivity.this, null,
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
                                    new CustomDialog(MyAccountFrndsActivity.this,
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
                        ProfileFriendsResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(
                                MyAccountFrndsActivity.this,
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        ProfileFriendsResponse body1 = response.body();
                        new CustomDialog(MyAccountFrndsActivity.this,
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
            public void onFailure(Call<ProfileFriendsResponse> call, Throwable t) {
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

    @Override
    public void onUnfriendClickListner(int position, String id) {

    }
    @Override
    public void onUSerNameClickClickListner(int position, String id) {
        Intent intent = new Intent(MyAccountFrndsActivity.this , MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        Bundle bundle = new Bundle();
        bundle.putString("PROFILE_ID",id);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
