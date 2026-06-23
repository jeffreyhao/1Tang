package com.base.util.safe;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;

import com.base.api.Logger;
import com.base.util.AppUtil;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;

/**
 * Created by haojiangfeng on 2025/8/17.
 */
public class ShaUtil {



    public static String getAppSignaturesHash() {
        String packageName = AppUtil.getPackageName();
        if(StringUtils.isSpace(packageName)){
            return "";
        }
        Signature[] signatures = getAppSignatures(packageName);
        if (signatures == null || signatures.length == 0) {
            return "";
        }
        ArrayList<String> result = new ArrayList<>();
        for (Signature signature : signatures) {
            String hash = DigestUtil.bytes2HexString(DigestUtil.hashTemplate(signature.toByteArray(), "SHA256"))
                    .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
            result.add(hash);
        }
        return result.get(0);
    }


    public static Signature[] getAppSignatures(final String packageName) {
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES);
                if (pi == null) return null;
                SigningInfo signingInfo = pi.signingInfo;
                return signingInfo.getApkContentsSigners();
            } else {
                PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                if (pi == null) return null;
                return pi.signatures;
            }
        } catch (Exception e) {
            Logger.exception(e);
            return null;
        }
    }


}
