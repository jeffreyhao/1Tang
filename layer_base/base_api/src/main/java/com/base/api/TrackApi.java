package com.base.api;

import com.base.abs.ITrack;

import org.json.JSONObject;

/**
 * Created by haojiangfeng on 2025/3/17.
 */
public class TrackApi {

    public static ITrack iTrack;


    public static void sensorTrack(String key, JSONObject jsonObject){
        if(iTrack != null){
            iTrack.tack(key, jsonObject);
        }
    }

    public static void tackDnsParse(String requestUrl, long dnsParseTime, String parseIp, long tslTime, boolean isDnsParse){
        if(iTrack != null){
            iTrack.tackDnsParse(requestUrl, dnsParseTime, parseIp, tslTime, isDnsParse);
        }
    }

}
