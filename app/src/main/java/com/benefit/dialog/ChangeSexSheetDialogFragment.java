package com.benefit.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.benefit.base.PPBottomSheetDialogFragment;
import com.benefit.novelverse.R;
import com.benefit.novelverse.utils.Constant;
import com.baidu.baselibrary.Widget;

/**
 * 1是男，2是女，0无性别
 */
public class ChangeSexSheetDialogFragment extends PPBottomSheetDialogFragment {
    /**
     * 单例
     */
    public static ChangeSexSheetDialogFragment newInstance(int sex) {
        ChangeSexSheetDialogFragment chapterSheetDialogFragment = new ChangeSexSheetDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("USER_SEX", sex);
        chapterSheetDialogFragment.setArguments(bundle);
        return chapterSheetDialogFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.select_sex_layout, null);
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
        view.setBackgroundColor(Color.TRANSPARENT);
        int height = Widget.dip2px(180);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
        BottomSheetBehavior.from(view).setPeekHeight(height);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textMan = view.findViewById(R.id.tv_man_text);
        ImageView imgMan = view.findViewById(R.id.tv_man_img);

        TextView textWoman = view.findViewById(R.id.tv_woman_text);
        ImageView imgWoman = view.findViewById(R.id.tv_woman_img);

        TextView textNo = view.findViewById(R.id.tv_no_sex_text);
        ImageView imgNo = view.findViewById(R.id.tv_no_sex_img);

        int sexType = 0;
        Bundle bundle = getArguments();
        if (null != bundle) {
            sexType = bundle.getInt("USER_SEX");
        }
        selectSex(textMan, imgMan, false);
        selectSex(textWoman, imgWoman, false);
        selectSex(textNo, imgNo, false);
        if (sexType == Constant.SEXTYPE.TYPE_UNKOWN) {
            selectSex(textNo, imgNo, true);
        } else if (sexType == Constant.SEXTYPE.TYPE_MAN) {
            selectSex(textMan, imgMan, true);
        } else if (sexType == Constant.SEXTYPE.TYPE_WOMAN) {
            selectSex(textWoman, imgWoman, true);
        }

        view.findViewById(R.id.tv_man).setOnClickListener(v -> {
            selectSex(textMan, imgMan, true);
            selectSex(textWoman, imgWoman, false);
            selectSex(textNo, imgNo, false);
            listener.selectedSex(Constant.SEXTYPE.TYPE_MAN);
            closeDialog();
        });

        view.findViewById(R.id.tv_woman).setOnClickListener(v -> {
            selectSex(textMan, imgMan, false);
            selectSex(textWoman, imgWoman, true);
            selectSex(textNo, imgNo, false);
            listener.selectedSex(Constant.SEXTYPE.TYPE_WOMAN);
            closeDialog();
        });

        view.findViewById(R.id.tv_no_sex).setOnClickListener(v -> {
            selectSex(textMan, imgMan, false);
            selectSex(textWoman, imgWoman, false);
            selectSex(textNo, imgNo, true);
            listener.selectedSex(Constant.SEXTYPE.TYPE_UNKOWN);
            closeDialog();
        });
    }

    private void selectSex(TextView textView, ImageView imageView, boolean selected) {
        if (selected) {
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            imageView.setBackgroundResource(R.drawable.icon_sex_selected);
        } else {
            textView.setTypeface(Typeface.DEFAULT);
            imageView.setBackgroundResource(R.drawable.icon_sex_unselected);
        }
    }

    private void closeDialog() {
        if (null == getActivity()) {
            return;
        }
        getActivity().runOnUiThread(this::dismiss);
    }

    private OnDialogClickListener listener;
    /**
     * 添加Dialog监听器
     *
     * @param listener 监听器
     */
    public void setOnDialogClickListener(@NonNull OnDialogClickListener listener) {
        this.listener = listener;
    }

    public interface OnDialogClickListener {
        void selectedSex(int sex);
    }
}