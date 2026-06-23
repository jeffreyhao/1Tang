package com.tencent.common.util;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fold.common.R;

/**
 * 吐司相关工具类
 *
 * @author adison
 * @date 2017/5/20
 * @time 下午5:34
 */
public final class ToastUtils {

    private static Toast sToast;
    private static int gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private static int xOffset = 0;
    private static int yOffset = (int) (64 * Abase.getContext().getResources().getDisplayMetrics().density + 0.5);
    @SuppressLint("StaticFieldLeak")
    private static View customView;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 设置吐司位置
     *
     * @param gravity 位置
     * @param xOffset x偏移
     * @param yOffset y偏移
     */
    public static void setGravity(int gravity, int xOffset, int yOffset) {
        ToastUtils.gravity = gravity;
        ToastUtils.xOffset = xOffset;
        ToastUtils.yOffset = yOffset;
    }

    /**
     * 设置吐司view
     *
     * @param layoutId 视图
     */
    public static void setView(@LayoutRes int layoutId) {
        LayoutInflater inflate = (LayoutInflater) Abase.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ToastUtils.customView = inflate.inflate(layoutId, null);
    }

    /**
     * 设置吐司view
     *
     * @param view 视图
     */
    public static void setView(View view) {
        ToastUtils.customView = view;
    }

    /**
     * 获取吐司view
     *
     * @return view 自定义view
     */
    public static View getView() {
        if (customView != null) return customView;
        if (sToast != null) return sToast.getView();
        return null;
    }

    /**
     * 安全地显示短时吐司
     *
     * @param text 文本
     */
    public static void showShortSafe(final CharSequence text) {
        try {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(text, Toast.LENGTH_SHORT);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShortSafe(final @StringRes int resId) {
        try {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(resId, Toast.LENGTH_SHORT);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showShortSafe(final @StringRes int resId, final Object... args) {
        try {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(resId, Toast.LENGTH_SHORT, args);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 安全地显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShortSafe(final String format, final Object... args) {
        try {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(format, Toast.LENGTH_SHORT, args);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 安全地显示长时吐司
     *
     * @param text 文本
     */
    public static void showLongSafe(final CharSequence text) {
        try {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(text, Toast.LENGTH_LONG);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLongSafe(final @StringRes int resId) {
        try {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(resId, Toast.LENGTH_LONG);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showLongSafe(final @StringRes int resId, final Object... args) {
        try {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(resId, Toast.LENGTH_LONG, args);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 安全地显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showLongSafe(final String format, final Object... args) {
        try {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(format, Toast.LENGTH_LONG, args);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示短时吐司
     *
     * @param text 文本
     */
    public static void showShort(CharSequence text) {
        if (TextUtils.isEmpty(text)) return;
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShort(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showShort(@StringRes int resId, Object... args) {
        show(resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShort(String format, Object... args) {
        if (TextUtils.isEmpty(format)) {
            show(format, Toast.LENGTH_SHORT);
        } else {
            show(format, Toast.LENGTH_SHORT, args);
        }
    }

    /**
     * 显示长时吐司
     *
     * @param text 文本
     */
    public static void showLong(CharSequence text) {
        show(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLong(@StringRes int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showLong(@StringRes int resId, Object... args) {
        show(resId, Toast.LENGTH_LONG, args);
    }

    /**
     * 显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showLong(String format, Object... args) {
        show(format, Toast.LENGTH_LONG, args);
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     */
    private static void show(@StringRes int resId, int duration) {
        if(Abase.getContext()==null) return;
        show(Abase.getContext().getResources().getText(resId).toString(), duration);
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     * @param args     参数
     */
    private static void show(@StringRes int resId, int duration, Object... args) {
        show(String.format(Abase.getContext().getResources().getString(resId), args), duration);
    }

    /**
     * 显示吐司
     *
     * @param format   格式
     * @param duration 显示时长
     * @param args     参数
     */
    private static void show(String format, int duration, Object... args) {
        show(String.format(format, args), duration);
    }

    /**
     * 显示吐司
     *
     * @param text     文本
     * @param duration 显示时长
     */
    private static void show(CharSequence text, int duration) {
        try {
            if (TextUtils.isEmpty(text))
                return;
            cancel();
            if (customView != null) {
                sToast = new Toast(Abase.getContext());
                sToast.setView(customView);
                sToast.setDuration(duration);
            } else {
                sToast = Toast.makeText(Abase.getContext(), text, duration);
            }
            sToast.setGravity(gravity, xOffset, yOffset);
            sToast.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示吐司
     *
     * @param text     文本
     */
    public static void showCenterShort(CharSequence text) {
        try {
            if (TextUtils.isEmpty(text))
                return;
            cancel();
            if (customView != null) {
                sToast = new Toast(Abase.getContext());
                sToast.setView(customView);
                sToast.setDuration(Toast.LENGTH_SHORT);
            } else {
                sToast = Toast.makeText(Abase.getContext(), text, Toast.LENGTH_SHORT);
            }
            int textViewId = Resources.getSystem().getIdentifier("message", "id", "android");
            if(textViewId!=0&&sToast.getView()!=null){
                View view = sToast.getView().findViewById(textViewId);
                if(view instanceof TextView){
                    ((TextView)view).setGravity(Gravity.CENTER);
                }
            }
            sToast.setGravity(Gravity.CENTER, 0, 0);
            sToast.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消吐司显示
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}