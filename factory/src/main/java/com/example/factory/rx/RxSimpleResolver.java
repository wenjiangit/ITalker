package com.example.factory.rx;

import com.example.commom.factory.data.DataSource;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 *
 * Created by douliu on 2017/8/9.
 */

public abstract class RxSimpleResolver<T> implements Observer<T>,DataSource.Callback<T>{

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onDataLoaded(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onDataNotAvailable(int strRes) {
    }
}
