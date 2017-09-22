package com.wei.commonlibrary.dialog;

import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlibrary.R;
import com.wei.commonlibrary.utils.StringUtil;

import butterknife.ButterKnife;

/**
 * Created by ${wei} on 2017/5/12.
 * <p>
 * 文本描述
 */

public final class TextDescDialog extends BaseDialog implements View.OnClickListener {

    protected TextDescDialog(Builder builder) {
        super(builder);
    }

    @Override
    protected void createDialog(BaseDialog.Builder builde) {
        View view = View.inflate(builde.getContext(), R.layout.dialog_layout_desc, null);
        mDialog = new Dialog(builde.getContext(), R.style.DialogStyle_round);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        TextView tv_title = (TextView) view.findViewById(R.id.dialog_tv_title);
        TextView tv_desc = (TextView) view.findViewById(R.id.dialog_tv_desc);
        TextView tv_left = (TextView) view.findViewById(R.id.dialog_tv_left);
        TextView tv_right = (TextView) view.findViewById(R.id.dialog_tv_right);

        tv_left.setTag(0);
        tv_right.setTag(1);
        tv_desc.setText(builde.getContent());
        String title = builde.getTitle();
        if (!StringUtil.isEmpty(title)) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }
        if (builde.getGravity() != -1) {
            tv_desc.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//            tv_desc.requestLayout();
        }
        String leftText = builde.getLeftText();
        if (!StringUtil.isEmpty(leftText)) {
            tv_left.setVisibility(View.VISIBLE);
            tv_left.setText(leftText);
        }
        String rightText = builde.getRightText();
        if (!StringUtil.isEmpty(rightText)) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(rightText);
        }

        float heightPercent = builde.getHeightPercent();
        if (heightPercent == 0) {
            heightPercent = 0.35f;
        }

        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        resetSize(mDialog, Gravity.CENTER, 0.8f, heightPercent);

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
                    mListener.rightClick("");
                }
                break;
        }
    }

    public static final class Builder extends BaseDialog.Builder {
        @Override
        public TextDescDialog build() {
            return new TextDescDialog(this);
        }
    }

}
