package com.common.config.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.common.config.R;
import com.tencent.common.util.ConvertUtils;
import com.tencent.common.util.ScreenUtils;
import com.tencent.common.widget.RoundFrameLayout;

import java.util.ArrayList;

/**
* Banner
*/

public class BannerView extends RoundFrameLayout {

    //---------------------------------自定义属性----------------------------------------------
    private boolean mCanPlay;                                               //是否轮播
    private int     mDefaultColor;                                          //indicator默认color
    private int     mIndicatorColor;                                        //indicator选中color
    private int     mIndicatorRadius;                                       //indicator radius
    private int     mIndicatorPadding;                                      //indicator 之间 padding
    private int     mIndicatorBottom;                                       //indicator 距离底部bottom
    private int     mAnimationDuration;                                     //放手之后banner归位动画时长
    private int     mBannerDuration;                                        //轮播时间间隔

    //--------------------------------默认值----------------------------------------------------
    private static final long ANIMATION_DURATION = 350;                     //放手之后banner归位动画时长

    private static final int BANNER_TIME = 5000;                           //轮播时间间隔

    //------------------------Bitmap数组----------------------
    private ArrayList<Bitmap> mBanners;
    private int mCount;                         //总banner数量
    private int mPrePosition;                   //前一个Banner
    private int mCurrPosition;                  //当前Banner
    private int mNextPosition;                  //下一个Banner

    private Rect mDstRectPre;                   //绘制区域
    private Rect mDstRectCurr;                  //当前绘制区域
    private Rect mDstRectNext;                  //绘制区域
    private Paint mBmpPaint;
    private Paint mIndicatorPaint;
    private Paint mDefaultPaint;                //默认灰色底
    private int mTouchSlop;
    private int mScrollX;
    private int mIndicatorCircleY;
    private ValueAnimator mAnimator;
    private boolean mRunning = false;
    private boolean mVisible = false;
    private boolean mUserPresent = true;
    private boolean mStop = false;
    private boolean mReceiverRegister = false;
    private boolean mIsAbove16 = false;
    private final int MSG_SCROLL = 1;
    private Handler mHandler;
    //轮播图高度
    public final static int BANNER_AD_HEIGHT= 180;

    private int mIndicatorRightPadding = 0;
    private int mIndicatorMarginEnd = 0;

