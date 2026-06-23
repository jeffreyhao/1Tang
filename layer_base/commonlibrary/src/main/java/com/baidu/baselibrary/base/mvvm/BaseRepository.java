package com.baidu.baselibrary.base.mvvm;

import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.bean.CacheEnum;
import com.base.net.callback.ResponseListener;
import com.base.net.callback.ResponseObserverCallback;
import com.base.net.request.BaseRequest;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class BaseRepository {

    protected final BaseRequest mRequest;

    public BaseRepository(ResponseObserverCallback callback) {
        mRequest = new BaseRequest(callback);
    }

    public void notifyViewDestroyed() {
        mRequest.notifyViewDestroyed();
    }

    // --- DataCallback methods (typed parsing) ---

    public <T> void postAndParse(String path, LinkedHashMap<String, Object> paramMap, boolean loading, Class<T> clazz, DataCallback<T> callback) {
        mRequest.loadNetOnly(path, paramMap, loading, wrapListener(clazz, callback));
    }

    public <T> void postAndParse(String path, LinkedHashMap<String, Object> paramMap, boolean loading, Type type, DataCallback<T> callback) {
        mRequest.loadNetOnly(path, paramMap, loading, wrapListener(type, callback));
    }

    public <T> void loadCacheElseNetAndParse(String path, LinkedHashMap<String, Object> paramMap, boolean loading, Class<T> clazz, DataCallback<T> callback) {
        mRequest.loadCacheElseNet(path, paramMap, paramMap, loading, wrapListener(clazz, callback));
    }

    private <T> ResponseListener wrapListener(Class<T> clazz, DataCallback<T> callback) {
        return new ResponseListener() {
            @Override
            public void onSuccess(String apiPath, Map<String, Object> paramMap, String content, Response<ResponseBody> response, boolean isCache) {
                try {
                    T data = new Gson().fromJson(content, clazz);
                    callback.onSuccess(data);
                } catch (Exception e) {
                    callback.onFail(new ApiException(ApiErrorCode.LOCAL_PARSE_BODY_ERROR, e.getMessage()));
                }
            }

            @Override
            public void onFail(String apiPath, Map<String, Object> paramMap, ApiException e, boolean isCache) {
                callback.onFail(e);
            }

            @Override
            public void onCancel(String apiPath, Map<String, Object> paramMap) {
            }
        };
    }

    private <T> ResponseListener wrapListener(Type type, DataCallback<T> callback) {
        return new ResponseListener() {
            @Override
            public void onSuccess(String apiPath, Map<String, Object> paramMap, String content, Response<ResponseBody> response, boolean isCache) {
                try {
                    T data = new Gson().fromJson(content, type);
                    callback.onSuccess(data);
                } catch (Exception e) {
                    callback.onFail(new ApiException(ApiErrorCode.LOCAL_PARSE_BODY_ERROR, e.getMessage()));
                }
            }

            @Override
            public void onFail(String apiPath, Map<String, Object> paramMap, ApiException e, boolean isCache) {
                callback.onFail(e);
            }

            @Override
            public void onCancel(String apiPath, Map<String, Object> paramMap) {
            }
        };
    }

    // --- Raw callback methods ---

    public void post(String path, LinkedHashMap<String, Object> paramMap, ResponseListener listener) {
        mRequest.loadNetOnly(path, paramMap, false, listener);
    }

    public void post(String path, LinkedHashMap<String, Object> paramMap, boolean loading, ResponseListener listener) {
        mRequest.loadNetOnly(path, paramMap, loading, listener);
    }

    public void post(String path, ResponseListener listener) {
        mRequest.loadNetOnly(path, null, false, listener);
    }

    public void postWithLoading(String path, LinkedHashMap<String, Object> paramMap, ResponseListener listener) {
        mRequest.loadNetOnly(path, paramMap, true, listener);
    }

    public void postWithLoading(String path, ResponseListener listener) {
        mRequest.loadNetOnly(path, null, true, listener);
    }

    public void loadCacheOnly(String path, LinkedHashMap<String, Object> paramMap, boolean loading, ResponseListener listener) {
        mRequest.loadCacheOnly(path, paramMap, loading, listener);
    }

    public void loadNetOnly(String path, LinkedHashMap<String, Object> paramMap, boolean loading, ResponseListener listener) {
        mRequest.loadNetOnly(path, paramMap, loading, listener);
    }

    public void loadNetOnly(String path, ResponseListener listener) {
        mRequest.loadNetOnly(path, null, false, listener);
    }

    public void loadCacheElseNet(String path, LinkedHashMap<String, Object> paramMap, boolean loading, ResponseListener listener) {
        mRequest.loadCacheElseNet(path, paramMap, paramMap, loading, listener);
    }

    public void loadCacheElseNet(String path, LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap, boolean loading, ResponseListener listener) {
        mRequest.loadCacheElseNet(path, cacheParamMap, netParamMap, loading, listener);
    }

    public void loadNetElseCache(String path, LinkedHashMap<String, Object> paramMap, boolean loading, ResponseListener listener) {
        mRequest.loadNetElseCache(path, paramMap, paramMap, loading, listener);
    }

    public void loadCacheAndNet(String path, LinkedHashMap<String, Object> paramMap, boolean loading, ResponseListener cacheListener, ResponseListener netListener) {
        mRequest.loadCacheAndNet(path, paramMap, paramMap, loading, cacheListener, netListener);
    }

    public void loadCacheAndNet(String path, LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap, boolean loading, ResponseListener cacheListener, ResponseListener netListener) {
        mRequest.loadCacheAndNet(path, cacheParamMap, netParamMap, loading, cacheListener, netListener);
    }

    public void load(CacheEnum cacheEnum, String path, LinkedHashMap<String, Object> cacheParamMap, LinkedHashMap<String, Object> netParamMap, boolean loading, ResponseListener cacheListener, ResponseListener netListener) {
        mRequest.load(cacheEnum, path, cacheParamMap, netParamMap, loading, cacheListener, netListener);
    }
}
