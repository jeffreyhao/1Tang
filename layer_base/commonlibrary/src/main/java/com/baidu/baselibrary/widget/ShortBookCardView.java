package com.baidu.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


/**
 * 自定义短篇阅读卡片容器，解决 ViewPager2 嵌套滑动冲突
 *
 * <ol>
 *   <li>非展开模式下，将 item 高度限制为父容器的 70% </li>
 *   <li>展开模式下，处理 ViewPager2 与内部 RecyclerView 的同向嵌套滑动冲突</li>
 *   <li>根据滑动方向和子 View 是否可滚动，动态控制父 ViewPager2 的拦截行为</li>
 * </ol>
 */
public class ShortBookCardView extends RelativeLayout {

    private int touchSlop;
    private float initialX = 0f;
    private float initialY = 0f;
    private float itemHeightRate;

    private boolean needInterceptTouchEvent;
    private boolean needMeasureCard;

    public ShortBookCardView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ShortBookCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShortBookCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ShortBookCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.itemHeightRate = 0.7f;
    }

    /**
     *  设置是否需要拦截 touch
     */
    public void setNeedInterceptTouch(boolean isNeed) {
        this.needInterceptTouchEvent = isNeed;
    }

    /**
     *  设置是否需要测量尺寸
     */
    public void setNeedMeasureCard(boolean isNeed) {
        if(needMeasureCard != isNeed) {
            this.needMeasureCard = isNeed;
            requestLayout();
        }
    }

    /**
     * 设置 item 的高度比例
     */
    public void setItemHeightRate(float itemHeightRate) {
        if(this.itemHeightRate != itemHeightRate) {
            this.itemHeightRate = itemHeightRate;
            requestLayout();
        }
    }

    /**
     * @return item 的高度比例
     */
    public float getItemHeightRate() {
        return itemHeightRate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(needMeasureCard) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
            int desiredHeight = (int) (parentHeight * itemHeightRate);
            int newHeightSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, newHeightSpec);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(needInterceptTouchEvent) {
            // 展开时，要 解决 ViewPager2 与 cardView内部的 RecyclerView 同向嵌套滑动冲突
            handleInterceptTouchEvent(e);
        } else {
            // 不展开时内部 RecyclerView 为 GONE，允许父 ViewPager2 正常拦截触摸事件
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.onInterceptTouchEvent(e);
    }

    private void handleInterceptTouchEvent(MotionEvent e) {
        View parent = findParentScrollable();
        if (parent == null) return;

        boolean isVertical = isParentVertical(parent);

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            initialX = e.getX();
            initialY = e.getY();
            getParent().requestDisallowInterceptTouchEvent(true);

        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = e.getX() - initialX;
            float dy = e.getY() - initialY;

            float absDx = Math.abs(dx);
            float absDy = Math.abs(dy);

            if (absDx > touchSlop || absDy > touchSlop) {

                if (isVertical && absDy > absDx) {   // 父容器竖直滑动，且当前是竖直滑动
                    if (dy > 0 && isParentRVOffset(parent)) {
                        // 场景3：外层 RV 有偏移 + 用户下滑 → 先让外层 RV 恢复
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (canChildScrollVertically(dy)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }

                } else if (!isVertical && absDx > absDy) {  // 父容器横向滑动，且当前是横向滑动
                    if (canChildScrollHorizontally(dx)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }

                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
        }
    }

    // 获取外层可滑动的父容器（ViewPager2 或 RecyclerView）
    private View findParentScrollable() {
        View v = (View) getParent();
        while (v != null) {
            if (v instanceof ViewPager2 || v instanceof RecyclerView) return v;
            v = (View) v.getParent();
        }
        return null;
    }

    private boolean isParentVertical(View parent) {
        if (parent instanceof ViewPager2) {
            return ((ViewPager2) parent).getOrientation() == ViewPager2.ORIENTATION_VERTICAL;
        } else if (parent instanceof RecyclerView) {
            RecyclerView.LayoutManager lm = ((RecyclerView) parent).getLayoutManager();
            if (lm instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) lm).getOrientation() == LinearLayoutManager.VERTICAL;
            }
        }
        return true;
    }

    private View getChildView() {
        return getChildCount() > 0 ? getChildAt(0) : null;
    }

    private boolean canChildScrollVertically(float delta) {
        int direction = (int) -Math.signum(delta);
        View child = getChildView();
        return child != null && child.canScrollVertically(direction);
    }

    private boolean canChildScrollHorizontally(float delta) {
        int direction = (int) -Math.signum(delta);
        View child = getChildView();
        return child != null && child.canScrollHorizontally(direction);
    }

    /**
     * 检测外层 RecyclerView 当前第一个可见 item 是否有负偏移（即已上移）
     */
    private boolean isParentRVOffset(View parent) {
        if (parent instanceof RecyclerView) {
            RecyclerView rv = (RecyclerView) parent;
            RecyclerView.LayoutManager lm = rv.getLayoutManager();
            if (lm instanceof LinearLayoutManager) {
                int firstVisible = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();
                View firstChild = lm.findViewByPosition(firstVisible);
                return firstChild != null && firstChild.getTop() < 0;
            }
        }
        return false;
    }

}
