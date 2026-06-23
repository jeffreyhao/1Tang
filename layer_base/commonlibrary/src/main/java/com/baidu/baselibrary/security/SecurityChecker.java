package com.baidu.baselibrary.security;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Base64;

import java.security.KeyStore;
import java.security.MessageDigest;

import javax.net.ssl.TrustManagerFactory;

public class SecurityChecker {
    private static final String EXPECTED_SIGNATURE = "";

    // 签名校验（防止被重新打包）
    private static boolean isSignatureValid(Context context) {
        try {
            byte[] signatureBytes;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(
                        context.getPackageName(),
                        PackageManager.GET_SIGNING_CERTIFICATES
                );
                signatureBytes = pkgInfo.signingInfo.getApkContentsSigners()[0].toByteArray();
            } else {
                PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(
                        context.getPackageName(),
                        PackageManager.GET_SIGNATURES
                );
                signatureBytes = pkgInfo.signatures[0].toByteArray();
            }

            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(signatureBytes);
            String encoded = Base64.encodeToString(digest, Base64.NO_WRAP);

            return encoded.equals(EXPECTED_SIGNATURE);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 检查是否有代理（如使用了 Fiddler、Charles）
    private static boolean isUsingProxy() {
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        return proxyHost != null && !proxyHost.isEmpty() && !"0".equals(proxyPort);
    }

    // 检查是否安装了抓包类证书（用户 CA 证书）
    private static boolean isUserDebugCAInstalled(Context context) {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm()
            );
            trustManagerFactory.init((KeyStore) null);

            return java.util.Arrays.stream(trustManagerFactory.getTrustManagers())
                    .anyMatch(tm ->
                            tm.getClass().getName().contains("TrustManagerImpl") ||
                                    tm.getClass().getName().contains("CustomTrustManager")
                    );
        } catch (Exception e) {
            return false;
        }
    }

    // 检测 VPN（部分抓包工具是 VPN 模式）
    private static boolean isVpnActive(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
        } else {
            android.net.Network[] networks = connectivityManager.getAllNetworks();
            for (android.net.Network network : networks) {
                if (connectivityManager.getNetworkInfo(network) != null &&
                        connectivityManager.getNetworkInfo(network).getType() == ConnectivityManager.TYPE_VPN) {
                    return true;
                }
            }
            return false;
        }
    }

    // 综合检测
    public static boolean isEnvironmentSafe(Context context) {
        return isSignatureValid(context) &&
                !isUsingProxy() &&
                !isVpnActive(context);
    }
}
