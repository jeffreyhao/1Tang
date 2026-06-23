package com.base.net.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.base.net.util.ErrorUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import androidx.annotation.NonNull;

import com.base.net.util.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Http 统一CallBack 处理
 */
public abstract class ResponseResultCallback<T> implements Callback<T> {
    @Override
    public final void onResponse(@NonNull Call<T> call, Response<T> response) {
        int statusCode = response.code();
        if (response.isSuccessful()) {
            T data = response.body();
            if (data == null) {
                onFail(call, new APIError(APIError.ERROR_BODY_EMPTY, statusCode));
            } else {
                onSuccess(call, data);
            }
        } else {
            APIError error = ErrorUtils.parseError(response);
            if (error.errorCode == 0) {
                error.errorCode = statusCode;
                if (TextUtils.isEmpty(error.errorMessage)) {
                    error.errorMessage = response.message();
                }
            }
            onFail(call, error);
        }

    }

    @Override
    public final void onFailure(@NonNull Call<T> call, @NonNull Throwable throwable) {
        APIError apiError = new APIError(APIError.ERROR_DEFAULT, throwable);
        if (throwable instanceof ConnectException) {
            apiError.errorUserMsg = "网络连接错误，请重试！";
        }else if(throwable instanceof SocketTimeoutException) {
            apiError.errorCode = ApiErrorCode.LOCAL_SOCKET_TIME_OUT;
        }
        onFail(call, apiError);
    }

    /**
     * 请求成功的回调
     */
    protected abstract void onSuccess(Call<T> call, T response);

    /**
     * 请求返回204
     */
    protected void onResponse204(Call<T> call) {

    }

    /**
     * 请求失败的回调 可以不处理
     */
    protected void onFail(Call<T> call, APIError exception) {

    }
}
