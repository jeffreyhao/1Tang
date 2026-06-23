package com.base.global;

import android.text.TextUtils;
import android.util.Log;


/**
 * Created by haojiangfeng on 2023/9/5.
 *
 *  大写字段 来自 build.gradle配置
 *  小写字段 则为 本地字段
 */
public class GlobalBuildConfig {


    public static  String APPLICATION_ID = "";  // "com.wall.greatnovel"

    public static  String BUILD_TYPE = "";      // "debug"
    public static  String FLAVOR = "";          // "greatnovel"
    public static  String CHANNEL = "";         // "10103"
    public static  String PRODUCT_ID = "";      // "16"
    public static  String GROUP_ID = "";        //
    public static  String ROUTER_ID = "";       // "greatnovel"
    public static  String ROUTER_HOST = "";     // "app1.storynester.com"


    public static  String VERSION_NAME;         // "1.7.8"
    public static  int VERSION_CODE;            // 178
    public static int EXIT_LIMIT = 0;           // 缺省0。这是设定在主页点几次能退出app。



    public static  boolean DEBUG        = true;
    public static  boolean LOG_DEBUG    = true;
    public static  boolean SENSORS_LOG  = false;
    public static  boolean CRASH_DEFEND = false;



    // Fields from default config.
    public static  String BOOK_STORE_PAGE = "9";
    public static  String BASE_URL = "";
    public static  String SEARCH_URL = "";
    public static  String BASE_URL_DEEP_LINK = "";       // 深链域名。另外有备用域名，见 DeeplinkHost
    public static  String UPLOAD_TIME_URL = "";
    public static  String BASE_HOST = "";
    public static  String SEARCH_HOST = "";
    public static  String UPLOAD_TIME_HOST = "";

    public static  String BASE_LANDING = "";

    public static  String WEB_ENDPOINT = "";
    public static  String WEB_ENDPOINT_PREFIX = "";






    /**
     * @return  「buildType」 为 debug包（设备debug、且未混淆的包）
     */
    public static boolean isFlagSecure(){
        if(TextUtils.isEmpty(BUILD_TYPE)){
           return true;
        }
        if((BUILD_TYPE.contains("debug") || BUILD_TYPE.contains("develop") || BUILD_TYPE.contains("publish"))
            && PreferencesUtil.get("FLAG_SECURE", true)){
            return false;
        }
        return true;
    }


    public static void showDebugLog() {
        if(LOG_DEBUG){
            StringBuilder sb = new StringBuilder("GlobalBuildConfig");
            sb.append(", BUILD_TYPE=").append(BUILD_TYPE);
            sb.append(", FLAVOR=").append(FLAVOR);
            sb.append(", CHANNEL=").append(CHANNEL);
            sb.append(", PRODUCT_ID=").append(PRODUCT_ID);
            sb.append(", ROUTER_ID=").append(ROUTER_ID);
            sb.append(", VERSION_NAME=").append(VERSION_NAME);
            sb.append(", VERSION_CODE=").append(VERSION_CODE);
            sb.append(", DEBUG=").append(DEBUG);
            sb.append(", LOG_DEBUG=").append(LOG_DEBUG);
            sb.append(", SENSORS_LOG=").append(SENSORS_LOG);
            sb.append(", BOOK_STORE_PAGE=").append(BOOK_STORE_PAGE);
            sb.append("\nBASE_URL=").append(BASE_URL);
            sb.append("\nBASE_LANDING=").append(BASE_LANDING);
            Log.d("GlobalBuildConfig", sb.toString());
        }
    }


}
