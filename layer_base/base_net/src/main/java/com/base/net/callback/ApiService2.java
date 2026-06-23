package com.base.net.callback;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService2 {
    @POST
    @FormUrlEncoded
    Call<Object> refreshToken(@Url String url, @FieldMap Map<String, Object> maps);
}

