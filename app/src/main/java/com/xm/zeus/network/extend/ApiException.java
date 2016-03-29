package com.xm.zeus.network.extend;

/**
 * Created by lvxia on 2016-03-22.
 */
public class ApiException extends RuntimeException {

    public ApiException() {
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public ApiException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }
}
