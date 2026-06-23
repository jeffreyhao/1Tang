package com.base.net.request;


import com.base.api.GlobalContext;
import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.bean.CacheEnum;
import com.base.net.bean.RequestInfo;
import com.base.net.bean.RequestType;
import com.base.net.callback.ResponseListener;
import com.base.net.callback.ResponseObserverCallback;
import com.base.net.request.observer.ObserverCache;
import com.base.net.request.observer.ObserverNetwork;
import com.base.net.request.observer.ResponseObserver;
import com.base.net.util.RequestLog;
import com.base.net.util.RequestUtil;
import com.base.util.collection.ListUtil;
import com.base.util.net.NetworkUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class BaseRequest {

    public static int index = 0;

    public static int nextIndex(){
        if(index == Integer.MAX_VALUE){
            index = 1;
        } else {
            index ++;
        }
        return index;
    }

    private final List<ResponseObserver> netSubscribes = new ArrayList<>();

    /**
     * IBaseView
     */
    @Nullable
    protected ResponseObserverCallback mCallback = null;


    public BaseRequest() {
    }

    public BaseRequest(@Nullable ResponseObserverCallback callback) {
        mCallback = callback;
    }

    public void setCallback(ResponseObserverCallback callback) {
        mCallback = callback;
    }


    public void disposeAllSubscribe() {
        if (ListUtil.isNotEmpty(netSubscribes)) {
            for (int i = 0; i < netSubscribes.size(); i++) {
                if (netSubscribes.get(i).getDisposable() != null) {
                    netSubscribes.get(i).getDisposable().dispose();
                }
            }
        }
    }

    public void notifyViewDestroyed() {
        if (ListUtil.isNotEmpty(netSubscribes)) {
            for (int i = 0; i < netSubscribes.size(); i++) {
                netSubscribes.get(i).setViewDestroyed(true);
            }
        }
    }

    public static OkHttpClient getClient() {
        return RetrofitUtils.getInstance().getClient();
    }


    public void loadCacheOnly(String path, LinkedHashMap<String, Object> cacheParamMap, boolean loading, ResponseListener netListener) {
        load(CacheEnum.CACHE_ONLY, path, cacheParamMap, cacheParamMap, loading,  netListener, null);
    }

    public void loadNetOnly(String path, LinkedHashMap<String, Object> netParamMap, boolean loading, ResponseListener netListener) {
        load(CacheEnum.NET_ONLY, path, netParamMap, netParamMap, loading,null, netListener);
    }

    public void loadCacheElseNet(String path, LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap, boolean loading, ResponseListener netListener) {
        load(CacheEnum.CACHE_ELSE_NET, path, cacheParamMap, netParamMap, loading, netListener, netListener);
    }

    public void loadNetElseCache(String path, LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap, boolean loading, ResponseListener netListener) {
        load(CacheEnum.NET_ELSE_CACHE, path, cacheParamMap, netParamMap, loading, netListener, netListener);
    }

    public void loadCacheAndNet(String path, LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap, boolean loading, ResponseListener cacheListener, ResponseListener netListener) {
        load(CacheEnum.CACHE_AND_NET, path, cacheParamMap, netParamMap, loading,  cacheListener, netListener);
    }


    /**
     * 发起请求
     *
     * @param cacheEnum     缓存策略
     * @param path          url-path
     * @param cacheParamMap 加载缓存用的 paramMap
     * @param netParamMap   请求网络用的 paramMap
     * @param loading       loading?
     * @param cacheListener 缓存回调
     * @param netListener   网络请求回调
     */
    public void load(CacheEnum cacheEnum, String path,
                     LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap,
                     boolean loading,
                     ResponseListener cacheListener, ResponseListener netListener) {

        doRequest(RequestType.POST, "", cacheEnum, path, cacheParamMap, netParamMap, loading, false, cacheListener, netListener);
    }

    public void load(CacheEnum cacheEnum, String path,
                     LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap,
                     boolean loading, boolean customShowLoadingFinish,
                     ResponseListener cacheListener, ResponseListener netListener) {

        doRequest(RequestType.POST, "", cacheEnum, path, cacheParamMap, netParamMap, loading, customShowLoadingFinish, cacheListener, netListener);
    }

    public void getRequest(CacheEnum cacheEnum, String path, String authToken,
                     LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap,
                     boolean loading,
                     ResponseListener cacheListener, ResponseListener netListener) {

        doRequest(RequestType.GET, authToken, cacheEnum, path, cacheParamMap, netParamMap, loading, false, cacheListener, netListener);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////


    private void doRequest(@RequestType String requestType, @Nullable String authToken, CacheEnum cacheEnum, String path,
                           LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap,
                           boolean loading, boolean customShowLoadingFinish,
                           ResponseListener cacheListener, ResponseListener netListener) {
        int index = nextIndex();
        RequestInfo cacheRequestInfo = new RequestInfo.Builder(path)
                .setIndex(index)
                .setParam(cacheParamMap)
                .setShowLoading(loading)
                .setFinishAction(RequestInfo.FinishAction.ACTION_NORMAL)
                .setResponseListener(cacheListener)
                .create();
        RequestInfo netRequestInfo = new RequestInfo.Builder(path)
                .setIndex(index)
                .setParam(netParamMap)
                .setShowLoading(loading)
                .setFinishAction(cacheEnum == CacheEnum.NET_ELSE_CACHE ? RequestInfo.FinishAction.ACTION_LOAD_CACHE : RequestInfo.FinishAction.ACTION_NORMAL)
                .setSaveCache(true)
                .setResponseListener(netListener)
                .create();
        doRequest(requestType, authToken, cacheEnum, cacheRequestInfo, netRequestInfo);
    }

    /**
     * 发起请求
     *
     * @param cacheEnum     缓存策略
     * @param cacheRequest  缓存请求
     * @param netRequest    网络请求
     */
    private void doRequest(@RequestType String requestType, @Nullable String authToken, CacheEnum cacheEnum, RequestInfo cacheRequest, RequestInfo netRequest) {
        switch (cacheEnum){
            case CACHE_ONLY:
                loadCache(requestType, authToken, cacheRequest);
                break;
            case NET_ONLY:
                loadOnline(requestType, authToken, cacheRequest, netRequest);
                break;
            case CACHE_ELSE_NET:
                loadCacheElseNet(requestType, authToken, cacheRequest, netRequest);
                break;
            case NET_ELSE_CACHE:
                loadNetElseCache(requestType, authToken, cacheRequest, netRequest);
                break;
            case CACHE_AND_NET:
                loadCacheAndOnline(requestType, authToken, cacheRequest, netRequest);
                break;
        }
    }

    /**
     * 只加载缓存
     *
     * @return cacheObservable 缓存请求观察对象
     */
    @Nullable
    private Observable<Response<ResponseBody>> loadCache(@RequestType String requestType, @Nullable String authToken, RequestInfo cacheRequest){
        Observable<Response<ResponseBody>> cacheObservable = RequestUtil.newObservableFromCache(cacheRequest.path, cacheRequest.paramMap);
        if(cacheObservable != null){
            cacheObservable
                    .compose(new LifecycleTransformer<>(mCallback == null ? null : mCallback.getLifecycleEvent()))
                    .subscribe(new ObserverCache(cacheRequest.index, cacheRequest.path, cacheRequest.paramMap, cacheRequest.needShowLoading, cacheRequest.customShowLoadingFinish, cacheRequest.finishAction, mCallback, cacheRequest.responseListener));
        }
        return cacheObservable;
    }

    /**
     *  只请求网络
     */
    private void loadOnline(@RequestType String requestType, @Nullable String authToken, RequestInfo cacheRequest, RequestInfo netRequest){
        if(!NetworkUtil.isNetAvailable(GlobalContext.getContext())){
            if(netRequest.responseListener != null){
                netRequest.responseListener.onFail(netRequest.path, netRequest.paramMap, new ApiException(ApiErrorCode.LOCAL_NET_AVAILABLE, "Net Available."), false);
            }
            return;
        }
        if(canAddRequest(netRequest, mCallback)){
            Observable<Response<ResponseBody>> netObservable = RequestUtil.newObservableFromNet(requestType, netRequest.path, authToken, netRequest.paramMap);
            ObserverNetwork subscribe;
            netObservable
                    .compose(new LifecycleTransformer<>(mCallback == null ? null : mCallback.getLifecycleEvent()))
                    .subscribe(subscribe = new ObserverNetwork(netRequest.index, netRequest.path, netRequest.paramMap, cacheRequest.paramMap, netRequest.needShowLoading, netRequest.customShowLoadingFinish, netRequest.finishAction, mCallback, netRequest.responseListener));
            netSubscribes.add(subscribe);
        } else {
            if(netRequest.responseListener != null){
                netRequest.responseListener.onCancel(netRequest.path, netRequest.paramMap);
            }
        }
    }

    /**
     *  先走缓存，如果无缓存再走网络。
     */
    private void loadCacheElseNet(@RequestType String requestType, @Nullable String authToken, RequestInfo cacheRequest, RequestInfo netRequest){
        Observable<Response<ResponseBody>> cacheObservable = loadCache(requestType, authToken, cacheRequest);
        if(cacheObservable != null){
            return;  // cache有效，则不走net
        }
        loadOnline(requestType, authToken, cacheRequest, netRequest);
    }

    /**
     * 先请求网络，请求失败时尝试加载缓存
     */
    private void loadNetElseCache(@RequestType String requestType, @Nullable String authToken, RequestInfo cacheRequest, RequestInfo netRequest){
        if(!NetworkUtil.isNetAvailable(GlobalContext.getContext())){
            Observable<Response<ResponseBody>> cacheObservable = loadCache(requestType, authToken, cacheRequest);
            if(cacheObservable == null && netRequest.responseListener != null){  // 无网，并且无缓存
                netRequest.responseListener.onFail(netRequest.path, netRequest.paramMap, new ApiException(ApiErrorCode.LOCAL_NET_AVAILABLE, "Net Available."), false);
            }
            return;
        }
        if(canAddRequest(netRequest, mCallback)){
            Observable<Response<ResponseBody>> netObservable = RequestUtil.newObservableFromNet(requestType, netRequest.path, authToken, netRequest.paramMap);
            ObserverNetwork subscribe;
            netObservable
                    .compose(new LifecycleTransformer<>(mCallback == null ? null : mCallback.getLifecycleEvent()))
                    .subscribe(subscribe = new ObserverNetwork(netRequest.index, netRequest.path, netRequest.paramMap, cacheRequest.paramMap, netRequest.needShowLoading, netRequest.customShowLoadingFinish, netRequest.finishAction, mCallback, netRequest.responseListener));
            netSubscribes.add(subscribe);
        } else {
            if(netRequest.responseListener != null){
                netRequest.responseListener.onCancel(netRequest.path, netRequest.paramMap);
            }
        }
    }

    /**
     * 先加载缓存，同时请求网络
     */
    private void loadCacheAndOnline(@RequestType String requestType, @Nullable String authToken, RequestInfo cacheRequest, RequestInfo netRequest){
        Observable<Response<ResponseBody>> cacheObservable = RequestUtil.newObservableFromCache(cacheRequest.path, cacheRequest.paramMap);

        if(!NetworkUtil.isNetAvailable(GlobalContext.getContext())){
            if(netRequest.responseListener != null){
                if(cacheObservable == null){    // 无网，并且无缓存
                    netRequest.responseListener.onFail(netRequest.path, netRequest.paramMap, new ApiException(ApiErrorCode.LOCAL_NET_AVAILABLE, "Net Available."), false);
                } else {                        // 无网，有缓存。先走缓存，再走无网络回调
                    cacheObservable
                            .compose(new LifecycleTransformer<>(mCallback == null ? null : mCallback.getLifecycleEvent()))
                            .subscribe(new ObserverCache(cacheRequest.index, cacheRequest.path, cacheRequest.paramMap, cacheRequest.needShowLoading, cacheRequest.customShowLoadingFinish, RequestInfo.FinishAction.ACTION_NET_FAIL, mCallback, cacheRequest.responseListener));
                }
            }
            return;
        }
        if(!canAddRequest(netRequest, mCallback)){    // 如果是重复请求，缓存也不要走了
            if(netRequest.responseListener != null){
                netRequest.responseListener.onCancel(netRequest.path, netRequest.paramMap);
            }
            return ;
        }

        if(cacheObservable != null){
            ObserverCache cacheSubscribe = new ObserverCache(cacheRequest.index, cacheRequest.path, cacheRequest.paramMap, cacheRequest.needShowLoading, cacheRequest.customShowLoadingFinish, cacheRequest.finishAction, mCallback, cacheRequest.responseListener);
            cacheObservable
                    .compose(new LifecycleTransformer<Response<ResponseBody>>(mCallback == null ? null : mCallback.getLifecycleEvent()))
                    .subscribe(cacheSubscribe);
        }

        Observable<Response<ResponseBody>> netObservable = RequestUtil.newObservableFromNet(requestType, netRequest.path, authToken, netRequest.paramMap);
        ObserverNetwork netSubscribe;
        netObservable.compose(new LifecycleTransformer<Response<ResponseBody>>(mCallback == null ? null : mCallback.getLifecycleEvent()))
                .subscribe(netSubscribe = new ObserverNetwork(netRequest.index, netRequest.path, netRequest.paramMap, cacheRequest.paramMap,
                        cacheRequest.needShowLoading && (cacheObservable == null),
                        netRequest.customShowLoadingFinish,
                        netRequest.finishAction,
                        mCallback,
                        netRequest.responseListener));
        netSubscribes.add(netSubscribe);

        if(cacheObservable != null){
            // 先加载缓存，后加载网络
            Observable<Response<ResponseBody>> concatObservable = Observable.concat(cacheObservable, netObservable);
//            concatObservable.subscribe();
        }
    }

    private boolean canAddRequest(RequestInfo request, ResponseObserverCallback callback) {
        String pageHash = callback == null ? "" : String.valueOf(callback.hashCode());
        boolean canRequest = RequestQueue.getInstance().addRequest(request.path, request.paramMap, pageHash);
        RequestLog.logRequest(request.path, request.paramMap, request.index, canRequest);
        return canRequest;
    }



}
