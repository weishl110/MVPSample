package com.wei.commonlibrary.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by ${wei} on 2017/5/22.
 * 修改状态栏的颜色
 */

public class StatusBarUtils {

    private static final String TAG = "debug_StatusBarUtils";

    /**
     * 设置状态栏的颜色
     *
     * @param activity
     * @param colorResId
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return;
            }
            Window window = activity.getWindow();
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            decorView = (ViewGroup) activity.findViewById(android.R.id.content);
//            decorView.setClipChildren(true);
//            decorView.setFitsSystemWindows(true);

            if (colorResId == -1) {
                //设置状态栏的flag
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                return;
            }
            int color = activity.getResources().getColor(colorResId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
                return;
            }
            // versionCode >= 4.4   <= 5.0的时候
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                if (color != -1) {
                    ViewGroup childViewGroup = (ViewGroup) decorView.getChildAt(0);
                    childViewGroup.setFitsSystemWindows(true);
//                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    View statusBarView = new View(activity);
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
                    statusBarView.setBackgroundColor(color);
                    decorView.addView(statusBarView, lp);
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
