package com.benefit.novelverse.utils;

import com.benefit.listener.RetryListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Time: 2023/12/27
 * Author: lhc
 * Desc:
 */
public class RetryUtil {
    public static void retryRequest(int delayTime, RetryListener listener){
        Observable.timer(delayTime, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                if(listener!=null){
                    listener.retry();
                }
            }
        });
    }
}
