package com.base.util.ui;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by haojiangfeng on 2024/12/4.
 */
public class SystemBarUtil {




    public static void setNavVisibility(Activity activity, boolean visible) {

        int newVis = 256 //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | 512 //View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | 1024; //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (!visible) {
            newVis = 256 //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | 512 //View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | 1024 //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | 4 //View.SYSTEM_UI_FLAG_FULLSCREEN
                    | 2 //View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | 4096 //View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | 2048; //View.SYSTEM_UI_FLAG_IMMERSIVE
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; //View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | 4096; //View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            activity.getWindow().getDecorView().setSystemUiVisibility(newVis);
        }else{
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            activity.getWindow().getDecorView().setSystemUiVisibility(newVis);
        }
//		if(visible){
//			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		}else{
//			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		}

//		Window window = getWindow();
//		window.setFlags(0x4000000, 0x4000000);
//		window.setFlags(0x9000000, 0x9000000);
    }



}
