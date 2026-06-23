package com.base.net.state.type;

import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Created by haojiangfeng on 2024/11/26.
 */
class NetworkCapability {

    /*
     *      int[] getCapabilities()
     *      网络功能能力
     */

    /**
     *  表示在系统探测网络时网络具有强制门户
     */
    public static final int NET_CAPABILITY_CAPTIVE_PORTAL                   = NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL;
    /**
     *  表示该网络能够访问运营商的 CBS 服务器，用于运营商特定的服务
     */
    public static final int NET_CAPABILITY_CBS                              = NetworkCapabilities.NET_CAPABILITY_CBS;
    /**
     *  表示该网络能够到达运营商的 DUN 或网络共享网关。
     */
    public static final int NET_CAPABILITY_DUN                              = NetworkCapabilities.NET_CAPABILITY_DUN;
    /**
     *  表示该网络能够访问运营商的紧急 IMS 服务器或其他服务，用于紧急呼叫期间的网络信令。
     */
    public static final int NET_CAPABILITY_EIMS                             = NetworkCapabilities.NET_CAPABILITY_EIMS;
    /**
     *  表示该网络适合企业使用。
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public static final int NET_CAPABILITY_ENTERPRISE                       = NetworkCapabilities.NET_CAPABILITY_ENTERPRISE;
    /**
     *  表示该网络可供应用程序使用，而不是在后台维持的网络，以便于快速进行网络切换。
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static final int NET_CAPABILITY_FOREGROUND                       = NetworkCapabilities.NET_CAPABILITY_FOREGROUND;
    /**
     *  表示该网络能够访问运营商的 FOTA 门户，用于无线更新。
     */
    public static final int NET_CAPABILITY_FOTA                             = NetworkCapabilities.NET_CAPABILITY_FOTA;
    /**
     *  表示该网络已连接至汽车主机。
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public static final int NET_CAPABILITY_HEAD_UNIT                        = NetworkCapabilities.NET_CAPABILITY_HEAD_UNIT;
    /**
     *  表示该网络能够连接运营商的初始连接服务器。
     */
    public static final int NET_CAPABILITY_IA                               = NetworkCapabilities.NET_CAPABILITY_IA;
    /**
     *  表示这是一个能够到达运营商的IMS服务器的网络，用于网络注册和信令。
     */
    public static final int NET_CAPABILITY_IMS                              = NetworkCapabilities.NET_CAPABILITY_IMS;
    /**
     *  表示网络设置为访问互联网。这只是设置，而不是实际能够到达公共服务器。
     */
    public static final int NET_CAPABILITY_INTERNET                         = NetworkCapabilities.NET_CAPABILITY_INTERNET;
    /**
     *  表示该网络为本地网络。
     */
    public static final int NET_CAPABILITY_LOCAL_NETWORK                    = 36; // NetworkCapabilities.NET_CAPABILITY_LOCAL_NETWORK;
    /**
     *  表示该网络能够访问运营商的关键任务服务器。
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static final int NET_CAPABILITY_MCX                              = NetworkCapabilities.NET_CAPABILITY_MCX;
    /**
     *  表示该网络能够连接运营商的 MMSC 以发送和接收 MMS 消息。
     */
    public static final int NET_CAPABILITY_MMS                              = NetworkCapabilities.NET_CAPABILITY_MMS;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static final int NET_CAPABILITY_MMTEL                            = NetworkCapabilities.NET_CAPABILITY_MMTEL;
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static final int NET_CAPABILITY_NOT_CONGESTED                    = NetworkCapabilities.NET_CAPABILITY_NOT_CONGESTED;
    /**
     *  表示网络不按流量计费
     */
    public static final int NET_CAPABILITY_NOT_METERED                      = NetworkCapabilities.NET_CAPABILITY_NOT_METERED;
    public static final int NET_CAPABILITY_NOT_RESTRICTED                   = NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED;
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static final int NET_CAPABILITY_NOT_ROAMING                      = NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING;
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static final int NET_CAPABILITY_NOT_SUSPENDED                    = NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED;
    /**
     *  表示网络不是虚拟专用网
     */
    public static final int NET_CAPABILITY_NOT_VPN                          = NetworkCapabilities.NET_CAPABILITY_NOT_VPN;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static final int NET_CAPABILITY_PRIORITIZE_BANDWIDTH             = NetworkCapabilities.NET_CAPABILITY_PRIORITIZE_BANDWIDTH;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static final int NET_CAPABILITY_PRIORITIZE_LATENCY               = NetworkCapabilities.NET_CAPABILITY_PRIORITIZE_LATENCY;
    public static final int NET_CAPABILITY_RCS                              = NetworkCapabilities.NET_CAPABILITY_RCS;
    public static final int NET_CAPABILITY_SUPL                             = NetworkCapabilities.NET_CAPABILITY_SUPL;
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static final int NET_CAPABILITY_TEMPORARILY_NOT_METERED          = NetworkCapabilities.NET_CAPABILITY_TEMPORARILY_NOT_METERED;
    public static final int NET_CAPABILITY_TRUSTED                          = NetworkCapabilities.NET_CAPABILITY_TRUSTED;
    /**
     *  表示在系统探测网络时网络提供对公共互联网的实际访问
     */
    public static final int NET_CAPABILITY_VALIDATED                        = NetworkCapabilities.NET_CAPABILITY_VALIDATED;
    public static final int NET_CAPABILITY_WIFI_P2P                         = NetworkCapabilities.NET_CAPABILITY_WIFI_P2P;
    public static final int NET_CAPABILITY_XCAP                             = NetworkCapabilities.NET_CAPABILITY_XCAP;






