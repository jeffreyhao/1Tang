package com.meta.ui.widget;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.baidu.baselibrary.Widget;

/**
 * Description: 分隔线
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    //一行几列
    private int spanCount;
    //间距
    private int spacing;
    //是否包含边缘
    private boolean includeEdge;
    private int topSpacing;

    public GridSpacingItemDecoration(int spanCount, int spacing, int topSpacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
        this.topSpacing = topSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        // item所在的列数
        int column = parent.getChildAdapterPosition(view) % spanCount;

        outRect.top = Widget.dip2px(15);
        if (column == 0) {
            outRect.left = Widget.dip2px(20);
            outRect.right = Widget.dip2px(8);
        } else {
            outRect.left = Widget.dip2px(8);
            outRect.right = Widget.dip2px(20);
        }

//        int childPosition = parent.getChildAdapterPosition(view);
//        int childCount = parent.getChildCount();
//        if(childPosition>childCount-2) {
//            outRect.bottom = Widget.dip2px(16);
//        }

    }
}
