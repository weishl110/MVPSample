package com.wei.commonlibrary.manager;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by ${wei} on 2017/6/8.
 */

public interface IListener {
    /**
     * 权限请求
     */
    interface IPermissionListener {
        void call(boolean granted);
    }

    interface IRequestBodyListener{
        void call(Map<String, RequestBody> map);
    }
}
