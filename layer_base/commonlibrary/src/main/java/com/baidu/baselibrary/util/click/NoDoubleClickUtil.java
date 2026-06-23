package com.baidu.baselibrary.util.click;

import android.os.SystemClock;
import android.view.View;

import com.jess.baselibrary.R;

/**
* @author lhc
* @date 2022/5/9 9:30
* @desc 防止快速点击工具类
*/
public class NoDoubleClickUtil {

    private static long lastClickTime = 0;

    public static final int MIN_CLICK_DELAY_TIME = 300;

    /**
     * @return 全局快速点击
     */
    public static boolean isFastClick() {
        return isFastClick(MIN_CLICK_DELAY_TIME);
    }

    /**
     * @return 全局快速点击
     */
    public static boolean isFastClick(int delayTime) {
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastClickTime > delayTime) {
            lastClickTime = currentTime;
            return false;
        }else {
            return true;
        }
    }

    /**
     * @return 指定view的快速点击
     */
    public static boolean isFastClick(View view) {
        return isFastClick(view, MIN_CLICK_DELAY_TIME);
    }

    /**
     * @return 指定view的快速点击
     */
    public static boolean isFastClick(View view, int delayTime) {
        long currentCpuTime = SystemClock.elapsedRealtime();
        Object lastClickTimeTag = view.getTag(R.id.tag_last_click_cpu_time);
        long lastClickCpuTime = lastClickTimeTag == null ? 0 : (long) lastClickTimeTag;
        long spendCpuTime = currentCpuTime - lastClickCpuTime;
        if (lastClickCpuTime == 0 || spendCpuTime > delayTime) {
            view.setTag(R.id.tag_last_click_cpu_time, currentCpuTime);
            return false;
        }else {
            return true;
        }
    }

    /**
     * @param lastClickCpuTime
     * @param delayTime
     * @return
     */
    public static boolean isFastClick(long lastClickCpuTime, int delayTime) {
        long currentTime = SystemClock.elapsedRealtime();
        if (lastClickCpuTime == 0 || currentTime - lastClickCpuTime > delayTime) {
            return false;
        }else {
            return true;
        }
    }

}
