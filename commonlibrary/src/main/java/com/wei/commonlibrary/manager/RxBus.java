package com.wei.commonlibrary.manager;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by ${wei} on 2017/8/31.
 */

public class RxBus {

    private static volatile RxBus instance;
    private static Object obj = new Object();

    public static synchronized RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    private RxBus() {
    }

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    /**
     * 订阅事件源
     *
     * @param observable
     * @param action1
     * @return
     */
    public RxBus onEvent(Observable<?> observable, Action1<Object> action1) {
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        return this;
    }


    /**
     * 注册事件源
     *
     * @param tag
     * @param <T>
     * @return
     */
    public <T> Observable<T> register(@Nullable Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (subjectList == null) {
            subjectList = new ArrayList<Subject>();
            subjectMapper.put(tag, subjectList);
        }
        Subject<T, T> subject;
        subjectList.add(subject = PublishSubject.<T>create());
        return subject;
    }

    public void unRegister(@Nullable Object tag) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (subjects != null) {
            subjects.clear();
            subjectMapper.remove(tag);
        }
    }

    /**
     * 取消监听
     *
     * @param tag
     * @param observable
     */
    public void unRegister(@Nullable Object tag, @Nullable Observable<?> observable) {
        if (null == observable) {
            throw new IllegalArgumentException("obserable 不能为空");
        }
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove(observable);
            if (isEmpty(subjects)) {
                subjects.remove(tag);
            }
        }
    }

    public void post(@Nullable Object content) {
        post(content.getClass().getName(), content);
    }

    /**
     * 触发事件
     *
     * @param tag
     * @param content
     */
    public void post(@Nullable Object tag, @Nullable Object content) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (!isEmpty(subjectList)) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection<Subject> collection) {
        return null == collection || collection.isEmpty();
    }


}
