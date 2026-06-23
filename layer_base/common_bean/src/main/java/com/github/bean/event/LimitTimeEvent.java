package com.github.bean.event;

import com.github.bean.qianbao.RechargePlatformBean;
import com.github.bean.zhifu.PayRule;

public class LimitTimeEvent {


    public PayRule payRule;

    public String orderNo;

    public int payClass;

//    public LimitTimeEvent(String paySystem, RechargePlatformBean platform, PayRule subscribePayBean, PayRule payRule){
//    }

    public LimitTimeEvent(int payClass, PayRule payRule){
        this.payClass = payClass;
        this.payRule = payRule;
    }

    public String platformProductId(){
        if(payRule != null){
            return payRule.platformProductId;
        }
        return "";
    }

    public int rechargeFace(){
        if(payRule != null){
            return payRule.rechargeFace;
        }
        return 0;
    }

    public double rechargeMoney() {
        if(payRule != null){
            return payRule.rechargeMoney;
        }
        return 0;
    }

    public int rechargeGive() {
         if(payRule != null){
            return payRule.rechargeGive;
        }
        return 0;
    }

}
