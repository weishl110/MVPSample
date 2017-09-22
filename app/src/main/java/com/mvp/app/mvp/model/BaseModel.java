package com.mvp.app.mvp.model;

import com.mvp.app.IApiService;
import com.wei.commonlibrary.mvp.model.CommModelImpl;

/**
 * Created by ${wei} on 2017/9/20.
 */

public abstract class BaseModel extends CommModelImpl<IApiService> {

    public BaseModel() {
        super(IApiService.class);
    }

    @Override
    public String getBaseUrl() {
        return "http://192.168.1.1:8080/test/";
    }
}
