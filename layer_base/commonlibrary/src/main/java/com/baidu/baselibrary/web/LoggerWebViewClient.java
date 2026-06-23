package com.baidu.baselibrary.web;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.baselibrary.util.sys.LogUtil;

import androidx.annotation.Nullable;


public class LoggerWebViewClient extends WebViewClient {
    private static final String TAG = "LoggerWebViewClient";

    /**
     * 当URL即将在当前WebView中加载时，给宿主应用一个控制机会。如果未提供WebViewClient，
     * 默认情况下WebView会请求Activity Manager为URL选择合适的处理程序。如果提供了WebViewClient，
     * 返回{@code true}会导致当前WebView中止加载URL，而返回{@code false}会导致WebView
     * 像往常一样继续加载URL。
     *
     * <p><b>注意：</b>不要使用相同的URL调用{@link WebView#loadUrl(String)}然后返回{@code true}。
     * 这会不必要地取消当前加载并使用相同的URL开始新的加载。继续加载给定URL的正确方法是简单地返回{@code false}，
     * 而不调用{@link WebView#loadUrl(String)}。
     *
     * <p><b>注意：</b>此方法不适用于POST请求。
     *
     * <p><b>注意：</b>此方法可能用于子框架和非HTTP(S)协议；使用此类URL调用{@link WebView#loadUrl(String)}将失败。
     *
     * @param view 发起回调的WebView。
     * @param url 要加载的URL。
     * @return {@code true}取消当前加载，否则返回{@code false}。
     * @deprecated 请改用{@link #shouldOverrideUrlLoading(WebView, WebResourceRequest)}。
     */
    @Override
    @Deprecated
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtil.d(TAG, "shouldOverrideUrlLoading(), view=" + view
                + "\n  url-> " + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    /**
     * 当URL即将在当前WebView中加载时，给宿主应用一个控制机会。如果未提供WebViewClient，
     * 默认情况下WebView会请求Activity Manager为URL选择合适的处理程序。如果提供了WebViewClient，
     * 返回{@code true}会导致当前WebView中止加载URL，而返回{@code false}会导致WebView
     * 像往常一样继续加载URL。
     *
     * <p><b>注意：</b>不要使用请求的URL调用{@link WebView#loadUrl(String)}然后返回{@code true}。
     * 这会不必要地取消当前加载并使用相同的URL开始新的加载。继续加载给定URL的正确方法是简单地返回{@code false}，
     * 而不调用{@link WebView#loadUrl(String)}。
     *
     * <p><b>注意：</b>此方法不适用于POST请求。
     *
     * <p><b>注意：</b>此方法可能用于子框架和非HTTP(S)协议；使用此类URL调用{@link WebView#loadUrl(String)}将失败。
     *
     * @param view 发起回调的WebView。
     * @param request 包含请求详细信息的对象。
     * @return {@code true}取消当前加载，否则返回{@code false}。
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        LogUtil.d(TAG, "shouldOverrideUrlLoading(), view=" + view
                + "\n  request-> " + (request != null && request.getUrl() != null ? request.getUrl().toString() : ""));
        return super.shouldOverrideUrlLoading(view, request);
    }

    /**
     * 通知宿主应用页面已开始加载。此方法对每个主框架加载调用一次，因此包含iframe或框架集的页面将为
     * 主框架调用一次onPageStarted。这也意味着当嵌入式框架的内容更改时（即点击目标为iframe的链接），
     * onPageStarted将不会被调用，也不会为片段导航（导航到#fragment_id）调用。
     *
     * @param view 发起回调的WebView。
     * @param url 要加载的URL。
     * @param favicon 如果页面图标已存在于数据库中，则为该页面的图标。
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        LogUtil.d(TAG, "onPageStarted(), view=" + view + ", favicon=" + (favicon != null ? "not null" : "null")
                + "\n  url-> " + url);
        super.onPageStarted(view, url, favicon);
    }

    /**
     * 通知宿主应用页面已完成加载。此方法仅为主框架调用。收到{@code onPageFinished()}回调并不
     * 保证WebView绘制的下一帧将反映此时DOM的状态。为了通知当前DOM状态已准备好渲染，
     * 请使用{@link WebView#postVisualStateCallback}请求视觉状态回调，并等待提供的回调被触发。
     *
     * @param view 发起回调的WebView。
     * @param url 页面的URL。
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        LogUtil.d(TAG, "onPageFinished(), view=" + view
                + "\n  url-> " + url);
        super.onPageFinished(view, url);
    }

    /**
     * 通知宿主应用WebView将加载指定URL的资源。
     *
     * @param view 发起回调的WebView。
     * @param url WebView将加载的资源的URL。
     */
    @Override
    public void onLoadResource(WebView view, String url) {
        LogUtil.d(TAG, "onLoadResource(), view=" + view
                + "\n  url-> " + url);
        super.onLoadResource(view, url);
    }

    /**
     * 通知宿主应用来自先前页面导航的{@link android.webkit.WebView}内容将不再绘制。
     *
     * <p>此回调可用于确定何时可以安全地显示回收的{@link android.webkit.WebView}，确保不显示陈旧内容。
     * 它在可以保证{@link WebView#onDraw}不再绘制先前导航的任何内容的最早点调用。下一帧将显示
     * {@link WebView}的{@link WebView#setBackgroundColor 背景颜色}或新加载页面的某些内容。
     *
     * <p>此方法在HTTP响应的主体开始加载时调用，反映在DOM中，并将在后续绘制中可见。此回调发生在
     * 文档加载过程的早期，因此您应该预期链接资源（例如CSS和图像）可能不可用。
     *
     * <p>有关视觉状态更新的更细粒度通知，请参阅{@link WebView#postVisualStateCallback}。
     *
     * <p>请注意，适用于{@link WebView#postVisualStateCallback}的所有条件和建议也适用于此API。
     *
     * <p>此回调仅为主框架导航调用。
     *
     * @param view 发生导航的{@link android.webkit.WebView}。
     * @param url 触发此回调的页面导航对应的URL。
     */
    @Override
    public void onPageCommitVisible(WebView view, String url) {
        LogUtil.d(TAG, "onPageCommitVisible(), view=" + view
                + "\n  url-> " + url);
        super.onPageCommitVisible(view, url);
    }

    /**
     * 通知宿主应用资源请求并允许应用返回数据。如果返回值为{@code null}，WebView将继续
     * 像往常一样加载资源。否则，将使用返回的响应和数据。
     *
     * <p>此回调针对各种URL方案（例如{@code http(s):}、{@code data:}、{@code file:}等）调用，
     * 而不仅仅是通过网络发送请求的方案。这不用于{@code javascript:} URL、{@code blob:} URL，
     * 或通过{@code file:///android_asset/}或{@code file:///android_res/} URL访问的资产。
     *
     * <p>在重定向的情况下，这仅用于初始资源URL，而不是任何后续重定向URL。
     *
     * <p><b>注意：</b>此方法在UI线程以外的线程上调用，因此客户端在访问私有数据或视图系统时应谨慎。
     *
     * <p><b>注意：</b>启用安全浏览时，这些URL仍会进行安全浏览检查。如果不需要，可以使用
     * {@link WebView#setSafeBrowsingWhitelist}跳过该主机的安全浏览检查，或在{@link
     * #onSafeBrowsingHit}中通过调用{@link SafeBrowsingResponse#proceed}取消警告。
     *
     * @param view 请求资源的{@link android.webkit.WebView}。
     * @param url 资源的原始URL。
     * @return 包含响应信息的{@link android.webkit.WebResourceResponse}，或如果WebView应自行加载资源则为{@code null}。
     * @deprecated 请改用{@link #shouldInterceptRequest(WebView, WebResourceRequest)}。
     */
    @Override
    @Deprecated
    @Nullable
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        LogUtil.d(TAG, "shouldInterceptRequest(), view=" + view
                + "\n  url-> " + url);
        return super.shouldInterceptRequest(view, url);
    }

