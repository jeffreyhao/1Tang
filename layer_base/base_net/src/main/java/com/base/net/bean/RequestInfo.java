package com.base.net.bean;

import com.base.net.callback.ResponseListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedHashMap;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

/**
 * Created by haojiangfeng on 2024/11/11.
 */
public class RequestInfo {



    @IntDef({
            FinishAction.ACTION_NORMAL,
            FinishAction.ACTION_LOAD_CACHE,
            FinishAction.ACTION_NET_FAIL,
            FinishAction.ACTION_NET_CANCEL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface FinishAction {

        /** 缺省值：正常结束 **/
        int ACTION_NORMAL           = 0;

        /** 需要在网络请求结束时，加载缓存 **/
        int ACTION_LOAD_CACHE       = 1;

        /** 需要在缓存加载结束时，调用无网络回调 **/
        int ACTION_NET_FAIL         = 2;

        /** 需要在缓存加载结束时，调用取消回调 **/
        int ACTION_NET_CANCEL       = 3;
    }


    public int index;

    /**
     *  接口path
     */
    @NonNull
    public String path = "";

    /**
     *  接口参数
     */
    public LinkedHashMap<String, Object> paramMap;

    /**
     * 需要保存缓存
     */
    public boolean needSaveCache    = true;

    /**
     * 需要展示loading框
     */
    public boolean needShowLoading  = false;

    /**
     * 自主展示loading结束
     */
    public boolean customShowLoadingFinish = false;

    public @FinishAction int finishAction;


    /**
     * 回调
     */
    public ResponseListener responseListener;



    public RequestInfo(int index,
            @NonNull String path, LinkedHashMap<String, Object> param,
                       boolean showLoading,
                       boolean customShowLoadingFinish,
                       boolean saveCache,
                       int finishAction,
                       ResponseListener responseListener){

        this.index = index;
        this.path = path;
        this.needShowLoading = showLoading;
        this.customShowLoadingFinish = customShowLoadingFinish;
        this.needSaveCache = saveCache;
        this.finishAction = finishAction;
        this.responseListener = responseListener;

        if(param == null){
            paramMap = new LinkedHashMap<>();
        } else {
            paramMap = param;
        }
    }



    public static class Builder {
        private int index = -1;
        @NonNull
        private String path;
        private LinkedHashMap<String, Object> param = null;
        private boolean showLoading = false;
        private boolean saveCache = true;
        private boolean customShowLoadingFinish = false;
        public @FinishAction int finishAction;
        private ResponseListener responseListener;


        public Builder(@NonNull String path){
            this.path = path;
        }


        public Builder setIndex(int index) {
            this.index = index;
            return this;
        }

        public Builder setPath(@NonNull String path) {
            this.path = path;
            return this;
        }

        public Builder setParam(LinkedHashMap<String, Object> param) {
            this.param = param;
            return this;
        }

        public Builder setShowLoading(boolean showLoading) {
            this.showLoading = showLoading;
            return this;
        }

        public Builder setCustomShowLoadingFinish(boolean customShowLoadingFinish) {
            this.customShowLoadingFinish = customShowLoadingFinish;
            return this;
        }

        public Builder setSaveCache(boolean saveCache) {
            this.saveCache = saveCache;
            return this;
        }

        public Builder setFinishAction(@FinishAction int action) {
            this.finishAction = action;
            return this;
        }

        public Builder setResponseListener(ResponseListener responseListener) {
            this.responseListener = responseListener;
            return this;
        }

        public RequestInfo create() {
            return new RequestInfo(index, path, param, showLoading, customShowLoadingFinish, saveCache, finishAction, responseListener);
        }
    }
}
