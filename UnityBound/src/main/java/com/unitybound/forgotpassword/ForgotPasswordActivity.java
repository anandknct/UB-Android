package com.unitybound.forgotpassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.forgotpassword.beans.ForgetPassowordResponse;
import com.unitybound.main.BaseActivity;
import com.unitybound.signup.SignUpActivity;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by charchitkasliwal on 03/05/17.
 */

public class ForgotPasswordActivity extends BaseActivity implements CustomDialog.IDialogListener {
    @BindView(R.id.tv_dontaccount)
    TextView tvDontaccount;
    @BindView(R.id.tv_sendlink)
    TextView tvSendlink;
    @BindView(R.id.edt_email)
    AppCompatEditText edtEmail;
    @BindView(R.id.ll_bottomlayout)
    LinearLayout llBottomlayout;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_dontaccount)
    public void setTvDontaccount() {
        finish();
        Intent i = new Intent(ForgotPasswordActivity.this, SignUpActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_cancel)
    public void setBtnCancel(){
        finish();
    }

    @OnClick(R.id.btn_submit)
    public void setBtnSubmit(){
        if (validate()) {
            forgotPasswordRequest(edtEmail.getText().toString());
        }
    }

    /**
     * To validate the login inputs.
     *
     * @return
     */
    private boolean validate() {

        if (edtEmail.getText().toString().length() == 0) {
            new CustomDialog(ForgotPasswordActivity.this, ForgotPasswordActivity.this, "",
                    "Please enter your email ID",
                    "ONFAILED").show();
            edtEmail.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param mContext the m context
     */
    public static void startForgotPassword(Context mContext) {
        Intent i = new Intent(mContext, ForgotPasswordActivity.class);
        mContext.startActivity(i);
    }

    private void forgotPasswordRequest(String email) {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ForgetPassowordResponse> call = apiService.forgotPasswordRequest(
                BuildConfig.API_KEY,
                Util.isNull(email));

        call.enqueue(new Callback<ForgetPassowordResponse>() {

            @Override
            public void onResponse(Call<ForgetPassowordResponse> call, Response<ForgetPassowordResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (c.startsWith("2")) {
                    ForgetPassowordResponse loginResponse = response.body();
                    CustomDialog customDialog1 = new CustomDialog(ForgotPasswordActivity.this, ForgotPasswordActivity.this,
                            "", loginResponse.getMsg(),
                            "SUCCESS");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                } else {
                    CustomDialog customDialog1 = new CustomDialog(ForgotPasswordActivity.this, null,
                            "", "Server Error code : " + sCode,
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<ForgetPassowordResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                CustomDialog customDialog1 = new CustomDialog(ForgotPasswordActivity.this, null,
                        "", t.toString(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {
        if (param.equalsIgnoreCase("SUCCESS")){
            finish();
        }
    }
}
