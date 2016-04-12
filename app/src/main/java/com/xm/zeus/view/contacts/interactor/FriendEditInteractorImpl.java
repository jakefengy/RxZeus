package com.xm.zeus.view.contacts.interactor;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.db.app.helper.FriendHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.view.contacts.entity.AddFriendResult;
import com.xm.zeus.view.contacts.entity.DeleteFriendResult;
import com.xm.zeus.view.contacts.entity.UpdateFriendResult;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 作者：小孩子xm on 2016-04-12 15:27
 * 邮箱：1065885952@qq.com
 */
public class FriendEditInteractorImpl implements IFriendEditInteractor {

    private FriendHelper friendHelper;

    public FriendEditInteractorImpl() {
        friendHelper = new FriendHelper();
    }

    @Override
    public void addFriend(final User user, final Friend friend, ApiSubscriber<Friend> subscriber) {
        Observable.create(new Observable.OnSubscribe<Friend>() {
            @Override
            public void call(Subscriber<? super Friend> subscriber) {
                RequestBody body = RequestBody.create(MediaType.parse("data"), new Gson().toJson(friend));
                Network.getZeusApis().addFriend(Network.baseUrl + "addnewcontact", user.getToken(), user.getOrg(), user.getUserId(), body)
                        .subscribeOn(Schedulers.io())
                        .compose(Network.<AddFriendResult>check())
                        .subscribeOn(Schedulers.io())
                        .flatMap(new Func1<AddFriendResult, Observable<Friend>>() {
                            @Override
                            public Observable<Friend> call(AddFriendResult result) {
                                if (result == null || TextUtils.isEmpty(result.getContactid())) {
                                    return Observable.error(new Throwable("添加失败"));
                                }

                                try {
                                    friend.setUid(result.getContactid());
                                    friendHelper.saveOrUpdate(friend);
                                    return Observable.just(friend);
                                } catch (Exception e) {
                                    friendHelper.deleteById(result.getContactid());
                                    return Observable.error(new Throwable("添加失败"));
                                }

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void getFriend(String friendId, Subscriber<Friend> subscriber) {
        Observable.just(friendId)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<Friend>>() {
                    @Override
                    public Observable<Friend> call(String s) {
                        if (TextUtils.isEmpty(s)) {
                            return Observable.error(new Throwable("friendId为空"));
                        } else {
                            Friend friend = friendHelper.findById(s);
                            if (friend == null) {
                                return Observable.error(new Throwable("好友不存在"));
                            } else {
                                return Observable.just(friend);
                            }
                        }

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void updateFriend(User user, final Friend friend, ApiSubscriber<Friend> subscriber) {
        RequestBody body = RequestBody.create(MediaType.parse("data"), new Gson().toJson(friend));
        Network.getZeusApis().updateFriend(Network.baseUrl + "updatepersonalcontact", user.getToken(), user.getOrg(), user.getUserId(), body)
                .subscribeOn(Schedulers.io())
                .compose(Network.<UpdateFriendResult>check())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<UpdateFriendResult, Observable<Friend>>() {
                    @Override
                    public Observable<Friend> call(UpdateFriendResult result) {
                        if (result == null || !result.getIsok()) {
                            return Observable.error(new Throwable("好友更新失败"));
                        }

                        friendHelper.saveOrUpdate(friend);

                        return Observable.just(friendHelper.findById(friend.getUid()));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void deleteFriend(User user, final String friendId, ApiSubscriber<String> subscriber) {
        Network.getZeusApis().deleteFriend(user.getToken(), user.getUserId(), user.getOrg(), friendId)
                .subscribeOn(Schedulers.io())
                .compose(Network.<DeleteFriendResult>check())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<DeleteFriendResult, Observable<String>>() {
                    @Override
                    public Observable<String> call(DeleteFriendResult result) {
                        if (result == null || !result.getIsok()) {
                            return Observable.error(new Throwable("好友删除失败"));
                        }

                        friendHelper.deleteById(friendId);
                        return Observable.just(friendId);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
