package com.base.net.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.base.api.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 *  用于生成curl命令
 */
public class CurlInterceptor implements Interceptor {

    private final String mTag;

    public CurlInterceptor(){
        mTag = "CURL";
    }

    public CurlInterceptor(String tag){
        if(TextUtils.isEmpty(tag)){
            mTag = "CURL";
        } else {
            mTag = tag;
        }
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String curl = buildCurlCommand(request);
        printCurl(curl);

//        long timeStamp = System.currentTimeMillis();
//        Log.w("DDD", "intercept: proceed.before, timeStamp = " + timeStamp + "\n" + curl);


        // 继续执行请求
        Response result = chain.proceed(request);

//        long interval = System.currentTimeMillis() - timeStamp;
//        Log.e("EEE", "onResponse: proceed.after, interval = " + interval + "\n" + curl);

        return result;
    }

    /**
     *  开始构建curl命令
     */
    private String buildCurlCommand(Request request){
        StringBuilder curlCommand = new StringBuilder(mTag + "\n  curl ");
        try {

            // 添加URL
            curlCommand.append("-X ").append(request.method()).append(" ");
            curlCommand.append("\"").append(request.url()).append("\"");

            // 添加Headers
            for (String headerName : request.headers().names()) {
                curlCommand.append(" -H \"").append(headerName).append(": ").append(request.headers().get(headerName)).append("\"");
            }

            // 如果是POST请求，添加body
            if ("POST".equals(request.method())) {
                RequestBody body = request.body();
                if (body != null) {
                    Buffer buffer = new Buffer();
                    body.writeTo(buffer);
                    curlCommand.append(" -d '").append(buffer.readUtf8()).append("&_debug=").append("-1'");
                }
            }
        } catch (Throwable e){
            e.printStackTrace();
        }
        return curlCommand.toString();
    }

    /**
     *  打印curl命令
     */
    private void printCurl(String curl){
        Logger.v("curl", curl);
    }


}