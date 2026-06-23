package com.github.bean.zhifu;

import com.baidu.baselibrary.virtual.UniqueKey;
import com.google.gson.annotations.SerializedName;

/**
 * Time: 2024/1/9
 * Author: lhc
 * Desc:
 */
public class PendingOrderBean implements UniqueKey {

    /**
     * 订单id
     */
    @SerializedName("order")
    public String order;
    @SerializedName("purchase_token")
    public String purchaseToken;
    @SerializedName("out_trade_no")
    public String outTradeNo;
    @SerializedName("recharge_platform")
    public String rechargePlatform;
    @SerializedName("money_amount")
    public String moneyAmount;
    @SerializedName("recharge_amount")
    public String rechargeAmount;
    @SerializedName("give_amount")
    public String giveAmount;
    @SerializedName("diamond_amount")
    public String diamondAmount;
    @SerializedName("recharge_rule_id")
    public String rechargeRuleId;
    @SerializedName("product_id")
    public String productId;
    @SerializedName("currency_code")
    public String currencyCode;
    @SerializedName("country_code")
    public String countryCode;

    /**
     * 书籍id
     */
    @SerializedName("item_id")
    public String itemId;
    @SerializedName("top_up_source")
    public String topUpSource;
    @SerializedName("book_module")
    public String bookModule;
    @SerializedName("book_module_id")
    public String bookModuleId;
    @SerializedName("book_position")
    public int bookPosition;
    @SerializedName("source")
    public String source;

    @Override
    public String getUniqueKey() {
        return order;
    }
}
