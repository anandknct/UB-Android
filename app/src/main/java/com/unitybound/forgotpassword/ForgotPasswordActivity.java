package com.unitybound.forgotpassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unitybound.R;
import com.unitybound.main.BaseActivity;
import com.unitybound.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charchitkasliwal on 03/05/17.
 */

public class ForgotPasswordActivity extends BaseActivity {
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

    /**
     * @param mContext the m context
     */
    public static void startForgotPassword(Context mContext) {
        Intent i = new Intent(mContext, ForgotPasswordActivity.class);
        mContext.startActivity(i);
    }

}
