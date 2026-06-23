package com.base.net.request.observer;

import android.text.TextUtils;

import com.base.api.Logger;
import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.bean.RequestInfo;
import com.base.net.cache.CacheControlManager;
import com.base.net.callback.ResponseListener;
import com.base.net.callback.ResponseObserverCallback;
import com.base.net.util.RequestUtil;

import java.util.LinkedHashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 *  接口返回数据统一管理处理类
 */
public class ObserverNetwork extends ResponseObserver {



    public ObserverNetwork(int index,
                           String apiPath,
                           LinkedHashMap<String,Object> netParamMap,
                           LinkedHashMap<String, Object> cacheParamMap,
                           boolean showLoading, boolean customShowLoadingFinish,
                           int finishAction,
                           ResponseObserverCallback responseObserverCallback,
                           ResponseListener networkResponseListener) {
        super(index, apiPath, showLoading, customShowLoadingFinish, netParamMap, cacheParamMap, finishAction, responseObserverCallback, networkResponseListener);
    }


    @Override
    public void onError(Throwable e) {
        super.onError(e);
        Logger.textSingle("ObserverNetwork", "onError", "onError->" + e.getMessage());
        Logger.exception(e);

        if (showLoading && !mViewDestroyed && !customShowLoadingFinish){
            showLoading(false);
        }
        if (!mViewDestroyed){
            onFail(false, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, e.getMessage()));
        }
        removeRequest();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        if (showLoading && !mViewDestroyed && !customShowLoadingFinish){
            showLoading(false);
        }
        removeRequest();
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        super.onSubscribe(disposable);
        this.disposable = disposable;
        if (showLoading && !mViewDestroyed){
            showLoading(true);
        }
        addRequest();
    }

    @Override
    public void onNext(Response<ResponseBody> response) {
        super.onNext(response);
        if(mViewDestroyed) {
            Logger.textSingle("ObserverNetwork", "onNext", "Request(" + index  + ") " + getApi(response) + "-->" + mBaseViewCallback + " destroyed");
            return;
        }
        if (response.isSuccessful()) {
            dealSuccessBody(false, response);
        } else if (response.code() == 401) {
            tokenLose(response);
        } else if(mFinishAction == RequestInfo.FinishAction.ACTION_LOAD_CACHE){
            tryLoadCache(response);
        } else {
            onFail(false, response);
        }
    }

    public Disposable getDisposable() {
        return this.disposable;
    }




    private void tryLoadCache(Response<ResponseBody> netResponse){
        String cacheData = CacheControlManager.getInstance().getCache(mApiPath, mCacheParamMap);
        if (TextUtils.isEmpty(cacheData)) {
            onFail(false, netResponse);
        } else {
            Response<ResponseBody> cacheResponse = RequestUtil.newResponse(mApiPath, cacheData);
            dealSuccessBody(true, cacheResponse);
        }
    }


}
