package com.benefit.novelverse.utils;

import android.text.TextUtils;

import com.base.api.GlobalContext;
import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.cache.ACache;
import com.base.util.net.NetworkUtil;
import com.base.util.thread.ExecutorsUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Time: 2023/10/27
 * Author: lhc
 * Desc: 统计网络请求时间统计
 */
public class RequestTimeTrackUtil {
    public static void saveRequestItem(String requestUrl, long requestTime, String requestStatus, String message, boolean isTry){
        ExecutorsUtils.getInstance().getAppExecutors().diskIO().execute(() -> {
            try{
                if(GlobalContext.getContext()!=null) {
                    JSONArray array = null;
                    String json = ACache.get(GlobalContext.getContext()).getAsString("requestJson");
                    if(!TextUtils.isEmpty(json)){
                        array = new JSONArray(json);
                    }
                    JSONObject jsonObject = new JSONObject();
                    String url = requestUrl;
                    if(url.contains("?")){
                        url = url.split("\\?")[0];
                    }
                    jsonObject.put("request_url", url);
                    jsonObject.put("request_time", requestTime);
                    jsonObject.put("request_status", NetworkUtil.isNetAvailable(GlobalContext.getContext())?requestStatus : ApiErrorCode.LOCAL_NET_AVAILABLE);
                    jsonObject.put("msg", message);
                    jsonObject.put("is_try", isTry);
                    if(array==null){
                        array = new JSONArray();
                    }
                    array.put(jsonObject);
                    if(array.length()>10){
                        uploadApiRequestEvent(array);
                        ACache.get(GlobalContext.getContext()).put("requestJson", "");
                    }else{
                        ACache.get(GlobalContext.getContext()).put("requestJson", array.toString());
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }

    public static void uploadApiRequestEvent(JSONArray array) {
        try{
            for(int i=0; i<array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                String requestUrl = object.optString("request_url");
                long requestTime = object.optLong("request_time");
                String requestStatus = object.optString("request_status");
                String msg = object.optString("msg");
                boolean isTry = object.optBoolean("is_try");
//                SensorsConfig.INSTANCE.trackRequestTime(requestUrl,requestTime,requestStatus,msg,isTry);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveDnsParseItem(String requestUrl, long dnsParseTime, String parseIp, long tslTime, boolean isDnsParse){
        ExecutorsUtils.getInstance().getAppExecutors().diskIO().execute(() -> {
            try{
                if(GlobalContext.getContext()!=null) {
                    JSONArray array = null;
                    String json = ACache.get(GlobalContext.getContext()).getAsString("requestJson_dns");
                    if(!TextUtils.isEmpty(json)){
                        array = new JSONArray(json);
                    }
                    JSONObject jsonObject = new JSONObject();
                    String url = requestUrl;
                    if(url.contains(",")){
                        url = url.split(",")[1];
                        if(url.contains("url=")){
                            url = url.replace("url=","");
                        }
                    }
                    jsonObject.put("request_url", url);
                    if(isDnsParse){
                        jsonObject.put("dns_parse_time_ms", dnsParseTime);
                        jsonObject.put("dns_parse_ip", parseIp);
                    }else{
                        jsonObject.put("tsl_time_ms", tslTime);
                    }
                    jsonObject.put("is_dns_parse", isDnsParse);
                    if(array==null){
                        array = new JSONArray();
                    }
                    array.put(jsonObject);
                    if(array.length()>4){
//                        uploadRequestEvent(array);
                        ACache.get(GlobalContext.getContext()).put("requestJson_dns", "");
                    }else{
                        ACache.get(GlobalContext.getContext()).put("requestJson_dns", array.toString());
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }



}
