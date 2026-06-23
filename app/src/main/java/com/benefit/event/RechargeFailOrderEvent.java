package com.benefit.event;

/**
 * Created by haojiangfeng on 2024/7/29.
 */
public class RechargeFailOrderEvent {


    public String order;
    public boolean isSuccess;


    public RechargeFailOrderEvent(String order, boolean success){
        this.order = order;
        this.isSuccess = success;
    }


}
