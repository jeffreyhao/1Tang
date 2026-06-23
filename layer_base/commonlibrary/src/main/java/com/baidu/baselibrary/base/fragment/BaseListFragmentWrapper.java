package com.baidu.baselibrary.base.fragment;

import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.base.IBaseListView;
import com.base.net.bean.ApiException;

import java.util.List;

import androidx.lifecycle.Lifecycle;
import io.reactivex.subjects.Subject;

/**
 * Created by haojiangfeng on 2024/11/25.
 */
// TODO: 2024/11/25  以后实现：整理 BaseListCustomFragment 和 BaseListFragment 的相同代码逻辑
public class BaseListFragmentWrapper<T, P extends BasePresenter> implements IBaseListView<List<T>> {


    @Override
    public void tokenLose() {

    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public Subject<Lifecycle.Event> getLifecycleEvent() {
        return null;
    }

    @Override
    public void onRequestCacheSuccess(List<T> ts) {

    }

    @Override
    public void onRequestCacheFail(ApiException e) {

    }

    @Override
    public void onRequestFail(ApiException e) {

    }

    @Override
    public void onRequestSuccess(List<T> ts) {

    }
}
