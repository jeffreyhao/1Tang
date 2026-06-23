package com.benefit.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.baidu.baselibrary.Widget;
import com.benefit.dialog.base.PPBottomSheetDialogFragment;
import com.benefit.novelverse.R;
import com.github.bean.database.table.BookInfo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Map;

public class ChapterSheetDialogFragment extends PPBottomSheetDialogFragment {
    /**
     * 单例
     */
    public static void newInstance(FragmentManager fragmentManager, BookInfo book, boolean needShowLock, boolean needCloseActivity) {
        String KEY_BOOK_INFO = "book_info";
        String KEY_LAST_UPDATED = "last_update";
        String KEY_SHOW_LOCK = "show_lock";
        String KEY_IN_READ = "in_read";
        String KEY_NEED_CLOSE = "need_close_activity";

        if (book != null) {
            ChapterSheetDialogFragment chapterSheetDialogFragment = new ChapterSheetDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_BOOK_INFO, book);
            bundle.putBoolean(KEY_SHOW_LOCK, needShowLock);
            bundle.putBoolean(KEY_IN_READ, false);
            bundle.putBoolean(KEY_NEED_CLOSE, needCloseActivity);
            chapterSheetDialogFragment.setArguments(bundle);
            chapterSheetDialogFragment.show(fragmentManager, "POP_STYLE_PERMISSION");
        }
    }

    /**
     * 单例
     * @param permissionsMap 权限列表
     *
     * @return 底部弹窗Fragment
     */
    public static ChapterSheetDialogFragment newInstance(Map<String, String> permissionsMap) {
        ChapterSheetDialogFragment ChapterSheetDialogFragment = new ChapterSheetDialogFragment();
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : permissionsMap.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }
        ChapterSheetDialogFragment.setArguments(bundle);
        return ChapterSheetDialogFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
//        // 阅读设置
//        ReadSettingInfo mReadSettingInfo = DataSHP.getReadSettingInfo();
//        if (mReadSettingInfo != null && mReadSettingInfo.isNight()) {
//            //夜间模式
//            setWhiteNavigationBar(dialog, 0xff262423);
//        } else {
//            setWhiteNavigationBar(dialog, 0xffffffff);
//        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.module_fragment_bottom_sheet_chapters, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (null == dialog) {
            return;
        }

        View view = dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (null == view || null == getActivity()) {
            return;
        }
//        // 阅读设置
//        ReadSettingInfo mReadSettingInfo = DataSHP.getReadSettingInfo();
//        if (mReadSettingInfo != null && mReadSettingInfo.isNight()) {
//            view.findViewById(R.id.permission_mask).setVisibility(View.VISIBLE);
//        } else {
//            view.findViewById(R.id.permission_mask).setVisibility(View.GONE);
//        }
        view.setBackgroundColor(Color.TRANSPARENT);
        int height = getResources().getDisplayMetrics().heightPixels - Widget.dip2px(80);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
        BottomSheetBehavior.from(view).setPeekHeight(height);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.bottom_sheet_chapter_arrow).setOnClickListener(v -> closeDialog());

    }

    private void closeDialog() {
        if (null == getActivity()) {
            return;
        }
        getActivity().runOnUiThread(this::dismiss);
    }
}