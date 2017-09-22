package com.mvp.app.mvp;

import com.mvp.app.mvp.model.BaseModel;
import com.mvp.app.mvp.presenter.BasePresenter;
import com.mvp.app.mvp.view.IBaseView;
import com.wei.commonlibrary.net.OkhttpRequest;

import okhttp3.OkHttpClient;

/**
 * Created  ${wei} on 2017/9/20.
 */

public interface contract {
    abstract class AbsTestModel extends BaseModel {
        public abstract OkhttpRequest test(String str1, String str2);
    }

    abstract class AbsTestPresenter extends BasePresenter<AbsTestModel, ITestView> {
        public abstract void test(String str1, String str2);
    }

    interface ITestView extends IBaseView<String> {
    }
}
