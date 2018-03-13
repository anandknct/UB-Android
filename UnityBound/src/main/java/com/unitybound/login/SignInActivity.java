package com.unitybound.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.forgotpassword.ForgotPasswordActivity;
import com.unitybound.login.beans.LoginResponse;
import com.unitybound.login.beans.User;
import com.unitybound.login.beans.socialLogin.SocialLoginResponse;
import com.unitybound.login.socialActivity.SocialIntegrations;
import com.unitybound.login.socialActivity.SocialInterface;
import com.unitybound.main.BaseActivity;
import com.unitybound.main.MainActivity;
import com.unitybound.signup.SignUpActivity;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import java.security.MessageDigest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 1nikhiljogdand@gmail.com on 03/09/17.
 */

public class SignInActivity extends BaseActivity implements CustomDialog.IDialogListener, SocialInterface {

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
    private final int WEB_REQUEST_CODE = 101;
    @BindView(R.id.fb_login_button)
    LoginButton login;
    private CallbackManager callbackManager = null;
    private SocialIntegrations mSocialIntegrations = null;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocialIntegrations = SocialIntegrations.getInstance(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        try {
            FirebaseApp.initializeApp(SignInActivity.this);
            String fcmToken = Util.getFcmToken();
            Log.d("nik", "FCM Token : " + fcmToken);
            if (fcmToken != null && fcmToken.length() > 0) {
                Util.savePreferences(Util.FCM_TOKEN, fcmToken, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        generateHashKey();
    }

    @OnClick(R.id.tv_dontaccount)
    public void setTvDontaccount() {
        Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(i);
    }

    /**
     * Method to open main Activity
     */
    @OnClick(R.id.btn_createacc)
    public void setBtnCreateacc() {
        if (Util.checkNetworkAvailablity(this)) {
            if (validate()) {
                loginRequest();
            }
        } else {
            CustomDialog customDialog1 = new CustomDialog(this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    @OnClick(R.id.ll_forgotpwd)
    public void openForgotPassword() {
        ForgotPasswordActivity.startForgotPassword(SignInActivity.this);
    }

    @OnClick(R.id.iv_twitter)
    public void openTwitterLogin() {
        if (Util.checkNetworkAvailablity(this)) {
//            loginToTwitter();
//            loginToTwitter2();
        } else {
            CustomDialog customDialog1 = new CustomDialog(this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    @OnClick(R.id.iv_googleicon)
    public void openGoogleLogin() {
        if (Util.checkNetworkAvailablity(this)) {
            showProgressDialog();
            //FACEBOOK_LOGIN = true;
            mSocialIntegrations.signIn(SocialInterface.SOCIAL_MEDIUMS.GOOGLEPLUS);
        } else {
            CustomDialog customDialog1 = new CustomDialog(this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    @OnClick(R.id.iv_fbicon)
    public void openFacebookLogin() {
        if (Util.checkNetworkAvailablity(this)) {
            showProgressDialog();
            //FACEBOOK_LOGIN = true;
            mSocialIntegrations.signIn(SocialInterface.SOCIAL_MEDIUMS.FACEBOOK);
        } else {
            CustomDialog customDialog1 = new CustomDialog(this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }


    public static void startSignInActivity(Context context) {
        Intent i = new Intent(context, SignInActivity.class);
        context.startActivity(i);
    }

    /**
     * To validate the login inputs.
     *
     * @return
     */
    private boolean validate() {

        if (edtEmail.getText().toString().length() == 0) {
            new CustomDialog(SignInActivity.this, SignInActivity.this, "",
                    "Please enter your email ID",
                    "ONFAILED").show();
            edtEmail.requestFocus();
            return false;
        } else if (edtPassword.getText().toString().length() == 0) {
            new CustomDialog(SignInActivity.this, SignInActivity.this, "",
                    "Please enter your password",
                    "ONFAILED").show();
            edtPassword.requestFocus();
            return false;
        } else if (edtPassword.getText().toString().length() < 5) {
            new CustomDialog(SignInActivity.this, SignInActivity.this, "",
                    "Password must be greater than 6 character",
                    "ONFAILED").show();
            edtPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void loginRequest() {
        showProgressDialog();

       String refreshedToken = Util.getFcmToken();
       if (refreshedToken!=null && refreshedToken.length()==0){
           refreshedToken = Util.loadPrefrence(Util.FCM_TOKEN,"",SignInActivity.this);
       }

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<LoginResponse> call = apiService.sendLoginRequest(
                BuildConfig.API_KEY,
                edtEmail.getText().toString().trim(),
                edtPassword.getText().toString().trim(),
                refreshedToken);

        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (c.startsWith("2")) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getData() != null && loginResponse.getStatus().equalsIgnoreCase("success")) {
                        User user = loginResponse.getData().getUser();
                        Util.savePreferences(Util.PREF_USER_EMAIL,
                                String.valueOf(user.getEmail()),
                                SignInActivity.this);
                        Util.savePreferences(Util.PREF_FIRST_NAME,
                                String.valueOf(user.getFirstName()),
                                SignInActivity.this);
                        Util.savePreferences(Util.PREF_LAST_NAME,
                                String.valueOf(user.getLastName()),
                                SignInActivity.this);
                        Util.savePreferences(Util.PREF_NICK_NAME,
                                String.valueOf(user.getNickName()),
                                SignInActivity.this);
                        Util.savePreferences(Util.PREF_USER_ID,
                                String.valueOf(user.getId()),
                                SignInActivity.this);
                        Util.savePreferences(Util.PREF_USER_NAME,
                                String.valueOf(user.getUserName()),
                                SignInActivity.this);
                        Util.saveBooleanPreferences(Util.PREF_IS_LOGIN, true, SignInActivity.this);

                        redirectToLoginScreen();
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(SignInActivity.this, null,
                                "", loginResponse.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                } else {
                    CustomDialog customDialog1 = new CustomDialog(SignInActivity.this, null,
                            "", "Server Error code : " + sCode,
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                CustomDialog customDialog1 = new CustomDialog(SignInActivity.this, null,
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

    }

    @Override
    public void OnReceiveSocialInfo(SocialInterface.SocialInfo socialInfo) {

        Log.d("Socail", "Name :" + socialInfo.getFirstName() + " " + socialInfo.getLastName());
        Log.d("Socail", "EmailId :" + socialInfo.getEmailId());
        Log.d("Socail", "ProfilePicture :" + socialInfo.getProfilePicture());
        Log.d("Socail", "Type :" + socialInfo.getSocialMediaType());
        Log.d("Socail", "ID :" + socialInfo.getSocialId());

        if (Util.checkNetworkAvailablity(this)) {
            signUpRequest(socialInfo.getEmailId(), socialInfo.getSocialMediaType(),
                    socialInfo.getFirstName(),
                    socialInfo.getLastName(), socialInfo.getProfilePicture(), socialInfo.getSocialId());
        } else {
            CustomDialog customDialog1 = new CustomDialog(this, null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }

        hideProgressDialog();
    }

    public void generateHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    /**
     * [api_key],[email],[provider],[first_name],[last_name],[profileImage],[accountid]
     */
    private void signUpRequest(String email, String provider, String first_name, String last_name,
                               String profileImage, String accountid) {
        showProgressDialog();
        String refreshedToken = Util.getFcmToken();
        if (refreshedToken!=null && refreshedToken.length()==0){
            refreshedToken = Util.loadPrefrence(Util.FCM_TOKEN,"",SignInActivity.this);
        }
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<SocialLoginResponse> call = apiService.socialLoginRequest(
                BuildConfig.API_KEY,
                Util.isNull(email),
                Util.isNull(provider),
                Util.isNull(first_name),
                Util.isNull(last_name),
                Util.isNull(profileImage),
                Util.isNull(accountid),
                refreshedToken);

        call.enqueue(new Callback<SocialLoginResponse>() {

            @Override
            public void onResponse(Call<SocialLoginResponse> call, Response<SocialLoginResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (c.startsWith("2")) {
                    SocialLoginResponse loginResponse = response.body();
                    if (loginResponse.getData() != null && loginResponse.getStatus().equalsIgnoreCase("success")) {
                        com.unitybound.login.beans.socialLogin.User user = loginResponse.getData().getUser();
                        Util.savePreferences(Util.PREF_USER_EMAIL,
                                String.valueOf(user.getEmail()),
                                SignInActivity.this);
                        Util.savePreferences(Util.PREF_FIRST_NAME,
                                String.valueOf(user.getFirstName()),
                                SignInActivity.this);
                        Util.savePreferences(Util.PREF_LAST_NAME,
                                String.valueOf(user.getLastName()),
                                SignInActivity.this);
                        Util.savePreferences(Util.PREF_USER_ID,
                                String.valueOf(user.getId()),
                                SignInActivity.this);
                        Util.savePreferences(Util.PREF_USER_NAME,
                                String.valueOf(user.getUserName()),
                                SignInActivity.this);
                        Util.saveBooleanPreferences(Util.PREF_IS_LOGIN, true, SignInActivity.this);

                        Util.savePreferences(Util.PREF_USER_IMAGE, profileImage, SignInActivity.this);

                        redirectToLoginScreen();
                    } else {
                        CustomDialog customDialog1 = new CustomDialog(SignInActivity.this, null,
                                "", loginResponse.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                } else {
                    CustomDialog customDialog1 = new CustomDialog(SignInActivity.this, null,
                            "", "Server Error code : " + sCode,
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<SocialLoginResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                CustomDialog customDialog1 = new CustomDialog(SignInActivity.this, null,
                        "", t.toString(),
                        "ONFAILED");
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }
                customDialog1.show();
            }
        });
    }

    private void redirectToLoginScreen() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();
    }
}
