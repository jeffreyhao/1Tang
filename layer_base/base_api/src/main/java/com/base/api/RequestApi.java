package com.base.api;


import com.base.abs.IRequest;
import com.base.callback.NetworkCallback;

import java.util.LinkedHashMap;

public class RequestApi {


    public static IRequest iRequestApi;


    public static void post(String url, LinkedHashMap<String, Object> params, NetworkCallback callback){
        if(iRequestApi != null){
            iRequestApi.post(url, params, callback);
        }
    }

}
