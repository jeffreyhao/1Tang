package com.baidu.baselibrary.main;


import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.baidu.baselibrary.fragment.CustomFragment;

import java.util.List;

import androidx.core.content.res.ResourcesCompat;

/**
 * Created by haojiangfeng on 2023/8/31.
 */
public class MainTabConfig {




    public static final int INIT        = -1;
    public static final int TAB_0       = 0;
    public static final int TAB_1       = 1;
    public static final int TAB_2       = 2;
    public static final int TAB_3       = 3;
    public static final int TAB_4       = 4;

    public static final int TAB_INDEX_BOOKSHELF     = TAB_0; //书架
    public static final int TAB_INDEX_BOOKSTORE     = TAB_1; //书城



    public static int NUM_TABS;

    public static int sPosition = INIT;

    public static List<ItemStyle> mItemConfig;

    private static OnMainTabConfigCallback sConfigCallback;




    public interface OnMainTabConfigCallback {

        List<ItemStyle> config();
    }



    public static class ItemStyle {

        public int tabNameRes;
        public Class<? extends CustomFragment> clazz;

        public int textColorRec;
        public int imageDrawableRes;
        public int topLineDrawableRes = 0;
        public int bottomLineDrawableRes = 0;
        public int animatorRes = 0;

        // 这几个，是神策统计用
        public String pageName;
        public String pageElementName;
        public String pageElementId;


        public ColorStateList getTitleColor(){
            return ResourcesCompat.getColorStateList(App.getResources(), textColorRec, App.getAppTheme());
        }

        public Drawable getImageDrawable(){
            return ResourcesCompat.getDrawable(App.getResources(), imageDrawableRes, App.getAppTheme());
        }

        public Drawable getTopLineDrawable(){
            return topLineDrawableRes == 0 ? null : ResourcesCompat.getDrawable(App.getResources(), topLineDrawableRes, App.getAppTheme());
        }

        public Drawable getBottomLineDrawable(){
            return bottomLineDrawableRes == 0 ? null :  ResourcesCompat.getDrawable(App.getResources(), bottomLineDrawableRes, App.getAppTheme());
        }
    }


    public static List<ItemStyle> getConfig(){
        if(mItemConfig == null){
            mItemConfig = (sConfigCallback == null ? null : sConfigCallback.config());
        }
        return mItemConfig;
    }

    public static void setMainTabConfig(OnMainTabConfigCallback callback){
        sConfigCallback = callback;
        if(sConfigCallback != null){
            mItemConfig = sConfigCallback.config();
        }
        NUM_TABS = mItemConfig == null ? 0 : mItemConfig.size();
    }


    public static CustomFragment buildFragment(int position) {
        CustomFragment fragment = null;
        try {
            ItemStyle item = mItemConfig.get(position);
            Class<? extends CustomFragment> clazz = item.clazz;
            fragment = clazz.newInstance();
        } catch (Throwable e){
            LogUtil.e(e);
        }
        if(fragment != null){
            Bundle bundle = new Bundle();
            bundle.putInt("index", position);
            fragment.setArguments(bundle);
        }
        return fragment;
    }
}
