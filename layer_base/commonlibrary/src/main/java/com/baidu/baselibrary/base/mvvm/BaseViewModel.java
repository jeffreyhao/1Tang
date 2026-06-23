package com.baidu.baselibrary.base.mvvm;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.callback.ResponseObserverCallback;
import com.jess.baselibrary.R;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class BaseViewModel extends ViewModel implements ResponseObserverCallback {

    private final MutableLiveData<UiState> uiState = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> tokenLoseEvent = new MutableLiveData<>();
    private final Subject<Lifecycle.Event> mLifecycleSubject = PublishSubject.create();
    protected final BaseRepository mRepository = new BaseRepository(this);

    // --- LiveData getters ---

    public final LiveData<UiState> getUiState() {
        return uiState;
    }

    public final LiveData<Event<Boolean>> getTokenLoseEvent() {
        return tokenLoseEvent;
    }

    // --- UI state operations ---

    protected void showLoading() {
        uiState.setValue(UiState.loading());
    }

    protected void hideLoading() {
        uiState.setValue(UiState.idle());
    }

    protected void showSuccess() {
        uiState.setValue(UiState.success());
    }

    protected void showEmptyView(int textResId, int iconResId) {
        uiState.setValue(UiState.empty(textResId, iconResId));
    }

    protected void showErrorView(int textResId, int iconResId) {
        uiState.setValue(UiState.error(textResId, iconResId));
    }

    public void showNoData() {
        uiState.setValue(UiState.empty(R.string.error_empty_data_tip, R.drawable.default_no_data));
    }

    public void showErrorForException(ApiException e) {
        if (e != null && e.getCode() == ApiErrorCode.LOCAL_NET_AVAILABLE) {
            showErrorView(R.string.error_net_tip, R.drawable.default_no_net);
        } else {
            showErrorView(R.string.error_data_tip, R.drawable.default_load_fail);
        }
    }

    // --- ResponseObserverCallback implementation ---

    @Override
    public void showLoading(boolean show) {
        uiState.setValue(show ? UiState.loading() : UiState.idle());
    }

    @Override
    public void tokenLose() {
        tokenLoseEvent.setValue(new Event<>(true));
    }

    @Override
    public Subject<Lifecycle.Event> getLifecycleEvent() {
        return mLifecycleSubject;
    }

    // --- Lifecycle forwarding (called by Activity) ---

    public void onLifecycleEvent(Lifecycle.Event event) {
        mLifecycleSubject.onNext(event);
    }

    // --- Cleanup ---

    @Override
    protected void onCleared() {
        super.onCleared();
        mLifecycleSubject.onComplete();
    }
}
