package com.xm.zeus.network.extend;

import com.xm.zeus.network.entity.HttpResult;

import rx.functions.Func1;

/**
 * Created by lvxia on 2016-03-29.
 */
public class MapFunc1<T> implements Func1<HttpResult<T>, T> {

    @Override
    public T call(HttpResult<T> httpResult) {
        if (httpResult.getCode() != 0) {
            throw new ApiException(httpResult.getDescription());
        }
        return httpResult.getBody();
    }
}
