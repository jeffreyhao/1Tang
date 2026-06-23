package com.meta.bean.model.glide;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.benefit.novelverse.R;
import com.baidu.baselibrary.Widget;

/**
 * Glide 工具类
 */
public final class GlideUtils {
    // 图片缓存子目录
    public static final String GLIDE_CACHE_DIR = "image_catch";

    public static RequestOptions getDefaultOptions() {
        return new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_bg_vertical)
                .error(R.drawable.default_bg_vertical);
    }

    public static RequestOptions getOptions(int placeholder) {
        return new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder);
    }

    public static void loadImage(Object model, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        Glide.with(Widget.getContext()).load(model)
                .apply(getDefaultOptions())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    public static void loadImage(Object model, ImageView imageView, int placeholder) {
        if (imageView == null) {
            return;
        }
        Glide.with(Widget.getContext()).load(model)
                .apply(getOptions(placeholder))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }
}
