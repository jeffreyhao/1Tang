package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

/**
 * Created by haojiangfeng on 2025/2/12.
 */
public class SubscribeDailyReward {

    public static final int STATE_MISSED            = 0;
    public static final int STATE_RECEIVED          = 1;
    public static final int TYPE_NOT_ARRIVED        = 2;



    public int state, day;
    public String bonus;

    public boolean checked = false;

    public SubscribeDailyReward() {
    }

    public int getReceiveState() {
        return state;
    }

}
