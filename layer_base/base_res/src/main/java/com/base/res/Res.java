package com.base.res;

import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorRes;
import androidx.core.content.res.ResourcesCompat;

/**
 * Created by haojiangfeng on 2023/9/7.
 */
public class Res {

    static Context sContext;

    public static String sAppName;
    public static String AppSimpleName;
    public static String appSimpleName;


    public static void setAppName(String appName, String simpleName){
        sAppName = appName;
        appSimpleName = simpleName;
        AppSimpleName = getBuildType(simpleName);
    }


    public static void setContext(Context context){
        if(context instanceof Application){
            sContext = context;
        } else {
            sContext = context.getApplicationContext();
        }
    }

    public static Context getContext(){
        return sContext;
    }


    /**
     * 首字母大写
     */
    private static String getBuildType(String str) {
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }


    public static Resources getResources(){
        return sContext.getResources();
    }

    public static int getColor(int resId){
        return ResourcesCompat.getColor(sContext.getResources(), resId, sContext.getTheme());
    }

    public static float getDimension(int resId){
        return sContext.getResources().getDimension(resId);
    }

    public static boolean getBoolean(int resId){
        return sContext.getResources().getBoolean(resId);
    }

    public static ColorStateList getColorStateList(int resId){
        return ResourcesCompat.getColorStateList(sContext.getResources(), resId, sContext.getTheme());
    }

    public static int getDimensionPixelOffset(int resId){
        return sContext.getResources().getDimensionPixelOffset(resId);
    }

    public static int getInteger(int resId){
        return sContext.getResources().getInteger(resId);
    }

    public static Drawable getDrawable(int resId){
        return ResourcesCompat.getDrawable(sContext.getResources(), resId, sContext.getTheme());
    }

    public static CharSequence getAccentText(String resText, ClickableSpan... clickableSpans){
        return getSpanText(resText, R.color.colorUnderline, clickableSpans);
    }

    public static CharSequence getSpanText(String resText, @ColorRes int colorRes, ClickableSpan... clickableSpans){
        return getSpanText(resText, colorRes, false, clickableSpans);
    }

    public static CharSequence getSpanText(String resText, @ColorRes int colorRes, boolean isBold, ClickableSpan... clickableSpans) {
        // 1. 提取被 # 包裹的文字
        String[] strings = resText.split("#");
        List<String> accentList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            if (i % 2 == 1) {
                accentList.add(strings[i]);
            }
        }

        // 2. 移除占位符
        String finalText = resText.replace("#", "");
        SpannableString spannableString = new SpannableString(finalText);
        int color = Res.getColor(colorRes);
        int clickSpanCount = clickableSpans == null ? 0 : clickableSpans.length;

        // 记录上一次搜索的位置，防止多个相同高亮词导致索引覆盖
        int lastIndex = 0;

        for (int i = 0; i < accentList.size(); i++) {
            String accentText = accentList.get(i);
            int accentStart = finalText.indexOf(accentText, lastIndex);
            if (accentStart == -1) continue; // 安全检查

            int accentEnd = accentStart + accentText.length();
            lastIndex = accentEnd; // 更新起始搜索点

            // A. 设置点击事件
            if (i < clickSpanCount) {
                ClickableSpan clickableSpan = clickableSpans[i];
                spannableString.setSpan(clickableSpan, accentStart, accentEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            // B. 设置颜色
            spannableString.setSpan(new ForegroundColorSpan(color), accentStart, accentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // C. 设置加粗
            if (isBold) {
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), accentStart, accentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }


    /**
     * @return  首字母小写的format字符串。 ex. novelverse
     */
    public static String formatString(Context context, int resId){
        String text = context.getString(resId);
        return String.format(text, Res.appSimpleName);
    }

    /**
     * @return  首字母大写的format字符串。 ex. Novelverse
     */
    public static String FormatString(Context context, int resId){
        String text = context.getString(resId);
        return String.format(text, Res.AppSimpleName);
    }


    public static String formatString(Context context, int resId, String replaceText){
        String text = context.getString(resId);
        return String.format(text, replaceText);
    }

    public static String formatString(Context context, int resId, Object... replaceText){
        String text = context.getString(resId);
        return String.format(text,  replaceText);
    }

    public static String formatArrayString(Context context, int resId, String replaceText){
        String text = context.getString(resId);
        String[] splitArray = text.split("%s");
        int count = splitArray.length - 1;

        Object[] replaceArray = new Object[count];
        for(int i = 0; i < count; i++){
            replaceArray[i] = replaceText;
        }

        return String.format(text, replaceArray);
    }
}
