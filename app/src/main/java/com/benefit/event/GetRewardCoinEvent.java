package com.benefit.event;

/**
* @author lhc
* @date 2022/11/3 13:38
* @desc 获取福利金币成功
*/
public class GetRewardCoinEvent {
    private boolean refreshUserInfo;

    public GetRewardCoinEvent() {
    }

    public GetRewardCoinEvent(boolean refreshUserInfo) {
        this.refreshUserInfo = refreshUserInfo;
    }

    public boolean isRefreshUserInfo() {
        return refreshUserInfo;
    }

    public void setRefreshUserInfo(boolean refreshUserInfo) {
        this.refreshUserInfo = refreshUserInfo;
    }
}
