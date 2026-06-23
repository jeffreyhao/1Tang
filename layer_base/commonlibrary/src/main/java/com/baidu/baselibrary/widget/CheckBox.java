package com.baidu.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

/**
 * Created by haojiangfeng on 2025/1/10.
 */
public class CheckBox extends AppCompatCheckBox {


    private OnCheckedChangeListener mCheckedChangeListener;


    public CheckBox(@NonNull Context context) {
        super(context);
    }

    public CheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
