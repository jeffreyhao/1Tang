package com.base.util.hybrid;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.EditText;

import androidx.annotation.Keep;

import com.base.api.GlobalContext;
import com.base.api.Logger;
import com.base.util.content.ClipBoardFocus;
import com.base.util.content.StringUtils;
import com.base.util.content.TextMatcher;


@Keep
public class ClipBoardUtil {


    /**
     * 获取剪贴板中的文字
     */
    public static String getTextFromClip() {
        try {
            ClipboardManager clipboardManager = (ClipboardManager) GlobalContext.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            //判断剪切版时候有内容
            if(clipboardManager == null || !clipboardManager.hasPrimaryClip()){
                return null;
            }
            ClipData clipData = clipboardManager.getPrimaryClip();
            //获取 lable
            if (clipData != null && clipData.getItemCount() > 0 && clipData.getItemAt(0).getText() != null) {
                //获取 text
                return clipData.getItemAt(0).getText().toString();
            }
        } catch (Throwable e){
            Logger.exception("ClipBoardUtil", "getTextFromClip", e);
        }
        return null;
    }

    /**
     * 拷贝文档到剪贴板
     */
    public static void copyText(Context ctx,String label, String content) {
        ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, content);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * 复制
     *
     * @param data 待复制文本
     */
    public static void copy(String data) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) GlobalContext.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
        // newHtmlText、
        // newIntent、
        // newUri、
        // newRawUri
        ClipData clipData = ClipData.newPlainText(null, data);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }


    public static String getMatchedClipText(TextMatcher textMatcher) {
        try {
            ClipboardManager clipboardManager = (ClipboardManager) GlobalContext.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if(clipboardManager == null || !clipboardManager.hasPrimaryClip()){  // 判断剪切版时候有内容
                return null;
            }
            ClipData clipData = clipboardManager.getPrimaryClip();
            if(clipData == null || clipData.getItemCount() == 0){
                return null;
            }
            if(textMatcher == null){
                CharSequence clipText = clipData.getItemAt(0).getText();
                return StringUtils.nonNull(clipText).toString();
            }
            for(int i = 0; i < clipData.getItemCount(); i++){
                ClipData.Item item = clipData.getItemAt(i);
                if(item.getText() != null && textMatcher.match(item.getText())){
                    return item.getText().toString();
                }
            }
        } catch (Throwable e){
            Logger.exception("ClipBoardUtil", "getMatchedClipText", e);
        }
        return null;
    }


    public static void checkClipBoard(Activity activity, EditText editText, boolean isInit, Runnable runnable){
        if(activity == null || activity.getWindow() == null || activity.isDestroyed() || activity.isFinishing()){
            Logger.textSingle("ClipBoardUtil", "checkClipBoard", "return: activity or widow is null, or activity is finish.");
            if(runnable != null) {
                runnable.run();
            }
            return;
        }
        Logger.textSingle("checkClipBoard(), activity: " + activity);
//        Logger.printStackTrace();

        ClipBoardFocus.newInstance(activity, editText, isInit).start(runnable);
    }



}
