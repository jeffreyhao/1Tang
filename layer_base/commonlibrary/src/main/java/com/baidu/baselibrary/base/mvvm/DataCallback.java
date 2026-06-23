package com.baidu.baselibrary.base.mvvm;

import com.base.net.bean.ApiException;

public interface DataCallback<T> {
    void onSuccess(T data);
    void onFail(ApiException e);
}
