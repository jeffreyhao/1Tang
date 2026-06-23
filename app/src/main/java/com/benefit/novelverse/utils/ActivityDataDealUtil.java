package com.benefit.novelverse.utils;

import android.os.SystemClock;
import android.text.TextUtils;

import com.base.util.collection.ListUtil;
import com.benefit.utils.CacheUtil;
import com.github.bean.operation.ActivityDataBean;

import java.util.ArrayList;
import java.util.List;

/**
* @author lhc
* @date 2022/9/27 14:29
* @desc 活动数据处理工具类
*/
public class ActivityDataDealUtil {
    private static final List<ActivityDataBean> mShelfDialogList = new ArrayList<>();
    private static final List<ActivityDataBean> mShelfFloatList = new ArrayList<>();
    private static final List<ActivityDataBean> mDiscoverDialogList = new ArrayList<>();
    private static final List<ActivityDataBean> mDiscoverFloatList = new ArrayList<>();
    private static final List<ActivityDataBean> mMineDialogList = new ArrayList<>();
    private static final List<ActivityDataBean> mMineFloatList = new ArrayList<>();
    private static int mCurShelfDialogIndex = 0;
    private static int mCurShelfFloatIndex = 0;
    private static int mCurDiscoverDialogIndex = 0;
    private static int mCurDiscoverFloatIndex = 0;
    private static int mCurMineDialogIndex = 0;
    private static int mCurMineFloatIndex = 0;

    public static void dealActivityData(List<ActivityDataBean> list) {
        mShelfDialogList.clear();
        mShelfFloatList.clear();
        mDiscoverDialogList.clear();
        mDiscoverFloatList.clear();
        mMineDialogList.clear();
        mMineFloatList.clear();
        for(int i = 0; i<list.size(); i++) {
            ActivityDataBean item = list.get(i);
            if(item==null) continue;
            if(item.countDown>0) {
                item.countDown = item.countDown*1000 + SystemClock.elapsedRealtime();
            }
            if(CacheUtil.shouldShowActivity(item.marketingId, item.frequencyNum)) {
                if(item.position.contains(1)) {
                    addShelfData(item);
                }
                if(item.position.contains(2)) {
                    addDiscoverData(item);
                }
                if(item.position.contains(3)) {
                    addMineData(item);
                }
            }
        }
    }

    public static void addShelfData(ActivityDataBean dataBean) {
        if(dataBean.isShow==1) {
            if(dataBean.tipsType==0) {
                mShelfDialogList.add(dataBean);
            }else{
                mShelfFloatList.add(dataBean);
            }
        }
    }

    public static void addDiscoverData(ActivityDataBean dataBean) {
        if(dataBean.isShow==1) {
            if(dataBean.tipsType==0) {
                mDiscoverDialogList.add(dataBean);
            }else{
                mDiscoverFloatList.add(dataBean);
            }
        }
    }

    public static void addMineData(ActivityDataBean dataBean) {
        if(dataBean.isShow==1) {
            if(dataBean.tipsType==0) {
                mMineDialogList.add(dataBean);
            }else{
                mMineFloatList.add(dataBean);
            }
        }
    }

    public static ActivityDataBean getPageData(int curPage, boolean isDialog) {
        if(curPage==1) {
            if(isDialog) {
                return getShelfData(true);
            }else{
                return getShelfData(false);
            }
        }else if(curPage==2) {
            if(isDialog) {
                return getDiscoverData(true);
            }else{
                return getDiscoverData(false);
            }
        }else if(curPage==3) {
            if(isDialog) {
                return getMineData(true);
            }else{
                return getMineData(false);
            }
        }
        return null;
    }

    public static ActivityDataBean getShelfData(boolean isDialog) {
        if(isDialog) {
            if(ListUtil.isNotEmpty(mShelfDialogList)&&mShelfDialogList.size()>mCurShelfDialogIndex) {
                ActivityDataBean activityDataBean = mShelfDialogList.get(mCurShelfDialogIndex);
                mCurShelfDialogIndex++;
                if(mCurShelfDialogIndex>=mShelfDialogList.size()) {
                    mCurShelfDialogIndex = 0;
                }
                return activityDataBean;
            }
        }else{
            if(ListUtil.isNotEmpty(mShelfFloatList)&&mShelfFloatList.size()>mCurShelfFloatIndex) {
                ActivityDataBean activityDataBean = mShelfFloatList.get(mCurShelfFloatIndex);
                mCurShelfFloatIndex++;
                if(mCurShelfFloatIndex>=mShelfFloatList.size()) {
                    mCurShelfFloatIndex = 0;
                }
                return activityDataBean;
            }
        }
        return null;
    }

