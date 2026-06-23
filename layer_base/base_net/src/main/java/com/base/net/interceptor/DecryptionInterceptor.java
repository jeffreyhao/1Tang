package com.base.net.interceptor;

import android.text.TextUtils;
import com.base.util.xyz.Dec;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DecryptionInterceptor implements Interceptor {
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        
        // 执行请求获取原始响应
        Response originalResponse = chain.proceed(chain.request());
        String encryptRespHeader = originalResponse.header("Encrypt-Resp");
        if(TextUtils.equals("1", encryptRespHeader)) {
            // 检查响应体是否为空
            if (originalResponse.body() == null) {
                return originalResponse;
            }
            // 读取加密的响应数据
            byte[] encryptedData = originalResponse.body().bytes();

            // 解密数据
            byte[] decryptedData = decrypt(encryptedData);

            // 创建新的响应体
            ResponseBody decryptedBody = ResponseBody.create(
                    decryptedData,
                    originalResponse.body().contentType()
            );

            // 返回新的响应
            return originalResponse.newBuilder()
                    .body(decryptedBody)
                    .build();
        }
        return originalResponse;
    }

    private byte[] decrypt(byte[] encryptedData) {
        return Dec.rrccres(encryptedData);
    }
}