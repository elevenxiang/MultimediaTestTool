package com.htc.eleven.multimediatesttool;

import android.app.Application;
import android.util.Log;

/**
 * Created by eleven on 17-8-21.
 */

public class App extends Application {

    private static final String TAG = "eleven-App";
    private static App mApp;

    private final String testResultFile = "results.xml";

    public String getTestResultFile() {
        return testResultFile;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "App Created !");

        mApp = (App) getApplicationContext();
    }

    public static App getApp() {
        return mApp;
    }
}
