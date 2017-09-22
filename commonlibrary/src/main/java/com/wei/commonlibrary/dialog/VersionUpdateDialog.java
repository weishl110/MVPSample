package com.wei.commonlibrary.dialog;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonlibrary.R;
import com.wei.commonlibrary.utils.StringUtil;

/**
 * Created by ${wei} on 2017/5/25.
 * <p>
 * 版本更新
 */

public class VersionUpdateDialog extends BaseDialog implements View.OnClickListener {

    protected VersionUpdateDialog(Builder builder) {
        super(builder);
    }

    @Override
    protected void createDialog(BaseDialog.Builder builde) {
        View view = View.inflate(builde.getContext(), R.layout.dialog_layout_version_update, null);
        mDialog = new Dialog(builde.getContext(), R.style.DialogStyle_round);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        TextView tv_left = (TextView) view.findViewById(R.id.dialog_tv_left);
        TextView tv_right = (TextView) view.findViewById(R.id.dialog_tv_right);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.dialog_iv_icon);
        TextView tv_versionCode = (TextView) view.findViewById(R.id.dialog_tv_version_code);
        TextView tv_desc = (TextView) view.findViewById(R.id.dialog_tv_desc);
        TextView tv_versionSize = (TextView) view.findViewById(R.id.dialog_tv_version_size);

        tv_left.setTag(0);
        tv_right.setTag(1);
        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        //设置图标
        Drawable iconDrawable = builde.getIconDrawable();
        if (iconDrawable != null) {
            iv_icon.setImageDrawable(iconDrawable);
        }
        //按钮左侧文案
        String leftText = builde.getLeftText();
        if (!StringUtil.isEmpty(leftText)) {
            if (tv_left.getVisibility() == View.GONE) {
                tv_left.setVisibility(View.VISIBLE);
            }
            tv_left.setText(leftText);
        }
        //右侧按钮文案
        String rightText = builde.getRightText();
        if (!StringUtil.isEmpty(rightText)) {
            if (tv_right.getVisibility() == View.GONE) {
                tv_right.setVisibility(View.VISIBLE);
            }
            tv_right.setText(rightText);
        }
        //版本号
        String versionCode = builde.getVersionCode();
        if (!StringUtil.isEmpty(versionCode)) {
            versionCode = String.format("%s%s", "版本号:v", versionCode);
            tv_versionCode.setText(versionCode);
        }
        //版本大小
        String versionSize = builde.getVersionSize();
        if (!StringUtil.isEmpty(versionSize)) {
            tv_versionSize.setText(versionSize);
        }
        //版本描述
        String content = builde.getContent();
        if (!StringUtil.isEmpty(content)) {
            tv_desc.setText(content);
        }
        //设置dialog大小
        resetSize(mDialog, Gravity.CENTER, 0.75f, 0.4f);
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        switch (tag) {
            case 0:
                if (mListener != null) {
                    mListener.leftClick();
                }
                break;
            case 1:
                if (mListener != null) {
                    mListener.rightClick(null);
                }
                break;
        }
    }

    public static final class Builder extends BaseDialog.Builder {
        @Override
        public BaseDialog build() {
            return new VersionUpdateDialog(this);
        }
    }
}
