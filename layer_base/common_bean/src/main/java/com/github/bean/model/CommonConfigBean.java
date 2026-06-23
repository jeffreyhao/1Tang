package com.github.bean.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Time: 2024/4/29
 * Author: lhc
 * Desc:
 */
public class CommonConfigBean {
    @SerializedName("user_books_view_multiple")
    public int userBooksViewMultiple = 1000;

    @SerializedName("app_domains")
    public List<AppDomainsBean> appDomains;


    /**
     *  "server_time": 1719468450.4117215
     *  小数点前的单位：秒
     */
    @SerializedName("server_time")
    public double serverTimeStamp;

    @SerializedName("show_user_comment_popup")
    public int show_user_comment_popup;

}
