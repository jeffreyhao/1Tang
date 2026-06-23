package com.baidu.baselibrary.global.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于存储全局运行数据的类 可以放置一些全局常量 比如屏幕宽高 跨界面传递数据
 */
public class GlobalObserver {
    private static GlobalObserver mInstance = new GlobalObserver();

    /** 个人中心用户信息更新观察者 */
//    private List<EpubFontSwitchObserver> mEpubFontSwitchObservers;

    private List<NightChangeObserver> mNightChangeObservers;

    private GlobalObserver() {
        init();
    }

    public static GlobalObserver getInstance() {
        return mInstance;
    }

    private void init() {
//        mEpubFontSwitchObservers = new ArrayList<>();
        mNightChangeObservers = new ArrayList<>();
    }


    public interface EpubFontSwitchObserver {
        void onEpubFontSwitchRefresh(boolean isSwitchOn);
    }


    public void registerEpubFontSwitchObserver(EpubFontSwitchObserver ob) {
//        synchronized (mEpubFontSwitchObservers) {
//            if (null != ob) {
//                if (!mEpubFontSwitchObservers.contains(ob)) {
//                    mEpubFontSwitchObservers.add(ob);
//                }
//            }
//        }
    }

    public void unRegisterEpubFontSwitchObserver(EpubFontSwitchObserver ob) {
//        synchronized (mEpubFontSwitchObservers) {
//            mEpubFontSwitchObservers.remove(ob);
//        }
    }

    public void notifyEpubFontSwitchChange(boolean isSwitchOn){
//        synchronized (mEpubFontSwitchObservers) {
//            for (EpubFontSwitchObserver observer : mEpubFontSwitchObservers) {
//                observer.onEpubFontSwitchRefresh(isSwitchOn);
//            }
//        }
    }

    public interface NightChangeObserver {
        void onNightChanged();
    }


    public void registerNightChangeObserver(NightChangeObserver ob) {
        synchronized (mNightChangeObservers) {
            if (null != ob) {
                if (!mNightChangeObservers.contains(ob)) {
                    mNightChangeObservers.add(ob);
                }
            }
        }
    }

    public void unRegisterNightChangeObserver(NightChangeObserver ob) {
        synchronized (mNightChangeObservers) {
            mNightChangeObservers.remove(ob);
        }
    }

    public void notifyNightChange(){
        synchronized (mNightChangeObservers) {
            for (NightChangeObserver observer : mNightChangeObservers) {
                observer.onNightChanged();
            }
        }
    }
}