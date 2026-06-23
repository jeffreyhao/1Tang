package com.base.util.sys;

import android.os.Environment;
import android.text.TextUtils;

import com.base.api.GlobalContext;
import com.base.api.Logger;
import com.base.util.content.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件简单操作辅助类
 */
public class FileUtil {

	public final static int BYTE_IN_SIZE 		= 4096;

	public static final String FILE_ZIP_EXT			= "zip";			// zip文件后缀 zip
	public static final String FILE_ZIP_DOT_EXT		= ".zip";			// zip文件后缀 .zip

	public static final String FILE_RMD_INFO_EXT	= "json";			// 推荐信息描述文件名后缀 rmdinfo
	public static final String FILE_RMD_INFO_DOT_EXT= ".json";			// 推荐信息描述文件名后缀.rmdinfo

	public static final String FILE_TEMP_EXT		= "tmp";
	public static final String FILE_TEMP_DOT_EXT	= ".tmp";
	public static final String FILE_DEL_EXT			= ".del";
    public final static String APK_SUFIX_EXT        = "i";                  // apk文件扩展符 为了防止第三软件清理掉我们下载的apk
    public final static String APK_SUFIX_I          = "apk"+APK_SUFIX_EXT;  // 我们识别的APK后缀13717547184
    public final static String APK_SUFIX            = "apk";
    public static final String[] APKS 				= new String[]{APK_SUFIX_I,APK_SUFIX};

	/**
	 * 判断是否为文件
	 */
	public final static boolean isFile(String fileName){
		if(StringUtils.isEmptyNull(fileName)) return false;
		File file=new File(fileName);
		return file!=null&&file.isFile();
	}

    /**
	 * 判断是否为apk
	 */
	public final static boolean isApk(String fileName) {
		if(StringUtils.isEmptyNull(fileName)) return false;
		String sufix = getExt(fileName).toLowerCase();
		return Arrays.asList(APKS).contains(sufix);
	}
	/**
	 * 关闭流
	 */
	public final static void close(Closeable closeable) {
		try {
			if(closeable != null)closeable.close();
		} catch (Throwable e) {
			Logger.e(e);
		}
	}

	/**
	 * 文件是否存在
	 */
	public static boolean isExist(String filePathName) {
		if(StringUtils.isEmptyNull(filePathName)) return false;
		File file = new File(filePathName);
		return (!file.isDirectory() && file.exists());
	}

	/**
	 * 目录是否存在
	 */
	public static boolean isDirExist(String filePathName) {
		if(StringUtils.isEmptyNull(filePathName)) return false;
		if(!filePathName.endsWith("/")) filePathName +="/";
		File file = new File(filePathName);
		return (file.isDirectory() && file.exists());
	}

