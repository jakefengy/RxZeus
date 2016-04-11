package com.xm.zeus.view.login.presenter;

import android.content.Context;

import com.xm.zeus.db.app.entity.TimeStamp;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.db.app.helper.TimeStampHelper;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.utils.Logger;
import com.xm.zeus.view.login.interactor.ILoginInteractor;
import com.xm.zeus.view.login.interactor.LoginInteractorImpl;
import com.xm.zeus.view.login.iview.ILoginView;

import java.util.Date;

public class LoginPresenterImpl implements ILoginPresenter {

    private final static String TAG = LoginPresenterImpl.class.getName();

    private ILoginView loginView;
    private LoginInteractorImpl interactor;
    private Context loginContext;

    private TimeStampHelper timeStampHelper;

    public LoginPresenterImpl(Context ctx, final ILoginView loginView) {
        this.loginContext = ctx;
        this.loginView = loginView;
        this.interactor = new LoginInteractorImpl();
        timeStampHelper = new TimeStampHelper();

    }

    @Override
    public void login(String username, String psw, String org, String appKey) {
        if (interactor.checkUserName(username)) {
            toError("用户名有问题");
            return;
        }
        if (interactor.checkPassword(psw)) {
            toError("密码有问题");
            return;
        }
        if (!interactor.checkNet(loginContext)) {
            toError("网络有问题");
            return;
        }
        interactor.loginToBusiness(username, psw, org, appKey, new ApiSubscriber<User>() {
            @Override
            public void onNext(User user) {
                loginToXmpp(user);
            }

            @Override
            protected void onCommonError(Throwable e) {
                toError(e.toString());
            }

        });
    }

    private void loginToXmpp(final User user) {
        interactor.loginToXmpp(user.getUserId(), user.getPassword(), new ILoginInteractor.LoginListener() {
            @Override
            public void onSuccess() {
                downloadContacts(user);
            }

            @Override
            public void onFail(Throwable e) {
                toError(e.toString());
            }
        });
    }

    @Override
    public void downloadContacts(User user) {
        Logger.i(TAG, "downloadContacts.start");

        long colleagueTS = timeStampHelper.findByKey(TimeStamp.TS_COLLEAGUE, 0);
        long friendTS = timeStampHelper.findByKey(TimeStamp.TS_FRIEND, 0);

        interactor.downloadContacts(user, colleagueTS, friendTS, new ApiSubscriber<Boolean>() {
            @Override
            public void onNext(Boolean isDown) {
            }

            @Override
            public void onCommonError(Throwable e) {
                Logger.i(TAG, "downloadContacts.error");
                if (loginView != null) {
                    loginView.toHome();
                }
            }

            @Override
            public void onCompleted() {
                Logger.i(TAG, "downloadContacts.completed");
                timeStampHelper.saveOrUpdate(TimeStamp.TS_COLLEAGUE, new Date().getTime());
                timeStampHelper.saveOrUpdate(TimeStamp.TS_FRIEND, new Date().getTime());

                // To home
                if (loginView != null) {
                    loginView.toHome();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    private void toError(String msg) {
        if (loginView != null)
            loginView.error(msg);
    }

}