    private Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取相应属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mCanPlay = typedArray.getBoolean(R.styleable.BannerView_can_play, true);
        mIndicatorColor = typedArray.getColor(R.styleable.BannerView_indicator_color, Color.WHITE);
        mDefaultColor = typedArray.getColor(R.styleable.BannerView_default_color, 0x99ffffff);
        mIndicatorRadius = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicator_radius, ConvertUtils.dp2px(3));
        mIndicatorPadding = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicator_padding, ConvertUtils.dp2px(7));
        mIndicatorBottom  = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicato_bottom, ConvertUtils.dp2px(13));
        mAnimationDuration = typedArray.getInt(R.styleable.BannerView_animation_duration, (int) ANIMATION_DURATION);
        mBannerDuration = typedArray.getInt(R.styleable.BannerView_banner_duration,  BANNER_TIME);
        mCount = typedArray.getInt(R.styleable.BannerView_banner_count, 0);
        mIndicatorRightPadding = ConvertUtils.dp2px(15);
        typedArray.recycle();

        init(context);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    private void init(Context context) {
        mBmpPaint = new Paint();
        mBmpPaint.setAntiAlias(true);
        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mDefaultPaint = new Paint();
        mDefaultPaint.setAntiAlias(true);
        mDefaultPaint.setStyle(Paint.Style.FILL);
        mDefaultPaint.setColor(0xfff8f8f8);
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(mAnimationDuration);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mDstRectPre = new Rect();
        mDstRectCurr = new Rect();
        mDstRectNext = new Rect();
        bgPaint.setColor(Color.GRAY);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mIsAbove16 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_SCROLL) {
                    if (mRunning) {
                        handleScroll();
                        sendEmptyMessageDelayed(MSG_SCROLL, mBannerDuration);
                    }
                }
            }

        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int width;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = ConvertUtils.dp2px(BANNER_AD_HEIGHT);
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(heightSize, height);
            }
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = ScreenUtils.getScreenWidth();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(widthSize, width);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //图片区域和view区域不完全一致的话，这套算法就有问题
        //srcRect是取的图片需要绘制的区域
        //dstRect时将需要绘制的目标区域
        mScrollX = 0;
        mIndicatorCircleY = getHeight() - mIndicatorBottom;
        updateRect(0);
    }

    public void setIndexBitmap(int index,Bitmap bmp) {
        if(index < mBanners.size()){
            bmp.prepareToDraw();
            mBanners.set(index, bmp);
        }
        invalidateView();
    }

    public void setCount(int count) {
        if(mCount != count) {
            mCount = count;
            if(mBanners != null){
                mBanners.clear();
                mBanners = null;
            }
            mCurrPosition = 0;
            mPrePosition = 0;
            mNextPosition = 0;
            mBanners = new ArrayList<>(mCount);
        }
        for (int i = 0; i < mCount; i++) {
            mBanners.add(null);
        }
        mCanPlay = mCount > 1;
        preDraw();
        if (mCanPlay) {
            updateRunning();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(mCount > 0) {
            drawBanner(canvas);
            if(mCanPlay) {
                drawIndicator(canvas);
            }
        } else {
            canvas.drawRect(getLeft(),getTop(),getRight(),getBottom(), bgPaint);
        }
    }

    /**
     * draw Banner bitmap
     *
     * @param canvas
     */
    private void drawBanner(Canvas canvas) {
        if(mBanners.get(mCurrPosition) == null){
            canvas.drawRect(mDstRectCurr, mDefaultPaint);
        }else {
            canvas.drawBitmap(mBanners.get(mCurrPosition), null, mDstRectCurr, mBmpPaint);
        }
        if (mDstRectCurr.left > 0) {
            if(mBanners.get(mPrePosition) == null){
                canvas.drawRect(mDstRectPre, mDefaultPaint);
            }else {
                canvas.drawBitmap(mBanners.get(mPrePosition), null, mDstRectPre, mBmpPaint);
            }
        }
        if (mDstRectCurr.left < 0) {
            if(mBanners.get(mNextPosition) == null){
                canvas.drawRect(mDstRectNext, mDefaultPaint);
            }else {
                canvas.drawBitmap(mBanners.get(mNextPosition), null, mDstRectNext, mBmpPaint);
            }
        }
    }

    /**
     * draw Banner indicator
     *
     * @param canvas
     */
    private void drawIndicator(Canvas canvas) {
        int indicatorRectWidth = mCount * mIndicatorRadius * 2 + (mCount - 1) * mIndicatorPadding;
//        int startIndicator = getWidth()/2 - indicatorRectWidth / 2;
        int startIndicator = getWidth()- indicatorRectWidth - mIndicatorRightPadding - mIndicatorMarginEnd;
        for (int i = 0; i < mCount; i++) {
            mIndicatorPaint.setColor(i == mCurrPosition?mIndicatorColor:mDefaultColor);
            canvas.drawCircle(startIndicator+ mIndicatorRadius+ i * (mIndicatorPadding +  2 * mIndicatorRadius), mIndicatorCircleY, mIndicatorRadius, mIndicatorPaint);
        }
    }

    public void setIndicatorMarginEnd(int marginEnd) {
        mIndicatorMarginEnd = marginEnd;
        invalidateView();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        preDraw();
    }

    /**
     * onDraw()前的计算
     */
    private void preDraw() {
        mPrePosition = mCurrPosition - 1 <0? mCount - 1:mCurrPosition - 1;
        mNextPosition = mCurrPosition + 1> mCount - 1?0:mCurrPosition + 1;
        updateRect(mScrollX);
    }

    private void handleScroll(){
        if (mScrollX == 0) {
            mScrollX = -1;
            updateRect(mScrollX);
            animToPosition();
        }
    }

    public void setStop(boolean stop){
        mStop  = stop;
        updateRunning();
    }

    private void updateRunning() {
        boolean running = mVisible && mUserPresent && mCanPlay && !mStop;
        if (running != mRunning) {
            mRunning = running;
            if (running) {
                mHandler.removeMessages(MSG_SCROLL);
                mHandler.sendEmptyMessageDelayed(MSG_SCROLL,mBannerDuration);
            } else {
                mHandler.removeMessages(MSG_SCROLL);
            }
        }
    }

    /**
     * 放手后滑动到期望位置
     */
    private void animToPosition() {
        if (mDstRectCurr.left != 0) {
            final int deltaX;
            final int originalX = mScrollX;
            if (mDstRectCurr.left > 0) {
                deltaX = -mDstRectPre.left;
            } else {
                deltaX = -mDstRectNext.left;
            }
            mAnimator.removeAllUpdateListeners();
            mAnimator.removeAllListeners();
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatorValue = (float) animation.getAnimatedValue();
                    mScrollX = (int) (originalX + animatorValue * deltaX);
                    preDraw();
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (deltaX < 0) {
                        mCurrPosition = mNextPosition;
                    } else {
                        mCurrPosition = mPrePosition;
                    }
                    mScrollX = 0;
                    preDraw();
                    if (mClickListener != null) {
                        if (mBanners!=null&&mBanners.size() > mCurrPosition) {
                            mClickListener.onBannerSwitch(mCurrPosition);

                        }
                    }
                }
            });
            mAnimator.start();
        }
    }

    private void updateRect(int scrollX) {
        mDstRectCurr.set(scrollX, 0, getWidth() + scrollX, getHeight());
        mDstRectPre.set(mDstRectCurr.left - getWidth(), mDstRectCurr.top, mDstRectCurr.left, mDstRectCurr.bottom);
        mDstRectNext.set(mDstRectCurr.left + getWidth(), mDstRectCurr.top, mDstRectCurr.left + 2 * getWidth(), mDstRectCurr.bottom);
        invalidateView();
    }

    //----------------------------------------Touch 逻辑---------------------------------
    private Point mDownPoint = new Point();
    private Point mMovePoint = new Point();
    private boolean mIsDragging;
    private int mLastX;
    private int mLastActionX;
    private int mLastActionY;
    private boolean mHandleEvent = true;
    private boolean mHasComputeHandleEvent;
    private long mDownTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if(mCanPlay) {
//            PluginRely.enableGesture(false);
//        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownPoint.set(x, y);
                mIsDragging = false;
                mScrollX = 0;
                mLastX = x;
                mLastActionX = x;
                mLastActionY = y;
                if (mCanPlay) {
                    mHandler.removeMessages(MSG_SCROLL);
                }
                mDownTime= SystemClock.uptimeMillis();
                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.cancel();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastX;
                mMovePoint.set(x, y);
                //判断是滑动还是点击
                if (calculateA2B(mDownPoint, mMovePoint) >= mTouchSlop) {
                    mIsDragging = true;
                }
                if (mIsDragging && !mHasComputeHandleEvent) {
                    mHandleEvent = Math.abs(x - mLastActionX) > Math.abs(y - mLastActionY);
                    mHasComputeHandleEvent = true;
                }
                if(mCanPlay) {
                    getParent().requestDisallowInterceptTouchEvent(mHandleEvent);
                }

                if (mIsDragging && mCanPlay && mHandleEvent) {
                    mScrollX = dx;
                    preDraw();
                }
                mLastActionX = x;
                mLastActionY = y;

                setPressed(true);
                break;
            case MotionEvent.ACTION_UP:
                setPressed(false);
                if (mIsDragging && mScrollX != 0) {
                    animToPosition();
                } else if (mClickListener != null && SystemClock.uptimeMillis() - mDownTime < 100) {
                    mClickListener.onClick(mCurrPosition);
                }
                if (mCanPlay) {
                    mHandler.sendEmptyMessageDelayed(MSG_SCROLL, mBannerDuration);
                }
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                mIsDragging = false;
                mLastX = 0;
                mLastActionX = 0;
                mLastActionY = 0;
                mHasComputeHandleEvent = false;
//                if(mCanPlay) {
//                    PluginRely.enableGesture(true);
//                }
                if (mCanPlay && mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.start();
                }
                break;
        }
        return true;
    }

    private BannerStateListener mClickListener;

    public interface BannerStateListener {
        void onClick(int position);
        void onBannerSwitch(int position);
    }

    public void setOnBannerClickListener(BannerStateListener clickListener) {
        mClickListener = clickListener;
    }


    @SuppressLint("NewApi")
    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        mVisible = screenState == SCREEN_STATE_ON;
        updateRunning();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisible = visibility == View.VISIBLE;
        updateRunning();
    }

    private void invalidateView() {
        if (mIsAbove16) {
            postInvalidateOnAnimation();
        } else {
            invalidate();
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == View.VISIBLE;
        updateRunning();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mReceiverRegister) {
            final IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getContext().registerReceiver(mReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
                }else{
                    getContext().registerReceiver(mReceiver, filter);
                }
                mReceiverRegister = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mReceiverRegister) {
            try {
                getContext().unregisterReceiver(mReceiver);
                mReceiverRegister = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateRunning();
        mHandler.removeCallbacksAndMessages(null);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mUserPresent = false;
                updateRunning();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mUserPresent = true;
                updateRunning();
            }
        }
    };

    /**
     * 计算两个点之间的距离
     *
     * @param startPoint
     * @param endPoint
     * @return
     */
    public static int calculateA2B(Point startPoint, Point endPoint) {
        int xDist = startPoint.x - endPoint.x;
        int yDist = startPoint.y - endPoint.y;
        return (int) Math.sqrt(xDist * xDist + yDist * yDist);
    }
}
