package com.unitybound.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.unitybound.account.activity.FullScreenPhotoActivity;
import com.unitybound.account.activity.MyAccountAboutActivity;
import com.unitybound.login.SignInActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;

/**
 * Created by charchitkasliwal on 10/05/17.
 */

public class Util {

    // Preference Constants
    public static String PREFERENCE_NAME = "twitter_oauth";
    public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
//    public static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
    // Twitter oauth urls
    public static final String URL_TWITTER_AUTH = "auth_url";
    public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    public static final String PREF_USER_IMAGE = "IMAGE_USER";
    public static final String FCM_TOKEN = "FCM_TOKEN";
    public static final String PREF_IS_LOGIN = "is_login";
    public static final String PREF_USER_EMAIL = "email";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_FIRST_NAME = "fname";
    public static final String PREF_LAST_NAME = "lname";
    public static final String PREF_NICK_NAME = "nickname";
    public static final String PREF_USER_NAME = "username";
    public static final int REQUEST_CAMERA = 00123;
    public static final int SELECT_FILE = 00124;
    public static final String CHURCH_ID = "CHURCH_ID";
    public static final String GROUP_ID = "GROUP_ID";
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static ProgressDialog dialog = null;
    public static final int DIALOG_LOGOUT_ID = 1000;
    public static final int DIALOG_REGISTRATION_ID = 2000;
    public static final int DIALOG_FORGOT_PASSWORD_ID = 3000;
    public static final int DIALOG_EMAIL_VERIFICATION = 4000;
    public static final int DIALOG_CHANGE_PWD_SUCCESS = 5000;
    public static final int DIALOG_UPDATED_SUCCESS = 6000;
    public static final int DIALOG_LOCATION_LIST = 7000;
    private static int REQUEST_CHECK_SETTINGS = 100;

    public static final String TWITTER_CONSUMER_KEY = "rVnQ3g6cxMBY75YgwDIWfve5K";
    public static final String TWITTER_CONSUMER_SECREAT_KEY = "f1loI0mTGxrIeAAhQW0lboSCjoqaewPIS2GVkBPasbqSgZSI6E";
    public static final String TWITTER_CALLBACK_URL = "http://www.hiteshi.com/";

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    /**
     * @param activity the calling activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    0);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }


    /* * Hides soft keyboard.
     *
             * @param editText EditText which has focus
     */
    public static void hideSoftKeyboard(Context mContext, EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static boolean haveNetworkConnection(Context mContext) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        final ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI") && ni.isConnected()) {
                haveConnectedWifi = true;
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE") && ni.isConnected()) {
                haveConnectedMobile = true;
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * This function checks for the network connection availability. If there is
     * any issue with connection it will throw exception
     *
     * @return true, if successful
     * @throws Exception the exception
     *                   {@link //NetConnectException} to with proper error message, which will be
     *                   send to calling activity.
     */
    public static boolean checkNetworkAvailablity(Context mContext) {
        try {
            ConnectivityManager cMgr = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
            Log.i("Charchit", "in lib checkNetworkAvailablity()= netInfo.getState() =  " + netInfo.getState());
            if (!netInfo.getState().toString().equals("CONNECTED")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Save the value in preference
     *
     * @param key
     * @param value
     * @param activity
     */
    public static void savePreferences(String key, String value,
                                       Context activity) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveBooleanPreferences(String key, Boolean value,
                                              Context activity) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean loadBooleanPrefrence(String key,
                                               Context activity) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * get the value from preference
     *
     * @param key
     * @param defaultStr
     * @param activity
     * @return
     */
    public static String loadPrefrence(String key, String defaultStr,
                                       Context activity) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        return sharedPreferences.getString(key, defaultStr);
    }

    /**
     * This method dismiss loader
     */
    public static void dismissLoader(Context context) throws NullPointerException {
        try {
            dialog = new ProgressDialog(context);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method shows toast message
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) throws NullPointerException {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method serialize the object
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    /**
     * This method deserialize the object
     *
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(byte[] bytes) throws IOException,
            ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }

    /**
     * @param context the application context
     * @param id      the id
     * @return resource color return
     */
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }


    /**
     * This method validate email id.
     *
     * @param email the email
     * @return true, if is email valid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void navigateTOFullScreenActivity(Context activity, String Url,
                                                    String User_profile_id, String likesCount,
                                                    String commentsCount) {
        Intent intent = new Intent(activity, FullScreenPhotoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra("URL",Url);
        intent.putExtra("PROFILE_USER_USER_ID",User_profile_id);
        intent.putExtra("LIKES",likesCount);
        intent.putExtra("COMMENTS",commentsCount);
        activity.startActivity(intent);
    }

    public static void navigateTOProfileAcitivity(Activity activity) {
        Intent intent = new Intent(activity, MyAccountAboutActivity.class);
        activity.startActivity(intent);
    }

    public static String getFcmToken() {
        // Get token
        String token = FirebaseInstanceId.getInstance().getToken();
        return token;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static String dateFormator(String dateTime) {
        String date = "2014-11-25 14:30";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Date testDate = null;
        try {
            testDate = sdf.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy"); // dd/MMM/yyyy hh:mm a
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);
        return newFormat;
    }

    public final static String eventDateFormator(String dateTime) { // 2017-08-29 13:03:55
        String date = "2014-11-25 14:30";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd,yyyy ( EEEE )"); // dd/MMM/yyyy hh:mm a
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);
        return newFormat;
    }

    public final static String weddingDateFormator(String dateTime) { // 15-09-2017
        String date = "2014-11-25 14:30";
        SimpleDateFormat comingDateFormator = new SimpleDateFormat("yyyy-mm-dd");
        Date testDate = null;
        try {
            testDate = comingDateFormator.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat outputDateFormator = new SimpleDateFormat("MMM dd, yyyy"); // dd/MMM/yyyy hh:mm a
        String newFormat = outputDateFormator.format(testDate);
        System.out.println(".....Date..." + newFormat);
        return newFormat;
    }

    public final static String eventDetailDateFormator(String dateTime) { // 2017-08-29 13:03:55
        String date = "2014-11-25 14:30";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy"); // dd/MMM/yyyy hh:mm a
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);
        return newFormat;
    }

    public final static String weddingDetailFormator(String dateTime) { // 04-11-2017
        String date = "2014-11-25 14:30";
        SimpleDateFormat comingDateFormator = new SimpleDateFormat("dd-mm-yyyy");
        Date testDate = null;
        try {
            testDate = comingDateFormator.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat outputDateFormator = new SimpleDateFormat("MMM dd, yyyy"); // dd/MMM/yyyy hh:mm a
        String newFormat = outputDateFormator.format(testDate);
        System.out.println(".....Date..." + newFormat);
        return newFormat;
    }

    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static String isNull(String string) {
        if (string!=null){
            return string;
        }else{
            return "";
        }
    }

    public static void unAuthorisedRedirectToLogin(Context mContext) {
        Util.clearAllPrefrences(mContext);
        ((Activity) mContext).finish();
        Intent i = new Intent(mContext, SignInActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(i);
    }

    public static void clearAllPrefrences(Context activity) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static int pxToDp(Context context,int px)
    {
        DisplayMetrics displayMetrics =context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
}
