package com.baidu.baselibrary.fragment.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.baidu.baselibrary.fragment.anim.CustomAnimation;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.App;
import com.base.api.Logger;
import com.base.util.ui.UIUtil;
import com.baidu.baselibrary.fragment.CustomFragment;
import com.baidu.baselibrary.fragment.callback.OnFragmentStateChangeListener;
import com.jess.baselibrary.R;

import java.lang.reflect.Field;


/**
 * 可以无穷叠加的view容器，支持手势、动画等 => FragmentViewContainer
 */
@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "UnusedParameters"})
public class FragmentLayout extends PreDrawFrameLayout {

    private boolean mHLock = false;
    private boolean mVLock = false;
    public static final int MAX_SETTLE_DURATION = 400; // 单位ms
    public static int MAX_COVER_COLOR_ALPHA = 400; //蒙板最大的不透明度，1000为完全不透明
    private static final int MIN_VERSION_GRADIENT = 14;//蒙板所支持的最低版本，为android 4.0
    protected static final int TOUCH_STATE_REST = 0;
    protected static final int TOUCH_STATE_SCROLLING = 1;
    protected static final int GRADIENT = 4;
    protected int mTouchState;
    private int mTouchSlop;
    private boolean mIsHardWareLayer = false;
    private int mStartLeft = 0;
    private int mEndLeft = 0;
    private float mMaxVelocity;
    private float mMinVelocity;
    private boolean mChildVisiable = true; //用来代替setVisiable，除了顶部的控件外，底部的控件是否显示，解决某些机型调用setVisiable方法会引起重新布局
    /**
     * 监听的触摸x坐标
     */
    protected float mLastMotionX;

    protected ScrollAnimation mScroller;
    /**
     * 起点坐标
     */
    private Point mStartPoint;
    private Point mMovePoint;

    private VelocityTracker mVelocityTracker;
    public static final int START_ALPHA = 400;//初始拖动时，右控件的初始透明度40%

    private boolean mCanScroll = false;

    private boolean mEnableGesture = false;

    private boolean mCanScrollRight = true;

    private int mWidth;

    boolean mIsCanTouch = true;
    boolean mIsCanShowShodow = true;

    private int mCaptrueViewHashCode = -1;
    private boolean mIsNeedDownAction = false;
    private int mScrollDuration;
    private OnFragmentStateChangeListener mOnCoverFragmentSateChange;
    //    private int mFirstChildLeft = 0;
    private boolean mWindowsAnimatingFieldInited = false;
    private Field mWindowsAnimatingField;
    private View mActivityContentView;
    private int mActivityContentWidth;
    private int mActivityContentHeight;
    private View mNightView;

    private boolean mSkipDraw = false;
    private int mLeftMargin = 0;
    private ViewGroup contentView;


    boolean mIsGetFoucus = false;




    public FragmentLayout(Context context) {
        super(context);
        init(context);
    }

    public FragmentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FragmentLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public int getScrollDuration() {
        return mScrollDuration;
    }

    public void setCanScroll(boolean canScroll) {
        mCanScroll = canScroll;
    }

    public void setCanScrollLeft(boolean canScrollLeft) {
//        mCanScrollLeft = canScrollLeft;
    }

    public void enableGesture(boolean enable) {
        mEnableGesture = enable;
    }


    public void setCanScrollRight(boolean canScrollRight) {
        mCanScrollRight = canScrollRight;
    }


