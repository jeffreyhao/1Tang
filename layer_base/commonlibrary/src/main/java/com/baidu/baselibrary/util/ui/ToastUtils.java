package com.baidu.baselibrary.util.ui;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;

import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.blankj.utilcode.util.ActivityUtils;


/**
 * Toast工具类
 */

public class ToastUtils {
//    /**
//     * Toast实例，用于对出现的所有Toast进行处理
//     */
//    private static Toast toast;

//    /**
//     * 此处是一个封装的Toast方法，可以取消掉上一次未完成的，直接进行下一次Toast * @param context context * @param text 需要toast的内容
//     */
//    public static void showToast(String text, int duration) {
//        if(TextUtils.isEmpty(text)){
//            return;
//        }
//        if (toast != null) {
//            toast.cancel();
//        }
//        toast = Toast.makeText(App.getAppContext(), text, duration);
//        toast.show();
//    }

    public static void showToastCenter(String text) {
        if(TextUtils.isEmpty(text)){
            return;
        }
        show(text, false);
    }

    public static void showToastCenterLong(String text) {
        if(TextUtils.isEmpty(text)){
            return;
        }
        show(text, true);
    }

    private static void show(String text, boolean isLongDuration){
        Activity topActivity  = ActivityUtils.getTopActivity();
        if(topActivity == null){
            LogUtil.e("topActivity is null -> toast()");
            toast(text, isLongDuration);
        } else if(topActivity.isDestroyed() || topActivity.isFinishing()){
            LogUtil.e("topActivity is finish -> post toast()");
            App.postDelayed(new Runnable() {
                @Override
                public void run() {
                    show(text, isLongDuration);
                }
            }, 200);
        } else {
            toast(text, isLongDuration);
        }
    }

    private static void toast(String text, boolean isLongDuration){
        com.blankj.utilcode.util.ToastUtils toastUtils = com.blankj.utilcode.util.ToastUtils.make().setGravity(Gravity.CENTER,0,0);
        if(isLongDuration){
            toastUtils.setDurationIsLong(true);
        }
        toastUtils.setMode(com.blankj.utilcode.util.ToastUtils.MODE.DARK);
        toastUtils.show(text);
    }


//    public static void showTop(CharSequence text, int topOffset) {
//        try {
//            if(null==App.getAppContext())
//                return;
//            if (TextUtils.isEmpty(text))
//                return;
//            if (toast != null) {
//                toast.cancel();
//            }
//            toast = Toast.makeText(App.getAppContext(), text, Toast.LENGTH_LONG);
//            int textViewId = Resources.getSystem().getIdentifier("message", "id", "android");
//            if(textViewId!=0 && toast.getView()!=null){
//                View view = toast.getView().findViewById(textViewId);
//                if(view instanceof TextView){
//                    ((TextView)view).setGravity(Gravity.CENTER);
//                }
//            }
//            toast.setGravity(Gravity.TOP,0, UIUtil.getStatusBarHeight(App.getAppContext())+topOffset);
//            toast.show();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void showBottom(CharSequence text, int bottomOffset) {
//        try {
//            if(null==App.getAppContext())
//                return;
//            if (TextUtils.isEmpty(text))
//                return;
//            if (toast != null) {
//                toast.cancel();
//            }
//            toast = Toast.makeText(App.getAppContext(), text, Toast.LENGTH_LONG);
//            int textViewId = Resources.getSystem().getIdentifier("message", "id", "android");
//            if(textViewId!=0 && toast.getView()!=null){
//                View view = toast.getView().findViewById(textViewId);
//                if(view instanceof TextView){
//                    ((TextView)view).setGravity(Gravity.CENTER);
//                }
//            }
//            toast.setGravity(Gravity.BOTTOM,0, bottomOffset);
//            toast.show();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

}
