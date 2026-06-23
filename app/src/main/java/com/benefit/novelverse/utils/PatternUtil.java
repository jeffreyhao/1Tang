package com.benefit.novelverse.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Time: 2023/11/16
 * Author: lhc
 * Desc:
 */
public class PatternUtil {



    /**
     {
     "ad_id":23855291147800594,
     "ad_objective_name":"APP_INSTALLS",
     "adgroup_id":23855289744500594,
     "adgroup_name":"测试andr_aeo_1U5081_The Appearance of Five Alphasr第二版_0523-1",
     "campaign_id":23855289744490594,
     "campaign_name":"新应用推广广告组",
     "campaign_group_id":23855289744510594,
     "campaign_group_name":"测试andr_aeo_1U5081_The Appearance of Five Alphasr第二版_0523-1",
     "account_id":762415928910791,
     "is_instagram":false,
     "publisher_platform":"facebook",
     "platform_position":null
     }
     */
    public static String findBookIdFromContent(String content){
        if(content == null){
            return "";
        }
        Pattern pattern = Pattern.compile("(1U[0-9]{3,})");
        Matcher matcher = pattern.matcher(content);
        String bookId1 = null, bookId2 = null;
        if(matcher.find()){
            bookId1 =  matcher.group(1);
        }
        if(matcher.find()){
            bookId2 =  matcher.group(1);
        }
//        System.out.println(bookId1);
//        System.out.println(bookId2);
        return bookId2 == null ? bookId1 : bookId2;
    }


    public static String findBookId(String content){
        if(content == null){
            return "";
        }
        Pattern pattern = Pattern.compile("(1U[0-9]{3,})");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
           return matcher.group(1);
        }
        return "";
    }

    public static boolean isValidBookId(String content){
        if(TextUtils.isEmpty(content)){
            return false;
        }
        Pattern pattern = Pattern.compile("(1U[0-9]{3,})|(1u[0-9]{3,})|([0-9]{4,})");
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }

    public static boolean isValidAdId(String adId) {
        if(TextUtils.isEmpty(adId)) {
            return false;
        }
        return !adId.contains("{") && !adId.contains("}");
    }
}
