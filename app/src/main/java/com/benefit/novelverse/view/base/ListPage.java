package com.benefit.novelverse.view.base;


import com.meta.bean.model.api.APIError;

import java.util.List;

/**
* 列表数据接口
* @author adison
* @date 2017/6/24 
* @time 上午1:12
*/

public interface ListPage<D,T> extends MvpView<T>{
    /**
     * 刷新数据
     * @param list 当前的数据列表
     * @param forceRefresh 是否是强制刷新操作
     * @param excepiton api error
     */
    void refreshData(List<D> list, boolean forceRefresh, APIError excepiton);

}
