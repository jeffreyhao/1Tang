package com.baidu.baselibrary.util.clz;


public class MathUtil {

    /*
        n = 0, fibonacci = 0
        n = 1, fibonacci = 1
        n = 2, fibonacci = 1
        n = 3, fibonacci = 2
        n = 4, fibonacci = 3
        n = 5, fibonacci = 5
        n = 6, fibonacci = 8
        n = 7, fibonacci = 13
        n = 8, fibonacci = 21
        n = 9, fibonacci = 34
        n = 10, fibonacci = 55
        n = 11, fibonacci = 89
        n = 12, fibonacci = 144
        n = 13, fibonacci = 233
        n = 14, fibonacci = 377
        n = 15, fibonacci = 610
        n = 16, fibonacci = 987
        n = 17, fibonacci = 1597
        n = 18, fibonacci = 2584
        n = 19, fibonacci = 4181
     */

    /**
     *  ⚠️注意：n特别大时候，该方法性能很差
     *
     * @param n 数列中第n个位置
     * @return  斐波那契数列第n位的值 {0-0, 1-1, 2-1, 3-2, 4-3, 5-5, 6-8, 7-13}
     */
    public static long fibonacci(int n) {
        if (n <= 1) return n;

        long prev = 0, current = 1;
        for (int i = 2; i <= n; i++) {
            long next = prev + current;
            prev = current;
            current = next;
        }
//        LogUtil.e("ObserverTimerTask", "fibonacci()-> n=" + n + ", current=" + current);

        return current;
    }


    /**
     * 递增的斐波那契数值 {0, 1, 2, 3, 5, 8...}
     */
    public static long incrementalFibonacci(int n) {
        if (n <= 2) return n; // 0-0, 1-1, 2-2
        /*
         *  {3-3, 4-5, 5-8, 6-13, ...}
         */
        return fibonacci(n + 1);
    }

}
