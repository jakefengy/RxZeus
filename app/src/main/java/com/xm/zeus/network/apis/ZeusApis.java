package com.xm.zeus.network.apis;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.Group;
import com.xm.zeus.db.app.entity.Org;
import com.xm.zeus.network.entity.BaseEntity;
import com.xm.zeus.view.contacts.entity.AddFriendResult;
import com.xm.zeus.view.contacts.entity.DeleteFriendResult;
import com.xm.zeus.view.contacts.entity.UpdateFriendResult;
import com.xm.zeus.view.login.entity.LoginResult;
import com.xm.zeus.view.home.entity.CheckVersionResult;
import com.xm.zeus.view.home.entity.LoginOutResult;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 业务请求接口
 */
public interface ZeusApis {

    // 登录
    @GET("login")
    Observable<BaseEntity<LoginResult>> login(@Query("username") String username, @Query("password") String password, @Query("organization") String organization, @Query("appkey") String appkey);

    // 登出
    @GET("logout")
    Observable<BaseEntity<LoginOutResult>> loginOut(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org, @Query("appkey") String appkey);

    // 获取软件更新信息
    @GET("checkversion")
    Observable<BaseEntity<CheckVersionResult>> checkVersion(@Query("platform") String platform, @Query("version") String version);

    // 通讯录

    // 获取同事
    @GET("getbusinesscontacts")
    Observable<BaseEntity<List<Colleague>>>
    getColleague(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org, @Query("timestamp") long timestamp, @Query("complete") String complete);

    // 获取好友
    @GET("getpersonalcontacts")
    Observable<BaseEntity<List<Friend>>>
    getFriends(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org, @Query("timestamp") long timestamp, @Query("complete") String complete);

    // 获取群组
    @GET("getgrouplist")
    Observable<BaseEntity<List<Group>>>
    getGroup(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org);

    // 获取组织树
    @GET("getorgtree")
    Observable<BaseEntity<Org>>
    getOrgs(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org);

    // 好友相关

    // 添加好友
    @Multipart
    @POST
    Observable<BaseEntity<AddFriendResult>> addFriend(
            @Url String url,
            @Query("AccessToken") String token,
            @Query("organization") String organization,
            @Query("uid") String uid,
            @Part("data") RequestBody params);

    // 更新好友
    @Multipart
    @POST
    Observable<BaseEntity<UpdateFriendResult>> updateFriend(
            @Url String url,
            @Query("AccessToken") String token,
            @Query("organization") String organization,
            @Query("uid") String uid,
            @Part("data") RequestBody params);

    // 删除好友
    @GET("removecontact")
    Observable<BaseEntity<DeleteFriendResult>>
    deleteFriend(@Query("AccessToken") String token, @Query("uid") String uid, @Query("organization") String org, @Query("contactid") String contactid);

}
