package com.xm.zeus.chat;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xm.zeus.chat.listener.LoginListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.util.Objects;

import rx.Observable;
import rx.Subscriber;

/**
 * 聊天控制器
 */
public class ChatPresenter {

    private ChatPresenter() {
        interactor = new ChatInteractor();
    }

    private static class SingletonHolder {
        private static final ChatPresenter INSTANCE = new ChatPresenter();
    }

    //获取单例
    public static ChatPresenter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ChatInteractor interactor;

    private AbstractXMPPConnection xmppConnection;
    private ReconnectionManager reconnectionManager;

    private String serviceName;
    private String serviceHost;
    private int servicePort;

    /**
     * 检查xmppConnection是否可用
     *
     * @return true isAvailable false unAvailable
     */
    private boolean isConnectionAvailable() {
        return xmppConnection != null && xmppConnection.isConnected() && xmppConnection.isAuthenticated();
    }

    public void login(@NonNull String serviceName, @NonNull String serviceHost, @NonNull int servicePort,
                      @NonNull String userName, @NonNull String password, @NonNull String source, final LoginListener listener) {

        setServiceHost(serviceHost);
        setServiceName(serviceName);
        setServicePort(servicePort);

        interactor.login(serviceName, serviceHost, servicePort, userName, password, source, new Subscriber<AbstractXMPPConnection>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null)
                    listener.onError();
            }

            @Override
            public void onNext(AbstractXMPPConnection connection) {
                setXmppConnection(connection);
                if (listener != null)
                    listener.onComplete();
            }
        });

    }

    // get set

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public AbstractXMPPConnection getXmppConnection() {
        return xmppConnection;
    }

    public void setXmppConnection(AbstractXMPPConnection xmppConnection) {
        this.xmppConnection = xmppConnection;
    }
}
