package com.wei.commonlibrary.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.wei.commonlibrary.app.AppManager;
import com.wei.commonlibrary.mvp.presenter.CommPresenter;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ${wei} on 2017/3/25.
 */

public abstract class CommonActivity<P extends CommPresenter> extends AppCompatActivity{

    public String TAG = "test_" + getClass().getSimpleName();

    protected P presenter;

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private Unbinder butterBind;

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
        setContentView(layoutView());
        AppManager.getAppManager().addActivity(this);
        //注解
        butterBind = ButterKnife.bind(this);
        presenter = createpresenter();
        initWidget();

    }

    /**
     * 子类返回布局
     */
    public abstract int layoutView();

    /**
     * 返回子类需要的prsenter
     */
    protected abstract P createpresenter();

    /**
     * 初始化布局id
     */
    public abstract void initWidget();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        if(butterBind != null){
            butterBind.unbind();
        }
        if (presenter != null) {
            presenter.onDestory();
        }
    }

    @Override
    public void finish() {
        super.finish();
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

    public void startActivityAndFinish(Class<?> clazz) {
        startActivity(clazz, null);
        this.finish();
    }

    public void startActivityAndFinish(Class<?> clazz, Bundle bundle) {
        startActivity(clazz, bundle);
        this.finish();
    }


//    public void startActivity(Class<?> clazz, Bundle bundle) {
//        Intent intent = new Intent();
//        intent.putExtras(bundle);
//        intent.setClass(getApplicationContext(), clazz);
//        Intent newIntent = new Intent();
//        intent.putExtra("class", clazz.getName());
//        if (test == 0) {
//            newIntent.setAction("com.wei.login");
//            newIntent.putExtras(intent);
//            startActivity(newIntent);
//        } else {
//            startActivity(intent);
//        }
//    }

    public int test = 0;

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = this.test == 0 ? 1 : 0;
    }
}
