package com.mvp.app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mvp.app.R;
import com.mvp.app.mvp.contract;
import com.mvp.app.mvp.presenter.TestPresenter;
import com.mvp.app.mvp.view.IBaseView;
import com.wei.commonlibrary.net.CommException;
import com.wei.commonlibrary.utils.ToastUtil;

import butterknife.BindView;

public class MainActivity extends BaseActivity<TestPresenter> implements contract.ITestView {

    @BindView(R.id.tv_get)
    TextView tv_get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        mPresenter.setView(this);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int layoutView() {
        return R.layout.activity_main;
    }

    @Override
    public void initWidget() {
        tv_get.setOnClickListener(this);
    }

    @Override
    public void widgetClick(View view) {
        mPresenter.test("123456", "123456");
    }

    @Override
    public void onError(CommException e) {
        ToastUtil.showShort(mContext, "网络异常，登录失败");
    }

    @Override
    public void onResponse(String s) {
        ToastUtil.showShort(mContext, "登录成功");
    }
}
