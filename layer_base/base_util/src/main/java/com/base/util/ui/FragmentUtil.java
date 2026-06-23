package com.base.util.ui;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.base.api.Logger;
import com.base.util.AppUtil;

/**
 * Created by haojiangfeng on 2025/6/3.
 */
public class FragmentUtil {


    public static void show(FragmentActivity activity, DialogFragment fragment, String tag){
        if(activity == null || activity.isDestroyed() || activity.isFinishing()){
            return;
        }
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        if (!supportFragmentManager.isStateSaved()) {
            fragment.show(supportFragmentManager, tag);
        } else {
            AppUtil.post(()->{
                if (!supportFragmentManager.isStateSaved()) {
                    fragment.show(supportFragmentManager, tag);
                } else {
                    Logger.e("fragment[" + tag + "] do not show, because fragmentManager isStateSaved().");
                }
            });
        }
    }

    public static void show(FragmentManager supportFragmentManager, DialogFragment fragment, String tag){
        if(supportFragmentManager == null){
            return;
        }
        if(!supportFragmentManager.isStateSaved()) {
            fragment.show(supportFragmentManager, tag);
        } else {
            AppUtil.post(()->{
                if(!supportFragmentManager.isStateSaved()){
                    fragment.show(supportFragmentManager, tag);
                }
            });
        }
    }


    public static <T> boolean containsClass(Fragment target, Fragment... array){
        if(target == null || array == null) {
            return false;
        }
        for(Fragment item : array){
            if(item.getClass().equals(target.getClass())){
                return true;
            }
        }
        return false;
    }

    public static <T> boolean contains(Fragment target, Fragment... array){
        if(target == null || array == null) {
            return false;
        }
        for(Fragment item : array){
            if(item == target){
                return true;
            }
        }
        return false;
    }


}
