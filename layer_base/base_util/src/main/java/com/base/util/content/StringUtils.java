package com.base.util.content;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.base.api.GlobalContext;
import com.base.api.Logger;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 字符串相关工具类
 * @author adison
 * @date 2017/5/20
 * @time 下午5:33
 */
public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }


    /**
     * 判断是否为空 并且不为NULL , null
     * @param str
     * @return
     */
    public static boolean isEmptyNull(String str) {
        boolean isEmpty = isEmpty(str);
        if(!isEmpty) {
            isEmpty = str.equalsIgnoreCase("null");
        }
        return isEmpty;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isTrimEmpty(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isSpace(String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 截取字符串（包含中文、Emoji表情）
     * @param message
     * @param byteLimit
     * @return
     */
    public static String truncateTextByByteLimit(String message, int byteLimit) {
        String result = "";
        try {
            Charset utf8Charset = Charset.forName("UTF-8");
            CharsetDecoder cd = utf8Charset.newDecoder();
            byte[] utf8Bytes = message.getBytes(utf8Charset);
//            System.out.println("check message: " + message + " /length: " +message.length()+ " //byte length: " + utf8Bytes.length + "/limit: " + byteLimit + " /codePoint: " +message.codePointCount(0, message.length()));
            ByteBuffer bb = ByteBuffer.wrap(utf8Bytes, 0, Math.min(utf8Bytes.length, byteLimit));
            CharBuffer cb = CharBuffer.allocate(byteLimit);
            // Ignore an incomplete character
            cd.onMalformedInput(CodingErrorAction.IGNORE);
            cd.decode(bb, cb, true);
            cd.flush(cb);
            result = new String(cb.array(), 0, cb.position());
            if (result.length()<=0) {
                return truncateTextByByteLimit(message, (byteLimit+1));
            } else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return message;
        }
    }


    /**
     * emoji表情替换
     *
     * @param source 原字符串
     * @param slipStr emoji表情替换成的字符串
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source,String slipStr) {
        if(!isTrimEmpty(source)){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        }else{
            return source;
        }
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }



    // ArrayList类型转成String类型
    public static String list2String(List<String> arrayList) {
        String result = "";
        if (arrayList != null && arrayList.size() > 0) {
            for (String item : arrayList) {
                // 把列表中的每条数据用逗号分割开来，然后拼接成字符串
                result += item + ",";
            }
            // 去掉最后一个逗号
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static List<String> string2List(String text) {
        if(TextUtils.isEmpty(text)){
            return new ArrayList<>();
        }
        String[] bookIds = text.split(",");
        return new ArrayList<>(Arrays.asList(bookIds));
    }


    /**
     * 对字符串进行抑或加密
     */
    public static String getOrEncryptionText(String content, int encryptKey){
        if(TextUtils.isEmpty(content)){
            return content;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < content.length(); i++) {
            char c = (char)(content.charAt(i) ^ encryptKey);
            sb.append(c);
        }
        return sb.toString();
    }

    public static boolean different(String value1, String value2){
        if(value1 == null && value2 == null){
            return false;
        }
        if(value1 != null && value1.equals(value2)){
            return false;
        }
        return true;
    }

    public static String nonNull(String text){
        return text == null ? "" : text;
    }

    public static CharSequence nonNull(CharSequence text){
        return text == null ? "" : text;
    }

    public static String toNonnullString(CharSequence text){
        return text == null ? "" : text.toString();
    }

    /**
     *  首字母大写
     */
    public static String capitalize(String str) {
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

}