package com.base.util.thread;

/**
 * Created by haojiangfeng on 2024/10/29.
 */
public class ExecutorsUtils {

    private static ExecutorsUtils mInstance = new ExecutorsUtils();

    private AppExecutors mAppExecutors = null;

    private ExecutorsUtils(){
        mAppExecutors = new AppExecutors();
    }

    public static ExecutorsUtils getInstance(){
        return mInstance;
    }

    public AppExecutors getAppExecutors() {
        return mInstance.mAppExecutors;
    }
}
