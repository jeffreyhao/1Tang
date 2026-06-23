package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by haojiangfeng on 2025/2/14.
 */
public class SubscribeReceiveBody {


    @SerializedName("unvalid")
    public List<SubscribeReceiveBean> unvalid;

    @SerializedName("valid")
    public List<SubscribeReceiveBean> valid;


}

