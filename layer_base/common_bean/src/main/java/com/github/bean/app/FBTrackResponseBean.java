package com.github.bean.app;

/**
 * Time: 2024/1/31
 * Author: lhc
 * Desc:
 */
public class FBTrackResponseBean {
    private int code;
    private String msg;
    private WebDeeplinkBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public WebDeeplinkBean getData() {
        return data;
    }

    public void setData(WebDeeplinkBean data) {
        this.data = data;
    }
}
