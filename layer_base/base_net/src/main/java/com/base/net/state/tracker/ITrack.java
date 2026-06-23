package com.base.net.state.tracker;

import com.base.net.state.type.NetworkState;

import androidx.annotation.NonNull;

/**
 * Created by haojiangfeng on 2024/11/26.
 */
public interface ITrack {

    void startTracking();
    void stopTracking();

    @NonNull
    NetworkState getCurrentState();

}
