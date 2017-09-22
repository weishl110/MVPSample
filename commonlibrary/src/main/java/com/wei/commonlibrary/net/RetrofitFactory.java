package com.wei.commonlibrary.net;

import android.text.TextUtils;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by ${wei} on 2017/3/25.
 * <p>
 * 如果baseUrl多，可以让此类为抽象类，由子类实现返回baseUrl
 */

public abstract class RetrofitFactory {

    private static RetrofitService service = new RetrofitService();

    private static Retrofit retrofit;

    public static Retrofit createRetrofit(String baseUrl) {
        if (retrofit == null || !TextUtils.equals(retrofit.baseUrl().toString(), baseUrl)) {
            retrofit = service.createRetrofit(baseUrl);
        }
        return retrofit;
    }

    /**
     * @param baseUrl IP和port
     * @param clazz   api接口类
     * @param <T>     返回class类的实力
     * @return
     */
    public static <T> T createIApiService(String baseUrl, Class<T> clazz) {
        retrofit = createRetrofit(baseUrl);
        return retrofit.create(clazz);
    }
}
