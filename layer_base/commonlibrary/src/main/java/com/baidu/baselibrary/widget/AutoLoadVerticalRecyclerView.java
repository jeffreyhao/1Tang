package com.baidu.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.baidu.baselibrary.util.sys.LogUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AutoLoadVerticalRecyclerView extends VerticalRecyclerView {
    private int mOnceRequestLimit = 10;
    private int mPage = 1;
    private Map<Integer,RequestStatus> mRequestStatusMap = new HashMap<>();
    public AutoLoadVerticalRecyclerView(@NonNull Context context) {
        super(context);
        addScrollListener();
    }

    public AutoLoadVerticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addScrollListener();
    }

    private void addScrollListener() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if(layoutManager!=null) {
                        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                        if((lastVisibleItemPosition+1)==mPage*mOnceRequestLimit-mOnceRequestLimit/2) {
                            if(loadMoreListener!=null) {
                                if((lastVisibleItemPosition/mOnceRequestLimit)+1==mPage
                                        && !mRequestStatusMap.containsKey(mPage+1)) {

                                    // put - LOADING  放在 loadMore() 回调里，真正加载更多的时候再设置状态
                                    // 因为页面初次加载就会调到这里，导致 mPage=2 一上来就处于 LOADING 状态
//                                    mRequestStatusMap.put(mPage+1, RequestStatus.LOADING);
                                    LogUtil.d("autoLoadMore==>" + (mPage+1));
                                    loadMoreListener.loadMore();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void setOnceRequestLimit(int onceRequestLimit) {
        this.mOnceRequestLimit = onceRequestLimit;
    }

    public void setCurPage(int page) {
        this.mPage = page;
        if(page==1) {
            mRequestStatusMap.clear();
        }
    }

    public void finishRequest(int page) {
        this.mPage = page;
        mRequestStatusMap.put(page, RequestStatus.FINISH);
    }

    public boolean isRequestPageInit(int page) {
        return !mRequestStatusMap.containsKey(page);
    }

    public void requestPage(int page) {
        mRequestStatusMap.put(page, RequestStatus.LOADING);
    }

    public void requestFail(int page) {
        mRequestStatusMap.remove(page);
    }

    enum RequestStatus {
        LOADING, FINISH
    }

    private LoadMoreListener loadMoreListener;
    public interface LoadMoreListener {
        void loadMore();
    }

    public void setOnLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
