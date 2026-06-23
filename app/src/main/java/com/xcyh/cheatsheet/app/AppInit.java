package com.xcyh.cheatsheet.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.baidu.baselibrary.timer.ObservableSecondTimer;
import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.util.device.IpUtils;
import com.baidu.baselibrary.util.glide.GlideUtils;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.baidu.baselibrary.widget.CustomRefreshFooter;
import com.base.global.GlobalBuildConfig;
import com.base.global.PreferencesUtil;
import com.base.net.ApiBase;
import com.base.net.request.RequestConfig;
import com.base.net.request.RetrofitUtils;
import com.base.net.state.NetChangeObserver;
import com.base.watcher.Watcher;
import com.base.watcher.WatcherEvent;
import com.benefit.interceptor.AccountDeletedInterceptor;
import com.benefit.interceptor.OkHttpEventListener;
import com.benefit.interceptor.RequestRetryInterceptor;
import com.benefit.novelverse.BuildConfig;
import com.benefit.novelverse.R;
import com.benefit.novelverse.utils.Constant;
import com.benefit.utils.DomainUtil;
import com.github.bean.database.AppDatabase;
import com.meta.ui.widget.RippleResource;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.DirectModelNotifier;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.common.ActivityStackManager;
import com.tencent.common.util.Abase;
import com.xcyh.cheatsheet.app.callback.AppActivityLifecycleCallback;
import com.xcyh.cheatsheet.model.constant.Url;

import java.util.concurrent.TimeUnit;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by haojiangfeng on 2023/11/29.
 */
public class AppInit {

    /******************************   public api  *********************************/

    /**
     *  初始化 BuildConfig 参数
     */
    public static void initBuildConfig(Context context) {
        GlobalBuildConfig.APPLICATION_ID = BuildConfig.APPLICATION_ID;
        GlobalBuildConfig.BUILD_TYPE = BuildConfig.BUILD_TYPE;
        GlobalBuildConfig.FLAVOR = BuildConfig.FLAVOR;
        GlobalBuildConfig.CHANNEL = BuildConfig.CHANNEL;
        GlobalBuildConfig.PRODUCT_ID = BuildConfig.PRODUCT_ID;
        GlobalBuildConfig.ROUTER_ID = getMetaDataRouter(context);

        GlobalBuildConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        GlobalBuildConfig.VERSION_CODE = BuildConfig.VERSION_CODE;

        GlobalBuildConfig.DEBUG = BuildConfig.DEBUG;
        GlobalBuildConfig.LOG_DEBUG = true;
        GlobalBuildConfig.SENSORS_LOG = true;
        GlobalBuildConfig.CRASH_DEFEND = false;
        GlobalBuildConfig.EXIT_LIMIT = BuildConfig.EXIT_LIMIT;


        GlobalBuildConfig.BOOK_STORE_PAGE = BuildConfig.BOOK_STORE_PAGE;

        GlobalBuildConfig.BASE_LANDING = BuildConfig.BASE_LANDING;
        GlobalBuildConfig.BASE_URL = BuildConfig.BASE_URL;
        GlobalBuildConfig.SEARCH_URL = BuildConfig.SEARCH_URL;
        GlobalBuildConfig.UPLOAD_TIME_URL = BuildConfig.UPLOAD_TIME_URL;

        GlobalBuildConfig.WEB_ENDPOINT = BuildConfig.WEB_ENDPOINT;
        GlobalBuildConfig.WEB_ENDPOINT_PREFIX = BuildConfig.WEB_ENDPOINT_PREFIX;
        GlobalBuildConfig.GROUP_ID = BuildConfig.GROUP_ID;

        Uri uri = Uri.parse(GlobalBuildConfig.BASE_URL);
        GlobalBuildConfig.BASE_HOST = uri.getHost();

        Uri searchUri = Uri.parse(GlobalBuildConfig.SEARCH_URL);
        GlobalBuildConfig.SEARCH_HOST = searchUri.getHost();

        Uri uploadTimeUri = Uri.parse(GlobalBuildConfig.UPLOAD_TIME_URL);
        GlobalBuildConfig.UPLOAD_TIME_HOST = uploadTimeUri.getHost();

        GlobalBuildConfig.showDebugLog();

        ApiBase.DEBUG = GlobalBuildConfig.DEBUG;
        ApiBase.BASE_HOST = GlobalBuildConfig.BASE_HOST;
        ApiBase.BASE_URL = GlobalBuildConfig.BASE_URL;
        ApiBase.SEARCH_URL = GlobalBuildConfig.SEARCH_URL;
        ApiBase.UPLOAD_TIME_URL = GlobalBuildConfig.UPLOAD_TIME_URL;
        ApiBase.GROUP_ID = GlobalBuildConfig.GROUP_ID;
        ApiBase.CHANNEL = GlobalBuildConfig.CHANNEL;
        ApiBase.PRODUCT_ID = GlobalBuildConfig.PRODUCT_ID;
        ApiBase.FLAVOR = GlobalBuildConfig.FLAVOR;

        Url.BASE_URL = GlobalBuildConfig.BASE_URL;
    }


    /**
     * {@link FoApplication#onCreate()}
     */
    public static void initOnApplicationCreate(Application application){
        AppInit.initALog(application);
        AppInit.initAppConfig();
//        AppInit.initDefenceCrash(); //异常控制
        AppInit.addRequestInterceptor();
        ActivityStackManager.getInstance().init(application);
        AppInit.initDbFlow();
        application.registerActivityLifecycleCallbacks(new AppActivityLifecycleCallback());
        AppInit.addRxJavaErrorHandler();
        AppInit.getNetworkInfo();
        AppInit.registerNetChangeListener();
        AppInit.initSmartRefreshLayout();
        AppInit.initRippleResource();
        AppInit.initRequestConfig();
        ALog.textSingle("initOnApplicationCreate()-->end");
    }

