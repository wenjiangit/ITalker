package com.example.commom.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.example.commom.R;
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
    protected ProgressDialog mProgressDialog;

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
        hideProgressLoading();
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
        } else {
            ProgressDialog dialog = mProgressDialog;
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setMessage(getText(R.string.prompt_loading));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                mProgressDialog = dialog;
            }
            dialog.show();
        }
    }

    /**
     * 隐藏loading
     */
    protected void hideLoading() {
        hideProgressLoading();
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }

    private void hideProgressLoading() {
        ProgressDialog dialog = mProgressDialog;
        if (dialog != null) {
            mProgressDialog = null;
            dialog.dismiss();
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
