package com.xm.zeus.view.login.interactor;

import android.content.Context;

import com.xm.zeus.db.user.entity.User;
import com.xm.zeus.network.extend.CancelSubscriber;

/**
 * Created by lvxia on 2016-03-28.
 */
public interface ISplashInteractor {

    interface InitListener {
        void onStart();

        void onComplete();

        void onFail();
    }

    void initAppDB(Context context);

    void initGallery();

    User getLastLoggedUser();

    boolean checkNetwork(Context context);

    void downloadContacts(User user, long timestamp, CancelSubscriber subscriber);

}
