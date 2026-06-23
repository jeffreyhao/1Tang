package com.baidu.baselibrary.util.ui;

import java.util.LinkedList;
import java.util.Queue;

public class FunctionLimiter {
    private static final int MAX_CALLS_PER_SECOND = 1;
    private final Queue<Long> callTimestamps;

    public FunctionLimiter() {
        callTimestamps = new LinkedList<>();
    }

    public synchronized boolean canCallFunction() {
        long currentTime = System.currentTimeMillis();
        long oneSecondAgo = currentTime - 1000;

        Long peekTime = callTimestamps.peek();
        if(peekTime==null){
            peekTime = 0L;
        }
        while (!callTimestamps.isEmpty() && peekTime < oneSecondAgo) {
            callTimestamps.poll();
        }

        if (callTimestamps.size() >= MAX_CALLS_PER_SECOND) {
            return false;
        }

        callTimestamps.offer(currentTime);
        return true;
    }
}
