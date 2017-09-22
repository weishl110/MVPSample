package com.wei.commonlibrary.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;

/**
 * Created by ${wei} on 2017/5/19.
 */

public class DialogManager {

    /**
     *
     * @param activity
     * @param content
     */
    public static void showPermissDialog(Activity activity, String content){
        final Activity tempActivity = activity;
        final TextDescDialog dialog = (TextDescDialog) new TextDescDialog.Builder().setContext(tempActivity)
                .setContent(content).setTitle("权限获取失败")
                .setGravity(Gravity.CENTER).setleftBtnText("取消").setRightBtnText("开启").build();
        dialog.show();
        dialog.setOnDialogListener(new BaseDialog.SimpleOnDialogListener() {
            @Override
            public void rightClick(String text) {
                dialog.dismiss();
                goToSettings(tempActivity);
            }

            @Override
            public void leftClick() {
                dialog.dismiss();
            }
        });
    }

    private static void goToSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }
}
