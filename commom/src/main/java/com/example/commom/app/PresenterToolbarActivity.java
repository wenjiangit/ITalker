package com.example.commom.app;

import com.example.commom.factory.presenter.BaseContract;
import com.example.commom.widget.invention.PlaceHolderView;

/**
 *
 * <p> BaseContract.Presenter
 * Created by wenjian on 2017/6/21.
 */

public abstract class PresenterToolbarActivity<P extends BaseContract.Presenter> extends ToolbarActivity
        implements BaseContract.View<P> {

    protected P mPresenter;
    protected PlaceHolderView mPlaceHolderView;

    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected abstract P createPresenter();

    @Override
    protected void initBefore() {
        super.initBefore();
        createPresenter();
    }

    @Override
    public void showError(int strId) {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(strId);
        } else {
            Application.showToast(strId);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    /**
     * 隐藏loading
     */
    protected void hideLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }


    /**
     * 设置占位布局视图
     *
     * @param placeHolderView 占位视图
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        mPlaceHolderView = placeHolderView;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
