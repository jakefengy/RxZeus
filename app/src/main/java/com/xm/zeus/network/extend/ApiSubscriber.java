package com.xm.zeus.network.extend;

import android.content.Intent;

import com.xm.zeus.app.App;
import com.xm.zeus.view.login.view.Activity_Login;

import rx.Subscriber;

/**
 * Created by lvxia on 2016-03-23.
 */
public abstract class ApiSubscriber<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        if (e instanceof TokenException) {
            onTokenError();
        } else {
            onApiError(e);
        }

    }

    protected abstract void onApiError(Throwable e);

    protected void onTokenError() {
        Intent intent = Activity_Login.getLoginIntent(App.instance);
        App.instance.startActivity(intent);
    }

}
