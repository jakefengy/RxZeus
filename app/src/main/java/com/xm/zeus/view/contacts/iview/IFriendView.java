package com.xm.zeus.view.contacts.iview;

import com.xm.zeus.db.app.entity.Friend;

import java.util.List;

/**
 * 作者：小孩子xm on 2016-04-12 13:27
 * 邮箱：1065885952@qq.com
 */
public interface IFriendView {

    void onGetFriendComplete(List<Friend> friends);

    void onRefreshComplete(List<Friend> friends);

    void onRefreshFail();

    void error(String msg);

}
