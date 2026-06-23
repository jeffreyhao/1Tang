package com.baidu.baselibrary.util.click;

import android.view.KeyEvent;

/**
 * Created by haojiangfeng on 2023/7/17.
 */
public class ClickUtil {



    private static long mLastQuickClickTime = -1;



    /**
     * @return true 属于快速连续点击 应该屏蔽掉  、false 可正常进行点击
     */
    public static boolean inQuickClick() {
        return inQuickClick(500);
    }

    /**
     * @param duration
     * @return true 属于快速连续点击 应该屏蔽掉  、false 可正常进行点击
     */
    public static boolean inQuickClick(long duration) {
        boolean result = System.currentTimeMillis() - mLastQuickClickTime >= 0 && System.currentTimeMillis() - mLastQuickClickTime < duration;
        mLastQuickClickTime = System.currentTimeMillis();
        return result;
    }



    public static String actionToString(int action) {
        switch (action) {
            case KeyEvent.ACTION_DOWN:
                return "ACTION_DOWN";
            case KeyEvent.ACTION_UP:
                return "ACTION_UP";
            case KeyEvent.ACTION_MULTIPLE:
                return "ACTION_MULTIPLE";
            default:
                return Integer.toString(action);
        }
    }

}
