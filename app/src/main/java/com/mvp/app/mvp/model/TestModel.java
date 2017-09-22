package com.mvp.app.mvp.model;

import com.mvp.app.mvp.contract.AbsTestModel;
import com.wei.commonlibrary.net.OkhttpRequest;

/**
 * Created by ${wei} on 2017/9/20.
 */

public class TestModel extends AbsTestModel {
    @Override
    public void onStart() {

    }

    @Override
    public OkhttpRequest test(String str1, String str2) {
        return OkhttpRequest.getInstance().url(iApiService.getLogin(str1,str2));
    }
}
