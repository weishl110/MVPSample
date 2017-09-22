package com.wei.commonlibrary.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Created by ${wei} on 2017/3/28.
 * 全局context 持有application的上下文
 */

public class GlobalContext {
    public static Application application;
    public static Context context;
    public static Activity activity;
}
