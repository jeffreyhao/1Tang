//package com.benefit.net.util;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.Network;
//import android.net.NetworkCapabilities;
//import android.net.NetworkInfo;
//import android.os.Build;
//
//import androidx.annotation.RequiresApi;
//
///**
// * Created by haojiangfeng on 2023/11/13.
// */
//class NetUtil {
//
//    public static final int TYPE_DATA           = 0;
//    public static final int TYPE_WIFI           = 1;
//    public static final int TYPE_DATA_WIFI      = 2;
//
//
//    public static final int Kbps_Downstream     = 0;
//    public static final int Kbps_Upstream       = 1;
//
//
//
//    /**
//     * 所有网络开关状态
//     *
//     * @param context
//     * @return
//     */
//    public static boolean isNetworkConnected(Context context) {
//        return isConnected(context, TYPE_DATA_WIFI);
//    }
//
//    /**
//     * WiFi网络开关状态
//     *
//     * @param context
//     * @return
//     */
//    public static boolean isWiFiConnected(Context context) {
//        return isConnected(context, TYPE_WIFI);
//    }
//
//    /**
//     * 移动数据流量开关状态
//     *
//     * @param context
//     * @return
//     */
//    public static boolean isMobileConnected(Context context) {
//        return isConnected(context, TYPE_DATA);
//    }
//
//    /**
//     * 获取各种类型网络状态
//     *
//     * @param context
//     * @param type    0 获取移动数据(蜂窝)网络开关状态， 1 获取Wifi网络开关状态， 2 获取所有网络开关状态
//     * @return
//     */
//    public static boolean isConnected(Context context, int type) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (type == TYPE_DATA_WIFI) {
//            if (networkInfo != null)
//                return networkInfo.isAvailable();
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            Network network = connectivityManager.getActiveNetwork();
//            if (null != network) {
//                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
//                if (null != networkCapabilities) {
//                    int TRANSPORT = NetworkCapabilities.TRANSPORT_CELLULAR;
//                    if (type == 1) TRANSPORT = NetworkCapabilities.TRANSPORT_WIFI;
//                    return networkCapabilities.hasTransport(TRANSPORT);
//                }
//            }
//        } else {
//            if (networkInfo != null && type == TYPE_DATA && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                return networkInfo.isAvailable();
//            } else if (networkInfo != null && type == TYPE_WIFI && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                return networkInfo.isAvailable();
//            }
//        }
//        return false;
//    }
//
//
//    /**
//     * 获取当前网络下载速率 单位：kbps
//     *
//     * @param context
//     * @return
//     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static int getLinkDownstreamBandwidthKbps(Context context) {
//        return getBandwidthKbps(context, Kbps_Downstream);
//    }
//
//    /**
//     * 获取当前网络上传速率 单位：kbps  宽带kbps与kB/s换算1KByte/s=8Kbps
//     *
//     * @param context
//     * @return
//     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static int getLinkUpstreamBandwidthKbps(Context context) {
//        return getBandwidthKbps(context, Kbps_Upstream);
//    }
//
//    /**
//     * @param context
//     * @param type    0 下载速率 {@link #Kbps_Downstream}
//     * @return 例：200 kbps
//     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static int getBandwidthKbps(Context context, int type) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        Network network = connectivityManager.getActiveNetwork();
//        if (null != network) {
//            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
//            if (null != networkCapabilities) {
//                if (type == Kbps_Downstream) {
//                    return networkCapabilities.getLinkDownstreamBandwidthKbps();
//                } else {
//                    return networkCapabilities.getLinkUpstreamBandwidthKbps();
//                }
//            }
//        }
//        return 0;
//    }
//
//}
