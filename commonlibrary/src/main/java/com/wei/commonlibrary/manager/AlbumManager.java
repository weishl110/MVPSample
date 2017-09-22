package com.wei.commonlibrary.manager;

import android.app.Activity;
import android.text.TextUtils;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.CameraWrapper;

/**
 * Created by ${wei} on 2017/6/8.
 */

public class AlbumManager {

    /**
     * @param activity
     * @param requestCode
     * @param selectorNum
     */
    public static void openAlbum(Activity activity, int requestCode, int selectorNum) {
        Album.album(activity)
                .camera(true)//是否显示相机
                .selectCount(selectorNum)//可选择数量
                .columnCount(4).start(requestCode);
    }

    public static void openCamera(Activity activity, String path, int requestCode) {
        CameraWrapper wrapper = Album.camera(activity);
        if (!TextUtils.isEmpty(path)) {
            wrapper.imagePath(path);
        }
        wrapper.start(requestCode);
    }
}
