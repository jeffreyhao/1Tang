package com.baidu.baselibrary.base;

/**
* @author lhc
* @date 2022/5/9 9:04
* @desc 需要刷新页面的BaseView
*/
public interface IBaseRefreshView<T> extends IBaseView<T> {
    void finishRefreshOrLoadMore();
}
