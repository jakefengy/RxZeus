package com.xm.zeus.view.login.presenter;


import com.xm.zeus.db.user.entity.User;

public interface ILoginPresenter {

    void login(String username, String psw, String org, String appKey);

    void downloadContacts(User user);

    void onDestroy();

}
