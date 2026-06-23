package com.github.bean.qianbao;

import com.google.gson.annotations.SerializedName;

public class ConsumptionBean {

    public static int PurchaseType_Unlock       = 2;

    @SerializedName("book_title")
    public String bookTitle;

    @SerializedName("chapters")
    public String chapters;

    @SerializedName("start")
    public String start;

    @SerializedName("end")
    public String end;

    @SerializedName("coins")
    public int coins;

    @SerializedName("bonus")
    public int bonus;

    @SerializedName("pay_amount")
    public int payAmount;

    @SerializedName("time")
    public String time;


    /**
     *      0-购买章节
     *      1-购买vip
     *      2-看广告解锁
     */
    @SerializedName("purchase_type")
    public int purchaseType;

}