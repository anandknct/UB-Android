package com.unitybound.account.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.account.beans.profile.ProfileAboutResponse;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAccountAboutActivity extends AppCompatActivity {

    private String mProfileUserId = null;
    private ProgressDialog mProgressDialog = null;
    private Call<ProfileAboutResponse> callAbout = null;
    private TextView tv_description = null,tv_phone1,tv_phone2,tv_address_1,tv_email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_layout);
        initView();
        if (getIntent()!=null && getIntent().getExtras()!=null) {
            getPreviousScreenData();
        }else{
            Toast.makeText(this, "bundle data null ", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPreviousScreenData() {
        tv_description.setText(getIntent().getStringExtra("about"));
        tv_phone1.setText(Util.isNull(getIntent().getStringExtra("phone1")));
        tv_phone2.setText(Util.isNull(getIntent().getStringExtra("phone2")));
        tv_address_1.setText(Util.isNull(getIntent().getStringExtra("address1"))+ ", "+
        Util.isNull(getIntent().getStringExtra("city")+ ", "+
                Util.isNull(getIntent().getStringExtra("state"))+ ", "+
                Util.isNull(getIntent().getStringExtra("country"))+ ", "+
                Util.isNull(getIntent().getStringExtra("zipcode"))));
        tv_email.setText(Util.isNull(getIntent().getStringExtra("email")));
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this, R.style.newDialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("About");
        if (getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().containsKey("profile_user_id")) {
            mProfileUserId = getIntent().getExtras().getString("profile_user_id");
        }else{
            Log.e("nik","profile_user_id not found");
        }

        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_phone1 = (TextView) findViewById(R.id.tv_phone1);
        tv_phone2 = (TextView) findViewById(R.id.tv_phone2);
        tv_address_1 = (TextView) findViewById(R.id.tv_address_1);
        tv_email = (TextView) findViewById(R.id.tv_email);

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

    private void getTimeline() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callAbout = apiService.getProfileAbout(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", MyAccountAboutActivity.this),
                mProfileUserId);

        callAbout.enqueue(new Callback<ProfileAboutResponse>() {

            @Override
            public void onResponse(Call<ProfileAboutResponse> call, Response<ProfileAboutResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (sCode.equalsIgnoreCase("200")) {
                    ProfileAboutResponse mResponse = response.body();
                    if (mResponse.getStatus().equalsIgnoreCase("200")) {
//                        tv_description.setText(Util.isNull(mResponse.getData().getUserInfo().get));
                        Toast.makeText(MyAccountAboutActivity.this,
                                getString(R.string.str_under_progress), Toast.LENGTH_SHORT).show();
                    }

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
                            new CustomDialog(MyAccountAboutActivity.this, null, "",
                                    msg,
                                    "ONFAILED").show();
                            hideProgressDialog();

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ProfileAboutResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
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

}
