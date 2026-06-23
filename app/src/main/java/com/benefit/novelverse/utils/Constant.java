package com.benefit.novelverse.utils;

import com.baidu.baselibrary.util.App;
import com.base.util.AppUtil;
import com.benefit.novelverse.R;

import java.io.File;
import java.util.Random;

/**
 * 常量类
 */
public class Constant {
    public static int NO_LOGIN_USER_ID = Integer.MAX_VALUE;
    public static String appChannel = "APP_CHANNEL";

    public static int getRandomRequestIntervalTime() {
        return 2 * 60 * 60 * 1000 + new Random().nextInt(3 * 60 * 60 * 1000);
//        return 60*1000 + new Random().nextInt(60*1000);
    }


    public static class WORK_ID {
        public static final String UPLOAD_READ_TIME = "upload_read_time";
        public static final String SNACKBAR_UPLOAD = "snackbar_upload";
        public static final String PULL_BOOK_SHELF = "pull_bookshelf";
        public static final String PULL_BOOK_UPDATE = "pull_book_update";

        public static final String REQUEST_USER_ID = "requestUserId";
        public static final String CHAPTER_DOWNLOAD = "ChapterDownload";
        public static final String FAIL_RECHARGE_ORDER = "FailRechargeOrder";
    }

    //sp
    public static class SP_CONSTANT {
        public static final String FIRST_ENTER_TIMESTAMP = "first_enter_timestamp";
        public static final String FIRST_ENTER_APP = "first_enter";
        public static final String IS_NEW_USER = "is_new_usr";
        /**
         * 开启推送
         */
        public static final String KEY_PUSH_ENABLE = "key_push_enable";
        /**
         * 应用版本号
         */
        public static final String KEY_VERSION_CODE = "key_version_code";
        /**
         * 是否展示新手引导
         */
        public static final String KEY_SHOW_NEW = "key_show_new";
        public static final String HAS_FETCH_INSTALL_REFERRER = "has_fetch_install_referrer";
        public static final String HAS_SET_INSTALL_REFERRER = "has_set_install_referrer";


        /**
         * 消息红点
         */
        public static final String KEY_PUSH_TYPE = "key_push_type";
        public static final String KEY_SHOW_DIALOG_AD = "show_dialog_ad";
        public static final String KEY_SERVER_ENV = "server_env";
        public static final String KEY_READ_TIME_PREFIX = "read_time_total_";
        public static final String FREE_BOOK_ID_FROM_CLIP = "clip_free_book";
        public static final String AD_ID = "adId";
        public static final String OPEN_DEEP_LINK = "open_deep_link_";
        public static final String SCREEN_SHOT_OPEN = "screen_shot_open";
        public static final String SCREEN_SHOT_COMPLETE_1 = "screen_shot_complete1";
        public static final String SCREEN_SHOT_COMPLETE_2 = "screen_shot_complete2";

        /**
         * 该字段用来解决没有正常退出阅读页的日志上报.
         * 1.进入阅读页，设置为true。
         * 2.退出阅读页时，上报日志，并设置为false。
         * 3.如果启动app时发现本地该字段为true，说明没有正常退出阅读页，那就上报alog日志。
         */
        public static final String NEED_UPLOAD_ALOG = "uploadAlog";

        public static final String IS_READING = "isReading";
        public static final String FACEBOOK_ID = "facebook_id";
        public static final String PIXEL_ID = "pixel_id";
        public static final String USER_IDENTITY_FLAG = "user_identity_flag";
        public static final String REPORT_FIRST_TIME_USER_INFO = "report_first_time_user_info-" + AppUtil.getVersionName();
        public static final String REPORT_FIRST_TIME_OPEN_REGISTER = "report_first_time_open_register-" + AppUtil.getVersionName();
        public static final String FIRST_TIME_OPEN_FROM_DEEPLINK = "first_time_open_from_deeplink";
        //标识用户是否第一次从googlePlay下载
        public static final String INSTALL_FROM_GOOGLE_PLAY = "install_from_google_play";
        public static final String HAS_UPLOAD_REGISTER_EVENT = "has_upload_register_event";
        public static final String MERGE_ACCOUNT = "mergeAccount";
        public static final String CAID_DATA = "caid_data";
        public static final String NEED_RECHARGE_LOGIN = "need_recharge_login";
        public static final String CAID_FROM_SERVER = "caid_from_server";
        public static final String THIRD_PARTY_INFO_POST_RESULT = "third_party_info_post_result";
        public static final String CHAPTER_SCROLL_GUIDE = "chapter_scroll_guide";
        public static final String THIRD_LOGIN_TYPES = "third_login_types";

