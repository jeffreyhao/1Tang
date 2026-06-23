package com.baidu.baselibrary.log.annotate;

/**
 * ALog三级标签命名规范：
 *      一级标签，命名使用全大写；
 *      二级标签，大写开头，后面驼峰和下划线命名；
 *      三级标签，小写开头，后面驼峰和下划线命名；
 */
public class LogTag {


    ////////////////////////////////////   一级标签   /////////////////////////////////////

    public static final String PAY              = "PAY";
    public static final String GOOGLE_PAY       = "GOOGLE_PAY";     // 谷歌支付
    public static final String DEEPLINK         = "DEEPLINK";

    public static final String ACTION           = "ACTION";         // 行为
    public static final String RESULT           = "RESULT";
    public static final String CRASH            = "CRASH";          // 走到Uncaught异常回调的崩溃
    public static final String EXCEPTION        = "EXCEPTION";      // 已catch到的异常
    public static final String ERROR            = "ERROR";          // 一些错误日志
    public static final String SINGLE_LINE      = "SINGLE_LINE";    // 单行的文本日志
    public static final String TEXT             = "TEXT";           // 文本内容
    public static final String JSON             = "JSON";           // 文本内容
    public static final String LIFECYCLE        = "LIFECYCLE";      // 生命周期
    public static final String ORDER            = "ORDER";          // 订单
    public static final String REQUEST          = "REQUEST";        // 请求
    public static final String SWITCH           = "SWITCH";         // 各种类型的开关
    public static final String FACEBOOK         = "FACEBOOK";       // facebook相关
    public static final String FIREBASE         = "FIREBASE";
    public static final String STACKTRACE       = "STACKTRACE";
    public static final String ADMOB            = "ADMOB";

    public static final String PRESENTER        = "PRESENTER";      // Presenter
    public static final String READ             = "READ";
    public static final String LOGIN            = "LOGIN";
    public static final String SENSORS          = "SENSORS";

    public static final String DOMAIN           = "DOMAIN";

    public static final String OLD_ALOG         = "OLD_ALOG";

    ////////////////////////////////////   一级标签   /////////////////////////////////////





    ////////////////////////////////////   二级标签   /////////////////////////////////////
    public static final String Application              = "Application";
    public static final String AppProgress              = "AppProgress";
    public static final String Fragment                 = "Fragment";
    public static final String Activity                 = "Activity";
    public static final String Dialog                   = "Activity";
    public static final String Action                   = "Action";
    public static final String Result                   = "Result";
    public static final String State                    = "State";
    public static final String Error                    = "Error";


    public static final String Deeplink_Content         = "Deeplink_Content";


    public static final String Pay_Fail_Repair          = "Pay_Fail_Repair";
    public static final String GooglePay_Connect        = "Connect";
    public static final String GooglePay_QueryPurchase  = "QueryPurchase";
    public static final String GooglePay_QuerySku       = "QuerySku";
    public static final String GooglePay_ProductDetails = "ProductDetails";
    public static final String GooglePay_Success        = "GooglePay_Success";
    public static final String GooglePay_Result         = "GooglePayResult";

    public static final String GooglePay_PurchasesUpdated= "OnPurchasesUpdated";


    public static final String GooglePay_Purchase       = "GooglePay_Purchase";

    public static final String Order_Create             = "Create";
    public static final String Order_RechargeV6         = "RechargeV6";

    public static final String Order_Web_Create         = "Order_Web_Create";
    public static final String Select_Payment           = "Select_Payment";
    public static final String Select_PayMethod         = "Select_PayMethod";


    public static final String RequestTime              = "RequestTime";
    public static final String RequestResult            = "RequestResult";

    public static final String Switch_Dns               = "Dns";
    public static final String Switch_Network           = "NetChange";
    public static final String Switch_Looper            = "MainLooper";

    public static final String Front_Background         = "Front_Background";

    public static final String Action_Click             = "Click";          // 点击事件
    public static final String Action_Slide             = "Slide";          // 滑动事件
    public static final String Action_Tick_Run          = "TimerTask_Run";       // 计时器运行task事件

    public static final String Presenter_FbTack         = "FbTack";

    public static final String Presenter_TrackBatch     = "TrackBatch";
    public static final String Presenter_LaunchReport   = "LaunchReport";
    public static final String Presenter_OpenReport     = "Presenter_OpenReport";
    public static final String Presenter_TrackLog       = "Presenter_TrackLog";
    public static final String Presenter_TrackAdmob     = "Presenter_TrackAdmob";

    public static final String Presenter_TrackSdk       = "TrackSdk";

    public static final String Presenter_PostGuest      = "PostGuest";
    public static final String Presenter_BookStore      = "BookStore";

    public static final String Presenter_ReportTask     = "ReportTask";
    public static final String Presenter_GetTaskData    = "GetTaskData";
    public static final String Presenter_RechargeFail   = "RechargeFail";
    public static final String Presenter_PurchaseChapter= "PurchaseChapter";

    public static final String Presenter_CheckNeedPay   = "CheckNeedPay";
    public static final String Presenter_ChapterInfo    = "ChapterInfo";
    public static final String Presenter_FreeChapter    = "FreeChapter";
    public static final String Presenter_GetTaskDataV3  = "GetTaskDataV3";


    public static final String Read_SaveInstance        = "SaveInstance";
    public static final String Read_RestoreInstance     = "RestoreInstance";
    public static final String Read_Error               = "ReadError";

    public static final String Read_Record              = "ReadRecord";

    public static final String Read_NewIntent           = "NewIntent";
    public static final String Read_Action              = "Read_Action";
    public static final String Read_Exit                = "Read_Exit";
    public static final String Read_PageChange          = "Read_PageChange";
    public static final String Read_ChapterChange       = "Read_ChapterChange";
    public static final String Read_GetDetail           = "Read_GetDetail";
    public static final String Read_OnStop              = "Read_OnStop";
    public static final String Read_Scroll              = "Read_Scroll";
    public static final String Read_Notify              = "Read_Notify";
    public static final String Read_NotifyItem          = "Read_NotifyItem";

    public static final String LoginDispatch            = "LoginDispatch";
    public static final String LogoutDispatch           = "LogoutDispatch";

    public static final String LoginResult              = "LoginResult";


    public static final String FirebaseMsg              = "FirebaseMsg";
    public static final String Sensors_Install          = "Sensors_Install";

    public static final String On_Paid                  = "On_Paid";

    ////////////////////////////////////   二级标签   /////////////////////////////////////





    ////////////////////////////////////   三级标签   /////////////////////////////////////


    ////////////////////////////////////   三级标签   /////////////////////////////////////


}
