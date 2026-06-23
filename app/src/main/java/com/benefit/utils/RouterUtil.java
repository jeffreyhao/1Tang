package com.benefit.utils;

import android.text.TextUtils;

import com.base.global.GlobalBuildConfig;
import com.base.util.AppUtil;

/**
 * Created by haojiangfeng on 2023/9/12.
 */
public class RouterUtil {


    public static String replaced(String url) {
        if (!TextUtils.isEmpty(url)) {
            return url.replace(
                    "yuanshu://com.foreader.yuanshu",
                    RouterUtil.getSchemeRouter());
        }
        return url;
    }


    /**
     * @return  novelverse://com.benefit.novelverse/feedback/4
     */
    public static String buildFeedbackRouter(){
        return getSchemeRouter() + "feedback/4";
    }


    public static String getSchemeRouter() {
//        String routerHost = TextUtils.equals(PRODUCT_ID_NOVELHIVE, getProductId())?"com.star.novelhive":GlobalBuildConfig.APPLICATION_ID;
        String routerHost = GlobalBuildConfig.APPLICATION_ID;
        return GlobalBuildConfig.ROUTER_ID + "://" + routerHost + "/";
//        return AppUtil.getSchemeRouter();
    }





}
