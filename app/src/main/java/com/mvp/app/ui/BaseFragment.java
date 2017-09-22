package com.mvp.app.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wei.commonlibrary.mvp.presenter.CommPresenter;
import com.wei.commonlibrary.utils.TUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 2017/3/30.
 */

public abstract class BaseFragment<P extends CommPresenter> extends Fragment implements View.OnClickListener {

    protected P mFPresenter;
    protected View rootView;
    protected static String TAG = "debug_fragment";
    private Unbinder mUnbinder;
    public Context mContext;

    /**
     * 根据class返回相应的fragment实例
     *
     * @param context
     * @param clazz
     * @param bundle
     * @param <T>
     * @return
     */
    public static <T> T getFragment(Context context, Class<T> clazz, Bundle bundle) {
        return (T) instantiate(context, clazz.getName(), bundle);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TAG = "debug_" + getClass().getSimpleName();
        rootView = inflater.inflate(layoutRes(), container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mFPresenter = TUtil.getT(this, 0);
        initPrsenter();
        initView(rootView);
        return rootView;
    }

    protected abstract void initPrsenter();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initWidget();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /**
     * 子类返回布局ID
     */
    protected abstract int layoutRes();

    protected abstract void initView(View rootView);

    public void resume() {
    }

    /**
     * 初始化数据
     */
    protected abstract void initWidget();

    /**
     * 点击事件
     */
    protected abstract void widgetClick(View v);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mFPresenter != null) {
            mFPresenter = null;
        }
        mContext = null;
    }

    public CommPresenter getPresenter() {
        return mFPresenter;
    }

}

