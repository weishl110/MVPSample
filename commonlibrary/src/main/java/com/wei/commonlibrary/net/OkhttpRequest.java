package com.wei.commonlibrary.net;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wei.commonlibrary.utils.LogUtil;
import com.wei.commonlibrary.utils.NetWorkUtil;
import com.wei.commonlibrary.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ${wei} on 2017/3/20.
 * <p>
 * <p>
 * </br>接口定义Observable<String>只能返回string类型，是为了应对接口返回多种类型的,
 * 在onNext中解析返回数据，根据callback的泛型转换成想要的数据格式，否则将会报错</>
 */

public class OkhttpRequest {

    private static final String TAG = "debug_OkhttpRequest";
    private CompositeSubscription mCompositeSubscription;
    private Observable mObservable;
    private static OkhttpRequest instance;
    private final Gson mGson;
    private Subscription subscription;

    private static ResultCallback<String> DEFAULT = new ResultCallback<String>() {
        @Override
        public void onError(CommException e) {

        }

        @Override
        public void response(String s) {

        }

        @Override
        public void onBefore() {

        }

        @Override
        public void onAfter() {

        }

        @Override
        public Context getContext() {
            return null;
        }

        @Override
        public boolean isAfter() {
            return false;
        }
    };
    private ResultCallback finalCall;

    public static OkhttpRequest getInstance() {
        if (instance == null) {
            synchronized (OkhttpRequest.class) {
                if (instance == null) {
                    instance = new OkhttpRequest();
                }
            }
        }
        return instance;
    }

    private OkhttpRequest() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .registerTypeAdapter(String.class, new StringConvert());

        mGson = gsonBuilder.create();
//        mGson = new Gson();
    }

    /**
     * 反注册，以免造成内存泄漏
     */
    public void onUnSubscribe() {
        if (mCompositeSubscription != null) {
            boolean hasSubscriptions = mCompositeSubscription.hasSubscriptions();
            boolean unSubscribed = mCompositeSubscription.isUnsubscribed();
            if (hasSubscriptions && !unSubscribed) {
                boolean unsubscribed = subscription.isUnsubscribed();
                if (subscription != null && !unsubscribed) {
                    mObservable.unsubscribeOn(Schedulers.io());
                    subscription.unsubscribe();
                }
                mCompositeSubscription.remove(subscription);
                mCompositeSubscription.clear();
            }
        }
        if (finalCall != null && !finalCall.isAfter()) {
            finalCall.onAfter();
        }
    }


    public OkhttpRequest url(Observable observable) {
        if (observable == null) {
            throw new IllegalArgumentException("observable 不能为空");
        }
        mObservable = observable.compose(new Observable.Transformer() {
            @Override
            public Object call(Object obserable) {
                return ((Observable) obserable).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        /*.observeOn(AndroidSchedulers.mainThread())*/;
            }
        });
        return instance;
    }

    /**
     * @param callback callback回调
     * @param <T>
     */
    public <T> void callback(ResultCallback<T> callback) {
        Context context = callback.getContext();
        boolean gConnected = NetWorkUtil.is3gConnected(context);
        boolean wifiConnected = NetWorkUtil.isWifiConnected(context);
        boolean netConnected = NetWorkUtil.isNetConnected(context);
        if (!gConnected && !wifiConnected && !netConnected) {
//            sendFailerCallback("请检查网络连接", null, callback);
            callback.onError(new CommException("请检查网络连接"));
            return;
        }
        if (mCompositeSubscription == null || mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription = new CompositeSubscription();
        }
        if (mObservable == null) {
            throw new IllegalArgumentException("先初始化url");
        }
        if (callback == null) {
            callback = (ResultCallback<T>) DEFAULT;
        }
        finalCall = callback;
        mCompositeSubscription.add(subscription = mObservable.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        finalCall.onBefore();//显示loading
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                        .compose(this.parsingResult())//解析数据,在子线程
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<T>() {

                            @Override
                            public void onCompleted() {
//                                if (!finalCall.isAfter()) {
                                    finalCall.onAfter();
//                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                finalCall.onAfter();
                                LogUtil.e(TAG, "178.onError. = " + e.toString());
                                String msg = "网络异常";
                                if (e instanceof ConnectException) {
                                    msg = "网络连接异常";
                                } else if (e instanceof TimeoutException || e instanceof SocketTimeoutException) {
                                    msg = "网络连接超时";
                                }
                                CommException exception;
                                if (e instanceof CommException) {
                                    exception = (CommException) e;
                                } else {
                                    exception = new CommException(msg);
                                }
                                if (StringUtil.isEmpty(exception.getMessage())) {
                                    exception.setMessage(msg);
                                }
                                finalCall.onError(exception);
                            }

                            @Override
                            public void onNext(T response) {
                                LogUtil.e(TAG, "202.onNext. = ");
                                //解析数据
                                finalCall.onAfter();
                                finalCall.response(response);
                            }
                        })
        );
    }

