package com.wei.commonlibrary.dialog;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.commonlibrary.R;
import com.wei.commonlibrary.view.LineProgressView;

/**
 * Created by ${wei} on 2017/5/15.
 * 显示下载进度的dialog
 */

public class DownloadDialog extends BaseDialog {

    private LineProgressView progressView;
    private TextView tv_download;

    protected DownloadDialog(Builder builder) {
        super(builder);
    }

    @Override
    protected void createDialog(BaseDialog.Builder builde) {
        View view = View.inflate(builde.getContext(), R.layout.dialog_layout_download, null);

        //进度条
        tv_download = (TextView) view.findViewById(R.id.dialog_tv_download);
        progressView = (LineProgressView) view.findViewById(R.id.dialog_progress);

        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        resetSize(mDialog, Gravity.CENTER, 0.85f, 0.15f);
    }

    /**
     * 设置下载进度
     *
     * @param downloadPercent 0~1
     */
    public void setPercent(float downloadPercent) {
        String text = String.format("%s%d%s", "下载中... ", (int) (downloadPercent * 100), "%");
        tv_download.setText(text);
        progressView.setPercent(downloadPercent);
    }

    public static class Builder extends BaseDialog.Builder {
        @Override
        public DownloadDialog build() {
            return new DownloadDialog(this);
        }
    }
}
