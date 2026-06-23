package com.base.net.state.type;

import com.base.util.net.NetworkType;

import androidx.annotation.NonNull;

/**
 * Created by haojiangfeng on 2024/11/4.
 *
 * @see androidx.work.impl.constraints.NetworkState
 */
public class NetworkState {


    /**
     * 网络是否已连接
     */
    private boolean mIsConnected;

    /**
     * 网络是否经过验证
     */
    private boolean mIsValidated;

    /**
     * 是否为计费网络
     */
    private boolean mIsMetered;

    /**
     *  网络是否处于非漫游状态
     */
    private boolean mIsNotRoaming;

    /**
     * 网络类型
     */
    private NetworkType mNetworkType;


    /**
     * @param isConnected   网络是否已连接
     * @param isValidated   网络是否经过验证。true-不仅网络已连接，而且可以成功访问互联网
     * @param isMetered     网络是否为计费网络
     * @param isNotRoaming  网络是否处于非漫游状态
     */
    public NetworkState(NetworkType networkType, boolean isConnected, boolean isValidated, boolean isMetered, boolean isNotRoaming) {
        mNetworkType = networkType;
        mIsConnected = isConnected;
        mIsValidated = isValidated;
        mIsMetered = isMetered;
        mIsNotRoaming = isNotRoaming;
    }

    /**
     * 更新网络状态
     */
    public void update(NetworkState state){
        mNetworkType = state.getNetworkType();
        mIsConnected = state.isConnected();
        mIsValidated = state.isValidated();
        mIsMetered = state.isMetered();
        mIsNotRoaming = state.isNotRoaming();
    }

    /**
     * @return 有效网络是否发生变化
     */
    public boolean isNetValidChange(NetworkState oldState){
        if(oldState == null){
            return true;
        }
        return isNetAvailable() != oldState.isNetAvailable();
    }

    /**
     * @return 弱网状态是否发生变化
     */
    public boolean isWeakNetChange(NetworkState oldState){
        if(oldState == null){
            return true;
        }
        return isWeakNetwork() != oldState.isWeakNetwork();
    }


    /**
     * @return 是否为有效网络
     */
    public boolean isNetAvailable(){
        return mIsConnected && mIsValidated;
    }

    /**
     * @return 是否为弱网
     * <br>
     * 目前的判定标准是：4G、5G、WLan为强网，2G、3G为弱网。 2025.1.17
     * <br>
     */
    public boolean isWeakNetwork(){
        return mNetworkType.isWeakNetwork();
    }





    /**
     * @return 网络类型
     */
    public NetworkType getNetworkType(){
        return mNetworkType;
    }

    /**
     *  @return 网络是否已连接
     */
    public boolean isConnected() {
        return mIsConnected;
    }

    /**
     * @return 网络是否经过验证。 true-不仅网络已连接，而且可以成功访问互联网
     */
    public boolean isValidated() {
        return mIsValidated;
    }

    /**
     *  @return 网络是否为计费网络
     */
    public boolean isMetered() {
        return mIsMetered;
    }

    /**
     *  @return 网络是否处于非漫游状态
     */
    public boolean isNotRoaming() {
        return mIsNotRoaming;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetworkState)) {
            return false;
        }
        NetworkState other = (NetworkState) o;
        return this.mNetworkType == other.mNetworkType
                && this.mIsConnected == other.mIsConnected
                && this.mIsValidated == other.mIsValidated
                && this.mIsMetered == other.mIsMetered
                && this.mIsNotRoaming == other.mIsNotRoaming;
    }

    @Override
    public int hashCode() {
        int result = 0x00000;
        if (mIsConnected) result += 0x00001;
        if (mIsValidated) result += 0x00010;
        if (mIsMetered) result += 0x00100;
        if (mIsNotRoaming) result += 0x01000;
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("[ 网络类型 Type=%s,  连接 Connected=%b,  有效 Validated=%b,  计费 Metered=%b,  非漫游 NotRoaming=%b ]",
                mNetworkType.getType(), mIsConnected, mIsValidated, mIsMetered, mIsNotRoaming);
    }



}
