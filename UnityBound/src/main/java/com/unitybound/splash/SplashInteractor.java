package com.unitybound.splash;

import android.app.Activity;
import android.content.Context;

import com.unitybound.utility.Util;


/**
 * Created by charchitkasliwal on 09/04/17.
 */

public class SplashInteractor {

    private final Context mContext;
    private SplashContract.onSplashListener mOnSplashListener;


    public SplashInteractor(SplashContract.onSplashListener mOnSplashListener, Context mContext)
    {
     this.mOnSplashListener = mOnSplashListener;
        this.mContext = mContext;
    }

    public void appSession(Activity mActivity){
                if (Util.loadBooleanPrefrence(Util.PREF_IS_LOGIN,mContext)) {
                    mOnSplashListener.onMainActivitySuccess("success");
                } else {
                    mOnSplashListener.onLoginActivitySuccess("success");
                }
    }
}
