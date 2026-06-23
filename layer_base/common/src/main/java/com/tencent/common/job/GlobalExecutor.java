package com.tencent.common.job;

import android.util.Log;

import com.tencent.common.util.DeviceUtils;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 全局线程池执行器
 * <pre>
 *     1.并发线程数保持在{@link #coreSize}，等于CPU核数*2（过多了CPU时间片过多的轮转分配造成吞吐量降低，过少了不能充分利用CPU）
 *     2.并发线程数多于{@link #coreSize}时会加入等待队列
 * </pre>
 *
 * @author adison
 * @date 17/5/25
 * @time 下午6:35
 */
public class GlobalExecutor implements Executor {
    private static final String TAG = GlobalExecutor.class.getSimpleName();
    /**
     * 调试模式
     */
    private boolean debug = false;

    /**
     * 默认空闲时间为5分钟
     */
    private static final int DEFAULT_CACHE_SENCOND = 5;
    private static ThreadPoolExecutor threadPool;

    private int coreSize;
    private int queueSize;
    private final Object lock = new Object();
    private LinkedList<WrappedRunnable> runningList = new LinkedList<WrappedRunnable>();
    private LinkedList<WrappedRunnable> waitingList = new LinkedList<WrappedRunnable>();

    private static class GlobalExecutorHolder {
        private static final GlobalExecutor INSTANCE = new GlobalExecutor();
    }

    public static GlobalExecutor getInstance() {
        return GlobalExecutorHolder.INSTANCE;
    }

    private GlobalExecutor() {
        init();
    }

    private void init() {
        int cpuCore = DeviceUtils.getCoresNumbers();
        coreSize = cpuCore * 2;
        queueSize = coreSize * 32;
        initThreadPool();
    }


    private void initThreadPool() {
        if (debug) {
            Log.v(TAG, "GlobalExecutor core-queue size: " + coreSize + " - " + queueSize
                    + "  running-wait task: " + runningList.size() + " - " + waitingList.size());
        }
        if (threadPool == null) {
            threadPool = createDefaultThreadPool();
        }
    }

    /**
     * 创建默认线程池
     *
     * @return
     */
    private ThreadPoolExecutor createDefaultThreadPool() {
        // 控制最多4个keep在pool中
        int corePoolSize = Math.min(4, coreSize);
        return new ThreadPoolExecutor(
                corePoolSize,
                Integer.MAX_VALUE,
                DEFAULT_CACHE_SENCOND, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadFactory() {
                    static final String NAME = "lite-";
                    AtomicInteger IDS = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, NAME + IDS.getAndIncrement());
                    }
                },
                new ThreadPoolExecutor.DiscardPolicy());
    }

    /**
     * 设置调试模式
     *
     * @param debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    /**
     * 取消等待任务
     *
     * @param command
     * @return
     */
    public boolean cancelWaitingTask(Runnable command) {
        boolean removed = false;
        synchronized (lock) {
            int size = waitingList.size();
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    if (waitingList.get(i).getRealRunnable() == command) {
                        waitingList.remove(i);
                        removed = true;
                    }
                }
            }
        }
        return removed;
    }

    interface WrappedRunnable extends Runnable {
        Runnable getRealRunnable();
    }

    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new FutureTask<T>(runnable, value);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new FutureTask<T>(callable);
    }

    /**
     * 提交 runnable
     *
     * @param task
     * @return
     */
    public Future<?> submit(Runnable task) {
        RunnableFuture<Void> ftask = newTaskFor(task, null);
        execute(ftask);
        return ftask;
    }


    /**
     * 提交 callable
     *
     * @param task
     * @param <T>
     * @return
     */
    public <T> Future<T> submit(Callable<T> task) {
        RunnableFuture<T> ftask = newTaskFor(task);
        execute(ftask);
        return ftask;
    }


    /**
     * submit RunnableFuture task
     */
    public <T> void submit(RunnableFuture<T> task) {
        execute(task);
    }


    /**
     * 执行任务
     * <pre>
     *     1.如果正在执行的任务小于 {@link #coreSize}，直接把任务加入{@link #runningList}并马上执行
     *     2.如果正在执行的任务大于 {@link #coreSize}，把任务加入{@link #waitingList}，等待执行
     *     3.如果任务执行完毕，会自动执行下一个任务直到{@link #waitingList}为空
     *     注意：应该从业务层面避免同时执行太多任务，目前对于加入的任务大于{@link #queueSize}不做处理，但需要上报
     * </pre>
     *
     * @param command
     */
    @Override
    public void execute(final Runnable command) {
        if (command == null) {
            return;
        }

        WrappedRunnable scheduler = new WrappedRunnable() {
            @Override
            public Runnable getRealRunnable() {
                return command;
            }

            @Override
            public void run() {
                try {
                    command.run();
                } finally {
                    scheduleNext(this);
                }
            }
        };

        synchronized (lock) {
            if (debug) {
                Log.d(TAG, "GlobalExecutor core-queue size: " + coreSize + " - " + queueSize
                        + "  running-wait task: " + runningList.size() + " - " + waitingList.size());
            }
            if (runningList.size() < coreSize) {
                runningList.add(scheduler);
                threadPool.execute(scheduler);
                Log.d(TAG, "GlobalExecutor task execute");
            } else if (waitingList.size() < queueSize) {
                waitingList.addLast(scheduler);
                Log.d(TAG, "GlobalExecutor task waiting");
            } else {
                waitingList.addLast(scheduler);
                if (debug) {
                    if (waitingList.size() < queueSize) {
                        Log.d(TAG, "GlobalExecutor task waiting");
                    } else {
                        //TODO 捕捉上传
                        Log.w(TAG, "GlobalExecutor overload queueSize:" + queueSize + ", waitingList size:" + waitingList.size());
                    }
                }
            }
            printThreadPoolInfo();
        }
    }

    /**
     * 执行下一个任务
     *
     * @param scheduler
     */
    private void scheduleNext(WrappedRunnable scheduler) {
        synchronized (lock) {
            boolean suc = runningList.remove(scheduler);
            if (debug) {
                Log.v(TAG, "Thread " + Thread.currentThread().getName()
                        + " is completed. remove prior: " + suc + ", try schedule next..");
            }
            if (!suc) {
                runningList.clear();
                Log.e(TAG,
                        "GlobalExecutor scheduler remove failed, so clear all(running list) to avoid unpreditable error : " + scheduler);
            }
            if (waitingList.size() > 0) {
                //后进先出
                WrappedRunnable waitingRun = waitingList.pollLast();

                if (waitingRun != null) {
                    runningList.add(waitingRun);
                    threadPool.execute(waitingRun);
                    Log.v(TAG, "Thread " + Thread.currentThread().getName() + " execute next task..");
                } else {
                    Log.e(TAG,
                            "GlobalExecutor get a NULL task from waiting queue: " + Thread.currentThread().getName());
                }
            } else {
                if (debug) {
                    Log.v(TAG, "GlobalExecutor: all tasks is completed. current thread: " +
                            Thread.currentThread().getName());
                    printThreadPoolInfo();
                }
            }
        }
    }

    public void printThreadPoolInfo() {
        if (debug) {
            Log.i(TAG, "___________________________");
            Log.i(TAG, "state (shutdown - terminating - terminated): " + threadPool.isShutdown()
                    + " - " + threadPool.isTerminating() + " - " + threadPool.isTerminated());
            Log.i(TAG, "pool size (core - max): " + threadPool.getCorePoolSize()
                    + " - " + threadPool.getMaximumPoolSize());
            Log.i(TAG, "task (active - complete - total): " + threadPool.getActiveCount()
                    + " - " + threadPool.getCompletedTaskCount() + " - " + threadPool.getTaskCount());
            Log
                    .i(TAG, "waitingList size : " + threadPool.getQueue().size() + " , " + threadPool.getQueue());
        }
    }

    public int getCoreSize() {
        return coreSize;
    }

    public int getRunningSize() {
        return runningList.size();
    }

    public int getWaitingSize() {
        return waitingList.size();
    }

    public int getQueueSize() {
        return queueSize;
    }

}
