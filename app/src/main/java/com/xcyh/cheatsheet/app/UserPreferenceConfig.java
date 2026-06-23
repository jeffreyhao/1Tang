package com.xcyh.cheatsheet.app;

import java.util.HashSet;
import java.util.Set;

/**
 * Time: 2024/3/28
 * Author: lhc
 * Desc:
 */
public class UserPreferenceConfig {
    public static boolean recharge_login = true;
    /**
     * 用户取消购买章节登录提示弹窗所在书籍id
     */
    public static Set<String> cancelPayChapterList = new HashSet<>();

}
