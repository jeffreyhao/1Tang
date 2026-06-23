/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.base.net.state.tracker;

import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.base.api.AppApi;
import com.base.api.Logger;
import com.base.net.ApiBase;
import com.base.net.state.NetChangeCallback;
import com.base.net.state.type.NetworkState;
import com.base.util.net.NetworkUtils;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class NetworkStateTracker implements ITrack{

    static final String TAG = "NetworkStateTracker";

    @Nullable
    static Runnable mNetChangeRunnable = null;


    private class NetworkStateCallback extends NetworkCallback {

        NetworkStateCallback() {}

        /**
         *  已连接到网络
         */
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog){
                Logger.verbose(TAG, "Network connection available: " + network);
            }
            checkNetStateChange(network);
        }

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
                Logger.verbose(TAG, "Network connection unavailable");
            }
            checkNetStateChange(null);
        }

        @Override
        public void onLosing(@NonNull Network network, int maxMsToLive) {
            super.onLosing(network, maxMsToLive);
            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
                Logger.verbose(TAG, "Network connection onLosing: " + network);
            }
        }

        /**
         *  连接已中断
         */
        @Override
        public void onLost(@NonNull Network network) {
            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
                Logger.verbose(TAG, "Network connection lost: " + network);
            }
            checkNetStateChange(network);
        }

        @Override
        public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
            super.onBlockedStatusChanged(network, blocked);
            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
                Logger.verbose(TAG, "Network connection onBlockedStatusChanged: " + network);
            }
            checkNetStateChange(network);
        }

        /**
         *  网络功能已更改
         *
         * @param network 新连接网络
         * @param capabilities 新连接网络的一些能力参数
         */
        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities capabilities) {

            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog){
                int signalStrength = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    signalStrength = capabilities.getSignalStrength();
                }
                String getCapabilities = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getCapabilities = Arrays.toString(capabilities.getCapabilities());
                }
                String getEnterpriseIds = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getEnterpriseIds = Arrays.toString(capabilities.getEnterpriseIds());
                }
                String netTypeString;
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    netTypeString = "WiFi网络";
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    netTypeString = "蜂窝网络";
                } else {
                    netTypeString = "其他类型";
                }
                NetworkInfo networkInfo = mConnectivityManager.getNetworkInfo(network);
                String netWorkType = NetworkUtils.getNetworkType(networkInfo).getType();
                int upKbps = capabilities.getLinkUpstreamBandwidthKbps();
                int downKbps = capabilities.getLinkDownstreamBandwidthKbps();

                if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
                    Logger.verbose(TAG, "onCapabilitiesChanged(): " + netTypeString +  network
                            + "\n   1. capabilities:" + capabilities
                            + "\n   2. networkInfo:" + networkInfo
                            + "\n   3. getCapabilities:" + getCapabilities
                            + "\n   4. getEnterpriseIds:" + getEnterpriseIds
                            + "\n   5. " + netWorkType + "信号强度: " + signalStrength + " dBm"
                            + "\n   6. 上行宽带: " + upKbps + " Kbps"
                            + "\n   7. 下行宽带: " + downKbps + " Kbps"
                    );
                }
            }
            checkNetStateChange(network);
        }

        /**
         * @param linkProperties LinkProperties对象，可以提供关于路由、链接地址、接口名称、代理信息（如有）和 DNS 服务器的信息
         */
        @Override
        public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
                Logger.verbose(TAG, "onLinkPropertiesChanged(): " + network
                        + "\n   linkProperties: " + linkProperties.toString()
                );
            }
            checkNetStateChange(network);
        }
    }




    @NonNull
    private final ConnectivityManager mConnectivityManager;

    @NonNull
    private final NetworkStateCallback mNetworkCallback;

    @Nullable
    private final NetChangeCallback mOnChangeCallback;

    @NonNull
    private final NetworkState mNetworkState;


    /**
     * Create an instance of {@link NetworkStateTracker}
     */
    public NetworkStateTracker(@NonNull ConnectivityManager connectivityManager, @Nullable NetChangeCallback callback) {
        this.mConnectivityManager = connectivityManager;
        this.mOnChangeCallback = callback;
        this.mNetworkCallback = new NetworkStateCallback();
        this.mNetworkState = NetworkStateUtil.getActiveNetworkState(mConnectivityManager);
        if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
            Logger.verbose(TAG, "init()"
                    + "\n   mNetworkState: " + mNetworkState);
        }
    }

    @Override
    public void startTracking() {
        try {
            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
                Logger.verbose(TAG, "Registering network callback");
            }
            mConnectivityManager.registerDefaultNetworkCallback(mNetworkCallback);
        } catch (IllegalArgumentException | SecurityException e) {
            // Catching the exceptions since and moving on - this tracker is only used for
            // GreedyScheduler and there is nothing to be done about device-specific bugs.
            // IllegalStateException: Happening on NVIDIA Shield K1 Tablets.  See b/136569342.
            // SecurityException: Happening on Solone W1450.  See b/153246136.
            Logger.exception(e);
        }

        if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
            Logger.verbose(TAG, "startTracking()"
                    + "\n   mNetworkState : " + mNetworkState);
        }
    }

    @Override
    public void stopTracking() {
        try {
            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
                Logger.verbose(TAG, "Unregistering network callback");
            }
            mConnectivityManager.unregisterNetworkCallback(mNetworkCallback);

        } catch (IllegalArgumentException | SecurityException e) {
            // Catching the exceptions since and moving on - this tracker is only used for
            // GreedyScheduler and there is nothing to be done about device-specific bugs.
            // IllegalStateException: Happening on NVIDIA Shield K1 Tablets.  See b/136569342.
            // SecurityException: Happening on Solone W1450.  See b/153246136.
            Logger.exception(e);
        }
    }

    @NonNull
    @Override
    public NetworkState getCurrentState() {
        return mNetworkState;
    }

    private void checkNetStateChange(@Nullable Network network){
        // 切换到有网的一瞬间，联网状态还不稳定。这里延迟1秒发请求
        if (mNetChangeRunnable == null){
            mNetChangeRunnable = new Runnable() {
                @Override
                public void run() {
                    NetworkState newState = NetworkStateUtil.getActiveNetworkState(mConnectivityManager);
                    checkChange(newState);
                }
            };
        }
        AppApi.getHandler().removeCallbacks(mNetChangeRunnable);
        AppApi.getHandler().postDelayed(mNetChangeRunnable, 1000);
    }


    private void checkChange(@NonNull NetworkState newState){
        if(mNetworkState.equals(newState)){
            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
                Logger.verbose(TAG, "checkNetStateChange(), NetworkState is same."
                        + "\n   NetworkState : " + newState);
            }
        } else {
            if(ApiBase.DEBUG && NetworkStateUtil.showNetStateLog) {
                Logger.debug(TAG, "checkNetStateChange(), NetworkState is different."
                        + "\n   oldState : " + mNetworkState
                        + "\n   newState : " + newState);
            }
            if(mOnChangeCallback != null){
                mOnChangeCallback.onNetChanged(mNetworkState, newState);
            }
            mNetworkState.update(newState);
        }
    }
}
