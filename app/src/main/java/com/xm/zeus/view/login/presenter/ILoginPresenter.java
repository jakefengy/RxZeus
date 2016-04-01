package com.xm.zeus.view.login.presenter;


public interface ILoginPresenter {

    void login(String username, String psw, String org, String appKey);

    void downloadContacts();

    void onDestroy();

}
