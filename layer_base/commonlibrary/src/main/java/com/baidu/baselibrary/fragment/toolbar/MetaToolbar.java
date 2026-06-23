package com.baidu.baselibrary.fragment.toolbar;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.baidu.baselibrary.global.statusbar.StatusBarHelper;
import com.baidu.baselibrary.util.App;
import com.base.util.ui.UIUtil;
import com.jess.baselibrary.R;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;


public class MetaToolbar extends Toolbar {
    private Drawable mNavIcon;
    /**
     * 是否需要支持沉浸式
     */
    private boolean mImmersive;
    /**
     * 是否titlebar允许绘制状态栏蒙版
     */
    private boolean mIsEnableDrawCover=true;
    private boolean mIsForceDrawCover;
    private int mStatusBarHeight;
    private Paint mStatusBarPaint;


    public MetaToolbar(Context context) {
        super(context);
        init(context, null);
    }

    public MetaToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MetaToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mStatusBarPaint = new Paint();
        mStatusBarPaint.setColor(StatusBarHelper.getStatusBarCoverColor());
    }


    //未提供获取mNavButtonView的方法
    public void setColorFilter(@ColorInt int color) {
        //返回按钮
        mNavIcon = getNavigationIcon();
        mNavIcon.mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        setTitleTextColor(color);


        //右边按钮
        if (getMenu() != null && getMenu().size() > 0) {
            View view;
            for (int i = 0; i < getMenu().size(); i++) {
                MenuItem item = (MenuItem) getMenu().getItem(i);
                Drawable iconDrawable = item.getIcon();
                if(iconDrawable != null){
                    iconDrawable.setColorFilter(color,PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
        int count = getChildCount();
        View view;
        for (int i = 0; i < count; i++) {
            view =getChildAt(i);
            if(view != null){
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(color);
//                }else if (view instanceof ITitlebarMenu) {
//                    ((ITitlebarMenu) view).setColorFilter(color);
                }
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mIsEnableDrawCover&&mImmersive && (StatusBarHelper.needDrawStatusBarCover() || mIsForceDrawCover)) {
            canvas.drawRect(0, 0, getRight(), mStatusBarHeight, mStatusBarPaint);
        }
    }

    /**
     * 设置沉浸模式
     *
     * @param immersive true 支持
     */
    public void setImmersive(boolean immersive) {
        if(mImmersive == immersive){ //Activity本身没有layout(直接启动fagment)时toolbar 被设置2次。
            return;
        }
        mImmersive = immersive;
        ViewGroup.LayoutParams params = getLayoutParams();
        if (mImmersive) {
            mStatusBarHeight = StatusBarHelper.getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
        params.height = params.height + mStatusBarHeight;
        setPadding(0,mStatusBarHeight,0,0);

    }

    public void setCoverColor(int color){
        mStatusBarPaint.setColor(color);
    }

    public void forceDrawStatusCover(boolean enable){
        mIsForceDrawCover=enable;
    }

    public void enableDrawStatusCover(boolean enable){
        mIsEnableDrawCover=enable;
        invalidate();
    }

    /**
     * 目前给添加听书图标使用。
     * @param view
     */
    public void addCustomView(View view){
        int height = (int) App.getResources().getDimension(R.dimen.height_title_bar);
        LayoutParams params = new LayoutParams(height,height);
        params.gravity = Gravity.RIGHT;
        view.setLayoutParams(params);
        addView(view,params);
    }

    public void addCustomView(View view, LayoutParams params){
        view.setLayoutParams(params);
        addView(view,params);
    }

    public void addCustomView(View view, int index){
        int height = (int) App.getResources().getDimension(R.dimen.height_title_bar);
        LayoutParams params = new LayoutParams(height,height);
        params.gravity = Gravity.RIGHT;
        view.setLayoutParams(params);
        addView(view, index);
    }


    /**
     * 添加批量下载页的tabstrip
     *
     * @param view
     */
    public void addCenteredCustomView(View view){
        int height = (int) App.getResources().getDimension(R.dimen.height_title_bar);
        LayoutParams params = new LayoutParams(UIUtil.dip2px(getContext(), 200), height);
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        addView(view,params);
    }


}
