package com.baidu.baselibrary.util.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.base.global.GlobalBuildConfig;
import com.baidu.baselibrary.util.sys.LogUtil;
import com.base.api.GlobalContext;
import com.base.net.state.NetChangeObserver;
import com.base.util.collection.ListUtil;
import com.base.util.net.NetworkType;
import com.base.util.ui.UIUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jess.baselibrary.R;

import androidx.databinding.BindingAdapter;


public class GlideUtils {

    public static final String TAG = "Glide";

    /**
     * 是否显示logcat日志
     */
    public static boolean showGlideLog = false;

    /**
     * 是否弱网
     */
    public static boolean isWeakNetwork;


    static {
//        isWeakNetwork = true; // todo test
        NetworkType networkType = NetChangeObserver.getInstance().getNetworkState().getNetworkType();
        isWeakNetwork = networkType.isWeakNetwork();
        if(GlobalBuildConfig.DEBUG){
            LogUtil.d(TAG, "初始值：" + networkType.getType() + "，是否为弱网：" + isWeakNetwork);
        }
    }


    /**
     * 暂停所有的图片请求
     */
    public static void pauseAllRequests(String... logs){
        RequestManager requestManager = Glide.with(GlobalContext.getContext());
        if(!requestManager.isPaused()){
            requestManager.pauseRequests();

            if(GlobalBuildConfig.DEBUG && showGlideLog){
                LogUtil.d(TAG, "pauseAllRequests() " + ListUtil.toString(logs));
            }
        }
    }

    /**
     * 重启所有的图片请求
     */
    public static void resumeAllRequests(String... logs){
        RequestManager requestManager = Glide.with(GlobalContext.getContext());
        if(requestManager.isPaused()){
            requestManager.resumeRequests();

            if(GlobalBuildConfig.DEBUG && showGlideLog){
                LogUtil.d(TAG, "resumeAllRequests() " + ListUtil.toString(logs));
            }
        }
    }




    @BindingAdapter({"imgUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if(!checkAlive(view)){
            return;
        }
        String url = transferImageUrl(imageUrl);
        log(url);

        GlideRequest<Drawable> glideRequest = GlideApp.with(view.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_bg_vertical)
                .error(R.drawable.default_bg_vertical)
                .centerCrop();
//                .transition(DrawableTransitionOptions.withCrossFade(100))
//                .useAnimationPool(true); // 启用动画池

        glideRequest.into(view);
    }

    public static void loadImageWithFitType(ImageView view, String imageUrl, int fitType, int placeHolder) {
        if (view == null) {
            return;
        }
        imageUrl = transferImageUrl(imageUrl);
        log(imageUrl);
        Context context = view.getContext();
        if(!checkAlive(context)) {
            return;
        }
        GlideRequest<Drawable> request = GlideApp.with(context)
                .load(imageUrl)
//                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .error(placeHolder);
        if(fitType==0) {
            request.centerCrop().into(view);
        }else if(fitType==1) {
            request.centerInside().into(view);
        }else if(fitType==2) {
            request.fitCenter().into(view);
        }else{
            request.into(view);
        }
    }

