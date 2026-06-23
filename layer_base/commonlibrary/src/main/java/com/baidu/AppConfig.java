package com.baidu;


public class AppConfig {
//    public static boolean isDebug = true;
//    public static String endPoint = "author.inovelclub.com";
//    public static String endPointApi = "https://author.inovelclub.com/api/";
//    public static String currencyCode = "";
//    public static String[] moduleApps = {
//
//    };
//    public static String deeplinkOpenSource = "";
//    public static String newDeviceId = "";


    public static int appStatus = -1;

    public static boolean isUpgradeInstall = false;
    //是否需要截屏
    public static int screenShotFlag = 1;
    public static boolean versionUpdate = false;
    public static int cancelOrderProcessNum = 0;

    public static String lastOrderPayPlatform = "";

    public static int lastCreateOrderPageType = -1; // 0 MainActivity 1 PremiumCenterActivity 2 ReadPayView2 3 ReadPayView 4 PremiumPayDialog

    public static void setLastOrderPayPlatform(int payPlatform, String subCode) {
        lastOrderPayPlatform = payPlatform + "-" + subCode;
    }

}