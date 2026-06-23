package com.github.bean.event;

import com.baidu.baselibrary.log.ALog;

public class CommonPaySuccessEvent {

    public String price;
    public String currency;
    public String orderId;
    public int payPageId;

    public CommonPaySuccessEvent(String price, String currency, String orderId) {
        this.price = price;
        this.currency = currency;
        this.orderId = orderId;
        ALog.textSingle("PaySuccessEvent(orderId:" + orderId + ", price:" + price + ", currency:" + currency + ")");
    }

    public CommonPaySuccessEvent(PaySuccessEvent event) {
        this.price = event.price;
        this.currency = event.currency;
        this.orderId = event.orderId;
        this.payPageId = event.payPageId;
    }

}
