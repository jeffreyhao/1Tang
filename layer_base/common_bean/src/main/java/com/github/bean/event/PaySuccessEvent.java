package com.github.bean.event;

import com.baidu.baselibrary.log.ALog;

public class PaySuccessEvent {


    public String price;
    public String currency;
    public String orderId;
    public int payClass;
    public int payPageId;

    public PaySuccessEvent(String price, String currency, String orderId, int payClass, int payPageId) {
        this.price = price;
        this.currency = currency;
        this.orderId = orderId;
        this.payClass = payClass;
        this.payPageId = payPageId;
        ALog.textSingle("PaySuccessEvent(orderId:" + orderId + ", price:" + price + ", currency:" + currency + ", payClass:" + payClass + ")");
    }

}
