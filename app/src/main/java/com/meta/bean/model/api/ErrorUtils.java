package com.meta.bean.model.api;


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
        Converter<ResponseBody, APIError> converter = APIManager.get().getRetrofit().responseBodyConverter(APIError.class, new Annotation[0]);
        APIError error;
        try {
            error = converter.convert(response.errorBody());
            error.statusCode=response.code();
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}
