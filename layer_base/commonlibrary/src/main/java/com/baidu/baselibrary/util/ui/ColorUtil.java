package com.baidu.baselibrary.util.ui;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

/**
 * Created by haojiangfeng on 2024/8/29.
 */
public class ColorUtil {


    public final static float NIGHT_PERCENT_DIM 				= 0.6f; // 夜间模式默认不透明度 百分比



    /**
     * 获取用于夜间模式的 ColorFilter
     *
     * @return
     */
    public static ColorMatrixColorFilter getNightModeColorFilter() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{
                1 - ColorUtil.NIGHT_PERCENT_DIM, 0, 0, 0, 0,
                0, 1 - ColorUtil.NIGHT_PERCENT_DIM, 0, 0, 0,
                0, 0, 1 - ColorUtil.NIGHT_PERCENT_DIM, 0, 0,
                0, 0, 0, 1f, 0
        });
        return new ColorMatrixColorFilter(colorMatrix);
    }


    /**
     * 将颜色整数转换为16进制字符串
     * @param color 颜色整数
     * @return 16进制字符串，格式为 #AARRGGBB
     */
    public static String colorToHex(int color) {
        return String.format("#%08X", color);
    }


    /**
     * 计算两个颜色值按照百分比变换过程中的中间值
     *
     * @param startColor 起始颜色值
     * @param endColor   目标颜色值
     * @param percentage 变换的百分比，范围从0到1
     * @return 中间颜色值
     */
    public static int calculateIntermediateColor(int startColor, int endColor, float percentage) {
        int startA = Color.alpha(startColor);
        int startR = Color.red(startColor);
        int startG = Color.green(startColor);
        int startB = Color.blue(startColor);

        int endA = Color.alpha(endColor);
        int endR = Color.red(endColor);
        int endG = Color.green(endColor);
        int endB = Color.blue(endColor);

        int intermediateA = (int) (startA + (endA - startA) * percentage);
        int intermediateR = (int) (startR + (endR - startR) * percentage);
        int intermediateG = (int) (startG + (endG - startG) * percentage);
        int intermediateB = (int) (startB + (endB - startB) * percentage);

        return Color.argb(intermediateA, intermediateR, intermediateG, intermediateB);
    }




}
