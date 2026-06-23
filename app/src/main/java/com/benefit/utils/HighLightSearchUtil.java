package com.benefit.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighLightSearchUtil {
    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        keyword=escapeExprSpecialWord(keyword);
        text=escapeExprSpecialWord(text);
        if (!TextUtils.isEmpty(text)&&!TextUtils.isEmpty(keyword)&&text.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT))){
            try {
                Pattern p = Pattern.compile(keyword.toLowerCase(Locale.ROOT));
                Matcher m = p.matcher(text.toLowerCase(Locale.ROOT));
                while (m.find()) {
                    int start = m.start();
                    int end = m.end();
                    s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return s;
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return keyword
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

}
