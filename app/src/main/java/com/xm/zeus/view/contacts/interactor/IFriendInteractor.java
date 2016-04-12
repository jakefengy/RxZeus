package com.xm.zeus.view.contacts.interactor;

import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.network.extend.ApiSubscriber;

import java.util.List;

import rx.Subscriber;

/**
 * 作者：小孩子xm on 2016-04-12 13:26
 * 邮箱：1065885952@qq.com
 */
public interface IFriendInteractor {

    void getFriends(Subscriber<List<Friend>> subscriber);

    void refreshFriends(User user, long friendTS, ApiSubscriber<List<Friend>> subscriber);

}
