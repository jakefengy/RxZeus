package com.xm.zeus.view.login.interactor;


import android.content.Context;

import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.network.extend.ApiSubscriber;

public interface ILoginInteractor {

    interface LoginListener {
        void onSuccess();

        void onFail(Throwable e);
    }

    boolean checkUserName(String username);

    boolean checkPassword(String psw);

    boolean checkNet(Context context);

    void loginToBusiness(String username, String password, String org, String appKey, ApiSubscriber<User> callback);

    void loginToXmpp(String username, String password, LoginListener listener);

    void downloadContacts(User user, long colleagueTS, long friendTS, ApiSubscriber subscriber);
}
