package com.unitybound.signup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.login.SignInActivity;
import com.unitybound.login.TermsAndConditionsActivity;
import com.unitybound.main.BaseActivity;
import com.unitybound.main.MainActivity;
import com.unitybound.signup.adapter.RelationShipSpinnerAdapter;
import com.unitybound.signup.beans.SignUpResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by charchitkasliwal on 03/05/17.
 */

public class SignUpActivity extends BaseActivity implements CustomDialog.IDialogListener {
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
    @BindView(R.id.edt_username)
    AppCompatEditText edtUsername;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    @BindView(R.id.rg_layout)
    RadioGroup rgLayout;
    @BindView(R.id.spinner_relationship_status)
    Spinner spRelationship;
    private ArrayList<String> spinnerData;

    /**
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        tvAlreadyaccount.setText(Html.fromHtml(getString(R.string.str_already_account_login)));

        spinnerData = new ArrayList<>();
        spinnerData.add("Engaged");
        spinnerData.add("Dating");
        spinnerData.add("Married");
        spinnerData.add("Single");
        spinnerData.add("Widow");
        RelationShipSpinnerAdapter spAdapter = new RelationShipSpinnerAdapter(
                this,
                spinnerData);
        spRelationship.setAdapter(spAdapter);
    }

    @OnClick(R.id.tv_alreadyaccount)
    public void setTvAlreadyaccount() {
        finish();
        Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_createacc)
    public void createAccount() {
        if (Util.checkNetworkAvailablity(this)) {
            if (validate()) {
                signUpRequest();
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

    @OnClick(R.id.edt_dob)
    public void selectDob() {
        showDatePicker(edtDob.getText().toString());
    }

    @OnClick(R.id.tv_declare)
    public void Terms() {
         Intent i = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
         i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
         startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * To validate the login inputs.
     *
     * @return
     */
    private boolean validate() {

        if (edtFirstname.getText().toString().length() == 0) {
            new CustomDialog(SignUpActivity.this, SignUpActivity.this, "",
                    "Please enter your first name",
                    "ONFAILED").show();
            edtFirstname.requestFocus();
            return false;
        } else if (edtLastname.getText().toString().length() == 0) {
            new CustomDialog(SignUpActivity.this, SignUpActivity.this, "",
                    "Please enter your last name",
                    "ONFAILED").show();
            edtLastname.requestFocus();
            return false;
        } else if (edtEmail.getText().toString().length() == 0) {
            new CustomDialog(SignUpActivity.this, SignUpActivity.this, "",
                    "Please enter your email ID",
                    "ONFAILED").show();
            edtEmail.requestFocus();
            return false;
        } else if (!Util.isEmailValid(edtEmail.getText().toString().trim())) {
            new CustomDialog(SignUpActivity.this, SignUpActivity.this, "",
                    "Please enter valid email ID",
                    "ONFAILED").show();
            edtEmail.requestFocus();
            return false;
        } else if (edtUsername.getText().toString().trim().length() == 0) {
            new CustomDialog(SignUpActivity.this, SignUpActivity.this, "",
                    "Please enter your username",
                    "ONFAILED").show();
            edtEmail.requestFocus();
            return false;
        } else if (edtUsername.getText().toString().trim().length() < 4) {
            new CustomDialog(SignUpActivity.this, SignUpActivity.this, "",
                    "Please enter your username minimum 4 characters",
                    "ONFAILED").show();
            edtEmail.requestFocus();
            return false;
        } else if (edtPassword.getText().toString().length() < 6) {
            new CustomDialog(SignUpActivity.this, SignUpActivity.this, "",
                    "Password must be greater than 6 character",
                    "ONFAILED").show();
            edtPassword.requestFocus();
            return false;
        } else if (edtDob.getText().toString().length() == 0) {
            new CustomDialog(SignUpActivity.this, SignUpActivity.this, "",
                    "Please enter your DOB",
                    "ONFAILED").show();
            edtDob.requestFocus();
            return false;
        } else if (edtAccesscode.getText().toString().length() == 0) {
            new CustomDialog(SignUpActivity.this, SignUpActivity.this, "",
                    "Please enter your access code",
                    "ONFAILED").show();
            edtAccesscode.requestFocus();
            return false;
        } else if (rgLayout.getCheckedRadioButtonId() == -1) {
            new CustomDialog(SignUpActivity.this, SignUpActivity.this, "",
                    "Please select your gender",
                    "ONFAILED").show();
            edtAccesscode.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void signUpRequest() {
        showProgressDialog();
        String refreshedToken = Util.getFcmToken();
        if (refreshedToken!=null && refreshedToken.length()==0){
            refreshedToken = Util.loadPrefrence(Util.FCM_TOKEN,"",SignUpActivity.this);
        }
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<SignUpResponse> call = apiService.registrationRequest(
                BuildConfig.API_KEY,
                Util.isNull(edtFirstname.getText().toString().trim()),
                Util.isNull(edtLastname.getText().toString().trim()),
                Util.isNull(edtEmail.getText().toString().trim()),
                Util.isNull(edtUsername.getText().toString().trim()),
                Util.isNull(edtPassword.getText().toString().trim()),
                Util.isNull(edtDob.getText().toString().trim()),
                Util.isNull(edtAccesscode.getText().toString().trim()),
                rbMale.isChecked() ? "male" : rbFemale.isChecked() ? "female" : "male",
                spinnerData.get(spRelationship.getSelectedItemPosition()),
                refreshedToken);

        call.enqueue(new Callback<SignUpResponse>() {

            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                if (c.equalsIgnoreCase("2")) {
                    SignUpResponse signUpResponse = response.body();
                    if (signUpResponse.getData() != null
                            && signUpResponse.getStatus().equalsIgnoreCase("success")) {
                        com.unitybound.signup.beans.User user = signUpResponse.getData().getUser();
                        Util.savePreferences(Util.PREF_USER_EMAIL,
                                String.valueOf(user.getEmail()),
                                SignUpActivity.this);
                        Util.savePreferences(Util.PREF_FIRST_NAME,
                                String.valueOf(user.getFirstName()),
                                SignUpActivity.this);
                        Util.savePreferences(Util.PREF_LAST_NAME,
                                String.valueOf(user.getLastName()),
                                SignUpActivity.this);
                        Util.savePreferences(Util.PREF_NICK_NAME,
                                String.valueOf(user.getFirstName()),
                                SignUpActivity.this);
                        Util.savePreferences(Util.PREF_USER_ID,
                                String.valueOf(user.getId()),
                                SignUpActivity.this);
                        Util.savePreferences(Util.PREF_USER_NAME,
                                String.valueOf(user.getUserName()),
                                SignUpActivity.this);

                        Util.saveBooleanPreferences(Util.PREF_IS_LOGIN,
                                true,
                                SignUpActivity.this);

                        Intent intent = new Intent(SignUpActivity.this,
                                MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        finish();
                    } else {

                        CustomDialog customDialog1 = new CustomDialog(SignUpActivity.this,
                                null,
                                "", signUpResponse.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                    }
                } else {

                    CustomDialog customDialog1 = new CustomDialog(SignUpActivity.this, null,
                            "", "Server error please try again.",
                            "ONFAILED");
                    if (customDialog1.isShowing()) {
                        customDialog1.dismiss();
                    }
                    customDialog1.show();
                }

            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
            }

        });
    }

    @Override
    public void onCancelPress(String param) {

    }

    @Override
    public void onYesPress(String param, String message) {

    }

    //----------DATE PICKER----------------------------------------------------------------------------
    @SuppressWarnings("deprecation")
    public void showDatePicker(String selectedDate) {
        Calendar calendar = Calendar.getInstance();
        int mTyear = calendar.get(Calendar.YEAR);
        int mTmonth = calendar.get(Calendar.MONTH);
        int mTday = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                ToDateListener, mTyear, mTmonth,
                mTday);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        if (selectedDate.length() > 0) {
            String[] separated = selectedDate.split("-");
            Log.d("Date ", "" + separated[0] + "-" + Integer.valueOf(separated[1]) + "-" + Integer.valueOf(separated[2]));
            datePickerDialog.updateDate(Integer.valueOf(separated[0]), Integer.valueOf(separated[1]) - 1, Integer.valueOf(separated[2]));
        }
        datePickerDialog.show();
    }

//    private int mTyear, mTmonth, mTday;
    private DatePickerDialog.OnDateSetListener ToDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
//            mTyear = arg1;
//            mTmonth = arg2 + 1;
//            mTday = arg3;
            setToDate(arg1, arg2 + 1, arg3);
        }
    };

    /**
     * @param year  the year
     * @param month the month
     * @param day   the day
     */
    private void setToDate(int year, int month, int day) {
//        this.mTyear = year;
//        this.mTmonth = month;
//        this.mTday = day;
       /*
        edt_to_date.setText(new StringBuilder().append(year).append("-")
                .append((month < 10 ? "0" + month : month)).append("-").append(day < 10 ? "0" + day : day));
        edt_to_date.setError(null);
        */

        StringBuilder fromDate = new StringBuilder().append(day < 10 ? "0" + day : day).append("/")
                .append((month < 10 ? "0" + month : month)).append("/").append(year);
        edtDob.setText(fromDate);
//            mListner.onFromDateSelected(fromDate.toString());

    }

}
