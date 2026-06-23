package com.benefit.novelverse.view.webview;

import android.app.Activity;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.benefit.novelverse.utils.Constant;
import com.tencent.common.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class JSBridgeHelper {

    private final WeakReference<BridgeWebView> mWebViewReference;
    private final WeakReference<Activity> mActivityReference;
    private final ArrayList<String> mToolbarBlackList = new ArrayList<>();

    public JSBridgeHelper(BridgeWebView webView) {
        mWebViewReference = new WeakReference<>(webView);
        mActivityReference = new WeakReference<>((Activity) webView.getContext());

        if (mWebViewReference.get() != null) {
            mWebViewReference.get().registerHandler("xcyh", (json, jsCallback) -> {
                try {
                    JSONObject jsonObejct = new JSONObject(json);
                    String action = jsonObejct.optString("action");
                    String data = jsonObejct.optString("data");
                    onJsActionEvent(action, data, jsCallback);
                } catch (Throwable thr) {
                    thr.printStackTrace();
                }
            });
        }
    }

    private void onJsActionEvent(String action, String data, CallBackFunction jsCallback) {
        switch (action) {
            case Constant.JS_ACTION.SHARE_NATIVE:
                shareFromWeb(data, jsCallback);
                break;
            case Constant.JS_ACTION.JUMP_TO_LOGIN:
                handleLoginAction();
                break;
            case Constant.JS_ACTION.TOOLBAR_BLACK_LIST:
                handleReceiveToolbarBlackList(data);
                break;
            case Constant.JS_ACTION.REQUEST_TOUCH_EVENT:
                handleRequestTouchEvent(data);
                break;
            case Constant.JS_ACTION.OPEN_APP:
                handleOpenApp(data);
                break;
            case Constant.JS_ACTION.DO_PAY:
                handleDoPayAction(data);
                break;
            case Constant.JS_ACTION.SHOW_TOAST:
                handleShowToast(data);
                break;
            case Constant.JS_ACTION.CLOSE_ACTIVIYY:
                handleCloseAction();
                break;
        }
    }

    private void handleCloseAction() {
        Activity context = mActivityReference.get();
        if (context != null) {
            context.finish();
        }
    }

    private void handleShowToast(String data) {
        Activity context = mActivityReference.get();
        if (context != null) {
            try {
                ToastUtils.showShort(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDoPayAction(String data) {
        Activity context = mActivityReference.get();
        if (context != null) {
            try {
                // JSONObject jsonObj = new JSONObject(data);
                // String platformName = jsonObj.getString("platform");
                // String payUrl = jsonObj.getString("url");
                // PayMethod platform = TextUtils.equals(platformName, PayMethod.ALIPAY.getPayName())
                //        ? PayMethod.ALIPAY : PayMethod.WECHAT;
                //
                // RechargeHelper.doPay(platform, payUrl, context, new PayListener() {
                //     @Override
                //     public void onSuccess(PayOrder payOrder) {
                //         ToastUtils.showShort(R.string.pp_pay_success);
                //         sendPayResultToJs(payOrder, "success");
                //     }
                //
                //     @Override
                //     public void onCancel(PayOrder payOrder) {
                //         ToastUtils.showShort(R.string.pp_pay_cancel);
                //         sendPayResultToJs(payOrder, "cancel");
                //     }
                //
                //     @Override
                //     public void onError(PayOrder payOrder, String err) {
                //         ToastUtils.showShort(R.string.pp_pay_error);
                //         sendPayResultToJs(payOrder, "error " + err);
                //     }
                // });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleOpenApp(String data) {
        Activity context = mActivityReference.get();
        if (context != null) {
            // FoAppUtils.openRouter(context, data);
        }
    }

    private void handleRequestTouchEvent(String data) {
        try {
            JSONObject json = new JSONObject(data);
            boolean requestDisallowInterceptTouchEvent =
                    json.optBoolean("requestDisallowInterceptTouchEvent", false);
            if (mWebViewReference.get() != null) {
                mWebViewReference.get().requestDisallowInterceptTouchEvent(requestDisallowInterceptTouchEvent);
            }
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
    }

    private void handleReceiveToolbarBlackList(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                mToolbarBlackList.add(jsonArray.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLoginAction() {
        // TODO: 登录逻辑
    }

    public void sendMsgToJs(String key, String message, CallBackFunction jsCallback) {
        if (mWebViewReference.get() != null) {
            mWebViewReference.get().callHandler(key, message, jsCallback);
        }
    }

    public void sendMsgToJs(String key, String message) {
        sendMsgToJs(key, message, null);
    }

    private void shareFromWeb(String json, CallBackFunction jsCallback) {
        Activity context = mActivityReference.get();
        if (context != null) {
            try {
                // JSONObject jsonObj = new JSONObject(json);
                // String platformName = jsonObj.getString("platform");
                // String shareUrl = jsonObj.optString("url", "");
                // String title = jsonObj.optString("title", Abase.getResources().getString(R.string.app_name));
                // String desc = jsonObj.optString("desc", "好看的小说都在这里");
                // String imgUrl = jsonObj.optString("image", "");
                //
                // String shareTypeStr = jsonObj.optString("type");
                // int shareType = TextUtils.equals(shareTypeStr, "image")
                //        ? ShareHelper.SHARE_IMG : ShareHelper.SHARE_WEB;
                //
                // if (!TextUtils.isEmpty(shareUrl)) {
                //     shareUrl = Uri.parse(shareUrl).buildUpon()
                //            .appendQueryParameter("channel", ChannelReaderUtil.getChannel(context))
                //            .build().toString();
                // }
                //
                // SocialMedia platform = null;
                // if (ShareHelper.QQ_NAME.equals(platformName)) {
                //     platform = SocialMedia.PLATFORM_QQ;
                // } else if (ShareHelper.QZ_NAME.equals(platformName)) {
                //     platform = SocialMedia.PLATFORM_QQ_ZONE;
                // } else if (ShareHelper.WECHAT_NAME.equals(platformName)) {
                //     platform = SocialMedia.PLATFORM_WECHAT;
                // } else if (ShareHelper.WECHAT_MOMENT_NAME.equals(platformName)) {
                //     platform = SocialMedia.PLATFORM_WECHAT_MOMENT;
                // }
                //
                // ShareHelper.ShareCallback shareCallback = new ShareHelper.ShareCallback() {
                //     @Override
                //     public void onCompleted(String platform, int action) {
                //         jsCallback.onCallBack("success");
                //     }
                //
                //     @Override
                //     public void onError(String platform, int action, String errMsg) {
                //         jsCallback.onCallBack("err " + errMsg);
                //     }
                //
                //     @Override
                //     public void onCancel(String platform, int action) {
                //         jsCallback.onCallBack("cancel");
                //     }
                // };
                //
                // if (platform != null) {
                //     if (shareType == ShareHelper.SHARE_WEB) {
                //         ShareHelper.shareWebPage(platform, shareUrl, title, shareCallback);
                //     } else {
                //         ShareHelper.shareImage(platform, imgUrl, shareCallback);
                //     }
                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final String TAG = "JSBridgeHelper";
    public static final String DATA_META = "data";
    public static final String ACTION_META = "action";
}
