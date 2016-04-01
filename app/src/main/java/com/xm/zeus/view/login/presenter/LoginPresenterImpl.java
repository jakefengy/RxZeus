package com.xm.zeus.view.login.presenter;

import com.xm.zeus.network.extend.CancelSubscriber;
import com.xm.zeus.view.login.interactor.LoginInteractorImpl;
import com.xm.zeus.view.login.view.ILoginView;

public class LoginPresenterImpl implements ILoginPresenter {

    private ILoginView loginView;
    private LoginInteractorImpl interactor;

    private CancelSubscriber<Boolean> busSubscriber, xmppSubscriber;

    public LoginPresenterImpl(ILoginView loginView) {
        this.loginView = loginView;
        this.interactor = new LoginInteractorImpl();

        busSubscriber = new CancelSubscriber<Boolean>() {
            @Override
            public void onEventNext(Boolean aBoolean) {

            }

            @Override
            public void onCompleted() {

            }
        };


    }

    @Override
    public void login(String username, String psw, String org, String appKey) {
        interactor.checkUserName(username);
        interactor.checkPassword(psw);

        interactor.loginToBusiness(username, psw, org, appKey, busSubscriber);
    }

    private void loginToXmpp(String uid, String psw) {
        interactor.loginToXmpp(uid, psw);
    }

    @Override
    public void downloadContacts() {

    }

    @Override
    public void onDestroy() {
        loginView = null;
    }
}
