package com.baidu.baselibrary.base;


import com.base.net.bean.CacheEnum;
import com.base.net.callback.ResponseListener;
import com.base.net.request.BaseRequest;

import java.util.LinkedHashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
* @author lhc
* @date 2022/5/9 9:01
* @desc Presenter 基类
*/

public class BasePresenter<V extends IView> implements LifecycleObserver {

    protected V mView;

    protected BaseRequest mRequest;



    public void attachView(@NonNull V view){
        this.mView = view;
        if(mRequest == null){
            mRequest = new BaseRequest(view);
        } else {
            mRequest.setCallback(view);
        }
    }

    public BasePresenter() {
        super();
        this.mRequest = new BaseRequest(null);
    }

    public BasePresenter(V view) {
        super();
        this.mView = view;
        this.mRequest = new BaseRequest(view);
    }


    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        this.mView = null;
//        mRequest.disposeAllSubscribe();
        mRequest.notifyViewDestroyed();
    }


    protected BaseRequest getBaseRequest(){
        return mRequest;
    }


    protected void post(String path, LinkedHashMap<String, Object> paramMap, ResponseListener listener) {
        loadNetOnly(path, paramMap, false, listener);
    }

    protected void post(String path, LinkedHashMap<String, Object> paramMap, boolean loading, ResponseListener listener) {
        loadNetOnly(path, paramMap, loading, listener);
    }

    protected void post(String path, ResponseListener listener) {
        loadNetOnly(path, listener);
    }

    protected void postWithLoading(String path, LinkedHashMap<String, Object> paramMap, ResponseListener listener) {
        loadNetOnly(path, paramMap, true, listener);
    }

    protected void postWithLoading(String path, ResponseListener listener) {
        loadNetOnly(path, null, true, listener);
    }




    protected void loadCacheOnly(String path, LinkedHashMap<String, Object> paramMap, boolean loading,
                                 ResponseListener listener) {
        mRequest.loadCacheOnly(path, paramMap, loading, listener);
    }

    protected void loadNetOnly(String path, ResponseListener listener) {
        mRequest.loadNetOnly(path, null, false, listener);
    }

    protected void loadNetOnly(String path, LinkedHashMap<String, Object> paramMap, boolean loading,
                               ResponseListener listener) {
        mRequest.loadNetOnly(path, paramMap, loading, listener);
    }

    protected void loadCacheElseNet(String path, LinkedHashMap<String, Object> paramMap, boolean loading,
                                    ResponseListener listener) {
        mRequest.loadCacheElseNet(path, paramMap, paramMap, loading, listener);
    }

    protected void loadCacheElseNet(String path, LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap, boolean loading,
                                    ResponseListener listener) {
        mRequest.loadCacheElseNet(path, cacheParamMap, netParamMap, loading, listener);
    }

    protected void loadCacheAndNet(String path, LinkedHashMap<String, Object> paramMap, boolean loading,
                                   ResponseListener cacheListener,
                                   ResponseListener netListener) {
        mRequest.loadCacheAndNet(path, paramMap, paramMap, loading, cacheListener, netListener);
    }

    protected void loadCacheAndNet(String path, LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap, boolean loading,
                                   ResponseListener cacheListener,
                                   ResponseListener netListener) {
        mRequest.loadCacheAndNet(path, cacheParamMap, netParamMap, loading, cacheListener, netListener);
    }

    protected void loadNetElseCache(String path, LinkedHashMap<String, Object> paramMap, boolean loading,
                                    ResponseListener listener) {
        mRequest.loadNetElseCache(path, paramMap, paramMap, loading, listener);
    }


    protected void load(CacheEnum cacheEnum, String path, LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap, boolean loading,
                        ResponseListener cacheListener,
                        ResponseListener netListener){
        mRequest.load(cacheEnum, path, cacheParamMap, netParamMap, loading, cacheListener, netListener);
    }


}
