package com.wei.commonlibrary.net;

import android.os.Handler;
import android.os.Message;

import com.wei.commonlibrary.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ${wei} on 2017/5/15.
 */

public class HttpDownload {
    private static final String TAG = "debug_HttpDownload";
    /**
     * @param apkUrl   下载地址
     * @param savePath 保存地址
     */
    private InputStream is;
    private OutputStream os;
    private OnDownloadListener mListener;

    private final int DOWNLOADING = 0;
    private final int DOWNLOAD_SUCCESS = 1;
    private final int DOWNLOAD_ERROR = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOADING://下载中
                    int fileLength = msg.arg1;
                    int downLoadLength = msg.arg2;
                    if (mListener != null) {
                        mListener.onDownloading(fileLength, downLoadLength);
                    }
                    break;
                case DOWNLOAD_SUCCESS://下载完毕
                    if (mListener != null) {
                        mListener.onDownload();
                    }
                    break;
                case DOWNLOAD_ERROR://下载异常
                    if (mListener != null) {
                        mListener.onError(new CommException("网络连接异常"));
                    }
                    break;
            }
        }
    };

    public HttpDownload downFile(final String apkUrl, final File saveFile) {
        new Thread() {
            @Override
            public void run() {
                downloadFile(apkUrl, saveFile);
            }
        }.start();
        return this;
    }

    private void downloadFile(String apkUrl, File saveFile) {
        try {
            URL url = new URL(apkUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent", "Android");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                int fileLenght = conn.getContentLength();//获取文件总大小
                os = new FileOutputStream(saveFile);
                byte[] buffer = new byte[1024 * 2];
                int len = -1;
                int downFileLength = 0;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);

                    downFileLength += len;
                    //刷新进度
                    Message message = mHandler.obtainMessage(DOWNLOADING, fileLenght, downFileLength);
                    mHandler.sendMessage(message);
                }
                os.flush();
                //下载结束
                mHandler.sendEmptyMessage(DOWNLOAD_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
            }
        } catch (MalformedURLException e) {
            LogUtil.e(TAG, "102.downloadFile. = " + e.toString());
            mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
            e.printStackTrace();
        } catch (IOException e) {
            LogUtil.e(TAG, "106.downloadFile. = " + e.toString());
            mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        this.mListener = listener;
    }

    public interface OnDownloadListener {
        /**
         * 开始下载
         *
         * @param fileLength 文件总大小
         * @param currLength 下载进度
         */
        void onDownloading(long fileLength, long currLength);

        /**
         * 下载完成
         */
        void onDownload();

        /**
         * 网络异常
         */
        void onError(CommException exception);
    }
}
