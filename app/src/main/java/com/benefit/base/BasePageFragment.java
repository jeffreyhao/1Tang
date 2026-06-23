package com.benefit.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.benefit.novelverse.view.base.BaseFragment;

/**
 * ViewPager中Fragment基类
 */
public abstract class BasePageFragment extends BaseFragment {
    // 懒加载机制核心标志位
    // View是否已经被创建出来， View的创建是在onCreateView中进行，
    private boolean isViewCreated = false;
    protected boolean isAttached;

    public BasePageFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        isAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        isViewCreated = false;
    }

    /**
     * 此方法会调用多次
     *
     * @param isVisibleToUser 是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 只有在Fragment的View已经被创建的前提下，UI处理才有意义
        if (isVisibleToUser) {
            lazyLoad();
        }
    }

    /**
     * 调用懒加载
     * 在lazyLoad()方法中进行双重标记判断,通过后即可进行数据加载
     */
    private void lazyLoad() {
        if (getUserVisibleHint() && isViewCreated) {
            onLazyLoad();
        }
    }

    /**
     * 刷新当前页面
     */
    public void refreshPage() {

    }

    /**
     * 抽象方法,具体加载数据的工作,交给子类去完成
     */
    protected abstract void onLazyLoad();
}