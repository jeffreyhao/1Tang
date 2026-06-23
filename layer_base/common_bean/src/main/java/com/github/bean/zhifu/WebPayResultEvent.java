package com.github.bean.zhifu;

import android.text.TextUtils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by niepan on 2/6/26.
 */
public class WebPayResultEvent {

    public static final int STATUS_NONE     = -1;
    public static final int STATUS_SUCCESS  = 1;
    public static final int STATUS_FAILED   = 2;
    public static final int STATUS_CLOSED   = 3;
    public static final int STATUS_PENDING  = 4;
    public static final int STATUS_UNKNOWN  = 5;

    @IntDef({
            WebPayResultEvent.STATUS_NONE,
            WebPayResultEvent.STATUS_SUCCESS,
            WebPayResultEvent.STATUS_FAILED,
            WebPayResultEvent.STATUS_CLOSED,
            WebPayResultEvent.STATUS_PENDING,
            WebPayResultEvent.STATUS_UNKNOWN
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {}




    private String accountId;
    private String rechargePlatform;
    private String rechargeRuleId;
    private String price;
    private String payment;
    public String orderNo;
    private String outTradeNo;
    private String tradeToken;
    private String status;

    public @Status int statusInt;

    public WebPayResultEvent(String accountId, String rechargePlatform, String rechargeRuleId, String price, String payment, String orderNo, String outTradeNo, String tradeToken, String status) {
        this.accountId = accountId;
        this.rechargePlatform = rechargePlatform;
        this.rechargeRuleId = rechargeRuleId;
        this.price = price;
        this.payment = payment;
        this.orderNo = orderNo;
        this.outTradeNo = outTradeNo;
        this.tradeToken = tradeToken;
        this.status = status;
        this.statusInt = parseStatus(status);
    }

    private int parseStatus(String status) {
        if(TextUtils.isEmpty(status)) {
            return STATUS_UNKNOWN;
        }
        if ("SUCCESS".equalsIgnoreCase(status)) {
            return STATUS_SUCCESS;
        } else if ("FAILED".equalsIgnoreCase(status)) {
            return STATUS_FAILED;
        } else if ("CLOSED".equalsIgnoreCase(status)) {
            return STATUS_CLOSED;
        } else if ("PENDING".equalsIgnoreCase(status)) {
            return STATUS_PENDING;
        } else {
            return STATUS_UNKNOWN;
        }
    }
}
