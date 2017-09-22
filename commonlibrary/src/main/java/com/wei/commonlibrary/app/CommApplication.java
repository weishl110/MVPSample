package com.wei.commonlibrary.app;

import android.app.Application;

import com.wei.commonlibrary.utils.GlobalContext;
import com.wei.commonlibrary.utils.LogUtil;

/**
 * Created by ${wei} on 2017/3/28.
 */

public class CommApplication extends Application {
    private static final String TAG = "debug_CommApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalContext.application = this;
        GlobalContext.context = getApplicationContext();

        //异常补货
//        Thread.setDefaultUncaughtExceptionHandler(new MyUnHandle());
    }

    private static class MyUnHandle implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {

            LogUtil.e(TAG, "uncaughtException:  = " + e.getMessage());
            LogUtil.e(TAG, "uncaughtException:  = " + e.toString());
            e.printStackTrace();
            //杀死进程，退出APP
            android.os.Process.killProcess(android.os.Process.myPid());
            AppManager.getAppManager().finishAllActivity();
            System.exit(0);
        }
    }
}