//    private <T> void parsingData(String response, ResultCallback<T> callback) {
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            String status = "", errorMessage = "", code = "";
//            T data;
//            if (jsonObject.has("status")) {
//                status = jsonObject.optString("status");
//                if (!TextUtils.equals(status, "0")) {
//                    errorMessage = jsonObject.optString("errorInfo");
//                    code = jsonObject.optString("code");
//                    sendFailerCallback(errorMessage, code, callback);
//                    return;
//                }
//            } else if (jsonObject.has("status_code")) {
//                status = jsonObject.optString("status_code");
//                if (TextUtils.equals(status, "0")) {
//                    errorMessage = jsonObject.optString("errorInfo");
//                    code = jsonObject.optString("code");
//                    sendFailerCallback(errorMessage, code, callback);
//                    return;
//                }
//            }
//            if (jsonObject.has("data") && jsonObject.isNull("data")) {
//                data = mGson.fromJson(new String(""), callback.mType);
//                LogUtil.e(TAG, "parsingData: data = " + data);
//                sendSuccessCallback(data, callback);
//                return;
//            }
//
//            data = mGson.fromJson(jsonObject.optString("data"), callback.mType);
//            sendSuccessCallback(data, callback);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            LogUtil.e(TAG, "parsingData: error = " + e.toString());
//            sendFailerCallback("网络异常", "", callback);
//        }
//    }
//
//    private <T> void sendSuccessCallback(T data, ResultCallback<T> callback) {
//        callback.onAfter();//取消loading
//        callback.response(data);
//    }
//
//    private <T> void sendFailerCallback(String errorMessage, String code, ResultCallback<T> callback) {
//        callback.onAfter();//取消loading
//        int codeInt = 0;
//        if (code != null) {
//            //解析code
//        }
//        callback.onError(new CommException(errorMessage, codeInt));
//    }


    private <T> Observable.Transformer<String, T> parsingResult() {
        return new Observable.Transformer<String, T>() {
            @Override
            public Observable<T> call(final Observable<String> observable) {
                return observable.flatMap(new Func1<String, Observable<T>>() {
                    @Override
                    public Observable<T> call(String result) {
                        return (Observable<T>) createData(result);
                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private <T> Observable<T> createData(String data) {
        final String tempData = data;
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    LogUtil.e(TAG, "286.call.json = " + tempData);
                    parsingData(tempData, subscriber);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     *
     * @param response
     * @param subscriber
     * @param <T>
     */
    private <T> void parsingData(String response, Subscriber subscriber) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = "", errorMessage = "", code = "";
            T data;
            if (jsonObject.has("status")) {
                status = jsonObject.optString("status");
                if (!TextUtils.equals(status, "0")) {
                    errorMessage = jsonObject.optString("errorInfo");
                    code = jsonObject.optString("code");
                    subscriber.onError(createException(errorMessage, code));
                    return;
                }
            } else if (jsonObject.has("status_code")) {
                status = jsonObject.optString("status_code");
                if (TextUtils.equals(status, "0")) {
                    errorMessage = jsonObject.optString("errorInfo");
                    code = jsonObject.optString("code");
                    subscriber.onError(createException(errorMessage, code));
                    return;
                }
            }
            if (jsonObject.has("data") && jsonObject.isNull("data")) {
                data = mGson.fromJson(new String(""), finalCall.mType);
                subscriber.onNext(data);
                return;
            }
            data = mGson.fromJson(jsonObject.optString("data"), finalCall.mType);
            subscriber.onNext(data);
        } catch (JSONException e) {
            subscriber.onError(createException("网络异常", null));
            LogUtil.e(TAG, "parsingData: error = " + e.toString());
        }
    }

    private CommException createException(String messager, String code) {
        CommException exception = new CommException();
        int codeInt = -1;
        if (code != null) {

        }
        exception.setMessage(messager);
        exception.setCode(codeInt);
        return exception;
    }
}
