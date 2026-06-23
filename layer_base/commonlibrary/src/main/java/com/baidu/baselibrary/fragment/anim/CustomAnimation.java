package com.baidu.baselibrary.fragment.anim;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import androidx.core.view.ViewCompat;

/**
 * 自定义动画抽象类
 * 为了追求更高的动画性能
 */
public abstract class CustomAnimation {
    /**
     * 动画无限重复，可以设置到{@link #mRepeatCount}}
     */
    public static final int INFINITE = -1;

    /**
     * 动画循环时，都是正方向执行,即动画因子由0到1
     */
    public static final int RESTART = 1;

    /**
     * 动画循环时，会一次正方向，一次反方向
     */
    public static final int REVERSE = 2;

    /**
     * 动画开始第一帧时进行计时
     */
    public static final int START_ON_FIRST_FRAME = -1;

    public static final int UN_DEFINED = -2;
    /**
     * 动画是否有效
     */
    boolean mEnded = false;

    /**
     * 动画是否已经开始
     */
    boolean mStarted = false;

    /**
     * 是否调用过start方法了
     */
    boolean mIsStartCalled = false;
    /**
     * 表明动画因子是正向只想还是反向，如果为true则表明动画因子从0.0-1.0，反之则1.0-0.0
     */
    boolean mCycleFlip = false;

    /**
     * 动画是否调用过初始化
     */
    boolean mInitialized = false;
    /**
     * 动画开始时间,以绘制的第一帧的时间为准
     */
    long mStartTime = UN_DEFINED;

    /**
     * 相对mStartTime时间，延迟多久开始
     */
    long mStartOffset;

    /**
     * 单次动画运行的时长
     */
    long mDuration;

    /**
     * 动画重复次数
     */
    int mRepeatCount = 0;

    /**
     * 动画已重复次数
     */
    int mRepeated = 0;

    /**
     * 动画重复模式时，动画因子的变化模式，可能是{@link #RESTART}, {@link #REVERSE}
     */
    int mRepeatMode = RESTART;

    /**
     * 动画的加速度器.
     */
    Interpolator mInterpolator;

    /**
     * 动画运行过程中的回调.
     */
    AnimationListener mListener;
    /**
     * 动画是否还有后续动画，可能是继续做动画或者是动画重复
     */
    private boolean mMore = true;
    private boolean mOneMoreTime = true;

    /**
     * 构造函数，默认给一个加速器
     */
    public CustomAnimation() {
        ensureInterpolator();
    }

    /**
     * 重置到动画初始化时的状态.
     *
     * @see #initialize()
     */
    public void reset() {
        mInitialized = false;
        mCycleFlip = false;
        mRepeated = 0;
        mMore = true;
        mOneMoreTime = true;
    }

    /**
     * 取消执行的动画
     * <p/>
     * If you cancel an animation manually, you must call {@link #reset()}
     * before starting the animation again.
     *
     * @see #reset()
     * @see #start()
     * @see #startNow()
     */
    public void cancel() {
        if (mStarted && !mEnded) {
            if (mListener != null) mListener.onAnimationCancel(this);
            mEnded = true;
        }
        mStartTime = Long.MIN_VALUE;
        mMore = mOneMoreTime = false;
        mIsStartCalled = false;
    }

    /**
     *
     */
    public void detach() {
        if (mStarted && !mEnded) {
            mEnded = true;
            if (mListener != null) mListener.onAnimationEnd(this);
        }
    }

    /**
     * 动画是否初始化过
     *
     * @return 是否已经初始化了动画
     * @see #initialize()
     */
    public boolean isInitialized() {
        return mInitialized;
    }

    /**
     * 初始化当前动画
     */
    public void initialize() {
        reset();
        mInitialized = true;
    }

    /**
     * 设置动画的加速器
     *
     * @param context context
     * @param resID   加速器的资源id
     */
    public void setInterpolator(Context context, int resID) {
        setInterpolator(AnimationUtils.loadInterpolator(context, resID));
    }

    /**
     * 设置动画的加速器
     *
     * @param i 加速器
     */
    public void setInterpolator(Interpolator i) {
        mInterpolator = i;
    }

    /**
     * 设置延迟多久开始动画
     *
     * @param startOffset 延迟的时间，单位是毫秒
     */
    public void setStartOffset(long startOffset) {
        mStartOffset = startOffset;
    }

    /**
     * 设置动画时长
     *
     * @param durationMillis 动画时长，单位毫秒
     * @throws IllegalArgumentException if the duration is < 0
     */
    public void setDuration(long durationMillis) {
        if (durationMillis < 0) {
            throw new IllegalArgumentException("Animation duration cannot be negative");
        }
        mDuration = durationMillis;
    }

    /**
     * 设置动画开始时间
     *
     * @param startTimeMillis 动画开始的时间
     */
    public void setStartTime(long startTimeMillis) {
        mStartTime = startTimeMillis;
        mStarted = mEnded = false;
        mCycleFlip = false;
        mRepeated = 0;
        mMore = true;
        mIsStartCalled = true;
    }

    /**
     * 开始动画，绘制第一帧的时间为开始时间
     */
    public void start() {
        setStartTime(START_ON_FIRST_FRAME);
    }

    /**
     * 开始动画，当前时间为动画开始时间
     */
    public void startNow() {
        setStartTime(AnimationUtils.currentAnimationTimeMillis());
    }

    /**
     * 设置动画重复模式
     *
     * @param repeatMode {@link #RESTART} or {@link #REVERSE}
     */
    public void setRepeatMode(int repeatMode) {
        mRepeatMode = repeatMode;
    }

    /**
     * 设置动画重复次数
     *
     * @param repeatCount 画重复次数
     */
    public void setRepeatCount(int repeatCount) {
        if (repeatCount < 0) {
            repeatCount = INFINITE;
        }
        mRepeatCount = repeatCount;
    }

