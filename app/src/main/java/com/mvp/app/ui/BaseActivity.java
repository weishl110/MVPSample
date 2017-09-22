package com.mvp.app.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mvp.app.R;
import com.wei.commonlibrary.app.AppManager;
import com.wei.commonlibrary.dialog.DialogManager;
import com.wei.commonlibrary.manager.IListener;
import com.wei.commonlibrary.manager.RxManager;
import com.wei.commonlibrary.mvp.presenter.CommPresenter;
import com.wei.commonlibrary.utils.LogUtil;
import com.wei.commonlibrary.utils.StatusBarUtils;
import com.wei.commonlibrary.utils.TUtil;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 2017/3/30.
 */

public abstract class BaseActivity<P extends CommPresenter> extends AppCompatActivity
        implements View.OnClickListener {

    public static String TAG = "debug_";

    protected P mPresenter;

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    public TextView title_left, title_right, title_center;
    protected ImageView title_iv_left;
    private Unbinder mUnbinder;
    public Context mContext;
    public BaseActivity mActivity;
    private View view;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置强制竖屏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        start(getIntent());
        mContext = this;
        mActivity = this;
        AppManager.getAppManager().addActivity(this);
        setContentView(layoutView());
        setStatusBar();
        //注解
        mUnbinder = ButterKnife.bind(this);
        mPresenter = TUtil.getT(this, 0);
        TAG = "debug_" + getClass().getSimpleName();
        LogUtil.e(TAG, "onCreate:  = " + getClass().getSimpleName());
        initPresenter();
        initTitleWidget();
        //防止崩溃
        if (view != null) {
            initTitle();
        }
        initWidget();

    }

    public void start(Intent intent) {

    }

    /**
     * 设置状态栏颜色
     */
    private void setStatusBar() {
        int color = -1;
        String className = getClass().getSimpleName();
        if ("SplashActivity".equals(className)) {
            color = R.color.color_transparent;
        } else if ("LoginActivity".equals(className)) {
            color = -1;
        } else {
            color = R.color.color_cc9833;
        }
        StatusBarUtils.setWindowStatusBarColor(this, color);
    }

    protected abstract void initPresenter();

    protected void initTitleWidget() {
        view = findViewById(R.id.layout_rl_title);
        if (view != null) {
            title_left = (TextView) view.findViewById(R.id.title_tv_left);
            title_center = (TextView) view.findViewById(R.id.title_tv_center);
            title_right = (TextView) view.findViewById(R.id.title_tv_right);
            title_iv_left = (ImageView) view.findViewById(R.id.title_iv_left);
            title_iv_left.setOnClickListener(this);

        }
    }

    protected abstract void initTitle();

    /**
     * 子类返回布局
     */
    public abstract int layoutView();

    /**
     * 初始化布局id
     */
    public abstract void initWidget();

    public <T> T findViewId(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_iv_left:
            case R.id.title_tv_left:
                onBackPressed();
                break;
            default:
                widgetClick(v);
                break;
        }
    }

    public abstract void widgetClick(View view);

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContext = this;
        mActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        mContext = null;
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        if (mPresenter != null) {
            mPresenter.onDestory();
            mPresenter = null;
        }
        LogUtil.e(TAG, "onDestroy:  = ");
        mActivity = null;
        System.gc();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        AppManager.getAppManager().removeActivity(this);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(getApplicationContext(), clazz);
        startActivity(intent);

        //设置切换动画
//        overridePendingTransition();
    }

    public void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * 跳转页面时需要申请权限
     *
     * @param clazz
     * @param permissions  权限数组
     * @param bundle       需要携带的数据
     * @param errorDesText 请求权限失败的描述语
     */
    public void startPermissionActivityForResult(final Class<?> clazz, final int requestCode, final Bundle bundle, final String[] permissions, final String errorDesText) {
        RxManager.requestPermission(this, permissions, new IListener.IPermissionListener() {
            @Override
            public void call(boolean granted) {
                if (granted) {
                    //开始定位
                    startActivityForResult(clazz, requestCode, bundle);
                } else {
                    if (!TextUtils.isEmpty(errorDesText)) {
                        DialogManager.showPermissDialog((BaseActivity) mContext, errorDesText);
                    }
                }
            }
        });
    }

    /**
     * 跳转页面时需要申请权限
     *
     * @param clazz
     * @param permissions  权限数组
     * @param bundle       需要携带的数据
     * @param errorDesText 请求权限失败的描述语
     */
    public void startPermissionActivity(final Class<?> clazz, final Bundle bundle, String[] permissions, final String errorDesText) {
        RxManager.requestPermission(this, permissions, new IListener.IPermissionListener() {
            @Override
            public void call(boolean granted) {
                if (granted) {
                    //开始定位
                    startActivity(clazz, bundle);
                } else {
                    if (!TextUtils.isEmpty(errorDesText)) {
                        DialogManager.showPermissDialog((BaseActivity) mContext, errorDesText);
                    }
                }
            }
        });
    }

    public void startCheckLoginActivity(Intent intent, Class<?> clazz) {

    }

    public void startActivityAndFinish(Class<?> clazz) {
        startActivityAndFinish(clazz, null);
    }

    public void startActivityAndFinish(Class<?> clazz, Bundle bundle) {
        startActivity(clazz, bundle);
        this.finish();
    }

    public void startActivity(Context context, Intent intent, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((BaseActivity) context, view, "transition_animation_news_photos");
            startActivity(intent, options.toBundle());
        } else {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(context, intent, optionsCompat.toBundle());
        }
    }

    public void startActivityForResult(Class<?> cla, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cla);
        startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Class<?> cla, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cla);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void logE(String msg) {
        LogUtil.e(TAG, msg);
    }
}
