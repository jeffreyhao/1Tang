package com.base.net.state.tracker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import com.base.api.GlobalContext;
import com.base.net.state.type.NetworkState;
import com.base.util.net.NetworkType;
import com.base.util.net.NetworkUtils;

import androidx.annotation.Nullable;
import androidx.core.net.ConnectivityManagerCompat;

/**
 * Created by haojiangfeng on 2024/11/4.
 */
public class NetworkStateUtil {


    public static boolean showNetStateLog = true;



    public static NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) GlobalContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }


    public static NetworkState getActiveNetworkState(ConnectivityManager connectivityManager) {
        // Use getActiveNetworkInfo() instead of getNetworkInfo(network) because it can detect VPNs.
        Network network = connectivityManager.getActiveNetwork();
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
        NetworkType netWorkType = NetworkUtils.getNetworkType(networkInfo);
        boolean isConnected = networkInfo != null && networkInfo.isConnected();
        boolean isValidated = isActiveNetworkValidated(connectivityManager);
        boolean isMetered = ConnectivityManagerCompat.isActiveNetworkMetered(connectivityManager);
        boolean isNotRoaming = networkInfo != null && !networkInfo.isRoaming();
        return new NetworkState(netWorkType, isConnected, isValidated, isMetered, isNotRoaming);
    }



    public static boolean isActiveNetworkValidated(ConnectivityManager connectivityManager) {
        try {
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } catch (SecurityException exception) {
            // b/163342798
//            Logger.get().error(TAG, "Unable to validate active network", exception);
            return false;
        }
    }


    public static NetworkType getActiveNetworkType(ConnectivityManager connectivityManager) {
        try {
            Network network = connectivityManager.getActiveNetwork();
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            NetworkType netWorkType = NetworkUtils.getNetworkType(networkInfo);
            return netWorkType;
        } catch (Throwable e) {
            e.printStackTrace();
            // b/163342798
//            Logger.get().error(TAG, "Unable to validate active network", exception);
            return NetworkType.NETWORK_UNKNOWN;
        }
    }



}
