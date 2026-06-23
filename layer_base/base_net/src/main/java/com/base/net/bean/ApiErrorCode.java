package com.base.net.bean;

public class ApiErrorCode {


/////////////////////////////////////////   本地错误码   /////////////////////////////////////////////


    public static final int LOCAL_UNKNOWN_ERROR         = -10000;
    public static final int LOCAL_PARSE_BODY_ERROR      = -10001;
    public static final int LOCAL_PARSE_NO_CODE         = -10002;
    public static final int LOCAL_NET_AVAILABLE         = -10003;
    public static final int LOCAL_SOCKET_TIME_OUT       = -10004;



/////////////////////////////////////////  业务接口错误码  ////////////////////////////////////////////


    public static final int API_UNKNOWN_ERROR           = 1;        // 未知错误
    public static final int API_INVALIDATE_PARAM        = 2;        // 无效参数
    public static final int CHAPTER_UNLOCK_USED         = 9;        // 广告解锁已使用

    public static final int REACH_MAX_TOKEN_INVALID     = 4003;     // ReachMax-token过期


    public static final int API_SIGN_FAIL               = 10004;    // 验签失败
    public static final int API_DEVICE_FAIL             = 10006;    // check device failed
    public static final int ACCOUNT_DELETED             = 10009;    // 账号删除
    public static final int LOGIN_NOT_ALLOWED_BIND      = 10010;    // 新三方登录不允许绑定提示
    public static final int LOGIN_MERGE_ASSETS_CODE     = 10011;    // 新三方登录游客合并账号资产提示

    public static final int LOGIN_EMAIL_CODE_EXPIRE     = 10012;
    public static final int LOGIN_EMAIL_CODE_ERROR      = 10013;
    public static final int ORDER_NO_EXISTS             = 20018;    // 订单不存在

    public static final int NO_BOOK                     = 20020;
    public static final int NO_CHAPTER                  = 20021;

    public static final int REQUEST_PAY_FAIL            = 20025;    // 请求支付端执行支付失败
    public static final int REPAIR_EXTEND_COUNT         = 20026;    // 补单超出限定次数
    public static final int NO_MATCH_TOKEN_PROD         = 20027;    // Token与产品ID不匹配
    public static final int RECHARGE_PENDING            = 20029;    // Pending状态



/////////////////////////////////////////      utils     ////////////////////////////////////////////


    /**
     * 1      未知错误
     * 2      无效参数
     * 10004  验签失败
     * 20018  订单不存在
     * 20025  请求支付端执行支付失败
     * 20026  补单超出限定次数
     * 20027  token与产品ID不匹配
     * 20029  pending状态
     * 1000   response null
     */
    public static boolean needSaveFailOrder(int code){
        return code==1||code==20025||code==1000||code==20029 || isServiceError(code) || isParseError(code);
    }

    public static boolean isInnerApiError(int code){
        return inArray(
                API_UNKNOWN_ERROR,
                API_SIGN_FAIL,
                ORDER_NO_EXISTS,
                REQUEST_PAY_FAIL,
                REPAIR_EXTEND_COUNT,
                NO_MATCH_TOKEN_PROD
        );
    }

    public static boolean isCDNError(int code){
        return code != 200;
    }

    /**
     * @return 是否是服务器报错
     */
    public static boolean isServiceError(int code) {
        return code >= 500 && code <= 505;
    }

    public static boolean isParseError(int code) {
        return code == ApiErrorCode.LOCAL_PARSE_BODY_ERROR;
    }


    public static boolean inArray(int code, int... array){
        if(array == null){
            return false;
        }
        for(int item: array){
            if(code == item){
                return true;
            }
        }
        return false;
    }


    public static boolean isNoBook(ApiException e) {
        if(e == null) {
            return false;
        }
        return e.getCode() == NO_BOOK;
    }


}
