package com.wei.commonlibrary.utils;

import android.util.Log;

import com.example.commonlibrary.BuildConfig;

/**
 * Created by admin on 2017/3/29.
 */

public final class LogUtil {
    public static boolean debug = true;
    public static void i(String tag,String msg){
        if(debug)
            Log.i(tag, msg);
    }
    public static void d(String tag,String msg){
        if(debug)
            Log.d(tag, msg);
    }
    public static void e(String tag,String msg){
        if(debug)
            Log.e(tag, msg);
    }

}
