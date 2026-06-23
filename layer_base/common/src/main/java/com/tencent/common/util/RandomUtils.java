package com.tencent.common.util;


import java.util.Random;

/**
 * 随机数工具类
 * @author adison
 * @date 2017/5/30
 * @time 上午1:48
 */
public final class RandomUtils {
    private static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS             = "0123456789";
    private static final String LETTERS             = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CAPITAL_LETTERS     = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE_LETTERS  = "abcdefghijklmnopqrstuvwxyz";

    private RandomUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取一个固定长度和的字符串(包括随机数字和字母)
     * @param length
     * @return
     */
    public static String getRandomNumbersAndLetters(int length) {
        return getRandom(NUMBERS_AND_LETTERS, length);
    }


    /**
     * 获取一个固定长度和的随机数字字符串
     *
     * @param length
     * @return
     */
    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }

    /**
     * 获取一个固定长度和的随机字母字符串
     * @param length
     * @return
     */
    public static String getRandomLetters(int length) {
        return getRandom(LETTERS, length);
    }


    /**
     * 获取一个固定长度和的随机大写字母字符串
     * @param length
     * @return
     */
    public static String getRandomCapitalLetters(int length) {
        return getRandom(CAPITAL_LETTERS, length);
    }

    /**
     * 获取一个固定长度和的随机小写字母字符串
     * @param length
     * @return
     */
    public static String getRandomLowerCaseLetters(int length) {
        return getRandom(LOWER_CASE_LETTERS, length);
    }

    /**
     * 获取一个固定长度的随机字符串(以source中的字符组合)
     * @param source
     * @param length
     * @return
     */
    public static String getRandom(String source, int length) {
        return source == null ? null : getRandom(source.toCharArray(), length);
    }


    /**
     * 获取一个固定长度的随机字符串(以sourceChar中的字符组合)
     * @param sourceChar
     * @param length
     * @return
     */
    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }

        StringBuilder str = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    /**
     * 获取一个在0和max之间的数字
     * <p>
     *      if max <= 0, return 0
     * </p>
     * @param max
     * @return
     */
    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    /**
     * 获取一个在min和max之间的数字
     * <p>
     *    if min > max, return 0
     *    if min == max, return min
     * </p>
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        if (min > max) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + new Random().nextInt(max - min);
    }
}
