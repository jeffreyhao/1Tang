package com.github.bean.qianbao;

import com.google.gson.annotations.SerializedName;

public class BonusBean {
    @SerializedName("give_no")
    public String giveNo;
    @SerializedName("give_amount")
    public String giveAmount;
    @SerializedName("surplus_amount")
    public String surplusAmount;
    @SerializedName("invalid_amount")
    public String invalidAmount;
    @SerializedName("obtain_type")
    public int obtainType;
    @SerializedName("obtain_type_tip")
    public String obtainTypeTip;
    @SerializedName("obtain_way")
    public String obtainWay;
    @SerializedName("obtain_time")
    public String obtainTime;
    @SerializedName("local_obtain_time")
    public String localObtainTime;
    @SerializedName("invalid_time")
    public String invalidTime;
    @SerializedName("local_invalid_time")
    public String localInvalidTime;
    @SerializedName("is_can_invalid")
    public String isCanInvalid;
    @SerializedName("is_expired")
    public String isExpired;
    @SerializedName("is_used")
    public String isUsed;
    @SerializedName("expired")
    public boolean expired;
}
