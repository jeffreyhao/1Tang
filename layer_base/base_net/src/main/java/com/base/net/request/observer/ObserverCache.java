package com.base.net.request.observer;

import com.base.api.Logger;
import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.bean.RequestInfo;
import com.base.net.callback.ResponseListener;
import com.base.net.callback.ResponseObserverCallback;

import java.util.LinkedHashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 *  接口返回数据统一管理处理类
 */
public class ObserverCache extends ResponseObserver {


    public ObserverCache(int index, String apiPath, LinkedHashMap<String, Object> cacheParamMap,
                         boolean showLoading,
                         boolean customShowLoadingFinish,
                         int finishAction,
                         ResponseObserverCallback responseObserverCallback,
                         ResponseListener cacheResponseListener) {
        super(index, apiPath, showLoading, customShowLoadingFinish, null, cacheParamMap, finishAction, responseObserverCallback, cacheResponseListener);
    }



    @Override
    public void onError(Throwable e) {
        super.onError(e);
        Logger.textSingle("ObserverCache", "onError", "Request(" + index  + ")onError->" + e.getMessage());
        Logger.exception(e);

        if (showLoading && !mViewDestroyed && !customShowLoadingFinish){
            showLoading(false);
        }
        if (!mViewDestroyed){
            onFail(true, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, e.getMessage()));

            if(mFinishAction == RequestInfo.FinishAction.ACTION_NET_FAIL){
                onFail(false, new ApiException(ApiErrorCode.LOCAL_NET_AVAILABLE, "Net Available."));
            }
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        if (showLoading && !mViewDestroyed && !customShowLoadingFinish){
            showLoading(false);
        }

        if(mFinishAction == RequestInfo.FinishAction.ACTION_NET_FAIL){
            onFail(false, new ApiException(ApiErrorCode.LOCAL_NET_AVAILABLE, "Net Available."));
        }
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        super.onSubscribe(disposable);
        if (showLoading && !mViewDestroyed){
            showLoading(true);
        }
    }


    @Override
    public void onNext(Response<ResponseBody> response) {
        super.onNext(response);
        if(mViewDestroyed) {
            Logger.textSingle("ResponseObserver", "onNext", "Request(" + index  + ") " + getApi(response) + "-->" + mBaseViewCallback + " destroyed");
            return;
        }
        if (response.isSuccessful()) {
            dealSuccessBody(true, response);
        } else {
            onFail(true, response);
        }
    }



}