    /*
     *      int[] getEnterpriseIds()
     *      企业网络标识，用于区分不同的企业网络环境或者企业网络中的不同区域。
     */
    public static final int NET_ENTERPRISE_ID_1 = 1;
    public static final int NET_ENTERPRISE_ID_2 = 2;
    public static final int NET_ENTERPRISE_ID_3 = 3;
    public static final int NET_ENTERPRISE_ID_4 = 4;
    public static final int NET_ENTERPRISE_ID_5 = 5;


    /**
     * 信号强度
     */
    public static final int SIGNAL_STRENGTH_UNSPECIFIED = Integer.MIN_VALUE;



    /**
     *  2-蓝牙传输
     */
    public static final int TRANSPORT_BLUETOOTH     = NetworkCapabilities.TRANSPORT_BLUETOOTH;
    /**
     *  0-蜂窝网络传输
     */
    public static final int TRANSPORT_CELLULAR      = NetworkCapabilities.TRANSPORT_CELLULAR;
    /**
     *  3-以太网传输
     */
    public static final int TRANSPORT_ETHERNET      = NetworkCapabilities.TRANSPORT_ETHERNET;
    /**
     *  6-低功耗无线个人区域网络
     */
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public static final int TRANSPORT_LOWPAN        = NetworkCapabilities.TRANSPORT_LOWPAN;
    /**
     *  9-基于 IPv6 的低功耗网状网络协议，多用于智能家居能物联网应用场景
     */
    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public static final int TRANSPORT_THREAD        = NetworkCapabilities.TRANSPORT_THREAD;
    /**
     *  8-USB网络共享传输
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public static final int TRANSPORT_USB           = NetworkCapabilities.TRANSPORT_USB;
    /**
     *  4-VPN网络
     */
    public static final int TRANSPORT_VPN           = NetworkCapabilities.TRANSPORT_VPN;
    /**
     *  1-Wifi网络
     */
    public static final int TRANSPORT_WIFI          = NetworkCapabilities.TRANSPORT_WIFI;
    /**
     *  5-wifi相邻设备发现协议网络
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static final int TRANSPORT_WIFI_AWARE    = NetworkCapabilities.TRANSPORT_WIFI_AWARE;

}
