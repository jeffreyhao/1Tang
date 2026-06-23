package com.benefit.event;

/**
 * 参加活动成功通知
 */
public class MarketingCallbackEvent {
    private String marketId;
    public MarketingCallbackEvent(String marketId) {
        this.marketId = marketId;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }
}
