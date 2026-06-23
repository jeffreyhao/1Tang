package com.base.util.xyz;

import com.base.api.GlobalContext;
import com.blankj.utilcode.util.AppUtils;
import com.example.crypto.XyzKQ99;


/**
 * Created by haojiangfeng on 2025/7/1.
 */
public class Dec {

    public static boolean init(){
        return XyzKQ99.init();
    }

    public static byte[] doSth(String sha256, byte[] encrypted){
        String packageName = AppUtils.getAppPackageName();
        return XyzKQ99.doSth(GlobalContext.getContext(), encrypted, packageName, sha256);
    }

    public static boolean c_xcyh() {
        return XyzKQ99.c_xcyh(GlobalContext.getContext(), AppUtils.getAppPackageName());
    }

    public static boolean cc_xcyh() {
        return XyzKQ99.ttrc();
    }

    public static byte[] rrccres(byte[] bytes) {
        return XyzKQ99.rrccres(bytes);
    }

}
