package com.benefit.utils;

import android.text.TextUtils;

import com.baidu.baselibrary.log.ALog;
import com.tencent.common.util.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import okhttp3.ResponseBody;

/**
 * 文件存储控制类
 */
public class PPFileUtils {
    public static final int XOR_KEY = 120;

    /**
     * 复制文件
     *
     * @param fromPathName
     * @param toPathName
     * @return
     */
    public static int copy(String fromPathName, String toPathName) {
        try {
            InputStream from = new FileInputStream(fromPathName);
            return copy(from, toPathName);
        } catch (FileNotFoundException e) {
            return -1;
        }
    }

    /**
     * 复制文件
     *
     * @param from
     * @param toPathName
     * @return
     */
    public static int copy(InputStream from, String toPathName) {
        try {
            PPFileUtils.delete(toPathName);
            OutputStream to = new BufferedOutputStream(new FileOutputStream(toPathName));
            byte buf[] = new byte[1024];
            int c;
            while ((c = from.read(buf)) > 0) {
                to.write(buf, 0, c);
            }
            from.close();
            to.close();
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * 删除文件
     */
    public static void delete(String filePathName) {
        try{
            if (TextUtils.isEmpty(filePathName)) return;
            File file = new File(filePathName);
            if (file.isFile() && file.exists()) {
                boolean flag = file.delete();
            }
        }catch (Exception e){

        }

    }

    public static void delete(File file) {
        try {
            if (file == null) return;
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    f.delete();
                }
                file.delete();//如要保留文件夹，只删除文件，请注释这行
            } else if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createDirWithFile(String path) {
        File file = new File(path);
        if (!path.endsWith("/")) {
            file = file.getParentFile();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static boolean fileIsExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static File saveFile(File file, byte[] b) {
        BufferedOutputStream stream = null;
        try {
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public static boolean saveFile(File file, String content) {
        BufferedOutputStream stream = null;
        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(content.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    private static boolean saveFile(File file, ResponseBody responseBody) {
        if(responseBody==null) {
            ALog.textSingle("PPFileUtils", "saveFile", "responseBody is null");
            return false;
        }
        FileOutputStream fstream = null;
        InputStream inputStream = null;
        try {
            fstream = new FileOutputStream(file);
            inputStream = responseBody.byteStream();
            byte[] buffer = new byte[1024];
            int read;
            while((read = inputStream.read(buffer))!=-1){
                fstream.write(buffer,0, read);
            }
            return true;
        } catch (Exception e) {
            ALog.exception("PPFileUtils", "saveFile", e);
        } finally {
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static String readFile(File file) {
        if(file==null||!file.exists()) return "";
        String content = "";
        byte[] bytes = readContentIntoByteArray(file);
        if(bytes!=null){
            byte[] decryptContent = decryptXOR(bytes);
            if(decryptContent!=null&&decryptContent.length>0){
                content = uncompressToString(decryptContent);
            }
        }
        return content;
    }

    public static String readChapterCompressContent(byte[] bytes) {
        if(bytes==null||bytes.length==0) return "";
        byte[] decryptContent = decryptXOR(bytes);
        if(decryptContent!=null&&decryptContent.length>0){
            return uncompressToString(decryptContent);
        }
        return "";
    }

    public static String uncompressToString(byte[] bytes) {
        String content = "";
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            content = out.toString("UTF-8");
            ungzip.close();
        } catch (IOException e) {
            ALog.exception("PPFileUtils", "uncompressToString", e);
            uploadExceptionMsg(e, "", bytes.length);
            return "";
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return content;
    }

    private static byte[] readContentIntoByteArray(File file) {
        if(file==null||!file.exists()) return null;
        FileInputStream fileInputStream;
        byte[] bFile = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            ALog.exception("PPFileUtils", "readContentIntoByteArray", e);
            uploadExceptionMsg(e,file.getAbsolutePath(),0);
            return null;
        }
        return bFile;
    }

    public static byte[] decryptXOR(byte[] data) {
        if(data==null) return null;
        byte[] result = new byte[data.length];
        // 使用密钥字节数组循环加密或解密
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ XOR_KEY);
        }
        return result;
    }


    private static void uploadExceptionMsg(Exception e, String filePath, long byteLength){
        String msg = TextUtils.isEmpty(e.getMessage())? getStackTrace(e) : e.getClass().getSimpleName() + "==>" + e.getMessage();
        String fileSize = "unKnown";
        if(!TextUtils.isEmpty(filePath)){
            fileSize = FileUtils.getFileSize(filePath);
        }else if(byteLength>0){
            fileSize = getByteLength(byteLength);
        }
//        SensorsConfig.INSTANCE.fileParseException(msg, fileSize);
        ALog.textSingle("PPFileUtils", "uploadExceptionMsg", "fileSize===>"+fileSize);
    }

    private static String getStackTrace(Exception e) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        String msg = "";
        if(buffer!=null){
            msg = buffer.toString();
        }
        return e.getClass().getSimpleName() + "==>" + msg;
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    private static String getByteLength(long bytes) {
        float returnValue = 0f;
        try{
            BigDecimal fileSize = new BigDecimal(bytes);
            BigDecimal megabyte = new BigDecimal(1024 * 1024);
            returnValue = fileSize.divide(megabyte, 2, RoundingMode.UP)
                    .floatValue();
            if (returnValue > 1)
                return returnValue + "MB";
            BigDecimal kilobyte = new BigDecimal(1024);
            returnValue = fileSize.divide(kilobyte, 2, RoundingMode.UP)
                    .floatValue();
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnValue + "KB";
    }




    public static void gzipFolder(File sourceFile, String destinationFile) throws Exception {
        GZIPOutputStream gzipOutputStream = null;
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            // 创建GZIPOutputStream来写入压缩文件
            gzipOutputStream = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(destinationFile)));
            fileInputStream = new FileInputStream(sourceFile);
            bufferedInputStream = new BufferedInputStream(fileInputStream);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                gzipOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (Throwable e){
            ALog.exception("PPFileUtils", "gzipFolder", e);
        } finally {
            // 关闭压缩流
            if(gzipOutputStream != null){
                gzipOutputStream.close();
            }
            if(fileInputStream != null){
                fileInputStream.close();
            }
            if(bufferedInputStream != null){
                bufferedInputStream.close();
            }
        }
    }



}
