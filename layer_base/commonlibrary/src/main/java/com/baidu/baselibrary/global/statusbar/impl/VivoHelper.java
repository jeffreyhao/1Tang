package com.baidu.baselibrary.global.statusbar.impl;

import android.os.Build;

import com.base.util.content.StringUtils;
import com.base.util.DevicesUtil;


/**
 * Vivo 深色字体帮助类
 */
public class VivoHelper {
    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为VIVO用户并且为Funtouch os 2.5及以下版本
     *
     * @return boolean 成功执行返回true
     */
    public static boolean setStatusBarLightMode() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getVivoType() > 0;
    }

    public static int getVivoType(){
        String vivoVersion = DevicesUtil.getSystemProperty("ro.vivo.os.version");
        if (!StringUtils.isEmptyNull(vivoVersion)) {
            try {
                String version[] = vivoVersion.split("\\.");
                if ((version.length == 1 && Integer.valueOf(version[0]) <= 2) ||
                        (version.length == 2 &&
                                (Integer.valueOf(version[0]) < 2 ||
                                        (Integer.valueOf(version[0]) == 2 && Integer.valueOf(version[1]) <= 5))) ||
                        (version.length > 2 &&
                                (Integer.valueOf(version[0]) < 2 ||
                                        (Integer.valueOf(version[0]) == 2 && Integer.valueOf(version[1]) < 5)))){
                    return 0;//2.5.1以下
                }else {
                    return 1;//2.5.1以上
                }
            } catch (Throwable e) {
                return 0;
            }
        }
        return -1;
    }
}
