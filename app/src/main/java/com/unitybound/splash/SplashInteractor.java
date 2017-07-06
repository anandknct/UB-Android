package com.unitybound.splash;

import android.app.Activity;

import com.unitybound.utility.AppSession;


/**
 * Created by charchitkasliwal on 09/04/17.
 */

public class SplashInteractor {

    private SplashContract.onSplashListener mOnSplashListener;


    public SplashInteractor(SplashContract.onSplashListener mOnSplashListener)
    {
     this.mOnSplashListener = mOnSplashListener;

    }

    public void appSession(Activity mActivity){
        AppSession appSession = new AppSession(mActivity);
                if (!appSession.getUserEmailId().equals("") && !appSession.getUserPassword().equals("")) {
                    mOnSplashListener.onMainActivitySuccess("success");
                } else {
                    mOnSplashListener.onLoginActivitySuccess("success");
                }
    }



}
