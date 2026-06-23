package com.baidu.baselibrary.recycler;

import android.annotation.SuppressLint;

import com.baidu.baselibrary.util.glide.GlideUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by haojiangfeng on 2024/12/10.
 */
public class ScrollUtil {

    public static boolean showScrollLog = false;


    /**
     * setPauseOnScrollListener:设置滚动暂停监听. <br/>
     */
    @Deprecated
    public static void setPauseOnScrollListener(@NonNull RecyclerView view, RecyclerView.OnScrollListener customScrollListener) {
        PauseOnScrollListener pauseOnScrollListener = new PauseOnScrollListener(true, true, customScrollListener);
        view.addOnScrollListener(pauseOnScrollListener);
    }


    public static void pauseOrResumeLoadImgOnScrolling(int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            GlideUtils.resumeAllRequests("BookStore.RecyclerView.SCROLL_STATE_IDLE");
        } else {
            GlideUtils.pauseAllRequests("BookStore.RecyclerView.SCROLL_STATE_NOT_IDLE");
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public static void notifyDataSetChangedSafely(RecyclerView recyclerView){
        if(recyclerView == null || recyclerView.getAdapter() == null){
            return;
        }
        if (!recyclerView.isComputingLayout() && !recyclerView.isAnimating()) {
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            recyclerView.post(() -> notifyDataSetChangedSafely(recyclerView));
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public static void notifyDataSetChangedSafely(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        if(recyclerView == null && adapter == null){
            return;
        }
        if(recyclerView == null){
            adapter.notifyDataSetChanged();
            return;
        }
        RecyclerView.Adapter targetAdapter = adapter == null ? recyclerView.getAdapter() : adapter;
        if(targetAdapter == null){
            return;
        }
        if (!recyclerView.isComputingLayout() && !recyclerView.isAnimating()) {
            targetAdapter.notifyDataSetChanged();
        } else {
            recyclerView.post(() -> notifyDataSetChangedSafely(recyclerView, adapter));
        }
    }

}
