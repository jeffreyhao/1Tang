package com.baidu.baselibrary.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.baidu.baselibrary.util.App;
import com.base.util.ui.UIUtil;
import com.jess.baselibrary.R;

import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * 带有ViewPager.Adapter的手势布局
 */
@SuppressWarnings("UnusedParameters")
public class GestureLayoutPager extends FrameLayout {


    public static int MIN_VERSION_GRADIENT = 11;
    /**
     * 当前处于滑动初始状态
     */
    public static final int SCROLL_STATE_IDLE = 0;

    /**
     * 当前处于滑动
     */
    public static final int SCROLL_STATE_DRAGGING = 1;

    /**
     * 当前处于滑动到某个页面的状态
     */
    public static final int SCROLL_STATE_SETTLING = 2;

    protected static final int TOUCH_STATE_REST = 0;
    protected static final int TOUCH_STATE_SCROLLING = 1;
    protected static final int GRADIENT = 2;
    private static final int MAX_SETTLE_DURATION = 600; // ms
    public static final int LEFT_OPENED = 0;
    public static final int RIGHT_OPENED = 0;
    public static final int BOOKSHELF_OPENED = 0;
    protected int mTouchState;
    private int mTouchSlop;
    /**
     * 监听的触摸x坐标
     */
    protected float mLastMotionX;
    protected Scroller mScroller;
    /**
     * 起点坐标
     */
    private Point mStartPoint;
    private Point mMovePoint;

    private VelocityTracker mVelocityTracker;

    private static boolean mIsEnable = true;
    private static boolean mCanScrollRight = true;
    private static boolean mCanScrollLeft = true;
    private boolean mLock = false;
    private boolean isHardware = false;
    private int mCurrentIndex = 0;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private boolean mIsFristLayout = true;
    private int mScrollState = SCROLL_STATE_IDLE;
    private PagerAdapter mAdapter;


    public GestureLayoutPager(Context context) {
        super(context);
        init(context, null, 0);
    }

