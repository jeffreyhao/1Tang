package com.baidu.baselibrary.log.process;

import android.text.TextUtils;

import com.baidu.baselibrary.log.LogConfig;
import com.baidu.baselibrary.util.date.DateUtil;
import com.baidu.baselibrary.util.sys.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *  alog文件处理器
 */
class LogFileProcessor {


    /**
     *  alog操作线程
     */
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();


    static void checkNeedDeleteLogs(LogConfig sConfig, String filePath) {
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            return;
        }
        File[] files = parentFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches("^[a-zA-Z]+-[0-9]{4}-[0-9]{2}-[0-9]{2}\\.txt$");
            }
        });
        if (files == null || files.length <= 0) {
            return;
        }
        final int length = filePath.length();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));   // 使用北京时间
        try {
            // /storage/emulated/0/Android/data/com.star.novelhive/cache/log/standard-2024-08-19.txt
            // 2024-08-19
            String curDay = filePath.substring(length - 14, length - 4);
            long dueMillis = sdf.parse(curDay).getTime() - sConfig.mSaveDays * DateUtil.MILLIS_PER_DAY;

            List<String> needDeleteFiles = new ArrayList<>();
            for (final File aFile : files) {
                String name = aFile.getName();
                int l = name.length();
                String logDay = name.substring(l - 14, l - 4);
                if (sdf.parse(logDay).getTime() <= dueMillis) {
                    needDeleteFiles.add(aFile.getName().replace(".txt", ""));
                }
            }

            if (needDeleteFiles.size() > 0) {
                ALogEvent.postOutOfDateLogs("LogFileProcessor", "checkNeedDeleteLogs", needDeleteFiles);
            }

        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    static String getDateFromFileName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        String[] splitNames = fileName.split(LogConst.Sep.HYPHEN_SEP);
        if (splitNames.length > 0) {
            String head = splitNames[0];
            String fileNamePrefix = fileName.replace(".txt", "");
            String date = fileNamePrefix.substring(head.length() + 1);
            return date;
        }
        return "";
    }

    public static void createOrInputFile(LogConfig sConfig, String content, String fullPath) {
        File file = new File(fullPath);
        if (file.exists()) {
            if(file.isFile()){
                EXECUTOR.execute(() -> input2FileSync(content, fullPath));
            } else {
                ALogEvent.postError("LogFileProcessor", "createOrExistsFile", "[" + fullPath + "] exists, but is not file");
                ALogEvent.postContent("LogFileProcessor", "createOrInputFile", content);
            }
        } else {
            EXECUTOR.execute(() -> {
                if(file.exists()){
                    input2FileSync(content, fullPath);
                } else {
                    boolean success = createFileInputContentSync(file, sConfig, content, fullPath);
                    if(!success){
                        ALogEvent.postContent("LogFileProcessor", "createOrInputFile", content);
                    }
                }
            });
        }
    }


    /**
     * 写入日志
     */
    private static boolean input2FileSync(final String content, final String filePath) {
        boolean result = false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filePath, true));
            bw.write(content);
            result = true;
        } catch (Exception e) {
            LogUtil.e(e);
            ALogEvent.postException("LogFileProcessor", "input2FileSync", e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                LogUtil.e(e);
            }
        }
        return result;
    }


    static String getFileName(final StackTraceElement targetElement) {
        String fileName = targetElement.getFileName();
        if (fileName != null) return fileName;
        // If name of file is null, should add
        // "-keepattributes SourceFile,LineNumberTable" in proguard file.
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1];
        }
        int index = className.indexOf('$');
        if (index != -1) {
            className = className.substring(0, index);
        }
        return className + ".java";
    }


    /**
     * 创建文件并写入日志
     * @return 是否写入日志
     */
    static boolean createFileInputContentSync(File file, LogConfig sConfig, String content, final String filePath) {
        File parentFile = file.getParentFile();
        if(parentFile == null){
            ALogEvent.postError("LogFileProcessor", "createFileInputContentSync", "file [" + filePath + "] parent is null");
            return false;
        }
        // mkdirs() Returns: true if and only if the directory was created, along with all necessary parent directories; false otherwise
        if (!createOrExistsDir(parentFile) && !parentFile.exists()) {
            ALogEvent.postError("LogFileProcessor", "createFileInputContentSync", "createOrExistsDir fail: " + parentFile.getPath());
            return false;
        }
        try {
            checkNeedDeleteLogs(sConfig, filePath);
            // createNewFile() Returns: true if the named file does not exist and was successfully created; false if the named file already exists
            boolean isCreate = file.createNewFile();
            if (isCreate || file.exists()) {
                String head = LogContent.getFileHead(sConfig);
                return input2FileSync(head + "\n" + content, filePath);
            } else {
                ALogEvent.postError("LogFileProcessor", "createFileInputContentSync", "[" + filePath + "] createNewFile():false, exists:false");
                return false;
            }
        } catch (Exception e) {
            LogUtil.e(e);
            if (file.exists()) {
                String head = LogContent.getFileHead(sConfig);
                return input2FileSync(head + "\n" + content, filePath);
            } else {
                ALogEvent.postException("LogFileProcessor", "createFileInputContentSync", e);
                return false;
            }
        }
    }

    static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    static String getLogDir(LogConfig sConfig) {
        return sConfig.mDir == null ? sConfig.mDefaultDir : sConfig.mDir;
    }


    static String getLogFileName(String prefix, String date) {
        return prefix + "-" + date + ".txt";
    }

    static String getGlobalFilePrefix(LogConfig sConfig) {
        return sConfig.mFilePrefix;
    }

}
