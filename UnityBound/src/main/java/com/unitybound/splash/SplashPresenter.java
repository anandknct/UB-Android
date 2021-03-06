package com.unitybound.splash;

import android.app.Activity;
import android.content.Context;

/**
 * Created by charchitkasliwal on 09/04/17.
 */

public class SplashPresenter implements SplashContract.Presenter, SplashContract.onSplashListener{

    private final Context mContext;
    public Activity mActivity;
    private SplashContract.view mSplashView;
    private  SplashInteractor mSplashInteractor;

    public SplashPresenter(SplashContract.view mSplashView, Context splashActivity) {
     this.mSplashView = mSplashView;
     this.mContext = splashActivity;
        this.mSplashInteractor = new SplashInteractor(this,mContext);

    }

    @Override
    public void checkAppSession(Activity activity) {
        mSplashInteractor.appSession(activity);

    }

    @Override
    public void onMainActivitySuccess(String message) {
        mSplashView.onMainActivitySuccess();

    }

    @Override
    public void onLoginActivitySuccess(String message) {

        mSplashView.onLoginActivitySuccess();

    }
}
