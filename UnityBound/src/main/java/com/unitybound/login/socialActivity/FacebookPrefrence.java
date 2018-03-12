package com.unitybound.login.socialActivity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by charchit.kasliwal on 17-08-2016.
 */
public final class FacebookPrefrence {
    private static final String PREF_NAME = "SBTS_FACEBOOK_PREF";
    private static final int MODE = Context.MODE_PRIVATE;
    private static FacebookPrefrence mAppPreferences;
    private SharedPreferences sharedPreferences;

    /**
     * Facebook Access Token key for preferences
     */
    public static final String FACEBOOK_ACCESS_TOKEN = "FacebookAccessToken";
    /**
     * Facebook User Id key for preferences
     */
    public static final String FACEBOOK_USER_ID = "FacebookUserId";
    public static final String FACEBOOK_APPLICATION_ID = "applicationId";
    public static final String FACEBOOK_PERMISSIONS = "permissions";
    public static final String FACEBOOK_EXPIRATION_TIME = "expirationTime";
    public static final String FACEBOOK_DECLINED_PERMISSIONS = "declinedPermissions";
    public static final String FACEBOOK_ACCESS_TOKEN_SOURCE = "accessTokenSource";
    public static final String FACEBOOK_LAST_REFRENCE_TIME = "lastRefreshTime";


    public static FacebookPrefrence getInstance(Context context) {
        if (mAppPreferences == null) {
            mAppPreferences = new FacebookPrefrence(context);
        }
        return mAppPreferences;
    }

    private FacebookPrefrence(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }


}
