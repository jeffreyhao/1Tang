package com.baidu.baselibrary.recycler;


import androidx.databinding.ViewDataBinding;

public interface OnItemClickListener<VB extends ViewDataBinding, T> {
    void onItemClick(BindingRecyclerAdapter.BindingViewHolder<VB, T> holder, T data, int position);
}
