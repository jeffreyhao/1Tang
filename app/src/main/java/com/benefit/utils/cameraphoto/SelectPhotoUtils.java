package com.benefit.utils.cameraphoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.benefit.novelverse.R;
import com.meta.ui.widget.IosBottomDialog;
import com.baidu.baselibrary.Widget;
import com.tencent.common.util.Abase;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.core.content.FileProvider;

/**
 * @author: yuhaibo
 * Description: 选取图片的工具类
 */
public class SelectPhotoUtils {
    public static final String fileName = "avatar.png";
    public static final int REQUEST_CODE_ALBUM = 2;//相册
    public static final int REQUEST_CODE_CROUP_PHOTO = 3;//裁剪
    public static File file;
    public static Uri uri;
    private Activity activity;
    private static SelectPhotoUtils selectPhotoUtils;
    private double aspectX = 1;
    private double aspectY = 1;

    public static File getFile() {
        return file;
    }

    public static void setFile(File file) {
        SelectPhotoUtils.file = file;
    }

    public synchronized static SelectPhotoUtils getInstance(Activity activity) {
        if (selectPhotoUtils == null) {
            selectPhotoUtils = new SelectPhotoUtils();
        }
        selectPhotoUtils.activity = activity;
        file = new File(FileUtil.getCachePath(Abase.getContext()), fileName);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            //通过FileProvider创建一个content类型的Uri(android 7.0需要这样的方法跨应用访问)
            uri = FileProvider.getUriForFile(Abase.getContext(), activity.getPackageName() + ".fileprovider", file);
        }
        return selectPhotoUtils;
    }

    /**
     * 显示仿iOS的弹窗
     */
    public SelectPhotoUtils showPopupWindow() {
        IosBottomDialog.Builder builder = new IosBottomDialog.Builder(activity);
        builder.setTitle(Widget.getResourcesString(R.string.pp_notiy), Color.RED).addOption(Widget.getResourcesString(R.string.pp_get_photo),
                Color.DKGRAY, new IosBottomDialog.OnOptionClickListener() {
            @Override
            public void onOptionClick() {
                uploadAvatarFromAlbumRequest();
            }
        }).create().show();
        return selectPhotoUtils;
    }

    /**
     * 相册
     */
    public SelectPhotoUtils uploadAvatarFromAlbumRequest() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, REQUEST_CODE_ALBUM);
        return this;
    }

    /**
     * 裁剪拍照裁剪
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", aspectX);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", aspectY);// x:y=1:1
        intent.putExtra("outputX", 500);//图片输出大小
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);//黑边
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());// 返回格式
        intent.putExtra("noFaceDetection", true); // no face detection

        activity.startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
    }

    /**
     * 上传图片
     */
    public void uploadAvatarFromPhoto(ImageView imageView) {
        compressAndUploadAvatar(file.getPath(), imageView);
    }

    /**
     * 压缩并显示
     *
     * @param fileSrc
     */
    private void compressAndUploadAvatar(String fileSrc, ImageView imageView) {
        File cover = FileUtil.getSmallBitmap(activity, fileSrc);
        Uri uri = Uri.fromFile(cover);
        //图片Uri
        imageView.setImageBitmap(decodeUriAsBitmap(uri));
    }

    /**
     * 将uri转换成bitmap
     *
     * @param uri
     * @return
     */
    public Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

}
