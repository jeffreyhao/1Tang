package com.baidu.baselibrary.request;

import androidx.lifecycle.Lifecycle;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

/**
 * @author: lhc
 * @date: 2020-03-19 21:50
 * @description 线程切换封装及生命周期管理
 */

public class MyObservableTransformer<T> implements ObservableTransformer<T,T> {
    private Subject<Lifecycle.Event> lifecycleEvent;
    public MyObservableTransformer(Subject<Lifecycle.Event> lifecycleEvent) {
        this.lifecycleEvent = lifecycleEvent;
    }
    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {

        Observable<T> observable = upstream.subscribeOn(Schedulers.io())
                                                .unsubscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread());
        if(lifecycleEvent!=null)
            return observable.takeUntil(lifecycleEvent);
        return observable;
    }
}