package com.unitybound.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.unitybound.account.activity.FullScreenPhotoActivity;
import com.unitybound.account.activity.MyAccountAboutActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by charchitkasliwal on 10/05/17.
 */

public class Util {

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
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
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


    public static void navigateTOFullScreenAcitivity(Activity activity) {
        Intent intent = new Intent(activity, FullScreenPhotoActivity.class);
        activity.startActivity(intent);
    }

    public static void navigateTOProfileAcitivity(Activity activity) {
        Intent intent = new Intent(activity, MyAccountAboutActivity.class);
        activity.startActivity(intent);
    }
}
