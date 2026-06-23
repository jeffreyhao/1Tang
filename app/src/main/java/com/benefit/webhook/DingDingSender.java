package com.benefit.webhook;


import com.base.global.GlobalBuildConfig;

import java.util.Map;

/**
 * Created by haojiangfeng on 2025/3/26.
 */
public class DingDingSender {



    private static boolean canSendDing = false;



    public static void init(){
        canSendDing = WebHookInit.shouldExecute();
    }

    public static void sendCrash(Thread thread, Throwable ex, String position){
        if(GlobalBuildConfig.DEBUG || canSendDing){
            WebHookCrashUtil.sendCrash(thread, ex, position);
        }
    }


    public static void sendFeedback(Map<String, Object> params, Object response){
        WebHookFeedbackUtil.sendFeedback(params, response);
    }



}
