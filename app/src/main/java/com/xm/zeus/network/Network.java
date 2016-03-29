package com.xm.zeus.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xm.zeus.network.apis.ZeusApis;
import com.xm.zeus.network.entity.HttpResult;
import com.xm.zeus.network.extend.ApiException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by lvxia on 2016-03-22.
 */
public class Network {

    private String baseUrl = "http://120.24.247.177:8080/open/";

    private Retrofit retrofit;

    private ZeusApis zeusApis;

    private Network() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

                .build();

        zeusApis = retrofit.create(ZeusApis.class);
    }

    private static class SingletonHolder {
        private static final Network INSTANCE = new Network();
    }

    //获取单例
    public static ZeusApis getZeusApis() {
        return SingletonHolder.INSTANCE.zeusApis;
    }

    // network available

    public static boolean isNetworkAvailable(Context ctx) {
        Context appContext = ctx.getApplicationContext();
        ConnectivityManager mgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mgr != null) {
            NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isConnected();
            }
        }
        return false;
    }

}
