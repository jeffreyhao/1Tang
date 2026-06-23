package com.baidu.baselibrary.timer;


/**
 * 时间间隔策略。
 *      1. 这里目前提供线性、指数型、乘数型以及斐波那契数列的间隔算法 {@link TimerStrategyFactory}
 *      2. 有其他需求的算法可以继承 {@link BaseStrategy} 实现
 * Created by haojiangfeng on 2024/7/12.
 */
public interface ObserverTimerStrategy {

    /**
     * 重置计时
     */
    void resetTiming();

    /**
     * @return 当前这轮计时的秒数
     */
    long getLatestStartSeconds();

    /**
     * @return 命中的次数
     */
    int getCount();

    /**
     * 重置count为本count
     */
    void resetCount(int count);

    /**
     * @param currentTimerSecond 秒数
     *
     * @return 是否匹配下一次命中的条件
     */
    boolean matchOnTick(long currentTimerSecond);

    /**
     * 匹配成功时的回调
     */
    void onMatched();
}
