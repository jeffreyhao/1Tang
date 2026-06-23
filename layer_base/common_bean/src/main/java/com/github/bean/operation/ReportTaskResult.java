package com.github.bean.operation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by haojiangfeng on 2025/3/14.
 */
public class ReportTaskResult {


    @SerializedName("advertise_status")
    public int advertise_status;

    /**
     *
     */
    @SerializedName("advertise_message")
    public String advertise_message;


    public boolean isSuccess(){
        return advertise_status == 0;
    }

    public int code(){
        return advertise_status;
    }

    public String msg(){
        return advertise_message;
    }
}
