package com.baidu.baselibrary.timer;


import com.baidu.baselibrary.virtual.UniqueKey;

/**
 * 定时器任务。
 * Created by haojiangfeng on 2024/7/12.
 */
public interface ITimerTask extends ITimerTaskAction, UniqueKey {


    /**
     * 当前task所属类别
     */
    String getCategory();

    /**
     * 时间间隔策略
     */
    ObserverTimerStrategy getStrategy();

    /**
     * 重置策略中的index。该重置操作可以让间隔拉大型的策略，再度拉小或者拉大
     *
     * @param count 命中次数，也可以理解为游标的概念
     */
    void resetCount(int count);

    /**
     * @return 已经执行的次数
     */
    int getCount();

    /**
     * 每秒钟的计时回调
     */
    void onTick(long currentTimerSecond);

    /**
     * @return task状态
     */
    @ObserverTimerTask.TaskState
    int getTaskState();

    /**
     * @param taskState task状态
     */
    void setTaskState(@ObserverTimerTask.TaskState int taskState);

}