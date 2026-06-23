package com.tencent.common.util;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * 设备相关工具类
 */
public final class DeviceUtils {

    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String PATH_CPU = "/sys/devices/system/cpu/";
    private static final String CPU_FILTER = "cpu[0-9]+";
    private static int CPU_CORES = 0;

    /**
     * 获取有效核心数
     * @return cpu核心数或者可用的处理器个数(cpu核心数获取失败)
     */
    public static int getCoresNumbers() {
        if (CPU_CORES > 0) {
            return CPU_CORES;
        }
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //检查文件名是否为"cpu",后跟一个数字
                return Pattern.matches(CPU_FILTER, pathname.getName());
            }
        }
        try {
            //获取cpu信息文件目录
            File dir = new File(PATH_CPU);
            //过滤我们关心的文件
            File[] files = dir.listFiles(new CpuFilter());
            //返回核心数（虚拟CPU设备）
            if (null != files) {
                CPU_CORES = files.length;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CPU_CORES < 1) {
            CPU_CORES = Runtime.getRuntime().availableProcessors();
        }
        if (CPU_CORES < 1) {
            CPU_CORES = 1;
        }
        return CPU_CORES;
    }
}