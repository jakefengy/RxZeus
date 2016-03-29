package com.xm.zeus.network.apis;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.network.entity.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 业务请求接口
 */
public interface ZeusApis {

    @GET("getbusinesscontacts")
    Observable<HttpResult<List<Colleague>>>
    getColleague(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org, @Query("timestamp") long timestamp, @Query("complete") String complete);

}
