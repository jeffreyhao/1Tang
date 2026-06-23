package com.tencent.common.util;

/**
* 空判断工具类
* @author adison
* @date 2017/7/9
* @time 下午4:26
*/

public final class NullUtils {
    private NullUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    /**
     * 所有对象都不为空
     * @param objects
     * @return <tt>true</tt> 如果所有对象都不为空 ,否则 <tt>false</tt>
     */
    public static boolean notNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) return false;
        }
        return true;
    }

    /**
     * 只要有一个对象为空则返回true，否则false
     * @param objects
     * @return
     */
    public static boolean hasNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) return true;
        }
        return false;
    }


    /**
     * 只要有一个对象不为空则返回true，否则false
     * @param objects
     * @return
     */
    public static boolean hasNotNull(Object... objects) {
        for (Object object : objects) {
            if (object != null) return true;
        }
        return false;
    }
}
