package com.xm.zeus.view.contacts.interactor;

import android.content.Context;

import com.xm.zeus.app.Constant;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.db.app.helper.FriendHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.utils.PinYin;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 作者：小孩子xm on 2016-04-12 13:27
 * 邮箱：1065885952@qq.com
 */
public class FriendInteractorImpl implements IFriendInteractor {

    private FriendHelper friendHelper;
    private PinYin pinYin;

    public FriendInteractorImpl() {
        friendHelper = new FriendHelper();
        pinYin = new PinYin();
    }

    @Override
    public void getFriends(Subscriber<List<Friend>> subscriber) {
        Observable.create(new Observable.OnSubscribe<List<Friend>>() {
            @Override
            public void call(Subscriber<? super List<Friend>> subscriber) {
                try {
                    subscriber.onNext(friendHelper.findAll());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void refreshFriends(final User user, final long friendTS, ApiSubscriber<List<Friend>> subscriber) {
        Network.getZeusApis().getFriends(user.getToken(), user.getUserId(), Constant.Organization, friendTS, "false")
                .subscribeOn(Schedulers.io())
                .compose(Network.<List<Friend>>check())
                .map(new Func1<List<Friend>, List<Friend>>() {
                    @Override
                    public List<Friend> call(List<Friend> friends) {
                        processFriend(friends);
                        return friendHelper.findAll();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private boolean processFriend(List<Friend> friends) {

        try {

            if (friends == null) {
                return true;
            }

            for (Friend friend : friends) {

                if (friend.getType() == 1) {
                    friendHelper.deleteById(friend.getUid());
                } else {
                    String selling = pinYin.toPinYin(friend.getUsername());
                    String firstLetter = selling.substring(0, 1).toUpperCase(Locale.getDefault());
                    friend.setSpelling(selling);
                    friend.setFirstLetter(firstLetter);
                    friend.setTimestamp(System.currentTimeMillis());
                    friendHelper.saveOrUpdate(friend);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
