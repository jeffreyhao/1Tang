package com.github.bean.adv;

import com.baidu.baselibrary.util.date.DateUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AdvUnlockMap implements Serializable {

    public static AdvUnlockMap newInstance(){
        AdvUnlockMap unlockMap = new AdvUnlockMap();
        unlockMap.date = DateUtil.getFixedTodayString();
        unlockMap.watchedMap = new HashMap<>();
        return unlockMap;
    }

    @SerializedName("date")
    public String date;

    @SerializedName("watchedMap")
    public Map<String, Integer> watchedMap;

    public AdvUnlockMap(){

    }

    public int getUnlockedTimes(int configUnlockChapters){
        if(configUnlockChapters == 0){
            return 0;
        }
        if(watchedMap.isEmpty()) {
            return 0;
        }
        int targetTimes = 0;
        for(int count: watchedMap.values()) {
            if(count >= configUnlockChapters) {
                targetTimes ++;
            }
        }
        return targetTimes;
    }

}
