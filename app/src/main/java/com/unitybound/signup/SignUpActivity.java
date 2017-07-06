package com.unitybound.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.login.SignInActivity;
import com.unitybound.main.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charchitkasliwal on 03/05/17.
 */

public class SignUpActivity extends BaseActivity {
    @BindView(R.id.tv_alreadyaccount)
    TextView tvAlreadyaccount;
    @BindView(R.id.iv_unity)
    ImageView ivUnity;
    @BindView(R.id.tv_createacc)
    TextView tvCreateacc;
    @BindView(R.id.rl_unitybound)
    RelativeLayout rlUnitybound;
    @BindView(R.id.edt_firstname)
    AppCompatEditText edtFirstname;
    @BindView(R.id.edt_lastname)
    AppCompatEditText edtLastname;
    @BindView(R.id.edt_email)
    AppCompatEditText edtEmail;
    @BindView(R.id.edt_password)
    AppCompatEditText edtPassword;
    @BindView(R.id.edt_dob)
    AppCompatEditText edtDob;
    @BindView(R.id.edt_accesscode)
    AppCompatEditText edtAccesscode;
    @BindView(R.id.tv_declare)
    TextView tvDeclare;
    @BindView(R.id.btn_createacc)
    AppCompatButton btnCreateacc;
    @BindView(R.id.ll_divider)
    LinearLayout llDivider;
    @BindView(R.id.tv_loginwith)
    TextView tvLoginwith;
    @BindView(R.id.iv_fbicon)
    ImageView ivFbicon;
    @BindView(R.id.iv_googleicon)
    ImageView ivGoogleicon;
    @BindView(R.id.iv_twitter)
    ImageView ivTwitter;
    @BindView(R.id.sv_main)
    ScrollView svMain;
    @BindView(R.id.ll_donthav)
    LinearLayout llDonthav;

    /**
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }


    /**
     * @param mContext the m context
     */
    public static void startSignUpActivity(Context mContext) {
        Intent i = new Intent(mContext, SignUpActivity.class);
        mContext.startActivity(i);
    }

    @OnClick(R.id.tv_alreadyaccount)
    public void setTvAlreadyaccount() {
        finish();
        Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(i);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
