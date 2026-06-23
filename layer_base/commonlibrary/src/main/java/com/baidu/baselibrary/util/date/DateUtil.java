package com.baidu.baselibrary.util.date;

import com.baidu.baselibrary.util.sys.LogUtil;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.Nullable;

import com.baidu.baselibrary.log.ALog;

/**
 * Created by haojiangfeng on 2023/7/17.
 */
public class DateUtil {


    public static final int     MILLISECOND_PER_SECOND      = 1000;

    public static final int     MILLISECOND_PER_HOUR        = 3600000;
    public static final int     MILLISECOND_PER_MINUTE      = 60000;

    public static final int     SECONDS_PER_MINUTE          = 60;
    public static final int     SECONDS_PER_HOUR            = 3600;
    public static final int     SECONDS_PER_DAY             = 86400;
    public static final int     SECONDS_PER_WEEK            = 7 * SECONDS_PER_DAY;
    public static final int     SECONDS_PER_MONTH           = 30 * SECONDS_PER_DAY;

    public static final long    MILLIS_PER_DAY              = 1000L * SECONDS_PER_DAY;

    public static final long    MILLIS_PER_WEEK             = 7 * MILLIS_PER_DAY;

    public static final long    MILLIS_PER_MONTH            = 30 * MILLIS_PER_DAY;

    public static final long    MILLIS_PER_YEAR             = 365 * MILLIS_PER_DAY;



    public final static String yyyyMMddHHmmssSSS 			    = "yyyy-MM-dd HH:mm:ss.SSS";

    /** 时间日期格式化到年月日时分秒. */
    public final static String formatYMDHMS 			        = "yyyy-MM-dd HH:mm:ss";

    /** 时间日期格式化到年月日. */
    public final static String formatYMD 				        = "yyyy-MM-dd";

    /** 时间日期格式化到年月. */
    public final static String formatYM 				        = "yyyy-MM";

    /** 时间日期格式化到年. */
    public final static String formatYYYY 				        = "yyyy";


    /** 时间日期格式化到年月日时分. */
    public final static String formatYMDHM 				        = "yyyy-MM-dd HH:mm";

    /** 时间日期格式化到月日. */
    public final static String formatMD 				        = "MM/dd";

    /** 时分秒. */
    public final static String formatHMS 				        = "HH:mm:ss";

    /** 时分. */
    public final static String formatHM 				        = "HH:mm";

    /** 时 */
    public final static String formatH 					        = "HH";

    public final static String Md_Hms 				            = "MMdd-HHmmss";
    public final static String MdHmsS 				            = "MMdd.HHmmss.SSS";

    public final static String HHmmss 				            = "HHmmss";
    public final static String formatHmsSSS 			        = "HH:mm:ss.SSS";

    public final static String yyyymmddhhmmssSSS 			    = "yyyyMMddHHmmssSSS";

    public final static String MMdd_HHmmss 			            = "MMdd-HHmmss";


    public final static String YMD_T_HMSs                       = "yyyy-MM-dd'T'HH:mm:ssXXX";


    public static final String GMT_08                           = "GMT+08";



    public static void main(String[] args) {
        System.out.println(compare("2021-12-01 12:12:12","2021-12-01 12:12:11"));
        System.out.println(getDateYMD(currDateLong()));
        System.out.println(getDateYMDHMS((currDateLong())));
        System.out.println(getDateYMDHM(currDateLong()));
        System.out.println(getDateLong(new Date()));
        System.out.println(getDateLong("2021-12-01 12:12:12"));
        System.out.println(getTimeZoneGMT());
    }



    /**
     * @return 设备时间
     */
    public static long currDateLong() {
        return System.currentTimeMillis();
    }

    /**
     * @return 获取经过修正的时间戳。单位 ms
     */
    public static long getFixedTimeStamp(){
        // 优先获取服务器时间，没有就拿手机时间
        return TimeUtil.getServerTimeOrPhoneTime();
    }

    public static String getFixedTodayString(){
        return getFixedDateYMD();
    }

    public static String getFixedHmsString(){
        Date date = new Date(DateUtil.getFixedTimeStamp());
        return DateUtil.getTimeFormatStr(date, DateUtil.formatHMS);
    }

