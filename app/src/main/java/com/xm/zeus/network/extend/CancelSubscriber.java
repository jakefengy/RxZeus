package com.xm.zeus.network.extend;

import rx.Subscriber;

/**
 * Created by lvxia on 2016-03-23.
 */
public abstract class CancelSubscriber<T> extends Subscriber<T> implements HttpCancelListener {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCancelProgress() {
        if (!isUnsubscribed()) {
            unsubscribe();
        }
    }

    @Override
    public void onNext(T t) {
        onEventNext(t);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
    }

    public abstract void onEventNext(T t);

}
