package com.meta.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.benefit.novelverse.R;
import com.fold.dialog.widget.base.BaseDialog;

/**
 * Created by adison on 2017/7/4.
 */
public class ProgressWheelDialog extends BaseDialog<ProgressWheelDialog> {
    private String mTitle;

    public ProgressWheelDialog(Context context) {
        super(context);
    }

    public ProgressWheelDialog(Context context, boolean hideNavigation) {
        super(context);
        if(hideNavigation){
            int uiOptions =View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_IMMERSIVE
                    |View.SYSTEM_UI_FLAG_FULLSCREEN;
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public View onCreateView() {
        View view = View.inflate(getContext(), R.layout.dialog_progress,null);
        TextView mDialogProgressText = view.findViewById(R.id.dialog_progress_text);
        mDialogProgressText.setText(mTitle);
        return view;
    }

    @Override
    public void setUiBeforShow() {

    }

    public void setDialogProgressText(String content) {
        mTitle = content;
    }
}
