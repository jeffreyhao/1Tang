package com.base.net.interceptor;

import android.os.Build;
import android.text.TextUtils;

import com.base.api.PreferenceApi;
import com.base.api.VersionApi;
import com.base.net.ApiBase;
import com.base.util.AppUtil;
import com.base.util.DevicesUtil;
import com.base.util.content.StringUtils;
import com.base.util.net.NetworkUtils;
import com.base.util.safe.EncryptUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 公共参数拦截器
 */
public class CommonParamsInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        //添加公共参数
        String host = originalRequest.url().host();
        if (ApiBase.BASE_URL.contains(host)
                || ApiBase.SEARCH_URL.contains(host)
                || ApiBase.UPLOAD_TIME_URL.contains(host)) {
            //添加公共参数
            Map<String, String> commonParams = getPublicParameters();
            if(TextUtils.equals("POST",originalRequest.method())){
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody;
                if(originalRequest.body() instanceof FormBody){
                    formBody = (FormBody) originalRequest.body();
                    for(int i=0; i<formBody.size(); i++){
                        bodyBuilder.addEncoded(formBody.encodedName(i),formBody.encodedValue(i));
                    }
                }
                for(Map.Entry<String, String> entry : commonParams.entrySet()){
                    bodyBuilder.addEncoded(entry.getKey(),entry.getValue());
                }
                formBody = bodyBuilder.build();
                originalRequest = originalRequest.newBuilder().post(formBody).build();
            }
        }
        return chain.proceed(originalRequest);
    }

    /**
     * http请求公共参数
     *
     * @return 公共参数
     */
    public Map<String, String> getPublicParameters() {

        // app版本
        String version = AppUtil.getVersionName();
        // app包名
        String packageName = AppUtil.getPackageName();
        // 当前时间
//        long time = System.currentTimeMillis()/1000;
        String productId = ApiBase.PRODUCT_ID;
        // Integer	渠道
        String channelId = ApiBase.CHANNEL;
        String language = AppUtil.getLanguage();
        //设备语言
        String deviceLanguage = AppUtil.getDeviceLanguage();
        //国家编码
        String country = AppUtil.getCountryCode();
        //货币
        String currencyCode = ApiBase.currencyCode;
        // 平台 0=ios 1=android
        String platform = "1";
        String appType = "0";
        // 设备ID
        String deviceId = DevicesUtil.getDeviceId();
        // 设备ID
//        String androidId = DevicesUtil.getAndroidId();
        // 设备ID
//        String imei = DevicesUtil.getIMEI();
        // 设备ID
//        String meid = DevicesUtil.getMEID();
        // 设备ID
        String serialNo = DevicesUtil.getSerialNo();
        // 手机类型
        String phoneType = Build.BRAND;
        // 设备
        String sysModel = Build.MODEL;
        // 系统版本
        String sysVersion = Build.VERSION.RELEASE;
        // mac地址
//        String mac = DevicesUtil.getMac();
        // talkingdata的id
        String utid = DevicesUtil.getUTID();

        //simOperator
        String simOperator = DevicesUtil.getSimOperator();
        //dns
        String dns = DevicesUtil.getDNS();
        //netCountry
        String netCountry = DevicesUtil.getNetCountry();

        String newDeviceId = PreferenceApi.get("device_id","");
        if(TextUtils.isEmpty(newDeviceId)) {
            newDeviceId = ApiBase.newDeviceId;
        }

        String signSecretKey = "iboKwvO#xSCaev9G";
        String desKey = "xSCaev9G";
        String params = format("channel_id=%s&device_id=%s&package=%s&platform=%s&product_id=%s&version=%s",
                channelId, deviceId, packageName, platform, productId, version);

        params += signSecretKey;
        String waitSignParams = new String(params.getBytes(), StandardCharsets.UTF_8);
        String signParams = EncryptUtils.encryptHmacSHA256ToString(waitSignParams, signSecretKey);
        LinkedHashMap<String, String> paramArray = new LinkedHashMap<>();
        paramArray.put("version",version);
        paramArray.put("package",packageName);
        paramArray.put("product_id",productId);
        paramArray.put("channel_id",channelId);
        paramArray.put("platform",platform);
        paramArray.put("language",language);
        paramArray.put("device_language",deviceLanguage);
        paramArray.put("country",country);
        paramArray.put("local_timezone", AppUtil.getTimeZone());
        paramArray.put("currency",currencyCode);
        paramArray.put("device_id",deviceId);
        paramArray.put("serial_no",serialNo);
        paramArray.put("phone_type",phoneType);
        paramArray.put("simOperator",simOperator);
        paramArray.put("dns",dns);
        paramArray.put("netCountry",netCountry);
        paramArray.put("sys_model",sysModel);
        paramArray.put("sys_version",sysVersion);
        paramArray.put("app_type",appType);
        paramArray.put("group_id", ApiBase.GROUP_ID);
        paramArray.put("new_device_id",newDeviceId);
        paramArray.put("sign",signParams);
        return paramArray;
    }

    private static String format(String format, Object... args) {
        return new Formatter(Locale.getDefault()).format(format, args).toString();
    }


    /**
     * 获取 UA header
     *
     * @return 公共头Header
     */
    public static String getUAHeader() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api-client/1 ");

        String packageName = AppUtil.getPackageName();

        if (!StringUtils.isTrimEmpty(packageName)) {
            stringBuilder.append(packageName).append("/");
        } else {
            stringBuilder.append("com.fold.video/");
        }

        String appVersionName = VersionApi.getVersionName();
        if (StringUtils.isTrimEmpty(appVersionName)) {
            appVersionName = "1.0";
        }

        int appVersionCode = AppUtil.getVersionCode();
        if (appVersionCode == -1) {
            appVersionCode = 1;
        }

        boolean isTablet = DevicesUtil.isTabletDevice();

        String osVersion = DevicesUtil.getOSVersion();
        if (StringUtils.isTrimEmpty(osVersion)) {
            osVersion = "unknown";
        }

        String manufacturer = DevicesUtil.getManufacturer();
        if (StringUtils.isTrimEmpty(manufacturer)) {
            manufacturer = "unknown";
        }

        String model = DevicesUtil.getModel();
        if (StringUtils.isTrimEmpty(model)) {
            model = "unknown";
        }

        String netWorkType = NetworkUtils.getNetworkType().getType();
        if (StringUtils.isTrimEmpty(netWorkType)) {
            netWorkType = "unknown";
        }

        stringBuilder.append(appVersionName).append("(").append(appVersionCode).append(") ").append(isTablet ? "pad" : "phone").append("/");
        stringBuilder.append(osVersion).append(" ").append(manufacturer).append(" ").append(model).append(" network/").append(netWorkType);
        return stringBuilder.toString();
    }
}