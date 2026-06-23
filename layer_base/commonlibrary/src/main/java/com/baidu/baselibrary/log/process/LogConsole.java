package com.baidu.baselibrary.log.process;

import android.util.Log;

import com.baidu.baselibrary.log.LogConfig;

/**
 *  控制台相关
 */
class LogConsole {


    private static final String TOP_CORNER = "┌";
    private static final String MIDDLE_CORNER = "├";
    private static final String LEFT_BORDER = "│ ";
    private static final String BOTTOM_CORNER = "└";
    private static final String SIDE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String MIDDLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_CORNER + SIDE_DIVIDER + SIDE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + MIDDLE_DIVIDER + MIDDLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_CORNER + SIDE_DIVIDER + SIDE_DIVIDER;

    private static final int MAX_LEN            = 4000;

    private static final String PLACEHOLDER     = " ";


    static void print2Console(LogConfig sConfig,
                              final int type,
                              final String tag,
                              final String[] head,
                              final String className,
                              final String methodName,
                              final Object object) {
        String msg = LogFormatter.formatObject(object);
        if (sConfig.mSingleTagSwitch) {
            printSingleTagMsg(sConfig, type, tag, processSingleTagMsg(sConfig, type, tag, head, className, methodName, msg));
        } else {
            printBorder(sConfig, type, tag, true);
            printHead(sConfig, type, tag, className, methodName, head);
            printMsg(sConfig, type, tag, className, methodName, msg);
            printBorder(sConfig, type, tag, false);
        }
    }

    private static void printSingleTagMsg(LogConfig sConfig, final int type, final String tag, final String msg) {
        int len = msg.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {
            if (sConfig.mLogBorderSwitch) {
                Log.println(type, tag, msg.substring(0, MAX_LEN) + LogConst.Sep.LINE_SEP + BOTTOM_BORDER);
                int index = MAX_LEN;
                for (int i = 1; i < countOfSub; i++) {
                    Log.println(type, tag, PLACEHOLDER + LogConst.Sep.LINE_SEP + TOP_BORDER + LogConst.Sep.LINE_SEP
                            + LEFT_BORDER + msg.substring(index, index + MAX_LEN)
                            + LogConst.Sep.LINE_SEP + BOTTOM_BORDER);
                    index += MAX_LEN;
                }
                if (index != len) {
                    Log.println(type, tag, PLACEHOLDER + LogConst.Sep.LINE_SEP + TOP_BORDER + LogConst.Sep.LINE_SEP
                            + LEFT_BORDER + msg.substring(index, len));
                }
            } else {
                Log.println(type, tag, msg.substring(0, MAX_LEN));
                int index = MAX_LEN;
                for (int i = 1; i < countOfSub; i++) {
                    Log.println(type, tag,
                            PLACEHOLDER + LogConst.Sep.LINE_SEP + msg.substring(index, index + MAX_LEN));
                    index += MAX_LEN;
                }
                if (index != len) {
                    Log.println(type, tag, PLACEHOLDER + LogConst.Sep.LINE_SEP + msg.substring(index, len));
                }
            }
        } else {
            Log.println(type, tag, msg);
        }
    }

    private static String processSingleTagMsg(LogConfig sConfig,
                                              final int type,
                                              final String tag,
                                              final String[] head,
                                              final String className,
                                              final String methodName,
                                              final String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(PLACEHOLDER).append(LogConst.Sep.LINE_SEP);
        if (sConfig.mLogBorderSwitch) {
            sb.append(TOP_BORDER).append(LogConst.Sep.LINE_SEP);
            if(tag != null || className != null || methodName != null) {
                String appendClass = className == null ? "-" : className;
                String appendMethod = methodName == null ? "-()" : (methodName.endsWith("()") ? methodName : methodName + "()");
                sb.append(LEFT_BORDER).append(appendClass).append(".").append(appendMethod).append("   ").append(tag).append(LogConst.Sep.LINE_SEP);
                sb.append(MIDDLE_BORDER).append(LogConst.Sep.LINE_SEP);
            }
            if (head != null) {
                for (String aHead : head) {
                    sb.append(LEFT_BORDER).append(aHead).append(LogConst.Sep.LINE_SEP);
                }
                sb.append(MIDDLE_BORDER).append(LogConst.Sep.LINE_SEP);
            }
            for (String line : msg.split(LogConst.Sep.LINE_SEP)) {
                sb.append(LEFT_BORDER).append(line).append(LogConst.Sep.LINE_SEP);
            }
            sb.append(BOTTOM_BORDER);
        } else {
            if (head != null) {
                for (String aHead : head) {
                    sb.append(aHead).append(LogConst.Sep.LINE_SEP);
                }
            }
            sb.append(msg);
        }
        return sb.toString();
    }



    private static void printBorder(LogConfig sConfig, final int type, final String tag, boolean isTop) {
        if (sConfig.mLogBorderSwitch) {
            Log.println(type, tag, isTop ? TOP_BORDER : BOTTOM_BORDER);
        }
    }

    private static void printHead(LogConfig sConfig, final int type, final String tag, String className, String methodName, final String[] head) {
        if (head != null) {
            for (String aHead : head) {
                Log.println(type, tag, sConfig.mLogBorderSwitch ? LEFT_BORDER + aHead : aHead);
            }
            if (sConfig.mLogBorderSwitch) Log.println(type, tag, MIDDLE_BORDER);
        }
    }

    private static void printMsg(LogConfig sConfig, final int type, final String tag, String className, String methodName, final String msg) {
        int len = msg.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {
                printSubMsg(sConfig, type, tag, className, methodName, msg.substring(index, index + MAX_LEN));
                index += MAX_LEN;
            }
            if (index != len) {
                printSubMsg(sConfig, type, tag, className, methodName, msg.substring(index, len));
            }
        } else {
            printSubMsg(sConfig, type, tag, className, methodName, msg);
        }
    }


    private static void printSubMsg(LogConfig sConfig, final int type, final String tag, String className, String methodName, final String msg) {
        if (!sConfig.mLogBorderSwitch) {
            Log.println(type, tag, className + "." + methodName + ", " + msg);
            return;
        }
        Log.println(type, tag, LEFT_BORDER + " " + className + "." + methodName + "()\n");

        String[] lines = msg.split(LogConst.Sep.LINE_SEP);
        for (String line : lines) {
            Log.println(type, tag, LEFT_BORDER + line);
        }
    }


}
