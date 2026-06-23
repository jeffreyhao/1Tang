package com.base.net.util;

import com.base.api.AppApi;
import com.base.net.bean.APIError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * APIError 解析
 */
public class ErrorUtils {


    public static APIError parseError(Response<?> response) {
        APIError error = null;
        try {
            Converter<ResponseBody, APIError> converter = AppApi.getApiManagerRetrofit().responseBodyConverter(APIError.class, new Annotation[0]);
            error = converter.convert(response.errorBody());
            error.statusCode=response.code();
        } catch (Throwable e) {
            return new APIError();
        }

        return error;
    }
}
