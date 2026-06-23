package com.baidu.baselibrary.recycler;


import android.view.View;

import androidx.databinding.ViewDataBinding;


public interface OnItemChildClickListener<VB extends ViewDataBinding, T>  {

    /**
     *  item中的点击事件。
     *
     * @param holder    holder
     * @param view      点击的view
     *              注：这里避免使用 R.id.view_id，原因如下
     *              Resource IDs will be non-final by default in Android Gradle Plugin version 8.0, avoid using them in switch case statements.
     * @param action    点击动作
     * @param data      item数据
     * @param position  第几个
     */
    void onItemChildClick(BindingRecyclerAdapter.BindingViewHolder<VB, T> holder, View view, int action, T data, int position);
}
