package com.benefit.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @ProjectName: reader-android
 * @Package: com.yueyou.adreader.ui.base
 * @ClassName: YYBaseFragment
 * @Description: java类作用描述
 * @Author: zqw
 * @CreateDate: 2020/12/7 11:13 AM
 * @UpdateUser: zqw
 * @UpdateDate: 2020/12/7 11:13 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public abstract class PPBaseFragment extends Fragment {

    public PPBaseFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getResId(), null);
    }
    protected abstract int getResId();
}
