package com.benefit.event;

/**
 * Time: 2024/3/26
 * Author: lhc
 * Desc: 取消充值或者章节购买登录提示弹窗
 */
public class CancelRechargeLoginEvent {
    //是否是取消充值登录提示弹窗
    private boolean isCancelRechargeLogin;
    public CancelRechargeLoginEvent(boolean isCancelRechargeLogin) {
        this.isCancelRechargeLogin = isCancelRechargeLogin;
    }

    public boolean isCancelRechargeLogin() {
        return isCancelRechargeLogin;
    }

    public void setCancelRechargeLogin(boolean cancelRechargeLogin) {
        isCancelRechargeLogin = cancelRechargeLogin;
    }
}
