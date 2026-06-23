package com.base.api;

import com.base.abs.INight;

/**
 * Created by haojiangfeng on 2025/2/13.
 */
public class NightApi {

    public static INight iNight;


    public static boolean isNightMode(){
        if(iNight != null){
            return iNight.isNightMode();
        }
        return false;
    }


}
