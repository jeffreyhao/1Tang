package com.baidu.baselibrary.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.jess.baselibrary.R;

public abstract class BindingRecyclerAdapter<VB extends ViewDataBinding, T> extends RecyclerView.Adapter<BindingRecyclerAdapter.BindingViewHolder<VB, T>> {

    public static class BindingViewHolder<VB, T> extends RecyclerView.ViewHolder {
        private VB viewDataBinding;
        private T data;
        private int index;
        public BindingViewHolder(View itemView) {
            super(itemView);
        }

        public VB getViewDataBinding() {
            return viewDataBinding;
        }

        public void setViewDataBinding(VB viewDataBinding) {
            this.viewDataBinding = viewDataBinding;
        }

        public void setData(T data){
            this.data = data;
        }

        public T getData(){
            return data;
        }

        public void setIndex(int index){
            this.index = index;
        }

        public int getIndex(){
            return index;
        }
    }




    protected Context mContext;
    protected RecyclerView mRecyclerView;
    protected int layoutId;
    @NonNull
    protected final List<T> mDataList = new ArrayList<>();

    protected OnItemClickListener<VB, T> mItemClickListener;
    protected OnItemChildClickListener<VB, T> mItemChildClickListener;



    public BindingRecyclerAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    public BindingRecyclerAdapter(int layoutId, List<T> dataList) {
        this.layoutId = layoutId;
        if(dataList != null){
            this.mDataList.addAll(dataList);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<T> dataList) {
        mDataList.clear();
        if(dataList != null){
            mDataList.addAll(dataList);
        }
        notifyDataSetChangedSafely();
    }

    public void addData(List<T> dataList) {
        if(dataList != null && dataList.size() > 0) {
            int preSize = this.mDataList.size();
            this.mDataList.addAll(dataList);
            notifyItemRangeInserted(preSize, dataList.size());
        }
    }

    public void insert(T item) {
        if(item != null) {
            this.mDataList.add(item);
            notifyItemInserted(mDataList.size() - 1);
        }
    }

    public void remove(int position) {
        if(position < mDataList.size()){
            mDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void update(int position){
        if(position < mDataList.size()){
            notifyItemChanged(position);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear(){
        mDataList.clear();
        notifyDataSetChangedSafely();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void notifyDataSetChangedSafely() {
        if(mRecyclerView == null){
            return;
        }
        if (!mRecyclerView.isComputingLayout() && !mRecyclerView.isAnimating()) {
            notifyDataSetChanged();
        } else {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }


    @NonNull
    public List<T> getData() {
        return mDataList;
    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    public void setOnItemClickListener (OnItemClickListener<VB, T> listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemChildClickListener (OnItemChildClickListener<VB, T> listener) {
        this.mItemChildClickListener = listener;
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////




    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
        this.mContext = recyclerView.getContext();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public BindingViewHolder<VB, T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VB viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
        BindingViewHolder<VB, T> viewHolder = new BindingViewHolder<>(viewDataBinding.getRoot());
        viewHolder.setViewDataBinding(viewDataBinding);
        return viewHolder;
    }

    /*
     *  Do not treat position as fixed; only use immediately and call holder.getAdapterPosition() to look it up later
     */
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final BindingViewHolder<VB, T> holder, int position) {
        T data = mDataList.get(position);
        holder.setData(data);
        holder.setIndex(position);
        ViewDataBinding binding = holder.getViewDataBinding();
        binding.executePendingBindings();
        binding.getRoot().setTag(R.id.id_recycler_position, position);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem(holder, data, position);
            }
        });
        handleView(holder, data, position);
    }


    @CallSuper
    protected void clickItem(BindingViewHolder<VB, T> holder, T data, int position) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(holder, data, position);
        }
    }


    /**
     * @param action 点击行为。 注：这里使用 action 代替 R.id.view_id。原因如下：
     *        Resource IDs will be non-final by default in Android Gradle Plugin version 8.0, avoid using them in switch case statements.
     */
    @CallSuper
    protected void performChildClick(BindingViewHolder<VB, T> holder, View view, int action, T data, int position) {
        if(mItemChildClickListener != null) {
            mItemChildClickListener.onItemChildClick(holder, view, action, data, position);
        }
    }


    protected abstract void handleView(BindingViewHolder<VB, T> holder, T data, int position);




}