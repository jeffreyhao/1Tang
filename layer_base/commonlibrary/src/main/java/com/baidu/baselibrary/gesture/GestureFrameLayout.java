package com.baidu.baselibrary.gesture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.base.util.ui.UIUtil;
import com.jess.baselibrary.R;

import androidx.core.view.ViewCompat;


/**
 * 手势布局，仅仅左右跟手。
 * 其第一个子View用来计算跟手操作
 */
@SuppressWarnings("UnusedParameters")
public class GestureFrameLayout extends FrameLayout {


	public interface OnCloseListener {

		/**
		 * 通知调用者关闭窗口
		 * @param base
		 */
		void onClose(GestureFrameLayout base) ;
	}



	private static final int MAX_SETTLE_DURATION 		= 350; // ms
	protected static final int TOUCH_STATE_REST      	= 0;
	protected static final int TOUCH_STATE_SCROLLING 	= 1;
	protected static final int GRADIENT              	= 2;

	private float     START_ALPHA         = 0.5f;//初始拖动时，右控件的初始透明度


	/**监听的触摸x坐标*/
	protected float    	mLastMotionX;

	protected int      	mTouchState;
	protected int		mTouchSlop;


	protected Scroller 	mScroller;

	/** 起点坐标*/
	private	Point    	mStartPoint;
	private	Point    	mMovePoint;
	private	Paint 		mPaint;

	private int mScaledMinimumFlingVelocity;
	private int mOffset = 0;
	private int mSlideShapeWidth;


	private View mCaptureView;
	private Drawable mRightGradientDrawable;
	private VelocityTracker mVelocityTracker;
	private OnCloseListener mOnCloseListener;

	private boolean 	mHasOpenAnimation = false;
	private boolean 	mWillGone = false;
	private boolean 	mIsHardware = false;
	private boolean 	mEnableScroll = true;
	private boolean 	mLock = false;
	private boolean 	mEnableBorderShadow = true;
	private boolean 	mAnimationLeftIn = true;

	
	public GestureFrameLayout(Context context) {
		super(context);
		init(context);
	}
	
	public GestureFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public GestureFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void setOnCloseListener(OnCloseListener listener){
		mOnCloseListener = listener;
	}

