package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

public class RechargeOrderBean {
    @SerializedName("order_no")
    public String orderNo;
    @SerializedName("approval_url")
    public String approvalUrl;
    @SerializedName("callback_url")
    public String callbackUrl;
    @SerializedName("repair_url")
    public String repairUrl;
    @SerializedName("primary_amount_before_recharge")
    public String primaryAmount;
    @SerializedName("give_amount_before_recharge")
    public String primaryGiveAmount;
    @SerializedName("activity_amount_before_recharge")
    public String primaryActivityAmount;

    /**
     * 本地字段
     */
    public PayRule bindingPayRule;
    public int bindingPayPageId;
}