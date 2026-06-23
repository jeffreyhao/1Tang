package com.baidu.baselibrary.timer;

import com.base.global.GlobalBuildConfig;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.ALogJsonBuilder;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.base.util.thread.AppExecutors;
import com.base.util.thread.ExecutorsUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Executor;

import androidx.annotation.CallSuper;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

/**
 *  定时任务task。
 *  如果有定时任务，请优先考虑下WorkManager的方案，其次再考虑本方案。
 * Created by haojiangfeng on 2024/7/12.
 */
public abstract class ObserverTimerTask implements ITimerTask, Runnable, Comparable<ObserverTimerTask> {

    /**
     * 任务结束标记。
     * <br>
     *  NOT_OVER-0-未结束，OVER_CANCEL-1-取消，OVER_FINISH-2-正常结束
     */
    private static final int NOT_OVER = 0, OVER_CANCEL = 1, OVER_FINISH = 2;

    /**
     * run()结束方式：1-自动结束；2-手动结束
     */
    @IntDef({RunFinishMode.RUN_FINISH_AUTO, RunFinishMode.RUN_FINISH_MANUAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RunFinishMode {
        /**
         *  标记run()执行完成的方式：1-自动设置完成
         *  <br>
         *  只管触发，触发后自动开始下一轮计时
         */
        int RUN_FINISH_AUTO     = 1;
        /**
         *  标记run()执行完成的方式：2-手动设置完成
         *  <br>
         *  手动设定本轮触发后的run()方法完成，并手动开启下一轮计时
         */
        int RUN_FINISH_MANUAL = 2;
    }

    /**
     * 任务状态
     */
    @IntDef({TaskState.INIT, TaskState.RUNNABLE, TaskState.RUNNING, TaskState.PAUSED, TaskState.CANCELLED, TaskState.FINISHED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TaskState {
        int INIT       = 0; // 初始状态
        int RUNNABLE   = 1; // 可执行，但目前不在执行
        int RUNNING    = 2; // 执行中
        int PAUSED     = 3; // 暂停
        int CANCELLED  = 4; // 已取消
        int FINISHED   = 5; // 已结束
    }

    /**
     * 优先级
     */
    @IntDef({TaskPriority.EXTREMELY_HIGH, TaskPriority.HIGH, TaskPriority.LITTLE_HIGH, TaskPriority.COMMON, TaskPriority.LITTLE_LOW, TaskPriority.LOW, TaskPriority.EXTREMELY_LOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TaskPriority {
        int EXTREMELY_HIGH             = 7; // 极高优先级
        int HIGH                       = 6; // 高优先级
        int LITTLE_HIGH                = 5; // 一般偏高优先级
        int COMMON                     = 4; // 一般优先级，默认值
        int LITTLE_LOW                 = 3; // 一般偏低优先级
        int LOW                        = 2; // 低优先级
        int EXTREMELY_LOW              = 1; // 极低优先级
    }



    /**
     * 标识当前task的唯一key
     */
    private @NonNull final String uniqueKey;
    /**
     * 类别
     */
    private @Nullable final String category;
    /**
     * Task状态
     */
    private final ObservableField<Integer> mObservableTaskState;
    /**
     * 最大限制次数
     */
    private int maxCount = Integer.MAX_VALUE;
    /**
     * 是否已unregister。 0-未取消注册，1-调用cancel取消注册，2-调用finish取消注册
     */
    private int isUnregistered = NOT_OVER;
    /**
     * run()执行完成设置标记。
     *      1-自动设置完成，缺省值。
     *      2-手动设置完成。
     * 如果为2，则需要在run()执行完成或者其回调中 手动调用{@link #restartTiming()}。
     */
    private @RunFinishMode int runFinishFlag = RunFinishMode.RUN_FINISH_AUTO;
    /**
     * 优先级
     */
    private @TaskPriority int mTimerPriority = TaskPriority.COMMON;
    /**
     * 线程调度器，缺省使用网络线程。
     */
    private @NonNull Executor mExecutor = ExecutorsUtils.getInstance().getAppExecutors().networkIO();
    /**
     * 时间间隔策略。这里使用策略模式来定制时间间隔算法。
     */
    private @NonNull ObserverTimerStrategy mTimerStrategy;


    /**
     *      线程调度器为网络线程；
     *
     * @param uniqueKey 标识当前task的唯一key
     * @param category  所属类别
     * @param strategy  时间间隔策略
     */
    public ObserverTimerTask(@NonNull String uniqueKey, @Nullable String category, @NonNull ObserverTimerStrategy strategy){
        this.uniqueKey = uniqueKey;
        this.category = category;
        this.mTimerStrategy = strategy;
        this.mObservableTaskState = new ObservableField<>(TaskState.INIT);
        this.mObservableTaskState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                onTaskStateChanged(propertyId);
            }
        });
    }

    /**
     *      线程调度器为网络线程；
     *
     * @param uniqueKey 标识当前task的唯一key
     * @param strategy  时间间隔策略
     */
    public ObserverTimerTask(@NonNull String uniqueKey, @NonNull ObserverTimerStrategy strategy){
        this(uniqueKey, null, strategy);
    }

    /**
     *      线程调度器为网络线程；
     *
     * @param uniqueKey 标识当前task的唯一key
     * @param strategy  时间间隔策略
     * @param maxCount  可重复执行的最大次数
     */
    public ObserverTimerTask(@NonNull String uniqueKey, @NonNull ObserverTimerStrategy strategy, int maxCount){
        this(uniqueKey, null, strategy);
        maxCount(maxCount);
    }

    /**
     * @param uniqueKey 标识当前task的唯一key
     * @param strategy  时间间隔策略
     * @param maxCount  可重复执行的最大次数
     * @param executor  线程调度器  {@link AppExecutors}
     */
    public ObserverTimerTask(@NonNull String uniqueKey, @NonNull ObserverTimerStrategy strategy, int maxCount, Executor executor){
        this(uniqueKey, null, strategy);
        maxCount(maxCount);
        executor(executor);
    }

    /**
     * 重置时间间隔策略
     * @param strategy  时间间隔策略
     */
    public ObserverTimerTask resetStrategy(@NonNull ObserverTimerStrategy strategy){
        this.mTimerStrategy = strategy;
        return this;
    }

    /**
     * @param maxCount  可重复执行的最大次数
     */
    public ObserverTimerTask maxCount(int maxCount){
        this.maxCount = maxCount;
        return this;
    }

    /**
     * @param executor  线程调度器  {@link AppExecutors}
     */
    public ObserverTimerTask executor(@NonNull Executor executor){
        this.mExecutor = executor;
        return this;
    }

    /**
     * @param runFinishFlag run()执行完成设置。  {@link RunFinishMode}
     */
    public ObserverTimerTask setRunFinishFlag(@RunFinishMode int runFinishFlag){
        this.runFinishFlag = runFinishFlag;
        return this;
    }

    /**
     * @param priority 优先级
     */
    public ObserverTimerTask priority(@TaskPriority int priority){
        this.mTimerPriority = priority;
        return this;
    }

    @Override
    public String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public ObserverTimerStrategy getStrategy(){
        return mTimerStrategy;
    }

    @CallSuper
    @Override
    public void restartTiming(){
        mTimerStrategy.resetTiming();
        mObservableTaskState.set(TaskState.RUNNABLE);
    }

    @Override
    public void continueTiming() {
        mObservableTaskState.set(TaskState.RUNNABLE);
    }

    @CallSuper
    @Override
    public void pauseTiming() {
        mObservableTaskState.set(TaskState.PAUSED);
    }

    @CallSuper
    @Override
    public void cancelTimingAndUnregister() {
        ObservableSecondTimer.getInstance().unregisterTask(this);
        if(getTaskState() == TaskState.RUNNING){
            isUnregistered = OVER_CANCEL;
        } else {
            mObservableTaskState.set(TaskState.CANCELLED);
        }
    }

    @CallSuper
    @Override
    public void clearTiming() {
        mTimerStrategy.resetTiming();
    }


    @Override
    public void execRunnable() {
        mExecutor.execute(() -> {
            clearTiming();
            mObservableTaskState.set(TaskState.RUNNING);
            ObserverTimerTask.this.run();
            setStateWhenRun();
        });
    }

    @CallSuper
    @Override
    public void resetCount(int count) {
        mTimerStrategy.resetCount(count);
        restartTiming();
    }

    @Override
    public int getCount(){
        return mTimerStrategy.getCount();
    }

    @Override
    public @TaskState int getTaskState(){
        Integer integer = mObservableTaskState.get();
        return integer == null ? TaskState.INIT : integer;
    }

    @Override
    public @TaskPriority int getPriority(){
        return mTimerPriority;
    }

    @Override
    public void setTaskState(@TaskState int taskState) {
        mObservableTaskState.set(taskState);
    }

    /**
     * 每秒钟的计时回调
     */
    @Override
    public void onTick(long currentTimerSecond){
        int taskState = getTaskState();
        if(taskState == TaskState.RUNNING || taskState == TaskState.PAUSED){
            return;
        }
        if(mTimerStrategy.matchOnTick(currentTimerSecond)){

            execRunnable();

            if(GlobalBuildConfig.DEBUG) {
                ALog.jsonObject(ALog.V, "ObserverTimerTask", "onTick", new String[]{LogTag.ACTION, LogTag.Action_Tick_Run},
                        ALogJsonBuilder.get()
                                .put("currentSecond", currentTimerSecond)
                                .put("uniqueKey", uniqueKey)
                                .put("count", mTimerStrategy.getCount())
                                .put("startedSeconds", mTimerStrategy.getLatestStartSeconds())
                                .build());
            }

            mTimerStrategy.onMatched();
        }
        if(mTimerStrategy.getCount() >= maxCount){
            ObservableSecondTimer.getInstance().unregisterTask(this);
            if(getTaskState() == TaskState.RUNNING){
                isUnregistered = OVER_FINISH;
            } else {
                mObservableTaskState.set(TaskState.FINISHED);
            }
        }
    }

    /**
     * runFinishFlag如果是自动设置，则在 run() 执行之后，就立即重置task的执行状态。
     * 如果需要手动设置，即runFinishFlag={@link RunFinishMode#RUN_FINISH_MANUAL}，需要自己手动重置 mObservableTaskState
     */
    private void setStateWhenRun() {
        if(runFinishFlag == RunFinishMode.RUN_FINISH_AUTO){
            switch (isUnregistered){
                case OVER_CANCEL: mObservableTaskState.set(TaskState.CANCELLED); break;
                case OVER_FINISH: mObservableTaskState.set(TaskState.FINISHED); break;
                default: restartTiming(); break;
            }
        }
    }

    @Override
    public int compareTo(ObserverTimerTask task) {
        // 优先级数值越大，优先级越高
        return Integer.compare(task.getPriority(), this.getPriority());
    }


    /**
     * 状态改变
     * @param taskState 新状态
     */
    protected void onTaskStateChanged(@TaskState int taskState) {
        if(GlobalBuildConfig.DEBUG){
//            LogUtil.v("ObserverTimerTask", "Task[" + getUniqueKey() + "] onTaskStateChanged(" + taskStateString(taskState) + ")");
        }
    }

    private static String taskStateString(@TaskState int taskState){
        switch (taskState){
            case TaskState.RUNNABLE:    return "RUNNABLE";
            case TaskState.RUNNING:     return "RUNNING";
            case TaskState.PAUSED:      return "PAUSED";
            case TaskState.CANCELLED:   return "CANCELLED";
            case TaskState.FINISHED:    return "FINISHED";
            case TaskState.INIT:
            default:                    return "INIT";
        }
    }

}
