package com.xm.zeus.view.contacts.presenter;

/**
 * 作者：小孩子xm on 2016-04-12 20:50
 * 邮箱：1065885952@qq.com
 */
public interface IPersonPresenter {

    // Friend

    void getFriend(String friendId);

    void deleteFriend(String friendId);

    // Colleague

    void getColleague(String colleagueId);

    void onDestroy();

}
