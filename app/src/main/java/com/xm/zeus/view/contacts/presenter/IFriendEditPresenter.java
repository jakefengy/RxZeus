package com.xm.zeus.view.contacts.presenter;

import com.xm.zeus.db.app.entity.Friend;

/**
 * 作者：小孩子xm on 2016-04-12 15:20
 * 邮箱：1065885952@qq.com
 */
public interface IFriendEditPresenter {

    // add friend
    void addFriend(Friend friend);

    // edit friend
    void getFriend(String friendId);

    void updateFriend(Friend friend);

    void onDestroy();
}
