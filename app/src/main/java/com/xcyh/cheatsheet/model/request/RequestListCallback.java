package com.xcyh.cheatsheet.model.request;

import android.text.TextUtils;

import com.baidu.baselibrary.util.clz.ClassUtil;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.callback.ResponseListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.Keep;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by haojiangfeng on 2024/12/12.
 *
 * @param <D>
 */
@Keep
public abstract class RequestListCallback<D> implements ResponseListener {


    @Override
    public void onSuccess(String apiPath, Map<String, Object> paramMap, String content, Response<ResponseBody> response, boolean isCache) {
        try {
            RequestResult<List<D>> result = RequestResult.formatList(content, "body", ClassUtil.getListClassType(this));
            if(result != null && result.getCode() == 0) {
                LogUtil.d("adv", "Response.onSuccess\n      " + paramMap.toString() + "\n       " + content);
                onSuccess(content, result.getData());
            } else {
                onFail(apiPath, paramMap, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, "parse json error"), isCache);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            String errMsg = TextUtils.isEmpty(e.getMessage())
                    ? ((response == null || TextUtils.isEmpty(response.message())) ? "response is empty" : response.message())
                    : e.getMessage();
            onFail(apiPath, paramMap, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, errMsg), isCache);
        }
    }

    @Override
    public void onFail(String apiPath, Map<String,Object> paramMap, ApiException e, boolean isCache) {
        LogUtil.e("RequestCallback", "onFail, " + apiPath  + (e == null ? "" : ", " + e.getMsg()));
        onFail(e);
    }


    @Override
    public void onCancel(String apiPath, Map<String, Object> paramMap) {

    }

    public abstract void onSuccess(String content, List<D> t);

    public abstract void onFail(ApiException e);
}