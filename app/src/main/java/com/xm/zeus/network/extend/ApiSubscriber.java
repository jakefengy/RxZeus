package com.xm.zeus.network.extend;

import rx.Subscriber;

/**
 * Created by lvxia on 2016-03-23.
 */
public abstract class ApiSubscriber<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        if (e instanceof TokenException) {
            onTokenError(e);
        } else {
            onApiError(e);
        }

    }

    @Override
    public void onCompleted() {

    }

    protected abstract void onApiError(Throwable e);

    protected abstract void onTokenError(Throwable e);

}
