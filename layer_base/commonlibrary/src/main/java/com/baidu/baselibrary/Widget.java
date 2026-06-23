package com.baidu.baselibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.baselibrary.global.config.AppCommonConfig;
import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.App;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.baselibrary.R;

import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.core.app.NotificationManagerCompat;
import androidx.palette.graphics.Palette;

public class Widget {
   


    public static Context getContext() {
        return App.getContext();
    }

    public static String getResourcesString(int resourceId) {
        Activity topActivity = ActivityUtils.getTopActivity();
        if(topActivity!=null) {
            return topActivity.getString(resourceId);
        }
        return getContext().getString(resourceId);
    }

    public static int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String getPackageName() {
        try {
            PackageManager packageManager = getContext().getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packInfo = packageManager.getPackageInfo(getContext().getPackageName(), PackageManager.GET_SIGNATURES);
            return packInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAppVersionName() {
        try {
            PackageManager packageManager = getContext().getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packInfo = packageManager.getPackageInfo(getContext().getPackageName(), PackageManager.GET_SIGNATURES);
            return packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    /**
     * 判断通知是否开启（非单个消息渠道）
     *
     * @param context 上下文
     * @return true 开启
     * API19 以上可用
     */
    public static boolean checkNotificationsEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    /**
     * 跳转到通知设置View
     *
     * @param activity activity
     */
    public static void gotoNotificationView(Activity activity) {
        String packageName = getPackageName();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, activity.getApplicationInfo().uid);
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", packageName);
                intent.putExtra("app_uid", activity.getApplicationInfo().uid);
                activity.startActivity(intent);
            } else {
                // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
                Intent intent = new Intent();
                //下面这种方案是直接跳转到当前应用的设置界面。
                //https://blog.csdn.net/ysy950803/article/details/71910806
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", packageName, null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        } catch (Exception e) {
            ALog.exception("Widget", "gotoNotificationView", e);
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            Intent intent = new Intent();
            //下面这种方案是直接跳转到当前应用的设置界面。
            //https://blog.csdn.net/ysy950803/article/details/71910806
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
            activity.startActivity(intent);
        }
    }

    /**
     * 跳转Google play
     *
     * @param context 上下文
     */
    public static void rateNow(final Context context) {
        try {
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            if (intent1.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent1);
                if(context instanceof Activity){
                    ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.anim_none);
                }
            } else {
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("market://details?id=" + getPackageName()));
                if (intent2.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent2);
                    if(context instanceof Activity){
                        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.anim_none);
                    }
                }
            }
        } catch (Throwable e) {
            Log.e("ActivityNotFound", "GoogleMarket Intent not found");
        }
    }

    /**
     * 跳转Google play
     *
     * @param context 上下文
     */
    public static void rateNow(final Context context, String url) {
        try {
            if(TextUtils.isEmpty(url)) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                if (intent1.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent1);
                }else{
                    Intent intent2 = new Intent(Intent.ACTION_VIEW);
                    intent2.setData(Uri.parse("market://details?id=" + getPackageName()));
                    if (intent2.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent2);
                    }
                }
            }else{
                String packageName = "";
                if(url.contains("id=")) {
                    String[] strArr = url.split("id=");
                    packageName = strArr[1];
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (!TextUtils.isEmpty(packageName)) {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                } else {
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                }
                if(intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        } catch (ActivityNotFoundException activityNotFoundException1) {
            Log.e("ActivityNotFound", "GoogleMarket Intent not found");
        }
    }

    public static String timeToDate(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String timeNow = formatter.format(time);
        return timeNow;
    }

    @SuppressLint("DefaultLocale")
    public static String getViewNum(int viewNum) {
        if(viewNum==0) {
            return "0";
        }
        viewNum = viewNum * AppCommonConfig.bookViewMultiple;
        if (viewNum >= 0 && viewNum <= 999) {
            return viewNum + "";
        } else if (viewNum >= 1000 && viewNum <= 999999) {
            if (viewNum % 1000 == 0) {
                return String.format("%.0f", viewNum/1000.f) + "K";
            }
            return String.format("%.1f", viewNum/1000.f) + "K";
        } else if (viewNum >= 1000000 && viewNum <= 999999999) {
            if (viewNum % 1000000 == 0) {
                return String.format("%.0f", viewNum/1000.f) + "K";
            }
            return String.format("%.1f", viewNum/1000000.f) + "M";
        } else {
            if (viewNum % 10000000 == 0) {
                return String.format("%.0f", viewNum/1000.f) + "K";
            }
            return String.format("%.1f", viewNum/10000000.f) + "B";
        }
    }

    @SuppressLint("DefaultLocale")
    public static String getBookWords(int viewNum) {
        if(viewNum==0) {
            return "0";
        }
        if (viewNum >= 0 && viewNum <= 999) {
            return viewNum + "";
        } else if (viewNum >= 1000 && viewNum <= 999999) {
            if (viewNum % 1000 == 0) {
                return String.format("%.0f", viewNum/1000.f) + "K";
            }
            return String.format("%.1f", viewNum/1000.f) + "K";
        } else if (viewNum >= 1000000 && viewNum <= 999999999) {
            if (viewNum % 1000000 == 0) {
                return String.format("%.0f", viewNum/1000.f) + "K";
            }
            return String.format("%.1f", viewNum/1000000.f) + "M";
        } else {
            if (viewNum % 10000000 == 0) {
                return String.format("%.0f", viewNum/1000.f) + "K";
            }
            return String.format("%.1f", viewNum/10000000.f) + "B";
        }
    }

    @SuppressLint("DefaultLocale")
    public static String getFormatCoin(int coin) {
        if(coin==0) {
            return "0";
        }
        if (coin >= 0 && coin <= 999) {
            return coin + "";
        } else if (coin >= 1000 && coin <= 999999) {
            if (coin % 1000 == 0) {
                return String.format("%.0f", coin/1000.f) + "K";
            }
            String content = format(coin/1000.f) + "K";
            return content;
        } else if (coin >= 1000000 && coin <= 999999999) {
            if (coin % 1000000 == 0) {
                return String.format("%.0f", coin/1000.f) + "K";
            }
            return format(coin/1000000.f) + "M";
        } else {
            if (coin % 10000000 == 0) {
                return String.format("%.0f", coin/1000.f) + "K";
            }
            return format(coin/10000000.f) + "B";
        }
    }

    public static String format(double value) {
        DecimalFormat df = new DecimalFormat("#0.0");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        decimalFormatSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decimalFormatSymbols);
        String format = df.format(value);
        if(!TextUtils.isEmpty(format)&&format.contains(",")) {
            format = format.replace(",", ".");
        }
        return format;
    }

    /**
     * md5字符串
     *
     * @param string 源字符串
     * @return 解析后的字符串
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 复制
     *
     * @param data 待复制文本
     */
    public static void copy(String data) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
        // newHtmlText、
        // newIntent、
        // newUri、
        // newRawUri
        ClipData clipData = ClipData.newPlainText(null, data);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }

    public static String getChannel() {
        return "10101";
    }

    private static final String[] BG_COLOR = {"#6C0000", "#B44444", "#620051", "#B44485", "#090062",
            "#4E44B4", "#001A62", "#4471B4", "#005862", "#44A6B4", "#006221", "#44B444",
            "#623E00", "#B48A44", "#373737", "#939393"};

    private static int[] int2rgb(int color) {
        return new int[]{(color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF};
    }

    private static int[] hex2rgb(String hex) {
        try {
            int i = Integer.decode(hex);
            return int2rgb(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static class BgColor {
        BgColor(String colorHex, double hsvDistance) {
            this.colorHex = colorHex;
            this.hsvDistance = hsvDistance;
        }

        String colorHex;
        double hsvDistance;
    }

    static class HSV {

        HSV(int[] rgb) {
            float[] tempHSV = new float[3];
            Color.colorToHSV(Color.argb(255, rgb[0], rgb[1], rgb[2]), tempHSV);
            H = tempHSV[0];
            S = tempHSV[1];
            V = tempHSV[2];
        }

        float H;
        float S;
        float V;
    }

    static void setHeaderBackgroundColor(String colorHex, ImageView imageView, View headerView) {
        int[] colors;
        int[] headerColors;
        if (colorHex != null) {
            colors = new int[]{Color.parseColor(colorHex), Color.parseColor(colorHex.replace("#", "#70"))};
            headerColors = new int[]{Color.parseColor(colorHex), Color.parseColor(colorHex.replace("#", "#ff"))};
        } else {
            colors = new int[]{Color.parseColor("#001138"), Color.parseColor("#929FBD")};
            headerColors = new int[]{Color.parseColor("#001138"), Color.parseColor("#929FBD")};
        }
        if (null != imageView) {
            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors);
            imageView.setAlpha(0);
            imageView.setBackground(drawable);
            imageView.animate().alpha(1.0f).setDuration(1000).start();
        }
        if (null != headerView) {
            GradientDrawable drawable1 = new GradientDrawable(GradientDrawable.Orientation.BL_TR, headerColors);
            headerView.setBackground(drawable1);
            headerView.animate().alpha(1.0f).setDuration(1000).start();
        }
    }

    public static void changeImageViewBackground(Bitmap bitmap, ImageView imageView, View headerView) {
        Palette.from(bitmap).generate(palette -> {
            if (palette != null) {
                List<Palette.Swatch> swatches = new ArrayList<>(palette.getSwatches());
                Collections.sort(swatches, (o1, o2) -> o2.getPopulation() - o1.getPopulation());
                int mainColor = swatches.get(0).getRgb();
                int[] mainColorRGB = int2rgb(mainColor);
                int bgLength = BG_COLOR.length;
                List<BgColor> colors = new ArrayList<>(bgLength);
                for (String colorHex : BG_COLOR) {
                    int[] colorRGB = hex2rgb(colorHex);
                    if (colorRGB == null) {
                        continue;
                    }
                    final double R = 100;
                    final double angle = 30;
                    final double h = R * Math.cos(angle / 180 * Math.PI);
                    final double r = R * Math.sin(angle / 180 * Math.PI);
                    HSV hsv1 = new HSV(mainColorRGB);
                    HSV hsv2 = new HSV(colorRGB);
                    double x1 = r * hsv1.V * hsv1.S * Math.cos(hsv1.H / 180 * Math.PI);
                    double y1 = r * hsv1.V * hsv1.S * Math.sin(hsv1.H / 180 * Math.PI);
                    double z1 = h * (1 - hsv1.V);
                    double x2 = r * hsv2.V * hsv2.S * Math.cos(hsv2.H / 180 * Math.PI);
                    double y2 = r * hsv2.V * hsv2.S * Math.sin(hsv2.H / 180 * Math.PI);
                    double z2 = h * (1 - hsv2.V);
                    double dx = x1 - x2;
                    double dy = y1 - y2;
                    double dz = z1 - z2;
                    double hsvDistance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    colors.add(new BgColor(colorHex, hsvDistance));
                }
                Collections.sort(colors, (o1, o2) -> {
                    if (o1.hsvDistance == o2.hsvDistance) {
                        return 0;
                    } else {
                        return o1.hsvDistance < o2.hsvDistance ? -1 : 1;
                    }
                });
                setHeaderBackgroundColor(colors.get(0).colorHex, imageView, headerView);
            } else {
                setHeaderBackgroundColor(null, imageView, headerView);
            }
        });
    }

    /**
     * 设置屏幕常亮
     *
     * @param activity activity
     */
    public static void keepScreenOn(Activity activity) {
        try {
            activity.runOnUiThread(() -> {
                //
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 状态栏、导航栏全透明去阴影（5.0以上）
     */
    public static void setStatusNavBarColor(Activity activity, int statusColorInt, int navColorInt) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(statusColorInt);
        window.setNavigationBarColor(navColorInt);
    }

    public static String format(String format, Object... args) {
        return new Formatter(Locale.getDefault()).format(format, args).toString();
    }
}