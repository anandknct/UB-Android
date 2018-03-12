package com.unitybound.login.socialActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.unitybound.R;
import com.unitybound.utility.Util;


public final class GooglePlusHandler implements GoogleApiClient.OnConnectionFailedListener {

    /**
     * Signing Options
     */
    private GoogleSignInOptions gso;

    /**
     * google api client
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Signin constant to check the activity result
     */
    public final static int RC_SIGN_IN = 100;
    private Context mContext;
    private SocialInterface mSocialInterface;
    private SocialInterface.SocialInfo mSocialInfo = new SocialInterface.SocialInfo();

    private GooglePlusHandler(Context mContext) {
        this.mContext = mContext;
        mSocialInterface = (SocialInterface) mContext;
    }

    public static GooglePlusHandler getInstance(Context mContext) {
//        if (mGooglePlusHandler == null)
        GooglePlusHandler mGooglePlusHandler = new GooglePlusHandler(mContext);
        return mGooglePlusHandler;
    }

    /**
     * Initializing google signin option
     */
    public void initializingGoogleSigninOptions() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    /**
     * Initializing google api client
     */
    public void initializingGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage((FragmentActivity) mContext /* FragmentActivity */, GooglePlusHandler.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    //This function will option signing intent
    public void signIn() {
      //  if (!mGoogleApiClient.isConnected()) {
             Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            ((Activity) mContext).startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void logOut(){

        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        Util.savePreferences("LoggedState","0",mContext);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(getClass().getSimpleName(), "getErrorMessage:=" + connectionResult.getErrorMessage());
        Toast.makeText(mContext.getApplicationContext(), "Login Failed" + connectionResult.getErrorMessage(),
                Toast.LENGTH_LONG).show();
        Util.dismissLoader(mContext);
    }

    //After the signing we are calling this function
    public void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            String resultData = "id=" + acct.getId() + ",name" + acct.getDisplayName() + ",email" +
                    acct.getEmail() + ",photo url" + acct.getPhotoUrl();

            try {
                Util.savePreferences("LoggedState","GOOGLEPLUS",mContext);
                Log.i(getClass().getSimpleName(), "response:=" + resultData);
                String displayName = acct.getDisplayName();
                if (displayName != null && !displayName.equalsIgnoreCase("") && displayName.contains(" ")) {
                    String[] displayNameArray = displayName.split(" ");
                    if (displayNameArray.length == 2) {
                        String firstName = displayNameArray[0];
                        String lastName = displayNameArray[1];
                        mSocialInfo.setFirstName(firstName);
                        mSocialInfo.setLastName(lastName);
                    }
                }

                mSocialInfo.setSocialId(acct.getId());
                mSocialInfo.setEmailId(acct.getEmail());
                mSocialInfo.setSocialMediaType(mContext.getResources().getString(R.string.google_plus));
                mSocialInfo.setProfilePicture(acct.getPhotoUrl().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mSocialInterface.OnReceiveSocialInfo(mSocialInfo);
        } else {
            //If login fails
            Log.i(getClass().getSimpleName(), "Login Failed");
            Toast.makeText(mContext.getApplicationContext(), "Login Failed",
                    Toast.LENGTH_LONG).show();
            Util.dismissLoader(mContext);
        }

    }
    protected void onDestroy() {
        mGoogleApiClient.stopAutoManage((FragmentActivity) mContext);
        mGoogleApiClient.disconnect();
    }

}
