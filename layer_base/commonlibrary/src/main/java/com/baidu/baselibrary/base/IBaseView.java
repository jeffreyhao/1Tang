package com.baidu.baselibrary.base;

import com.base.net.bean.ApiException;

/**
* @author lhc
* @date 2022/5/9 9:05
* @desc
*/

public interface IBaseView<T> extends IView {


    void onRequestFail(ApiException e);

    void onRequestSuccess(T t);

}