    public void attachToActivity(Activity activity) {
        ViewGroup contentView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if(contentView == null) return;
        ViewGroup contentViewParent = (ViewGroup) contentView.getParent();
        if (contentViewParent != null) {
            contentViewParent.removeView(contentView);
            this.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            contentViewParent.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

//    public void attachToActivity(Activity activity) {
//        ViewGroup contentViewParent = (ViewGroup) activity.getWindow().getDecorView();
//        ViewGroup child = (ViewGroup) contentViewParent.getChildAt(0);
//        if (contentViewParent != null) {
//            contentViewParent.removeView(child);
//            addView(child);
//        }
////        contentViewParent.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        contentViewParent.addView(this);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        boolean isNeedMeasureChild = false;
        if (width != getWidth() || height != getHeight() || (contentView != null && width != contentView.getMeasuredWidth()) || (contentView != null && height != contentView.getMeasuredHeight())) {
            isNeedMeasureChild = true;
        }
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (i == 0) {
                if ((isNeedMeasureChild || child.isLayoutRequested()) && child.getVisibility() != GONE) {
                    try {
                        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                    } catch (Exception e){
                        Log.e("绘制", "FragmentLayout==="+e.getMessage());
                        ALog.crash("FragmentLayout", "onMeasure", e);
                        Logger.sendWebHook(Thread.currentThread(), e, "FragmentLayout.onMeasure()");
                    }
                }
            } else {
                if ((isNeedMeasureChild || child.isLayoutRequested()) && child.getVisibility() != GONE) {
                    child.measure(MeasureSpec.makeMeasureSpec(getChildAt(0).getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(getChildAt(0).getMeasuredHeight(), MeasureSpec.EXACTLY));
                }
            }
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
        mWidth = getMeasuredWidth();
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (getChildCount() <= 0) {
            if (mNightView != null) {
                mNightView.layout(0, 0, 0, 0);
            }
            super.dispatchDraw(canvas);
            return;
        }
        if (hasNoWebView()) {
            mScroller.onCallDraw(this);
        }
        View child = getChildAt(getChildCount() - 1);
        float moveX;
        float moveY;
        if (hasNoWebView()) {
            moveX = child.getX();
            moveY = child.getTop();
        } else {
            moveX = -child.getScrollX();
            moveY = -child.getScrollY();
        }

        if (getChildCount() > 1) {
            if (moveX == 0 && moveY == 0 && mIsAnimEnd) {
                mChildVisiable = false;
            } else {
                mChildVisiable = true;
            }
        }
        super.dispatchDraw(canvas);
        if (!hasNoWebView()) {
            mScroller.onCallDraw(this);
        }
//        mFirstChildLeft = child.getX();

        if (getChildCount() == 1) {
            if (mNightView != null) {
                mNightView.layout(Math.round(moveX + mLeftMargin), 0, Math.round(moveX + mLeftMargin + mNightView.getWidth()), mNightView.getBottom());
            }
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret;
        int canvasRestore = -1;
        int index = indexOfChild(child);
        int count = getChildCount() - 1;
        if (index < count) {
            //只有最顶上的fragment是全屏的，才能不绘制底下的fragment
            if (!mChildVisiable
                    && mOnCoverFragmentSateChange.isContainTopFragment()
                    && mOnCoverFragmentSateChange.getTopFragment() != null
                    && mOnCoverFragmentSateChange.getTopFragment().isFullScreen()
                    && !(mOnCoverFragmentSateChange.getTopFragment().getView().getAnimation() != null
                    && !mOnCoverFragmentSateChange.getTopFragment().getView().getAnimation().hasEnded())) {
                return true;
            }
            if (mOnCoverFragmentSateChange.isContainTopFragment()) {
                float x;
                if (hasNoWebView()) {
                    x = getChildAt(count).getX();
                } else {
                    x = -getChildAt(count).getScrollX();
                }
                if (x != 0) {
                    canvasRestore = canvas.save();
                    canvas.clipRect(0, 0, x, getHeight());
                }
            }
        }
        ret = super.drawChild(canvas, child, drawingTime);
        if (canvasRestore != -1) {
            canvas.restoreToCount(canvasRestore);
        }
        return ret;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //转屏后加上刷新 个别大屏设备二级页面转屏后大概率只显示一半 或者压根看不见了
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        try {
            mLeftMargin = ((LayoutParams) ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).getChildAt(0).getLayoutParams()).leftMargin;
        } catch (Throwable e) {
            ALog.exception("FragmentLayout", "onLayout", e);
        }
        super.onLayout(changed, left, top, right, bottom);
    }


    protected void init(Context context) {
        mScroller = new ScrollAnimation();
        mScroller.setInterpolator(AnimationUtils.loadInterpolator(context, R.anim.interpolator_decelerate));
        mScroller.setAnimationListener(new CustomAnimation.AnimationListener() {
            @Override
            public void onAnimationStart(CustomAnimation animation) {

            }

            @Override
            public void onAnimationEnd(CustomAnimation animation) {
                if (mOnCoverFragmentSateChange != null) {
                    mOnCoverFragmentSateChange.setAnimating(false);
                }
                mIsCanTouch = true;
                mIsCanShowShodow = true;
                if (mEndLeft != 0 && getChildCount() != 0) {
                    App.postAtFrontOfQueue(new Runnable() {
                        @Override
                        public void run() {
                            if (getChildCount() != 0) {
                                removeViewInLayout(getChildAt(getChildCount() - 1));
                            }
                        }
                    });
                }
            }

            @Override
            public void onAnimationRepeat(CustomAnimation animation) {

            }

            @Override
            public void onAnimationCancel(CustomAnimation animation) {
                mIsCanTouch = true;
                mIsCanShowShodow = true;
            }
        });
        mTouchState = TOUCH_STATE_REST;
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop() * 3 / 2;
        mMaxVelocity = vc.getScaledMaximumFlingVelocity();
        mMinVelocity = vc.getScaledMinimumFlingVelocity();
        mStartPoint = new Point();
        mMovePoint = new Point();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mSkipDraw) {
            return true;
        }
        int childCount = getChildCount();
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mIsGetFoucus = false;
        }
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {//不支持多点触摸
            if (onInterceptTouchEvent(ev)) {//表明自己处理触摸事件
                //如果自己没有获取到触摸焦点，而是在move事件的时候获取，则应给之前获取到触摸焦点控件发送cancel事件
                if (!mIsGetFoucus && childCount != 0 && action == MotionEvent.ACTION_MOVE) {
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    cancelEvent.offsetLocation(-getChildAt(childCount - 1).getLeft(), -getChildAt(childCount - 1).getTop());
                    getChildAt(childCount - 1).dispatchTouchEvent(cancelEvent);
                }
                mIsGetFoucus = true;
                return onTouchEvent(ev);
            } else {//释放触摸焦点
                mIsGetFoucus = false;
            }
        } else if (mIsGetFoucus) {
            //如果自己已经或者触摸焦点了，而且事件又是非down和move(即可能是up、cancel、outside)
            //则该事件应交给自己
            return onTouchEvent(ev);
        }

        //由于触摸事件不能传递到非顶端控件，所以该触摸事件交给顶端的子控件处理
        if (childCount == 0) {
            return false;
        } else {
            boolean childDispatch;
            try {
                ev.offsetLocation(-getChildAt(childCount - 1).getLeft(), -getChildAt(childCount - 1).getTop());
                childDispatch = getChildAt(childCount - 1).dispatchTouchEvent(ev);
            } catch (Exception e) {
                ALog.exception("FragmentLayout", "dispatchTouchEvent", e);
                childDispatch = false;
            }
            if (childDispatch) {
                return true;
            } else {
                mIsGetFoucus = true;
                return true;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mOnCoverFragmentSateChange != null
                && mOnCoverFragmentSateChange.getTopFragment() != null
                && !mOnCoverFragmentSateChange.getTopFragment().enableScrollRight()) {
            return super.onInterceptTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mHLock = false;
            mVLock = false;
        }
        float x = ev.getX();
        float y = ev.getY();
        if (ev.getAction() == KeyEvent.ACTION_DOWN) {
            mLastMotionX = x;
            mStartPoint.x = (int) x;
            mStartPoint.y = (int) y;
            if (mScroller.isFinished()) {
                mTouchState = TOUCH_STATE_REST;
            } else {
                mCaptrueViewHashCode = getChildAt(getChildCount() - 1).hashCode();
                mIsNeedDownAction = true;
                mTouchState = TOUCH_STATE_SCROLLING;
            }
        }

        if (!mIsCanTouch || mOnCoverFragmentSateChange == null || getChildCount() == 0) {
            if (ev.getAction() != MotionEvent.ACTION_MOVE) {
                App.setEnableScrollToRight(true);
            }
            return super.onInterceptTouchEvent(ev);
        }

        if (!App.getEnableScrollToRigh() || !mCanScroll || !mEnableGesture) {
            if (ev.getAction() != MotionEvent.ACTION_MOVE) {
                App.setEnableScrollToRight(true);
            }
            return super.onInterceptTouchEvent(ev);
        }

        final int action = ev.getAction();
        float left;
        float deltaX = x - mLastMotionX;
        View child = getChildAt(getChildCount() - 1);
        //fix bug#58734
        if (child == null) {
            return true;
        }
        if (hasNoWebView()) {
            left = child.getX();
        } else {
            left = -child.getScrollX();
        }

        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST) && left + deltaX >= 0) {
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                mMovePoint.x = (int) x;
                mMovePoint.y = (int) y;

                int slop = UIUtil.calculateA2B(mStartPoint, mMovePoint);
                float grad = UIUtil.calculateGradient(mStartPoint, mMovePoint);
                if (!mHLock && slop >= mTouchSlop) {
                    if (Math.abs(grad) > GRADIENT) {
                        mLastMotionX = x;
                        if (getChildAt(getChildCount() - 1) != null) {
                            mCaptrueViewHashCode = getChildAt(getChildCount() - 1).hashCode();
                        }
                        mIsNeedDownAction = true;
                        if (left + deltaX <= 0) {
                            mTouchState = TOUCH_STATE_REST;
                        } else {
                            mTouchState = TOUCH_STATE_SCROLLING;
                        }
                    } else if (mTouchState != TOUCH_STATE_SCROLLING && Math.abs(grad) < 2F) {
                        mHLock = true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return (mTouchState != TOUCH_STATE_REST) && !mHLock;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventFloatX = event.getX();
        float eventFloatY = event.getY();
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mScroller != null && !mScroller.isFinished() && catchTopView(eventFloatX, eventFloatY)) {
                stopAnimation();
            }
            mLastMotionX = eventFloatX;
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
                mVelocityTracker.addMovement(event);
            }
        }

