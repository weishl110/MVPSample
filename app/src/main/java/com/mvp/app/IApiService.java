package com.mvp.app;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ${wei} on 2017/9/20.
 */

public interface IApiService {

    @GET("login")
    Observable<String> getLogin(@Query("username") String username, @Query("password") String password);
}
