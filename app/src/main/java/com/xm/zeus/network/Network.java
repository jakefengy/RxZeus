package com.xm.zeus.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xm.zeus.network.apis.ZeusApis;
import com.xm.zeus.network.entity.BaseEntity;
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

    public final static String baseUrl = "http://120.24.247.177:8080/open/";

    public static class ResultCode {
        public static final int RESULT_OK = 0;
        public static final int RESULT_PARAMS_ERROR = 1;
        public static final int RESULT_USER_NOT_EXIST = 2;
        public static final int RESULT_TOKEN_INVALID = 3; // Token 无效
        public static final int RESULT_TOKEN_EXPIRED = 4; // Token 过期
    }

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

    public static boolean isAvailable(Context ctx) {
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

    // 网络返回统一处理
    public static <T> Observable.Transformer<BaseEntity<T>, T> check() {
        return new Observable.Transformer<BaseEntity<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseEntity<T>> httpResultObservable) {
                return httpResultObservable.flatMap(new Func1<BaseEntity<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseEntity<T> result) {
                        if (result == null) {
                            return Observable.error(new Throwable("获取内容失败"));
                        } else {
                            int errorCode = result.getCode();
                            if (errorCode == ResultCode.RESULT_OK) {
                                return Observable.just(result.getBody());
                            } else {
                                return Observable.error(new ApiException(errorCode, result.getMessage()));
                            }
                        }
                    }
                });
            }
        };
    }

}