	public void setEnableScroll(boolean enableScroll){
		mEnableScroll = enableScroll;
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if(mEnableScroll){
			try {
				if (mScroller.computeScrollOffset()) {
					mCaptureView.offsetLeftAndRight(mScroller.getCurrX() - mCaptureView.getLeft());
					if(mScroller.isFinished() ) {
						if(mIsHardware){
							enableHardwareOrSoftwareAccelerateBuffer(false);
						}
						if( mCaptureView.getLeft() != 0 ){
							closeWithoutAnimation();
						}
					}
					ViewCompat.postInvalidateOnAnimation(this);
				}
				super.dispatchDraw(canvas);
				mRightGradientDrawable.setBounds(mCaptureView.getRight(), 0, mCaptureView.getRight() + mSlideShapeWidth, getMeasuredHeight());
				if(mEnableBorderShadow){
					mRightGradientDrawable.draw(canvas);
				}
				if(mCaptureView.getRight() < getMeasuredWidth() && mCaptureView.getRight() > 0){
					float f = (float)mCaptureView.getRight()/mCaptureView.getMeasuredWidth();
					int alpha = (int)(255 * (f * START_ALPHA));
					mPaint.setAlpha(alpha);
					canvas.drawRect(mCaptureView.getRight(), 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
				}
			} catch (Exception e) {
				LogUtil.e(e);
			}
		} else {
			super.dispatchDraw(canvas);
		}
	}


	protected void init(Context context) {
		mScroller 	= new Scroller(context , AnimationUtils.loadInterpolator(context, R.anim.interpolator_decelerate));
		mTouchState = TOUCH_STATE_REST;
		mTouchSlop 	= ViewConfiguration.get(context).getScaledTouchSlop();
		mScaledMinimumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
		mStartPoint = new Point();
		mMovePoint 	= new Point();
		mRightGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,  new int[] {0x00000000, 0x55333333});
		mSlideShapeWidth = UIUtil.dip2px(context, 5);
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		disableAnimation();
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mEnableScroll){
					onCloseAnimation();
				}
			}
		});
	}



	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(mEnableScroll){
			float x = ev.getX();
			float y = ev.getY();
			if(ev.getAction()==MotionEvent.ACTION_DOWN){
				mLastMotionX = x;
				mStartPoint.x = (int)x;
				mStartPoint.y = (int)y;
				mLock = false;
			}

			if(!App.getEnableScrollToLeft()){
				if(ev.getAction()!=MotionEvent.ACTION_MOVE){
					App.setEnableScrollToLeft(true);
				}else{
					return super.onInterceptTouchEvent(ev);
				}
			}
			final int action = ev.getAction();
			if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST) ) {
				return x < mCaptureView.getRight() || super.onInterceptTouchEvent(ev);
			}
			switch (action) {
				case MotionEvent.ACTION_DOWN:
					mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
					break;
				case MotionEvent.ACTION_MOVE:
					mMovePoint.x = (int)x;
					mMovePoint.y = (int)ev.getY();
					int slop = UIUtil.calculateA2B(mStartPoint, mMovePoint);
					float grad = UIUtil.calculateGradient(mStartPoint,mMovePoint);

					if (!mLock&&slop >= mTouchSlop ) {
						if(Math.abs(grad) > GRADIENT){
							mLastMotionX = x;
							mTouchState = TOUCH_STATE_SCROLLING;
							mWillGone = false;
						}else if(mTouchState!= TOUCH_STATE_SCROLLING && Math.abs(grad) < 0.8f){
							mLock = true;
						}
					}
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					mTouchState = TOUCH_STATE_REST;
					break;
			}
			return (mTouchState != TOUCH_STATE_REST)&&!mLock;
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}

	/**
	 * 是否启用硬件加速
	 *
	 * @param isEnable  true-启用硬件加速
	 */
	private void enableHardwareOrSoftwareAccelerateBuffer(boolean isEnable){
		mIsHardware = isEnable;
		final int layerType = isEnable ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;
		int count = getChildCount();
		for(int i = 0; i< count; i++){
			getChildAt(i).setLayerType(layerType, null);
        }
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(mEnableScroll){
			mCaptureView = getChildAt(0);
			int offsetLeft = mCaptureView.getLeft() + mOffset;

			mCaptureView.layout(offsetLeft, 0, offsetLeft+mCaptureView.getMeasuredWidth(), mCaptureView.getMeasuredHeight());
			if(mHasOpenAnimation){
				if (mAnimationLeftIn) {
					startScroll(-mCaptureView.getWidth() - mSlideShapeWidth, mCaptureView.getWidth() + mSlideShapeWidth, 0);
				} else {
					startScroll(mCaptureView.getWidth() + mSlideShapeWidth, -(mCaptureView.getWidth() + mSlideShapeWidth), 0);
				}
				mHasOpenAnimation  = false;
			}
		} else {
			super.onLayout(changed, l, t, r, b);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mEnableScroll){
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				mLock = false;
			}
			if(!App.getEnableScrollToLeft()){
				if(event.getAction()!=MotionEvent.ACTION_MOVE){
					App.setEnableScrollToLeft(true);
				}else{
					return super.onTouchEvent(event);
				}
			}

			float eventFloatX = event.getX();
			int action = event.getAction();
			switch (action) {
				case MotionEvent.ACTION_DOWN:
					if (!mScroller.isFinished() && eventFloatX < mCaptureView.getRight()) {
						mWillGone = false;
						stopAnimation();
					}
					mWillGone = mScroller.isFinished() && eventFloatX > mCaptureView.getRight();
					mLastMotionX = eventFloatX;
					if (mVelocityTracker == null) {
						mVelocityTracker = VelocityTracker.obtain();
						mVelocityTracker.addMovement(event);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					float deltaX = eventFloatX- mLastMotionX;
					mMovePoint.x = (int)eventFloatX;
					mMovePoint.y = (int)event.getY();
					if (mVelocityTracker == null) {
						mVelocityTracker = VelocityTracker.obtain();
					}
					mVelocityTracker.addMovement(event);
					float sx = mCaptureView.getLeft();

					int slop = UIUtil.calculateA2B(mStartPoint, mMovePoint);
					if (slop >= mTouchSlop ) {
						if((mAnimationLeftIn && sx + deltaX <= 0 && sx + deltaX >= -mCaptureView.getWidth())
								||(!mAnimationLeftIn && sx + deltaX >= 0 && sx + deltaX <= mCaptureView.getWidth())){
							if(!mIsHardware){
								enableHardwareOrSoftwareAccelerateBuffer(true);
							}else{
								mCaptureView.offsetLeftAndRight((int)deltaX);
								ViewCompat.postInvalidateOnAnimation(this);
							}
						}
						mWillGone = false;
					}
					mLastMotionX = eventFloatX;
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_OUTSIDE:
					int velocityX = 0;
					if (mVelocityTracker != null){
						mVelocityTracker.addMovement(event);
						mVelocityTracker.computeCurrentVelocity(1000);
						velocityX = (int) mVelocityTracker.getXVelocity();
					}
					if(mWillGone){
						mWillGone = false;
						if (mAnimationLeftIn) {
							startScroll(mCaptureView.getLeft(), -mCaptureView.getWidth() - mSlideShapeWidth - mCaptureView.getLeft(), 0);
						} else {
							startScroll(mCaptureView.getLeft(), mCaptureView.getWidth() + mSlideShapeWidth - mCaptureView.getLeft(), 0);
						}
					}else{
						animationEnd(eventFloatX, velocityX);
					}
					break;
				default:
					break;
			}
			return true;
		} else {
			return super.onTouchEvent(event);
		}
	}
	
	public void onEnterAnimation() {
		mHasOpenAnimation = true;
	}


	/**
	 * 关闭，开始动画
	 */
	public void onCloseAnimation() {
		if(mCaptureView == null)return;
		int offset = mCaptureView.getLeft();
		if (mAnimationLeftIn) {
			startScroll(offset, -mCaptureView.getWidth() - mSlideShapeWidth - offset, 0);
		} else {
			startScroll(offset, mCaptureView.getWidth() + mSlideShapeWidth - offset, 0);
		}
	}
	
	/**
	 * 清理Scroller 状态
	 */
	protected void cleanState() {
		mTouchState = TOUCH_STATE_REST;
		if(mVelocityTracker != null) mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	public void startScroll(int startX, int dx, int absVelocitX ) {
		if(dx != 0){
			if(!mIsHardware){
				 enableHardwareOrSoftwareAccelerateBuffer(true);
			 }
			int duration;
			final int width = getMeasuredWidth();
	        final int halfWidth = width / 2;
	        final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
	        final float distance = halfWidth + halfWidth *
	                UIUtil.distanceInfluenceForSnapDuration(distanceRatio);
	        
	        if (absVelocitX > 0) {
	            duration =  4*Math.round(1000 * Math.abs(distance / absVelocitX));
	        } else {
	            duration = MAX_SETTLE_DURATION;
	        }
	        duration = Math.min(duration, MAX_SETTLE_DURATION);
			mScroller.startScroll(startX, 0, dx , 0, duration);
			ViewCompat.postInvalidateOnAnimation(this);
		}else{
			if(mIsHardware){
				enableHardwareOrSoftwareAccelerateBuffer(false);
			}
		}
	}
	
	private void animationEnd(float eventFloatX , int velocityX) {
		cleanState();
		int offset = mCaptureView.getLeft();
		if(mAnimationLeftIn) {
			// 1. 速率判断
			if (velocityX < -(mScaledMinimumFlingVelocity << 1)) {
				startScroll(offset, -mCaptureView.getWidth() - mSlideShapeWidth - offset, Math.abs(velocityX));
				return;
			} else if (velocityX > mScaledMinimumFlingVelocity << 1) {
				startScroll(offset, -offset, Math.abs(velocityX));
				return;
			}

			if (offset < -mCaptureView.getMeasuredWidth() * 0.5) {
				startScroll(offset, -mCaptureView.getWidth() - mSlideShapeWidth - offset, Math.abs(velocityX));
			} else {
				startScroll(offset, -offset, Math.abs(velocityX));
			}
		}
		else {
			// 1. 速率判断
			if (velocityX < -(mScaledMinimumFlingVelocity << 1)) {
				startScroll(offset,  - offset, Math.abs(velocityX));
				return;
			} else if (velocityX > mScaledMinimumFlingVelocity << 1) {
				startScroll(offset, mCaptureView.getWidth() + mSlideShapeWidth - offset, Math.abs(velocityX));
				return;
			}

			if (offset > mCaptureView.getMeasuredWidth() * 0.5) {
				startScroll(offset, mCaptureView.getWidth() + mSlideShapeWidth - offset, Math.abs(velocityX));
			} else {
				startScroll(offset, -offset, Math.abs(velocityX));
			}
		}
	}
	
	protected void stopAnimation() {
		 if (mScroller != null && !mScroller.isFinished()){    
          mScroller.abortAnimation();    
      }   
	}

	public void setAnimationLeftIn(boolean isLeftIn){
		mAnimationLeftIn = isLeftIn;
	}

	/**
	 * 边界阴影
	 * @param enable 开关
	 */
	public void setBorderShadowEnable(boolean enable){
		mEnableBorderShadow = enable;
	}

	private void closeWithoutAnimation() {
		int i = 0;
		if(mOnCloseListener != null){
			mOnCloseListener.onClose(this);
		}
	}

	private void disableAnimation() {
		int i = 0;
	}
}
