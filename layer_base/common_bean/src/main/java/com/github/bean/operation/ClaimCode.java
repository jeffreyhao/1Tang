package com.github.bean.operation;

/**
 * 任务领取回调的返回code
 *
 * Created by haojiangfeng on 2025/3/19.
 */
public class ClaimCode {


    public static final int SUCCESS                 = 0; // 成功

    public static final int ERROR_UNKNOWN           = 1;  // 未知错误

    public static final int ERROR_REPETITION        = 20500;  // 广告奖励重复领取错误

    public static final int ERROR_CONFIG            = 20501;  // 任务表未存在对应配置错误

    public static final int ERROR_NO_ADMOB_CALL     = 20502;  // 没有admob平台回调记录错误

    public static final int ERROR_REQUEST_PARAMS    = 20503;  // 请求参数错误

    public static final int ERROR_DATABASE          = 20504;  // 数据表更新记录失败
    public static final int ERROR_BEYOND_COUNT      = 20505;  // 领取记录超过配置次数错误


}

