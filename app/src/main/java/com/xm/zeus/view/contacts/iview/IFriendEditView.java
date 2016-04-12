package com.xm.zeus.view.contacts.iview;

import com.xm.zeus.db.app.entity.Friend;

/**
 * 作者：小孩子xm on 2016-04-12 15:23
 * 邮箱：1065885952@qq.com
 */
public interface IFriendEditView {

    void onGetComplete(Friend friend);

    void onAddComplete(Friend friend);

    void onUpdateComplete(Friend friend);

    void onDeleteComplete(String friendId);

    void error(String msg);

}
