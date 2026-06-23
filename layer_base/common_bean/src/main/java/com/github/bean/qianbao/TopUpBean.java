package com.github.bean.qianbao;

import com.google.gson.annotations.SerializedName;

public class TopUpBean {

    @SerializedName("order")
    private String order;
    @SerializedName("recharge_platform")
    private int rechargePlatform;
    @SerializedName("recharge_platform_name")
    private String rechargePlatformName;
    @SerializedName("money_amount")
    private String moneyAmount;
    @SerializedName("recharge_amount")
    private String rechargeAmount;
    @SerializedName("give_amount")
    private int giveAmount;
    @SerializedName("diamond_amount")
    private String diamondAmount;
    @SerializedName("recharge_rule_id")
    private String rechargeRuleId;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("update_time")
    private String updateTime;
    @SerializedName("order_type")
    private int orderType;
    @SerializedName("premium_title")
    private String premiumTitle;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getRechargePlatform() {
        return rechargePlatform;
    }

    public void setRechargePlatform(int rechargePlatform) {
        this.rechargePlatform = rechargePlatform;
    }

    public String getRechargePlatformName() {
        return rechargePlatformName;
    }

    public void setRechargePlatformName(String rechargePlatformName) {
        this.rechargePlatformName = rechargePlatformName;
    }

    public String getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(String moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public int getGiveAmount() {
        return giveAmount;
    }

    public void setGiveAmount(int giveAmount) {
        this.giveAmount = giveAmount;
    }

    public String getDiamondAmount() {
        return diamondAmount;
    }

    public void setDiamondAmount(String diamondAmount) {
        this.diamondAmount = diamondAmount;
    }

    public String getRechargeRuleId() {
        return rechargeRuleId;
    }

    public void setRechargeRuleId(String rechargeRuleId) {
        this.rechargeRuleId = rechargeRuleId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getPremiumTitle() {
        return premiumTitle;
    }

    public void setPremiumTitle(String premiumTitle) {
        this.premiumTitle = premiumTitle;
    }
}
