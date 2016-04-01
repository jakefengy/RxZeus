package com.xm.zeus.view.login.interactor;

import android.content.Context;
import android.text.TextUtils;

import com.hp.hpl.sparta.xpath.BooleanExpr;
import com.xm.zeus.network.Network;
import com.xm.zeus.network.entity.LoginResult;
import com.xm.zeus.network.extend.CancelSubscriber;
import com.xm.zeus.network.extend.MapFunc1;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lvxia on 2016-04-01.
 */
public class LoginInteractorImpl implements ILoginInteractor {

    @Override
    public boolean checkUserName(String username) {
        return TextUtils.isEmpty(username);
    }

    @Override
    public boolean checkPassword(String psw) {
        return TextUtils.isEmpty(psw);
    }

    @Override
    public boolean checkNet(Context ctx) {
        return Network.isNetworkAvailable(ctx);
    }

    @Override
    public void loginToBusiness(String username, String password, String org, String appKey, CancelSubscriber<Boolean> callback) {
        Network.getZeusApis().login(username, password, org, appKey)
                .subscribeOn(Schedulers.io())
                .map(new MapFunc1<LoginResult>())
                .map(new Func1<LoginResult, Boolean>() {
                    @Override
                    public Boolean call(LoginResult loginResult) {
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);

    }

    @Override
    public void loginToXmpp(String username, String password) {

    }
}
