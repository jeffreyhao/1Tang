package com.base.net.webhook;

import com.base.net.ApiBase;

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
        if(ApiBase.DEBUG || canSendDing){
            WebHookCrashUtil.sendCrash(true, thread, ex, position);
        }
    }

    public static void sendException(Thread thread, Throwable ex, String position){
        if(ApiBase.DEBUG || canSendDing){
            WebHookCrashUtil.sendCrash(false, thread, ex, position);
        }
    }

    public static void sendFeedback(Map<String, Object> params, Object response){
        WebHookFeedbackUtil.sendFeedback(params, response);
    }

    public static void monitorError(String text, String position, String... params){
        WebHookMonitorUtil.sendMonitor(WebHookUtil.TYPE_ERROR, text, position, params);
    }

    public static void monitorWarning(String text, String position, String... params){
        WebHookMonitorUtil.sendMonitor(WebHookUtil.TYPE_WARNING, text, position, params);
    }

    public static void monitorTest(String message, String text){
        WebHookMonitorUtil.sendTest(message, text);
    }

    public static void monitorDeepLink(String text, String position, String... params){
        WebHookMonitorUtil.sendMonitor(WebHookUtil.TYPE_DEEPLINK, text, position, params);
    }

    public static void monitorRead(String text, String position, String... params){
        WebHookMonitorUtil.sendMonitor(WebHookUtil.TYPE_READ, text, position, params);
    }

    public static void monitorPurchase(String text, String position, String... params){
        WebHookMonitorUtil.sendMonitor(WebHookUtil.TYPE_PURCHASE, text, position, params);
    }

    public static void monitorOrder(String text, String position, String... params){
        WebHookMonitorUtil.sendMonitor(WebHookUtil.TYPE_ORDER, text, position, params);
    }

    public static void monitorReachMaxWarning(String text, String position, String... params){
        WebHookMonitorUtil.sendMonitor(WebHookUtil.TYPE_REACH_MAX_W, text, position, params);
    }

}