    /**
     * @return 获取当前日期，将字符串转换为整数yyyyMMdd
     */
    public static int currDate() {
        Date date = new Date(); //当前时间，转为整数存储
        return Integer.parseInt(DateFormat.format("yyyyMMdd", date).toString());
    }

    /**
     * @return 根据时间获取到Long的时间
     */
    public static long getDateLong(Date date) {
        return date.getTime();
    }

    /**
     * @return 根据时间获取到Long的时间
     */
    public static long getDateLong(String date) {
        SimpleDateFormat format = new SimpleDateFormat(formatYMDHMS, Locale.ENGLISH);
        try {
            Date tmp  = format.parse(date);
            return tmp == null ? 0 : getDateLong(tmp);
        } catch (ParseException e) {
            ALog.exception("DateUtil", "getDateLong", e);
            return 0;
        }
    }

    /**
     * @return 获取修正过的时间字符串，格式 "yyyy-MM-dd"
     */
    public static String getFixedDateYMD(){
        Date date = new Date(DateUtil.getFixedTimeStamp());
        return DateUtil.getTimeFormatStr(date, DateUtil.formatYMD);
    }


    /**
     * 系统当前时间
     * <br>时间日期格式化到年</br>
     */
    public static String getDateYYYY(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatYYYY, Locale.ENGLISH);
        return format.format(date);
    }

    public static String getDateYYYY() {
        return getDateYYYY(new Date());
    }


    public static String getDateMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatMD, Locale.ENGLISH);
        return format.format(date);
    }

    /**
     * @return 系统当前时间
     * <br>时间日期格式化到年月日时分秒</br>
     */
    public static String getDateYMDHMS() {
        SimpleDateFormat format = new SimpleDateFormat(formatYMDHMS, Locale.ENGLISH);
        return format.format(new Date());
    }

    /**
     * @return 系统当前时间
     * <br>时间日期格式化到年月日</br>
     */
    public static String getDateYMD() {
        SimpleDateFormat format = new SimpleDateFormat(formatYMD, Locale.ENGLISH);
        return format.format(new Date());
    }

    /**
     * @return 系统当前时间
     * <br>时间日期格式化到年月日时分</br>
     */
    public static String getDateYMDHM() {
        SimpleDateFormat format = new SimpleDateFormat(formatYMDHM, Locale.ENGLISH);
        return format.format(new Date());
    }

    /**
     * @return 系统当前时间
     * <br>时间日期格式化到时分秒</br>
     */
    public static String getDateHMS() {
        SimpleDateFormat format = new SimpleDateFormat(formatHMS, Locale.ENGLISH);
        return format.format(new Date());
    }

    /**
     * @return 系统当前时间
     * <br>时间日期格式化到时分</br>
     */
    public static String getDateHM() {
        SimpleDateFormat format = new SimpleDateFormat(formatHM, Locale.ENGLISH);
        return format.format(new Date());
    }

    /**
     * @return 系统当前时间
     * <br>时间日期格式化到时</br>
     */
    public static String getDateH() {
        SimpleDateFormat format = new SimpleDateFormat(formatH, Locale.ENGLISH);
        return format.format(new Date());
    }

    /**
     * @return 获取该日期的小时时间
     *
     * <br>时间日期格式化到时</br>
     */
    public static String getDateH(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatH, Locale.ENGLISH);

        return format.format(date);
    }

    /**
     * @return 根据long 转为为响应格式的时间
     * <br>时间日期格式化到年月日时分秒</br>
     */
    public static String getDateYMDHMS(long date) {
        SimpleDateFormat format = new SimpleDateFormat(formatYMDHMS, Locale.ENGLISH);
        Date date2 = new Date(date);
        return format.format(date2);
    }

    /**
     * @return 根据long 转为为响应格式的时间
     * <br>时间日期格式化到年月日</br>
     */
    public static String getDateYMD(long date) {
        SimpleDateFormat format = new SimpleDateFormat(formatYMD, Locale.ENGLISH);
        Date date2 = new Date(date);
        return format.format(date2);
    }

    /**
     * @return 根据long 转为为响应格式的时间
     * <br>时间日期格式化到年月日时分</br>
     */
    public static String getDateYMDHM(long date) {
        SimpleDateFormat format = new SimpleDateFormat(formatYMDHM, Locale.ENGLISH);
        Date date2 = new Date(date);
        return format.format(date2);
    }

    /**
     * @return 根据long 转为为响应格式的时间
     * <br>时间日期格式化到年月日时分秒</br>
     */
    public static String getDateYMDHMS(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatYMDHMS, Locale.ENGLISH);
        return format.format(date);
    }

    /**
     * @return 根据long 转为为响应格式的时间
     * <br>时间日期格式化到年月日</br>
     */
    public static String getDateYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatYMD, Locale.ENGLISH);
        return format.format(date);
    }

    /**
     * @return 根据Date 转为为响应格式的时间
     * <br>时间日期格式化到年月日时分</br>
     */
    public static String getDateYMDHM(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatYMDHM, Locale.ENGLISH);
        return format.format(date);
    }

    /**
     * @return 根据long 转为为响应格式的时间
     * <br>时间日期格式化到时分秒</br>
     */
    public static String getDateHMS(long date) {
        SimpleDateFormat format = new SimpleDateFormat(formatHMS, Locale.ENGLISH);
        Date date2 = new Date(date);
        return format.format(date2);
    }

    /**
     * 两个时间比较
     *
     * @return 1：src 在 dst 后、 -1 dst 在 src 后 、0 相同
     */
    public static int compare(long src , long dst) {
        int compare = 0;
        if(src > dst) {
            compare = 1;
        } else if(src < dst) {
            compare = -1;
        }
        return compare;
    }

    /**
     * 两个时间比较
     *
     * @return 1：src 在 dst 后、 -1 dst 在 src 后 、0 相同
     */
    public static int compare(Date src , Date dst) {
        try {
            return compare(src.getTime() , dst.getTime());
        } catch (Exception e) {
            ALog.exception("DateUtil", "compare1", e);
            return -2;
        }
    }

    /**
     * 两个时间比较
     *
     * @return 1：src 在 dst 后、 -1 dst 在 src 后 、0 相同
     */
    public static int compare(String src , String dst) {
        Date srcDate = null;
        Date dstDate = null;
        try {
            SimpleDateFormat srcF = new SimpleDateFormat(formatYMDHMS, Locale.ENGLISH);
            SimpleDateFormat dstF = new SimpleDateFormat(formatYMDHMS, Locale.ENGLISH);
            srcDate = srcF.parse(src);
            dstDate = dstF.parse(dst);
        } catch (ParseException e) {
            ALog.exception("DateUtil", "compare2", e);
        }
        return compare(srcDate , dstDate);
    }

    /**
     * 两个时间比较（年月日）
     *
     * @return 1：src 在 dst 后、 -1 dst 在 src 后 、0 相同
     */
    public static int compareYMD(String src , String dst) {
        Date srcDate = null;
        Date dstDate = null;
        try {
            SimpleDateFormat srcF = new SimpleDateFormat(formatYMD, Locale.ENGLISH);
            SimpleDateFormat dstF = new SimpleDateFormat(formatYMD, Locale.ENGLISH);
            srcDate = srcF.parse(src);
            dstDate = dstF.parse(dst);
        } catch (ParseException e) {
            ALog.exception("DateUtil", "compareYMD", e);
        }
        return compare(srcDate , dstDate);
    }


    /**
     * 描述：获取偏移之后的Date.
     *
     * @param date 日期时间
     * @param offsetDay 偏移天(值大于0,表示+,值小于0,表示－)
     *
     * @return Date 偏移之后的日期时间
     */
    public static Date getDateOffsetDay(Date date, int offsetDay) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(date);
            calendar.add(Calendar.DATE, offsetDay);
        } catch (Exception e) {
            ALog.exception("DateUtil", "getDateOffsetDay", e);
        }
        return calendar.getTime();
    }

    /**
     * 描述：获取偏移之后的Date.
     *
     * @param date 日期时间
     * @param offsetHour 偏移小时(值大于0,表示+,值小于0,表示－)
     *
     * @return Date 偏移之后的日期时间
     */
    public static Date getDateOffsetHOUR(Date date, int offsetHour) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, offsetHour);
        } catch (Exception e) {
            ALog.exception("DateUtil", "getDateOffsetHOUR", e);
        }
        return calendar.getTime();
    }

    /**
     * 一个星期后
     * 描述：获取偏移之后的Date.
     *
     * @param date 日期时间
     *
     * @return Date 偏移之后的日期时间
     */
    public static Date getDateOffsetWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 7);
        } catch (Exception e) {
            ALog.exception("DateUtil", "getDateOffsetWeek", e);
        }
        return calendar.getTime();
    }

    /**
     * 描述：计算两个日期所差的天数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     *
     * @return int 所差的天数
     */
    public static int getOffsetDay(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        //先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }

    /**
     * 描述：计算两个日期所差的小时数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     *
     * @return int 所差的小时数
     */
    public static int getOffsetHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = getOffsetDay(date1, date2);
        h = h1-h2+day*24;
        return h;
    }

    /**
     * 描述：计算两个日期所差的分钟数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     *
     * @return int 所差的分钟数
     */
    public static int getOffsetMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = getOffsetHour(date1, date2);
        int m = 0;
        m = m1-m2+h*60;
        return m;
    }


    /**
     * @return 获取今日零时的时间戳
     */
    public static long getDayBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 1);
        return cal.getTimeInMillis();
    }

    /**
     * @return 获取某个时间戳当天零时的时间戳
     *
     * @param mills 时间戳
     */
    public static long getDayBegin(long mills) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mills);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 1);
        return cal.getTimeInMillis();
    }

    /**
     * @return 获取该时间戳，已过当前小时的时间
     */
    public static long getHourSpend(long mills){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mills);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 1);
        return mills - cal.getTimeInMillis();
    }


    /**
     * @return 获取1970以来的毫秒数
     *
     * @param gmtTime GMT时间字符串
     */
    public static long getMillsTime(String gmtTime) {
        Date date = null;
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.ENGLISH);
            date = sdf1.parse(gmtTime);
        }catch (Exception e){
            ALog.exception("DateUtil", "getMillsTime1", e);
        }
        try {
            if(date == null) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd-MMM-yy HH:mm:ss zzz", Locale.ENGLISH);
                date = sdf2.parse(gmtTime);
            }
        }catch (Exception e){
            ALog.exception("DateUtil", "getMillsTime2", e);
        }
        try {
            if(date == null) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
                date = sdf3.parse(gmtTime);
            }
        }catch (Exception e){
            ALog.exception("DateUtil", "getMillsTime3", e);
        }
        return date == null ? 0 : date.getTime();
    }

    /**
     * @return 获取格式化后的时间字符串
     */
    public static String getTimeFormatStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String getDeviceDateWithBeijingTime(String pattern){
        return getDateWithBeijingTime(pattern, System.currentTimeMillis());
    }

    public static String getDateWithBeijingTime(String pattern, long timeStamp){
        if(timeStamp <= 0){
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.CHINA);//设置日期格式
        df.setTimeZone(TimeZone.getTimeZone(TimeUtil.GMT_08));  // 设置时区，+08是北京时间
        return df.format(new Date(timeStamp));
    }

    public static String getFixedDateWithBeijingTime(){
        return getDateWithBeijingTime(formatYMD, getFixedTimeStamp());
    }

    public static String getFixedMdHmsWitBeijingTime(){
        return getDateWithBeijingTime(MMdd_HHmmss, getFixedTimeStamp());
    }


    public static String getTimeZoneGMT(){
        TimeZone timeZone = TimeZone.getDefault();
        int offsetInMillis = timeZone.getRawOffset();
        return  "GMT" + (offsetInMillis / MILLISECOND_PER_HOUR);
    }

    /**
     *
     * @param dateTime  2025-02-24 10:53:10
     * @param pattern   yyyy-MM-dd HH:mm:ss
     * @param timezone  Asia/Shanghai
     *
     * @return 根据日期、时区、格式，获取时间戳
     */
    public static long getMillsTimestamp(String dateTime, String pattern, String timezone){
        try {
            TimeZone timeZone = TimeZone.getTimeZone(timezone);
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
            Date date = sdf.parse(dateTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(timeZone);
            if(date != null){
                calendar.setTime(date);
            }
            return calendar.getTimeInMillis();
        } catch (Throwable e){
            LogUtil.e(e);
            return 0;
        }
    }

    /**
     * @param milliseconds 毫秒数
     *
     * @return 时长格式 **:**
     */
    @SuppressLint("DefaultLocale")
    public static String getDurationMs(long milliseconds){
        long totalSeconds = milliseconds / 1000;

        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String getFormatDate(long timeStamp, String formatString, Locale locale) {
        Date date = new Date(timeStamp);
        return getFormatDate(date, formatString, locale);
    }

    public static String getFormatDate(Date date, String formatString, Locale locale) {
        SimpleDateFormat format = new SimpleDateFormat(formatString, locale);
        return format.format(date);
    }

    public static String getFormatDate(Date date, String formatString, TimeZone timeZone, Locale locale) {
        SimpleDateFormat format = new SimpleDateFormat(formatString, locale);
        format.setTimeZone(timeZone);
        return format.format(date);
    }

    /**
     * @return 周开始的时间戳
     */
    public static Date getWeekStartDate(){
        Calendar calendar = Calendar.getInstance();

        // 将当前日期设置为星期一
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return calendar.getTime();
    }

    /**
     * @return 月开始的时间戳
     */
    public static Date getMonthStartDate(){
        Calendar calendar = Calendar.getInstance();

        // 将日期设置为当前月的第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    /**
     * @return 今天零点时刻的时间戳
     */
    public static long getTodayStartTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * @return 今天终点时刻的时间戳
     */
    public static long getTodayEndTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * @return 明天零点时刻的时间戳
     */
    public static long getTomorrowStartTimestamp(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * @param originalDateTime ISO8061格式的日期
     *
     * @return 日期时间
     */
    public static String parseFromISO8061(String originalDateTime, String inputFormat, String outputFormat){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(originalDateTime); // 解析原始日期时间字符串
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outputFormat); // 定义目标格式
            return zonedDateTime.format(formatter); // 格式化日期时间
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(inputFormat, Locale.ENGLISH); // 定义输入格式
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));   // 设置时区
            Date date = null;
            try {
                date = dateFormat.parse(originalDateTime);  // 解析原始日期时间字符串
            } catch (ParseException e) {
                ALog.exception("DateUtil", "parseFromISO8061", e);
            }
            SimpleDateFormat targetDateFormat = new SimpleDateFormat(outputFormat, Locale.ENGLISH);  // 定义目标格式
            targetDateFormat.setTimeZone(TimeZone.getTimeZone(TimeUtil.GMT_08)); // 设置时区为原始日期时间字符串中的时区
            return date == null ? "" : targetDateFormat.format(date);  // 格式化日期时间
        }
    }

    public static Date parseDateFromISO8061(String originalDateTime, String timeZone){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        // 设置时区，使其能正确解析带时区偏移量的时间字符串
        sdf.setTimeZone(TimeZone.getTimeZone(TextUtils.isEmpty(timeZone) ? TimeUtil.GMT_08 : timeZone));
        try {
            return sdf.parse(originalDateTime);
        } catch (ParseException e) {
            LogUtil.e(e);
            return new Date();
        }
    }

    @Nullable
    public static Date parse(String format, String dateString) {
        Date date = null;
        try {
            SimpleDateFormat srcF = new SimpleDateFormat(format, Locale.ENGLISH);
            srcF.setTimeZone(TimeZone.getTimeZone("UTC"));   // 设置时区
            date = srcF.parse(dateString);
        } catch (ParseException e) {
            ALog.exception("DateUtil", "parse", e);
        }
        return date;
    }

    @Nullable
    public static Date parseYmdDate(String dateString) {
        Date date = null;
        try {
            SimpleDateFormat srcF = new SimpleDateFormat(formatYMD, Locale.ENGLISH);
            date = srcF.parse(dateString);
        } catch (ParseException e) {
            ALog.exception("DateUtil", "parseYmdDate", e);
        }
        return date;
    }

    public static String dateConvert(long timeStamp, String pattern){
        Date date = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return format.format(date);
    }
}
