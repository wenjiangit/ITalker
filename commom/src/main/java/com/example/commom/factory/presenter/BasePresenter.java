package com.example.commom.factory.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Presenter的基类
 *
 * @author wenjian
 * @date 2017/6/12
 */

public abstract class BasePresenter<T extends BaseContract.View>
        implements BaseContract.Presenter {

    private T mView;

    public BasePresenter(T view) {
        setView(view);
    }

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * 将presenter与view进行双向绑定
     *
     * @param view View
     */
    @SuppressWarnings("unchecked")
    protected void setView(T view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    /**
     * 给子类使用
     *
     * @return View
     */
    public final T getView() {
        return mView;
    }

    @Override
    public void start() {
        T view = mView;
        if (view != null) {
            view.showLoading();
        }
    }

    protected void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }


    /**
     * 将presenter与view进行解绑
     */
    @SuppressWarnings("unchecked")
    @Override
    public void destroy() {
        T view = mView;
        mView = null;
        mCompositeDisposable.clear();
        if (view != null) {
            view.setPresenter(null);
        }
    }


}
