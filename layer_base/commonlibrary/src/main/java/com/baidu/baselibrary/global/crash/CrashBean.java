package com.baidu.baselibrary.global.crash;

import org.json.JSONObject;

public class CrashBean {
	public String mCrashId;		   //区别崩溃信息的唯一标识
	public String mErrorMsg;	   //崩溃信息
	public String mClientVersion;  //客户端的版本号
	public String mSystemVerison;  //手机android系统的版本号
	public String mPhoneModel;     //手机型号
	public String mOtherMsg;	   //可供扩展的今后需要的其他信息，支持jsonArray格式
	
	public String mVersionName;    //VersionName 
	public String mVersionCode;    //VersionCode
	public String mCrashTime;      //崩溃时间
	public String mPackageName;    //包名
	public String mChannelID;      //渠道号
	public String mClientSource;   //客户端类型
	public String mRomName;  	   //rom标识
	
	public String mLCDType; 	   //屏幕分辩率
	public String mScreenInsh;     //屏幕尺寸
	
	public String mUserId;		   //账号
	
	/**
	 * 将对象转为JSON
	 * @return
	 */
	public JSONObject obj2Json() {
		JSONObject crashJObject = null;
		try {
			crashJObject = new JSONObject();
    	    crashJObject.put(CrashUtil.ID_TRACE, mCrashId);
    	    crashJObject.put(CrashUtil.STACK_TRACE, mErrorMsg);
    	    crashJObject.put(CrashUtil.CLIENT_VERSION, mClientVersion);
    	    crashJObject.put(CrashUtil.SYSTEM_VERISON, mSystemVerison);
			crashJObject.put(CrashUtil.PHONE_MODEL, mPhoneModel);
			crashJObject.put(CrashUtil.OTHER_MESSAGE, mOtherMsg);

			crashJObject.put(CrashUtil.PACKAGE_NAME, mPackageName);
			crashJObject.put(CrashUtil.VERSION_NAME, mVersionName);
			crashJObject.put(CrashUtil.VERSION_CODE, mVersionCode);
			crashJObject.put(CrashUtil.CRASH_TIME, mCrashTime);
			crashJObject.put(CrashUtil.ROM_NAME, mRomName);
			crashJObject.put(CrashUtil.OTHER_MESSAGE, mOtherMsg);
			crashJObject.put(CrashUtil.CLIENT_SOURCE, mClientSource);
			crashJObject.put(CrashUtil.CHANNEL_ID, mChannelID);

			crashJObject.put(CrashUtil.LCD_TYPE, mLCDType);
			crashJObject.put(CrashUtil.SCREEN_INSH, mScreenInsh);
			crashJObject.put(CrashUtil.USER_ID, mUserId);
			
		} catch (Exception e) {
			e.printStackTrace();
			crashJObject = null;
		}  
		return crashJObject;
	}
	
}
