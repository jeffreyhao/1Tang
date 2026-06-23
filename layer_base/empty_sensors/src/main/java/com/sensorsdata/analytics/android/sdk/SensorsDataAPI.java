package com.sensorsdata.analytics.android.sdk;

import android.content.Context;
import android.view.View;

import com.sensorsdata.analytics.android.sdk.core.business.exposure.SAExposureData;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by haojiangfeng on 2024/9/19.
 */
public class SensorsDataAPI {

    private static SensorsDataAPI instance = new SensorsDataAPI();

    /**
     * 初始化神策 SDK
     *
     * @param context App 的 Context
     * @param saConfigOptions SDK 的配置项
     */
    public static void startWithConfigOptions(Context context, SAConfigOptions saConfigOptions) {

    }


    public static SensorsDataAPI sharedInstance() {
        return instance;
    }


    /**
     * 曝光 view 标记
     *
     * @param view 被标记的 view
     * @param exposureData 曝光配置
     */
    public void addExposureView(View view, SAExposureData exposureData) {
    }

    public void trackFragmentAppViewScreen(){

    }

    public void ignoreAutoTrackActivities(List<Class<?>> activitiesList) {

    }

    public void enableAutoTrackFragments(List<Class<?>> fragmentsList) {

    }

    public void registerSuperProperties(final JSONObject superProperties) {

    }


    public JSONObject getLastScreenTrackProperties() {
        return new JSONObject();
    }

    public void profileSet(final JSONObject properties) {

    }

    public void profileSet(final String property, final Object value) {

    }

    public void login(final String loginId) {

    }

    public void login(final String loginId, final JSONObject properties) {

    }

    public void identify(final String distinctId) {

    }

    public void track(final String eventName) {

    }

    public void track(final String eventName, final JSONObject properties) {

    }


    public String trackTimerStart(String eventName) {
        return "";
    }

    public void trackTimerEnd(final String eventName, final JSONObject properties) {

    }

}
