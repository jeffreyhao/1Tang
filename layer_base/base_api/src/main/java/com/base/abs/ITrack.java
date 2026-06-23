package com.base.abs;

import org.json.JSONObject;

/**
 * Created by haojiangfeng on 2025/3/17.
 */
public interface ITrack {

    void tack(String key, JSONObject jsonObject);

    void tackDnsParse(String requestUrl, long dnsParseTime, String parseIp, long tslTime, boolean isDnsParse);
}
