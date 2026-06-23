package com.base.net.bean;

import android.text.TextUtils;

/**
* @author lhc
* @date 2022/5/9 9:16
* @desc 异常处理封装类
 *
 * @see ApiErrorCode
*/
public class ApiException extends Exception {


    /**
     * 错误码。 具体错误码参考 {@link ApiErrorCode}
     */
    private int code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * Object封装
     */
    private Object extra;



    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiException(int code, String msg, Object extra) {
        this.code = code;
        this.msg = msg;
        this.extra = extra;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        String message = getMessage();
        return TextUtils.isEmpty(message) ? msg : msg + "|" + message;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

}