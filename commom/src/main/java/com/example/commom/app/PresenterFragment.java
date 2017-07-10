package com.example.commom.app;

import android.content.Context;

import com.example.commom.factory.presenter.BaseContract;
import com.example.commom.widget.invention.PlaceHolderView;

/**
 * mvp中fragment作为view的基类
 *
 * Created by wenjian on 2017/6/12.
 */

public abstract class PresenterFragment<P extends BaseContract.Presenter> extends BaseFragment
        implements BaseContract.View<P> {

    protected P mPresenter;
    protected PlaceHolderView mPlaceHolderView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        createPresenter();
    }

    @SuppressWarnings("UnusedReturnValue")
    protected abstract P createPresenter();

    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showError(int strId) {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(strId);
        } else {
            Application.showToast(strId);
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
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.destroy();
    }
}