    /**
     * 通知宿主应用资源请求并允许应用返回数据。如果返回值为{@code null}，WebView将继续
     * 像往常一样加载资源。否则，将使用返回的响应和数据。
     *
     * <p>此回调针对各种URL方案（例如{@code http(s):}、{@code data:}、{@code file:}等）调用，
     * 而不仅仅是通过网络发送请求的方案。这不用于{@code javascript:} URL、{@code blob:} URL，
     * 或通过{@code file:///android_asset/}或{@code file:///android_res/} URL访问的资产。
     *
     * <p>在重定向的情况下，这仅用于初始资源URL，而不是任何后续重定向URL。
     *
     * <p><b>注意：</b>此方法在UI线程以外的线程上调用，因此客户端在访问私有数据或视图系统时应谨慎。
     *
     * <p><b>注意：</b>启用安全浏览时，这些URL仍会进行安全浏览检查。如果不需要，可以使用
     * {@link WebView#setSafeBrowsingWhitelist}跳过该主机的安全浏览检查，或在{@link
     * #onSafeBrowsingHit}中通过调用{@link SafeBrowsingResponse#proceed}取消警告。
     *
     * @param view 请求资源的{@link android.webkit.WebView}。
     * @param request 包含请求详细信息的对象。
     * @return 包含响应信息的{@link android.webkit.WebResourceResponse}，或如果WebView应自行加载资源则为{@code null}。
     */
    @Override
    @Nullable
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        LogUtil.d(TAG, "shouldInterceptRequest(), view=" + view
                + "\n  request-> " + (request != null && request.getUrl() != null ? request.getUrl().toString() : ""));
        return super.shouldInterceptRequest(view, request);
    }

    /**
     * 向宿主应用报告Web资源加载错误。这些错误通常表示无法连接到服务器。请注意，与回调的已弃用版本不同，
     * 新版本将针对任何资源（iframe、图像等）调用，而不仅仅是主页面。因此，建议在此回调中执行最低要求的工作。
     * @param view 发起回调的WebView。
     * @param request 发起的请求。
     * @param error 发生的错误信息。
     */
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        LogUtil.e(TAG, "onReceivedError(), view=" + view  + ", error=" + error
                + "\n  request-> " + (request != null && request.getUrl() != null ? request.getUrl().toString() : ""));
        super.onReceivedError(view, request, error);
    }

    /**
     * 通知宿主应用在加载资源时从服务器收到HTTP错误。HTTP错误的状态码>=400。此回调将针对
     * 任何资源（iframe、图像等）调用，而不仅仅是主页面。因此，建议在此回调中执行最低要求的工作。
     * 请注意，服务器响应的内容可能不在{@code errorResponse}参数中提供。
     * @param view 发起回调的WebView。
     * @param request 发起的请求。
     * @param errorResponse 发生的错误信息。
     */
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        LogUtil.e(TAG, "onReceivedHttpError(), view=" + view + ", errorResponse=" + errorResponse
                + "\n  request-> " + (request != null && request.getUrl() != null ? request.getUrl().toString() : ""));
        super.onReceivedHttpError(view, request, errorResponse);
    }

    /**
     * 询问宿主应用浏览器是否应重新发送数据，因为请求的页面是POST的结果。默认为不重新发送数据。
     *
     * @param view 发起回调的WebView。
     * @param dontResend 如果浏览器不应重新发送，则发送的消息
     * @param resend 如果浏览器应重新发送数据，则发送的消息
     */
    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        LogUtil.d(TAG, "onFormResubmission(), view=" + view + ", dontResend=" + dontResend + ", resend=" + resend);
        super.onFormResubmission(view, dontResend, resend);
    }

    /**
     * 通知宿主应用更新其访问过的链接数据库。
     *
     * @param view 发起回调的WebView。
     * @param url 正在访问的URL。
     * @param isReload 如果此URL正在重新加载，则为{@code true}。
     */
    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        LogUtil.w(TAG, "doUpdateVisitedHistory(), view=" + view + ", isReload=" + isReload
                + "\n  url-> " + url);
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    /**
     * 通知宿主应用在加载资源时发生SSL错误。宿主应用必须调用{@link SslErrorHandler#cancel}或
     * {@link SslErrorHandler#proceed}。请注意，该决定可能会保留用于响应未来的SSL错误。默认行为是取消加载。
     * <p>
     * 此API仅针对可恢复的SSL证书错误调用。在不可恢复的错误（例如服务器拒绝客户端）的情况下，
     * WebView将使用{@link #ERROR_FAILED_SSL_HANDSHAKE}调用{@link #onReceivedError(WebView, WebResourceRequest, WebResourceError)}。
     * <p>
     * 建议应用程序不要向用户提示SSL错误，因为用户不太可能做出明智的安全决定，
     * 并且WebView不提供任何UI以有意义的方式显示错误细节。
     * <p>
     * 此方法的应用程序覆盖可以显示自定义错误页面或静默记录问题，但强烈建议始终调用
     * {@link SslErrorHandler#cancel}，从不允许跳过错误。
     *
     * @param view 发起回调的WebView。
     * @param handler 将处理用户响应的{@link SslErrorHandler}。
     * @param error SSL错误对象。
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        LogUtil.e(TAG, "onReceivedSslError(), view=" + view + ", handler=" + handler + ", error=" + error);
        super.onReceivedSslError(view, handler, error);
    }

    /**
     * 通知宿主应用处理SSL客户端证书请求。宿主应用负责根据需要显示UI并提供密钥。有三种响应方式：
     * {@link ClientCertRequest#proceed}、{@link ClientCertRequest#cancel}或{@link
     * ClientCertRequest#ignore}。如果调用{@link ClientCertRequest#proceed}或{@link ClientCertRequest#cancel}，
     * Webview会将响应存储在内存中（在应用程序的生命周期内），并且不会再为同一主机和端口对调用{@code onReceivedClientCertRequest()}。
     * 如果调用{@link ClientCertRequest#ignore}，Webview不会存储响应。请注意，Chromium网络堆栈的多个层可能会
     * 缓存响应，因此忽略的行为只是最佳努力。
     *
     * 此方法在UI线程上调用。在回调期间，连接被挂起。
     *
     * 对于大多数用例，应用程序应该实现{@link android.security.KeyChainAliasCallback}接口，
     * 并将其传递给{@link android.security.KeyChain#choosePrivateKeyAlias}以启动一个活动，让用户选择适当的别名。
     * 钥匙串活动将通过实现接口中的回调方法提供别名。接下来，应用程序应该创建一个异步任务来调用
     * {@link android.security.KeyChain#getPrivateKey}来接收密钥。
     *
     * 客户端证书的示例实现可以在<A href="https://android.googlesource.com/platform/packages/apps/Browser/+/android-5.1.1_r1/src/com/android/browser/Tab.java">
     * AOSP Browser</a>中看到
     *
     * 默认行为是取消，不返回客户端证书。
     *
     * @param view 发起回调的WebView
     * @param request {@link ClientCertRequest}的实例
     */
    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        LogUtil.d(TAG, "onReceivedClientCertRequest(), view=" + view + ", request=" + request);
        request.cancel();
    }

    /**
     * 通知宿主应用WebView收到HTTP身份验证请求。宿主应用可以使用提供的{@link HttpAuthHandler}
     * 设置WebView对请求的响应。默认行为是取消请求。
     *
     * <p><b>注意：</b>提供的HttpAuthHandler必须在UI线程上使用。
     *
     * @param view 发起回调的WebView
     * @param handler 用于设置WebView响应的HttpAuthHandler
     * @param host 需要身份验证的主机
     * @param realm 需要身份验证的领域
     * @see WebView#getHttpAuthUsernamePassword
     */
    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        LogUtil.d(TAG, "onReceivedHttpAuthRequest(), view=" + view + ", handler=" + handler + ", host=" + host + ", realm=" + realm);
        handler.cancel();
    }

    /**
     * 给宿主应用一个同步处理按键事件的机会。例如，菜单快捷键事件需要通过这种方式过滤。如果返回true，
     * WebView将不处理按键事件。如果返回{@code false}，WebView将始终处理按键事件，因此视图链中的
     * 任何上级都不会看到按键事件。默认行为返回{@code false}。
     *
     * @param view 发起回调的WebView。
     * @param event 按键事件。
     * @return 如果宿主应用想自己处理按键事件，返回{@code true}，否则返回{@code false}
     */
    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        LogUtil.d(TAG, "shouldOverrideKeyEvent(), view=" + view + ", event=" + event);
        return false;
    }

    /**
     * 通知宿主应用某个按键未被WebView处理。除了系统按键，WebView在正常流程中始终消耗按键，
     * 或者如果{@link #shouldOverrideKeyEvent}返回{@code true}。这是从按键分派的地方异步调用的。
     * 它给宿主应用一个处理未处理按键事件的机会。
     *
     * @param view 发起回调的WebView。
     * @param event 按键事件。
     */
    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        LogUtil.d(TAG, "onUnhandledKeyEvent(), view=" + view + ", event=" + event);
        super.onUnhandledKeyEvent(view, event);
    }

    /**
     * 通知宿主应用应用于WebView的比例已更改。
     *
     * @param view 发起回调的WebView。
     * @param oldScale 旧的比例因子
     * @param newScale 新的比例因子
     */
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        LogUtil.d(TAG, "onScaleChanged(), view=" + view + ", oldScale=" + oldScale + ", newScale=" + newScale);
        super.onScaleChanged(view, oldScale, newScale);
    }

    /**
     * 通知宿主应用已处理自动登录用户的请求。
     * @param view 请求登录的WebView。
     * @param realm 用于查找账户的账户领域。
     * @param account 可选账户。如果不为{@code null}，应将该账户与设备上的账户进行检查。
     *                如果是有效账户，应用于登录用户。
     * @param args 用于登录用户的特定于身份验证器的参数。
     */
    @Override
    public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
        LogUtil.d(TAG, "onReceivedLoginRequest(), view=" + view + ", realm=" + realm + ", account=" + account + ", args=" + args);
        super.onReceivedLoginRequest(view, realm, account, args);
    }

    /**
     * 通知宿主应用给定WebView的渲染进程已退出。
     *
     * 多个WebView实例可能与单个渲染进程相关联；将为每个受影响的WebView调用onRenderProcessGone。
     * 此回调的应用程序实现应仅尝试清理作为参数给出的特定WebView，并且不应假设其他WebView实例受到影响。
     *
     * 给定的WebView不能使用，应从视图层次结构中删除，所有对它的引用都应清理，例如使用{@link android.view.View#findViewById}
     * 和类似调用在Activity或其他类中保存的任何引用等。
     *
     * 为了测试目的导致渲染进程崩溃，应用程序可以在WebView上调用{@code loadUrl("chrome://crash")}。
     * 请注意，如果多个WebView实例共享一个渲染进程，不仅是加载chrome://crash的特定WebView，
     * 其他实例也可能受到影响。
     *
     * @param view 需要清理的WebView。
     * @param detail 它退出的原因。
     * @return 如果宿主应用处理了进程退出的情况，返回{@code true}，否则，如果渲染进程崩溃，应用程序将崩溃，
     *         或者如果渲染进程被系统杀死，应用程序将被杀死。
     */
    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        LogUtil.d(TAG, "onRenderProcessGone(), view=" + view + ", detail=" + detail);
        return super.onRenderProcessGone(view, detail);
    }

    /**
     * 通知宿主应用加载的URL已被安全浏览标记。
     *
     * 应用程序必须调用回调以指示首选响应。默认行为是向用户显示 interstitial，其中报告复选框可见。
     *
     * 如果应用程序需要显示自己的自定义 interstitial UI，可以根据用户响应使用{@link SafeBrowsingResponse#backToSafety}
     * 或{@link SafeBrowsingResponse#proceed}异步调用回调。
     *
     * @param view 命中恶意资源的WebView。
     * @param request 包含请求详细信息的对象。
     * @param threatType 资源被安全浏览捕获的原因，对应于{@code SAFE_BROWSING_THREAT_*}值。
     * @param callback 应用程序必须调用其中一个回调方法。
     */
    @Override
    public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
        LogUtil.d(TAG, "onSafeBrowsingHit(), view=" + view + ", threatType=" + threatType + ", callback=" + callback
                + "\n  request-> " + (request != null && request.getUrl() != null ? request.getUrl().toString() : ""));
        super.onSafeBrowsingHit(view, request, threatType, callback);
    }
}