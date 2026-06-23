package com.baidu.baselibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by haojiangfeng on 2024/12/18.
 */
@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SwitchButton extends Switch {


    private OnCheckedChangeListener mCheckedChangeListener;


    public SwitchButton(@NonNull Context context) {
        super(context);
    }

    public SwitchButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 会触发 onCheckChanged
    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
    }


    /**
     * 设置是否选中。 该api不会触发 onCheckChanged
     * @param checked
     */
    public void setCheckedWithoutCallback(boolean checked){
        super.setOnCheckedChangeListener(null);
        setChecked(checked);
        super.setOnCheckedChangeListener(mCheckedChangeListener);
    }

    @Override
    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
        mCheckedChangeListener = listener;
    }

    public OnCheckedChangeListener getOnCheckedChangeListener(){
        return mCheckedChangeListener;
    }
}
