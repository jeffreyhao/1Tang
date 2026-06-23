package com.benefit.utils;

/**
 * Time: 2024/1/3
 * Author: lhc
 * Desc:
 */
public class ScreenShotUploadUtil {
//    public static void screenShot(Activity context, BookChapter chapter){
//        if(chapter.getIndex() != 1) {
//            return;
//        }
//        boolean screenShotOpen = PreferencesUtil.get(Constant.SP_CONSTANT.SCREEN_SHOT_OPEN,false);
//        boolean screenShotComplete = CacheUtil.getBookScreenShot(chapter.getCom_book_id());
//        if(!screenShotOpen || (screenShotComplete&&chapter.getIndex() == 1)){
//            return;
//        }
//        App.postDelayed(() -> {
//            String fileName = TextUtils.isEmpty(UserManager.getUserId())?
//                    DevicesUtil.getDeviceId() + "-" + chapter.getCom_book_id() + "-" + chapter.getIndex() + ".jpg" :
//                    UserManager.getUserId() + "-" + chapter.getCom_book_id() + "-" + chapter.getIndex() + ".jpg";
//            ViewToBitmapUtils.saveScreenToBitmap(context,fileName,chapter.getCom_book_id());
//        }, 50);
//    }

}
