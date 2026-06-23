package com.fold.recyclyerview;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class LoopQuickAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K>{

    private boolean enableLoop;

    private int loopInterval = 10000;

    private int currentPosition = 0;

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            currentPosition++;
            RecyclerView recyclerView = getRecyclerView();
            if(recyclerView == null) {
                return;
            }
            recyclerView.smoothScrollToPosition(currentPosition);
            recyclerView.postDelayed(this, loopInterval); // 每 loopInterval 毫秒 滚动一次
        }
    };

    public LoopQuickAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    public LoopQuickAdapter(@Nullable List data) {
        super(data);
    }

    public LoopQuickAdapter(int layoutResId) {
        super(layoutResId);
    }

    public boolean enableLoop() {
        return enableLoop;
    }

    public void setLoopEnable(boolean enable) {
        this.enableLoop = enable;
    }

    @Override
    public int getItemCount() {
        List<T> list = getData();
        if(enableLoop) {
            return (list == null || list.isEmpty()) ? 0 : Integer.MAX_VALUE;
        } else {
            return list == null ? 0 : list.size();
        }
    }

    @Override
    public long getItemId(int position) {
        List<T> list = getData();
        if (list == null || list.isEmpty()) {
            return RecyclerView.NO_ID;
        }
        if(enableLoop) {
            return position % list.size();
        } else {
            return position;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(enableLoop) {
            List<T> list = getData();
            if (list != null && !list.isEmpty()) {
                return getDefItemViewType(position % list.size());
            }
        }
        return super.getItemViewType(position);
    }

    public void startAutoScroll() {
        RecyclerView recyclerView = getRecyclerView();
        if(recyclerView == null) {
            return;
        }
        recyclerView.removeCallbacks(task);
        recyclerView.postDelayed(task, loopInterval);
    }

    public void stopAutoScroll() {
        RecyclerView recyclerView = getRecyclerView();
        if(recyclerView == null) {
            return;
        }
        recyclerView.removeCallbacks(task);
    }

    public void setLoopPosition(int position) {
        currentPosition = position;
    }

    public void setLoopInterval(int mills) {
        loopInterval = mills;
    }
}