	/**
	 * 创建目录，整个路径上的目录都会创建
	 */
	public static File createDirWithFile(String path) {
		File file = new File(path);
		if(!path.endsWith("/")){
			file = file.getParentFile();
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 获取路径，不带文件名，末尾带'/'
	 */
	public static String getPath(String filePathName) {
		try {
			return filePathName.substring(0, filePathName.lastIndexOf('/') + 1);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取目录的名称 注意：只能获取如：/aaaa/ssss/ 或 /aaaa/dsddd
	 */
	public static String getDirPathName(String filePathName) {
		try {
			if(filePathName.endsWith("/"))
				filePathName = filePathName.substring(0, filePathName.lastIndexOf('/'));
			return filePathName.substring(filePathName.lastIndexOf("/") + 1, filePathName.length());
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 获取文件名，带后缀
	 */
	public static String getName(String filePathName) {
		try {
			return filePathName.substring(filePathName.lastIndexOf('/') + 1);
		} catch (Exception e) {
			return "";
		}
	}

	public static void deleteDirectorySafe(File file) {
		if(file==null||!file.isDirectory()) {
			return;
		}
		File[] paths = file.listFiles();
		if(paths != null) {
			for (File pathF : paths) {
				if (pathF.isDirectory()) {
					deleteDirectorySafe(pathF);
				} else {
					deleteFileSafe(pathF);
				}
			}
		}
		deleteFileSafe(file);
	}


	/**
	 * 为防止创建一个正在被删除的文件夹，所以在删除前先重命名该文件夹
	 * @param file
	 */
	public static void deleteFileSafe(File file){
		if(file==null||!file.exists())return;
		String newPathName=file.getAbsolutePath() + System.currentTimeMillis();
		File to = new File(newPathName);
		boolean renameSuccess=rename(file.getAbsolutePath(),newPathName);
		if (!renameSuccess) {
			final String message =
					"Unable to rename file " + file.getAbsolutePath();
			Logger.e(message);
		}
		if (!to.delete()) {
			final String message =
					"Unable to delete file " + to.getAbsolutePath() + ", isHidden="+to.isHidden()+
							", exists="+to.exists()+ ", canRead="+to.canRead()+ ", canWrite="+to.canWrite();
			Logger.e(message);
		}
	}

	/**
	 * 为防止创建一个正在被删除的文件夹，所以在删除前先重命名该文件夹
	 * @param filePath
	 */
	public static void deleteFileSafe(String filePath){
		if(TextUtils.isEmpty(filePath)) return ;
		File file = new File(filePath);
		if(!file.exists())return;
		String newPathName=file.getAbsolutePath() + System.currentTimeMillis();
		File to = new File(newPathName);
		boolean renameSuccess=rename(file.getAbsolutePath(),newPathName);
		if (!renameSuccess) {
			final String message =
					"Unable to rename file " + file.getAbsolutePath();
			Logger.e(message);
		}
		if (!to.delete()) {
			final String message =
					"Unable to delete file " + to.getAbsolutePath() + ", isHidden="+to.isHidden()+
							", exists="+to.exists()+ ", canRead="+to.canRead()+ ", canWrite="+to.canWrite();
			Logger.e(message);
		}
	}

	/**
	 * 获取文件名，不带后缀
	 */
	public static String getNameNoPostfix(String filePathName) {
		try {
			return filePathName.substring(filePathName.lastIndexOf('/') + 1, filePathName.lastIndexOf('.'));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取文件扩展名
	 */
	public static String getExt(String filePathName){
		if(filePathName == null) return null;
		final int index = filePathName.lastIndexOf('.');
		return (index != -1) ? filePathName.substring(index + 1).toLowerCase().intern() : "";
	}


	/**
	 * 获取文件最后修改时间
	 * @param filePathName
	 * @return 文件不存在直接返回-1,else return milliseconds since January 1st, 1970
	 */
	public static long getLastModified(String filePathName){
		File file = new File(filePathName);
		if(file != null && file.exists()){
			return file.lastModified();
		}else{
			return -1;
		}
	}

	/**
	 * 重命名
	 * @param filePathName
	 * @param newPathName
	 */
	public static boolean rename(String filePathName, String newPathName) {
		if(TextUtils.isEmpty(filePathName)) return false;
		if(TextUtils.isEmpty(newPathName)) return false;

		FileUtil.delete(newPathName); // 新名称对应的文件可能已经存在，先删除

		File file = new File(filePathName);
		if(!file.exists())return false;
		file.setReadable(true);
		file.setWritable(true);
		File newFile = new File(newPathName);
		newFile.setReadable(true);
		newFile.setWritable(true);
		File parentFile = newFile.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		return file.renameTo(newFile);
	}

	/**
	 * 删除文件
	 */
	public static boolean delete(String filePathName) {
        if (TextUtils.isEmpty(filePathName)) return false;
        File file = new File(filePathName);
        return delete(file);
    }

	public static boolean delete(File file) {
		return file != null
				&& file.isFile()
				&& file.exists()
				&& file.delete();
	}

	/**
	 * 创建目录，整个路径上的目录都会创建
	 * @param path
	 */
	public static void createDir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 尝试创建空文件
	 * <br> 如果文件已经存在不操作,返回true
	 * @param path 路径
	 * @return	如果创建失败(Exception) 返回false，否则true
	 */
	public static boolean createEmptyFile(String path){
		File file = new File(path);
		if(!file.exists()){
			try{
				return file.createNewFile();
			}
			catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取文件大小
	 * @param filePathName
	 */
	public static long getSize(String filePathName) {
		if(TextUtils.isEmpty(filePathName)) return 0;
		File file = new File(filePathName);
		if (file.isFile()) return file.length();
		return 0;
	}

    /**
     * 获取文件大小 M
     * @param filePathName
     */
    public static String getFileSize(String filePathName) {
        long fileSize = getSize(filePathName);
        return getFormatSizeM(fileSize);
    }

	/**
	 * 获取文件大小 M
	 * @param fileSize
     */
	public static String getFormatSizeM(long fileSize){
		double kSize = fileSize / 1024.0f;
		kSize = kSize / 1024.0f;
		DecimalFormat df = new DecimalFormat("0.00");
		return String.valueOf(df.format(kSize)) + "MB";
	}

	/**
	 * 读取文件数据到byte数组
	 * @param filePathName 文件名
	 * @param readOffset 从哪里开始读
	 * @param readLength 读取长度
	 * @param dataBuf 保存数据的缓冲区
	 * @param bufOffset 从哪里保存
	 */
	public static boolean readData(String filePathName, int readOffset, int readLength, byte[] dataBuf, int bufOffset) {
		try {
			int readedTotalSize = 0;
			int onceReadSize = 0;

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePathName));
			in.skip(readOffset);
			while (readedTotalSize < readLength
					&& (onceReadSize = in.read(dataBuf, bufOffset + readedTotalSize, readLength - readedTotalSize)) >= 0) {
				readedTotalSize += onceReadSize;
			}
			in.read(dataBuf, bufOffset, readLength);
			in.close();
			in = null;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 将某个流的内容输出到文件
	 * @param in 输入流
	 * @param filePathName 目标文件
	 */
	public static boolean writeFile(InputStream in,String filePathName){
		boolean flag = false;
		OutputStream outStream = null;
		if(in == null){
			return false;
		}
		try {
			File destFile = new File(filePathName);
			if (destFile.exists()) {
				destFile.delete();
			} else {
				destFile.createNewFile();
			}
			outStream = new BufferedOutputStream(new FileOutputStream(filePathName));
			byte[] buffer = new byte[1024];
			int count = 0;
			while(true){
				int length = in.read(buffer, 0, 1024);
				if(length > 0){
					outStream.write(buffer, 0, length);
				}else{
					break;
				}
				count+=length;
			}
			if(count > 0){
				flag = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(outStream != null){
				try{outStream.close();}catch(Exception e){e.printStackTrace();}
			}
		}
		return flag;
	}

	/**
	 * 将字节数据写入到文件
	 * <br>覆盖
	 *
	 * @param data
	 * @param filePathName
	 * @return null：写入成功 ； 其他：写入失败，返回的是失败相关信息
	 */
	public static String writeFileReturnError(byte[] data, String filePathName) {
		OutputStream outStream = null;
		try {
			File destFile = new File(filePathName);
			if (!destFile.exists()) {
				destFile.createNewFile();
			}
			outStream = new BufferedOutputStream(new FileOutputStream(filePathName));
			outStream.write(data);
			outStream.flush();
		} catch (Exception ex) {
			Logger.exception(ex);
			String errorMsg = "writeFile catch=" + ex.getMessage();
			if (errorMsg.contains("Not a directory")) {
				errorMsg += ", filePathName=" + filePathName;
			}
			return errorMsg;
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 将字节数据写入到文件
	 * <br>覆盖
	 * @param data
	 * @param filePathName
	 */
	public static boolean writeFile(byte[] data,String filePathName){
		OutputStream outStream = null;
		try{
			File destFile = new File(filePathName);
			if (destFile.exists()) {
				destFile.delete();
			} else {
				destFile.createNewFile();
			}
			outStream = new BufferedOutputStream(new FileOutputStream(filePathName));
			outStream.write(data);
			outStream.flush();
		}catch(Exception ex){
			return false;
		}finally{
			if(outStream != null){
				try{outStream.close();}catch(Exception e){e.printStackTrace();}
			}
		}

		return true;
	}

	/**
	 * 判断当前字符串是否为空
	 * @param str
	 */
	public static boolean isNullString(String str) {
        return str == null || str.equals("");
    }

	/**
	 * 复制文件
	 * @param fromPathName
	 * @param toPathName
	 */
	public static int copy(String fromPathName, String toPathName) {
		try {
			File newFile = new File(toPathName);
			File parentFile = newFile.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			InputStream from = new FileInputStream(fromPathName);
			return copy(from, toPathName);
		} catch (FileNotFoundException e) {
			return -1;
		}
	}

	/**
	 * 复制文件
	 * @param from
	 * @param toPathName
	 */
	public static int copy(InputStream from, String toPathName) {
		OutputStream to = null;
		try {
			File newFile = new File(toPathName);
			File parentFile = newFile.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			FileUtil.delete(toPathName); // 先删除
			to = new BufferedOutputStream(new FileOutputStream(toPathName));
			byte buf[] = new byte[1024];
			int c;
			while ((c = from.read(buf)) > 0) {
				to.write(buf, 0, c);
			}
			return 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			close(to);
			close(from);
		}
	}


	/**
	 * 复制文件夹及其中的文件
	 *
	 * @param oldPath String 原文件夹路径 如：data/user/0/com.test/files
	 * @param newPath String 复制后的路径 如：data/user/0/com.test/cache
	 * @return <code>true</code> if and only if the directory and files were copied;
	 * <code>false</code> otherwise
	 */
	public static boolean copyFolder(String oldPath, String newPath) {
		try {
			File newFile = new File(newPath);
			if (!newFile.exists()) {
				if (!newFile.mkdirs()) {
//					Log.e("--Method--", "copyFolder: cannot create directory.");
					return false;
				}
			}
			File oldFile = new File(oldPath);
			String[] files = oldFile.list();
			File temp;
			for (String file : files) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file);
				} else {
					temp = new File(oldPath + File.separator + file);
				}

				if (temp.isDirectory()) {   //如果是子文件夹
					copyFolder(oldPath + "/" + file, newPath + "/" + file);
				} else if (!temp.exists()) {
//					Log.e("--Method--", "copyFolder:  oldFile not exist.");
					return false;
				} else if (!temp.isFile()) {
//					Log.e("--Method--", "copyFolder:  oldFile not file.");
					return false;
				} else if (!temp.canRead()) {
//					Log.e("--Method--", "copyFolder:  oldFile cannot read.");
					return false;
				} else {
					FileInputStream fileInputStream = new FileInputStream(temp);
					FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
					byte[] buffer = new byte[1024];
					int byteRead;
					//!=-1 也可以写成！=null,意思是读取的数据不为负数或者null就说明还没有读取完毕
					while ((byteRead = fileInputStream.read(buffer)) != -1) {
						fileOutputStream.write(buffer, 0, byteRead);
					}
					fileInputStream.close();
					fileOutputStream.flush();
					fileOutputStream.close();
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据zip文件路径转换为文件路径
	 *  @param zipFullPath 必须带.zip
	 */
	public static String zip2FileFullPath(String zipFullPath) {
		int zipIndex = zipFullPath.lastIndexOf(".zip");
		int zipIndexTmp = zipFullPath.lastIndexOf(".ZIP");
		String tmp = "";
		if(zipIndex > -1) {
			tmp = zipFullPath.substring(0, zipIndex);
		} else if(zipIndexTmp > -1) {
			tmp = zipFullPath.substring(0, zipIndexTmp);
		}
		return tmp;
	}

	/**
	 * 改变文件权限
	 *
	 * @param permission
	 * @param filePathName
	 */
	public static void chmod(String permission, String filePathName)
	{
		try {
			String command = "chmod " + permission + " " + filePathName;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过NIO的方式拷贝文件
	 * <br>提高性能
	 *
	 * @param source
	 * @param target
	 */
	public static void copyByNIO(File source, File target) {
		try {
			FileInputStream fin = new FileInputStream(source);
			FileOutputStream fout = new FileOutputStream(target);
			ByteBuffer buffer = ByteBuffer.allocate(512);
			FileChannel ch1 = fin.getChannel();
			FileChannel ch2 = fout.getChannel();
			while (ch1.read(buffer) != -1) { // 从通道中读取内容到缓冲区
				buffer.flip(); // 让缓冲区回到初始位置才能进行写操作
				ch2.write(buffer);
				buffer.clear(); // 清空缓冲区
			}
			ch1.close();
			ch2.close();
			fin.close();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void deleteFilesInDirectory(String dirPath) {
		File dirF = new File(dirPath);
		File[] files = dirF.listFiles();
		if(files != null && files.length >0) {
			for (File fileF : files) {
				if (fileF.isFile()) {
					fileF.delete();
				}
			}
		}
	}

	public static void deleteDirectory(File file) {
		if(!file.isDirectory()) {
			return;
		}
		File[] paths = file.listFiles();
		if(paths != null && paths.length >0){
			for(File pathF : paths) {
				if(pathF.isDirectory()) {
					deleteDirectory(pathF);
				} else {
					pathF.delete();
				}
			}
		}
		file.delete();
	}

	/**
	 * 获取某个文件夹下某种文件的最大版本号
	 * <br>把文件名作为版本号的话返回最大版本号，
	 * 如:1.zip、2.zip、3.zip 返回3
	 *
	 * @param dir	文件夹
	 * @param ext	被查找的文件扩展名 如.zip
	 * @return 最大的那个版本号,如果没有找到返回 -1
	 */
	public static int getMaxVersion(String dir,final String ext){
		File path = new File(dir);
		int maxVersion = -1;
		if(path.exists() && path.isDirectory()){
			File[] files = path.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String filename) {
                    return filename != null && filename.endsWith(ext);
				}
			});

			if(files != null){
				for(int i = 0;i<files.length;i++){
					try{
						String fileName = FileUtil.getNameNoPostfix(files[i].getAbsolutePath());
						int ver = Integer.parseInt(fileName);
						if(ver > maxVersion) maxVersion = ver;
					}catch (Exception ex){

					}
				}
			}
		}
		return maxVersion;
	}

	/**
	 * 获取某个文件夹下某种文件的最大版本号文件
	 * <br>把文件名作为版本号的话返回最大版本号文件，
	 * 如:1.zip、2.zip、3.zip 返回/xx/xx/3.zip
	 *
	 * @param dir	文件夹
	 * @param ext	被查找的文件扩展名 如.zip
	 * @return 最大的那个版本号的文件路径,如果没有找到返回 null
	 */
	public static String getMaxVersionFile(String dir,final String ext){
		File path = new File(dir);
		String maxVerFile = null;
		int maxVersion = -1;
		if(path.exists() && path.isDirectory()){
			File[] files = path.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String filename) {
                    return filename.endsWith(ext);
				}
			});

			if(files != null){
				for(int i = 0;i<files.length;i++){
					try{
						String fileName = FileUtil.getNameNoPostfix(files[i].getAbsolutePath());
						int ver = Integer.parseInt(fileName);
						if(ver > maxVersion){
							maxVersion = ver;
							maxVerFile = files[i].getAbsolutePath();
						}
					}catch (Exception ex){

					}
				}
			}
		}

		return maxVerFile;
	}

	/**
	 * 获取某个文件夹下某种文件的最大版本号文件夹
	 * <br>把文件名作为版本号的话返回最大版本号文件夹，
	 * 如:/xx/xx/1/、/xx/xx/2/、/xx/xx/3/ 返回/xx/xx/3/
	 *
	 * @param dir	文件夹
	 * @return 最大的那个版本号的文件夹路径,如果没有找到返回 null
	 */
	public static String getMaxVersionDir(String dir){
		File path = new File(dir);
		String maxVerDir = null;
		int maxVersion = -1;
		if(path.exists() && path.isDirectory()){
			File[] files = path.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});

			if(files != null){
				for(int i = 0;i<files.length;i++){
					try{
						String fileName = files[i].getName();
						int ver = Integer.parseInt(fileName);
						if(ver > maxVersion){
							maxVersion = ver;
							maxVerDir = files[i].getAbsolutePath()+"/";
						}
					}catch (Exception ex){

					}
				}
			}
		}

		return maxVerDir;
	}
	
	public static List<String> getVersionDirBelowMax(String dir){
		List<String> result = new ArrayList<>();
		if(TextUtils.isEmpty(dir)) return result;
		
		File path = new File(dir);
		String maxVerDir = null;
		int maxVersion = -1;
		if(path.exists() && path.isDirectory()){
			File[] files = path.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});

			if(files != null){
				for(int i = 0;i<files.length;i++){
					try{
						String fileName = files[i].getName();
						int ver = Integer.parseInt(fileName);
						if(ver > maxVersion){
							if(maxVersion >= 0 && !TextUtils.isEmpty(maxVerDir)){
								result.add(maxVerDir);
							}
							maxVersion = ver;
							maxVerDir = files[i].getAbsolutePath()+"/";
						}else if(maxVersion >= 0 && !TextUtils.isEmpty(maxVerDir)){
							result.add(files[i].getAbsolutePath()+"/");
						}
					}catch (Exception ex){

					}
				}
			}
		}
		
		return result;
	}

	public static int getMaxVersion(String dir){
		File path = new File(dir);
		int maxVersion = -1;
		if(path.exists() && path.isDirectory()){
			File[] files = path.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});

			if(files != null){
				for(int i = 0;i<files.length;i++){
					try{
						String fileName = files[i].getName();
						int ver = Integer.parseInt(fileName);
						if(ver > maxVersion){
							maxVersion = ver;
						}
					}catch (Exception ex){

					}
				}
			}
		}

		return maxVersion;
	}

	public final static String read(String filePath) {
		BufferedInputStream bis 	= null;
		ByteArrayOutputStream baos  = null;
		try {
			File file = new File(filePath);
			if(!file.exists()) return "";
			baos 	= new ByteArrayOutputStream();
			bis 	= new BufferedInputStream(new FileInputStream(file));
			int length = 0;
			byte[] buffer = new byte[BYTE_IN_SIZE];
			while((length = bis.read(buffer, 0, BYTE_IN_SIZE)) > 0) {
				baos.write(buffer, 0, length);
			}
			if(baos.size() > 0) {
				return new String(baos.toByteArray(), "UTF-8");
			}
		} catch (Exception e) {
		} finally {
			close(bis);
			close(baos);
		}
		return "";
	}

	public final static byte[] readToByte(String filePath) {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) return null;
			baos = new ByteArrayOutputStream();
			bis = new BufferedInputStream(new FileInputStream(file));
			int length = 0;
			byte[] buffer = new byte[BYTE_IN_SIZE];
			while ((length = bis.read(buffer, 0, BYTE_IN_SIZE)) > 0) {
				baos.write(buffer, 0, length);
			}
			if (baos.size() > 0) {
				return baos.toByteArray();
			}
		} catch (Exception e) {
		} finally {
			close(bis);
			close(baos);
		}
		return null;
	}

	public final static String read(InputStream inputStream) {
		ByteArrayOutputStream baos  = null;
		try {
			baos 	= new ByteArrayOutputStream();
			int length = 0;
			byte[] buffer = new byte[BYTE_IN_SIZE];
			while((length = inputStream.read(buffer, 0, BYTE_IN_SIZE)) > 0) {
				baos.write(buffer, 0, length);
			}
			if(baos.size() > 0) {
				return new String(baos.toByteArray(), "UTF-8");
			}
		} catch (Exception e) {
		} finally {
			close(inputStream);
			close(baos);
		}
		return "";
	}

	/**
	 * 通过Java NIO拷贝
	 *
	 * @param source 原文件
	 * @param dest 目标文件
	 */
	public final static void copyFileUsingFileChannels(String source, String dest) {
		if(!FileUtil.isExist(source)||!FileUtil.isFile(source))
			return;
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(inputChannel);
			close(outputChannel);
		}

	}

	/**
	 * 通过Java NIO拷贝
	 *
	 * @param source 原文件
	 * @param dest 目标文件
	 * @param position 拷贝起点
	 * @param count 拷贝长度
	 */
	public final static void copyFileUsingFileChannelsToCount(String source, String dest,long position,long count) {
		if(!FileUtil.isExist(source)||!FileUtil.isFile(source))
			return;
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			if(position<0)
				position=0;
			if(count>inputChannel.size())
				count=inputChannel.size();
			outputChannel.transferFrom(inputChannel, position, count);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(inputChannel);
			close(outputChannel);
		}
	}

	/**
	 * 获取文件大小，如果是目录则递归计算其内容的总大小
     */
	public static long getFileSize(File file) {
		//判断文件是否存在
		if (file!=null&&file.exists()) {
			//如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				long size = 0;
				if(children != null) {
					for (File f : children) {
						size += getFileSize(f);
					}
				}
				return size;
			} else {//如果是文件则直接返回其大小
				return file.length();
			}
		} else {
			return 0;
		}
	}

	public static void writePathContent(String path, String content) {
		BufferedWriter bufferedWriter = null;
		try {
			File file = new File(path);
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(content);
			bufferedWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String readPathContent(String path) {
		String result = "";
		File file = new File(path);
		if (file.exists()) {
			FileReader fr = null;
			BufferedReader br = null;
			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				String line = "";
				while ((line = br.readLine()) != null) {
					result += line;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fr != null) fr.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
				try {
					if (br != null) br.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static void createDirAndClear(String dir) {
		File dirF = new File(dir);
		if (!dirF.exists()) {
			dirF.mkdirs();
		} else {
			File[] files = dirF.listFiles();
			if(files != null && files.length >0) {
				for (File fileF : files) {
					if (fileF.isFile()) {
						fileF.delete();
					}
				}
			}
		}
	}

	/**
	 * 确保父目录存在
	 */
	public static boolean ensureParentDir(File file) {
		if(file == null){
			return false;
		}
		File parent = file.getParentFile();
		if(parent == null){
			return false;
		}
		if(parent.exists()){
			return true;
		}
		String parentDir = parent.getAbsolutePath();
//		String cacheDir = GlobalContext.getContext().getCacheDir().getAbsolutePath();

		// External缓存目录： /storage/emulated/0/Android/data/com.xxx/cache
		// 从Android11开始，/Android/data/<package> 目录被 系统沙箱化，该目录不允许app手动拼写访问
		// 该目录在这里走 parent.mkdirs()会创建失败。必须通过调用 Context().getExternalCacheDir() 来拼接。
		File extCacheFile = GlobalContext.getContext().getExternalCacheDir();
		if(extCacheFile != null) {
			String extCacheDir = extCacheFile.getAbsolutePath();
			if(parentDir.contains(extCacheDir)){
				String restPath = parentDir.replace(extCacheDir, "");
				File targetParentFile = new File(extCacheFile, restPath);
				boolean targetParentMkSuccess = targetParentFile.mkdirs();
				Logger.textSingle("FileUtil", "ensureParentDir", "parent [" + parent.getAbsolutePath() + "] mkDir result: " + targetParentMkSuccess);
				return targetParentMkSuccess;
			}
		}

		boolean parentMkSuccess = parent.mkdirs();
		if(!parentMkSuccess){
			Logger.textSingle("FileUtil", "ensureParentDir", "mkdirs [" + parent.getAbsolutePath() + "] result = false" +
					", canWrite=" + parent.canWrite() +
					", externalState=" + Environment.getExternalStorageState());
		}
		return parentMkSuccess;
	}

}
