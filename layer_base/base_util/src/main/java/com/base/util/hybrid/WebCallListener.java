package com.base.util.hybrid;


import androidx.annotation.Keep;

/**
 * SO回调
 */
@Keep
public interface WebCallListener {

    /**
     * 日志打印回调
     */
    @Keep
    void hybrid(String message);
}
