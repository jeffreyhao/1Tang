package com.baidu.baselibrary.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.baidu.baselibrary.global.Const;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.clz.ClassUtil;
import com.baidu.baselibrary.util.device.ScreenUtil;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;


public class FragmentWindowWrapLayout extends FrameLayout {

    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = null;
    private Paint mNightPaint;
    private boolean mContentCanChange = true;
    private static Field mAddedField = null;
    private static boolean mAddedFieldIsInited = false;
    private int mOffsetYOnVivo = 0;//vivo的异形屏手机上，当前的window的起点是在状态栏的下方，所以需要向上偏移状态栏高度
    public static boolean sIsVivoDiff = ScreenUtil.isDiffScreenVivo();

    public FragmentWindowWrapLayout(@NonNull Context context) {
        super(context);
        mNightPaint = new Paint();
        mNightPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mNightPaint.setColor(Color.argb(
                Math.round(255F * Const.NIGHT_PERCENT_DIM), 0, 0, 0));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //拦截所有事件
        return true;
//        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mOffsetYOnVivo != 0) {
            canvas.translate(0, mOffsetYOnVivo);
        }
        if (mOnPreDrawListener != null && mOnPreDrawListener.onPreDraw()) {
            return;
        }
        super.dispatchDraw(canvas);
        if (App.mEnableNight) {
            canvas.drawRect(0F, 0F, getWidth(), getHeight(), mNightPaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //如果是vivo异形屏则向下偏移一个状态栏高度，这是vivo系统的bug，这里做一个兼容
        if (sIsVivoDiff) {
            int location[] = new int[2];
            getLocationOnScreen(location);
//            if (location[1] == IMenu.MENU_HEAD_HEI) {
//                mOffsetYOnVivo = -location[1];
//            }
        }
    }

    public void setOnPreDrawListener(ViewTreeObserver.OnPreDrawListener listener) {
        mOnPreDrawListener = listener;
    }

    public void detachAllViewsFromParentExt() {
        mContentCanChange = false;

        if (Build.VERSION.SDK_INT >= 21) {
            callGapWorkerRemove(this);
        }
        super.detachAllViewsFromParent();
        if (!mAddedFieldIsInited && getParent() != null) {
            mAddedFieldIsInited = true;
            mAddedField = ClassUtil.getFieldInClass(getParent().getClass(), "mAdded");
        }

        if(mAddedField != null){
            try {
                mAddedField.set(getParent(), false);
            } catch (Throwable e) {
                ALog.exception("FragmentWindowWrapLayout", "detachAllViewsFromParentExt", e);
            }
        }
    }

    public void resetIsAddedInViewrootImplClass() {
        if(mAddedField != null){
            try {
                mAddedField.set(getParent(), true);
            } catch (Throwable e) {
                ALog.exception("FragmentWindowWrapLayout", "resetIsAddedInViewrootImplClass", e);
            }
        }
    }

    private void callGapWorkerRemove(View view) {
        if (view != null && ClassUtil.instanceOfClass(view.getClass(), ViewGroup.class)) {
//            if (Util.instanceOfClass(view.getClass(), RecyclerView.class)) {
//                MetaGapWorker.removeRecyclerView((RecyclerView) view);
//            }
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                callGapWorkerRemove(viewGroup.getChildAt(i));
            }
        }
    }

    @Override
    public void requestLayout() {
        if (!mContentCanChange) return;
        super.requestLayout();
    }

    @Override
    public void forceLayout() {
        if (!mContentCanChange) return;
        super.forceLayout();
    }

    @Override
    public void invalidate() {
        if (!mContentCanChange) return;
        super.invalidate();
    }

    @Override
    public void invalidate(Rect dirty) {
        if (!mContentCanChange) return;
        super.invalidate(dirty);
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        if (!mContentCanChange) return;
        super.invalidate(l, t, r, b);
    }

    @Override
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        if (!mContentCanChange) return null;
        return super.invalidateChildInParent(location, dirty);
    }

    @Override
    public void postInvalidateOnAnimation() {
        if (!mContentCanChange) return;
        super.postInvalidateOnAnimation();
    }

    @Override
    public void postInvalidateOnAnimation(int left, int top, int right, int bottom) {
        if (!mContentCanChange) return;
        super.postInvalidateOnAnimation(left, top, right, bottom);
    }

}
