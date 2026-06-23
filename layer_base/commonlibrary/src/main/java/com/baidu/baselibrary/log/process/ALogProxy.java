package com.baidu.baselibrary.log.process;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.log.IFormatter;
import com.baidu.baselibrary.log.LogConfig;
import com.baidu.baselibrary.log.annotate.LogFilePrefix;
import com.baidu.baselibrary.log.annotate.LogLevel;
import com.baidu.baselibrary.log.annotate.LogTag;
import com.baidu.baselibrary.log.annotate.LogType;

/**
 * Created by haojiangfeng on 2024/6/12.
 */
public class ALogProxy {


    public static void console(LogConfig sConfig, String dirName, final String prefix, final int type, @LogLevel int level, final String tag, final Object... contents) {
        try {
            if (!sConfig.mLogSwitch && !sConfig.mLog2ConsoleSwitch && !sConfig.mLog2FileSwitch) {
                return;
            }
            final LogContent.TagHead tagHead = LogContent.processTagAndHead(sConfig, tag);
            String body = LogContent.processBody(type, contents);

            if (sConfig.mLog2ConsoleSwitch && level >= sConfig.mConsoleFilter) {
                LogConsole.print2Console(sConfig, level, tag, tagHead.consoleHead, "", "", body);
            }
            if (sConfig.mLog2FileSwitch && level >= sConfig.mFileFilter) {
//                LogFileProcessor.print2File(sConfig, dirName, prefix, level, tag, tagHead.fileHead, body);
                LogStandard.print2File(sConfig, prefix, dirName, level, LogType.TEXT, "", "", new String[]{LogTag.OLD_ALOG, tag}, LogFormatter.formatString(contents));
            }
        } catch (Exception e) {
            ALog.exception("ALogProxy", "console", e);
        }
    }


    /**
     *
     * @param sConfig       Log配置
     * @param level         Log展示级别
     * @param filePrefix    文件后缀
     * @param logType       log类型
     * @param className     所在类名
     * @param methodName    所在方法名
     * @param tags          三层标签
     * @param object        日志内容bean
     */
    public static void standard(LogConfig sConfig, @LogLevel int level, @LogFilePrefix String filePrefix, @LogType int logType, String className, String methodName, String[] tags, Object object) {
        try{
            if (!sConfig.mLogSwitch){
                return;
            }
            String dirName = sConfig.mDirName;

            if (sConfig.mLog2ConsoleSwitch && level >= sConfig.mConsoleFilter) {
                LogConsole.print2Console(sConfig, level, LogContent.tag(tags), null, className, methodName, object);
            }
            if (sConfig.mLog2FileSwitch && level >= sConfig.mFileFilter) {
                LogStandard.print2File(sConfig, filePrefix, dirName, level, logType, className, methodName, tags, object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String content = LogStandard.getStandardContent(level, logType, className, methodName, tags, object);
            ALogEvent.postContent("ALogProxy", "standard", content);
            ALogEvent.postException("ALogProxy", "standard", e);
        }
    }


    public static String getLogDir(LogConfig sConfig) {
        return LogFileProcessor.getLogDir(sConfig);
    }

    public static boolean isSpace(String s) {
        return LogFileProcessor.isSpace(s);
    }


    public static <T> void addFormatter(final IFormatter<T> iFormatter) {
        if (iFormatter != null) {
            LogFormatter.I_FORMATTER_MAP.put(LogFormatter.getTypeClassFromInterface(iFormatter), iFormatter);
        }
    }



}
