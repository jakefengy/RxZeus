package com.xm.zeus.chat;

import android.support.annotation.NonNull;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ReconnectionManager;

import rx.Subscriber;

/**
 * 聊天控制器
 */
public class Xmpp {

    private Xmpp() {
        xmppHelper = new XmppHelper();
    }

    private static class SingletonHolder {
        private static final Xmpp INSTANCE = new Xmpp();
    }

    //获取单例
    public static Xmpp getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private XmppHelper xmppHelper;

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
                      @NonNull String userId, @NonNull String password, @NonNull String source, Subscriber<AbstractXMPPConnection> subscriber) {

        setServiceHost(serviceHost);
        setServiceName(serviceName);
        setServicePort(servicePort);

        xmppHelper.login(serviceName, serviceHost, servicePort, userId, password, source, subscriber);

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
