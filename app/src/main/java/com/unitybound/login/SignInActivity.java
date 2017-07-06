package com.unitybound.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.forgotpassword.ForgotPasswordActivity;
import com.unitybound.main.BaseActivity;
import com.unitybound.main.MainActivity;
import com.unitybound.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charchitkasliwal on 03/05/17.
 */

public class SignInActivity extends BaseActivity {
    @BindView(R.id.tv_dontaccount)
    TextView tvDontaccount;
    @BindView(R.id.rl_mainlayout)
    RelativeLayout rlMainlayout;
    SignUpActivity mSignActivity;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.edt_email)
    AppCompatEditText edtEmail;
    @BindView(R.id.edt_password)
    AppCompatEditText edtPassword;
    @BindView(R.id.ll_forgotpwd)
    LinearLayout llForgotpwd;
    @BindView(R.id.btn_createacc)
    AppCompatButton btnCreateacc;
    @BindView(R.id.ll_bottomlayout)
    LinearLayout llBottomlayout;
    @BindView(R.id.tv_loginwith)
    TextView tvLoginwith;
    @BindView(R.id.iv_fbicon)
    ImageView ivFbicon;
    @BindView(R.id.iv_googleicon)
    ImageView ivGoogleicon;
    @BindView(R.id.iv_twitter)
    ImageView ivTwitter;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.tv_dontaccount)
    public void setTvDontaccount() {
        SignUpActivity.startSignUpActivity(SignInActivity.this);
    }

    /**
     * Method to open main Activity
     */
    @OnClick(R.id.btn_createacc)
    public void setBtnCreateacc() {
        MainActivity.startMainActivity(SignInActivity.this);
    }

    @OnClick(R.id.ll_forgotpwd)
    public void openForgotPassword(){
        ForgotPasswordActivity.startForgotPassword(SignInActivity.this);
    }


    public static void startSignInActivity(Context context) {
        Intent i = new Intent(context, SignInActivity.class);
        context.startActivity(i);
    }
}
