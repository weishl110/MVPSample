package com.wei.commonlibrary.net;

import android.os.Environment;

import com.wei.commonlibrary.utils.GlobalContext;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ${wei} on 2017/4/5.
 */

public final class RetrofitService {

    public Retrofit createRetrofit(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkhttpClient());
        return builder.build();
    }

    /**
     * 创建okhttpClient,添加设置信息,缓存设置,请求消息头
     */
    private OkHttpClient createOkhttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //缓存路径
        File sdCardPath;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            sdCardPath = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "cacheData");
        } else {
            sdCardPath = new File(GlobalContext.context.getCacheDir(), "cacheData");
        }
        //缓存空间
        Cache cache = new Cache(sdCardPath, 10 * 1024 * 1024);

//        builder.cache(cache)
        //添加超时时间
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new InterceptroManager.AuthInterceptor())
                .addInterceptor(new InterceptroManager.HeaderInterceptor());
//                .addInterceptor(new InterceptroManager.ResponseInterceptor());
                /*.addNetworkInterceptor(new InterceptroManager.MyNetWorkInterceptor())*/
        return builder.build();
    }
}
