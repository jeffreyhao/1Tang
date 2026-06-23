package com.benefit.listener;


import java.io.File;

/**
 * Time: 2024/1/3
 * Author: lhc
 * Desc:
 */
public interface UploadBucketResultListener {
    void onUploadResult(boolean success, File file, int retryIndex, String message);

}
