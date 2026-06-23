package com.base.util.net;

/**
 * Created by haojiangfeng on 2025/1/17.
 */
public enum NetworkType {

    NETWORK_WIFI("WiFi"),
    NETWORK_5G("5G"),
    NETWORK_4G("4G"),
    NETWORK_3G("3G"),
    NETWORK_2G("2G"),
    NETWORK_UNKNOWN("unknown"),
    NETWORK_NO("no");

    private String type;

    NetworkType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isWeakNetwork(){
        switch (this){
            case NETWORK_UNKNOWN:
            case NETWORK_NO:
            case NETWORK_2G:
            case NETWORK_3G:
                return true;
            default:
            case NETWORK_WIFI:
            case NETWORK_5G:
            case NETWORK_4G:
                return false;
        }
    }
}