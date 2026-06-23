package com.base.net.callback;

import androidx.lifecycle.Lifecycle;
import io.reactivex.subjects.Subject;

/**
 * Created by haojiangfeng on 2024/10/29.
 */
public interface ResponseObserverCallback {

    void tokenLose();

    void showLoading(boolean show);

    Subject<Lifecycle.Event> getLifecycleEvent();

}
