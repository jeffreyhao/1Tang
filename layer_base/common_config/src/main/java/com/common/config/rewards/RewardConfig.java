package com.common.config.rewards;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.github.bean.operation.RewardSignBean;

import java.util.List;


public class RewardConfig {

    public interface RewardRecycleStyleCreate {
        RecyclerView.ItemDecoration getDecoration();

        RecyclerView.LayoutManager getLayoutManager();

        RecyclerView.Adapter<?> getAdapter(Context context, List<RewardSignBean.SignBean.SignListBean> list);
    }

    public static RewardRecycleStyleCreate sRewardRecycleStyleCreator;



}
