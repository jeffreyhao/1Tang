package com.baidu.baselibrary.security;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Debug;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.net.Socket;

public class AntiHookUtils {

    // 检查是否处于调试状态
    public static boolean isDebugging() {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger();
    }

    public static boolean isDebuggable(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    // 通过堆栈信息检测Xposed或Frida
    public static boolean detectHookByStackTrace() {
        try {
            throw new Exception("detect hook");
        } catch (Exception e) {
            for (StackTraceElement element : e.getStackTrace()) {
                String clazzName = element.getClassName();
                if (clazzName.contains("de.robv.android.xposed") || clazzName.contains("frida")) {
                    return true;
                }
            }
        }
        return false;
    }

    // 检查Xposed桥接类
    public static boolean detectXposedByClass() {
        try {
            Class.forName("de.robv.android.xposed.XposedBridge");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    // 检查Frida默认端口
    public static boolean detectFridaPort() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 27042), 200);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // 检查关键方法是否被替换为native
    public static boolean checkMethodIntegrity(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            if ((method.getModifiers() & Modifier.NATIVE) != 0) {
                return false; // 方法被替换为native
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 综合检测是否Hook
    public static boolean isHookDetected() {
        return isDebugging() ||
               detectHookByStackTrace() ||
               detectXposedByClass() ||
               detectFridaPort();
    }
}
