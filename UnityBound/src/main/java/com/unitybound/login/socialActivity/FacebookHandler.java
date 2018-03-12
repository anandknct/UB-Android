package com.unitybound.login.socialActivity;

/**
 * Created by chetan.joshi on 03-08-2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.unitybound.R;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * This class handles all facebook actions like: Wall post, Share via Messenger, App invite.
 */
public final class FacebookHandler {

    public static final String IDENTITYAPP_PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=com.facebook.katana&hl=en";
    public static final String FACEBOOK_MESSENGER_PACKAGE = "com.facebook.orca";
    public static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    private static FacebookPrefrence mAppPreferences;
    public CallbackManager mCallbackManager = null;

    private Context mContext;
    private SocialInterface mSocialInterface;
    private SocialInterface.SocialInfo mSocialInfo = new SocialInterface.SocialInfo();

    private FacebookHandler(Context mContext) {
        this.mContext = mContext;
        mSocialInterface = (SocialInterface) mContext;
        mAppPreferences = FacebookPrefrence.getInstance(mContext);
    }

    public static FacebookHandler getInstance(Context mContext) {
//        if (mFacebookHandler == null)
        FacebookHandler mFacebookHandler = new FacebookHandler(mContext);
        return mFacebookHandler;
    }


    public void initFacebookSdk() {
        //Init FACEBOOK SDK
        FacebookSdk.sdkInitialize(mContext.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
    }

    /**
     * This method init facebook login
     *
     * @param activity
     * @param callbackManager
     */
    public void initFaceBookLogin(final Activity activity,
                                  final CallbackManager callbackManager) {
        /**
         * Check if device network is on or not
         */
        if (Util.haveNetworkConnection(mContext)) {
            /**
             * Go for fallback web version of facebook
             */
            List<String> permissionNeeds = Arrays.asList("public_profile", "email");

            //this loginManager helps you eliminate adding a LoginButton to your UI
            LoginManager manager = LoginManager.getInstance();

            manager.logInWithReadPermissions(activity, permissionNeeds);
            manager.registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Util.savePreferences("LoggedState","FACEBOOK",mContext);
                            storeAccessToken(loginResult.getAccessToken());
                            FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
                            getLoginInfo(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Util.showToast(activity, exception.toString());
                        }
                    });
        } else {
            CustomDialog customDialog1 = new CustomDialog(activity, null,
                    "", activity.getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    // This Method is used to generate &quot;Android Package Name&quot; hash key
    public String getFacebookHashKey(Context mContext) {
        String mHashKey = "";
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    "com.sbts.parentapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                mHashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", "mHashKey" + mHashKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return mHashKey;
    }

    /**
     * method is used to get Login Details and  Login to Facebook
     */
    public void signIn() {
        // Utility.showLoader(mContext,mContext.getResources().getString(R.string.alt_pleasewait));
        FacebookPrefrence appPreferences = FacebookPrefrence.getInstance(mContext);
        //If FACEBOOK ACCESS TOKEN IS EMPTY THEN LOGIN TO FACEBOOK ELSE SHARE DATA ON FACEBOOK
        if (appPreferences.getData(appPreferences.FACEBOOK_ACCESS_TOKEN).equalsIgnoreCase("")) {
            initFaceBookLogin((Activity) mContext, mCallbackManager);
        } else {
            try {
                AccessToken accessToken = getAccessToken();
                if (accessToken != null) {
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
                    Util.savePreferences("LoggedState","FACEBOOK",mContext);
                    getLoginInfo(accessToken);
                } else {
                    initFaceBookLogin((Activity) mContext, mCallbackManager);
                }
            } catch (Exception e) {
                e.printStackTrace();
                initFaceBookLogin((Activity) mContext, mCallbackManager);
            }

        }
    }


    /**
     * method is used to get user info by calling Graph API
     */
    private void getLoginInfo(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject response,
                            GraphResponse graphResponse) {
                        // Application code

                        try {
                            Log.i(getClass().getSimpleName(), "response:=" + graphResponse);
                            mSocialInfo.setSocialId(response.optString("id"));
                            mSocialInfo.setEmailId(response.optString("email"));
                            mSocialInfo.setFirstName(response.optString("first_name"));
                            mSocialInfo.setLastName(response.optString("last_name"));
                            mSocialInfo.setProfilePicture(getFacebookProfilePictureUrl(mSocialInfo.getSocialId()));
                            mSocialInfo.setSocialMediaType(mContext.getResources().getString(R.string.facebook));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Util.dismissLoader(mContext);
                        }
                        mSocialInterface.OnReceiveSocialInfo(mSocialInfo);

                    }


                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,birthday,first_name,last_name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * method is used to get facebook profile picture url
     */
    private String getFacebookProfilePictureUrl(String userID) {
        return "https://graph.facebook.com/" + userID + "/picture?type=large";
    }


    private void storeAccessToken(AccessToken accessToken) {
        mAppPreferences.saveData(mAppPreferences.FACEBOOK_ACCESS_TOKEN, accessToken.getToken());
        mAppPreferences.saveData(mAppPreferences.FACEBOOK_USER_ID, accessToken.getUserId());
        mAppPreferences.saveData(mAppPreferences.FACEBOOK_ACCESS_TOKEN_SOURCE, accessToken.getSource().toString());
        mAppPreferences.saveData(mAppPreferences.FACEBOOK_APPLICATION_ID, accessToken.getApplicationId());
        String declinedPermissions = TextUtils.join(", ", accessToken.getDeclinedPermissions());
        mAppPreferences.saveData(mAppPreferences.FACEBOOK_DECLINED_PERMISSIONS, declinedPermissions);
        String permissions = TextUtils.join(", ", accessToken.getPermissions());
        mAppPreferences.saveData(mAppPreferences.FACEBOOK_PERMISSIONS, permissions);
        try {
            String date1 = Base64.encodeToString(Util.serialize(accessToken.getExpires()), Base64.DEFAULT);
            mAppPreferences.saveData(mAppPreferences.FACEBOOK_EXPIRATION_TIME, date1);
            String date2 = Base64.encodeToString(Util.serialize(accessToken.getLastRefresh()), Base64.DEFAULT);
            mAppPreferences.saveData(mAppPreferences.FACEBOOK_LAST_REFRENCE_TIME, date2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOut(){
        FacebookPrefrence appPreferences = FacebookPrefrence.getInstance(mContext);
        //If FACEBOOK ACCESS TOKEN IS EMPTY THEN LOGIN TO FACEBOOK ELSE SHARE DATA ON FACEBOOK
        if (!appPreferences.getData(appPreferences.FACEBOOK_ACCESS_TOKEN).equalsIgnoreCase("")) {
            LoginManager.getInstance().logOut();
            Util.savePreferences("LoggedState","0",mContext);
        }
    }

    public AccessToken getAccessToken() {
        AccessToken accessToken = null;
        String applicationId = mAppPreferences.getData(mAppPreferences.FACEBOOK_APPLICATION_ID);
        String userId = mAppPreferences.getData(mAppPreferences.FACEBOOK_USER_ID);
        String token = mAppPreferences.getData(mAppPreferences.FACEBOOK_ACCESS_TOKEN);
        String permissions = mAppPreferences.getData(mAppPreferences.FACEBOOK_PERMISSIONS);
        String declinedPermissions = mAppPreferences.getData(mAppPreferences.FACEBOOK_DECLINED_PERMISSIONS);
        String[] array1 = permissions.split(",");
        String[] array2 = declinedPermissions.split(",");
        ArrayList<String> permissionsList = new ArrayList(Arrays.asList(array1));
        ArrayList<String> declinedPermissionsList = new ArrayList(Arrays.asList(array2));
        AccessTokenSource source = AccessTokenSource.TEST_USER;
        String expires = mAppPreferences.getData(mAppPreferences.FACEBOOK_EXPIRATION_TIME);
        String lastRefresh = mAppPreferences.getData(mAppPreferences.FACEBOOK_LAST_REFRENCE_TIME);
        try {
            Date expiresDate = (Date) Util.deserialize(Base64.decode(expires, Base64.DEFAULT));
            Date lastRefreshDate = (Date) Util.deserialize(Base64.decode(lastRefresh, Base64.DEFAULT));
            accessToken = new AccessToken(
                    token,
                    applicationId,
                    userId,
                    permissionsList,
                    declinedPermissionsList,
                    source,
                    expiresDate,
                    lastRefreshDate);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

}


//{"last_name":"Row",
//        "id":"1054255761339536",
//        "first_name":"James",
//        "email":"testjames@yahoo.in",
//        "link":"https:\/\/www.facebook.com\/app_scoped_user_id\/1054255761339536\/"}

