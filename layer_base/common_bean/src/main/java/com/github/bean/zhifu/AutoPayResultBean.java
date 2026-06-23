package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

public class AutoPayResultBean {
    @SerializedName("download_url")
    public String downloadUrl;
    @SerializedName("need_pay")
    public boolean needPay;
    @SerializedName("chapter_content")
    public String chapterContent;
}