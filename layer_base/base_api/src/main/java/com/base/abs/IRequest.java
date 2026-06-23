package com.base.abs;

import com.base.callback.NetworkCallback;

import java.util.LinkedHashMap;

public interface IRequest {

    void post(String url, LinkedHashMap<String, Object> params, NetworkCallback callback);


}
