package com.wei.commonlibrary.manager;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuCompat;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ${wei} on 2017/6/8.
 */

public class RxManager {

    private static RxBus mRxBus = RxBus.getInstance();
    // 管理rxbus订阅
    private static Map<String, Observable<?>> mObservables = new HashMap<>();
    /* 管理observable 和 subscribers订阅 */
    private static CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    /**
     * 权限请求
     *
     * @param activity
     * @param permissions 权限数组
     * @param listener    回调
     */
    public static void requestPermission(Activity activity, String[] permissions, final IListener.IPermissionListener listener) {
        //跳转至地图页面
        new RxPermissions(activity).request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (listener != null) {
                            listener.call(granted);
                        }
                    }
                });
    }

    /**
     * 延时执行
     *
     * @param delayTime 延迟时间 单位毫秒
     * @param action1
     */
    public static void delayPerform(long delayTime, Action1<Long> action1) {
        Observable.timer(delayTime, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(action1);
    }

    /**
     * RxBus注入监听
     *
     * @param eventName
     * @param action
     * @param <T>
     */
    public static <T> void on(String eventName, Action1<T> action) {
        Observable<T> observable = mRxBus.register(eventName);
        mObservables.put(eventName, observable);
        mCompositeSubscription.add(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }));
    }

    /**
     * 单纯的Observables 和 Subscribers管理
     *
     * @param subscription
     */
    public static void add(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    /**
     * 清除Rxbus监听
     */
    public static void clear() {
        mCompositeSubscription.unsubscribe();
        Set<Map.Entry<String, Observable<?>>> entries = mObservables.entrySet();
        for (Map.Entry<String, Observable<?>> entry : entries) {
            mRxBus.unRegister(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 发送rxbus
     */
    public static void post(@Nullable Object tag,@Nullable Object content){
        mRxBus.post(tag,content);
    }


}
