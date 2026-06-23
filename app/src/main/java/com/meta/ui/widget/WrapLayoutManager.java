package com.meta.ui.widget;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

/**
* 避免java.lang.IndexOutOfBoundsException: Inconsistency detected.问题
* @author adison
* @date 2017/7/1
* @time 下午4:32
*/
public class WrapLayoutManager extends LinearLayoutManager {
    public WrapLayoutManager(Context context) {
        super(context);
    }

    public WrapLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
