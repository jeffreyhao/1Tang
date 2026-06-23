package com.baidu.baselibrary.timer;

import com.base.global.GlobalBuildConfig;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.baidu.baselibrary.util.clz.MathUtil;


/**
 * 这里目前提供 线性、指数型、乘数型以及斐波那契数列的间隔算法
 * Created by haojiangfeng on 2024/7/12.
 */
public class TimerStrategyFactory {


    /**
     * 线性间隔策略
     */
    public static class LinearStrategy extends BaseStrategy {

        /**
         * 每次间隔时间，单位：秒
         */
        private final long interval;

        public LinearStrategy(long interval){
            this.interval = interval;
        }

        @Override
        protected boolean match(long latestStartSecond) {
            if(interval == 0 || latestStartSecond == 0){
                return false;
            }
            return latestStartSecond % interval == 0;
        }
    }


    /**
     *  本策略是每次间隔数符合斐波那契
     */
    public static class FibonacciStrategy extends BaseStrategy {
        /**
         * 单元时间。    每次间隔时间 = 斐波那契数列中所处位置数值 * 单元时间
         */
        private final long unitInterval;
        /**
         * 下一次命中时的斐波那契秒数。  {1, 2, 3, 5, 8...}
         */
        private long nextFibonacciSecond;

        public FibonacciStrategy(long unitInterval){
            this.unitInterval = unitInterval;
            this.nextFibonacciSecond = unitInterval;
        }

        @Override
        protected boolean match(long latestStartSecond) {
            if(unitInterval == 0 || latestStartSecond == 0){
                return false;
            }
            return latestStartSecond == nextFibonacciSecond;
        }

        @Override
        public void onMatched() {
            super.onMatched();
            nextFibonacciSecond = MathUtil.incrementalFibonacci(getCount() + 1);
            if(GlobalBuildConfig.DEBUG){
                LogUtil.d("FibonacciStrategy.onMatched()", "nextFibonacciSecond = " + nextFibonacciSecond + ", count = " + getCount());
            }
        }

        @Override
        public void resetCount(int count) {
            super.resetCount(count);
            nextFibonacciSecond = MathUtil.incrementalFibonacci(getCount() + 1);
        }
    }

    /**
     * 指数型间隔策略
     */
    public static class ExponentialStrategy extends BaseStrategy {
        /**
         * 底数
         */
        private final long baseNumber;
        /**
         * 下一个命中时的指数值
         */
        private long nextExponentialValue;

        /**
         * 指数型间隔策略。 指数=底数^n =（底数的n次幂）
         * @param baseNumber 底数
         */
        public ExponentialStrategy(int baseNumber){
            this.baseNumber = baseNumber;
            this.nextExponentialValue = baseNumber;
        }

        @Override
        protected boolean match(long latestStartSecond) {
            if(baseNumber == 0 || latestStartSecond ==0){
                return false;
            }
            return latestStartSecond == nextExponentialValue;
        }

        @Override
        public void onMatched() {
            super.onMatched();
            // baseNumber 的 第 （n+1）次幂
            nextExponentialValue = (long) Math.pow(baseNumber, getCount() + 1);
            if(GlobalBuildConfig.DEBUG){
                LogUtil.d("ExponentialStrategy.onMatched()", "nextExponentialValue = " + nextExponentialValue + ", count = " + getCount());
            }
        }

        @Override
        public void resetCount(int count) {
            super.resetCount(count);
            nextExponentialValue = (long) Math.pow(baseNumber, getCount() + 1);
        }
    }

    /**
     * 乘数型间隔策略
     */
    public static class MultiplierStrategy extends BaseStrategy {
        /**
         * 单元时间。
         */
        private final long unitInterval;
        /**
         * 下一个命中时的乘数值
         */
        private long nextMultiplierValue;

        public MultiplierStrategy(int unitInterval){
            this.unitInterval = unitInterval;
        }

        @Override
        protected boolean match(long latestStartSecond) {
            if(unitInterval == 0 || latestStartSecond ==0){
                return false;
            }
            return latestStartSecond == nextMultiplierValue;
        }

        @Override
        public void onMatched() {
            super.onMatched();
            // baseNumber 的 第 （n+1）次幂
            nextMultiplierValue = (long) unitInterval * (getCount() + 1);
            if(GlobalBuildConfig.DEBUG){
                LogUtil.d("MultiplierStrategy.onMatched()" + "nextMultiplierValue = " + nextMultiplierValue + ", count = " + getCount());
            }
        }

        @Override
        public void resetCount(int count) {
            super.resetCount(count);
            nextMultiplierValue = (long) unitInterval * (getCount() + 1);
        }
    }


}
