package com.base.global;

import android.app.Activity;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.util.Map;

public class GlobalUtil {


    @Deprecated
    public static Activity getTopActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (ArrayMap) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass(); // class android.app.ActivityThread$ActivityClientRecord
                Field activityField = activityRecordClass.getDeclaredField("activity"); // android.app.Activity android.app.ActivityThread$ActivityClientRecord.activity
                activityField.setAccessible(true);
                return (Activity) activityField.get(activityRecord);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
