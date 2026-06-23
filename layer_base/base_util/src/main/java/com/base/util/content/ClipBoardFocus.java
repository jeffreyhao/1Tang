package com.base.util.content;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.api.AppApi;
import com.base.api.Logger;
import com.base.callback.EmptyActivityLifecycleCallback;
import com.base.util.R;

public class ClipBoardFocus {


    public static ClipBoardFocus newInstance(@NonNull Activity activity, @Nullable EditText editText, boolean isInit) {
       return new ClipBoardFocus(activity, editText, isInit);
    }


    private Activity mActivity;
    private EditText mEditText;
    private Runnable mRunnable;

    /**
     * 确保只调用一次回调
     */
    private boolean calledRun = false;
    /**
     * 是否要初始化
     * <br>
     * Splash页面需要初始化，得监听onPause，而不是onDestroy
     */
    private boolean isInit = false;

    private final Runnable mCountDownRunnable = () -> {
        doRun();
    };

    private final View.OnFocusChangeListener mFocusChangeListener = (v, hasFocus) -> {
        if(hasFocus) {
            doRun();
        }
    };

    private final Application.ActivityLifecycleCallbacks mActivityCallback = new EmptyActivityLifecycleCallback() {

        @Override
        public void onActivityPaused(Activity activity) {
            if(isInit) {
                doRun();
            }
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            if(!isInit) {
                doRun();
            }
        }
    };


    private ClipBoardFocus(@NonNull Activity activity, @Nullable EditText editText, boolean isInit){
        this.mActivity = activity;
        this.mEditText = addEditText(activity, editText);
    }

    public void start(Runnable runnable){
        this.mRunnable = runnable;
        if(mEditText.hasFocus()) {
            if(mRunnable != null) {
                mRunnable.run();
            }
        } else {
            registerCallbacks();
        }
    }

    private void registerCallbacks(){
        mEditText.setOnFocusChangeListener(mFocusChangeListener);

        // 延迟兜底逻辑，如果不能调用到 onFocusChange(), 也要给调用 run()
        AppApi.getHandler().postDelayed(mCountDownRunnable, 500);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mActivity.registerActivityLifecycleCallbacks(mActivityCallback);
        }
    }

    private void doRun() {
        if(!calledRun) {
            Logger.textSingle("ClipBoardFocus", "doRun","ClipBoard.run()-> editText.hasFocus: " + (mEditText == null ? "null" : mEditText.hasFocus()));
            if(mRunnable != null) {
                mRunnable.run();
            }
            calledRun = true;
        }
        if(mEditText != null) {
            mEditText.setOnFocusChangeListener(null);
        }
        AppApi.getHandler().removeCallbacks(mCountDownRunnable);
        removeEditText();
    }

    @NonNull
    private EditText addEditText(Activity activity, EditText editText){
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        if(editText == null) {
            editText = decorView.findViewById(R.id.clip_board_1234_edit_text);
            if(editText == null) {
                editText = new EditText(activity);
                editText.setId(R.id.clip_board_1234_edit_text);
                decorView.addView(editText, new ViewGroup.LayoutParams(1, 1));
            }
        }
        return editText;
    }

    private void removeEditText(){
        if(mEditText == null) {
            return;
        }
        if(mEditText.getId() == R.id.clip_board_1234_edit_text) {
            ViewParent viewParent = mEditText.getParent();
            if(viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeView(mEditText);
            }
        }
    }

}
