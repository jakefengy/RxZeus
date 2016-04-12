package com.xm.zeus.app;

import android.os.Environment;

/**
 * Created by lvxia on 2016-03-29.
 */
public class Constant {

    //阿里云
    public static final String RequestUrl = "http://120.24.247.177:8080/open/";
    public static final String ImageUrl = "http://120.24.247.177:8081/cooa/";
    public static final String ServiceName = "120.24.247.177";
    public static final String ServiceHost = "120.24.247.177";
    public static final int ServicePort = 5222;

    public static final String Organization = "1";
    public static final String Platform = "1";
    public static final String PlatformResource = "wewe_mobile";


    /**
     * 应用文件结构SD卡
     * --Zeus
     * ----Image
     * ----Network
     * ----Other
     */

    private static final String sd = getAvailableCachePath();
    public static final String ImageCache = sd + "Zeus/Image/";

    private static String getAvailableCachePath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            return App.instance.getExternalCacheDir().getPath() + "/Zeus/";
        } else {
            return App.instance.getCacheDir().getPath() + "/Zeus/";
        }

    }

}
