package com.baidu.baselibrary.web;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

import com.baidu.baselibrary.util.sys.LogUtil;

import androidx.annotation.Nullable;



public class LoggerWebChromeClient extends WebChromeClient {
    private static final String TAG = "LoggerWebChromeClient";

    /**
     * 告知宿主应用加载页面的当前进度。
     * @param view 发起回调的WebView。
     * @param newProgress 当前页面加载进度，用0到100之间的整数表示。
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        LogUtil.i(TAG, "onProgressChanged: view=" + view + ", newProgress=" + newProgress);
    }

    /**
     * 通知宿主应用文档标题发生变化。
     * @param view 发起回调的WebView。
     * @param title 包含新文档标题的字符串。
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        LogUtil.i(TAG, "onReceivedTitle: view=" + view + ", title=" + title);
    }

    /**
     * 通知宿主应用当前页面有新的收藏图标。
     * @param view 发起回调的WebView。
     * @param icon 包含当前页面收藏图标的Bitmap。
     */
    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        LogUtil.i(TAG, "onReceivedIcon: view=" + view + ", icon=" + (icon != null ? "not null" : "null"));
    }

    /**
     * 通知宿主应用苹果触摸图标的URL。
     * @param view 发起回调的WebView。
     * @param url 图标的URL。
     * @param precomposed 如果URL是预合成的触摸图标，则为true。
     */
    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        LogUtil.i(TAG, "onReceivedTouchIconUrl: view=" + view + ", url=" + url + ", precomposed=" + precomposed);
    }

    /**
     * 通知宿主应用当前页面已进入全屏模式。在此调用后，网页内容将不再在WebView中渲染，而是在{@code view}中渲染。
     * 宿主应用应将此View添加到配置了{@link android.view.WindowManager.LayoutParams#FLAG_FULLSCREEN}标志的Window中，
     * 以实际全屏显示此网页内容。
     *
     * <p>应用可以通过调用{@code callback}显式退出全屏模式（例如当用户按下返回按钮时）。
     * 但这通常不是必需的，因为网页通常会显示自己的UI来关闭全屏。
     * 无论WebView如何退出全屏模式，WebView都会调用{@link #onHideCustomView()}，
     * 通知应用移除自定义View。
     *
     * <p>如果重写此方法，应用还必须重写{@link #onHideCustomView()}。
     *
     * @param view 要显示的View对象。
     * @param callback 调用此回调以请求页面退出全屏模式。
     */
    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        LogUtil.i(TAG, "onShowCustomView: view=" + view + ", callback=" + callback);
    }

    /**
     * 通知宿主应用当前页面希望以特定方向显示自定义View。
     * @param view 要显示的View对象。
     * @param requestedOrientation 作为{@link ActivityInfo#screenOrientation ActivityInfo.screenOrientation}中使用的方向常量。
     * @param callback 当View被 dismiss 时要调用的回调。
     * @deprecated 此方法支持已过时的插件机制，将来不会被调用
     */
    @Override
    @Deprecated
    public void onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
        LogUtil.i(TAG, "onShowCustomView: view=" + view + ", requestedOrientation=" + requestedOrientation + ", callback=" + callback);
    }

    /**
     * 通知宿主应用当前页面已退出全屏模式。宿主应用必须隐藏自定义View（之前传递给{@link
     * #onShowCustomView(View, WebChromeClient.CustomViewCallback) onShowCustomView()}的View）。
     * 在此调用后，网页内容将再次在原始WebView中渲染。
     *
     * <p>注意：如果重写此方法，应用必须同时重写{@link #onShowCustomView(View, WebChromeClient.CustomViewCallback) onShowCustomView()}。
     */
    @Override
    public void onHideCustomView() {
        LogUtil.i(TAG, "onHideCustomView");
    }

    /**
     * 请求宿主应用创建新窗口。如果宿主应用选择响应此请求，应从此方法返回{@code true}，
     * 创建新的WebView来承载窗口，将其插入到View系统中，并将提供的resultMsg消息发送到其目标，
     * 并将新WebView作为参数。如果宿主应用选择不响应请求，应从此方法返回{@code false}。
     * 此方法的默认实现不执行任何操作，因此返回{@code false}。
     * <p>
     * 当{@code isUserGesture}标志为false时，应用通常不应允许创建窗口，因为这可能是不需要的弹出窗口。
     * <p>
     * 应用在显示新窗口时应谨慎：不要简单地将其覆盖在现有WebView上，因为这可能会误导用户他们正在查看的站点。
     * 如果应用显示主页的URL，请确保也以类似方式显示新窗口的URL。如果应用不显示URL，
     * 请考虑完全禁止创建新窗口。
     * <p>注意：没有可靠的方法来判断哪个页面请求了新窗口：请求可能源自WebView内的第三方iframe。
     *
     * @param view 发起新窗口请求的WebView。
     * @param isDialog 如果新窗口应为对话框而非全屏窗口，则为true。
     * @param isUserGesture 如果请求是由用户手势（如用户点击链接）发起的，则为true。
     * @param resultMsg 创建新WebView后要发送的消息。resultMsg.obj是{@link WebView.WebViewTransport}对象。
     *                  应通过调用{@link WebView.WebViewTransport#setWebView(WebView) WebView.WebViewTransport.setWebView(WebView)}
     *                  来使用此对象传输新WebView。
     * @return 如果宿主应用将创建新窗口，此方法应返回{@code true}，此时应将resultMsg发送到其目标。
     *         否则，此方法应返回{@code false}。从此方法返回{@code false}但同时发送resultMsg将导致未定义的行为。
     */
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        LogUtil.i(TAG, "onCreateWindow: view=" + view + ", isDialog=" + isDialog + ", isUserGesture=" + isUserGesture + ", resultMsg=" + resultMsg);
        return false;
    }

    /**
     * 请求显示和聚焦此WebView。这可能是由于另一个WebView在此WebView中打开链接并请求显示此WebView而发生。
     * @param view 需要聚焦的WebView。
     */
    @Override
    public void onRequestFocus(WebView view) {
        LogUtil.i(TAG, "onRequestFocus: view=" + view);
    }

    /**
     * 通知宿主应用关闭给定的WebView，并在必要时将其从视图系统中移除。
     * 此时，WebCore已停止此窗口中的任何加载，并已移除JavaScript中的任何跨脚本能力。
     * <p>
     * 与{@link #onCreateWindow}一样，应用应确保更新显示的任何URL或安全指示器，
     * 以便用户可以知道他们正在交互的页面已关闭。
     *
     * @param window 需要关闭的WebView。
     */
    @Override
    public void onCloseWindow(WebView window) {
        LogUtil.w(TAG, "onCloseWindow: window=" + window);
    }

    /**
     * 通知宿主应用网页想要显示JavaScript {@code alert()}对话框。
     * <p>如果此方法返回{@code false}或未被重写，默认行为是显示包含警告消息的对话框，
     * 并暂停JavaScript执行直到对话框被关闭。
     * <p>要显示自定义对话框，应用应从此方法返回{@code true}，此时将不会显示默认对话框，
     * 并且JavaScript执行将被暂停。当自定义对话框被关闭时，应用应调用{@code JsResult.confirm()}，
     * 以便可以恢复JavaScript执行。
     * <p>要抑制对话框并允许JavaScript执行继续，请立即调用{@code JsResult.confirm()}，然后返回{@code true}。
     * <p>注意：如果{@link WebChromeClient}设置为{@code null}，或者根本没有设置{@link WebChromeClient}，
     * 默认对话框将被抑制，JavaScript执行将立即继续。
     * <p>注意：默认对话框不会从父窗口继承{@link android.view.Display#FLAG_SECURE}标志。
     *
     * @param view 发起回调的WebView。
     * @param url 请求对话框的页面的URL。
     * @param message 要在窗口中显示的消息。
     * @param result 用于确认用户关闭窗口的JsResult。
     * @return 如果请求被处理或忽略，返回boolean {@code true}。如果WebView需要显示默认对话框，返回{@code false}。
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        LogUtil.i(TAG, "onJsAlert: view=" + view + ", url=" + url + ", message=" + message + ", result=" + result);
        return false;
    }

    /**
     * 通知宿主应用网页想要显示JavaScript {@code confirm()}对话框。
     * <p>如果此方法返回{@code false}或未被重写，默认行为是显示包含消息的对话框，
     * 并暂停JavaScript执行直到对话框被关闭。当用户按下“确认”按钮时，默认对话框将返回{@code true}
     * 到JavaScript {@code confirm()}代码，当用户按下“取消”按钮或关闭对话框时，将返回{@code false}到JavaScript代码。
     * <p>要显示自定义对话框，应用应从此方法返回{@code true}，此时将不会显示默认对话框，
     * 并且JavaScript执行将被暂停。当自定义对话框被关闭时，应用应调用{@code JsResult.confirm()}或{@code JsResult.cancel()}。
     * <p>要抑制对话框并允许JavaScript执行继续，请立即调用{@code JsResult.confirm()}或{@code JsResult.cancel()}，然后返回{@code true}。
     * <p>注意：如果{@link WebChromeClient}设置为{@code null}，或者根本没有设置{@link WebChromeClient}，
     * 默认对话框将被抑制，默认值{@code false}将立即返回给JavaScript代码。
     * <p>注意：默认对话框不会从父窗口继承{@link android.view.Display#FLAG_SECURE}标志。
     *
     * @param view 发起回调的WebView。
     * @param url 请求对话框的页面的URL。
     * @param message 要在窗口中显示的消息。
     * @param result 用于向javascript发送用户响应的JsResult。
     * @return 如果请求被处理或忽略，返回boolean {@code true}。如果WebView需要显示默认对话框，返回{@code false}。
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        LogUtil.i(TAG, "onJsConfirm: view=" + view + ", url=" + url + ", message=" + message + ", result=" + result);
        return false;
    }

    /**
     * 通知宿主应用网页想要显示JavaScript {@code prompt()}对话框。
     * <p>如果此方法返回{@code false}或未被重写，默认行为是显示包含消息的对话框，
     * 并暂停JavaScript执行直到对话框被关闭。一旦对话框被关闭，JavaScript {@code prompt()}将返回用户输入的字符串，
     * 如果用户按下“取消”按钮，则返回null。
     * <p>要显示自定义对话框，应用应从此方法返回{@code true}，此时将不会显示默认对话框，
     * 并且JavaScript执行将被暂停。当自定义对话框被关闭时，应用应调用{@code JsPromptResult.confirm(result)}。
     * <p>要抑制对话框并允许JavaScript执行继续，请立即调用{@code JsPromptResult.confirm(result)}，然后返回{@code true}。
     * <p>注意：如果{@link WebChromeClient}设置为{@code null}，或者根本没有设置{@link WebChromeClient}，
     * 默认对话框将被抑制，{@code null}将立即返回给JavaScript代码。
     * <p>注意：默认对话框不会从父窗口继承{@link android.view.Display#FLAG_SECURE}标志。
     *
     * @param view 发起回调的WebView。
     * @param url 请求对话框的页面的URL。
     * @param message 要在窗口中显示的消息。
     * @param defaultValue 提示对话框中显示的默认值。
     * @param result 用于向javascript发送用户响应的JsPromptResult。
     * @return 如果请求被处理或忽略，返回boolean {@code true}。如果WebView需要显示默认对话框，返回{@code false}。
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        LogUtil.i(TAG, "onJsPrompt: view=" + view + ", url=" + url + ", message=" + message + ", defaultValue=" + defaultValue + ", result=" + result);
        return false;
    }

    /**
     * 通知宿主应用网页想要确认来自JavaScript {@code onbeforeunload}的导航。
     * <p>如果此方法返回{@code false}或未被重写，默认行为是显示包含消息的对话框，
     * 并暂停JavaScript执行直到对话框被关闭。当用户确认导航时，默认对话框将继续导航，
     * 如果用户想留在当前页面，则停止导航。
     * <p>要显示自定义对话框，应用应从此方法返回{@code true}，此时将不会显示默认对话框，
     * 并且JavaScript执行将被暂停。当自定义对话框被关闭时，应用应调用{@code JsResult.confirm()}以继续导航，
     * 或调用{@code JsResult.cancel()}以留在当前页面。
     * <p>要抑制对话框并允许JavaScript执行继续，请立即调用{@code JsResult.confirm()}或{@code JsResult.cancel()}，然后返回{@code true}。
     * <p>注意：如果{@link WebChromeClient}设置为{@code null}，或者根本没有设置{@link WebChromeClient}，
     * 默认对话框将被抑制，导航将立即恢复。
     * <p>注意：默认对话框不会从父窗口继承{@link android.view.Display#FLAG_SECURE}标志。
     *
     * @param view 发起回调的WebView。
     * @param url 请求对话框的页面的URL。
     * @param message 要在窗口中显示的消息。
     * @param result 用于向javascript发送用户响应的JsResult。
     * @return 如果请求被处理或忽略，返回boolean {@code true}。如果WebView需要显示默认对话框，返回{@code false}。
     */
    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        LogUtil.i(TAG, "onJsBeforeUnload: view=" + view + ", url=" + url + ", message=" + message + ", result=" + result);
        return false;
    }

    /**
     * 告知客户端特定来源的Web SQL数据库API已超出配额，并请求新配额。
     * 客户端必须通过调用提供的{@link WebStorage.QuotaUpdater}实例的
     * {@link WebStorage.QuotaUpdater#updateQuota(long) updateQuota(long)}方法来响应。
     * 新配额可以设置的最小值是当前配额。默认实现以当前配额响应，因此配额不会增加。
     * @param url 触发通知的页面的URL
     * @param databaseIdentifier 超出配额的数据库的标识符。
     * @param quota 来源的配额，以字节为单位
     * @param estimatedDatabaseSize 有问题的数据库的估计大小，以字节为单位
     * @param totalQuota 所有来源的总配额，以字节为单位
     * @param quotaUpdater {@link WebStorage.QuotaUpdater}的实例，必须用于通知WebView新的配额。
     * @deprecated 此方法不再被调用；WebView现在使用HTML5 / JavaScript配额管理API。
     */
    @Override
    @Deprecated
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        LogUtil.i(TAG, "onExceededDatabaseQuota: url=" + url + ", databaseIdentifier=" + databaseIdentifier + ", quota=" + quota + ", estimatedDatabaseSize=" + estimatedDatabaseSize + ", totalQuota=" + totalQuota + ", quotaUpdater=" + quotaUpdater);
        // 此默认实现将当前配额传递回WebCore。WebCore将解释为新配额被拒绝。
        quotaUpdater.updateQuota(quota);
    }

    /**
     * 通知宿主应用程序缓存已达到最大大小。客户端必须通过调用提供的
     * {@link WebStorage.QuotaUpdater}实例的{@link WebStorage.QuotaUpdater#updateQuota(long) updateQuota(long)}
     * 方法来响应。新配额可以设置的最小值是当前配额。默认实现以当前配额响应，因此配额不会增加。
     * @param requiredStorage 触发此通知的应用程序缓存操作所需的存储量，以字节为单位。
     * @param quota 当前最大的应用程序缓存大小，以字节为单位
     * @param quotaUpdater {@link WebStorage.QuotaUpdater}的实例，必须用于通知WebView新的配额。
     * @deprecated 此方法不再被调用；WebView现在使用HTML5 / JavaScript配额管理API。
     * @removed 此方法不再被调用；WebView现在使用HTML5 / JavaScript配额管理API。
     */
    @Deprecated
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        LogUtil.i(TAG, "onReachedMaxAppCacheSize: requiredStorage=" + requiredStorage + ", quota=" + quota + ", quotaUpdater=" + quotaUpdater);
        quotaUpdater.updateQuota(quota);
    }

    /**
     * 通知宿主应用来自指定来源的网页内容正在尝试使用地理位置API，
     * 但当前该来源没有设置权限状态。宿主应用应使用指定的回调设置所需的权限状态。
     * 有关详细信息，请参阅{@link GeolocationPermissions}。
     *
     * <p>注意：对于针对Android N及更高版本SDK（API级别>{@link android.os.Build.VERSION_CODES#M}）的应用，
     * 此方法仅在来自安全来源（如https）的请求时调用。在非安全来源上，地理位置请求会自动被拒绝。
     *
     * @param origin 尝试使用地理位置API的网页内容的来源。
     * @param callback 用于设置来源权限状态的回调。
     */
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        LogUtil.i(TAG, "onGeolocationPermissionsShowPrompt: origin=" + origin + ", callback=" + callback);
    }

    /**
     * 通知宿主应用对地理位置权限的请求（通过先前对
     * {@link #onGeolocationPermissionsShowPrompt(String,GeolocationPermissions.Callback) onGeolocationPermissionsShowPrompt()}的调用发出）已被取消。
     * 因此，任何相关的UI都应隐藏。
     */
    @Override
    public void onGeolocationPermissionsHidePrompt() {
        LogUtil.i(TAG, "onGeolocationPermissionsHidePrompt");
    }

    /**
     * 通知宿主应用网页内容正在请求访问指定资源的权限，并且当前权限未被授予或拒绝。
     * 宿主应用必须调用{@link PermissionRequest#grant(String[])}或{@link PermissionRequest#deny()}。
     *
     * 如果不重写此方法，权限将被拒绝。
     *
     * @param request 当前网页内容的PermissionRequest。
     */
    @Override
    public void onPermissionRequest(PermissionRequest request) {
        LogUtil.i(TAG, "onPermissionRequest: request=" + request);
        super.onPermissionRequest(request);
    }

    /**
     * 通知宿主应用给定的权限请求已被取消。因此，任何相关的UI都应隐藏。
     *
     * @param request 需要取消的PermissionRequest。
     */
    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {
        LogUtil.i(TAG, "onPermissionRequestCanceled: request=" + request);
    }

    /**
     * 告知客户端发生了JavaScript执行超时。客户端可以决定是否中断执行。
     * 如果客户端返回{@code true}，JavaScript将被中断。如果客户端返回{@code false}，执行将继续。
     * 请注意，在继续执行的情况下，超时计数器将重置，并且如果脚本在下一个检查点未完成，回调将继续发生。
     * @return 是否应中断JavaScript执行的boolean值。
     * @deprecated 此方法不再受支持，不会被调用。
     */
    // 此方法仅在使用JSC javascript引擎时调用。Froyo将V8设为默认JS引擎，并且在b/5495373中删除了使用JSC构建的支持。
    // V8没有用于进行此类回调的机制。
    @Override
    @Deprecated
    public boolean onJsTimeout() {
        LogUtil.w(TAG, "onJsTimeout");
        return true;
    }

    /**
     * 向宿主应用报告JavaScript错误消息。ChromeClient应重写此方法以按其认为合适的方式处理日志消息。
     * @param message 要报告的错误消息。
     * @param lineNumber 错误的行号。
     * @param sourceID 导致错误的源文件的名称。
     * @deprecated 请改用{@link #onConsoleMessage(ConsoleMessage) onConsoleMessage(ConsoleMessage)}。
     */
    @Override
    @Deprecated
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        LogUtil.i(TAG, "onConsoleMessage: message=" + message + ", lineNumber=" + lineNumber + ", sourceID=" + sourceID);
    }

    /**
     * 向宿主应用报告JavaScript控制台消息。ChromeClient应重写此方法以按其认为合适的方式处理日志消息。
     * @param consoleMessage 包含控制台消息详细信息的对象。
     * @return 如果消息被客户端处理，返回{@code true}。
     */
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        LogUtil.i(TAG, "onConsoleMessage: consoleMessage=" + consoleMessage);
        // 为了向后兼容，调用此函数的旧版本。
        super.onConsoleMessage(consoleMessage);
        return false;
    }

    /**
     * 当不播放时，视频元素由"海报"图像表示。可以通过HTML中video标签的poster属性指定要使用的图像。
     * 如果缺少该属性，则将使用默认海报。此方法允许ChromeClient提供该默认图像。
     *
     * @return 用作默认海报的图像Bitmap，或如果没有可用图像则为{@code null}。
     */
    @Override
    @Nullable
    public Bitmap getDefaultVideoPoster() {
        Bitmap poster = super.getDefaultVideoPoster();
        LogUtil.i(TAG, "getDefaultVideoPoster: return=" + (poster != null ? "not null" : "null"));
        return poster;
    }

    /**
     * 获取在全屏视频缓冲时要显示的View。宿主应用可以重写此方法以提供包含旋转器或类似内容的View。
     *
     * @return 视频加载时要显示的View。
     */
    @Nullable
    public View getVideoLoadingProgressView() {
        View view = super.getVideoLoadingProgressView();
        LogUtil.i(TAG, "getVideoLoadingProgressView: return=" + (view != null ? "not null" : "null"));
        return view;
    }

    /**
     * 获取所有已访问历史记录项的列表，用于链接着色
     */
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        LogUtil.i(TAG, "getVisitedHistory: callback=" + callback);
        super.getVisitedHistory(callback);
    }

    /**
     * 告知客户端显示文件选择器。
     *
     * 这是为了处理具有"file"输入类型的HTML表单，响应于用户按下"选择文件"按钮。
     * 要取消请求，请调用<code>filePathCallback.onReceiveValue(null)</code>并返回{@code true}。
     *
     * @param webView 发起请求的WebView实例。
     * @param filePathCallback 调用此回调以提供要上传的文件路径列表，或{@code null}以取消。
     *                         仅当{@link #onShowFileChooser}实现返回{@code true}时才必须调用。
     * @param fileChooserParams 描述要打开的文件选择器的模式，以及要使用的选项。
     * @return 如果将调用filePathCallback，返回{@code true}；否则返回{@code false}以使用默认处理。
     *
     * @see WebChromeClient.FileChooserParams
     */
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        LogUtil.i(TAG, "onShowFileChooser: webView=" + webView + ", filePathCallback=" + filePathCallback + ", fileChooserParams=" + fileChooserParams);
        return false;
    }
}