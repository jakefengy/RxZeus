package com.xm.zeus.network.extend;

/**
 * Created by lvxia on 2016-04-01.
 */
public class TokenException extends RuntimeException {

    public TokenException() {
    }

    public TokenException(String detailMessage) {
        super(detailMessage);
    }

    public TokenException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TokenException(Throwable throwable) {
        super(throwable);
    }
}
