package com.wei.commonlibrary.net;

import android.text.TextUtils;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.wei.commonlibrary.utils.GlobalContext;
import com.wei.commonlibrary.utils.LogUtil;
import com.wei.commonlibrary.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by ${wei} on 2017/4/5.
 */

public interface InterceptroManager {

    String TAG = "debug_InterceptroManager";
    String TYPE_JSON = "application/json; charset=utf-8";
    String TYPE_STREAM = "multipart/form-data; charset=utf-8";
    String TYPE_FORM = "application/x-www-form-urlencoded";
    String CONTENTTYPE = "Content-Type";

    /**
     * 请求拦截器,可以用于往请求体中添加默认参数等
     * post： 如果是表单格式的全部转换成了json格式
     */
    class AuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originRequest = chain.request();
            LogUtil.e(TAG, "intercept.url = " + originRequest.url());
            LogUtil.e(TAG, "intercept.method = " + originRequest.method());
            //如果是post请求，则输出请求体log
            Buffer buffer = new Buffer();
            try {
                if ("POST".equals(originRequest.method())) {
                    Charset charset = Charset.forName("utf-8");
                    RequestBody body = originRequest.body();
                    String paramStr = null;
                    if (body != null) {
                        body.writeTo(buffer);
                        paramStr = buffer.readString(charset);
                    }
                    String contentType = originRequest.body().contentType().toString();
                    if (TextUtils.equals(TYPE_FORM, contentType)) {
                        Request.Builder builder = originRequest.newBuilder();
                        //如果是表单形式的，则转换为json格式
                        if (paramStr != null) {
                            List<String> list = new ArrayList<>();
                            if (paramStr.contains("&")) {
                                String[] split = paramStr.split("&");
                                for (int i = 0; i < split.length; i++) {
                                    String s = split[i];
                                    list.add(s);
                                }
                            } else {
                                list.add(paramStr);
                            }
                            String json = parsingParams(list);
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
                            builder.method(originRequest.method(), requestBody);
                        }
                        Request newRequest = builder.build();
                        //输出改变后的参数
                        buffer.clear();
                        newRequest.body().writeTo(buffer);
                        String s = buffer.readString(charset);
                        buffer.clear();
                        buffer.close();
                        LogUtil.e(TAG, "intercept.contentType = " + newRequest.body().contentType());
                        LogUtil.e(TAG, "intercept.参数 = " + s);
                        return chain.proceed(newRequest);
                    } else {
                        LogUtil.e(TAG, "intercept: params = " + paramStr);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (buffer != null) {
                    buffer.clear();
                    buffer.close();
                }
            }
            return chain.proceed(originRequest);
        }

        //转换为JSON数据格式
        private String parsingParams(List<String> list) {
            JSONObject jsonObject = new JSONObject();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String str = list.get(i);
                String[] param = str.split("=");
                try {
                    if (param.length == 1) {
                        jsonObject.put(param[0], "");
                    } else {
                        jsonObject.put(param[0], param[1]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject.toString();
        }
    }

    /**
     * 添加请求消息头
     */
    class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //向请求头写入Content-Type参数，设置参数搁置为application/json
//            newHeaders.add("Content-Type", "application/json;charset=UTF-8");
//            Request.Builder newRequest = origin.newBuilder()
//                    .headers(newHeaders.build());

            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            /*.addHeader("Content-type", "application/json; charset=utf-8")*/
            builder.addHeader("User-Agent", "Android");
//                    .addHeader("Accept-Encoding", "gzip, deflate")
//                    .addHeader("Connection", "keep-alive");
            request = builder.build();
//
//            Headers headers = request.headers();
//
//            Set<String> names = headers.names();
//            for (String key : names) {
//                LogUtil.e(TAG, "intercept.key = " + key + "  value = " + headers.get(key));
//            }
            return chain.proceed(request);
        }
    }

    class NomalInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String method = request.method();

            switch (method) {
                case "GET":
                    StringBuilder sb = new StringBuilder();
                    String url = request.url().toString();
                    if (url.contains("?")) {
                        sb.append(url).append("&");
                    } else {
                        sb.append(url).append("?");
                    }
                    url = sb.append("token=").append("dd").toString();
                    break;
                case "POST":

                    break;
            }
            return null;
        }
    }


    /**
     * 如果不用缓存，可以不设置 请求体中添加默认的请求信息等都在interceptor中添加
     * 设置网络缓存
     */
    class MyNetWorkInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Headers headers = request.headers();
            Response proceed = chain.proceed(request);
            ResponseBody body = proceed.body();
            String s = new String(body.bytes());
            return proceed.newBuilder()
                    .body(ResponseBody.create(MediaType.parse("application/json"), s))
                    .build();
        }
    }

    /**
     * 响应拦截器
     */
    class ResponseInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String s = request.url().toString();
            boolean isLogin = s.contains("isLogin");
            if (isLogin) {//如果是登录接口，则获取token
                //获取相应体
                String body = chain.proceed(request).body().string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.has("data")) {
                        JSONObject job = jsonObject.optJSONObject("data");
                        if (job.has("token")) {
                            String token = job.optString("token");
                            LogUtil.e(TAG, "intercept.token = " + token);
                            SPUtils.saveStringData(GlobalContext.context, "token", token);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return chain.proceed(request);
        }
    }
}
