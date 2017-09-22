package com.wei.commonlibrary.mvp.model;


import com.wei.commonlibrary.net.OkhttpRequest;
import com.wei.commonlibrary.net.RetrofitFactory;
import com.wei.commonlibrary.utils.LogUtil;
import com.wei.commonlibrary.utils.TUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ${wei} on 2017/3/26.
 */

public abstract class CommModelImpl<T> implements IModel {
    protected static String TAG = "";
    protected static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    protected OkhttpRequest okhttpRequest;
    protected T iApiService;

    public CommModelImpl(Class<T> clazz) {
        TAG = "debug_" + getClass().getSimpleName();
        okhttpRequest = OkhttpRequest.getInstance();
        iApiService = RetrofitFactory.createIApiService(getBaseUrl(), clazz);
    }

    public abstract String getBaseUrl();

    /**
     * 在线程池中操作，发生在子线程
     */
    public final void newThread(Runnable runnable) {
        executorService.execute(runnable);
    }

    @Override
    public final void onDestory() {
        if (okhttpRequest != null) {
            okhttpRequest.onUnSubscribe();
        }
    }
}
