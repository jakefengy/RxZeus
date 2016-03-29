package com.xm.zeus.view.login.presenter;

import android.content.Context;

import com.xm.zeus.Constant;
import com.xm.zeus.chat.ChatPresenter;
import com.xm.zeus.chat.listener.LoginListener;
import com.xm.zeus.db.app.dao.TimeStampDao;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.TimeStamp;
import com.xm.zeus.db.app.helper.TimeStampHelper;
import com.xm.zeus.db.user.entity.User;
import com.xm.zeus.network.extend.CancelSubscriber;
import com.xm.zeus.view.login.interactor.ISplashInteractor;
import com.xm.zeus.view.login.interactor.SplashInteractorImpl;
import com.xm.zeus.view.login.view.ISplashView;

import java.util.Date;

/**
 * Created by lvxia on 2016-03-28.
 */
public class SplashPresenterImpl implements ISplashPresenter {

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
        this.timeStampHelper = new TimeStampHelper(context);
    }

    @Override
    public void init() {

        // Start Anim

        interactor.initAppDB(context);
        interactor.initGallery();

        checkUserAndLogin();
    }

    private void checkUserAndLogin() {
        User user = interactor.getLastLoggedUser();
        if (user != null) {
            // To login
        }

        if (!interactor.checkNetwork(context)) {
            // To Home
        }

        loginToXmpp(user);

    }

    private void loginToXmpp(final User user) {

        chatPresenter.login(Constant.ServiceName, Constant.ServiceHost, Constant.ServicePort,
                user.getUserName(), user.getPassword(), Constant.PlatformResource, new LoginListener() {
                    @Override
                    public void onComplete() {
                        downloadContacts(user);
                    }

                    @Override
                    public void onError() {
                        // To login
                    }
                });

    }

    private void downloadContacts(User user) {

        long timeStamp = timeStampHelper.findByKey(TimeStamp.TS_COLLEAGUE, 0);

        interactor.downloadContacts(user, timeStamp, new CancelSubscriber<Colleague>() {
            @Override
            public void onEventNext(Colleague colleague) {

            }

            @Override
            public void onCompleted() {
                timeStampHelper.saveOrUpdate(TimeStamp.TS_COLLEAGUE, new Date().getTime());

                // To home

            }
        });
    }

    @Override
    public void onDestroy() {
        iview = null;
    }
}
