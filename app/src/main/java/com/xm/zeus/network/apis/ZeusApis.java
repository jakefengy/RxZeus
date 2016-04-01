package com.xm.zeus.network.apis;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.Group;
import com.xm.zeus.db.app.entity.Org;
import com.xm.zeus.network.entity.HttpResult;
import com.xm.zeus.network.entity.LoginResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 业务请求接口
 */
public interface ZeusApis {

    // 登录
    @GET("login")
    Observable<HttpResult<LoginResult>> login(@Query("username") String username, @Query("password") String password, @Query("organization") String organization, @Query("appkey") String appkey);

    // 通讯录

    // 获取同事
    @GET("getbusinesscontacts")
    Observable<HttpResult<List<Colleague>>>
    getColleague(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org, @Query("timestamp") long timestamp, @Query("complete") String complete);

    // 获取好友
    @GET("getpersonalcontacts")
    Observable<HttpResult<List<Friend>>>
    getFriends(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org, @Query("timestamp") long timestamp, @Query("complete") String complete);

    // 获取群组
    @GET("getgrouplist")
    Observable<HttpResult<List<Group>>>
    getGroup(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org);

    // 获取组织树
    @GET("getorgtree")
    Observable<HttpResult<Org>>
    getOrgs(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org);


}
