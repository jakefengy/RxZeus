package com.xm.zeus.view.login.presenter;

import android.content.Context;

import com.xm.zeus.app.Constant;
import com.xm.zeus.chat.Xmpp;
import com.xm.zeus.db.app.entity.TimeStamp;
import com.xm.zeus.db.app.entity.User;
import com.xm.zeus.db.app.helper.TimeStampHelper;
import com.xm.zeus.network.extend.ApiException;
import com.xm.zeus.network.extend.ApiSubscriber;
import com.xm.zeus.utils.Logger;
import com.xm.zeus.view.login.interactor.ISplashInteractor;
import com.xm.zeus.view.login.interactor.SplashInteractorImpl;
import com.xm.zeus.view.login.iview.ISplashView;

import org.jivesoftware.smack.AbstractXMPPConnection;

import java.util.Date;

import rx.Subscriber;

/**
 * Created by lvxia on 2016-03-28.
 */
public class SplashPresenterImpl implements ISplashPresenter {

    private static final String TAG = "SplashPresenterTag";

    private Context context;

    private ISplashView iview;
    private ISplashInteractor interactor;
    private Xmpp xmpp;

    private TimeStampHelper timeStampHelper;

    public SplashPresenterImpl(Context context, ISplashView iview) {
        this.context = context;
        this.iview = iview;
        this.interactor = new SplashInteractorImpl();
        this.xmpp = Xmpp.getInstance();
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
        if (user == null || !user.getLogged() || !user.getAutoLogin()) {
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
        xmpp.login(Constant.ServiceName, Constant.ServiceHost, Constant.ServicePort,
                user.getUserId(), user.getPassword(), Constant.PlatformResource, new Subscriber<AbstractXMPPConnection>() {
                    @Override
                    public void onCompleted() {
                        downloadContacts(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (iview != null) {
                            iview.toLogin();
                        }
                    }

                    @Override
                    public void onNext(AbstractXMPPConnection connection) {
                        xmpp.setXmppConnection(connection);
                    }
                });

    }

    private void downloadContacts(User user) {

        Logger.i(TAG, "downloadContacts.start");

        long colleagueTS = timeStampHelper.findByKey(TimeStamp.TS_COLLEAGUE, 0);
        long friendTS = timeStampHelper.findByKey(TimeStamp.TS_FRIEND, 0);


        interactor.downloadContacts(user, colleagueTS, friendTS, new ApiSubscriber<Boolean>() {
            @Override
            public void onNext(Boolean isDown) {
            }

            @Override
            public void onCommonError(Throwable e) {
                Logger.i(TAG, "downloadContacts.onCommonError");
                if (iview != null) {
                    iview.toHome();
                }
            }

            @Override
            public void onCompleted() {
                Logger.i(TAG, "downloadContacts.completed");
                timeStampHelper.saveOrUpdate(TimeStamp.TS_COLLEAGUE, new Date().getTime());
                timeStampHelper.saveOrUpdate(TimeStamp.TS_FRIEND, new Date().getTime());

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
