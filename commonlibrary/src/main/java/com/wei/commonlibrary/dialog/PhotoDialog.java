package com.wei.commonlibrary.dialog;

import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.commonlibrary.R;

/**
 * Created by ${wei} on 2017/4/20.
 * 选择照片、拍照
 */

public class PhotoDialog extends BaseDialog implements View.OnClickListener {


    private final int TAG_CAMERA = 0;
    private final int TAG_PHOTO = 1;
    private final int TAG_CANCEL = 2;

    private OnDialogClickListener mPhotoListener;
    private View view;

    private final OnDialogClickListener DEFAULT = new OnDialogClickListener() {
        @Override
        public void onCamera() {

        }

        @Override
        public void onAlbum() {

        }
    };

    protected PhotoDialog(BaseDialog.Builder builder) {
        super(builder);
    }

    @Override
    protected void createDialog(BaseDialog.Builder builde) {
        mDialog = new Dialog(builde.getContext(), R.style.DialogStyle_bottom);
        view = View.inflate(builde.getContext(), R.layout.dialog_layout_photo, null);
        mDialog.setContentView(view);
        TextView tv_camera = (TextView) view.findViewById(R.id.dialog_tv_camera);
        TextView tv_photo = (TextView) view.findViewById(R.id.dialog_tv_photo);
        TextView tv_cancel = (TextView) view.findViewById(R.id.dialog_tv_cancel);
        tv_camera.setTag(TAG_CAMERA);
        tv_cancel.setTag(TAG_CANCEL);
        tv_photo.setTag(TAG_PHOTO);
        tv_camera.setOnClickListener(this);
        tv_photo.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        //设置dialog的宽高
        resetSize(mDialog, Gravity.BOTTOM, 1, 0);
    }

    @Override
    public void onClick(View v) {
        if (mPhotoListener == null) {
            mPhotoListener = DEFAULT;
        }
        int tag = (int) v.getTag();
        switch (tag) {
            case TAG_CAMERA:
                dismiss();
                mPhotoListener.onCamera();
                break;
            case TAG_PHOTO:
                dismiss();
                mPhotoListener.onAlbum();
                break;
            case TAG_CANCEL:
                dismiss();
                break;
        }
    }


    public static class Builder extends BaseDialog.Builder {
        @Override
        public BaseDialog build() {
            return new PhotoDialog(this);
        }
    }

    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.mPhotoListener = listener;
    }

    public interface OnDialogClickListener {
        /**
         * 相机
         */
        void onCamera();

        /**
         * 照片
         */
        void onAlbum();
    }
}
