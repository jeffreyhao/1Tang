package com.base.net.state;

import com.base.net.state.type.NetworkState;

/**
 * Created by haojiangfeng on 2023/9/21.
 */
public interface NetChangeCallback {

    void onNetChanged(NetworkState oldState, NetworkState newState);

}
