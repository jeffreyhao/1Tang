package com.baidu.baselibrary.timer;


import android.text.TextUtils;

import com.base.global.GlobalBuildConfig;
import com.baidu.baselibrary.util.sys.LogUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 可观察的秒计时器，每秒计时一次
 * Created by haojiangfeng on 2024/7/12.
 */
public class ObservableSecondTimer {

    private static final ObservableSecondTimer mInstance = new ObservableSecondTimer();
    private ObservableSecondTimer(){
        init();
    }
    public static ObservableSecondTimer getInstance(){
        return mInstance;
    }


    public static ObservableSecondTimer register(ObserverTimerTask task) {
        return ObservableSecondTimer.getInstance().registerTask(task);
    }

    public static ObservableSecondTimer unregister(ObserverTimerTask task) {
        return ObservableSecondTimer.getInstance().unregisterTask(task);
    }

    public static ObservableSecondTimer unregister(String uniqueKey) {
        return ObservableSecondTimer.getInstance().unregisterTask(uniqueKey);
    }

    public static ObserverTimerTask findTask(String uniqueKey) {
        return ObservableSecondTimer.getInstance().getTask(uniqueKey);
    }



    private Disposable disposable;

    /**
     *  无类别的task-map。 key：uniqueKey
     */
    private final Map<String, ObserverTimerTask> mTaskMap = new ConcurrentHashMap<>();

    /**
     *  各种类别的task-map。  key：category
     *  </br>
     *  同一类别的任务，只有优先级最高的那个task处于Runnable状态，其他任务都先暂停
     */
    private final Map<String, PriorityTimerTaskMap<ObserverTimerTask>> mCategoryTaskMap = new ConcurrentHashMap<>();




    private void init() {

    }

    public boolean haveStarted(){
        return disposable != null && !disposable.isDisposed();
    }

    public void start(){
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(this::onTick);
        for (ObserverTimerTask task : mTaskMap.values()) {
            if (task != null) {
                task.restartTiming();
            }
        }
    }

    public void stop() {
        if (null != disposable) {
            disposable.dispose();
            disposable = null;
        }
    }

    private void onTick(long currentTimerSecond){
        if(GlobalBuildConfig.DEBUG){
//            LogUtil.v("ObserverTimer.onTick(" + currentTimerSecond + ")");
        }
        for (ObserverTimerTask task : mTaskMap.values()) {
            if (task != null) {
                task.onTick(currentTimerSecond);
            }
        }
        for (PriorityTimerTaskMap<ObserverTimerTask> taskMap : mCategoryTaskMap.values()){
            ObserverTimerTask topTask = taskMap.getTopTask();
            if(topTask != null){
                topTask.onTick(currentTimerSecond);
            }
        }
    }

    /**
     * 注册任务
     * </br>
     *      1. 如果没有类别，放入mTaskMap
     *      2. 有类别，则放入mCategoryTaskMap，该map只能有一个任务处于Runnable状态
     * </br>
     * @param task 定时任务
     */
    public ObservableSecondTimer registerTask(ObserverTimerTask task) {
        if (task == null || TextUtils.isEmpty(task.getUniqueKey())) {
            return this;
        }
        if(TextUtils.isEmpty(task.getCategory())){
            if (!mTaskMap.containsKey(task.getUniqueKey())) {
                mTaskMap.put(task.getUniqueKey(), task);
            } else {
                LogUtil.w("Observer [" + task + "] is already registered.");
            }
        } else {
            PriorityTimerTaskMap<ObserverTimerTask> taskMap = null;
            if (mCategoryTaskMap.containsKey(task.getCategory())) {
                taskMap = mCategoryTaskMap.get(task.getCategory());
            }
            if(taskMap == null){
                taskMap = new PriorityTimerTaskMap<>();
            }
            if(!taskMap.contains(task)){
                taskMap.addTask(task.getUniqueKey(), task);
            } else {
                LogUtil.w("Observer [" + task + "] is already registered.");
            }
            mCategoryTaskMap.put(task.getCategory(), taskMap);
        }
        if(haveStarted()){
            task.restartTiming();
        }
        if(GlobalBuildConfig.DEBUG){
            LogUtil.v("ObserverTimer.registerTask(" + task.getUniqueKey() + ")");
        }
        return this;
    }

    public ObservableSecondTimer unregisterTask(ObserverTimerTask task) {
        if (task == null || TextUtils.isEmpty(task.getUniqueKey())) {
            return this;
        }
        if(TextUtils.isEmpty(task.getCategory())){
            mTaskMap.remove(task.getUniqueKey());
        } else {
            if (mCategoryTaskMap.containsKey(task.getCategory())) {
                PriorityTimerTaskMap<ObserverTimerTask> taskMap = mCategoryTaskMap.get(task.getCategory());
                if(taskMap != null){
                    taskMap.removeTask(task.getUniqueKey());
                }
            }
        }
        if(GlobalBuildConfig.DEBUG){
            LogUtil.v("ObserverTimer.unregisterTask(" + task.getUniqueKey() + ")");
        }
        return this;
    }

    public ObservableSecondTimer unregisterTask(String uniqueKey) {
        if (TextUtils.isEmpty(uniqueKey)) {
            return this;
        }
        if(mTaskMap.containsKey(uniqueKey)){
            mTaskMap.remove(uniqueKey);
        } else if(mCategoryTaskMap.size() > 0){
            Collection<PriorityTimerTaskMap<ObserverTimerTask>> collection = mCategoryTaskMap.values();
            for(PriorityTimerTaskMap<ObserverTimerTask> taskMap : collection){
                taskMap.removeTask(uniqueKey);
            }
        }
        if(GlobalBuildConfig.DEBUG){
            LogUtil.v("ObserverTimer.unregisterTask(" + uniqueKey + ")");
        }
        return this;
    }

    public @Nullable ObserverTimerTask getTask(String uniqueKey){
        if (TextUtils.isEmpty(uniqueKey)) {
            return null;
        }
        if(mTaskMap.containsKey(uniqueKey)){
            return mTaskMap.get(uniqueKey);
        }
        if(mCategoryTaskMap.size() == 0){
            return null;
        }
        Collection<PriorityTimerTaskMap<ObserverTimerTask>> collection = mCategoryTaskMap.values();
        for(PriorityTimerTaskMap<ObserverTimerTask> taskMap : collection){
            if(taskMap.contains(uniqueKey)){
                return taskMap.get(uniqueKey);
            }
        }
        return null;
    }



}
