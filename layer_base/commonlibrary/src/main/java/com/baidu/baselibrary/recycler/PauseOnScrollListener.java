/**
 * File Name:PauseOnScrollListener.java <br/>
 * Package Name:com.zhangyue.iReader.cache.extend <br/>
 * Date:2015年6月1日<br/>
 * Copyright (c) 2015, zy All Rights Reserved.
 */

package com.baidu.baselibrary.recycler;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.baidu.baselibrary.util.glide.GlideUtils;
import com.base.api.GlobalContext;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * ClassName:PauseOnScrollListener <br/>
 * 滚动暂停监听
 * 
 * <pre>
 * 从而保证滚动顺滑度
 * 通过列表views的{@link AbsListView#setOnScrollListener(OnScrollListener) }设置该类
 * 同时该类可以包装你自己的
 * {@link OnScrollListener}.
 */
public class PauseOnScrollListener extends RecyclerView.OnScrollListener {


    /** 滚动时是否暂停 **/
    private boolean isPauseOnScroll;

    /** 快速滚动时是否暂停 **/
    private boolean isPauseOnFling;

    /** 包装的滚动监听器 **/
    private RecyclerView.OnScrollListener mExternalListener;



    /**
     * Creates a new instance of PauseOnScrollListener.
     *
     * @param pauseOnScroll 滚动时是否暂停图片加载
     * @param pauseOnFling 快速滚动时是否暂停图片加载
     */
    public PauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        this(pauseOnScroll, pauseOnFling, null);
    }

    /**
     * Creates a new instance of PauseOnScrollListener.
     *
     * @param pauseOnScroll 滚动时是否暂停图片加载
     * @param pauseOnFling 快速滚动时是否暂停图片加载
     * @param customListener  需要包装的滚动监听器 
     */
    public PauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling, RecyclerView.OnScrollListener customListener) {
        this.isPauseOnScroll = pauseOnScroll;
        this.isPauseOnFling = pauseOnFling;
        this.mExternalListener = customListener;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        switch(newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                GlideUtils.resumeAllRequests("RecyclerView.SCROLL_STATE_IDLE");
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                if(isPauseOnScroll) {
                    GlideUtils.pauseAllRequests("RecyclerView.SCROLL_STATE_DRAGGING");
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                if(isPauseOnFling) {
                    GlideUtils.pauseAllRequests("RecyclerView.SCROLL_STATE_SETTLING");
                }
                break;
        }
        if(mExternalListener != null) {
            mExternalListener.onScrollStateChanged(recyclerView, newState);
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if(mExternalListener != null) {
            mExternalListener.onScrolled(recyclerView, dx, dy);
        }
    }



}
