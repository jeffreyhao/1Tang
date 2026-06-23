package com.baidu.baselibrary.timer;

/**
 * 定时任务的操作
 * Created by haojiangfeng on 2024/7/26.
 */
public interface ITimerTaskAction {

    /**
     * 继续开始计时
     */
    void continueTiming();

    /**
     * 重新计时，并开启计时任务
     */
    void restartTiming();

    /**
     * 暂停
     */
    void pauseTiming();

    /**
     * 仅仅清除当次计时的间隔时间
     */
    void clearTiming();

    /**
     * 取消计时，并注销掉task
     */
    void cancelTimingAndUnregister();

    /**
     * 执行 run()
     */
    void execRunnable();

    /**
     * @return 优先级
     */
    @ObserverTimerTask.TaskPriority
    int getPriority();

}
