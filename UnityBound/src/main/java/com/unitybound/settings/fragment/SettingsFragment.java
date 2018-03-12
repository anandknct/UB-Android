package com.unitybound.settings.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unitybound.BuildConfig;
import com.unitybound.R;
import com.unitybound.settings.fragment.beans.UsersSettingsResponse;
import com.unitybound.settings.fragment.general.activity.GeneralSettingsActivity;
import com.unitybound.settings.fragment.notifications.activity.NotificationsSettingsActivity;
import com.unitybound.settings.fragment.preferance.activity.PreferenceSettingsActivity;
import com.unitybound.settings.fragment.privacy.PrivacySettingsActivity;
import com.unitybound.utility.ErrorResponse;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;
import com.unitybound.utility.network.ApiClient;
import com.unitybound.utility.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by charchitkasliwal on 10/05/17.
 */

public class SettingsFragment extends Fragment {

    private boolean mCOMING_FROM_PROFILE = false;

    @BindView(R.id.tv_general)
    TextView tvGeneral;
    @BindView(R.id.tv_privacy)
    TextView tvPrivacy;
    @BindView(R.id.tv_notification)
    TextView tvNotification;
    @BindView(R.id.tv_preference)
    TextView tvPreference;
    private ProgressDialog mProgressDialog;
    private UsersSettingsResponse mSettingsResponse = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_layout, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this, view);

        mProgressDialog = new ProgressDialog(getActivity(), R.style.newDialog);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("COMING_FROM_PROFILE")) {
            mCOMING_FROM_PROFILE = bundle.getBoolean("COMING_FROM_PROFILE");
        }

        return view;
    }

    @OnClick({R.id.tv_general, R.id.tv_privacy, R.id.tv_notification, R.id.tv_preference})
    public void onClicks(View view) {
        switch (view.getId()) {
            case R.id.tv_general:
                startActivity(GeneralSettingsActivity.class);
                break;
            case R.id.tv_privacy:
                startActivityp(PrivacySettingsActivity.class);
                break;
            case R.id.tv_notification:
                startActivityn(NotificationsSettingsActivity.class);
                break;
            case R.id.tv_preference:
                startActivitypp(PreferenceSettingsActivity.class);
                break;
        }
    }

    private void startActivity(Class<GeneralSettingsActivity> activity) {
        Intent intent = new Intent(getActivity(), activity);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",mSettingsResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void startActivityp(Class<PrivacySettingsActivity> activity) {
        Intent intent = new Intent(getActivity(), activity);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",mSettingsResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void startActivityn(Class<NotificationsSettingsActivity> activity) {
        Intent intent = new Intent(getActivity(), activity);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",mSettingsResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void startActivitypp(Class<PreferenceSettingsActivity> activity) {
        Intent intent = new Intent(getActivity(), activity);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",mSettingsResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        if (Util.checkNetworkAvailablity(getActivity())) {
            if (mCOMING_FROM_PROFILE) {
                getAllUserSettingsAndInfo();
            } else {
                getAllUserSettingsAndInfo();
            }
        } else {
            CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
                    "", getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
        super.onResume();
    }

    private void getAllUserSettingsAndInfo() {
        showProgressDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<UsersSettingsResponse> call = apiService.getAllSettings(
                BuildConfig.API_KEY,
                Util.loadPrefrence(Util.PREF_USER_ID, "", getActivity()));

        call.enqueue(new Callback<UsersSettingsResponse>() {

            @Override
            public void onResponse(Call<UsersSettingsResponse> call, Response<UsersSettingsResponse> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    Log.d("nik", response.body().toString());
                }
                String sCode = response.code() + "";
                String c = String.valueOf(sCode.charAt(0));

                switch (c) {
                    case "2":
                        UsersSettingsResponse settingsResponse = response.body();
                        if (settingsResponse.getData() != null) {
                            mSettingsResponse = settingsResponse;
                        }
                        break;
                    case "4":
                        if (sCode == "401") {
                            CustomDialog customDialog1 = new CustomDialog(getActivity(), null,
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
                                    new CustomDialog(getActivity(), null, "",
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
                        UsersSettingsResponse loginResponse1 = response.body();
                        CustomDialog customDialog1 = new CustomDialog(getActivity(),
                                null, "", loginResponse1.getMsg(),
                                "ONFAILED");
                        if (customDialog1.isShowing()) {
                            customDialog1.dismiss();
                        }
                        customDialog1.show();
                        hideProgressDialog();
                        break;

                    default: // TODO Handle error message and show dialog here
                        UsersSettingsResponse body1 = response.body();
                        new CustomDialog(getActivity(), null, "",
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
            public void onFailure(Call<UsersSettingsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("nik", t.toString());
                hideProgressDialog();
                new CustomDialog(getActivity(), null, "",
                        t.getMessage() != null ?
                                t.getMessage().length() > 0 ?
                                        t.getMessage()
                                        : "Something went wrong" : "Something went wrong",
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

}
