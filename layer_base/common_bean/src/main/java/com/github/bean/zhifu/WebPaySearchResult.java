package com.github.bean.zhifu;

import com.google.gson.annotations.SerializedName;

/**
 * Created by niepan on 2/6/26.
 */
public class WebPaySearchResult {


    /**
     * `status` int NOT NULL DEFAULT '0' COMMENT '充值状态；
     *
     *  0-待支付,1-已完成,2-支付失败,3-已退款,4-支付取消,5-支付处理中,7-已关闭,8-支付成功,9-已冻结,10-已解冻',
     */
    @SerializedName("status")
    public int status;

}
