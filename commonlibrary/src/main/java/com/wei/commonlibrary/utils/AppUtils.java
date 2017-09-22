package com.wei.commonlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;

public class AppUtils {
    private static final String TAG = "debug_AppUtils";

    private AppUtils() {
    }

    /**
     * 安装apk
     */
    public static void installApk(Activity activity, File apkFile) {
        if (!apkFile.exists()) {
            return;
        }
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            String providerString = getFileProviderString(activity);
            Uri uri = FileProvider.getUriForFile(activity.getApplicationContext(), providerString, apkFile);
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            installIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        activity.getApplicationContext().startActivity(installIntent/*, 0x0101*/);
    }

    public static String getFileProviderString(Context context) {
        try {
            ProviderInfo[] providers = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PROVIDERS).providers;
            for (ProviderInfo p : providers) {
                if (FileProvider.class.getName().equals(p.name) && p.authority.endsWith("jtyw.fileprovider")) {
                    return p.authority;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 隐藏软键盘
     **/
    public static void hiddenSoftInput(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

}
