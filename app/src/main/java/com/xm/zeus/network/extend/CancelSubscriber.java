package com.xm.zeus.network.extend;

import android.content.Intent;

import com.xm.zeus.app.App;
import com.xm.zeus.view.login.view.Activity_Login;

import rx.Subscriber;

/**
 * Created by lvxia on 2016-03-23.
 */
public abstract class CancelSubscriber<T> extends Subscriber<T> implements HttpCancelListener {

    @Override
    public void unSubscribe() {
        if (!isUnsubscribed()) {
            unsubscribe();
        }
    }

    @Override
    public void onNext(T t) {
        onEventNext(t);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof TokenException) {
            preToLogin();
            toLogin();
        } else {
            onEventError(e);
        }

    }

    public abstract void onEventNext(T t);

    protected void onEventError(Throwable e) {

    }

    protected void preToLogin() {

    }

    private void toLogin() {
        Intent intent = Activity_Login.getLoginIntent(App.instance);
        App.instance.startActivity(intent);
    }

}
