package com.xm.zeus.network.extend;

import com.xm.zeus.network.Network;
import com.xm.zeus.utils.RxBus;
import com.xm.zeus.utils.entity.rxbus.TokenError;

import rx.Subscriber;

/**
 * Created by lvxia on 2016-03-23.
 */
public abstract class ApiSubscriber<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            if (exception.getErrorCode() == Network.ResultCode.RESULT_TOKEN_EXPIRED || exception.getErrorCode() == Network.ResultCode.RESULT_TOKEN_INVALID) {
                onTokenError(exception);
            } else {
                onCommonError(e);
            }
        } else {
            onCommonError(e);
        }

    }

    @Override
    public void onCompleted() {

    }

    private void onTokenError(ApiException e) {
        TokenError tokenError = new TokenError();
        RxBus.getInstance().post(tokenError);
    }

    protected void onCommonError(Throwable e) {
    }

}
