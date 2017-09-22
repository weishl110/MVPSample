package com.wei.commonlibrary.mvp.view;

import com.wei.commonlibrary.net.CommException;

/**
 * Created by ${wei} on 2017/3/26.
 */

public interface ICommView<T> {
    void onError(CommException e);

    void onResponse(T t);
}
