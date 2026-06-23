package com.xcyh.cheatsheet.model.constant;


/**
 * Created by haojiangfeng on 2024/12/12.
 */
public class Url {



    public static String BASE_URL                  = "https://launch.novelsbd.com/";


    /** 登录接口 **/
    public static String API_LOGIN;

    /** 报表 **/
    public static String API_ADV_REPORT;

    /** 负责人 **/
    public static String API_LAUNCH_LIST;



    static {
        reset();
    }


    public static void reset(){

        /** 登录接口 **/
        API_LOGIN            = BASE_URL + "launch/login";

        /** 报表 **/
        API_ADV_REPORT       = BASE_URL + "mobile/analysis/launch";

        /** 负责人 **/
        API_LAUNCH_LIST      = BASE_URL + "launch/list";

    }






}