        if (!mIsCanTouch || mOnCoverFragmentSateChange == null || getChildCount() == 0) {
            if (event.getAction() != MotionEvent.ACTION_MOVE) {
                App.setEnableScrollToRight(true);
            }
            return super.onTouchEvent(event);
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mHLock = false;
            mVLock = false;
        }

        if (!App.getEnableScrollToRigh() || !mCanScroll || !mEnableGesture) {
            if (event.getAction() != MotionEvent.ACTION_MOVE) {
                App.setEnableScrollToRight(true);
            }
            return super.onTouchEvent(event);
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished() && catchTopView(eventFloatX, eventFloatY)) {
                    stopAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mMovePoint.x = (int) event.getX();
                mMovePoint.y = (int) event.getY();
                View mChild = getChildAt(getChildCount() - 1);
                if (!mScroller.isFinished() && !catchTopView(eventFloatX, eventFloatY)) {
                    return true;
                } else if (mTouchState != TOUCH_STATE_REST) {
                    if (mChild.hashCode() != mCaptrueViewHashCode) {
                        int slop = UIUtil.calculateA2B(mStartPoint, mMovePoint);
                        float grad = UIUtil.calculateGradient(mStartPoint, mMovePoint);
                        if (slop < mTouchSlop) {
                            return true;
                        } else {
                            if ((mVLock || Math.abs(grad) < 2)) {
                                if ((hasNoWebView() && mChild.getX() == 0) ||
                                        (!hasNoWebView() && mChild.getScrollX() == 0)) {
                                    mVLock = true;
                                    mLastMotionX = eventFloatX;
                                    if (mIsNeedDownAction) {
                                        mIsNeedDownAction = false;
                                        MotionEvent childEvent = MotionEvent.obtain(event);
                                        childEvent.setLocation(childEvent.getX() + mTouchSlop * 2, childEvent.getY());
                                        childEvent.setAction(MotionEvent.ACTION_DOWN);
                                        mChild.dispatchTouchEvent(childEvent);
                                        childEvent.recycle();
                                    }
                                    return mChild.dispatchTouchEvent(event);
                                }
                            }
                        }
                    } else {
                        mIsNeedDownAction = false;
                    }
                }
                float deltaX = eventFloatX - mLastMotionX;
                mLastMotionX = eventFloatX;

                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mVelocityTracker.addMovement(event);
                float left;
                float resultX;
                if (hasNoWebView()) {
                    left = mChild.getX();
                    resultX = left + deltaX;
                } else {
                    left = -mChild.getScrollX();
                    resultX = left + deltaX;
                }

                if (left >= 0 && resultX <= mWidth && resultX >= 0) {
                    if (deltaX != 0) {
                        if (mOnCoverFragmentSateChange != null) {
                            mOnCoverFragmentSateChange.setAnimating(true);
                        }
                        if (hasNoWebView()) {
                            mChild.setX(mChild.getX() + deltaX);
                        } else {
                            mChild.scrollBy((int) -deltaX, 0);
                        }
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                View child = getChildAt(getChildCount() - 1);
                int velocityX = 0;
                int velocityY = 0;
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    velocityX = (int) mVelocityTracker.getXVelocity();
                    velocityY = (int) mVelocityTracker.getYVelocity();
                }
                animationEnd(velocityX, velocityY);
                mTouchState = TOUCH_STATE_REST;
                if (mTouchState != TOUCH_STATE_REST) {
                    if (child.hashCode() != mCaptrueViewHashCode) {
                        boolean ret = child.dispatchTouchEvent(event);
                        return ret;
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean hasNoWebView() {
        return !(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP &&
                mOnCoverFragmentSateChange != null &&
                mOnCoverFragmentSateChange.getTopFragment() != null &&
                mOnCoverFragmentSateChange.getTopFragment().hasWebView());
    }

    @Override
    public void removeViewInLayout(final View view) {
        if (mOnCoverFragmentSateChange == null) {
            super.removeViewInLayout(view);
            invalidate();
            return;
        }
        int index = indexOfChild(view);
        if (index < 0) {
            invalidate();
            return;
        }

//        if (index == getChildCount() - 1) {
//            mFirstChildLeft = 0;
//        }
        try {
            super.removeViewInLayout(view);
        } catch (Throwable e) {
            ALog.exception("FragmentLayout", "removeViewInLayout", e);
        }
        if (getChildCount() > 0) {
            mChildVisiable = true;
        }
        mOnCoverFragmentSateChange.onDestroy(view);

        mIsCanTouch = true;
        mIsCanShowShodow = true;
        addOtherFragment();
        if(getChildCount() > 0) {
            View child = getChildAt(getChildCount() - 1);
            if(!child.hasFocus() && child.findFocus() == null) {
                child.requestFocus();
            }
        }
    }

    public void addOtherFragment() {
        int fragmentCount = mOnCoverFragmentSateChange.getFragmentCount();
        for (int i = Math.max(getChildCount() - 1, 1); i < fragmentCount; i++) {
            View bottomView = mOnCoverFragmentSateChange.getFragmentView(i);
            if (i == fragmentCount - 1 && !mOnCoverFragmentSateChange.isContainerNull()) break;
            CustomFragment fragment = mOnCoverFragmentSateChange.getFragmentByLastIndex(i);
            mChildVisiable = true;
            if (bottomView == null) return;
            if (bottomView.getParent() == null) {
                if (bottomView.isLayoutRequested()) {
                    addView(bottomView, 1);
                } else {
                    addViewInLayout(bottomView, 1, bottomView.getLayoutParams(), true);
                }
            }
            if (fragment != null && fragment.isFullScreen()) break;
        }
        invalidate();
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        child.forceLayout();
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        child.forceLayout();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        child.forceLayout();
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        child.forceLayout();
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        child.forceLayout();
    }

    @Override
    public boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if(!preventRequestLayout){
            child.forceLayout();
        }
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    /**
     * 清理Scroller 状态
     */
    protected void cleanState() {
        mTouchState = TOUCH_STATE_REST;
        if (mVelocityTracker != null) mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    private boolean catchTopView(float x, float y) {
        final int childCount = getChildCount();
        if (hasNoWebView()) {
            return (x > Math.abs(getChildAt(childCount - 1).getX()));
        } else {
            return (x > Math.abs(getChildAt(childCount - 1).getScrollX()));
        }
    }

    private void animationEnd(int velocityX, int velocityY) {
        cleanState();
        if (getChildCount() <= 0) return;
        int currScrollX;
        if (hasNoWebView()) {
            currScrollX = Math.round(getChildAt(getChildCount() - 1).getX());
        } else {
            currScrollX = getChildAt(getChildCount() - 1).getScrollX();
        }
        int absVelocityX = Math.abs(velocityX);

        int ScaledMinimumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        // 1. 速率判断
        if (absVelocityX > (ScaledMinimumFlingVelocity << 1)) {
            if (velocityX > 0) { // 向右移动
                if (hasNoWebView()) {
                    startScroll(currScrollX, mWidth - currScrollX, velocityX, velocityY);
                } else {
                    startScroll(currScrollX, -mWidth - currScrollX, velocityX, velocityY);
                }
                return;
            } else if (velocityX < 0) {//向左移动
                startScroll(currScrollX, -currScrollX, velocityX, velocityY);
                return;
            }
        }

        if (currScrollX > getMeasuredWidth() * 0.5) {
            if (hasNoWebView()) {
                startScroll(currScrollX, mWidth - currScrollX, velocityX, velocityY);
            } else {
                startScroll(currScrollX, -mWidth - currScrollX, velocityX, velocityY);
            }
        } else {
            startScroll(currScrollX, -currScrollX, velocityX, velocityY);
        }
        return;
    }

    protected void stopAnimation() {
        if (mScroller != null && !mScroller.isFinished()) {
            mScroller.cancel();
        }
    }

    private void startScroll(int startX, int dx, int velocityX, int velocityY) {
        velocityX = clampMag(velocityX, (int) mMinVelocity, (int) mMaxVelocity);
        if (dx != 0) {
            if (mOnCoverFragmentSateChange != null) {
                mOnCoverFragmentSateChange.setAnimating(true);
            }
            mStartLeft = startX;
            mEndLeft = startX + dx;
            int duration;
            int width = getMeasuredWidth();
            int maxTime = MAX_SETTLE_DURATION;
            if (width == 0) {
                mScroller.setDuration(MAX_SETTLE_DURATION);
                mScroller.start();
                mScrollDuration = MAX_SETTLE_DURATION;
                invalidate();
                return;
            }
            if (startX <= 0 && (startX + dx == getWidth() || startX + dx == 0)) {
                maxTime = MAX_SETTLE_DURATION * width / width;
            }

            final int halfWidth = width / 2;
            final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
            final float distance = halfWidth + halfWidth * UIUtil.distanceInfluenceForSnapDuration(distanceRatio);

            if (velocityX > 0) {
                duration = 3 * Math.round(1000 * Math.abs(distance / velocityX));
            } else {
                duration = Math.abs(dx) * 3;
            }
            duration = Math.min(duration, maxTime);
            mScroller.setDuration(duration);
            mScroller.start();
            mScrollDuration = duration;
            invalidate();
        } else {
            if (mOnCoverFragmentSateChange != null) {
                mOnCoverFragmentSateChange.setAnimating(false);
            }
            mIsCanTouch = true;
            mIsCanShowShodow = true;
            if (startX != 0 && getChildCount() != 0) {
                removeLastViewInLayout();
            }

        }
    }

    private void removeLastViewInLayout(){
        App.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                if (getChildCount() != 0) {
                    removeViewInLayout(getChildAt(getChildCount() - 1));
                }
            }
        });
    }


    /**
     * 把顶部的view都移走，只留最底下的一个
     */
    public void clearTop() {
        if (mScroller == null || !mScroller.isFinished() || mOnCoverFragmentSateChange == null || mOnCoverFragmentSateChange.getFragmentCount() < 2) {
            return;
        }
        mIsCanTouch = false;
        try {
            if (getChildCount() > 1) {
                //保留最底部的contentView和最上部的View
                removeViewsInLayout(1, getChildCount() - 2);
            }
            //销毁除了最顶部和最底部的其他Fragment
            while (2 < mOnCoverFragmentSateChange.getFragmentCount()) {
                mOnCoverFragmentSateChange.onDestroy(1);
            }
        } catch (Throwable e) {
            ALog.exception("FragmentLayout", "clearTop", e);
        }

        View bottomView = mOnCoverFragmentSateChange.getFragmentView(mOnCoverFragmentSateChange.getFragmentCount() - 1);
        mChildVisiable = true;
        invalidate();
        if (bottomView.getParent() == null) {
            if (bottomView.isLayoutRequested()) {
                addView(bottomView, 1);
            } else {
                addViewInLayout(bottomView, 1, bottomView.getLayoutParams(), true);
            }
        }
        rollRight();
    }


    /**
     * 向右移动
     */
    protected void rollRight() {
        if (!isScrollCompleted()) {
            return;
        }

        if (getChildCount() <= 0) return;
        int currScrollX;
        if (hasNoWebView()) {
            currScrollX = Math.round(getChildAt(getChildCount() - 1).getX());
        } else {
            currScrollX = -getChildAt(getChildCount() - 1).getScrollX();
        }

        if (currScrollX >= 0) {
            if (hasNoWebView()) {
                startScroll(currScrollX, mWidth - currScrollX, 0, 0);
            } else {
                startScroll(currScrollX, -mWidth - currScrollX, 0, 0);
            }
        }
        mIsCanShowShodow = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    private static int clampMag(int value, int absMin, int absMax) {
        final int absValue = Math.abs(value);
        if (absValue < absMin)
            return 0;
        if (absValue > absMax)
            return value > 0 ? absMax : -absMax;
        return value;
    }

    public boolean isScrollCompleted() {
        return mScroller.isFinished();
    }

    private boolean mIsAnimEnd = true;

    public boolean onBackPress() {
        if (getChildCount() > 1) {
            if (!isScrollCompleted() || !mIsAnimEnd) {    // 如果正在滑动，屏蔽事件
                return true;
            }

            if(!mCanScroll){
                removeLastViewInLayout();
                return true;
            }

            CustomFragment fragment = mOnCoverFragmentSateChange.getTopFragment();
            int anim = fragment.onCreateAnimation(false);
            if (anim != 0) {
                mIsAnimEnd = false;
                final View view = getChildAt(getChildCount() - 1);
                Animation animation = AnimationUtils.loadAnimation(getContext(), anim);
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        App.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mIsAnimEnd = true;
                                removeViewInLayout(view);
                            }
                        }, 80);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            } else {
                rollRight();
            }
            return true;
        }
        return false;
    }