        public static final String SELECT_PREFERENCE_TAG = "select_preference_tag";
        public static final String REFRESH_DATA_INTERVAL_TIME = "refresh_data_interval_time";//刷新数据时间间隔
        public static final String PREFERENCE_TAG_SKIP = "preference_tag_skip";
        public static final String CACHE_APP_VERSION = "cur_app_version";
        public static final String IS_NEW_INSTALL = "is_new_install";
        public static final String IS_NEW_INSTALL_REGISTER = "is_new_install_register";
        public static final String IS_UPGRADE_INSTALL = "is_upgrade_install";
        public static final String REPORT_USER_INFO_BACKTRACE = "report_user_info_backtrace";
        public static final String VERSION_CRASH_DING     = "version_name_crash_dingtalk";

        public static final String LIMIT_REST_MILLS     = "limit_timer_rest_mills";
        public static final String LIMIT_REST_ITEM     = "limit_timer_item";
    }

    // EventBus Code
    public static final class EVENT_CODE {
        public static final int LOGIN = 1;
        public static final int LOGOUT = 2;
        public static final int JUMP_TO_CHANNEL_TAB = 9;
        public static final int ISNIGHT = 10;
        public static final int ISADDBOOKSHELF = 11;
        public static final int REWARDMONTHTICKET_SUCCESS = 12;
        public static final int RELEASE_NOTE_SUCCESS = 13;
        public static final int RELEASE_NOTE_REWARD = 14;
        public static final int REQUEST_CHAPTER_CONTENT_SUCCESS = 15;
        public static final int READ_ACTIVITY_FINISH = 16;
        public static final int UPDATE_PREMIUM_STATUS = 17;
    }

    public static class INTETN_KEY {
        public static final String URL_KEY = "url";
        public static final String BOOK_ID = "book_id";
        public static final String RANK_ID = "rank_id";
        public static final String UNIT_KEY = "UNIT_KEY";
        public static final String APPINTRO_POSITION = "appintro_position";
        public static final String WEB_TITLE = "web_title";
        public static final String WEB_STATUS_COLOR_RES = "web_status_color";
        public static final String WEB_NAV_COLOR_RES = "web_nav_color";

    }

    public static class JS_ACTION {
        public static final String SHARE_NATIVE = "shareToNative";
        public static final String JUMP_TO_LOGIN = "jumpToLogin";
        public static final String TOOLBAR_BLACK_LIST = "toolbar_black_list";
        public static final String REQUEST_TOUCH_EVENT = "requestEvent";
        public static final String OPEN_APP = "openApp";
        public static final String DO_PAY = "doPay";
        public static final String SHOW_TOAST = "toast";
        public static final String CLOSE_ACTIVIYY = "close_activity";
        public static final String ADD_TO_BOOKSHELF = "add_to_bookshelf";
    }

    /**
     * 性别类型 1男 2女
     */
    public static class SEXTYPE {
        public static final int TYPE_UNKOWN = 0;
        public static final int TYPE_MAN = 1;
        public static final int TYPE_WOMAN = 2;
    }

    /**
     * 频道类型 1男频 2女频 3出版 4精选
     */
    public static class channelType {
        public static final int CHANNEL_MAN = 3;
        public static final int CHANNELE_WOMAN = 4;
        public static final int CHANNELE_CHOSEN = 2;
    }

    /**
     * 设备信息
     */
    public static class DeviceData {
        public static final String DEVICE_ID = "device_id";
        public static final String GA_ID = "ga_id";
        public static final String ANDROID_ID = "android_id";
    }

    //BOOK_ID Date Convert Format
    public static final String FORMAT_TIME = "HH:mm";

    //BookCachePath (因为getCachePath引用了Context，所以必须是静态变量，不能够是静态常量)

    public static class TimerCategory {
        public static String Recharge                   = "Recharge";


    }

    public static class TimerKey {

        public static final String Alog                 = "alog";
        public static final String AdTrack              = "AdTrack";
        public static final String Purchase             = "Purchase";
        public static final String RepairOrders         = "RepairOrders";

    }
}