    public GestureLayoutPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public GestureLayoutPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }


    private void init(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GestureLayoutPager, defStyle, 0);
            mCurrentIndex = a.getInt(R.styleable.GestureLayoutPager_initPageIndex, 0);
            a.recycle();
        }
        mScroller = new Scroller(context, sInterpolator);
        mTouchState = TOUCH_STATE_REST;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mStartPoint = new Point();
        mMovePoint = new Point();
    }



    public static void setIsEnable(boolean isEnable) {
        mIsEnable = isEnable;
    }

    public static void setCanGestureLeftScroll(boolean canLeftScroll) {
        mCanScrollLeft = canLeftScroll;
    }

    public static void setCanGestureRightScroll(boolean canRightScroll) {
        mCanScrollRight = canRightScroll;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            scrollTo(currX, 0);
            ViewCompat.postInvalidateOnAnimation(this);
            if (!pageScrolled(currX)) {
                mScroller.abortAnimation();
                scrollTo(0, 0);
            }
            if (mScroller.isFinished()) {
                ViewCompat.postOnAnimation(this, new Runnable() {
                    @Override
                    public void run() {
                        setScrollState(SCROLL_STATE_IDLE);
                        if (isHardware) {
                            enableHardwareOrSoftwareAccelerateBuffer(false);
                        }
                    }
                });
            }
        }
        super.dispatchDraw(canvas);

    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return !((child.getRight() - getScrollX() <= 0) || (child.getLeft() - getScrollX() >= getMeasuredWidth())) && super.drawChild(canvas, child, drawingTime);
    }

    //如果滑动或动画过程中，触发了layout，请在此打断点然后跟踪是谁触发的。
    @Override
    public void requestLayout() {
        super.requestLayout();
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        int childCount = getChildCount();
        int leftOffset = getPaddingLeft();//  modify by caoxinyu  --增加左边距的支持
        for (int i = 0; i < childCount; i++) {
            View chlidView = getChildAt(i);
            if (chlidView.getVisibility() != View.GONE) {
                chlidView.layout(leftOffset, 0, leftOffset + chlidView.getMeasuredWidth(), getMeasuredHeight());
                leftOffset += chlidView.getMeasuredWidth();
                leftOffset += getPaddingLeft();//  modify by caoxinyu  --增加左边距的支持
            }
        }
        if (mIsFristLayout) {
            scrollTo(mCurrentIndex * getMeasuredWidth(), 0);
            pageScrolled(mCurrentIndex * getMeasuredWidth());
            post(new Runnable() {
                @Override
                public void run() {
                    if (mOnPageChangeListener != null) {
                        mOnPageChangeListener.onPageSelected(mCurrentIndex);
                    }
                }
            });
        }
        mIsFristLayout = false;
    }

    //分屏转屏后layout位置不对  需要再次layout
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mIsFristLayout = true;
    }

    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    public void setAdapter(PagerAdapter adapter) {
        mAdapter = adapter;
        removeAllViews();
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            mAdapter.instantiateItem(this, i);
        }
    }


    public PagerAdapter getAdapter() {
        return mAdapter;
    }

    private void enableHardwareOrSoftwareAccelerateBuffer(boolean isEnable) {
        //这里除了包含有WebView的控件不能开启硬件离屏缓冲以外，为了性能其他控件都应开启。
        isHardware = isEnable;
        final int layerType = isEnable ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).setLayerType(layerType, null);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        onIntercept(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            float x = ev.getX();
            float y = ev.getY();
            mStartPoint.x = (int) x;
            mStartPoint.y = (int) y;
            mLock = false;
        }

        if (!mIsEnable) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_UP) {
                mIsEnable = true;
            }
            return super.onInterceptTouchEvent(ev);
        }
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }
        float x = ev.getX();
        ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                if (!mScroller.isFinished()) stopAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                mMovePoint.x = (int) x;
                mMovePoint.y = (int) ev.getY();
                int slop = UIUtil.calculateA2B(mStartPoint, mMovePoint);
                float grad = UIUtil.calculateGradient(mStartPoint, mMovePoint);
                if ((x > mLastMotionX && !mCanScrollRight) || (x < mLastMotionX) && !mCanScrollLeft) {
                    return false;
                }

                if (!mLock && slop >= mTouchSlop) {
                    if (Math.abs(grad) > GRADIENT) {
                        mLastMotionX = x;
                        mTouchState = TOUCH_STATE_SCROLLING;
                    } else if (mTouchState != TOUCH_STATE_SCROLLING && Math.abs(grad) < 0.8f) {
                        mLock = true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        mLastMotionX = x;
        return (mTouchState != TOUCH_STATE_REST) && !mLock;
    }

    private void onIntercept(MotionEvent event) {
        boolean enableLeft = false; //默认事件不拦截
        boolean enableRight = false; //默认事件不拦截
        float eventFloatX = event.getX();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            App.setEnableScrollToLeft(false);
            App.setEnableScrollToRight(false);
        } else if (action == MotionEvent.ACTION_MOVE) {
            int deltaX = (int) (mLastMotionX - eventFloatX);
            int ScrollX = getScrollX();
            if (ScrollX <= 0 && deltaX < 0) {//向右
                enableRight = true; //上层把事件拦截
            } else if ((ScrollX >= getWidth() * (getChildCount() - 1)) && deltaX > 0) {//向左
                enableLeft = true;
            }
            App.setEnableScrollToLeft(enableLeft);
            App.setEnableScrollToRight(enableRight);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onIntercept(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLock = false;
        }
        if (!mIsEnable) {
            if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                mIsEnable = true;
            }
            return super.onTouchEvent(event);
        }

        float eventFloatX = event.getX();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) stopAnimation();
                mLastMotionX = eventFloatX;
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                    mVelocityTracker.addMovement(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (mLastMotionX - eventFloatX);

                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mVelocityTracker.addMovement(event);
                float sx = getScrollX();
                if (sx + deltaX <= getMeasuredWidth() * (getChildCount() - 1) &&
                        sx + deltaX >= 0) {
                    if (!isHardware) {
                        enableHardwareOrSoftwareAccelerateBuffer(true);
                    } else {
                        scrollBy(deltaX, 0);
                        ViewCompat.postInvalidateOnAnimation(this);
                        setScrollState(SCROLL_STATE_DRAGGING);
                        pageScrolled(getScrollX());
                    }
                }
                mLastMotionX = eventFloatX;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                int velocityX = 0;
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    velocityX = (int) mVelocityTracker.getXVelocity();
                }
                animationEnd(eventFloatX, velocityX);
                break;
            default:
                break;
        }
        return true;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public void setCurrentItem(int gotoIndex, boolean isSmoothScroll) {
        if (gotoIndex < 0 || gotoIndex >= getChildCount()) return;
        mCurrentIndex = gotoIndex;
        if (mIsFristLayout) {
            requestLayout();
        } else {
            int des = gotoIndex * getMeasuredWidth();
            if (isSmoothScroll) {
                startScroll(getScrollX(), des - getScrollX(), 0);
            } else {
                scrollTo(des, 0);
                pageScrolled(des);
            }
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageSelected(mCurrentIndex);
            }
        }
    }

    public void setCurrentItem(int gotoIndex) {
        setCurrentItem(gotoIndex, true);
    }

    /**
     * 清理Scroller 状态
     */
    private void cleanState() {
        mTouchState = TOUCH_STATE_REST;
        if (mVelocityTracker != null) mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    private void animationEnd(float eventFloatX, int velocityX) {
        cleanState();
        int width = getMeasuredWidth();
        int currScrollX = getScrollX();
        int absVelocitX = Math.abs(velocityX);

        int ScaledMinimumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        // 1. 速率判断
        if (absVelocitX > (ScaledMinimumFlingVelocity << 1)) {
            if (velocityX > 0) { // 向右移动
                if (currScrollX < mCurrentIndex * width) {
                    scrollToItem(mCurrentIndex - 1, velocityX);
                    return;
                } else {
                    scrollToItem(mCurrentIndex, velocityX);
                }
                return;
            } else if (velocityX < 0) {//向左移动
                if (currScrollX < mCurrentIndex * width) {
                    scrollToItem(mCurrentIndex, velocityX);
                    return;
                } else {
                    scrollToItem(mCurrentIndex + 1, velocityX);
                }
                return;
            }
        }

        if (currScrollX % width >= width * 0.5) {
            if (currScrollX < mCurrentIndex * width) {
                scrollToItem(mCurrentIndex, velocityX);
                return;
            } else {
                scrollToItem(mCurrentIndex + 1, velocityX);
            }
            return;
        } else if (currScrollX % width < width * 0.5) {
            if (currScrollX < mCurrentIndex * width) {
                scrollToItem(mCurrentIndex - 1, velocityX);
                return;
            } else {
                scrollToItem(mCurrentIndex, velocityX);
            }
        }
    }

    private void scrollToItem(int itemIndex, int velocitX) {
        if (itemIndex < 0 || itemIndex >= getChildCount()) return;
        int des = itemIndex * getMeasuredWidth();
        int curX = getScrollX();
        if (mCurrentIndex != itemIndex) {
            mCurrentIndex = itemIndex;
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageSelected(mCurrentIndex);
            }
        }
        if (des != curX) {
            startScroll(curX, des - curX, Math.abs(velocitX));
        } else {
            if (isHardware) {
                enableHardwareOrSoftwareAccelerateBuffer(false);
            }
        }
    }

    protected void stopAnimation() {
        if (mScroller != null && !mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    public void startScroll(int startX, int dx, int absVelocitX) {
        if (dx != 0) {
            if (!isHardware) {
                enableHardwareOrSoftwareAccelerateBuffer(true);
            }
            int duration;
            final int width = getMeasuredWidth();
            final int halfWidth = width / 2;
            final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
            final float distance = halfWidth + halfWidth *
                    UIUtil.distanceInfluenceForSnapDuration(distanceRatio);

            if (absVelocitX > 0) {
                duration = 4 * Math.round(1000 * Math.abs(distance / absVelocitX));
            } else {
                duration = MAX_SETTLE_DURATION;
            }
            duration = Math.min(duration, MAX_SETTLE_DURATION);
            mScroller.startScroll(startX, 0, dx, 0, duration);
            ViewCompat.postInvalidateOnAnimation(this);
            setScrollState(SCROLL_STATE_SETTLING);
        }
    }

    private void setScrollState(int newState) {
        if (mScrollState == newState) {
            return;
        }
        mScrollState = newState;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(newState);
        }
    }

    private boolean pageScrolled(int xpos) {
        final int width = getMeasuredWidth();

        if (getChildCount() == 0 || width == 0) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrolled(0, 0, 0);
            }
            return false;
        }

        final int currentPage = getScrollX() / width;
        final int offsetPixels = (getScrollX() % width);
        final float pageOffset = (float) offsetPixels / width;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(currentPage, pageOffset, offsetPixels);
        }
        return true;
    }

    public int getCurrentItem() {
        return mCurrentIndex;
    }
}