    /**
     * 设置nightView
     *
     * @param view
     */
    public void setNightView(View view) {
        mNightView = view;
    }


    public void setOnCoverFragmentSateChange(OnFragmentStateChangeListener onCoverFragmentSateChange) {
        mOnCoverFragmentSateChange = onCoverFragmentSateChange;
    }

    class ScrollAnimation extends CustomAnimation {
        @Override
        protected void applyTransformation(float interpolatedTime) {
            if (getChildCount() != 0) {
                View view = getChildAt(getChildCount() - 1);
                if (hasNoWebView()) {
                    view.setX(mStartLeft + (mEndLeft - mStartLeft) * interpolatedTime);
                } else {
                    view.scrollTo((int) (mStartLeft + (mEndLeft - mStartLeft) * interpolatedTime), 0);
                }
            }
            invalidate();
        }
    }


//    public void setSkipDraw(boolean skipDraw) {
//        mSkipDraw = skipDraw;
//        if (!mSkipDraw) {
//            if(getChildCount() >1){
//                getChildAt(getChildCount() -2).invalidate();
//            }
//            invalidate();
//        }
//    }
//
//    @Override
//    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
//        if (!mSkipDraw) {
//            return super.invalidateChildInParent(location, dirty);
//        } else {
//            return null;
//        }
//    }
}
