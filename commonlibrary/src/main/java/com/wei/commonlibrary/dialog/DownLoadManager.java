package com.wei.commonlibrary.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.wei.commonlibrary.manager.IListener;
import com.wei.commonlibrary.manager.RxManager;
import com.wei.commonlibrary.net.CommException;
import com.wei.commonlibrary.net.HttpDownload;
import com.wei.commonlibrary.utils.ToastUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by ${wei} on 2017/5/16.
 * 下载新版本
 */

public class DownLoadManager {
    public static void startDownload(Activity activity, String downloadUrl, final OnDownloadListener listener) {
        final Activity lActivity = activity;
        final String apkUrl = downloadUrl;
        //查看是否打开读写SD卡的权限

        RxManager.requestPermission(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new IListener.IPermissionListener() {
                    @Override
                    public void call(boolean granted) {
                        if (!granted) {
                            ToastUtil.showShort(lActivity, "需开启读写SD卡权限");
                            listener.onCancel();
                            return;
                        }
                        //开始下载
                        showDownload(lActivity, apkUrl, listener);
                    }
                });
    }

    private static void showDownload(Activity activity, String downloadUrl, final OnDownloadListener listener) {
        final DownloadDialog dialog = new DownLoadManager().showDownloadDialog(activity);//显示下载进度
        try {//下载保存路径
            String savePath = Environment.getExternalStorageDirectory() + "/jtyw/downFile";
            File file = new File(savePath);
            //查看文件夹是否存在，没有则创建
            if (!file.exists()) {
                file.mkdirs();
            }

            final String apkPath = savePath + "/jtyw.apk";
            final File apkFile = new File(apkPath);
            //如果该文件已经存在，则直接删除
            if (apkFile.exists()) {
                apkFile.delete();
            }
            if (!apkFile.exists()) {
                apkFile.createNewFile();
            }
            //开始下载
            new HttpDownload().downFile(downloadUrl, apkFile).setOnDownloadListener(new HttpDownload.OnDownloadListener() {
                @Override
                public void onDownloading(long fileLength, long currLength) {
                    float percent = (float) currLength / fileLength;
                    dialog.setPercent(percent);
                }

                @Override
                public void onDownload() {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onDownSuccess(apkFile);
                    }
                }

                @Override
                public void onError(CommException exception) {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onError(exception);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载进度条 Dialog
     */
    private DownloadDialog showDownloadDialog(Context context) {
        DownloadDialog downloadDialog = (DownloadDialog) new DownloadDialog.Builder()
                .setContext(context).build();
        downloadDialog.show();
        return downloadDialog;
    }

    public interface OnDownloadListener {
        void onDownSuccess(File apkFile);

        void onCancel();

        void onError(CommException exception);
    }
}
