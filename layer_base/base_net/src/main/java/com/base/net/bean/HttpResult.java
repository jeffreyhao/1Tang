package com.base.net.bean;

/**
* @author lhc
* @date 2022/5/9 9:18
* @desc 网络请求返回基类
*/

public class HttpResult {

    public static final int SUCCESS = 0;


    private int code;
    private String message;


    public HttpResult() {

    }

    public HttpResult(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }




}
