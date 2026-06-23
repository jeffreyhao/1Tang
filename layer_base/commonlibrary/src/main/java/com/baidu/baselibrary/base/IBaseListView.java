package com.baidu.baselibrary.base;

import com.base.net.bean.ApiException;

/**
* @author lhc
* @date 2022/5/9 9:05
* @desc
*/

public interface IBaseListView<T> extends IBaseView<T> {

    void onRequestCacheSuccess(T t);

    void onRequestCacheFail(ApiException e);


}
