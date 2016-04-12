package com.xm.zeus.view.contacts.iview;

import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.Friend;

/**
 * 作者：小孩子xm on 2016-04-12 20:55
 * 邮箱：1065885952@qq.com
 */
public interface IPersonView {

    void onGetFriendComplete(Friend friend);

    void onDeleteFriendComplete();

    void onGetColleagueComplete(Colleague colleague);

    void onError(String msg);

}
