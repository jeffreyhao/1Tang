package com.xcyh.cheatsheet.main.login;

import com.baidu.baselibrary.base.BasePresenter;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.base.global.PreferencesUtil;
import com.base.net.bean.ApiErrorCode;
import com.base.net.bean.ApiException;
import com.base.net.callback.ResponseListener;
import com.xcyh.cheatsheet.model.bean.LoginBean;
import com.xcyh.cheatsheet.model.constant.SpKey;
import com.xcyh.cheatsheet.model.bean.Pitcher;
import com.xcyh.cheatsheet.model.request.RequestResult;
import com.xcyh.cheatsheet.model.constant.Url;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by haojiangfeng on 2024/11/12.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {


    public void doLogin(String userName, String password) {
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("username", userName);
        paramMap.put("password", password);

        ResponseListener responseListener = new ResponseListener(){

            @Override
            public void onSuccess(String apiPath, Map<String, Object> paramMap, String content, Response<ResponseBody> response, boolean isCache) {
                try {
                    RequestResult<LoginBean> result = RequestResult.formatBody(content, "data", LoginBean.class);
                    if(result != null && result.getCode() == 0 && result.getData() != null) {
                        saveLoginInfo(content, result.getData());
                        mView.onRequestSuccess(result.getData());
                    } else {
                        onFail(apiPath, paramMap, new ApiException(ApiErrorCode.LOCAL_UNKNOWN_ERROR, "parse json error"), isCache);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    mView.onRequestFail(new ApiException(0, "登录解析失败"));
                }
            }

            @Override
            public void onFail(String apiPath, Map<String, Object> paramMap, ApiException e, boolean isCache) {
                mView.onRequestFail(new ApiException(0, "登录失败"));
            }

            @Override
            public void onCancel(String apiPath, Map<String, Object> paramMap) {
                LogUtil.e("login", "onCancel");
            }
        };

        post(Url.API_LOGIN, paramMap, false, responseListener);
    }


    private void saveLoginInfo(String content, LoginBean loginBean){
        Pitcher.getInstance().setData(loginBean);
        PreferencesUtil.put(SpKey.USER_LOGIN, content);
        PreferencesUtil.put(SpKey.TIME_LOGIN, System.currentTimeMillis());
        PreferencesUtil.put(SpKey.BASE_URL, Url.BASE_URL);
    }
}
