package com.benefit.novelverse.view.base;

/**
 * MVP View基类
 * @author adison
 * @date 2017/3/23
 * @time 下午11:57
 */
public interface MvpView<T> {
    T createPresenter();
}
