package com.baidu.baselibrary.util.date;

import android.annotation.SuppressLint;
import android.os.SystemClock;

import com.baidu.baselibrary.log.ALog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by haojiangfeng on 2024/6/21.
 */
public class TimeUtil {

    public static final String GMT_08                               = "GMT+08";

    /**
     * 服务器时间。  ⚠️ 单位：秒
     */
    public volatile static long serverTimeSeconds = 0L;
    /**
     * 初始化服务器时间 所对应的 cpu时间
     */
    public volatile static long bindCpuTime = 0L;
    /**
     * 是否启用服务器时间
     */
    public volatile static boolean useServeTime = false;



    /**
     * 更新服务器时间
     */
    public static void updateTs(String apiPath, JSONObject jsonObject) {
        try {
            long ts = jsonObject.optLong("_ts", 0); // 10位时间戳，单位：秒
            if(ts == 0) {
                return;
            }
            if (serverTimeSeconds == 0L || bindCpuTime == 0L) {
                serverTimeSeconds = ts;
                bindCpuTime = SystemClock.elapsedRealtime();
                ALog.textSingle("TimeUtil", "updateTs",  "接口 " + apiPath + " 初始化服务器时间：serverTimeSeconds = " + ts + "。localMills = " + System.currentTimeMillis());
            } else {
                long estimatedServerMillis = serverTimeSeconds * 1000 + (SystemClock.elapsedRealtime() - bindCpuTime);
                long diff = Math.abs(ts * 1000 - estimatedServerMillis);
                if (diff > 10000) { // 误差大于10秒则更新
                    serverTimeSeconds = ts;
                    bindCpuTime = SystemClock.elapsedRealtime();
                    ALog.textSingle("TimeUtil", "updateTs", "服务器时间已重新同步: " + ts);
                }
            }
        } catch (Throwable e){
            ALog.exception("TimeUtil", "updateTs", e);
        }
    }

    /**
     * 获取服务器时间如果服务器时间获取错误返回手机时间
     *
     * @return 服务器时间，如果服务器时间错误返回手机时间
     */
    public static long getServerTimeOrPhoneTime() {
        if(useServeTime && serverTimeSeconds != 0 && bindCpuTime != 0) {
            return getServerTimeOrZero();
        } else {
            return System.currentTimeMillis();
        }
    }

    public static void checkAdjustLocalTime(){
        if(serverTimeSeconds == 0 || bindCpuTime == 0) {
            useServeTime = false;
            return;
        }
        long currentMills = System.currentTimeMillis();
        if (Math.abs(getServerTimeOrZero() - currentMills) > 10000) { // 误差超过10s，就使用服务器时间
            ALog.textSingle("TimeUtil", "checkAdjustLocalTime",  "启用服务器时间：serverTimeSeconds = " + serverTimeSeconds + "。localMills = " + currentMills);
            useServeTime = true;
        }
    }

    /**
     * 获取服务器的当前时间戳。单位：毫秒
     */
    public static long getServerTimeOrZero() {
        if(serverTimeSeconds == 0 || bindCpuTime == 0) {
            return 0;
        }
        // 服务器时间初始值 + cpu spend time
        return serverTimeSeconds * 1000 + SystemClock.elapsedRealtime() - bindCpuTime;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getBJTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);//设置日期格式
        df.setTimeZone(TimeZone.getTimeZone(TimeUtil.GMT_08));  //设置时区，+08是北京时间
        return df.format(new Date(time));
    }

    /**
     * 毫秒时长，转换为 hms
     */
    public static String formatMillisToHMS(long millis) {
        long hours = millis / (1000 * 60 * 60);
        long minutes = (millis / (1000 * 60)) % 60;
        long seconds = (millis / 1000) % 60;
        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);
    }



}
