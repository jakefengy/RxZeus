package com.xm.zeus.network.extend;

/**
 * 数据请求错误 当code为 Network.ResultCode 时触发。
 */
public class ApiException extends Throwable {

    private int errorCode;
    private String errorMsg;

    public ApiException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "ErrorCode " + errorCode + " , ErrorMsg " + errorMsg;
    }
}
