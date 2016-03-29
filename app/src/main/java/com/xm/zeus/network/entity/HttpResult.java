package com.xm.zeus.network.entity;

/**
 * Created by lvxia on 2016-03-22.
 */
public class HttpResult<T> {

    private int Code;
    private String Message;
    private T Body;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getBody() {
        return Body;
    }

    public void setBody(T body) {
        Body = body;
    }

    public String getDescription() {
        return "code = " + getCode() + " , message = " + getMessage();
    }

}
