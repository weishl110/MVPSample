package com.wei.commonlibrary.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ${wei} on 2017/4/20.
 */

public class BitmapCommUtils {

    //存放图片压缩后的路径
    private static ArrayList<String> mCompressList = new ArrayList<>();
    /**
     * 根据路径从硬盘中读取图片
     *
     * @param path      图片路径
     * @param reqWidth  显示宽度
     * @param reqHeight 显示高度
     * @return 图片Bitmap
     */
    private static final String TAG = "debug_BitmapCommUtils";

    public static Bitmap decodeBitmapFromDisk(String path, int reqWidth, int reqHeight) {

        Bitmap bmp = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            if (options.outWidth == -1 || options.outHeight == -1) {
                try {
                    ExifInterface exifInterface = new ExifInterface(path);
                    int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                    int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                    options.outWidth = width;
                    options.outHeight = height;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //初始压缩比例
            options.inSampleSize = calculateBitmapSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(path, options);
            int degree = readPictureDegree(path);
            if (degree != 0) {
                bmp = rotateBitmap(bmp, degree);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "44.decodeBitmapFromDisk. catch = " + e.toString());
        }
        return bmp;
    }

    /**
     * 解析原图
     *
     * @param path
     * @return
     */
    public static Bitmap decodeOriginalBitmap(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    /**
     * 根据路径获取图片信息并按比例压缩，返回bitmap
     *
     * @param filePath 图片路径
     * @param percent  压缩比例
     */
    public static Bitmap getSmallBitmap(String filePath, int percent) {
        File file = new File(filePath);
        int minKb = 1024 * 300;//300kb
        if (file.length() / 1024 < 500) {
            return getBitmap(filePath);
        }
        //压缩图片
        float temp = file.length() / (float) minKb;
        percent = temp < percent ? Math.round(temp) : percent;
        Bitmap bitmap = getCompressBitmap(filePath, percent);

        //获取压缩后的图片大小，判断压缩后的图片是否小于500kb
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int length = baos.toByteArray().length;
        if (length / 1024 < 500) {
            return bitmap;
        }
        int tempPercent = length / minKb;
        bitmap.recycle();
        bitmap = getCompressBitmap(filePath, tempPercent + percent);
        return bitmap;
    }

    private static Bitmap getBitmap(String filePath) {
        int minKb = 1024 * 400;//300kb
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int length = baos.toByteArray().length;
        bitmap.recycle();
        if (length / 1024 < 500) {
            return BitmapFactory.decodeFile(filePath);
        }
        int tempPercent = length / minKb;
        return getCompressBitmap(filePath, tempPercent);
    }

    /**
     * 根据压缩比压缩指定路径图片
     *
     * @param filePath
     * @param percent
     * @return
     */
    private static Bitmap getCompressBitmap(String filePath, int percent) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = percent;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * 读取照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(exifInterface.TAG_ORIENTATION, exifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param bitmap
     * @param degress 旋转角度
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degress);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }

    /**
     * 计算压缩率
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateBitmapSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (options == null) {
            return 1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
//            inSampleSize = Math.max(height / reqHeight, width / reqWidth);
            //使用需要的宽高的最大值来计算比率
            final int suitedValue = reqHeight > reqWidth ? reqHeight : reqWidth;
            final int heightRatio = Math.round((float) height / (float) suitedValue);
            final int widthRatio = Math.round((float) width / (float) suitedValue);

            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;//用最大
        }
        return inSampleSize;
    }


    /**
     * 删除指定路径的图片
     *
     * @param context
     * @param path    图片路径
     */
    public static void deleteBitmap(Context context, String path) {
        if (!TextUtils.isEmpty(path)) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));//刷新图库

            ContentResolver resolver = context.getContentResolver();
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = MediaStore.Images.Media.query(resolver, contentUri, new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + "=?", new String[]{path}, null);
            if (cursor.moveToFirst()) {
                String where = MediaStore.Images.Media.DATA + "='" + path + "'";
                int count = resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null);
            }
            File file = new File(path);
            if (file.exists()) {
                boolean result = file.delete();
            }
            if (mCompressList.contains(path)) {
                mCompressList.remove(path);
            }
        }
    }

    /**
     * 删除压缩后的全部图片及文件夹
     */
    public static void deleteCompressBitmap() {
        String dirPath = getDirPath();
        if (TextUtils.isEmpty(dirPath)) {
            int size = mCompressList.size();
            for (int i = 0; i < size; i++) {
                File file = new File(mCompressList.get(i));
                if (file.exists()) {
                    file.delete();
                }
            }
        } else {
            File dirFile = new File(dirPath);
            if (dirFile.exists()) {
                File[] files = dirFile.listFiles();
                int length = files.length;
                for (int i = 0; i < length; i++) {
                    if (files[i].isFile() && files[i].exists()) {
                        files[i].delete();
                    }
                }
                dirFile.delete();
            }
        }
        mCompressList.clear();
    }

    /**
     * 采用Rxjava 在子线程压缩图片
     *
     * @param filePath
     * @param action1
     */
    public static void compressImage(String filePath, Action1<String> action1) {
        final String path = filePath;
        Observable.just(filePath).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String filePath) {
                Bitmap bitmap = getSmallBitmap(filePath, 7);//压缩图片
                if (bitmap == null) {
                    return null;
                }
                int degree = readPictureDegree(filePath);
                if (degree > 0) {
                    bitmap = rotateBitmap(bitmap, degree);
                }
                return bitmap;
            }
        }).map(new Func1<Bitmap, String>() {
            @Override
            public String call(Bitmap bitmap) {
                if (bitmap == null) {
                    return "";
                }
                String fileFolder = path.substring(0, path.lastIndexOf("/") + 1);
                String tempPath = getDirPath();
                if (!TextUtils.isEmpty(tempPath)) {
                    fileFolder = tempPath;
                }
                String name = path.substring(path.lastIndexOf("."));
                String targetPath = String.format("%s%s%s", fileFolder, System.currentTimeMillis(), name);
                FileOutputStream fos = null;
                try {
                    File file = new File(targetPath);
//                    if (file.exists()) {
//                        file.delete();
//                    }
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                    }

                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    LogUtil.e(TAG, "293.call. = " + file.length() + "  " + file.getAbsolutePath());
                    String compressPath = file.getPath();
                    mCompressList.add(compressPath);
                    return compressPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return targetPath;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    private static String getDirPath() {
        return GlobalContext.context.getExternalCacheDir().getAbsolutePath()+"/compress/";
    }
}
