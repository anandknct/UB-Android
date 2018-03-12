package com.unitybound.login.socialActivity;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.unitybound.R;
import com.unitybound.utility.Util;
import com.unitybound.utility.customView.CustomDialog;

/**
 * Created by chetan.joshi on 05-08-2016.
 * Class SocialIntegrations is used to provide common Interface for multiple social mediums
 */
public class SocialIntegrations {

    private Context mContext;
    private FacebookHandler facebookHandler;
    private GooglePlusHandler googlePlusHandler;
    private SocialInterface.SOCIAL_MEDIUMS SELECTED_MEDIUM;

    /**
     * Singleton Constructor
     */
    private SocialIntegrations(Context mContext) {
        this.mContext = mContext;
        initSocialIntegrations();
    }

    /**
     * Singleton method to get instance of  SocialIntegrations
     */
    public static SocialIntegrations getInstance(Context mContext) {
//        if (mSocialIntegrations == null)
        SocialIntegrations mSocialIntegrations = new SocialIntegrations(mContext);
        return mSocialIntegrations;
    }

    /**
     * init all social mediums first time
     */
    public void initSocialIntegrations() {
        facebookHandler = FacebookHandler.getInstance(mContext);
        facebookHandler.initFacebookSdk();
        googlePlusHandler = GooglePlusHandler.getInstance(mContext);
        googlePlusHandler.initializingGoogleSigninOptions();
        googlePlusHandler.initializingGoogleApiClient();
    }

    /**
     * catching  onActivityResult from Activity class
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (SELECTED_MEDIUM) {
            case FACEBOOK:
                facebookHandler.mCallbackManager.onActivityResult(requestCode, resultCode, data);
                break;

            case GOOGLEPLUS:
                if (requestCode == GooglePlusHandler.RC_SIGN_IN) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    //Calling a new function to handle sign in
                    googlePlusHandler.handleSignInResult(result);
                }
                break;
        }
    }

    /**
     * method is used to initialize social media type and call sigIn method of selected one.
     */
    public void signIn(SocialInterface.SOCIAL_MEDIUMS SELECTED_MEDIUM) {
        this.SELECTED_MEDIUM = SELECTED_MEDIUM;
        /**
         * Check if device network is on or not
         */
        if (Util.haveNetworkConnection(mContext)) {
            switch (this.SELECTED_MEDIUM) {
                case FACEBOOK:
                    facebookHandler.signIn();
                    break;
                case GOOGLEPLUS:
                    googlePlusHandler.signIn();
                    break;
                case TWITTER:

                    break;
            }
        } else {
            CustomDialog customDialog1 = new CustomDialog(mContext, null,
                    "", mContext.getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    public void logOut(SocialInterface.SOCIAL_MEDIUMS SELECTED_MEDIUM) {
        this.SELECTED_MEDIUM = SELECTED_MEDIUM;
        /**
         * Check if device network is on or not
         */
        if (Util.haveNetworkConnection(mContext)) {
            switch (this.SELECTED_MEDIUM) {
                case FACEBOOK:
                    facebookHandler.logOut();
                    break;

                case GOOGLEPLUS:
                    googlePlusHandler.logOut();
                    break;
            }
        } else {
            CustomDialog customDialog1 = new CustomDialog(mContext, null,
                    "", mContext.getResources().getString(R.string.alt_checknet),
                    "ONFAILED");
            if (customDialog1.isShowing()) {
                customDialog1.dismiss();
            }
            customDialog1.show();
        }
    }

    public void onDestroy() {
        switch (this.SELECTED_MEDIUM) {
            case FACEBOOK:
                //  facebookHandler.logOut();
                break;

            case GOOGLEPLUS:
                googlePlusHandler.onDestroy();
                break;
        }
    }

}