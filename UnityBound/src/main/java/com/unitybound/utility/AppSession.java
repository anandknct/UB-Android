package com.unitybound.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by charchitkasliwal on 05/03/17.
 */

public class AppSession {
    /**
     * Variable declaration
     */
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor prefsEditor;
    private static final String PREF_USER_LOGGED_IN_FIRST = "userLoggedInFirst";
    private static final String PREF_USER_EMAIL_ID = "userEmailId";
    private static final String PREF_USER_LOGIN_TYPE = "userLoginType";
    private static final String PREF_USER_PASSWORD = "userPassword";
    private static final String PREF_USER_FIRST_NAME = "userFirstName";
    private static final String PREF_USER_LAST_NAME = "userLastName";
    private static final String PREF_USER_GENDER = "userGender";
    private static final String PREF_USER_DATE_OF_BIRTH = "userDob";
    private static final String PREF_USER_ACCESS_CODE = "userAccessCode";

    private static final String SESSION_NAME = "com.unitybound";


    /**
     * AppSession
     * @param context
     */
    public AppSession(Context context) {
        mSharedPreferences = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
    }

    public void createUserLoginSession(String email, String password,  String gender,
                                       String firstName,String lastName, String loginType,String userAccessCode, String dob){
        prefsEditor = mSharedPreferences.edit();
        if ( TextUtils.isEmpty(email)){
            prefsEditor.putString(PREF_USER_EMAIL_ID, "");
            prefsEditor.putString(PREF_USER_PASSWORD, "");
            prefsEditor.putString(PREF_USER_FIRST_NAME, "");
            prefsEditor.putString(PREF_USER_LAST_NAME, "");
            prefsEditor.putString(PREF_USER_GENDER, "");
            prefsEditor.putString(PREF_USER_ACCESS_CODE, "");
            prefsEditor.putString(PREF_USER_DATE_OF_BIRTH, "");
            prefsEditor.putString(PREF_USER_LOGGED_IN_FIRST,"");
        }
        else  {
            prefsEditor.putString(PREF_USER_EMAIL_ID, email);
            prefsEditor.putString(PREF_USER_PASSWORD, password);
            prefsEditor.putString(PREF_USER_GENDER, gender);
            prefsEditor.putString(PREF_USER_ACCESS_CODE, userAccessCode);
            prefsEditor.putString(PREF_USER_DATE_OF_BIRTH, dob);
            prefsEditor.putString(PREF_USER_FIRST_NAME, firstName);
            prefsEditor.putString(PREF_USER_LAST_NAME, lastName);
            prefsEditor.putString(PREF_USER_LOGGED_IN_FIRST,"true");
        }
        prefsEditor.commit();
    }

    public void setEmailIdPref(String token){
        prefsEditor = mSharedPreferences.edit();
        if ( TextUtils.isEmpty(token ))
            prefsEditor.putString(PREF_USER_EMAIL_ID, "");
        else  {
            prefsEditor.putString(PREF_USER_EMAIL_ID, token);
        }
        prefsEditor.commit();
    }

    public void setPasswordPref(String token){
        prefsEditor = mSharedPreferences.edit();
        if ( TextUtils.isEmpty(token ))
            prefsEditor.putString(PREF_USER_PASSWORD, "");
        else  {
            prefsEditor.putString(PREF_USER_PASSWORD, token);
        }
        prefsEditor.commit();
    }

    public void setPrefUserLoggedInFirst(String token){
        prefsEditor = mSharedPreferences.edit();
        if ( TextUtils.isEmpty(token ))
            prefsEditor.putString(PREF_USER_LOGGED_IN_FIRST, "");
        else  {
            prefsEditor.putString(PREF_USER_LOGGED_IN_FIRST, token);
        }
        prefsEditor.commit();
    }

    public String getUserEmailId(){
        return mSharedPreferences.getString(PREF_USER_EMAIL_ID, "");
    }

    public String getUserPassword(){
        return mSharedPreferences.getString(PREF_USER_PASSWORD, "");
    }
    public String getPrefUserLoggedInFirst(){
        return mSharedPreferences.getString(PREF_USER_LOGGED_IN_FIRST, "");
    }

    public String getPrefUserFirstName(){
        return mSharedPreferences.getString(PREF_USER_FIRST_NAME, "");
    }

    public String getPrefUserLastName(){
        return mSharedPreferences.getString(PREF_USER_LAST_NAME, "");
    }

    public String getPrefUserLoginType(){
        return mSharedPreferences.getString(PREF_USER_LOGIN_TYPE, "");
    }
}



