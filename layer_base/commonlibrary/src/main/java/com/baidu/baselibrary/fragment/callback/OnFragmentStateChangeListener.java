package com.baidu.baselibrary.fragment.callback;


import android.view.View;

import com.baidu.baselibrary.fragment.CustomFragment;


public interface OnFragmentStateChangeListener {

    /**
     * 获取最顶端的fragment
     * @return
     */
    CustomFragment getTopFragment();
    /**
     * 点击返回键执行的回调
     * @return
     */
    boolean onBackPress();
    /**
     * View到前台显示了
     * @param indexByLast
     */
    void onShow(int indexByLast);

    /**
     * View到后台隐藏了
     * @param indexByLast
     */
    void onHide(int indexByLast);

    /**
     * view被移除了
     * @param view
     */
    void onDestroy(View view);

    /**
     * view被移除了, 以index计算
     * @param indexByLast
     */
    void onDestroy(int indexByLast);
    /**
     * 获取当前Fragment栈的个数
     * @return
     */
    int getFragmentCount();

    /**
     * 获取最顶端第indexBylast个Fragment的View
     * @param indexBylast
     * @return
     */
    View getFragmentView(int indexBylast);

    /**
     * 获取该View所对应的Fragment的倒序索引
     * @param view
     * @return
     */
    int getFragmentIndexByLast(View view);
    /**
     * 获取倒数第几个的Fragment
     * @param index
     * @return
     */
    CustomFragment getFragmentByLastIndex(int index);

    /**
     * 设置是进行动画或者是拖动
     * @param isAnimating
     */
    void setAnimating(boolean isAnimating);

    /**
     * container 是否为空
     * @return
     */
    boolean isContainerNull();

    /**
     * 是否包含顶部Fragment
     * @return
     */
    boolean isContainTopFragment();
}
