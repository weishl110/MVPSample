package com.mvp.app.mvp.presenter;

import com.mvp.app.mvp.model.BaseModel;
import com.mvp.app.mvp.view.IBaseView;
import com.wei.commonlibrary.mvp.presenter.CommPresenter;

/**
 * Created by ${wei} on 2017/9/20.
 */

public abstract class BasePresenter<M extends BaseModel,V extends IBaseView> extends CommPresenter<M,V> {
}
