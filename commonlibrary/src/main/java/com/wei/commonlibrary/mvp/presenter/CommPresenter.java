package com.wei.commonlibrary.mvp.presenter;

import android.support.annotation.Nullable;

import com.wei.commonlibrary.mvp.model.CommModelImpl;
import com.wei.commonlibrary.mvp.view.ICommView;

import java.lang.ref.WeakReference;

/**
 * Created by ${wei} on 2017/3/21.
 */

public abstract class CommPresenter<M extends CommModelImpl, V extends ICommView> implements IPresenter {

    protected static String TAG;
    //    protected V mView;
    protected M mModel;
    private boolean mCancel;
    private WeakReference<ICommView> mWeakRef;

    public CommPresenter() {
        TAG = "debug_" + this.getClass().getSimpleName();
        mModel = createModel();
    }

    @Override
    public void onStart() {
    }

    protected abstract M createModel();

    public final void setView(@Nullable V view) {
        mWeakRef = new WeakReference<ICommView>(view);
    }

    public final V getView() {
        if (mWeakRef == null || mWeakRef.get() == null) return null;

        return (V) mWeakRef.get();
    }

    protected final void detachView() {
        if (mModel != null) {
            mModel.onDestory();
        }
        if (mWeakRef.get() != null) {
            mWeakRef.clear();
        }
//        mView = null;
    }

    public final boolean isCancel() {
        return mCancel;
    }

    public final void cancel() {
        mCancel = true;
        if (mModel != null) {
            mModel.onDestory();
        }
    }

    /**
     * 用于反注册
     */
    public final void onDestory() {
        detachView();
    }


}
