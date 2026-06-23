package com.tencent.common.util;

import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Created by adison on 2017/7/20.
 */

public class DrawableUtils {

    private DrawableUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * tint source with color, compat api before lollipop(API 21)
     * */
    public static Drawable tint(@NonNull Drawable source, @ColorInt int color) {
        final Drawable tintDrawable = DrawableCompat.wrap(source);
        DrawableCompat.setTint(tintDrawable, color);
        return tintDrawable;
    }

    /**
     * tint source with color, compat api before lollipop(API 21)
     * */
    public static Drawable tint(@NonNull int drawable, @ColorInt int color) {
        final Drawable tintDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(Abase.getContext(),drawable));
        DrawableCompat.setTint(tintDrawable, color);
        return tintDrawable;
    }
}
