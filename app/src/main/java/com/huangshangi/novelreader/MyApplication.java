package com.huangshangi.novelreader;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {


    static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;

    }


    public static MyApplication getApplication() {
        return application;
    }
}
