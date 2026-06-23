package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

public class GooglePayResponse {

    /**
     * 后台订单id
     */
    @SerializedName("orderId")
    public String orderId;
    @SerializedName("productId")
    public String productId;

    /**
     * 第三方交易号
     */
    @SerializedName("tradeId")
    public String tradeId;
    @SerializedName("token")
    public String token;
    @SerializedName("amount")
    public String amount;
    @SerializedName("countryCode")
    public String countryCode;
    /**
     * 第三方交易号
     */
    @SerializedName("billingId")
    public String billingId;

    @SerializedName("virtual_currency_name")
    public String virtual_currency_name;
    @SerializedName("virtual_currency_value")
    public int virtual_currency_value;

    public double  price;

    public String bookId;

    public int fromPageId;


    /**
     * @ProductType
     */
    public int productType;

    public Object penetrator;

    public @PayClass int payClass;


    public GooglePayResponse(){

    }


    @Override
    public String toString() {
        return "PayResponse{" +
                "orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", token='" + token + '\'' +
                ", amount='" + amount + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", billingId='" + billingId + '\'' +
                ", virtual_currency_name='" + virtual_currency_name + '\'' +
                ", virtual_currency_value=" + virtual_currency_value +
                ", price=" + price +
                ", bookId='" + bookId + '\'' +
                ", fromPageId=" + fromPageId +
                ", productType=" + productType +
                ", penetrator=" + penetrator +
                '}';
    }

}