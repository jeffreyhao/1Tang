package com.base.net.dns;

import android.text.TextUtils;

import com.base.api.Logger;
import com.base.net.ApiBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haojiangfeng on 2025/5/6.
 */
public class DeeplinkHost {


    /**
     * 深链备用IP
     */
    public static List<String> backupDeeplinkIps = new ArrayList<>();



    static {
        backupDeeplinkIps.add("http://54.234.180.247/");
        backupDeeplinkIps.add("http://52.23.233.222/");
    }




    public static boolean isAppDeeplink(String hostName) {
        Logger.textSingle("ApiDns", "isAppDeeplink", "isAppDeeplink->hostName: " + hostName);
        if(TextUtils.isEmpty(hostName)) {
            return false;
        }
        if(ApiBase.WEB_DEEP_LINK_BASE_URL.contains(hostName)) {
            return true;
        }
        for(String url : backupDeeplinkIps){
            if(url.contains(hostName)){
                return true;
            }
        }
        return false;
    }


    public static String getNextHost(String hostName){
        if(ApiBase.WEB_DEEP_LINK_BASE_URL.contains(hostName)) {
            return backupDeeplinkIps.get(0);
        } else if(backupDeeplinkIps.get(0).contains(hostName)){
            return backupDeeplinkIps.get(1);
        } else {
            return ApiBase.WEB_DEEP_LINK_BASE_URL;
        }
    }



}
