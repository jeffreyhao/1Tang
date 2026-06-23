package com.benefit.utils.Executor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPoolByFIFOQueue {

    private static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY - 1;

    private static Executor mExecutorService;

    public static Executor getExecutor() {
        if (mExecutorService == null || ((ExecutorService) mExecutorService).isShutdown()) {
            synchronized (ThreadPoolByFIFOQueue.class) {
                mExecutorService = createExecutor();
            }
            return mExecutorService;
        }
        return mExecutorService;
    }

    public static void submit(Runnable runnable) {
        synchronized (ThreadPoolByFIFOQueue.class) {
            getExecutor().execute(runnable);
        }
    }

    private static Executor createExecutor() {
        return mExecutorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setPriority(DEFAULT_THREAD_PRIORITY);
                thread.setName("FIFO Thread");
                return thread;
            }
        });
    }
}
