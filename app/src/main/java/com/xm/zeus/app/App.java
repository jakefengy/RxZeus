package com.xm.zeus.app;

import android.app.Application;

import com.xm.zeus.db.AppDbHelper;

/**
 * Created by lvxia on 2016-04-01.
 */
public class App extends Application {

    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppDbHelper.getInstance().init(this);
    }
}
