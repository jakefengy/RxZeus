package com.xm.zeus.chat;

import android.support.annotation.NonNull;

import com.xm.zeus.BuildConfig;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 聊天相关方法实现
 */
public class XmppHelper {

    public void login(final @NonNull String serviceName, final @NonNull String serviceHost, final @NonNull int servicePort,
                      final @NonNull String userId, final @NonNull String password, final @NonNull String source,
                      @NonNull Subscriber<AbstractXMPPConnection> callback) {

        Observable
                .create(new Observable.OnSubscribe<AbstractXMPPConnection>() {
                    @Override
                    public void call(Subscriber<? super AbstractXMPPConnection> subscriber) {
                        try {
                            subscriber.onNext(login(serviceName, serviceHost, servicePort, userId, password, source));
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);

    }

    private AbstractXMPPConnection login(@NonNull String serviceName, @NonNull String serviceHost,
                                         @NonNull int servicePort, @NonNull String userId,
                                         @NonNull String password, @NonNull String source) throws Exception {

        DomainBareJid jid = (DomainBareJid) JidCreate.from(serviceName);

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(userId + "@" + serviceHost, password)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setXmppDomain(jid)
                .setHost(serviceHost)
                .setPort(servicePort)
                .setResource(source)
                .setSendPresence(true)
                .setDebuggerEnabled(BuildConfig.DEBUG);

        AbstractXMPPConnection xmppConnection = new XMPPTCPConnection(config.build());
        xmppConnection.connect();
        xmppConnection.login();

        return xmppConnection;

    }

}
