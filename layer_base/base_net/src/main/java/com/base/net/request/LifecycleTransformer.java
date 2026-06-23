package com.base.net.request;

import androidx.lifecycle.Lifecycle;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;


/**
 * 线程切换封装及生命周期管理
 * @param <T> bean
 */
public class LifecycleTransformer<T> implements ObservableTransformer<T, T> {

    private final Subject<Lifecycle.Event> lifecycleEvent;

    public LifecycleTransformer(Subject<Lifecycle.Event> lifecycleEvent) {
        this.lifecycleEvent = lifecycleEvent;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        Observable<T> observable = upstream.subscribeOn(Schedulers.io())
                                                .unsubscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread());
        if(lifecycleEvent != null){
            return observable.takeUntil(lifecycleEvent);
        } else {
            return observable;
        }
    }
}