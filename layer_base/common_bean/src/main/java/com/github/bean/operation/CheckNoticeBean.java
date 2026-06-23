package com.github.bean.operation;

import com.google.gson.annotations.SerializedName;

public class CheckNoticeBean {
    @SerializedName("is_show")
    private final boolean isShow;
    private final String content;

    public CheckNoticeBean(boolean isShow, String content) {
        this.isShow = isShow;
        this.content = content;
    }

    public boolean isShow() {
        return isShow;
    }

    public String getContent() {
        return content;
    }
}
