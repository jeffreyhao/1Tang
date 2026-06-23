package com.baidu.baselibrary.util.sys;

import com.base.global.GlobalBuildConfig;

import org.greenrobot.eventbus.EventBus;

/**
 * lhc
 * EventBus  注册 发送
 */
public class EventUtil {

    public static void register(Object obj) {
        if(obj == null){
            return;
        }
        if(!EventBus.getDefault().isRegistered(obj)){
            EventBus.getDefault().register(obj);
        }
        if(GlobalBuildConfig.DEBUG){
            LogUtil.v("EventUtil", "register: " + obj.getClass().getSimpleName());
        }
    }

    public static void unregister(Object obj) {
        if(obj == null){
            return;
        }
        if(EventBus.getDefault().isRegistered(obj)){
            EventBus.getDefault().unregister(obj);
        }
        if(GlobalBuildConfig.DEBUG){
            LogUtil.v("EventUtil", "unregister: " + obj.getClass().getSimpleName());
        }
    }

    /**
     * ⚠️注意：不要滥用EventBus。
     *      1. 一对多关系，可以使用EventBus。
     *      2. 一对一关系，需要回调的地方，优先使用接口。
     * @param obj
     */
    public static void post(Object obj) {
        if(obj == null){
            return;
        }
        EventBus.getDefault().post(obj);
        if(GlobalBuildConfig.DEBUG){
            LogUtil.v("EventUtil", "post: " + obj.getClass().getSimpleName());
        }
    }

    public static void postSticky(Object obj) {
        if(obj == null){
            return;
        }
        EventBus.getDefault().postSticky(obj);
        if(GlobalBuildConfig.DEBUG){
            LogUtil.v("EventUtil", "postSticky: " + obj.getClass().getSimpleName());
        }
    }

    /**
     * @param obj 调用register的页面，而非event
     * @return
     */
    public static boolean isRegistered(Object obj) {
        if(obj == null){
            return false;
        }
        boolean isRegistered = EventBus.getDefault().isRegistered(obj);
        if(GlobalBuildConfig.DEBUG){
            LogUtil.v("EventUtil",  obj.getClass().getSimpleName() + "isRegistered: " + isRegistered);
        }
        return isRegistered;
    }

}
