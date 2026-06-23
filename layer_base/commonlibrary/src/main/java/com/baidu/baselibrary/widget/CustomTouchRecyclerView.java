package com.baidu.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CustomTouchRecyclerView extends RecyclerView {
    private OnRecyclerViewTouchListener mListener;
    public CustomTouchRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CustomTouchRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN) {
            if(mListener!=null) {
                mListener.onTouch();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface OnRecyclerViewTouchListener {
        void onTouch();
    }

    public void setOnRecyclerViewTouchListener(OnRecyclerViewTouchListener listener) {
        this.mListener = listener;
    }
}