    /**
     * 获取动画的加速器
     *
     * @return the {@link Interpolator} 动画加速器
     */
    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    /**
     * 获取动画开始时间,为-1则表示从第一帧绘制时间算起
     *
     * @return 动画开始时间, 为-1则表示从第一帧绘制时间算起
     */
    public long getStartTime() {
        return mStartTime;
    }

    /**
     * 获取动画时长
     *
     * @return 动画时长
     */
    public long getDuration() {
        return mDuration;
    }

    /**
     * 获取动画延迟执行时间
     *
     * @return 动画延迟执行时间
     */
    public long getStartOffset() {
        return mStartOffset;
    }

    /**
     * 获取重复时，动画因子变化模式
     *
     * @return 可能这两个中的一个 {@link #REVERSE} / {@link #RESTART}
     */
    public int getRepeatMode() {
        return mRepeatMode;
    }

    /**
     * 获取动画重复次数
     *
     * @return 动画重复次数，可能是 {@link #INFINITE}
     */
    public int getRepeatCount() {
        return mRepeatCount;
    }

    /**
     * 设置动画回调
     *
     * @param listener
     */
    public void setAnimationListener(AnimationListener listener) {
        mListener = listener;
    }

    /**
     * 设置一个默认的加速器
     */
    protected void ensureInterpolator() {
        if (mInterpolator == null) {
            mInterpolator = new AccelerateDecelerateInterpolator();
        }
    }

    public void onCallDraw(View v) {
        if (getTransformation(SystemClock.uptimeMillis())) {
            ViewCompat.postInvalidateOnAnimation(v);
        }
    }

    public void onCallDraw(Drawable drawable) {
        if (getTransformation(SystemClock.uptimeMillis())) {
            drawable.invalidateSelf();
        }
    }

    /**
     * 动画执行时会调用
     *
     * @param currentTime 当前的时钟时间
     * @return 如果动画还在执行，则返回true，否则返回false
     */
    public boolean getTransformation(long currentTime) {

        boolean needRefresh = false;
        //表明这是第一次调用，开始时间从这帧时钟时间算起
        if (mStartTime == UN_DEFINED) {
            return false;
        } else if (mStartTime == START_ON_FIRST_FRAME) {
            mStartTime = currentTime;
        }

        if (!isInitialized()) {
            initialize();
        }
        final long startOffset = getStartOffset();
        final long duration = mDuration;
        float normalizedTime;
        if (duration != 0) {
            //已经运行的时间除以动画时间，得到的值可能大于1
            normalizedTime = ((float) (currentTime - (mStartTime + startOffset))) /
                    (float) duration;
        } else {
            normalizedTime = currentTime < mStartTime ? 0.0f : 1.0f;
        }

        //如果大于1,表明当前这次动画已经执行完毕了，需要判断是否结束还是重复动画
        final boolean expired = normalizedTime >= 1.0f;
        //表明动画是否还有后续
        mMore = !expired;

        if ((normalizedTime >= 0.0f) && (normalizedTime <= 1.0f)) {
            if (!mStarted) {
                if (mListener != null) {
                    mListener.onAnimationStart(this);
                }
                mStarted = true;

            }

            if (mCycleFlip) {
                normalizedTime = 1.0f - normalizedTime;
            }

            float interpolatedTime = mInterpolator.getInterpolation(normalizedTime);

            if(interpolatedTime > 1.0f || interpolatedTime < 0){
                interpolatedTime = 0;
            }
//            LogUtil.d("applyTransformation", "applyTransformation, normalizedTime = " + normalizedTime + ", interpolatedTime = " + interpolatedTime);

            applyTransformation(interpolatedTime);
        }

        if (expired) {
            if (mRepeatCount == mRepeated) {
                if (!mEnded) {
                    mEnded = true;
                    if (mListener != null) {
                        mListener.onAnimationEnd(this);
                        needRefresh = true;
                    }
                }
            } else {
                if (mRepeatCount > 0) {
                    mRepeated++;
                }

                if (mRepeatMode == REVERSE) {
                    mCycleFlip = !mCycleFlip;
                }

                mStartTime = START_ON_FIRST_FRAME;
                mMore = true;

                if (mListener != null) {
                    mListener.onAnimationRepeat(this);
                }
            }
        }

        if (!mMore && mOneMoreTime) {
            mOneMoreTime = false;
            return true;
        }

        return needRefresh || mMore;
    }

    /**
     * 表明动画是否已经开始
     *
     * @return 如果动画开始则返回true
     */
    public boolean hasStarted() {
        return mStarted;
    }

    /**
     * 表明动画是否可用
     *
     * @return 如果动画已经结束则返回true
     */
    public boolean hasEnded() {
        return mEnded;
    }

    /**
     * 外界需要重载这个方法，这里可以获取到动画因子
     *
     * @param interpolatedTime 动画因子,值可能是介于0.0到1.0之间
     */
    protected void applyTransformation(float interpolatedTime) {

    }


    public boolean isFinished() {
        return !hasStarted() ||hasEnded() || (SystemClock.uptimeMillis() - mStartTime >= mDuration);
    }

    /**
     * 动画回调接口
     */
    public interface AnimationListener {

        /**
         * 通知动画开始
         */
        void onAnimationStart(CustomAnimation animation);

        /**
         * 通知动画结束
         */
        void onAnimationEnd(CustomAnimation animation);

        /**
         * 通知动画开始重复进行
         */
        void onAnimationRepeat(CustomAnimation animation);

        /**
         * 通知动画被取消
         * @param animation
         */
        void onAnimationCancel(CustomAnimation animation);
    }
}
