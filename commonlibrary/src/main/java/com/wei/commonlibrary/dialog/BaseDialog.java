package com.wei.commonlibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.example.commonlibrary.R;
import com.wei.commonlibrary.utils.DisplayUtil;

/**
 * Created by admin on 2017/3/30.
 */

public abstract class BaseDialog {

    private Builder mBuilder;
    protected Dialog mDialog;
    protected OnDialogListener mListener;

    protected BaseDialog(Builder builder) {
        mDialog = new Dialog(builder.getContext(), R.style.DialogStyle_nomal);
        createDialog(builder);
        this.mBuilder = builder;
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.cancel();
            mDialog.dismiss();
            mListener = null;
//            mDialog = null;
        }
    }

    public boolean isShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        } else {
            return false;
        }
    }

    public void show() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog.show();
        }
    }

    public void cancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
//            mDialog = null;
            mListener = null;

        }
    }


    public void setOnkeyListener() {
        setDialogOnKeyListener();
    }

    private void setDialogOnKeyListener() {
        if (mDialog != null) {
            mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    dismiss();
                    AppCompatActivity activity = (AppCompatActivity) mBuilder.getContext();
                    activity.onBackPressed();
                    return false;
                }
            });
        }
    }

    /**
     * 创建dialog
     *
     * @param builde
     */
    protected void createDialog(Builder builde) {

    }

    public Dialog getDialog() {
        return mDialog;
    }


    /**
     * 设置dialog的宽高
     *
     * @param dialog
     * @param gravity       显示位置  中间 和 下面
     * @param widthPrecent  宽度比例 0~1
     * @param heightPrecent <=0:wrap_content 否则：height * heightprecent
     */
    public void resetSize(Dialog dialog, int gravity, double widthPrecent, double heightPrecent) {
        Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        WindowManager.LayoutParams lp = window.getAttributes();
        float[] screenWH = DisplayUtil.getScreenWH(dialog.getContext());
        if (widthPrecent == 0) {
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.width = (int) (screenWH[0] * widthPrecent);
        }
        if (heightPrecent <= 0) {
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.height = (int) (screenWH[1] * heightPrecent);
        }
        lp.gravity = gravity;
        window.setAttributes(lp);
    }

    public interface OnDialogListener {
        void rightClick(String text);

        void leftClick();
    }

    public void setOnDialogListener(OnDialogListener listener) {
        this.mListener = listener;
    }

    public static class SimpleOnDialogListener implements OnDialogListener {

        @Override
        public void rightClick(String text) {

        }

        @Override
        public void leftClick() {

        }
    }

    public abstract static class Builder {

        private String title;
        private String leftText;
        private String rightText;
        private String content;
        private int leftColor, rightColor;
        private Context context;
        private int gravity;
        private Drawable iconDrawable;
        private String versionCode;
        private String versionSize;
        private float heightPercent;

        public Context getContext() {
            return context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public String getLeftText() {
            return leftText;
        }

        public String getRightText() {
            return rightText;
        }

        public String getContent() {
            return content;
        }

        public int getLeftColor() {
            return leftColor;
        }

        public int getRightColor() {
            return rightColor;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setleftBtnText(String leftText) {
            this.leftText = leftText;
            return this;
        }

        public Builder setRightBtnText(String rightText) {
            this.rightText = rightText;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setLeftColor(int leftColor) {
            this.leftColor = leftColor;
            return this;
        }

        public Builder setRightColor(int rightColor) {
            this.rightColor = rightColor;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public int getGravity() {
            return gravity;
        }

        public Builder setHeightPercent(float heightPercent) {
            this.heightPercent = heightPercent;
            return this;
        }

        public float getHeightPercent() {
            return heightPercent;
        }

        public Builder setLeftText(String leftText) {
            this.leftText = leftText;
            return this;
        }

        public Builder setRightText(String rightText) {
            this.rightText = rightText;
            return this;
        }

        public Drawable getIconDrawable() {
            return iconDrawable;
        }

        public Builder setIconDrawable(Drawable iconDrawable) {
            this.iconDrawable = iconDrawable;
            return this;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public Builder setVersionCode(String versionCode) {
            this.versionCode = versionCode;
            return this;
        }

        public String getVersionSize() {
            return versionSize;
        }

        public Builder setVersionSize(String versionSize) {
            this.versionSize = versionSize;
            return this;
        }

        public abstract BaseDialog build();
    }

}
