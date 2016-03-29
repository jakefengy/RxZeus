package com.xm.zeus.utils;

import android.util.Log;

import com.xm.zeus.BuildConfig;

/**
 * Created by lvxia on 2016-03-29.
 */
public class Logger {

    public static void i(String tag, String msg) {
        if (!isDebug()) {
            return;
        }

        Log.i(tag, msg);
    }

    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

}
