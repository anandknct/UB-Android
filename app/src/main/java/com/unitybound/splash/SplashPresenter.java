package com.unitybound.splash;

import android.app.Activity;

/**
 * Created by charchitkasliwal on 09/04/17.
 */

public class SplashPresenter implements SplashContract.Presenter, SplashContract.onSplashListener{

    public Activity mActivity;
    private SplashContract.view mSplashView;
    private  SplashInteractor mSplashInteractor;

    public SplashPresenter(SplashContract.view mSplashView) {
     this.mSplashView = mSplashView;
        this.mSplashInteractor = new SplashInteractor(this);

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
