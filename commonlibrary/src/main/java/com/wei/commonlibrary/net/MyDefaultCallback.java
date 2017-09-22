package com.wei.commonlibrary.net;

import android.app.Activity;
import android.content.Context;

import com.wei.commonlibrary.dialog.LoadingDialog;
import com.wei.commonlibrary.utils.LogUtil;

/**
 * Created by ${wei} on 2017/5/4.
 */

public abstract class MyDefaultCallback<T> extends ResultCallback<T> {

    private Activity mActivity;
    private boolean mIsShowloading;
    private LoadingDialog loading;
    private boolean isAfter;

    /**
     * @param activity
     * @param isShowloading {true 显示loading false 不显示}
     */
    public MyDefaultCallback(Activity activity, boolean isShowloading) {
        this.mActivity = activity;
        this.mIsShowloading = isShowloading;
    }

    private static final String TAG = "debug_MyDefaultCallback";

    @Override
    public void onBefore() {
        if (mIsShowloading) {
            if (loading != null && loading.isShowing()) {
                loading.dismiss();
            }
            if (mActivity != null) {
                loading = (LoadingDialog) new LoadingDialog.Builder().setContext(mActivity).build();
                loading.show();
            }
//            loading.setOnkeyListener();
            LogUtil.e(TAG, "38.onBefore. = ");
            isAfter = false;
        }
    }

    @Override
    public void onAfter() {
//        if (mIsShowloading) {
        LogUtil.e(TAG, "50.onAfter. = ");
        if (loading != null) {
            LogUtil.e(TAG, "50.onAfter.d = dismiss");
            loading.dismiss();
        }
//        loading = null;
        isAfter = true;
//        }
    }

    @Override
    public Context getContext() {
        return mActivity.getApplicationContext();
    }

    @Override
    public boolean isAfter() {
        return isAfter;
    }
}