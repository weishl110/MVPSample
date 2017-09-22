package com.mvp.app.mvp.presenter;

import android.app.Activity;

import com.mvp.app.mvp.contract;
import com.mvp.app.mvp.contract.AbsTestPresenter;
import com.mvp.app.mvp.model.TestModel;
import com.wei.commonlibrary.net.CommException;
import com.wei.commonlibrary.net.MyDefaultCallback;

/**
 * Created by ${wei} on 2017/9/20.
 */

public class TestPresenter extends AbsTestPresenter {
    @Override
    public void test(String str1, String str2) {
        mModel.test(str1,str2).callback(new MyDefaultCallback<String>((Activity) getView(),true) {
            @Override
            public void onError(CommException e) {
                if (getView() != null) {
                    getView().onError(e);
                }
            }

            @Override
            public void response(String s) {
                if (getView() != null) {
                    getView().onResponse("登录成功");
                }
            }
        });
    }

    @Override
    protected contract.AbsTestModel createModel() {
        return new TestModel();
    }
}
