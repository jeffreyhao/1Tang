package com.benefit.webhook;

import android.text.TextUtils;

import com.baidu.baselibrary.util.date.DateUtil;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.base.global.PreferencesUtil;
import com.base.util.AppUtil;
import com.benefit.novelverse.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by haojiangfeng on 2025/5/28.
 */
public class WebHookInit {


    /**
     * 发版后首次运行 7天内的崩溃，报给钉钉
     */
    private static final int DAYS   = 7;



    public static boolean shouldExecute() {
        String spVersionDate = PreferencesUtil.get(Constant.SP_CONSTANT.VERSION_CRASH_DING, "");  // 1.3.5_2025-05-22
        String currentVersionName = AppUtil.getVersionName();  // 1.3.5

        if(TextUtils.isEmpty(spVersionDate)){
            PreferencesUtil.put(Constant.SP_CONSTANT.VERSION_CRASH_DING, currentVersionName + "_" + DateUtil.getFixedDateYMD());
            return true;
        }
        String[] parts = spVersionDate.split("_"); // 拆分本地存储的版本与日期
        if (parts.length != 2) {
            PreferencesUtil.put(Constant.SP_CONSTANT.VERSION_CRASH_DING, currentVersionName + "_" + DateUtil.getFixedDateYMD());
            return false;
        }
        String localVersion = parts[0];
        String localDateStr = parts[1];

        if (isNewerVersion(currentVersionName, localVersion)){
            PreferencesUtil.put(Constant.SP_CONSTANT.VERSION_CRASH_DING, currentVersionName + "_" + DateUtil.getFixedDateYMD());
            return true;
        }

        // 日期逻辑
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.formatYMD, Locale.ENGLISH);
            Date versionFirstRunDate = sdf.parse(localDateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(versionFirstRunDate);
            calendar.add(Calendar.DAY_OF_YEAR, DAYS);  // 加 3 天

            Date deadline = calendar.getTime();
            Date today = new Date();

            // 当前日期在版本首次运行 +{DAYS} 天内
            return !today.after(deadline);

        } catch (Throwable e) {
            LogUtil.e(e);
            return false;
        }
    }

    private static boolean isNewerVersion(String currentVersion, String localVersion) {
        String[] currentParts = currentVersion.split("\\.");
        String[] localParts = localVersion.split("\\.");
        int maxLength = Math.max(currentParts.length, localParts.length);

        for (int i = 0; i < maxLength; i++) {
            int current = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int local = i < localParts.length ? Integer.parseInt(localParts[i]) : 0;

            if (current > local) return true;
            if (current < local) return false;
        }

        return false; // 两个版本相等
    }


}
