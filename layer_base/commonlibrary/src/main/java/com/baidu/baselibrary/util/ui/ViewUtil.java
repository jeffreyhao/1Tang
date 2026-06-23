package com.baidu.baselibrary.util.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.baidu.baselibrary.log.ALog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewUtil {

    public abstract static class ViewGoneMatcher {
        private Object mObject;
        public ViewGoneMatcher(Object object){
            this.mObject = object;
        }
        abstract boolean matchGone();
        abstract String getText();
    }

    public static void showViews(View... views){
        if(views == null){
            return;
        }
        for(View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideViews(View... views){
        if(views == null){
            return;
        }
        for(View view : views) {
            view.setVisibility(View.GONE);
        }
    }


    public static void setVisible(View view, boolean visible){
        if(view == null){
            return;
        }
        if(visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void setTextOrGone(TextView textView, String text){
        if(textView == null){
            return;
        }
        if(TextUtils.isEmpty(text)){
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    public static void setTextOrGone(TextView textView, int number){
        if(textView == null){
            return;
        }
        if(number == 0){
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(number));
        }
    }

    public static void setNumOrGone(TextView textView, int number){
        if(number == 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(number));
        }
    }

    public static void setNumOrParentGone(TextView textView, int number){
        ViewGoneMatcher matcher = new ViewGoneMatcher(number) {
            @Override
            boolean matchGone() {
                return number == 0;
            }

            @Override
            String getText() {
                return String.valueOf(number);
            }
        };
        setTextOrParentGone(textView, matcher);
    }

    public static void setTextOrParentGone(TextView textView, String number){
        ViewGoneMatcher matcher = new ViewGoneMatcher(number) {
            @Override
            boolean matchGone() {
                return TextUtils.isEmpty(number);
            }

            @Override
            String getText() {
                return String.valueOf(number);
            }
        };
        setTextOrParentGone(textView, matcher);
    }



    public static void setTextOrGone(TextView textView, ViewGoneMatcher matcher){
        if(textView == null || matcher == null){
            return;
        }
        if(matcher.matchGone()){
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(matcher.getText());
        }
    }

    public static void setTextOrParentGone(TextView textView, ViewGoneMatcher matcher){
        if(textView == null || matcher == null){
            return;
        }
        ViewParent parent = textView.getParent();
        if(!(parent instanceof ViewGroup)){
            return;
        }
        if(matcher.matchGone()){
            ((ViewGroup) parent).setVisibility(View.GONE);
        } else {
            ((ViewGroup) parent).setVisibility(View.VISIBLE);
            textView.setText(matcher.getText());
        }
    }

    /**
     * 适配导航栏
     * <p/>
     * android15系统上强制使用全面屏，导航栏会盖在view上。
     */
    public static void adapterNavigationBar(View rootView){
        if(rootView == null){
            return;
        }
        ViewCompat.setOnApplyWindowInsetsListener(rootView, new androidx.core.view.OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View view, @NonNull WindowInsetsCompat insets) {
                int navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
                rootView.setPadding(
                        view.getPaddingLeft(),
                        view.getPaddingTop(),
                        view.getPaddingRight(),
                        navBarHeight
                );
                return insets; // 返回原 Insets
            }
        });
    }


    /**
     * 截取 view 内容成 Bitmap
     *
     * @param view 需要截图的 view
     * @return Bitmap 截图，失败返回 null
     */
    @Nullable
    public static Bitmap snapshot(View view) {
        if (view == null) {
            return null;
        }
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }

        // 创建 Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        try {
            view.draw(canvas);
        } catch (Exception e) {
            ALog.exception(e);
            return null;
        }

        return bitmap;
    }


}
