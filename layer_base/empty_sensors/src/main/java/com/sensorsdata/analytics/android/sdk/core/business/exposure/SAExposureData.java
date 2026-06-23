package com.sensorsdata.analytics.android.sdk.core.business.exposure;

import org.json.JSONObject;

/**
 * Created by haojiangfeng on 2024/9/19.
 */
public class SAExposureData {


    public SAExposureData(String event) {
        this(event, null, null, null);
    }

    public SAExposureData(String event, JSONObject properties) {
        this(event, properties, null, null);
    }

    public SAExposureData(String event, String exposureIdentifier) {
        this(event, null, exposureIdentifier, null);
    }

    public SAExposureData(String event, JSONObject properties, String exposureIdentifier) {
        this(event, properties, exposureIdentifier, null);
    }

    public SAExposureData(String event, JSONObject properties, SAExposureConfig exposureConfig) {
        this(event, properties, null, exposureConfig);
    }


    public SAExposureData(String event, JSONObject properties, String exposureIdentifier, SAExposureConfig exposureConfig) {

    }



}
