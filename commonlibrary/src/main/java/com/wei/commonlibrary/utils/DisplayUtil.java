package com.wei.commonlibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatProperty;

/**
 * Created by ${wei} on 2017/4/12.
 */

public class DisplayUtil {
    private static final String TAG = "debug_AppInfoUtil";

    /**
     * 返回AndroidManifest里的适配信息
     *
     * @param context
     * @return 0.width, 1.height
     */
    public static int[] getAutolayoutMetaData(Context context) {
        int[] arr = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = applicationInfo.metaData;
            int width = metaData.getInt("design_width");
            int height = metaData.getInt("design_height");
            arr = new int[]{width, height};
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return arr;
    }


    /**
     * 获取屏幕的宽高
     *
     * @param context
     * @return 返回数组 0.width,1.height
     */
    public static float[] getScreenWH(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float height = metrics.heightPixels;
        float width = metrics.widthPixels;
        return new float[]{width, height};
    }


    /**
     * 获取当前屏幕与适配屏幕的比例
     *
     * @param context
     * @return 返回比例 int值
     */
    public static float getCurrentScreenPercent(Context context) {
        int[] autolayoutArr = getAutolayoutMetaData(context);
        float[] screenWHArr = getScreenWH(context);
        if (screenWHArr != null && autolayoutArr != null) {
            float widthPercent = screenWHArr[0] / autolayoutArr[0];
            float heightPercent = screenWHArr[1] / autolayoutArr[1];
            return Math.min(widthPercent, heightPercent);
        }
        return -1;
    }

    /**
     * 获取屏幕密度
     *
     * @param context
     * @return
     */
    public static float getScreendensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(Context context, int dp_value) {
        float density = getScreendensity(context);
        return (int) (dp_value * density + 0.5f);
    }
    public static int px2dp(Context context, int dp_value) {
        float density = getScreendensity(context);
        return (int) (dp_value / density - 0.5f);
    }
}
