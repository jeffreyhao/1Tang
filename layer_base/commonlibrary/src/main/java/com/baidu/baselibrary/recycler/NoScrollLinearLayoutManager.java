package com.baidu.baselibrary.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by haojiangfeng on 2025/6/18.
 */
public class NoScrollLinearLayoutManager extends LinearLayoutManager {


    private boolean horizonEnable = false;  // 禁止水平滑动
    private boolean verticalEnable = false; // 禁止垂直滑动


    public NoScrollLinearLayoutManager(Context context) {
        super(context);
    }
    public NoScrollLinearLayoutManager(Context context, @RecyclerView.Orientation int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
    public NoScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public NoScrollLinearLayoutManager(Context context, boolean horizonEnable, boolean verticalEnable) {
        super(context);
        this.horizonEnable = horizonEnable;
        this.verticalEnable = verticalEnable;
    }

    public NoScrollLinearLayoutManager(Context context, @RecyclerView.Orientation int orientation, boolean reverseLayout, boolean horizonEnable, boolean verticalEnable) {
        super(context, orientation, reverseLayout);
        this.horizonEnable = horizonEnable;
        this.verticalEnable = verticalEnable;
    }

    public NoScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, boolean horizonEnable, boolean verticalEnable) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.horizonEnable = horizonEnable;
        this.verticalEnable = verticalEnable;
    }



    @Override
    public boolean canScrollHorizontally() {
        return horizonEnable && super.canScrollHorizontally();
    }

    @Override
    public boolean canScrollVertically() {
        return verticalEnable && super.canScrollVertically();
    }

}