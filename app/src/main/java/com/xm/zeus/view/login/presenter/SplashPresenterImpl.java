package com.xm.zeus.view.login.presenter;

import android.content.Context;

import com.xm.zeus.app.Constant;
import com.xm.zeus.chat.ChatPresenter;
import com.xm.zeus.chat.listener.LoginListener;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.TimeStamp;
import com.xm.zeus.db.app.helper.TimeStampHelper;
import com.xm.zeus.db.user.entity.User;
import com.xm.zeus.network.extend.CancelSubscriber;
import com.xm.zeus.utils.Logger;
import com.xm.zeus.view.login.interactor.ISplashInteractor;
import com.xm.zeus.view.login.interactor.SplashInteractorImpl;
import com.xm.zeus.view.login.view.ISplashView;

import java.util.Date;

/**
 * Created by lvxia on 2016-03-28.
 */
public class SplashPresenterImpl implements ISplashPresenter {

    private static final String TAG = "SplashPresenterTag";

    private Context context;

    private ISplashView iview;
    private ISplashInteractor interactor;
    private ChatPresenter chatPresenter;

    private TimeStampHelper timeStampHelper;

    public SplashPresenterImpl(Context context, ISplashView iview) {
        this.context = context;
        this.iview = iview;
        this.interactor = new SplashInteractorImpl();
        this.chatPresenter = ChatPresenter.getInstance();
        this.timeStampHelper = new TimeStampHelper();
    }

    @Override
    public void init() {

        // Start Anim
        if (iview != null) {
            iview.initStart();
        }

        interactor.initGallery();

        checkUserAndLogin();
    }

    private void checkUserAndLogin() {
        Logger.i(TAG, "checkUserAndLogin");
        User user = interactor.getLastLoggedUser();
        if (user == null || !user.getLogged()) {
            // To login
            if (iview != null) {
                iview.toLogin();
            }
            return;
        }

        if (!interactor.checkNetwork(context)) {
            // To Home
            if (iview != null) {
                iview.toHome();
            }

            return;
        }

        loginToXmpp(user);

    }

    private void loginToXmpp(final User user) {

        Logger.i(TAG, "loginToXmpp");
        chatPresenter.login(Constant.ServiceName, Constant.ServiceHost, Constant.ServicePort,
                user.getUserName(), user.getPassword(), Constant.PlatformResource, new LoginListener() {
                    @Override
                    public void onComplete() {
                        downloadContacts(user);
                    }

                    @Override
                    public void onError() {
                        // To login
                        if (iview != null) {
                            iview.toLogin();
                        }
                    }
                });

    }

    private void downloadContacts(User user) {

        Logger.i(TAG, "downloadContacts.start");

        long colleagueTS = timeStampHelper.findByKey(TimeStamp.TS_COLLEAGUE, 0);
        long friendTS = timeStampHelper.findByKey(TimeStamp.TS_FRIEND, 0);

        interactor.downloadContacts(user, colleagueTS, new CancelSubscriber<Colleague>() {
            @Override
            public void onEventNext(Colleague colleague) {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Logger.i(TAG, "downloadContacts.error");
                if (iview != null) {
                    iview.toHome();
                }
            }

            @Override
            public void onCompleted() {
                Logger.i(TAG, "downloadContacts.completed");
                timeStampHelper.saveOrUpdate(TimeStamp.TS_COLLEAGUE, new Date().getTime());

                // To home
                if (iview != null) {
                    iview.toHome();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        iview = null;
    }
}
