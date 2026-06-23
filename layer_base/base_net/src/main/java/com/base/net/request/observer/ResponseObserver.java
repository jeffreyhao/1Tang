package com.base.net.request.observer;

import android.net.Uri;
import android.text.TextUtils;

import com.base.api.Logger;
import com.base.net.ApiBase;
import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.bean.RequestInfo;
import com.base.net.callback.ResponseListener;
import com.base.net.callback.ResponseObserverCallback;
import com.base.net.request.RequestQueue;
import com.base.net.util.RequestUtil;

import java.util.LinkedHashMap;

import androidx.annotation.CallSuper;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by haojiangfeng on 2024/11/4.
 */
public class ResponseObserver implements Observer<Response<ResponseBody>> {

    /**
     * 请求序列
     */
    protected int index;
    /**
     *  接口path
     */
    protected String mApiPath;
    /**
     *  接口参数 map
     */
    protected LinkedHashMap<String,Object> mNetParamMap;
    /**
     *  用于缓存的参数 map
     */
    protected LinkedHashMap<String,Object> mCacheParamMap;

    protected @RequestInfo.FinishAction int mFinishAction;

    protected boolean showLoading;
    protected boolean customShowLoadingFinish;
    protected boolean mViewDestroyed;
    protected Disposable disposable;


    protected ResponseObserverCallback mBaseViewCallback;
    protected ResponseListener mResponseListener;

    protected int onNextCount = 0;


    public ResponseObserver(int index, String apiPath, boolean showLoading, boolean customShowLoadingFinish,
                            LinkedHashMap<String,Object> netParamMap,
                            LinkedHashMap<String, Object> cacheParamMap,
                            int finishAction,
                            ResponseObserverCallback baseViewCallback,
                            ResponseListener responseListener) {
        this.index = index;
        this.mApiPath = apiPath;
        this.showLoading = showLoading;
        this.customShowLoadingFinish = customShowLoadingFinish;
        this.mNetParamMap = netParamMap;
        this.mCacheParamMap = cacheParamMap;
        this.mFinishAction = finishAction;
        this.mViewDestroyed = false;
        this.mBaseViewCallback = baseViewCallback;
        this.mResponseListener = responseListener;
    }

    @CallSuper
    @Override
    public void onSubscribe(Disposable d) {
        log("onSubscribe()");
        this.disposable = disposable;
    }

    @Override
    public void onNext(Response<ResponseBody> responseBodyResponse) {
        onNextCount ++;
        log("onNext()");
    }

    @Override
    public void onComplete() {
        log("onComplete()");
    }

    @Override
    public void onError(Throwable e) {
        log("onError()");
    }

    public Disposable getDisposable(){
        return disposable;
    }

    public void setViewDestroyed(boolean viewDestroyed) {
        this.mViewDestroyed = viewDestroyed;
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////

    protected void log(String methodName){
        if(ApiBase.DEBUG){
//            Logger.debug("ResponseObserver",
//                    mApiPath + ", " + methodName
//                            + "\nthread:" + Thread.currentThread()
//                            + "\nthis:" + this
//            );
        }
    }

    protected String getApi(Response<ResponseBody> response){
        try {
            String url = response.raw().request().url().toString();
            Uri uri = Uri.parse(url);
            String path = uri.getPath();
            return path;
        } catch (Throwable e){
            Logger.exception(e);
            return null;
        }
    }

    protected void tokenLose(Response<ResponseBody> response){
        if(mBaseViewCallback != null){
            Logger.textSingle("ResponseObserver", "tokenLose", "Request(" + index  + ") tokenLose: " + getApi(response));
            mBaseViewCallback.tokenLose();
        }
    }

    protected void showLoading(boolean loading){
        if(mBaseViewCallback != null){
            mBaseViewCallback.showLoading(loading);
        }
    }


    protected void removeRequest(){
        if(mBaseViewCallback != null){
            String pageHash = String.valueOf(mBaseViewCallback.hashCode());
            RequestQueue.getInstance().removeRequest(mApiPath, mNetParamMap, pageHash);
        }
    }

    protected void addRequest(){
        if(mBaseViewCallback != null){
            String pageHash = String.valueOf(mBaseViewCallback.hashCode());
            RequestQueue.getInstance().addRequest(mApiPath, mNetParamMap, pageHash);
        }
    }



    protected void dealSuccessBody(boolean isCache, Response<ResponseBody> response){
        try {
            String content = response.body().string();

            if (!isCache && !TextUtils.isEmpty(content)) {
                RequestUtil.saveCache(mApiPath, mCacheParamMap, content);
            }

            onSuccess(isCache, content, response);

        } catch (Throwable e){
            onFail(isCache, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, "body is null"));
        }
    }

    protected void onSuccess(boolean isCache, String content, Response<ResponseBody> response){
        if(mResponseListener != null){
            Logger.textSingle("ResponseObserver", "onSuccess", "Request(" + index  + ") onSuccess: " + getApi(response) + ", isCache: " + isCache);
            mResponseListener.onSuccess(mApiPath, mNetParamMap, content, response, isCache);
        }
    }

    protected void onFail(boolean isCache, Response<ResponseBody> response){
        if(response != null){
            onFail(isCache, new ApiException(response.code(), response.message()));
        } else {
            onFail(isCache, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, "response is null"));
        }
    }


    protected void onFail(boolean isCache, ApiException apiException){
        if(mResponseListener != null){
            Logger.textSingle("ResponseObserver", "onFail","Request(" + index  + ") " + mApiPath + " onFail: " + exc(apiException) + ", isCache: " + isCache);
            mResponseListener.onFail(mApiPath, mNetParamMap, apiException, isCache);
        }
    }

    private String exc(ApiException ex){
        return ex == null ? "" : ex.getCode() + "-" + ex.getMessage();
    }

}