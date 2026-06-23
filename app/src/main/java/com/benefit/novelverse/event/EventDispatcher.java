package com.benefit.novelverse.event;


import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * 事件分发处理中心
 */
public class EventDispatcher implements LifecycleEventObserver {

    private final EventBus mEventBus;
    private static EventDispatcher sInstance;

    private EventDispatcher(EventBus eventBus) {
        mEventBus = eventBus;
    }

    public static EventDispatcher get() {
        if (sInstance == null) {
            sInstance = new EventDispatcher(EventBus.getDefault());
        }
        return sInstance;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void register(Object subscriber) {
        if (subscriber != null && !mEventBus.isRegistered(subscriber)) {
            mEventBus.register(subscriber);
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void unregister( Object subscriber) {
        if (subscriber != null&&mEventBus.isRegistered(subscriber)) {
            mEventBus.unregister(subscriber);
        }
    }

    public <T>void post( Event<T> event) {
        if (event != null) {
            mEventBus.post(event);
        }
    }
    public void post( Object event) {
        if (event != null) {
            mEventBus.post(event);
        }
    }

    public void cancelEventDelivery(Object event){
        mEventBus.cancelEventDelivery(event);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

    }
}
