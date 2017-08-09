package com.example.factory.rx;

import com.example.commom.factory.data.DataSource;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.RspModel;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 *
 * Created by douliu on 2017/8/9.
 */

public class RxResolver<T> implements Observer<RspModel<T>>{

    private DataSource.Callback<T> mCallback;

    public RxResolver(DataSource.Callback<T> callback) {
        mCallback = callback;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(RspModel<T> tRspModel) {
        if (tRspModel.success()) {
            T result = tRspModel.getResult();
            mCallback.onDataLoaded(result);
        } else {
            Factory.decodeRspCode(tRspModel, mCallback);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mCallback.onDataNotAvailable(R.string.data_network_error);
    }

    @Override
    public void onComplete() {

    }
}
