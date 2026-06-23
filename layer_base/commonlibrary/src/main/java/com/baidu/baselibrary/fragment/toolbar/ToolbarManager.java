package com.baidu.baselibrary.fragment.toolbar;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;


import com.jess.baselibrary.R;

import androidx.appcompat.widget.Toolbar;


/**
 * 1.提供给 Activity 和 Fragment创建 toolbar
 * 2.设置 toolbar 通用的配置
 */
public class ToolbarManager {
    private   IToolbar  mIToolbar;
    protected MetaToolbar mToolbar;
    private   boolean   mIsInitted;

    public MetaToolbar createActivityToolbar(IToolbar iToolbar, Activity activity){
//        if(activity instanceof ActivityContainer || activity instanceof CaptureActivity){
//            return null;
//        }
        if(mToolbar == null){
            mIToolbar = iToolbar;
            mToolbar = (MetaToolbar)activity.findViewById(R.id.toolbar_layout_id);
        }
        return mToolbar;
    }


    public MetaToolbar createFragmentToolbar(IToolbar iToolbar, View rootView){
        if(mToolbar == null) {
            mIToolbar = iToolbar;
            mToolbar = (MetaToolbar) rootView.findViewById(R.id.toolbar_layout_id);
        }
        return mToolbar;
    }

    public void initToolbar(boolean hasPadding) {
        if(!mIsInitted){
            setToolbarListener();
            mToolbar.setImmersive(hasPadding);
            mIToolbar.assembleToolbar();
            mIsInitted = true;
        }
    }

    private void setToolbarListener() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mIToolbar.onToolMenuItemClick(item);
                return false;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIToolbar.onNavigationClick(view);
            }
        });
    }
}
