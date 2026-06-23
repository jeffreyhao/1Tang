package com.meta.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.common.util.ConvertUtils;

/**
 * 流式布局
 *
 * @author adison
 * @date 16/4/20
 * @time 下午7:57
 */

@SuppressWarnings("UnusedParameters")
public class ExpandableFlowLayout extends ViewGroup {

    @Deprecated
    public static final int FLOW_RIGHT              = 1;
    public static final int FLOW_LEFT               = 2;
    @Deprecated
    public static final int FLOW_MINE_HEAD          = 3;

    private int mFlowType                           = FLOW_LEFT;

    private int mVerticalSpacing;
    private int mHorizontalSpacing;

    private int mTotalMeasuredWidth;

    public ExpandableFlowLayout(Context context) {
        super(context);
    }

    public ExpandableFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setFlowType(int flowType) {
        mFlowType = flowType;
    }

    /**
     * 设置横向边距
     *
     * @param pixelSize
     */
    public void setHorizontalSpacing(int pixelSize) {
        mHorizontalSpacing = pixelSize;
    }

    /**
     * 设置纵向边距
     *
     * @param pixelSize
     */
    public void setVerticalSpacing(int pixelSize) {
        mVerticalSpacing = pixelSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mTotalMeasuredWidth = 0;
        int myWidth = resolveSize(0, widthMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int childLeft = paddingLeft;
        int childTop = paddingTop;

        int lineHeight = 0;

        //如果有足够空间，则测量每一个clild并且加到之前child的右边,否则换行
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            mTotalMeasuredWidth += childWidth;

            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > myWidth) {
                // 换行，重新赋值
                childLeft = paddingLeft;
                childTop += mVerticalSpacing + lineHeight;
                lineHeight = childHeight;
            }
            childLeft += childWidth + mHorizontalSpacing;
        }

        int wantedHeight = childTop + lineHeight + paddingBottom;

        setMeasuredDimension(myWidth, resolveSize(wantedHeight, heightMeasureSpec));
    }

    /**
     * 从左边开始排列
     */
    private void onFlowLeftLayout(boolean changed, int l, int t, int r, int b) {
        int myWidth = r - l;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int childLeft = paddingLeft;
        int childTop = paddingTop;
        int lineHeight = 0;

        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > myWidth) {
                childLeft = paddingLeft;
                childTop += mVerticalSpacing + lineHeight;
                lineHeight = childHeight;
            }

            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth + mHorizontalSpacing;
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        switch (mFlowType){
            case FLOW_LEFT:
                onFlowLeftLayout(changed, l, t, r, b);
                break;
            case FLOW_RIGHT:
                onFlowRightLayout(changed, l, t, r, b);
                break;
            case FLOW_MINE_HEAD:
                onFlowMineHead(changed, l, t, r, b);
                break;
        }
    }

    private void onFlowRightLayout(boolean changed, int l, int t, int r, int b) {
        int myWidth = r - l;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int childTop = paddingTop;
        int lineHeight = 0;


        int childLeft = paddingLeft;

        int childRight = myWidth - paddingRight;

        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);

            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            lineHeight = Math.max(childHeight, lineHeight);
            if (childRight - childWidth - paddingLeft < 0) {
                childRight = myWidth - paddingRight;
                childTop += mVerticalSpacing + lineHeight;
                lineHeight = childHeight;
            }
            childView.layout(childRight - childWidth, childTop, childRight, childTop + childHeight);
            childRight -= (childWidth + mHorizontalSpacing);
        }
    }

    /**
     * 我的页面顶部的排列适配
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    private void onFlowMineHead(boolean changed, int l, int t, int r, int b) {
        if(mTotalMeasuredWidth <= getMeasuredWidth()){
            onFlowLeftLayout(changed, l, t, r, b);
            return;
        }

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();

        int firstChildHeight = 0;
        int secondRowTChildTop = 0;
        int childLeft = paddingLeft;

        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            if(i == 0){
                firstChildHeight = childView.getMeasuredHeight();
                childView.layout(paddingLeft,
                                paddingTop,
                                getMeasuredWidth() - paddingLeft - paddingRight,
                                paddingTop + firstChildHeight);

            } else{
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();

                if(secondRowTChildTop == 0) {
                    secondRowTChildTop = paddingTop + firstChildHeight - ConvertUtils.dp2px(10);
                }

                childView.layout(childLeft, secondRowTChildTop, childLeft + childWidth, secondRowTChildTop + childHeight);
                childLeft += childWidth;
            }
        }
    }
}
