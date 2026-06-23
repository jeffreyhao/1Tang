package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

public class RechargeQueryBean {
    @SerializedName("code")
    public Integer code;
    @SerializedName("localized_message")
    public String localizedMessage;
    @SerializedName("message")
    public String message;
    public String order_no;
}