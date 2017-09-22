package com.wei.commonlibrary.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.style.LineHeightSpan;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GenericTranscodeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;

import static java.lang.System.load;

/**
 * Created by ${wei} on 2017/4/10.
 */

public class GlideUtil {

    /**
     * 加载URL图片
     *
     * @param context       上下文
     * @param url           图片url
     * @param imageView     加载完展示控件
     * @param errorDrawable 异常图片
     */
    public static void loadImage(Context context, String url, Drawable errorDrawable, Drawable loadDrawable, ImageView imageView) {
        WeakReference<ImageView> reference = new WeakReference<>(imageView);
        RequestManager with;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            with = Glide.with(activity);
        } else {
            with = Glide.with(context);
        }
        DrawableRequestBuilder<String> builder = with.load(url);
        if (errorDrawable != null) {
            builder.error(errorDrawable).skipMemoryCache(true);
        }
        if(loadDrawable != null){
            builder.placeholder(loadDrawable);
        }
        if (reference.get() != null) {
            builder.into(reference.get());
        }
    }

    public static void loadImage(Activity activity, String url, int errorRes, int loadRes, ImageView imageView) {
        Drawable drawable = activity.getResources().getDrawable(errorRes);
        Drawable loadDrawable = activity.getResources().getDrawable(loadRes);
        loadImage(activity, url, drawable, loadDrawable, imageView);
    }

    public static void loadImage(Fragment fragment, String url, Drawable errorDrawable, Drawable loadDrawable, ImageView imageView) {
        WeakReference<ImageView> reference = new WeakReference<>(imageView);
        DrawableTypeRequest<String> load = Glide.with(fragment).load(url);
        if (errorDrawable != null) {
            load.error(errorDrawable).skipMemoryCache(true);
        }
        if(loadDrawable != null){
            load.placeholder(loadDrawable);
        }
        if (reference.get() != null) {
            load.into(reference.get());
        }
    }

    public static void loadImage(Fragment fragment, String url, int errorRes, int loadRes, ImageView imageView) {
        Drawable drawable = fragment.getResources().getDrawable(errorRes);
        Drawable loadDrawable = fragment.getResources().getDrawable(loadRes);
        loadImage(fragment, url, drawable, loadDrawable, imageView);
    }

    /**
     * 加载sd卡的图片
     *
     * @param context       上下文
     * @param filePath      图片路径
     * @param imageView     加载完展示的控件
     * @param errorDrawable 加载异常的图片ID
     */
    public static void loadSdImage(Context context, String filePath, Drawable errorDrawable, Drawable loadDrawable, ImageView imageView) {
        WeakReference<ImageView> reference = new WeakReference<>(imageView);
        RequestManager with;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            with = Glide.with(activity);
        } else {
            with = Glide.with(context);
        }
        DrawableTypeRequest<String> load = with.load(String.format("%s%s", "file://", filePath));
        if (errorDrawable != null) {
            load.error(errorDrawable).skipMemoryCache(true);
        }
        if (loadDrawable != null) {
            load.placeholder(loadDrawable);
        }
        if (reference.get() != null) {
            load.into(reference.get());
        }
    }

    public static void loadSdImage(Activity activity, String filePath, int errorResId, int loadResId, ImageView imageView) {
        Drawable drawable = activity.getResources().getDrawable(errorResId);
        Drawable loadDrawable = null;
        if (loadResId != -1) {
            loadDrawable = activity.getResources().getDrawable(loadResId);
        }
        loadSdImage(activity, filePath, drawable, loadDrawable, imageView);
    }

    public static void loadSdImage(Fragment fragment, String filePath, Drawable errorDrawable, Drawable loadDrawable, ImageView imageView) {
        WeakReference<ImageView> reference = new WeakReference<>(imageView);
        DrawableTypeRequest<String> load = Glide.with(fragment).load(String.format("%s%s", "file://", filePath));
        if (errorDrawable != null) {
            load.error(errorDrawable).skipMemoryCache(true);
        }
        if (loadDrawable != null) {
            load.placeholder(loadDrawable);
        }
        if (reference.get() != null) {
            load.into(reference.get());
        }
    }

    public static void loadSdImage(Fragment fragment, String filePath, int errorResId, int loadResId, ImageView imageView) {
        Drawable drawable = fragment.getResources().getDrawable(errorResId);
        Drawable loadDrawable = null;
        if (loadResId != -1) {
            loadDrawable = fragment.getResources().getDrawable(loadResId);
        }
        loadSdImage(fragment, filePath, drawable, loadDrawable, imageView);
    }

}
