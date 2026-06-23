package com.xcyh.cheatsheet.home.list;

import android.widget.TextView;

import com.benefit.novelverse.R;
import com.fold.recyclyerview.BaseQuickAdapter;
import com.fold.recyclyerview.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by haojiangfeng on 2025/6/30.
 */
public class HomeListAdapter extends BaseQuickAdapter<HomeItem, BaseViewHolder> {


    public HomeListAdapter() {
        super(R.layout.layout_home_item);
    }

    public HomeListAdapter(@Nullable List<HomeItem> data) {
        super(R.layout.layout_home_item, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, HomeItem item) {
        TextView titleView = helper.getView(R.id.title_view);
        titleView.setText(item.title);
    }
}
