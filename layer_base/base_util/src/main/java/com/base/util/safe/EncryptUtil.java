package com.base.util.safe;


import android.os.Build;

import com.base.api.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EncryptUtil {


    /**
     *  偏移字符 加密方法，goodnovel 的referrer加密方案
     */
    public static String encodeOffsetChars(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int code = text.charAt(i) + 2; // 每个字符 ASCII +2
            result.append(Integer.toHexString(code));  // 转16进制
        }
        return result.toString();
    }

    /**
     *  偏移字符 解密方法，goodnovel 的referrer参数 逆向还原
     */
    public static String decodeOffsetChars(String hexText) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < hexText.length(); i += 2) {
            String hex = hexText.substring(i, i + 2);
            int code = Integer.parseInt(hex, 16) - 2; // 还原 ASCII -2
            result.append((char) code);
        }
        return result.toString();
    }

    public static String encodeB64(String text){
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // java.util.Base64
                return java.util.Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
            } else {
                // android.util.Base64
                return android.util.Base64.encodeToString(text.getBytes(StandardCharsets.UTF_8), android.util.Base64.NO_WRAP);
            }
        } catch (Exception e) {
            Logger.exception(e);
            return null;
        }
    }

    public static String decodeB64(String str) {
        try {
            byte[] decodedBytes;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // java.util.Base64
                decodedBytes = java.util.Base64.getDecoder().decode(str);
            } else {
                // android.util.Base64
                decodedBytes = android.util.Base64.decode(str, android.util.Base64.NO_WRAP);
            }
            return new String(decodedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            Logger.exception(e);
            return null;  // 解不出来说明不是 Base64
        }
    }

    public static String urlEncode(String text){
        if(text == null || text.isEmpty()) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return URLEncoder.encode(text, StandardCharsets.UTF_8);
        } else {
            try {
                return URLEncoder.encode(text, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Logger.exception(e);
                return "";
            }
        }
    }

    public static String urlDecode(String text){
        if(text == null || text.isEmpty()) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return URLDecoder.decode(text, StandardCharsets.UTF_8);
        } else {
            try {
                return URLDecoder.decode(text, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Logger.exception(e);
                return "";
            }
        }
    }

}
