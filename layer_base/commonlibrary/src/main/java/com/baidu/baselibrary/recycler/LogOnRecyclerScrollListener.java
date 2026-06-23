package com.baidu.baselibrary.recycler;

import com.baidu.baselibrary.log.ALog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by niepan on 27/01/26.
 */
public class LogOnRecyclerScrollListener extends RecyclerView.OnScrollListener{

    int percentDy = 0;

    public String className(){
        return "LogOnScrollListener";
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        percentDy += dy;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                percentDy = 0;
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                ALog.textSingle(className(), "onScrollStateChanged", "本次滑动距离: " + percentDy);
                percentDy = 0;
                break;
        }
    }

}
