package com.wei.commonlibrary.dialog;

import android.view.Gravity;
import android.view.View;

import com.example.commonlibrary.R;
import com.wei.commonlibrary.view.LoadingView;

/**
 * Created by ${wei} on 2017/5/4.
 * 加载loading
 */

public class LoadingDialog extends BaseDialog {

    private LoadingView loadingView;

    protected LoadingDialog(Builder builder) {
        super(builder);
    }

    @Override
    protected void createDialog(BaseDialog.Builder builde) {
        super.createDialog(builde);
        View view = View.inflate(builde.getContext(), R.layout.layout_loading, null);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        loadingView = (LoadingView) view.findViewById(R.id.loadingview);
        resetSize(mDialog, Gravity.CENTER, 0.33, 0.065);
    }


    @Override
    public void show() {
        if (loadingView != null) {
            loadingView.startAnimator(true);
        }
        super.show();
    }

    @Override
    public void dismiss() {
        if (loadingView != null) {
            loadingView.startAnimator(false);
        }
        super.dismiss();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public static class Builder extends BaseDialog.Builder {
        @Override
        public BaseDialog build() {
            return new LoadingDialog(this);
        }
    }
}
