package com.xm.zeus.view.login.interactor;

import android.content.Context;


import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.network.extend.ApiSubscriber;

/**
 * Created by lvxia on 2016-03-28.
 */
public interface ISplashInteractor {

    void initGallery();

    User getLastLoggedUser();

    boolean checkNetwork(Context context);

    void downloadContacts(User user, long colleagueTS, long friendTS, ApiSubscriber subscriber);

}
