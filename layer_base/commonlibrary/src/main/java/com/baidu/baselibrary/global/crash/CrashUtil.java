package com.baidu.baselibrary.global.crash;

import android.content.Context;



public class CrashUtil {						//1M
	public static final int  MAX_UPLOAD_SIZE = 1024*1024;//单个文件上传大小限制在100kb。不过后续不走php，且客户端走统一上传，已不做这个强制要求也可。
	/**错误收集信息*/
	public static final String ID_TRACE 			    = "id"; 			 		 //区别崩溃信息的唯一标识
	public static final String STACK_TRACE  		    = "errorMsg";        		 //崩溃信息
	public static final String CLIENT_VERSION 		    = "clientVersion";  		 //客户端的版本号
	public static final String SYSTEM_VERISON 	        = "systemVerison";  		 //手机android系统的版本号
	public static final String PHONE_MODEL 		        = "phoneModel"; 			 //手机型号
	public static final String OTHER_MESSAGE 		    = "otherMsg"; 				 //可供扩展今后需要的其他信息，可嵌套为jsonArray格式

	public static final String CLIENT_SOURCE		    = "clientSource";    		 //客户端类型
	public static final String ROM_NAME		    	    = "romName";         		 //系统rom名
	public static final String CRASH_TIME		        = "crashTime";       		 //崩溃时间
	public static final String VERSION_CODE		        = "versionCode";     		 //versionCode
	public static final String VERSION_NAME		        = "versionName";    		 //versionName
	public static final String CHANNEL_ID		        = "channelId";      		 //渠道号
	public static final String PACKAGE_NAME		        = "packageName";             //包名
	
	public static final String LCD_TYPE		    	    = "lcdType";       			 //屏幕分辩率
	public static final String SCREEN_INSH		        = "screenInsh";  			 //屏幕尺寸
	public static final String USER_ID		    	    = "userId";         		 //用户id
	
	/**android读书版本标识信息*/
	public static final String READ = "01";     		         //客户端来源01读书，02听书
	
	/**日志存放目录与文件格式*/
	public static String CRASH_SAVE_FILENAME		    = ".csh";				 	 //错误日志文件格式
	public static String CRASH_SAVE_ADDRESS;    									 //错误日志存放的目录
	
	/**服务端返回错误日志标识字段*/
	public static String JSON_KEY_CRASH					= "crashLog";	
	
	/** 
     * 保存错误信息到文件中 
     * @param crashStr 错误信息的json字符串 
     * @return fileName 文件名
     */  
    public static void saveCrashInfoToFile(String crashStr,String fileName) {  
//    	if(TextUtils.isEmpty(crashStr)){return ;}
//    	if(!SDCardUtils.hasSdcard() || !SDCardUtils.hasFreeSpace()){return ;}//sd卡不存在或sd卡空间不足时，舍弃存储。
//    	if(crashStr.length()>MAX_UPLOAD_SIZE) return;
//
//    	File logFile 				   = null;
//        FileOutputStream fileOutStream = null;
//        try {
//         	//如果安装时就崩溃了，初始化工作都没完成，所有文件也没创建。return掉。
//        	File file = new File(SDCardUtils.getLogDir());
//        	if(file == null || !file.exists()) return;
//
//        	logFile = new File(SDCardUtils.getLogDir(),fileName);
//        	crashStr = "{\"crashInfo\":["+crashStr+"]}";
//    		fileOutStream 	= new FileOutputStream(logFile);
//    		fileOutStream.write(crashStr.toString().getBytes("UTF-8"));
//    		fileOutStream.flush();
//        } catch (Exception e) {
//        	e.printStackTrace();
//        	if(logFile!=null)logFile.delete();
//        } finally{
//        	if(fileOutStream!=null){try {fileOutStream.close();} catch (IOException e) {e.printStackTrace();}}
//        }
    }  
    
    
    /** 
     * 收集程序崩溃的设备信息 
     */  
    public static void collectCrashInfo (Context ctx, Throwable ex) throws Exception{ 
//    	CrashBean crash = new CrashBean();
//    	StringBuffer sb = new StringBuffer();
//
//    	Writer info = null;
//    	PrintWriter printWriter = null;
//        try {
//        	info = new StringWriter();
//        	printWriter = new PrintWriter(info);
//            ex.printStackTrace(printWriter);
//            ex.printStackTrace();
//
//            // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。
//            Throwable cause = ex.getCause();
//            while (cause != null) {
//                cause.printStackTrace(printWriter);
//                cause.printStackTrace();
//                cause = cause.getCause();
//            }
//            printWriter.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			File.close(printWriter);
//		}
//
//        String result = info.toString();
//        sb.append(result);
//
//       // 使用反射来收集设备信息.在Build类中包含各种设备信息
//       Field[] fields = Build.class.getDeclaredFields();
//       for (Field field : fields) {
//           try {
//               field.setAccessible(true);  //public static final java.lang.String android.os.Build.BOARD
//               String name = field.getName();
//               if(name.equals("MODEL")){
//            	   crash.mPhoneModel 	= (String) field.get(null);	//get(null),获取静态变量
//               }else if(name.equals("DISPLAY")){
//            	   crash.mRomName 	    = (String) field.get(null);
//               }
////               sb.append(field.getName()+"="+String.valueOf(field.get(null)));
//           } catch (Exception e) {
//           	e.printStackTrace();
//           }
//       }
//
//		try {
//			PackageManager pm = ctx.getPackageManager();
//			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),PackageManager.GET_ACTIVITIES);
//			if (pi != null) {
//				crash.mVersionName = pi.versionName == null ? "": pi.versionName;
//				crash.mVersionCode = String.valueOf(pi.versionCode);
//				crash.mPackageName = pi.packageName;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		crash.mCrashId        = MD5.getMD5(result);        					  				 //获取id
//		crash.mChannelID 	  = Device.CUSTOMER_ID;							  				 //p2
//        crash.mClientVersion  = Device.APP_UPDATE_VERSION;					  				 //p3
//        crash.mClientSource   = Device.PLATFORM_ID+ CrashUtil.READ;
//        crash.mLCDType        = Device.DisplayHeight()+"*"+Device.DisplayWidth();  //p5
//        crash.mScreenInsh     = Device.getScreenInch()+"";
//
//		crash.mErrorMsg 	  = sb.toString();
//		crash.mCrashTime 	  = System.currentTimeMillis()+"";
//		crash.mOtherMsg 	  = "";
//       	crash.mSystemVerison  = Build.VERSION.RELEASE;
////		crash.mUserId         = Account.getInstance().getUserName();
//
//		JSONObject jObject = crash.obj2Json();
//		if(jObject!=null){
//			String fileName = System.currentTimeMillis()+ CrashUtil.CRASH_SAVE_FILENAME;
//		    CrashUtil.saveCrashInfoToFile(jObject.toString(),fileName);
//		}
    }  
}