    private static void initRequestConfig() {
        RequestConfig.Builder builder = new RequestConfig.Builder();
        builder.configConnectionPool(10, 5, TimeUnit.MINUTES)
                .configConnectTimeout(30, TimeUnit.SECONDS)
                .configReadTimeout(15, TimeUnit.SECONDS)
                .configWriteTimeout(15, TimeUnit.SECONDS);
        RetrofitUtils.getInstance().init(builder.build());
    }

    private static void initRippleResource() {
        RippleResource.init();
    }

    /**
     * 延迟初始化的操作
     * @param application
     */
    public static void initOnApplicationCreatePost(Application application){
        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(PreferencesUtil.get(Constant.SP_CONSTANT.NEED_UPLOAD_ALOG, false)){
                    // 上报日志
                }
            }
        }, 5000); // 5s后的初始化
    }

    public static void initOnMainTabCreate(){
        ObservableSecondTimer.getInstance().start();

    }

    public static void callOnMainTabDestroy(){
        ObservableSecondTimer.getInstance().stop();
        App.clearOnMainExit();
    }

    /**
     * {@link FoApplication#onTerminate()}
     */
    public static void onTerminate() {
        App.getHandler().removeCallbacksAndMessages(null);
    }


    private static void getNetworkInfo() {
        IpUtils.getNetIp();
    }

    private static void initALog(Context context) {
        LogUtil.setDebug(GlobalBuildConfig.LOG_DEBUG);

        ALog.init(context);

        // 新增 ArrayList 格式化器，默认已支持 Array, Throwable, Bundle, Intent 的格式化输出 .addFormatter<ArrayList<*>> { list -> "ALog Formatter ArrayList { $list }" }
        // setFilePrefix()，如有修改，需要注意⚠️ 日志文件上报是以缺省值为 yyyy-MM-dd.txt
    }



    /**
     * 获取meta-data信息: router
     */
    private static String getMetaDataRouter(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null && applicationInfo.metaData.containsKey("router")) {
                        return applicationInfo.metaData.getString("router");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "router";
    }



    private static void initAppConfig() {
        ApiBase.BASE_URL = DomainUtil.getBaseUrl();
        ApiBase.SEARCH_URL = DomainUtil.getSearchUrl();
        ApiBase.UPLOAD_TIME_URL = DomainUtil.getUploadTimeUrl();
//        ApiManager.BASE_LANDING = GlobalBuildConfig.BASE_LANDING;
        Abase.initialize(App.getAppContext());
    }


    /**
     * 初始化dbFlow数据库
     */
    private static void initDbFlow() {
        DatabaseConfig databaseConfig = new DatabaseConfig.Builder(AppDatabase.class)
                .modelNotifier(DirectModelNotifier.get())
                .databaseName(App.getString(R.string.database_name))
                .build();
        FlowConfig flowConfig = new FlowConfig.Builder(App.getAppContext())
                .addDatabaseConfig(databaseConfig)
                .build();
        FlowManager.init(flowConfig);
    }


    private static void registerNetChangeListener() {
        NetChangeObserver.getInstance().registerNetChangeObserver((oldState, newState) -> {
            boolean isNetValid = newState.isNetAvailable();
            boolean isNetValidChange = newState.isNetValidChange(oldState);
            boolean isWeakNetChange = newState.isWeakNetChange(oldState);

            if(isNetValid){ // 有网啦～
                if(isNetValidChange) {  // 切到有网了
                    ALog.textSingle("切到有网了");

                }

                if(isWeakNetChange){
                    if(newState.isWeakNetwork()){ // 切到弱网了
                        GlideUtils.isWeakNetwork = true;
                    } else { // 切到强网了
                        GlideUtils.isWeakNetwork = false;
                    }
                } else {
//                    printGlideLog("是否弱网值没有变化，网络类型：" + newState.getNetworkType().getType());
                }
            } else {
                GlideUtils.isWeakNetwork = true;
//                printGlideLog("切到" + (newState.isConnected() ? "无效网络" : "无网") + "，网络类型：" + newState.getNetworkType().getType());
            }
        });
    }


    private static void addRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(throwableHandler->{
            if(throwableHandler instanceof UndeliverableException) {
                throwableHandler.printStackTrace();
            }
        });
    }


    private static void addRequestInterceptor() {
        RetrofitUtils.getInstance().addInterceptor(new RequestRetryInterceptor.Builder().build());
        RetrofitUtils.getInstance().addInterceptor(new AccountDeletedInterceptor());
        RetrofitUtils.getInstance().setEventListener(new OkHttpEventListener());
        Watcher.getInstance().registerDataSetObserver((event, object) -> {
            if(event == WatcherEvent.EVENT_SWITCH_DNS){ // 使用备用dns域名
                ALog.keyValue("AppInit", "addRequestInterceptor", new String[]{LogTag.SWITCH, LogTag.Switch_Dns}, "url", object.toString());
                ApiBase.BASE_URL = (String) object;
            }
        });
    }


    private static void initSmartRefreshLayout(){
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            return new MaterialHeader(context);
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new CustomRefreshFooter(context));
    }



}
