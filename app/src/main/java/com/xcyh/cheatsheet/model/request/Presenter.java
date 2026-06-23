package com.xcyh.cheatsheet.model.request;

import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.base.IBaseView;
import com.base.net.bean.CacheEnum;
import com.base.net.callback.ResponseListener;
import com.xcyh.cheatsheet.model.bean.Pitcher;

import java.util.LinkedHashMap;


/**
 * Created by haojiangfeng on 2024/12/12.
 */
public class Presenter<V extends IBaseView> extends BasePresenter<V> {



    protected void get(String path, ResponseListener listener) {
        get(path, null, false, listener);
    }

    protected void get(String path, LinkedHashMap<String, Object> paramMap, ResponseListener listener) {
        get(path, paramMap, false, listener);
    }

    protected void get(String path, LinkedHashMap<String, Object> paramMap, boolean loading, ResponseListener listener) {
        String authCode = "Bearer " + Pitcher.getInstance().getToken();
        mRequest.getRequest(CacheEnum.NET_ONLY, path, authCode, paramMap, paramMap, loading, null,  listener);
    }




}
