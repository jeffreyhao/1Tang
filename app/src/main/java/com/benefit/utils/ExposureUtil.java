package com.benefit.utils;

import android.text.TextUtils;
import android.view.View;

import com.baidu.baselibrary.log.ALog;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.core.business.exposure.SAExposureConfig;
import com.sensorsdata.analytics.android.sdk.core.business.exposure.SAExposureData;

import org.json.JSONObject;

/**
 * Time: 2024/6/27
 * Author: lhc
 * Desc:
 */
public class ExposureUtil {
    public static void bookExpose(View view, String pageName, String moduleName, String moduleId, String bookId, int position) {
        try{
            if(view==null|| TextUtils.isEmpty(bookId)) return;
            SAExposureConfig exposureConfig = new SAExposureConfig(0.5f, 0.1, false);
            JSONObject properties = new JSONObject();
            properties.put("page_name", pageName);
            properties.put("item_id", bookId);
            properties.put("book_module", moduleName);
            properties.put("book_module_id", moduleId);
            properties.put("book_position", position);
            SAExposureData exposureData = new SAExposureData("book_exposure", properties, bookId, exposureConfig);
            SensorsDataAPI.sharedInstance().addExposureView(view, exposureData);
        }catch(Exception e) {
            e.printStackTrace();
            ALog.e("ExposureUtil-->bookExpose-->exception: " + e.getMessage());
        }
    }
}
