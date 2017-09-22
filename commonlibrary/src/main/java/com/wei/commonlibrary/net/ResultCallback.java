package com.wei.commonlibrary.net;

import android.content.Context;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by ${wei} on 2017/3/21.
 */

public abstract class ResultCallback<T> {

    private static final String TAG = "debug_ResultCallback";
    public Type mType;

    public ResultCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    private static Type getSuperclassTypeParameter(Class<?> subclass) throws IllegalArgumentException {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new IllegalArgumentException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;

        Type type = $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        return type;
    }

    public abstract void onError(CommException e);

    public abstract void response(T t);

    public abstract void onBefore();

    public abstract void onAfter();

    public abstract boolean isAfter();

    public abstract Context getContext();

}
