package com.unitybound.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;

import com.unitybound.R;
import com.unitybound.login.SignInActivity;
import com.unitybound.main.BaseActivity;
import com.unitybound.main.MainActivity;

/**
 * Created by charchitkasliwal on 08/05/17.
 */

public class SplashActivity extends BaseActivity implements SplashContract.view{

    /**
     * The splash time out.
     */
    private static int SPLASH_TIME_OUT = 3000;
    private SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bindClasses();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSplashPresenter.checkAppSession(SplashActivity.this);
            }
        }, SPLASH_TIME_OUT);
    }

    /**
     * Bind All the Classes
     */
    private void bindClasses() {
        mSplashPresenter = new SplashPresenter(SplashActivity.this);

    }

    @Override
    public void onMainActivitySuccess() {
        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);// LoginActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginActivitySuccess() {
        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);// LoginActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
}