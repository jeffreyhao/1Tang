//package com.benefit.net;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.Network;
//import android.net.NetworkCapabilities;
//import android.net.NetworkRequest;
//import android.os.Build;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//
//import com.bytedance.baselibrary.ALog;
//
///**
// * 网络状态监听类
// */
//public class NetStateMonitor {
//    private static NetStateMonitor mInstance;
//    private static int mCurNetState = 1;  // 0: 不可用  1: 可用
//    private NetStateMonitor(){
//
//    }
//    public static NetStateMonitor getInstance(){
//        if(mInstance==null){
//            synchronized (NetStateMonitor.class){
//                if(mInstance==null){
//                    mInstance = new NetStateMonitor();
//                }
//            }
//        }
//        return mInstance;
//    }
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void registerCallback(Context context, NetStateChangeCallback netStateChangeCallback){
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        Network activeNetwork = cm.getActiveNetwork();
//        if (activeNetwork == null) {
//            mCurNetState = 0;
//        }else{
//            NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(activeNetwork);
//            if (networkCapabilities != null && !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
//                mCurNetState = 0;
//            }
//        }
//
//        //定义ConnectivityManager.NetworkCallback回调方法
//        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
//            // 可用网络接入
//            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
//                super.onCapabilitiesChanged(network, networkCapabilities);
//            }
//
//            @Override
//            public void onAvailable(@NonNull Network network) {
//                super.onAvailable(network);
//                if(mCurNetState ==0){
//                    ALog.d("onAvailable-------------");
//                    NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(network);
//                    checkNetworkCapabilities(networkCapabilities,netStateChangeCallback);
//                }
//            }
//
//            @Override
//            public void onLost(@NonNull Network network) {
//                super.onLost(network);
//                ALog.d("onLost-------------");
//                Network activeNetwork = cm.getActiveNetwork();
//                if (activeNetwork == null) {
//                    //连接不到可用网络
//                    if(netStateChangeCallback!=null){
//                        netStateChangeCallback.onNetStateChange(false);
//                        mCurNetState = 0;
//                    }
//                    return;
//                }
//                NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(activeNetwork);
//                checkNetworkCapabilities(networkCapabilities,netStateChangeCallback);
//            }
//        };
//        NetworkRequest.Builder builder = new NetworkRequest.Builder();
//        cm.registerNetworkCallback(builder.build(), callback);
//    }
//
//    //判断当前网络连接情况
//    private void checkNetworkCapabilities(NetworkCapabilities networkCapabilities,NetStateChangeCallback netStateChangeCallback) {
//        if (networkCapabilities == null) {
//            mCurNetState = 0;
//            return;
//        }
//        // 表明网络连接成功
//        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
//            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
//                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
//                // 使用WI-FI
//                if(netStateChangeCallback!=null){
//                    netStateChangeCallback.onNetStateChange(true);
//                }
//                mCurNetState = 1;
//            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
//                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
//                // 使用蜂窝网络
//                if(netStateChangeCallback!=null){
//                    netStateChangeCallback.onNetStateChange(true);
//                }
//                mCurNetState = 1;
//            } else {
//                // 未知网络
//                if(netStateChangeCallback!=null){
//                    netStateChangeCallback.onNetStateChange(true);
//                }
//                mCurNetState = 1;
//            }
//        } else {
//            //网络连接失败
//            if(netStateChangeCallback!=null){
//                netStateChangeCallback.onNetStateChange(false);
//            }
//            mCurNetState = 0;
//        }
//    }
//}
