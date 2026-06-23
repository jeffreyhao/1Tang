package com.github.bean.qianbao;

import com.google.gson.annotations.SerializedName;

public class QianbaoBean {
    @SerializedName("favourable_percent")
    public double discount;
    @SerializedName("expiring_soon")
    public int expiringSoon;
    @SerializedName("primary_amount")
    public int primaryBalance;
    @SerializedName("secondary_amount")
    public int secondaryBalance;
}