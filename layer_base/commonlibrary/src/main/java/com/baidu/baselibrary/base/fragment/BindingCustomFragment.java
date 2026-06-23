package com.baidu.baselibrary.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baidu.baselibrary.base.BasePresenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
* @author lhc
* @date 2022/5/9 9:01
* @desc fragment 基类
*/
public abstract class BindingCustomFragment<VB extends ViewDataBinding, P extends BasePresenter> extends BaseCustomFragment<P> {

    protected VB mBinding;

    @Override
    protected ViewGroup createRootView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);
        return (ViewGroup) mBinding.getRoot();
    }

    protected abstract int getLayoutId();


}