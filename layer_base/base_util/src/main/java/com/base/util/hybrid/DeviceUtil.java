package com.base.util.hybrid;

import android.content.Context;
import android.content.pm.PackageManager;

import com.base.api.GlobalContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class DeviceUtil {
    public static boolean isRT() {
        return isRT1() || isRT2() || isRT3()
                || isInstalled("com.topjohnwu.magisk", GlobalContext.getContext())
                || isInstalled("eu.chainfire.supersu", GlobalContext.getContext());
    }

    private static boolean isRT1() {
        String[] paths = {
                "/system/bin/su",
                "/system/xbin/su",
                "/sbin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/app/Superuser.apk",
                "/system/app/Magisk.apk",
                "/system/bin/.ext/.su"
        };
        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRT2() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
        return false;
    }

    private static boolean isRT3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
        return false;
    }

    private static boolean isInstalled(String packageName, Context context) {
        try {
            if(context==null) {
                return false;
            }
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
