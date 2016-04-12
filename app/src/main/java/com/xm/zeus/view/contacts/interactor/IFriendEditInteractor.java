package com.xm.zeus.view.contacts.interactor;

import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.network.extend.ApiSubscriber;

import rx.Subscriber;

/**
 * 作者：小孩子xm on 2016-04-12 15:23
 * 邮箱：1065885952@qq.com
 */
public interface IFriendEditInteractor {

    // add friend
    void addFriend(User user, Friend friend, ApiSubscriber<Friend> subscriber);

    // edit friend
    void getFriend(String friendId, Subscriber<Friend> subscriber);

    void updateFriend(User user, Friend friend, ApiSubscriber<Friend> subscriber);

}
