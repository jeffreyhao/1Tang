package com.tencent.common.util;

import android.net.Uri;
/**
 * 资源工具类
 * @author adison
 * @date 2017/5/29
 * @time 上午3:24
 */
public class ResourceUtils {

    private ResourceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    public static Uri resourceIdToUri(int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + AppUtils.getAppPackageName() + FOREWARD_SLASH + resourceId);
    }
}
