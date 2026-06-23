package com.xcyh.cheatsheet.app.utils;

import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.FragmentActivity;

/**
 * Created by haojiangfeng on 2024/11/18.
 */
public class DatePickUtil {



    public static void showMultiDatePick(FragmentActivity activity){
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());

//        @SuppressLint("SetTextI18n")
//        DatePickerDialog dpd = DatePickerDialog.newInstance(
//                (view12, year, monthOfYear, dayOfMonth) -> {
//                    ToastUtils.showLong(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
////                    uploadParameter("birthday", mBinding.layout.profileUserBirthday.getText().toString());
//                },
//                date.get(Calendar.YEAR),
//                date.get(Calendar.MONTH),
//                date.get(Calendar.DAY_OF_MONTH)
//        );
////        dpd.setAccentColor(getResources().getColor(R.color.color_date_pick_title, getTheme()));
////        dpd.setOkColor(getResources().getColor(R.color.color_date_pick_ok, getTheme()));
////        dpd.setCancelColor(getResources().getColor(R.color.color_date_pick_cancel, getTheme()));
//        dpd.setThemeDark(false);
//        dpd.vibrate(true);
//        dpd.showYearPickerFirst(false);
//        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
//        dpd.show(activity.getSupportFragmentManager(), "date_pick_dialog");
    }

}
