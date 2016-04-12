package com.xm.zeus.view.contacts.interactor;

import android.text.TextUtils;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.db.app.helper.ColleagueHelper;
import com.xm.zeus.db.app.helper.FriendHelper;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.view.contacts.entity.DeleteFriendResult;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 作者：小孩子xm on 2016-04-12 20:55
 * 邮箱：1065885952@qq.com
 */
public class PersonInteractorImpl implements IPersonInteractor {

    private FriendHelper friendHelper;
    private ColleagueHelper colleagueHelper;

    public PersonInteractorImpl() {
        friendHelper = new FriendHelper();
        colleagueHelper = new ColleagueHelper();
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

    @Override
    public void getColleague(final String colleagueId, Subscriber<Colleague> subscriber) {
        Observable.just(colleagueId)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<Colleague>>() {
                    @Override
                    public Observable<Colleague> call(String s) {
                        if (TextUtils.isEmpty(s)) {
                            return Observable.error(new Throwable("colleagueId为空"));
                        } else {
                            Colleague friend = colleagueHelper.findById(s);
                            if (friend == null) {
                                return Observable.error(new Throwable("同事不存在"));
                            } else {
                                return Observable.just(friend);
                            }
                        }

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
