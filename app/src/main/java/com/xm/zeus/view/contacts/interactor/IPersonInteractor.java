package com.xm.zeus.view.contacts.interactor;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.network.extend.ApiSubscriber;

import rx.Subscriber;

/**
 * 作者：小孩子xm on 2016-04-12 20:53
 * 邮箱：1065885952@qq.com
 */
public interface IPersonInteractor {

    // Friend

    void getFriend(String friendId, Subscriber<Friend> subscriber);

    void deleteFriend(User user, String friendId, ApiSubscriber<String> subscriber);

    // Colleague

    void getColleague(String colleagueId, Subscriber<Colleague> subscriber);

}
