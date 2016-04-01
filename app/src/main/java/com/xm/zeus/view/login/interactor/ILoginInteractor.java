package com.xm.zeus.view.login.interactor;


import android.content.Context;

import com.xm.zeus.network.extend.CancelSubscriber;

public interface ILoginInteractor {

    boolean checkUserName(String username);

    boolean checkPassword(String psw);

    boolean checkNet(Context context);

    void loginToBusiness(String username, String password, String org, String appKey, CancelSubscriber<Boolean> callback);

    void loginToXmpp(String username, String password);

}
