package com.baidu.baselibrary.timer;

import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.Nullable;

/**
 * 有优先级的定时任务队列。
 * </br>
 * 该队列只会让优先级最高的任务处于可执行状态，其他任务都会处于暂停状态。
 * </br>
 * Created by haojiangfeng on 2024/7/24.
 */
public class PriorityTimerTaskMap<T extends ITimerTaskAction> {

    /**
     *  task集合。key-uniqueKey
     */
    private final Map<String, T> taskMap;
    /**
     *  优先级队列
     */
    private final PriorityQueue<T> priorityQueue;
    /**
     * 优先级最高的task
     */
    private T topTask;

    public PriorityTimerTaskMap() {
        taskMap = new ConcurrentHashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            priorityQueue = new PriorityQueue<>((o1, o2) -> {
                // 优先级数值越大，优先级越高
                return o2.getPriority() - o1.getPriority();
            });
        } else {
            priorityQueue = new PriorityQueue<>();
        }
    }

    public boolean contains(ObserverTimerTask task){
        return task != null && contains(task.getUniqueKey());
    }

    public boolean contains(String uniqueKey){
        return !TextUtils.isEmpty(uniqueKey) && taskMap.containsKey(uniqueKey);
    }

    public void addTask(String key, T task) {
        taskMap.put(key, task);
        priorityQueue.add(task);
        updateTaskStates();
    }

    public void removeTask(String key) {
        T removedTask = taskMap.remove(key);
        if (removedTask!= null) {
            priorityQueue.remove(removedTask);
            updateTaskStates();
        }
    }

    public T get(String uniqueKey){
        return taskMap.get(uniqueKey);
    }

    public @Nullable T getTopTask(){
        return topTask;
    }

    private void updateTaskStates() {
        if(priorityQueue.size() == 0){
            return;
        }
        topTask = priorityQueue.peek();
        List<T> tasks = new ArrayList<>(priorityQueue);
        for (ITimerTaskAction task : tasks) {
            if (task == topTask) {
                task.restartTiming();
            } else {
                task.pauseTiming();
            }
        }
    }

}
