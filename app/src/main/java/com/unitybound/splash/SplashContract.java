package com.unitybound.splash;

import android.app.Activity;

/**
 * Created by charchitkasliwal on 09/04/17.
 */

public class SplashContract {

    /**
     * the view success
     */
    interface view{
         void onMainActivitySuccess();

         void onLoginActivitySuccess();
    }

    interface Interactor{
        void appSession();
    }


    interface Presenter{
        void checkAppSession(Activity activity);
    }

    interface  onSplashListener{
        void onMainActivitySuccess(String message);

        void onLoginActivitySuccess(String message);
    }
}
