package com.baidu.baselibrary.timer;

import androidx.annotation.CallSuper;


/**
 * 可继承「BaseStrategy」来实现时间间隔算法
 */
abstract class BaseStrategy implements ObserverTimerStrategy{

    /**
     * 最近开始时的秒数
     */
    private long latestStartSecond;

    /**
     * 命中的次数
     */
    private int index;


    @Override
    public int getCount() {
        return index;
    }

    @Override
    public long getLatestStartSeconds(){
        return latestStartSecond;
    }

    /**
     *
     * 重置index。
     * <br>
     * ⚠️注意：重置index后，task需要调用 {@link ObserverTimerTask#restartTiming()}，否则可能不生效
     *
     * @param count 命中的次数。也相当于游标
     */
    @Override
    @CallSuper
    public void resetCount(int count){
        this.index = count;
    }

    @Override
    public void resetTiming() {
        this.latestStartSecond = 0;
    }

    @Override
    public boolean matchOnTick(long currentTimerSecond) {
        latestStartSecond ++;
        if(index == 0 || match(latestStartSecond)){
            return true;
        }
        return false;
    }

    /**
     * 匹配成功后的回调。一般需要重新计算下一次命中的时间间隔。
     */
    @CallSuper
    @Override
    public void onMatched() {
        latestStartSecond = 0;
        index ++;
    }

    /**
     * @param latestStartSecond 本次计时的秒数
     * @return 是否匹配
     */
    protected abstract boolean match(long latestStartSecond);
}