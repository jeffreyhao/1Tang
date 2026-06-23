package com.benefit.utils.Executor;

import android.os.Process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 全局线程池
 * https://blog.csdn.net/wuyinlei/article/details/73864131
 */
public class DefaultExecutorSupplier {
    /*
     * Number of cores to decide the number of threads
     */
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    int KEEP_ALIVE_TIME = 1;

    TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    /*
     * thread pool executor for background tasks
     * 普通线程池
     */
//    private final ThreadPoolExecutor mForBackgroundTasks;
    /**
     * 可以指定任务优先级线程
     */
    private final PriorityThreadPoolExecutor mForBackgroundTasks;
    /*
     * thread pool executor for light weight background tasks
     */
    private final ThreadPoolExecutor mForLightWeightBackgroundTasks;

    /*
     * thread pool executor for main thread tasks
     */
    private final Executor mMainThreadExecutor;
    /*
     * an instance of DefaultExecutorSupplier
     */
    private static DefaultExecutorSupplier sInstance;

    /*
     * returns the instance of DefaultExecutorSupplier
     */
    public static DefaultExecutorSupplier getInstance() {
        if (sInstance == null) {
            synchronized (DefaultExecutorSupplier.class) {
                sInstance = new DefaultExecutorSupplier();
            }
        }
        return sInstance;
    }

//    BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    PriorityBlockingQueue<Runnable> taskQueue = new PriorityBlockingQueue<Runnable>();

    BlockingQueue<Runnable> taskLWQueue = new LinkedBlockingQueue<Runnable>();

    /*
     * constructor for  DefaultExecutorSupplier
     */
    private DefaultExecutorSupplier() {

        // setting the thread factory
        ThreadFactory backgroundPriorityThreadFactory = new
                PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);

        // setting the thread pool executor for mForBackgroundTasks;
//        mForBackgroundTasks = new ThreadPoolExecutor(
//                NUMBER_OF_CORES,
//                NUMBER_OF_CORES * 2,
//                KEEP_ALIVE_TIME,
//                KEEP_ALIVE_TIME_UNIT,
//                taskQueue,
//                backgroundPriorityThreadFactory
//        );
        //创建可以指定任务优先级的线程池
        mForBackgroundTasks = new PriorityThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES * 2,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                taskQueue,
                backgroundPriorityThreadFactory
        );
        // setting the thread pool executor for mForLightWeightBackgroundTasks;
        mForLightWeightBackgroundTasks = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES * 2,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                taskLWQueue,
                backgroundPriorityThreadFactory
        );

        // setting the thread pool executor for mMainThreadExecutor;
        mMainThreadExecutor = new MainThreadExecutor();

    }

    /*
     * returns the thread pool executor for background task
     */
    public ThreadPoolExecutor forBackgroundTasks() {
        return mForBackgroundTasks;
    }

    /*
     * returns the thread pool executor for light weight background task
     */
    public ThreadPoolExecutor forLightWeightBackgroundTasks() {
        return mForLightWeightBackgroundTasks;
    }

    /*
     * returns the thread pool executor for main thread task
     */
    public Executor forMainThreadTasks() {
        return mMainThreadExecutor;
    }
}


//    public void doSomeTaskAtHighPriority(){
//        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
//                .submit(new PriorityRunnable(Priority.HIGH) {
//                    @Override
//                    public void run() {
//                        // do some background work here at high priority.
//                    }
//                });
//    }
//    /*
//     * Using it for Background Tasks
//     */
//    public void doSomeBackgroundWork(){
//        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
//                .execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        // do some background work here.
//                    }
//                });
//    }
//
//    /*
//     * Using it for Light-Weight Background Tasks
//     */
//    public void doSomeLightWeightBackgroundWork(){
//        DefaultExecutorSupplier.getInstance().forLightWeightBackgroundTasks()
//                .execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        // do some light-weight background work here.
//                    }
//                });
//    }
//
//    /*
//     * Using it for MainThread Tasks
//     */
//    public void doSomeMainThreadWork(){
//        DefaultExecutorSupplier.getInstance().forMainThreadTasks()
//                .execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        // do some Main Thread work here.
//                    }
//                });
//    }