package com.wei.commonlibrary.dialog;

import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.commonlibrary.R;
import com.wei.commonlibrary.utils.FormatUtil;
import com.wei.commonlibrary.utils.ToastUtil;

/**
 * Created by ${wei} on 2017/4/17.
 */

public class EditTextDialog extends BaseDialog {

    private EditText editText;

    protected EditTextDialog(Builder builder) {
        super(builder);
    }

    @Override
    protected void createDialog(final BaseDialog.Builder builde) {
        View view = View.inflate(builde.getContext(), R.layout.dialog_layout_edittext, null);
        mDialog = new Dialog(builde.getContext(),R.style.DialogStyle_round);
        mDialog.setContentView(view);
        editText = (EditText) view.findViewById(R.id.dialog_edit);
        TextView tv_change = (TextView) view.findViewById(R.id.dialog_tv_change);
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    String text = editText.getText().toString();
                    if (FormatUtil.regexPhone(text)) {
                        mListener.rightClick(text);
                        dismiss();
                    } else {
                        ToastUtil.showShort(builde.getContext(), "请输入正确的手机号");
                    }
                }
            }
        });
        resetSize(mDialog, Gravity.CENTER,0.85f,0);
    }

    public static class Builder extends BaseDialog.Builder {
        @Override
        public BaseDialog build() {
            return new EditTextDialog(this);
        }
    }
}
