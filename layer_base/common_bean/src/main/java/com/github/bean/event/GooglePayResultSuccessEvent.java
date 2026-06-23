package com.github.bean.event;

import com.github.bean.zhifu.GooglePayResponse;

/**
 * Created by haojiangfeng on 2024/7/24.
 */
public class GooglePayResultSuccessEvent {


    public String order;
    public int fromPageId;
    public GooglePayResponse response;

    public GooglePayResultSuccessEvent(String orderNo, int fromPageId, GooglePayResponse response){
        this.order = orderNo;
        this.fromPageId = fromPageId;
        this.response = response;
    }





}
