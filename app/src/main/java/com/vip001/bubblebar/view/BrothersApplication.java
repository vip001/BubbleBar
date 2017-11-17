package com.vip001.bubblebar.view;

import android.app.Application;

/**
 * Created by vip001 on 2017/11/1.
 */

public class BrothersApplication extends Application {
    private static BrothersApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static Application getApplicationInstance() {
        return mApplication;
    }
}
