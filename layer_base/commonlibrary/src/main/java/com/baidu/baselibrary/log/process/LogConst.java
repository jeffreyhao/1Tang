package com.baidu.baselibrary.log.process;


import com.baidu.baselibrary.log.annotate.LogType;


/**
 *  一些常量
 */
public class LogConst {


    public static class Sep {

        /**
         * 文件分隔符：file.separator。 在 Windows 上是\，在 Linux 和 macOS 上是/。
         */
        public static final String FILE_SEP = System.getProperty("file.separator");

        /**
         * 路径分隔符：path.separator。 在 Windows 上是;，在 Linux 和 macOS 上是:。
         */
        public static final String PATH_SEP = System.getProperty("path.separator");

        /**
         * 行分隔符：line.separator。 在 Windows 上是\r\n，在 Linux 和 macOS 上是\n。
         */
        public static final String LINE_SEP = System.getProperty("line.separator");

        /**
         *  管道符号：｜。 用来分割日志通用参数
         */
        public static final String PIP_SEP = "|";

        /**
         *  标点符号点： .
         */
        public static final String DOT_SEP = ".";


        /**
         *  连字符： -
         */
        public static final String HYPHEN_SEP = "-";
    }

    public static String typeString(@LogType int logType){
        switch (logType){
            case LogType.TEXT:
                return "TEXT";
            case LogType.JSON:
                return "JSON";
            case LogType.XML:
                return "XML";
            case LogType.EXCEPTION:
                return "EXCEPTION";
            case LogType.FILE:
                return "FILE";
            case LogType.STACKTRACE:
                return "STACKTRACE";
            default:
                return "UNKNOWN";
        }
    }

    public static final String NULL = "null";


    public static final char[] T = new char[]{'V', 'D', 'I', 'W', 'E', 'A'};



}