    public static ActivityDataBean getDiscoverData(boolean isDialog) {
        if(isDialog) {
            if(ListUtil.isNotEmpty(mDiscoverDialogList)&&mDiscoverDialogList.size()>mCurDiscoverDialogIndex) {
                ActivityDataBean activityDataBean = mDiscoverDialogList.get(mCurDiscoverDialogIndex);
                mCurDiscoverDialogIndex++;
                if(mCurDiscoverDialogIndex>=mDiscoverDialogList.size()) {
                    mCurDiscoverDialogIndex = 0;
                }
                return activityDataBean;
            }
        }else{
            if(ListUtil.isNotEmpty(mDiscoverFloatList)&&mDiscoverFloatList.size()>mCurDiscoverFloatIndex) {
                ActivityDataBean activityDataBean = mDiscoverFloatList.get(mCurDiscoverFloatIndex);
                mCurDiscoverFloatIndex++;
                if(mCurDiscoverFloatIndex>=mDiscoverFloatList.size()) {
                    mCurDiscoverFloatIndex = 0;
                }
                return activityDataBean;
            }
        }
        return null;
    }

    public static ActivityDataBean getMineData(boolean isDialog) {
        if(isDialog) {
            if(ListUtil.isNotEmpty(mMineDialogList)&&mMineDialogList.size()>mCurMineDialogIndex) {
                ActivityDataBean activityDataBean = mMineDialogList.get(mCurMineDialogIndex);
                mCurMineDialogIndex++;
                if(mCurMineDialogIndex>=mMineDialogList.size()) {
                    mCurMineDialogIndex = 0;
                }
                return activityDataBean;
            }
        }else{
            if(ListUtil.isNotEmpty(mMineFloatList)&&mMineFloatList.size()>mCurMineFloatIndex) {
                ActivityDataBean activityDataBean = mMineFloatList.get(mCurMineFloatIndex);
                mCurMineFloatIndex++;
                if(mCurMineFloatIndex>=mMineFloatList.size()) {
                    mCurMineFloatIndex = 0;
                }
                return activityDataBean;
            }
        }
        return null;
    }

    public static void removeShelfData(int index, boolean isDialog) {
        if(isDialog) {
            mShelfDialogList.remove(index);
            if(mCurShelfDialogIndex >= mShelfDialogList.size()) {
                mCurShelfDialogIndex = 0;
            }
        }else{
            mShelfFloatList.remove(index);
            if(mCurShelfFloatIndex >= mShelfFloatList.size()) {
                mCurShelfFloatIndex = 0;
            }
        }
    }

    public static void removeDiscoverData(int index, boolean isDialog) {
        if(isDialog) {
            mDiscoverDialogList.remove(index);
            if(mCurDiscoverDialogIndex >= mDiscoverDialogList.size()) {
                mCurDiscoverDialogIndex = 0;
            }
        }else{
            mDiscoverFloatList.remove(index);
            if(mCurDiscoverFloatIndex >= mDiscoverFloatList.size()) {
                mCurDiscoverFloatIndex = 0;
            }
        }
    }

    public static void removeMineData(int index, boolean isDialog) {
        if(isDialog) {
            mMineDialogList.remove(index);
            if(mCurMineDialogIndex >= mMineDialogList.size()) {
                mCurMineDialogIndex = 0;
            }
        }else{
            mMineFloatList.remove(index);
            if(mCurMineFloatIndex >= mMineFloatList.size()) {
                mCurMineFloatIndex = 0;
            }
        }
    }

    public static void removePageData(int mCurPageValue, String id, boolean isDialog) {
        if(TextUtils.isEmpty(id)) return;
        if(isDialog) {
            for(int i=0; i<mShelfDialogList.size();i++) {
                if(TextUtils.equals(id,mShelfDialogList.get(i).marketingId)) {
                    removeShelfData(i,true);
                    break;
                }
            }
            for(int i=0; i<mDiscoverDialogList.size();i++) {
                if(TextUtils.equals(id,mDiscoverDialogList.get(i).marketingId)) {
                    removeDiscoverData(i,true);
                    break;
                }
            }
            for(int i=0; i<mMineDialogList.size();i++) {
                if(TextUtils.equals(id,mMineDialogList.get(i).marketingId)) {
                    removeMineData(i,true);
                    break;
                }
            }
        }else{
            for(int i=0; i<mShelfFloatList.size();i++) {
                if(TextUtils.equals(id,mShelfFloatList.get(i).marketingId)) {
                    removeShelfData(i,false);
                    break;
                }
            }
            for(int i=0; i<mDiscoverFloatList.size();i++) {
                if(TextUtils.equals(id,mDiscoverFloatList.get(i).marketingId)) {
                    removeDiscoverData(i,false);
                    break;
                }
            }
            for(int i=0; i<mMineFloatList.size();i++) {
                if(TextUtils.equals(id,mMineFloatList.get(i).marketingId)) {
                    removeMineData(i,false);
                    break;
                }
            }
        }
    }

    /**
     * 情况活动缓存数据
     */
    public static void clearData() {
        mShelfDialogList.clear();
        mShelfFloatList.clear();
        mDiscoverDialogList.clear();
        mDiscoverFloatList.clear();
        mMineDialogList.clear();
        mMineFloatList.clear();
    }
}
