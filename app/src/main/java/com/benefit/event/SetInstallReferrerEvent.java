package com.benefit.event;

import org.json.JSONObject;

/**
 * Created by haojiangfeng on 2023/12/15.
 */
public class SetInstallReferrerEvent {

    public JSONObject jsonObject;

    public SetInstallReferrerEvent(JSONObject jsonObject){
        this.jsonObject =  jsonObject;
    }

}