    @BindingAdapter({"imgUrl", "placeHolder"})
    public static void loadImage(ImageView view, String imageUrl, int placeHolder) {
        if (view == null) {
            return;
        }
        imageUrl = transferImageUrl(imageUrl);
        log(imageUrl);

        Context context = view.getContext();
        if(!checkAlive(context)) {
            return;
        }
        GlideApp.with(context)
                .load(imageUrl)
//                .skipMemoryCache(true)
                .placeholder(placeHolder)
                .error(placeHolder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
                .into(view);
    }

    @BindingAdapter({"imgUrl"})
    public static void loadImageNoPlaceHolder(ImageView view, String imageUrl) {
        if (view == null) {
            return;
        }
        imageUrl = transferImageUrl(imageUrl);
        log(imageUrl);
        Context context = view.getContext();
        if(!checkAlive(context)) {
            return;
        }
        GlideApp.with(context)
                .load(imageUrl)
//                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void loadImage2(ImageView view, String imageUrl, Drawable placeHolder) {
        if (view == null) {
            return;
        }
        imageUrl = transferImageUrl(imageUrl);
        log(imageUrl);

        Context context = view.getContext();
        if(!checkAlive(context)) {
            return;
        }
        GlideApp.with(context)
                .load(imageUrl)
//                .skipMemoryCache(true)
                .placeholder(placeHolder)
                .error(placeHolder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
                .into(view);
    }

    @BindingAdapter({"imgUrl", "placeHolder"})
    public static void loadOriginalImage(ImageView view, String imageUrl, Drawable placeHolder) {
        if (view == null) {
            return;
        }
        Context context = view.getContext();
        if(!checkAlive(context)) {
            return;
        }
        log(imageUrl);

        GlideApp.with(context)
                .load(imageUrl)
//                .skipMemoryCache(true)
                .placeholder(placeHolder)
                .error(placeHolder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
                .into(view);
    }

    @BindingAdapter({"imgUrl", "placeHolder"})
    public static void loadOriginalImage(ImageView view, String imageUrl, int placeHolder) {
        if (view == null) {
            return;
        }
        Context context = view.getContext();
        if(!checkAlive(context)) {
            return;
        }
        log(imageUrl);

        GlideApp.with(context)
                .load(imageUrl)
//                .skipMemoryCache(true)
                .placeholder(placeHolder)
                .error(placeHolder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
                .into(view);
    }

    @BindingAdapter({"imgRoundUrl"})
    public static void loadRoundCornerImage(ImageView view, String imageUrl) {
        if (view == null) {
            return;
        }
        Context context = view.getContext();
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (((Activity) context).isFinishing() || ((Activity) context).isDestroyed()) {
                    return;
                }
            }
        }
        imageUrl = transferImageUrl(imageUrl);
        log(imageUrl);

        GlideApp.with(context)
                .load(imageUrl)
//                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.default_bg_vertical)
                .error(R.drawable.default_bg_vertical)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(UIUtil.dip2px(context, 4))))
                .into(view);

    }

    @BindingAdapter(value = {"imgRoundUrl", "corner"})
    public static void loadRoundCornerImage(ImageView view, String imageUrl, int corner) {
        if (view == null) {
            return;
        }
        imageUrl = transferImageUrl(imageUrl);
        log(imageUrl);

        Context context = view.getContext();
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (((Activity) context).isFinishing() || ((Activity) context).isDestroyed()) {
                    return;
                }
            }
        }
        GlideApp.with(context)
                .load(imageUrl)
//                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.default_bg_vertical)
                .error(R.drawable.default_bg_vertical)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(UIUtil.dip2px(context, corner))))
                .into(view);
    }



    public static void loadResource(ImageView view, int drawable) {
        if (view == null) {
            return;
        }
        Context context = view.getContext();
        if(!checkAlive(context)) {
            return;
        }
        Glide.with(context)
                .load(drawable)
                .into(view);
    }


    public static void preload(Context context, String imageUrl) {
        if(context==null)return;
        if(context instanceof Activity
                && (((Activity)context).isFinishing()||((Activity)context).isDestroyed())){
            return;
        }
        log(imageUrl);

        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .preload();
    }


    public static String transferImageUrl(String imageUrl) {
        if(TextUtils.isEmpty(imageUrl)){
            return imageUrl;
        }
        String url = imageUrl;
        if(url.contains(".jpg")) {
            url = url.replace(".jpg", ".webp");
        }
        if(isWeakNetwork){
            url = url.replace(".webp", "_low.webp");
        }
        return url;
    }

    private static boolean checkAlive(Context context) {
        if(context instanceof Activity){
            return !((Activity)context).isFinishing() && !((Activity)context).isDestroyed();
        }
        return true;
    }

    private static boolean checkAlive(View view){
        if (view == null) {
            return false;
        }
        return checkAlive(view.getContext());
    }

    private static void log(String imageUrl){
        if(GlobalBuildConfig.DEBUG && showGlideLog){
            LogUtil.v(TAG, "loadImage: " + imageUrl);
        }
    }
}
